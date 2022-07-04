package com.hitrosttech.notetaker.di

import android.app.Application
import androidx.room.Room
import com.hitrosttech.notetaker.feature_note.data.data_source.NoteDatabase
import com.hitrosttech.notetaker.feature_note.data.repositoty.NoteRepositoryImpl
import com.hitrosttech.notetaker.feature_note.domain.repository.NoteRepository
import com.hitrosttech.notetaker.feature_note.domain.use_case.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AppModule {
	
	@Provides
	@Singleton
	fun providesNoteDatabase(app: Application): NoteDatabase {
		return Room.databaseBuilder(
			app,
			NoteDatabase::class.java,
			NoteDatabase.DATABASE_NAME
		)
			.build()
	}
	
	@Provides
	@Singleton
	fun providesNoteRepository(db: NoteDatabase): NoteRepository {
		return NoteRepositoryImpl(db.noteDao)
	}
	
	@Provides
	@Singleton
	fun provideNotesUseCases(repository: NoteRepository): NoteUseCases {
		return NoteUseCases(
			getNotesUseCase = GetNotesUseCase(repository),
			deleteNoteUseCase = DeleteNoteUseCase(repository),
			addNotesUseCase = AddNotesUseCase(repository),
			getNoteUseCase = GetNoteUseCase(repository)
		)
	}
}