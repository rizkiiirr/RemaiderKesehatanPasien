package com.example.remainderkesehatanpasien.presentation.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navOptions // <-- IMPORT INI
import com.example.remainderkesehatanpasien.presentation.auth.AuthViewModel
import com.example.remainderkesehatanpasien.presentation.auth.ChangePasswordScreen
import com.example.remainderkesehatanpasien.presentation.auth.LoginScreen
import com.example.remainderkesehatanpasien.presentation.auth.RegisterScreen
import com.example.remainderkesehatanpasien.presentation.checkList.CheckListScreen
import com.example.remainderkesehatanpasien.presentation.dashboard.DashboardScreen
import com.example.remainderkesehatanpasien.presentation.news.NewsScreen
import com.example.remainderkesehatanpasien.presentation.note.AddNoteScreen
import com.example.remainderkesehatanpasien.presentation.note.ListNoteScreen
import com.example.remainderkesehatanpasien.presentation.profile.ProfileScreen
import com.example.remainderkesehatanpasien.presentation.reminder.AddEditReminderScreen
import com.example.remainderkesehatanpasien.presentation.reminder.ReminderListScreen
import com.example.remainderkesehatanpasien.presentation.search.SearchScreen
import com.example.remainderkesehatanpasien.presentation.settings.SettingsScreen

// Tambahkan rute baru untuk NewsScreen
enum class Route(val path: String) {
    Login("login"),
    Register("register"),
    Dashboard("dashboard"),
    Profile("profile"),
    Settings("settings"),
    Search("search"),
    ListNote("list_note"),
    NoteDetail("note_detail?noteId={noteId}"),
    News("news_screen"),
    ChangePassword("change_password"),
    CheckList("checklist"),
    ReminderList("reminderList/{category}"),
    AddEditReminder("add_edit_reminder/{category}?reminderId={reminderId}")
}

@Composable
fun NavigationApp(
    navController: NavHostController,
    darkMode: Boolean,
    onToggleDarkMode: (Boolean) -> Unit
){
    val authViewModel: AuthViewModel = hiltViewModel()

    Scaffold { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Route.Login.path,
            modifier = Modifier.padding(innerPadding)
        ){
            composable(Route.Login.path) {
                LoginScreen(
                    onLoginClicked = {
                        navController.navigate(
                            Route.Dashboard.path,
                            navOptions { // <-- GUNAKAN navOptions BUILDER INI
                                popUpTo(Route.Login.path) { inclusive = true }
                            }
                        )
                    },
                    onTextHereClicked = { navController.navigate(Route.Register.path) }
                )
            }
            composable(Route.Register.path) {
                RegisterScreen(
                    navController = navController,
                    onRegisterButtonClicked = { /* Perilaku navigasi ditangani di dalam RegisterScreen */ },
                    onTextHereClicked = { navController.navigate(Route.Login.path) }
                )
            }
            composable(Route.Dashboard.path) {
                AddEditReminderScreen(navController = navController)
                DashboardScreen(
                    navController = navController,
                    onHomeClicked = {},
                    onProfileClicked = { navController.navigate(Route.Profile.path) },
                    onSettingsClicked = { navController.navigate(Route.Settings.path) },
                    onSearchClicked = { navController.navigate(Route.Search.path) },
                    onListNoteClicked = { navController.navigate(Route.ListNote.path) },
                    onNewsMoreClicked = { navController.navigate(Route.News.path) },
                    onCheckListClicked = { navController.navigate(Route.CheckList.path) },
                    onDrinkMedicineClicked = { navController.navigate("reminderList/OBAT") },
                    onConsultationClicked = { navController.navigate("reminderList/KONSULTASI") },
                )
            }
            composable(Route.Profile.path) {
                ProfileScreen(onHomeClicked = { navController.popBackStack() })
            }
            composable(Route.Settings.path) {
                SettingsScreen(
                    onProfileClicked = { navController.navigate(Route.Profile.path) },
                    onHomeClicked = { navController.popBackStack() },
                    darkMode = darkMode,
                    onToggleDarkMode = onToggleDarkMode,
                    onLoginSecurityClicked = { navController.navigate(Route.ChangePassword.path) },
                    onNotificationsEnabled = { isEnabled -> /* TODO: Implement actual notification toggle logic */ },
                    onLogoutClicked = {
                        authViewModel.logoutUser()
                        navController.navigate(
                            Route.Login.path,
                            navOptions { // <-- GUNAKAN navOptions BUILDER INI
                                popUpTo(Route.Dashboard.path) {
                                    inclusive = true
                                }
                            }
                        )
                    }
                )
            }
            composable(Route.Search.path) {
                SearchScreen(onNavController = navController)
            }

            composable(
                route = Route.NoteDetail.path,
                arguments = listOf(
                    navArgument("noteId") {
                        type = NavType.IntType
                        defaultValue = 0
                    }
                )
            ) {
                AddNoteScreen(navController = navController)
            }

            composable(Route.ListNote.path) {
                ListNoteScreen(
                    navController = navController,
                    onAddNoteClicked = {
                        navController.navigate(Route.NoteDetail.path.replace("{noteId}", "0"))
                    },
                    onNoteClicked = { noteId ->
                        navController.navigate(Route.NoteDetail.path.replace("{noteId}", noteId.toString()))
                    }
                )
            }

            composable(Route.News.path) {
                NewsScreen(navController = navController)
            }

            composable(Route.ChangePassword.path) { // <-- PERBARUI INI
                ChangePasswordScreen(navController = navController)
            }

            composable(Route.CheckList.path) {
                CheckListScreen(navController = navController) // <-- TAMBAHKAN INI
            }

            composable(
                route = Route.AddEditReminder.path,
                arguments = listOf(
                    navArgument("category") { type = NavType.StringType },
                    navArgument("reminderId") { type = NavType.IntType; defaultValue = 0 }
                )
            ) {
                AddEditReminderScreen(navController = navController)
            }

            composable(
                route = Route.ReminderList.path,
                arguments = listOf(navArgument("category") { type = NavType.StringType })
            ) {
                // Pemanggilan menjadi sangat sederhana
                ReminderListScreen(navController = navController)
            }
        }
    }
}