package com.example.remainderkesehatanpasien.presentation.profile

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.example.remainderkesehatanpasien.R
import com.example.remainderkesehatanpasien.ui.theme.RemainderKesehatanPasienTheme
import kotlinx.coroutines.flow.collectLatest

@Composable
fun ProfileScreen(
    onHomeClicked: () -> Unit,
    viewModel: ProfileViewModel = hiltViewModel()
){
    val context = LocalContext.current

    LaunchedEffect(key1 = true) {
        viewModel.uiEvent.collectLatest { event ->
            when (event) {
                is ProfileUiEvent.ShowSnackbar -> {
                    Toast.makeText(context, event.message, Toast.LENGTH_LONG).show()
                }
                ProfileUiEvent.SaveProfileSuccess -> {
                    onHomeClicked() 
                }
            }
        }
    }
    Column(modifier = Modifier
        .verticalScroll(rememberScrollState())
        .padding(8.dp)
        .background(MaterialTheme.colorScheme.background)
        ){
        Row(modifier = Modifier
        .fillMaxWidth()
        .padding(8.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ){
            Text(
                text = "Batalkan",
                fontStyle = FontStyle.Normal,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier
                    .clickable { onHomeClicked() }
            )
            Text(
                text = "Simpan",
                fontStyle = FontStyle.Normal,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier
                    .clickable { viewModel.onEvent(ProfileFormEvent.SaveProfile) }
            )
        }

        ProfileImagePicker(
            imageUrl = viewModel.profileImageUrl, 
            onImageSelected = { uri -> viewModel.onEvent(ProfileFormEvent.UpdateProfileImage(uri.toString())) }
        )

        Row(modifier = Modifier
            .fillMaxWidth()
            .padding(start = 4.dp, end = 4.dp),
        verticalAlignment = Alignment.CenterVertically
            ){
                Text(
                    text = "Nama Lengkap",
                    fontStyle = FontStyle.Normal,
                    modifier = Modifier
                        .width(100.dp)
                )
            TextField(
                value = viewModel.name,
                onValueChange = { viewModel.onEvent(ProfileFormEvent.EnteredName(it)) },
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    focusedTextColor = MaterialTheme.colorScheme.onBackground,
                    unfocusedTextColor = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f)
                ),
                modifier = Modifier.weight(1f)
            )
        }

        Row(modifier = Modifier
            .fillMaxWidth()
            .padding(start = 4.dp, end = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ){
            Text(
                text = "Nama Akun",
                fontStyle = FontStyle.Normal,
                modifier = Modifier
                    .width(100.dp)
            )
            TextField(
                value = viewModel.username,
                onValueChange = { viewModel.onEvent(ProfileFormEvent.EnteredUsername(it)) },                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    focusedTextColor = MaterialTheme.colorScheme.onBackground,
                    unfocusedTextColor = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f)
                ),
                modifier = Modifier.weight(1f)
            )
        }

        Row(modifier = Modifier
            .fillMaxWidth()
            .padding(start = 4.dp, end = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ){
            Text(
                text = "Email",
                fontStyle = FontStyle.Normal,
                modifier = Modifier
                    .width(120.dp)
            )
            TextField(
                value = viewModel.email,
                onValueChange = { viewModel.onEvent(ProfileFormEvent.EnteredEmail(it)) },
                readOnly = true,
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    focusedTextColor = MaterialTheme.colorScheme.onBackground,
                    unfocusedTextColor = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
                    disabledIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent
                ),
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
fun ProfileImagePicker(
    imageUrl: String?, 
    onImageSelected: (Uri) -> Unit 
){
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ){ uri: Uri? ->
        uri?.let { onImageSelected(it) }
    }

    Column(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        Card(shape = CircleShape,
            modifier = Modifier
                .padding(8.dp)
                .size(100.dp)
        ){
            AsyncImage( 
                model = imageUrl ?: R.drawable.user, 
                contentDescription = "Profile Picture",
                modifier = Modifier
                    .fillMaxSize() 
                    .clickable { launcher.launch("image/*")}, 
                contentScale = ContentScale.Crop
            )
        }
        Text(
            text = "Ganti Gambar Profil",
            color = MaterialTheme.colorScheme.primary,
            fontWeight = FontWeight.SemiBold
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ProfilePreview(){
    RemainderKesehatanPasienTheme(darkTheme = false) {
        ProfileScreen(
            onHomeClicked = {}
        )
    }
}