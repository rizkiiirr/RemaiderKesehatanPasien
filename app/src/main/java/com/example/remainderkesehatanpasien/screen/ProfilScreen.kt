package com.example.remainderkesehatanpasien.screen

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import com.example.remainderkesehatanpasien.R
import com.example.remainderkesehatanpasien.ui.theme.RemainderKesehatanPasienTheme

@Composable
fun ProfileScreen(
    onHomeClicked: () -> Unit
){
    val notification = rememberSaveable { mutableStateOf("") }
    if (notification.value.isNotEmpty()){
        Toast.makeText(LocalContext.current, notification.value, Toast.LENGTH_LONG).show()
        notification.value = ""
    }

    var name by rememberSaveable { mutableStateOf("Nama Anda") }
    var username by rememberSaveable { mutableStateOf("Nama Akun Anda") }
    var bio by rememberSaveable { mutableStateOf("Bio Anda") }

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
                    .clickable { notification.value = "Dibatalkan" }
            )
            Text(
                text = "Simpan",
                fontStyle = FontStyle.Normal,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier
                    .clickable { notification.value = "Profile telah berubah"}
                    .clickable {
                        onHomeClicked()
                    }
            )
        }

        ProfileImage()

        Row(modifier = Modifier
            .fillMaxWidth()
            .padding(start = 4.dp, end = 4.dp),
        verticalAlignment = Alignment.CenterVertically
            ){
                Text(
                    text = "Nama",
                    fontStyle = FontStyle.Normal,
                    modifier = Modifier
                        .width(100.dp)
                )
            TextField(
                value = name,
                onValueChange = {name = it},
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    focusedTextColor = Color.Black,
                    unfocusedTextColor = Color.DarkGray
                )
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
                value = username,
                onValueChange = {username= it},
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    focusedTextColor = Color.Black,
                    unfocusedTextColor = Color.DarkGray
                )
            )
        }

        Row(modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        verticalAlignment = Alignment.Top
        ){
            Text(
                text = "Bio",
                fontStyle = FontStyle.Normal,
                modifier = Modifier
                    .width(100.dp)
                    .padding(top = 8.dp)
            )
            TextField(
                value = bio,
                onValueChange = {bio = it},
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    focusedTextColor = Color.Black,
                    unfocusedTextColor = Color.DarkGray
                ),
                singleLine = false,
                modifier = Modifier
                    .height(150.dp)
            )
        }
    }
}

@OptIn(ExperimentalCoilApi::class)
@Composable
fun ProfileImage(){
    val imageUri = rememberSaveable { mutableStateOf("") }
    val painter = rememberImagePainter(
        if (imageUri.value.isEmpty())
            R.drawable.user
        else
            imageUri.value
    )
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ){uri: Uri? ->
        uri?.let {imageUri.value = it.toString()}

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
            Image(painter = painter, contentDescription = null,
                modifier = Modifier
                    .wrapContentSize()
                    .clickable { launcher.launch("image/*")},
                contentScale = ContentScale.Crop
                )
        }
        Text(
            text = "Ganti Gambar Profil"
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