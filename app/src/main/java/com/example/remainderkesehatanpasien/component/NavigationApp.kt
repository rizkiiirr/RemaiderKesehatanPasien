package com.example.remainderkesehatanpasien.component

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.remainderkesehatanpasien.screen.AddNoteScreen
import com.example.remainderkesehatanpasien.screen.DashboardScreen
import com.example.remainderkesehatanpasien.screen.ListNoteScreen
import com.example.remainderkesehatanpasien.screen.LoginScreen
import com.example.remainderkesehatanpasien.screen.NoteScreen
import com.example.remainderkesehatanpasien.screen.ProfileScreen
import com.example.remainderkesehatanpasien.screen.RegisterScreen
import com.example.remainderkesehatanpasien.screen.SearchScreen
import com.example.remainderkesehatanpasien.screen.SettingsScreen

enum class Screen {
    Login, Register, Dashboard, Profile, Settings, Search, Note, AddNote, ListNote
}

@Composable
fun NavigationApp(
    navController: NavHostController,
    darkMode: Boolean,
    onToggleDarkMode: () -> Unit
){
    Scaffold { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Login.name,
            modifier = Modifier.padding(innerPadding)
        ){
            composable(Screen.Login.name) {
                LoginScreen(
                    onLoginClicked = {
                        navController.navigate(Screen.Dashboard.name){
                            popUpTo(Screen.Dashboard.name){
                                inclusive = true
                            }
                        }
                    },
                    onTextHereClicked = {
                        navController.navigate(Screen.Register.name){
                            popUpTo(Screen.Register.name){
                                inclusive = true
                            }
                        }
                    },
                    onBackButtonClicked = {
                        navController.navigate(Screen.Register.name){
                            popUpTo(Screen.Register.name){
                                inclusive = true
                            }
                        }
                    }
                )
            }
            composable(Screen.Register.name) {
                RegisterScreen(
                    onRegisterButtonClicked = {},
                    onTextHereClicked = {
                        navController.navigate(Screen.Login.name){
                            popUpTo(Screen.Login.name){
                                inclusive = true
                            }
                        }
                    },
                    onBackButtonClicked = {
                        navController.navigate(Screen.Login.name){
                            popUpTo(Screen.Login.name){
                                inclusive = true
                            }
                        }
                    }
                )
            }
            composable(Screen.Dashboard.name) {
                DashboardScreen(
                    navController = navController,
                    darkMode = darkMode,
                    onToggleDarkMode = onToggleDarkMode,
                    onHomeClicked = {},
                    onProfileClicked = {
                        navController.navigate(Screen.Profile.name){
                            popUpTo(Screen.Profile.name){
                                inclusive = true
                            }
                        }
                    },
                    onSettingsClicked = {
                        navController.navigate(Screen.Settings.name){
                            popUpTo(Screen.Settings.name){
                                inclusive = true
                            }
                        }
                    },
                    onSearchClicked = {
                        navController.navigate(Screen.Search.name){
                            popUpTo(Screen.Search.name){
                                inclusive = true
                            }
                        }
                    },
                    onListNoteClicked = {
                        navController.navigate(Screen.ListNote.name){
                            popUpTo(Screen.ListNote.name) {
                                inclusive = true
                            }
                        }
                    }
                )
            }
            composable(Screen.Profile.name) {
                ProfileScreen(
                    onHomeClicked = {
                        navController.navigate(Screen.Dashboard.name){
                            popUpTo(Screen.Dashboard.name){
                                inclusive = true
                            }
                        }
                    }
                )
            }
            composable(Screen.Settings.name) {
                SettingsScreen(
                    onProfileClicked = {
                        navController.navigate(Screen.Profile.name){
                            popUpTo(Screen.Profile.name){
                                inclusive = true
                            }
                        }
                    },
                    onHomeClicked = {
                        navController.navigate(Screen.Dashboard.name){
                            popUpTo(Screen.Dashboard.name){
                                inclusive = true
                            }
                        }}
                )
            }
            composable(Screen.Search.name) {
                SearchScreen(
                    onHomeClicked = {
                        navController.navigate(Screen.Dashboard.name) {
                            popUpTo(Screen.Dashboard.name) {
                                inclusive = true
                            }
                        }
                    }
                )
            }
            composable(Screen.Note.name) {
                NoteScreen(
                    onHomeClicked = {
                        navController.navigate(Screen.Dashboard.name) {
                            popUpTo(Screen.Dashboard.name) {
                                inclusive = true
                            }
                        }
                    },
                    onListNoteClicked = {
                        navController.navigate(Screen.ListNote.name) {
                            popUpTo(Screen.ListNote.name) {
                                inclusive = true
                            }
                        }
                    }
                )
            }
            composable(Screen.AddNote.name) {
                AddNoteScreen(
                    onListNoteClicked = {
                        navController.navigate(Screen.ListNote.name) {
                            popUpTo(Screen.ListNote.name) {
                                inclusive = true
                            }
                        }
                    }
                )
            }
            composable(Screen.ListNote.name) {
                ListNoteScreen(
                    onHomeClicked = {
                        navController.navigate(Screen.Dashboard.name) {
                            popUpTo(Screen.Dashboard.name) {
                                inclusive = true
                            }
                        }
                    },
                    onAddNoteClicked = {
                        navController.navigate(Screen.AddNote.name) {
                            popUpTo(Screen.AddNote.name) {
                                inclusive = true
                            }
                        }
                    },
                    onNoteClicked = {
                        navController.navigate(Screen.Note.name) {
                            popUpTo(Screen.Note.name) {
                                inclusive = true
                            }
                        }
                    }
                )
            }
        }
    }
}