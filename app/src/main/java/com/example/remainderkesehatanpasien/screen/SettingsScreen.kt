package com.example.remainderkesehatanpasien.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import com.example.remainderkesehatanpasien.R
import com.example.remainderkesehatanpasien.ui.theme.RemainderKesehatanPasienTheme

@Composable
fun SettingsScreen(
    onProfileClicked: () -> Unit,
    onHomeClicked: () -> Unit,
){
    Column (
        Modifier
            .fillMaxSize()
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
            .background(MaterialTheme.colorScheme.background),
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        ConstraintLayout {
            val (border) = createRefs()
            Box (modifier = Modifier
                .fillMaxWidth()
                .height(70.dp)
                .constrainAs(border){
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                }.background(Color.Red),

                )
            Row(modifier = Modifier
                .padding(top = 10.dp, start = 10.dp, end = 24.dp)
                .fillMaxWidth()
            ){
                Column(modifier = Modifier
                    .height(50.dp)
                    .padding(start = 8.dp)
                    .weight(0.1f),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.Start
                ){
                    Icon(
                        imageVector = Icons.Default.ArrowBackIosNew,
                        contentDescription = null,
                        modifier = Modifier
                            .size(38.dp)
                            .padding(end = 6.dp)
                            .clickable {
                                onHomeClicked()
                            }
                    )
                }

                Column(modifier = Modifier
                    .height(50.dp)
                    .padding(start = 8.dp)
                    .weight(0.6f),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.Start
                ){
                    Text(
                        text = "Pengaturan",
                        color = Color.White,
                        fontSize = 25.sp,
                        fontStyle = FontStyle.Normal,
                        fontWeight = FontWeight.Bold
                    )
                }

                Column(modifier = Modifier
                    .height(50.dp)
                    .padding(start = 8.dp)
                    .weight(0.15f),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.Start
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.account),
                        contentDescription = null,
                        modifier = Modifier
                            .width(50.dp)
                            .height(50.dp)
                            .clickable {
                                onProfileClicked()
                            }
                    )
                }
            }
        }

        var searchText by rememberSaveable{
            mutableStateOf("")
        }
        TextField(
            value = searchText,
            onValueChange = {searchText = it},
            label = {Text(text = "Pencarian...")},
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = null,
                    modifier = Modifier
                        .size(38.dp)
                        .padding(end = 6.dp)
                )
            },
            shape = RoundedCornerShape(12.dp),
            colors = TextFieldDefaults.colors(
                unfocusedContainerColor = Color.White,
                focusedContainerColor = Color.White,
                focusedPlaceholderColor = Color.White
            ),
            modifier = Modifier
                .fillMaxWidth()
                .height(70.dp)
                .padding(top = 24.dp, end = 24.dp, start = 24.dp)
                .shadow(3.dp, shape = RoundedCornerShape(50.dp))
                .background(Color.White, CircleShape)
        )

        ConstraintLayout(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 24.dp, end = 24.dp, start = 24.dp)
                .shadow(5.dp, shape = RoundedCornerShape(10.dp))
                .height(150.dp)
                .background(Color.Red)
        ) {
                Column {
                    Text(
                        text = "Lihat Profil Anda",
                        color = Color.White,
                        fontSize = 16.sp,
                        fontStyle = FontStyle.Normal,
                        fontWeight = FontWeight.Bold
                    )
                }
                    Image(
                        painter = painterResource(id = R.drawable.account_box),
                        contentDescription = null,
                        modifier = Modifier
                            .width(100.dp)
                            .padding(top = 20.dp)
                            .height(100.dp)
                            .clickable {
                                onProfileClicked()
                            }
                    )
        }
        ConstraintLayout() {
            Column(
                modifier = Modifier
                    .padding(horizontal = 14.dp)
                    .padding(top = 10.dp)
            ) {
                Text(
                    text = "Umum",
                    color = Color.Black,
                    fontSize = 20.sp,
                    fontStyle = FontStyle.Normal,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .padding(vertical = 8.dp)
                )
            }
        }

        SettingsItem(icon = Icons.Default.PlayArrow, "Pengaturan 1", onClick = {})
        SettingsItem(icon = Icons.Default.PlayArrow, "Pengaturan 2", onClick = {})
        SettingsItem(icon = Icons.Default.PlayArrow, "Pengaturan 3", onClick = {})
        SettingsItem(icon = Icons.Default.PlayArrow, "Pengaturan 4", onClick = {})
        SettingsItem(icon = Icons.Default.PlayArrow, "Pengaturan 5", onClick = {})

    }
}

@Composable
fun SettingsItem(
    icon: ImageVector,
    mainText: String,
    onClick:()->Unit
){
    Card(
        onClick = {
            onClick()
        },
        modifier = Modifier
            .padding(bottom = 8.dp)
            .fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.Red),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(vertical = 10.dp, horizontal = 14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ){
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(34.dp)
                        .clip(shape = MaterialTheme.shapes.medium)
                        .background(color = Color.Gray)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.user),
                        contentDescription = "",
                        tint = Color.Black,
                        modifier = Modifier
                            .padding(8.dp)
                    )
                }

                Spacer(modifier = Modifier
                    .width(14.dp)
                )

                Column{
                    Text(
                        text = mainText,
                        fontStyle = FontStyle.Normal,
                        color = Color.White,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier
                    .size(38.dp)
                    .padding(end = 6.dp)
            )
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun SettingsPreview(){
    RemainderKesehatanPasienTheme(darkTheme = false) {
        SettingsScreen(
            onProfileClicked = {},
            onHomeClicked = {}
        )
    }
}