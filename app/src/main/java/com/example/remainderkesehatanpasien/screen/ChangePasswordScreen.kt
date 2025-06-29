package com.example.remainderkesehatanpasien.screen

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.remainderkesehatanpasien.presentation.auth.ChangePasswordViewModel
import com.example.remainderkesehatanpasien.presentation.auth.ChangePasswordFormEvent
import com.example.remainderkesehatanpasien.presentation.auth.ChangePasswordUiEvent
import com.example.remainderkesehatanpasien.ui.theme.RemainderKesehatanPasienTheme
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChangePasswordScreen(
    navController: NavController,
    viewModel: ChangePasswordViewModel = hiltViewModel()
) {
    val currentPasswordState = viewModel.currentPassword
    val newPasswordState = viewModel.newPassword
    val confirmNewPasswordState = viewModel.confirmNewPassword

    var currentPasswordVisibility by remember { mutableStateOf(false) }
    var newPasswordVisibility by remember { mutableStateOf(false) }
    var confirmNewPasswordVisibility by remember { mutableStateOf(false) } // Ini sudah benar dideklarasikan di sini

    val context = LocalContext.current

    LaunchedEffect(key1 = true) {
        viewModel.uiEvent.collectLatest { event ->
            when (event) {
                is ChangePasswordUiEvent.ShowSnackbar -> {
                    Toast.makeText(context, event.message, Toast.LENGTH_LONG).show()
                }
                ChangePasswordUiEvent.ChangePasswordSuccess -> {
                    Toast.makeText(context, "Password berhasil diubah!", Toast.LENGTH_SHORT).show()
                    navController.popBackStack()
                }
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Login dan Keamanan") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Kembali")
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
                .background(MaterialTheme.colorScheme.background),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "Ubah Password Anda",
                style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
                modifier = Modifier.padding(bottom = 16.dp)
            )

            // Password Saat Ini
            OutlinedTextField(
                value = currentPasswordState.text,
                onValueChange = { viewModel.onEvent(ChangePasswordFormEvent.EnteredCurrentPassword(it)) },
                label = { Text("Password Saat Ini") },
                leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                visualTransformation = if (currentPasswordVisibility) VisualTransformation.None else PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth(),
                isError = currentPasswordState.error != null,
                trailingIcon = {
                    IconButton(onClick = { currentPasswordVisibility = !currentPasswordVisibility }) {
                        Icon(
                            imageVector = if (currentPasswordVisibility) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                            contentDescription = "Toggle password visibility"
                        )
                    }
                }
            )
            currentPasswordState.error?.let { error ->
                Text(text = error, color = MaterialTheme.colorScheme.error, fontSize = 12.sp, modifier = Modifier.fillMaxWidth().padding(start = 16.dp))
            }

            // Password Baru
            OutlinedTextField(
                value = newPasswordState.text,
                onValueChange = { viewModel.onEvent(ChangePasswordFormEvent.EnteredNewPassword(it)) },
                label = { Text("Password Baru") },
                leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                visualTransformation = if (newPasswordVisibility) VisualTransformation.None else PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth(),
                isError = newPasswordState.error != null,
                trailingIcon = {
                    IconButton(onClick = { newPasswordVisibility = !newPasswordVisibility }) {
                        Icon(
                            imageVector = if (newPasswordVisibility) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                            contentDescription = "Toggle password visibility"
                        )
                    }
                }
            )
            newPasswordState.error?.let { error ->
                Text(text = error, color = MaterialTheme.colorScheme.error, fontSize = 12.sp, modifier = Modifier.fillMaxWidth().padding(start = 16.dp))
            }

            // Konfirmasi Password Baru
            OutlinedTextField(
                value = confirmNewPasswordState.text,
                onValueChange = { viewModel.onEvent(ChangePasswordFormEvent.EnteredConfirmNewPassword(it)) },
                label = { Text("Konfirmasi Password Baru") },
                leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                visualTransformation = if (confirmNewPasswordVisibility) VisualTransformation.None else PasswordVisualTransformation(), // <-- Ini baris 187 yang saya perbaiki
                modifier = Modifier.fillMaxWidth(),
                isError = confirmNewPasswordState.error != null,
                trailingIcon = {
                    IconButton(onClick = { confirmNewPasswordVisibility = !confirmNewPasswordVisibility }) {
                        Icon(
                            imageVector = if (confirmNewPasswordVisibility) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                            contentDescription = "Toggle password visibility"
                        )
                    }
                }
            )
            confirmNewPasswordState.error?.let { error ->
                Text(text = error, color = MaterialTheme.colorScheme.error, fontSize = 12.sp, modifier = Modifier.fillMaxWidth().padding(start = 16.dp))
            }

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = { viewModel.onEvent(ChangePasswordFormEvent.SubmitChange) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Selesai", fontSize = 18.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ChangePasswordScreenPreview() {
    RemainderKesehatanPasienTheme(darkTheme = false) {
        ChangePasswordScreen(
            navController = rememberNavController()
        )
    }
}
