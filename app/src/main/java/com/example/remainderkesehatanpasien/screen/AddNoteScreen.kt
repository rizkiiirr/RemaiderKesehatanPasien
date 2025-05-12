package com.example.remainderkesehatanpasien.screen


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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.RestoreFromTrash
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import com.example.remainderkesehatanpasien.ui.theme.RemainderKesehatanPasienTheme

@Composable
fun AddNoteScreen(
    onListNoteClicked: () -> Unit,
){
    Column (
        Modifier
            .fillMaxSize()
            .fillMaxWidth()
            .background(Color.Red),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ConstraintLayout {
            val (border) = createRefs()
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(70.dp)
                    .constrainAs(border) {
                        top.linkTo(parent.top)
                        start.linkTo(parent.start)
                    }.background(Color.Red)
            )
            Row(
                modifier = Modifier
                    .padding(top = 10.dp, start = 10.dp, end = 24.dp)
                    .fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .height(50.dp)
                        .padding(start = 8.dp)
                        .weight(0.1f),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.Start
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowBackIosNew,
                        contentDescription = null,
                        modifier = Modifier
                            .size(38.dp)
                            .padding(end = 6.dp)
                            .clickable {
                                onListNoteClicked()
                            }

                    )
                }
                Column(
                    modifier = Modifier
                        .height(50.dp)
                        .padding(start = 8.dp)
                        .weight(0.6f),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.Start
                ) {
                    Text(
                        text = "Catatan Anda",
                        color = Color.White,
                        fontSize = 25.sp,
                        fontWeight = FontWeight.Bold,
                        fontStyle = FontStyle.Normal
                    )
                }
                Column(
                    modifier = Modifier
                        .height(50.dp)
                        .padding(start = 8.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.Start
                ) {
                    Icon(
                        imageVector = Icons.Default.RestoreFromTrash,
                        contentDescription = null,
                        modifier = Modifier
                            .size(38.dp)
                            .padding(start = 6.dp)
                            .clickable {}
                    )
                }
            }
        }

        TextField(
            value = "",
            onValueChange = {},
            modifier = Modifier
                .fillMaxWidth(),
            placeholder = {
                Text(
                    text = "Masukkan Judul"
                )
            }
        )

        TextField(
            value = "",
            onValueChange = {},
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            placeholder = {
                Text(
                    text = "Tambahkan Catatan Anda"
                )
            }
        )
        Spacer(modifier = Modifier.height(20.dp))
    }
}

@Preview
@Composable
fun AddNotePreview(){
    RemainderKesehatanPasienTheme(darkTheme = false) {
        AddNoteScreen(
            onListNoteClicked = {}
        )
    }
}