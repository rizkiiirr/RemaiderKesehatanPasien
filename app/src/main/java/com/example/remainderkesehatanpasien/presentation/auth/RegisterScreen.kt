package com.example.remainderkesehatanpasien.presentation.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
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
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.remainderkesehatanpasien.component.CustomTextField
import com.example.remainderkesehatanpasien.ui.theme.RemainderKesehatanPasienTheme
import kotlinx.coroutines.flow.collectLatest // Import collectLatest

@Composable
fun RegisterScreen(
    navController: NavController,
    onRegisterButtonClicked: () -> Unit,
    onTextHereClicked: () -> Unit,
    viewModel: AuthViewModel = hiltViewModel()
) {
    val fullNameState = viewModel.registerFullName
    val usernameState = viewModel.registerUsername
    val emailState = viewModel.registerEmail
    val passwordState = viewModel.registerPassword
    var passwordVisibility by remember { mutableStateOf(false) } // State untuk visibility password

    val context = LocalContext.current

    LaunchedEffect(key1 = true) {
        viewModel.uiEvent.collectLatest { event ->
            when (event) {
                is AuthUiEvent.ShowSnackbar -> {
                    android.widget.Toast.makeText(context, event.message, android.widget.Toast.LENGTH_SHORT).show()
                }
                AuthUiEvent.RegistrationSuccess -> {
                    // Setelah registrasi sukses, kembali ke halaman Login.
                    navController.popBackStack()
                }
                else -> Unit // Handle event lain jika ada
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .fillMaxWidth()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
            .background(MaterialTheme.colorScheme.background)
    ) {
        Text(
            text = "Daftar",
            style = MaterialTheme.typography.headlineMedium.copy(
                fontWeight = FontWeight.Bold
            ),
            modifier = Modifier
                .padding(horizontal = 8.dp, vertical = 16.dp),
            color = MaterialTheme.colorScheme.onBackground
        )

        Column(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CustomTextField(
                value = fullNameState.text,
                onValueChange = { viewModel.onRegisterEvent(RegisterFormEvent.EnteredFullName(it)) },
                placeholder = "Nama Lengkap",
                icon = Icons.Default.AccountCircle,
                contentDesc = "Name"
            )
            usernameState.error?.let { error ->
                Text(
                    text = error,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(horizontal = 8.dp)
                )
            }

            CustomTextField(
                value = usernameState.text,
                onValueChange = { viewModel.onRegisterEvent(RegisterFormEvent.EnteredUsername(it)) },
                placeholder = "Nama Akun",
                icon = Icons.Default.AccountCircle,
                contentDesc = "Username"
            )
            usernameState.error?.let { error ->
                Text(
                    text = error,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(horizontal = 8.dp)
                )
            }


            CustomTextField(
                value = emailState.text,
                onValueChange = { viewModel.onRegisterEvent(RegisterFormEvent.EnteredEmail(it)) },
                placeholder = "Email",
                icon = Icons.Default.Email,
                contentDesc = "Email"
            )
            emailState.error?.let { error ->
                Text(
                    text = error,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(horizontal = 8.dp)
                )
            }

            OutlinedTextField(
                value = passwordState.text,
                onValueChange = { viewModel.onRegisterEvent(RegisterFormEvent.EnteredPassword(it)) },
                placeholder = {Text("Password", color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f))},
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Lock,
                        contentDescription = "Password",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
                    )
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password), // <--- Tipe keyboard untuk password
                visualTransformation = if (passwordVisibility) VisualTransformation.None else PasswordVisualTransformation(), // <--- Atur visibilitas
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp, vertical = 16.dp)
                    .border(
                        width = 2.dp,
                        color = MaterialTheme.colorScheme.outline,
                        shape = RoundedCornerShape(12.dp)
                    ),
                shape = RoundedCornerShape(12.dp),
                colors = TextFieldDefaults.colors( // Sesuaikan warna TextField
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    disabledContainerColor = Color.Transparent,
                    focusedTextColor = MaterialTheme.colorScheme.onSurface,
                    unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
                    cursorColor = MaterialTheme.colorScheme.primary, // Warna kursor
                    focusedIndicatorColor = MaterialTheme.colorScheme.primary, // Warna indikator saat fokus
                    unfocusedIndicatorColor = MaterialTheme.colorScheme.outline // Warna indikator saat tidak fokus
                ),
                trailingIcon = {
                    Icon(
                        imageVector = if (passwordVisibility) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                        contentDescription = "Visibility is wheter is true",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f),
                        modifier = Modifier.clickable { passwordVisibility = !passwordVisibility } // Toggle visibility
                    )
                }
            )
            passwordState.error?.let { error ->
                Text(
                    text = error,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(horizontal = 8.dp)
                )
            }

            Button(
                onClick = {
                    viewModel.onRegisterEvent(RegisterFormEvent.Register) // Panggil event register ke ViewModel
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp, vertical = 16.dp)
                    .background(
                        MaterialTheme.colorScheme.primary,
                        shape = RoundedCornerShape(12.dp)
                    ),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Transparent
                )

            ) {
                Text(
                    text = "Daftar",
                    fontStyle = FontStyle.Normal,
                    fontWeight = FontWeight.Bold,
                    fontSize = 24.sp,
                    modifier = Modifier
                        .padding(8.dp))
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp),
                horizontalArrangement = Arrangement.Center
            ){
                Text(
                    text = "Punya akun? Masuk",
                    fontStyle = FontStyle.Normal,
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f)
                )
                Text(
                    text = " "
                    ,color = MaterialTheme.colorScheme.onBackground
                )
                Text(
                    text = "di sini",
                    color = MaterialTheme.colorScheme.primary,
                    fontStyle = FontStyle.Normal,
                    fontWeight = FontWeight.SemiBold,
                    textDecoration = TextDecoration.Underline,
                    modifier = Modifier
                        .clickable {
                            onTextHereClicked()
                        }
                )
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun RegisterPreview() {
    RemainderKesehatanPasienTheme(darkTheme = false) {
        RegisterScreen(
            navController = rememberNavController(), // Tambahkan ini untuk Preview
            onTextHereClicked = {},
            onRegisterButtonClicked = {},
        )
    }
}
