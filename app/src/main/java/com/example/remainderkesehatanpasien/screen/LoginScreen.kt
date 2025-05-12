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
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.remainderkesehatanpasien.component.CustomTextField
import com.example.remainderkesehatanpasien.ui.theme.RemainderKesehatanPasienTheme


@Composable
fun LoginScreen(
    onLoginClicked: () -> Unit,
    onTextHereClicked: () -> Unit,
    onBackButtonClicked: () -> Unit

) {

    var email by remember{ mutableStateOf( "") }
    var password by remember{ mutableStateOf("") }
    var passwordVisibility by remember { mutableStateOf(false) }


    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
            .background(MaterialTheme.colorScheme.background)
        ) {

        Icon(
            imageVector = Icons.Default.ArrowBackIosNew,
            contentDescription = "Back",
            modifier = Modifier
                .padding(horizontal = 8.dp, vertical = 14.dp)
                .clickable {
                    onBackButtonClicked()
                }
        )

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
                value = email,
                onValueChange = {email = it},
                placeholder = "Email",
                icon = Icons.Default.Email,
                contentDesc = "Email"
            )
            if (email.isNotEmpty() && !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                Text(
                    text = "Email tidak valid",
                    color = Color.Red,
                    modifier = Modifier
                        .padding(horizontal = 8.dp)
                )
            }

            OutlinedTextField(
                value = password,
                onValueChange = {password = it},
                placeholder = {Text("Password", color = Color.LightGray)},
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Lock,
                        contentDescription = "Password",
                        tint = Color.LightGray
                    )
                },
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
                        contentDescription = "Visibility is wheter is true",
                        tint = Color.LightGray,
                    )
                }
            )
            if (password.isNotEmpty() && password.length < 6){
                Text(
                    text = "Password minimal 6 karakter",
                    color = Color.Red,
                    modifier = Modifier
                        .padding(horizontal = 8.dp)
                )
            }

            Text(
                text = "Lupa password?",
                color = Color.Red,
                fontStyle = FontStyle.Normal,
                fontWeight = FontWeight.SemiBold,
                textAlign = TextAlign.End,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp)

            )
            Button(
                onClick = {
                    onLoginClicked()
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

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp, vertical = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ){
                Divider(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                )
                Text(
                    text = "atau Masuk dengan",
                    modifier = Modifier
                        .padding(horizontal = 16.dp),
                    color = Color.LightGray,
                    fontStyle = FontStyle.Normal

                    )
                Divider(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp, vertical = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)

            ){
                OutlinedButton(
                    onClick = { },
                    modifier = Modifier
                        .weight(1f),
                    shape = RoundedCornerShape(12.dp),
                    border = BorderStroke(
                        width = 2.dp,
                        color = Color.LightGray
                    )
                ) {
                    Image(painter = painterResource(id = R.drawable.google),
                        contentDescription = "Google",
                        modifier = Modifier.size(50.dp)
                    )
                }
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
            onTextHereClicked = {},
            onBackButtonClicked = {}
        )
    }
}