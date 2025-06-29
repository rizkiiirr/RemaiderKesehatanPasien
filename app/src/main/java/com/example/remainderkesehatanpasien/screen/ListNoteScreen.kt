package com.example.remainderkesehatanpasien.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.remainderkesehatanpasien.component.NoteItem
import com.example.remainderkesehatanpasien.presentation.note.NoteEvent
import com.example.remainderkesehatanpasien.presentation.note.NoteViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListNoteScreen(
    navController: NavController,
    viewModel: NoteViewModel = hiltViewModel(),
    onAddNoteClicked: () -> Unit,
    onNoteClicked: (Int) -> Unit
) {
    val state = viewModel.listState.collectAsState().value

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Catatan Anda") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Kembali")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { onAddNoteClicked() }) {
                Icon(Icons.Default.Add, contentDescription = "Tambah Catatan")
            }
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(vertical = 8.dp)
        ) {
            items(state.notes) { note ->
                NoteItem(
                    note = note,
                    onItemClick = { onNoteClicked(note.id) },
                    onDeleteClick = { viewModel.onEvent(NoteEvent.DeleteNote(note)) }
                )
            }
        }
    }
}