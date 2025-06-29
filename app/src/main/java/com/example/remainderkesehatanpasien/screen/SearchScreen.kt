package com.example.remainderkesehatanpasien.screen

import androidx.compose.foundation.Image
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.Checklist
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.remainderkesehatanpasien.R
import com.example.remainderkesehatanpasien.component.Route
import com.example.remainderkesehatanpasien.data.Menu
import com.example.remainderkesehatanpasien.domain.model.ItemType
import com.example.remainderkesehatanpasien.domain.model.SearchableItem
import com.example.remainderkesehatanpasien.presentation.search.SearchEvent
import com.example.remainderkesehatanpasien.presentation.search.SearchViewModel
import com.example.remainderkesehatanpasien.ui.theme.RemainderKesehatanPasienTheme
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    onNavController: NavController,
    viewModel: SearchViewModel = hiltViewModel(),
){
    val query by viewModel.searchQuery.collectAsState()
    val searchResults by viewModel.searchResults.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Pencarian") },
                navigationIcon = {
                    IconButton(onClick = { onNavController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Kembali")
                    }
                }
            )
        }
    ){innerPadding ->
        Column(
            Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // Kotak Pencarian
            OutlinedTextField(
                value = query,
                onValueChange = { viewModel.onEvent(SearchEvent.OnQueryChange(it)) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                placeholder = { Text("Cari catatan, checklist, atau pengingat...") },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Cari") }
            )

            // Daftar Hasil Pencarian
            if (searchResults.isEmpty() && query.isNotEmpty()) {
                // Tampilkan pesan jika hasil kosong dan pengguna sedang mencari
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Tidak ada hasil ditemukan untuk '$query'")
                }
            } else {
                // Tampilkan LazyColumn jika ada hasil, atau jika query masih kosong
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(searchResults, key = { it.universalId }) { item ->
                        SearchItem(
                            item = item,
                            onClick = {
                                when (item.type) {
                                    ItemType.NOTE -> {
                                        onNavController.navigate(
                                            Route.NoteDetail.path.replace("{noteId}", item.originalId.toString())
                                        )
                                    }
                                    ItemType.CHECKLIST -> {
                                        onNavController.navigate(Route.CheckList.path)
                                    }
                                    ItemType.REMINDER -> {
                                        // Arahkan ke list yang sesuai berdasarkan kategori di dalam reminder
                                        // Kita perlu mengambil kategori dari item reminder asli
                                        // Untuk saat ini, kita bisa asumsikan dari judul atau default.
                                        val category = if (item.title.contains("Obat", ignoreCase = true)) "OBAT" else "KONSULTASI"
                                        onNavController.navigate("reminderList/$category")
                                    }
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}

// Composable baru untuk menampilkan satu item hasil pencarian
@Composable
fun SearchItem(
    item: SearchableItem,
    onClick: () -> Unit
) {
    val icon = when (item.type) {
        ItemType.NOTE -> Icons.Default.Description
        ItemType.CHECKLIST -> Icons.Default.Checklist
        ItemType.REMINDER -> Icons.Default.Notifications
    }

    val dateFormat = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
    val formattedDate = dateFormat.format(Date(item.timestamp))

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(imageVector = icon, contentDescription = item.type.name, modifier = Modifier.size(40.dp))
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(text = item.title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                Text(
                    text = item.description,
                    style = MaterialTheme.typography.bodyMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = "Diubah: $formattedDate",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )
            }
        }
    }
}