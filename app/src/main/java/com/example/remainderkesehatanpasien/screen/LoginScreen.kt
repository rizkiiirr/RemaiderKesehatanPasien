package com.example.remainderkesehatanpasien.screen

import com.example.remainderkesehatanpasien.R
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.remainderkesehatanpasien.component.CustomTextField
import com.example.remainderkesehatanpasien.presentation.auth.AuthUiEvent
import com.example.remainderkesehatanpasien.presentation.auth.AuthViewModel
import com.example.remainderkesehatanpasien.presentation.auth.LoginFormEvent
import com.example.remainderkesehatanpasien.ui.theme.RemainderKesehatanPasienTheme
import kotlinx.coroutines.flow.collectLatest

@Composable
fun LoginScreen(
    onLoginClicked: () -> Unit,
    onTextHereClicked: () -> Unit,
    viewModel: AuthViewModel = hiltViewModel()
) {

    val emailState = viewModel.loginEmail
    val passwordState = viewModel.loginPassword
    var passwordVisibility by remember { mutableStateOf(false) } // State untuk visibility password

    val context = LocalContext.current

    LaunchedEffect(key1 = true) {
        viewModel.uiEvent.collectLatest { event ->
            when (event) {
                is AuthUiEvent.ShowSnackbar -> {
                    // Gunakan Snackbar atau Toast untuk pesan error
                    // Untuk kesederhanaan, kita akan pakai Toast dulu seperti di AddNoteScreen
                    android.widget.Toast.makeText(context, event.message, android.widget.Toast.LENGTH_SHORT).show()
                }
                AuthUiEvent.LoginSuccess -> {
                    onLoginClicked() // Navigasi ke Dashboard jika login sukses
                }
                else -> Unit // Handle event lain jika ada
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
            .background(MaterialTheme.colorScheme.background)
        ) {
        Text(
            text = "Masuk",
            style = MaterialTheme.typography.headlineMedium.copy(
                fontWeight = FontWeight.Bold
            ),
            modifier = Modifier
                .padding(horizontal = 8.dp, vertical = 16.dp)

        )

        Column(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CustomTextField(
                value = emailState.text,
                onValueChange = { viewModel.onLoginEvent(LoginFormEvent.EnteredEmail(it)) },
                placeholder = "Email",
                icon = Icons.Default.Email,
                contentDesc = "Email"
            )
            emailState.error?.let { error ->
                Text(
                    text = error,
                    color = Color.Red,
                    modifier = Modifier.padding(horizontal = 8.dp)
                )
            }

            OutlinedTextField(
                value = passwordState.text,
                onValueChange = { viewModel.onLoginEvent(LoginFormEvent.EnteredPassword(it)) },
                placeholder = {Text("Password", color = Color.LightGray)},
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Lock,
                        contentDescription = "Password",
                        tint = Color.LightGray
                    )
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                visualTransformation = if (passwordVisibility) VisualTransformation.None else PasswordVisualTransformation(),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp, vertical = 16.dp)
                    .border(
                        width = 2.dp,
                        color = Color.LightGray,
                        shape = RoundedCornerShape(12.dp)
                    ),
                shape = RoundedCornerShape(12.dp),
                trailingIcon = {
                    Icon(
                        imageVector = if (passwordVisibility) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                        contentDescription = "Password visibility",
                        tint = Color.LightGray,
                        modifier = Modifier.clickable { passwordVisibility = !passwordVisibility } // Toggle visibility
                    )
                }
            )
            //password error akan ditampilkan di bawah textfield
            passwordState.error?.let{error->
                Text(
                    text = error,
                    color = Color.Red,
                    modifier = Modifier.padding(horizontal = 8.dp)
                )
            }

            Button(
                onClick = {
                    viewModel.onLoginEvent(LoginFormEvent.Login)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp, vertical = 16.dp)
                    .background(
                        Color.Red,
                        shape = RoundedCornerShape(12.dp)
                    ),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Transparent
                )

            ) {
                Text(
                    text = "Masuk",
                    fontSize = 24.sp,
                    fontStyle = FontStyle.Normal,
                    fontWeight = FontWeight.Bold,
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
                    text = "Tidak punya akun? Daftar",
                    color = Color.LightGray,
                    fontStyle = FontStyle.Normal
                )
                Text(
                    text = " ")
                Text(
                    text = "di sini",
                    color = Color.Red,
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
fun LoginPreview() {
    RemainderKesehatanPasienTheme(darkTheme = false) {
        LoginScreen(
            onLoginClicked = {},
            onTextHereClicked = {}
        )
    }
}