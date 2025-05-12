package com.example.remainderkesehatanpasien.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.RestoreFromTrash
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
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
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.remainderkesehatanpasien.ui.theme.RemainderKesehatanPasienTheme

@Composable
fun ListNoteScreen(
    viewModel: NoteViewModel = viewModel(),
    onHomeClicked: () -> Unit,
    onAddNoteClicked: () -> Unit,
    onNoteClicked: () -> Unit
) {
    val note = viewModel.note

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    onAddNoteClicked()
                },
                containerColor = MaterialTheme.colorScheme.onSurface
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = null,
                    tint = Color.White
                )
            }
        }
    ) { innerPadding ->

        Column(
            Modifier
                .fillMaxSize()
                .fillMaxWidth()
                .padding(innerPadding)
                .background(MaterialTheme.colorScheme.background),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            ConstraintLayout {
                val (border) = createRefs()
                Box(
                    modifier = Modifier.fillMaxWidth()
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
                                    onHomeClicked()
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
                                .clickable {}
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding()
                    .background(MaterialTheme.colorScheme.background),
                verticalArrangement = Arrangement.spacedBy(14.dp),
                contentPadding = PaddingValues(
                    vertical = 18.dp
                )
            ) {
                items(note.size) { index ->
                    val notes = note[index]

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(
                                horizontal = 16.dp
                            )
                            .background(
                                Color.Red,
                                shape = RoundedCornerShape(10.dp)
                            )
                            .padding(
                                horizontal = 16.dp,
                                vertical = 14.dp
                            )
                            .clickable {
                                onNoteClicked()
                            }
                    ) {
                        Text(
                            text = notes.title,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = notes.description
                        )
                    }
                }
            }
        }
    }
}


@Preview
@Composable
private fun ListNotePreview(){
    RemainderKesehatanPasienTheme(darkTheme = false) {
        ListNoteScreen(
            onHomeClicked = {},
            onAddNoteClicked = {},
            onNoteClicked = {}
        )
    }
}