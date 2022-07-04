package com.hitrosttech.notetaker.feature_note.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.hitrosttech.notetaker.feature_note.presentation.ui.theme.*

@Entity
data class Note(
	val title: String,
	val body: String,
	val timestamp: Long,
	val color: Int,
	@PrimaryKey val id: Int? = null
) {
	companion object {
		val noteColors = listOf(Teal, RedPink, ShadowGreen, Glacier, Gold, LightPurple)
	}
}

class InvalidNoteException(message: String): Exception(message)