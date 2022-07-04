package com.hitrosttech.notetaker.feature_note.presentation.add_edit_notes

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.toArgb
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hitrosttech.notetaker.feature_note.domain.model.InvalidNoteException
import com.hitrosttech.notetaker.feature_note.domain.model.Note
import com.hitrosttech.notetaker.feature_note.domain.use_case.NoteUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddEditNoteViewModel @Inject constructor(
	private val noteUseCases: NoteUseCases,
	savedStateHandle: SavedStateHandle
) : ViewModel() {
	
	
	private val _noteTitle = mutableStateOf(NoteTextFieldState(hint = "Enter Title..."))
	val noteTitle: State<NoteTextFieldState> = _noteTitle
	
	private val _noteBody = mutableStateOf(NoteTextFieldState(hint = "Share your thoughts..."))
	val noteBody: State<NoteTextFieldState> = _noteBody
	
	private val _noteColor = mutableStateOf(Note.noteColors.random().toArgb())
	val noteColor: MutableState<Int> = _noteColor
	
	private val _eventFlow = MutableSharedFlow<UiEvent>()
	val eventFlow = _eventFlow.asSharedFlow()
	
	private var currentNoteId: Int? = null
	
	init {
		savedStateHandle.get<Int>("noteId")?.let { noteId ->
			if (noteId != -1) {
				viewModelScope.launch {
					noteUseCases.getNoteUseCase(noteId)?.also {
						currentNoteId = it.id
						_noteTitle.value = noteTitle.value.copy(text = it.title, isHintVisible = false)
						_noteBody.value = noteBody.value.copy(text = it.body, isHintVisible = false)
						_noteColor.value = it.color
					}
				}
			}
		}
	}
	
	fun onEvent(event: AddEditNoteEvent) {
		when (event) {
			is AddEditNoteEvent.EnteredTitle -> {
				_noteTitle.value = noteTitle.value.copy(text = event.value)
			}
			is AddEditNoteEvent.ChangeTitleFocus -> {
				_noteTitle.value =
					noteTitle.value.copy(isHintVisible = !event.focusState.isFocused && noteTitle.value.text.isBlank())
			}
			is AddEditNoteEvent.EnteredBody -> {
				_noteBody.value = noteBody.value.copy(text = event.value)
			}
			is AddEditNoteEvent.ChangeBodyFocus -> {
				_noteBody.value =
					noteBody.value.copy(isHintVisible = !event.focusState.isFocused && noteBody.value.text.isBlank())
			}
			is AddEditNoteEvent.ChangeColor -> {
				_noteColor.value = event.color
			}
			is AddEditNoteEvent.SaveNote -> {
				viewModelScope.launch {
					try {
						noteUseCases.addNotesUseCase(
							Note(
								title = noteTitle.value.text,
								body = noteBody.value.text,
								timestamp = System.currentTimeMillis(),
								color = noteColor.value,
								id = currentNoteId
							)
						)
						_eventFlow.emit(UiEvent.SaveNote)
					} catch (e: InvalidNoteException) {
						_eventFlow.emit(
							UiEvent.ShowSnackbar(
								message = e.message ?: "Couldn't save note. Try again"
							)
						)
					}
				}
			}
		}
	}
	
	sealed class UiEvent {
		data class ShowSnackbar(val message: String) : UiEvent()
		object SaveNote : UiEvent()
	}
}