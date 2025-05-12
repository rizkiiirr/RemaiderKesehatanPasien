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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import com.example.remainderkesehatanpasien.R
import com.example.remainderkesehatanpasien.data.Menu
import com.example.remainderkesehatanpasien.ui.theme.RemainderKesehatanPasienTheme

@Composable
fun SearchScreen(
    onHomeClicked: () -> Unit

){
    Scaffold{innerPadding ->
        Column(
            Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.background),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            ConstraintLayout {
                val (border) = createRefs()
                Box (modifier = Modifier
                    .fillMaxWidth()
                    .height(70.dp)
                    .constrainAs(border){
                        top.linkTo(parent.top)
                        start.linkTo(parent.start)
                    }.background(Color.Red)
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
                            text = "Pencarian",
                            color = Color.White,
                            fontSize = 25.sp,
                            fontStyle = FontStyle.Normal,
                            fontWeight = FontWeight.Bold
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
                        modifier = Modifier.size(38.dp)
                            .padding(end = 6.dp)

                    )
                },
                shape = RoundedCornerShape(12.dp),
                colors = TextFieldDefaults.colors(
                    unfocusedContainerColor = Color.White,
                    focusedContainerColor = Color.White,
                    focusedPlaceholderColor = Color.White
                ),
                modifier = Modifier.fillMaxWidth()
                    .height(70.dp)
                    .padding(top = 24.dp, end = 24.dp, start = 24.dp)
                    .shadow(3.dp, shape = RoundedCornerShape(50.dp))
                    .background(Color.White, CircleShape)
            )

            Text(
                text = "REKAP ANDA",
                color = Color.Black,
                fontSize = 20.sp,
                fontStyle = FontStyle.Normal,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .padding(top = 16.dp)
            )

            val item1 = listOf(
                Menu (
                    title = "Jadwal Pengingat",
                    image = R.drawable.note,
                    color = Color.Red,
                ),
                Menu (
                    title = "Catatan Riwayat",
                    image = R.drawable.note,
                    color = Color.Red,
                ),
                Menu (
                    title = "Catatan Kesehatan",
                    image = R.drawable.note,
                    color = Color.Red,
                ),
                Menu (
                    title = "Sesuaikan judul Anda",
                    image = R.drawable.note,
                    color = Color.Red,
                ),
                Menu (
                    title = "Sesuaikan judul Anda",
                    image = R.drawable.note,
                    color = Color.Red,
                ),
                Menu (
                    title = "Sesuaikan judul Anda",
                    image = R.drawable.note,
                    color = Color.Red,
                ),
                Menu (
                    title = "Sesuaikan judul Anda",
                    image = R.drawable.note,
                    color = Color.Red,
                ),
                Menu (
                    title = "Sesuaikan judul Anda",
                    image = R.drawable.note,
                    color = Color.Red
                )
            )

            LazyColumn(
                modifier = Modifier
                    .padding(8.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)

            ){
                items(item1.size){ index ->
                    val item = item1[index]
                    MenuItem(item)
                }
            }
        }
    }
}

@Composable
fun MenuItem(menu: Menu) {
    Column(
        modifier = Modifier
            .padding(end = 8.dp)
            .fillMaxWidth()
            .background(menu.color, shape = RoundedCornerShape(16.dp))
            .padding(8.dp),
    ) {
        Row(
            modifier = Modifier
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = menu.image),
                contentDescription = null,
                modifier = Modifier
                    .size(40.dp)
            )

            Spacer(modifier = Modifier
                .width(12.dp)
            )

            Text(
                text = menu.title,
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                modifier = Modifier
                    .padding(top = 8.dp)
            )
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun SearchPreview() {
    RemainderKesehatanPasienTheme(darkTheme = false) {
        SearchScreen(
            onHomeClicked = {}
        )
    }
}
