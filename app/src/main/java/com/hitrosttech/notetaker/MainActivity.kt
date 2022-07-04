package com.hitrosttech.notetaker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.hitrosttech.notetaker.feature_note.presentation.add_edit_notes.AddEditNotesScreen
import com.hitrosttech.notetaker.feature_note.presentation.notes.NotesScreen
import com.hitrosttech.notetaker.feature_note.presentation.ui.theme.NoteTakerTheme
import com.hitrosttech.notetaker.feature_note.presentation.util.Screen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContent {
			NoteTakerTheme {
				// A surface container using the 'background' color from the theme
				Surface(
					modifier = Modifier.fillMaxSize(),
					color = MaterialTheme.colors.background
				) {
					val navController = rememberNavController()
					NavHost(
						navController = navController,
						startDestination = Screen.NotesScreen.route
					) {
						composable(route = Screen.NotesScreen.route) {
							NotesScreen(navController = navController)
						}
						composable(
							route = Screen.AddEditNotesScreen.route + "?noteId={noteId}&noteColor={noteColor}",
							arguments = listOf(
								navArgument(name = "noteId") {
									type = NavType.IntType
									defaultValue = -1
								},
								navArgument(name = "noteColor") {
									type = NavType.IntType
									defaultValue = -1
								}
							)
						) {
							val color = it.arguments?.getInt("noteColor") ?: -1
							AddEditNotesScreen(navController = navController, noteColor = color)
						}
					}
				}
			}
		}
	}
}
