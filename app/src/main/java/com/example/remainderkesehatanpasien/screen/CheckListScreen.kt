package com.example.remainderkesehatanpasien.screen

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.remainderkesehatanpasien.data.local.entity.CheckListItem
import com.example.remainderkesehatanpasien.presentation.checkList.CheckListEvent
import com.example.remainderkesehatanpasien.presentation.checkList.CheckListState
import com.example.remainderkesehatanpasien.presentation.checkList.CheckListViewModel
import com.example.remainderkesehatanpasien.presentation.checkList.UIEvent
import kotlinx.coroutines.flow.collectLatest


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CheckListScreen(
    navController: NavController,
    viewModel: CheckListViewModel = hiltViewModel()
) {
    val state by viewModel.state
    val newItemTitle by viewModel.newItemTitle
    val context = LocalContext.current

    var showDialog by remember {mutableStateOf(false)}
    var itemToDelete by remember {mutableStateOf<CheckListItem?>(null)}

    // State untuk dialog edit
    var showEditDialog by remember { mutableStateOf(false) }
    var itemToEdit by remember { mutableStateOf<CheckListItem?>(null) }
    var editText by remember { mutableStateOf("") }

    LaunchedEffect(key1 = true){
        viewModel.eventFlow.collectLatest{ event ->
            when(event){
                is UIEvent.ShowSnackbar -> {
                    Toast.makeText(context, event.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Riwayat Pengecekan") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Kembali")
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            // LazyColumn untuk menampilkan daftar item
            LazyColumn(modifier = Modifier.weight(1f)) {
                items(state.items) { item ->
                    ChecklistItemRow(
                        item = item,
                        onCheckedChange = { viewModel.onEvent(CheckListEvent.OnItemCheckedChange(item)) },
                        onDeleteClick = {
                            itemToDelete = item
                            showDialog = true
                        },
                        onEditClick = { // Tambahkan aksi untuk edit
                            itemToEdit = item
                            editText = item.title // Isi textfield dengan judul saat ini
                            showEditDialog = true
                        }
                    )
                    Divider()
                }
            }

            // Bagian untuk menambah item baru
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = newItemTitle,
                    onValueChange = { viewModel.onEvent(CheckListEvent.OnNewItemTitleChange(it)) },
                    label = { Text("Tambah riwayat baru...") },
                    modifier = Modifier.weight(1f)
                )
                Spacer(modifier = Modifier.width(8.dp))
                IconButton(onClick = { viewModel.onEvent(CheckListEvent.OnAddItem) }) {
                    Icon(Icons.Default.Add, contentDescription = "Tambah Item")
                }
            }
        }
    }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = {
                // Aksi saat dialog ditutup (misal: klik di luar dialog atau tombol back)
                showDialog = false
            },
            title = {
                Text(text = "Konfirmasi Hapus")
            },
            text = {
                Text(text = "Apakah Anda yakin ingin menghapus item '${itemToDelete?.title}'?")
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        // Aksi saat tombol "Hapus" ditekan
                        itemToDelete?.let { item ->
                            viewModel.onEvent(CheckListEvent.OnDeleteItem(item))
                        }
                        showDialog = false
                    }
                ) {
                    Text("Hapus")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        // Aksi saat tombol "Batal" ditekan
                        showDialog = false
                    }
                ) {
                    Text("Batal")
                }
            }
        )
    }

    if (showEditDialog) {
        AlertDialog(
            onDismissRequest = { showEditDialog = false },
            title = { Text(text = "Ubah Riwayat") },
            text = {
                // TextField di dalam dialog untuk mengubah judul
                OutlinedTextField(
                    value = editText,
                    onValueChange = { editText = it },
                    label = { Text("Judul Baru") }
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        itemToEdit?.let {
                            viewModel.onEvent(CheckListEvent.OnSaveEdit(it, editText))
                        }
                        showEditDialog = false
                    }
                ) {
                    Text("Simpan")
                }
            },
            dismissButton = {
                TextButton(onClick = { showEditDialog = false }) {
                    Text("Batal")
                }
            }
        )
    }
}


@Composable
fun ChecklistItemRow(
    item: CheckListItem,
    onCheckedChange: (Boolean) -> Unit,
    onDeleteClick: () -> Unit,
    onEditClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onCheckedChange(!item.isChecked) } // Klik di mana saja pada row akan mengubah status
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(
            checked = item.isChecked,
            onCheckedChange = onCheckedChange
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = item.title,
            modifier = Modifier.weight(1f),
            textDecoration = if (item.isChecked) TextDecoration.LineThrough else TextDecoration.None
        )

        IconButton(onClick = onEditClick) {
            Icon(Icons.Default.Edit, contentDescription = "Ubah Item")
        }

        IconButton(onClick = onDeleteClick) {
            Icon(Icons.Default.Delete, contentDescription = "Hapus Item")
        }
    }
}