import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.remainderkesehatanpasien.presentation.note.NoteEvent
import com.example.remainderkesehatanpasien.presentation.note.NoteViewModel
import com.example.remainderkesehatanpasien.presentation.note.UiEvent
import kotlinx.coroutines.flow.collectLatest

// NoteScreen sekarang akan menjadi wrapper untuk AddNoteScreen,
// karena UI untuk Tambah dan Edit pada dasarnya sama.
// Ini adalah praktik yang baik untuk menghindari duplikasi kode.
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoteScreen(
    navController: NavController,
    viewModel: NoteViewModel = hiltViewModel()
) {
    val titleState = viewModel.noteTitle.value
    val descriptionState = viewModel.noteDescription.value
    val context = LocalContext.current

    LaunchedEffect(key1 = true) {
        viewModel.eventFlow.collectLatest { event ->
            when(event) {
                is UiEvent.ShowSnackbar -> {
                    Toast.makeText(context, event.message, Toast.LENGTH_SHORT).show()
                }
                is UiEvent.SaveNote -> {
                    navController.navigateUp()
                }
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Buat Catatan") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Kembali")
                    }
                }) },
        floatingActionButton = {
            FloatingActionButton(onClick = { viewModel.onEvent(NoteEvent.SaveNote) }) {
                Icon(imageVector = Icons.Default.Done, contentDescription = "Simpan Perubahan")
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            TextField(
                value = titleState.text,
                onValueChange = { viewModel.onEvent(NoteEvent.EnteredTitle(it)) },
                placeholder = { Text(text = titleState.hint) },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
            Spacer(modifier = Modifier.height(16.dp))
            TextField(
                value = descriptionState.text,
                onValueChange = { viewModel.onEvent(NoteEvent.EnteredDescription(it)) },
                placeholder = { Text(text = descriptionState.hint) },
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}