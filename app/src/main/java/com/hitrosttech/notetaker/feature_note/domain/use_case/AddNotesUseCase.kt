package com.hitrosttech.notetaker.feature_note.domain.use_case

import com.hitrosttech.notetaker.feature_note.domain.model.InvalidNoteException
import com.hitrosttech.notetaker.feature_note.domain.model.Note
import com.hitrosttech.notetaker.feature_note.domain.repository.NoteRepository
import kotlin.jvm.Throws

class AddNotesUseCase(
	private val repository: NoteRepository
) {
	
	@Throws(InvalidNoteException::class)
	suspend operator fun invoke(note: Note) {
		if (note.title.isBlank()) {
			throw InvalidNoteException("Title of the note can't be empty")
		}
		if (note.body.isBlank()) {
			throw InvalidNoteException("Body can't be empty")
		}
		repository.insertNote(note)
	}
}