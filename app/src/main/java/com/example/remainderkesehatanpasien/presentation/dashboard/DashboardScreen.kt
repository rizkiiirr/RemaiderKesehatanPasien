package com.example.remainderkesehatanpasien.presentation.dashboard

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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.example.remainderkesehatanpasien.R
import com.example.remainderkesehatanpasien.domain.model.BottomNavigation
import com.example.remainderkesehatanpasien.data.remote.Article
import com.example.remainderkesehatanpasien.presentation.news.NewsState
import com.example.remainderkesehatanpasien.presentation.news.NewsViewModel
import com.example.remainderkesehatanpasien.ui.theme.RemainderKesehatanPasienTheme

@Composable
fun DashboardScreen(
    navController: NavHostController,
    onHomeClicked: () -> Unit,
    onProfileClicked: () -> Unit,
    onSettingsClicked: () -> Unit,
    onSearchClicked: () -> Unit,
    onListNoteClicked: () -> Unit,
    newsViewModel: NewsViewModel = hiltViewModel(),
    dashboardViewModel: DashboardViewModel = hiltViewModel(), // Injeksi DashboardViewModel
    onNewsMoreClicked: () -> Unit,
    onCheckListClicked: () -> Unit,
    onDrinkMedicineClicked: () -> Unit,
    onConsultationClicked: () -> Unit
){
    val newsState = newsViewModel.state.value
    val currentUser by dashboardViewModel.currentUser.collectAsState() // Ambil currentUser dari DashboardViewModel

    Scaffold(
        bottomBar = {
            BottomNavigationBar(
                navController = navController,
                onHomeClicked = onHomeClicked,
                onProfileClicked = onProfileClicked,
                onSettingsClicked = onSettingsClicked,
                onSearchClicked = onSearchClicked
            )
        }
    ){ innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
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
                    }.background(MaterialTheme.colorScheme.primary)
                )
                Row(modifier = Modifier
                    .padding(top = 10.dp, start = 10.dp, end = 24.dp)
                    .fillMaxWidth()
                ){
                    Column(modifier = Modifier
                        .height(50.dp)
                        .padding(start = 8.dp)
                        .weight(0.7f),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.Start
                    ){
                        Text(
                            text = "REKAP",
                            color = MaterialTheme.colorScheme.onPrimary,
                            fontSize = 30.sp,
                            fontWeight = FontWeight.Bold,
                            fontStyle = FontStyle.Normal
                        )
                    }

                    Spacer(modifier = Modifier.weight(0.7f))

                    AsyncImage(
                        model = currentUser?.profileImageUrl ?: R.drawable.account,
                        contentDescription = "Profile Picture",
                        modifier = Modifier
                            .width(50.dp)
                            .height(50.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.surfaceVariant)
                            .clickable { onProfileClicked() },
                        contentScale = ContentScale.Crop
                    )
                }
            }

            NewsSection(
                newsState = newsState,
                onNewsMoreClicked = onNewsMoreClicked,
                onArticleClick = { /* Nanti bisa diimplementasikan untuk membuka artikel di WebView jika perlu, atau cukup buka link */ }
            )


            Text(
                text = "KATEGORI",
                color = MaterialTheme.colorScheme.onBackground,
                fontSize = 25.sp,
                fontStyle = FontStyle.Normal,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .padding(top = 16.dp)
            )
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, end = 16.dp, top = 16.dp)
            ){
                Column(
                    modifier = Modifier
                        .weight(0.25f),
                    horizontalAlignment = Alignment.CenterHorizontally
                ){
                    Image(
                        painter = painterResource(id = R.drawable.reminder1),
                        contentDescription = null,
                        modifier = Modifier
                            .padding(top = 8.dp, bottom = 4.dp)
                            .background(
                                color = MaterialTheme.colorScheme.secondary, // Warna secondary dari tema
                                shape = RoundedCornerShape(10.dp)
                            )
                            .padding(16.dp)
                            .clickable { onDrinkMedicineClicked() }
                    )
                    Text(
                        text = "Minum Obat",
                        color = MaterialTheme.colorScheme.onBackground, // Warna teks dari tema
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .padding(top = 8.dp)
                    )
                }

                Column(
                    modifier = Modifier
                        .weight(0.25f),
                    horizontalAlignment = Alignment.CenterHorizontally
                ){
                    Image(
                        painter = painterResource(id = R.drawable.reminder2),
                        contentDescription = null,
                        Modifier
                            .padding(top = 8.dp, bottom = 4.dp)
                            .background(
                                color = MaterialTheme.colorScheme.secondary, // Warna secondary dari tema
                                shape = RoundedCornerShape(10.dp)
                            )
                            .padding(16.dp)
                            .clickable { onConsultationClicked() }
                    )
                    Text(
                        text = "Jadwal Konsultasi",
                        color = MaterialTheme.colorScheme.onBackground, // Warna teks dari tema
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .padding(top = 8.dp)
                    )
                }
            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, end = 16.dp, top = 16.dp)
            ){
                Column(
                    modifier = Modifier
                        .weight(0.25f),
                    horizontalAlignment = Alignment.CenterHorizontally
                ){
                    Image(
                        painter = painterResource(id = R.drawable.check),
                        contentDescription = null,
                        Modifier
                            .padding(top = 8.dp, bottom = 4.dp)
                            .background(
                                color = MaterialTheme.colorScheme.secondary, // Warna secondary dari tema
                                shape = RoundedCornerShape(10.dp)
                            )
                            .padding(16.dp)
                            .clickable { onCheckListClicked() }
                    )
                    Text(
                        text = "Cek Riwayat",
                        color = MaterialTheme.colorScheme.onBackground, // Warna teks dari tema
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .padding(top = 8.dp)
                    )
                }
                Column(
                    modifier = Modifier
                        .weight(0.25f),
                    horizontalAlignment = Alignment.CenterHorizontally
                ){
                    Image(
                        painter = painterResource(id = R.drawable.notes),
                        contentDescription = null,
                        Modifier
                            .padding(top = 8.dp, bottom = 4.dp)
                            .background(
                                color = MaterialTheme.colorScheme.secondary, // Warna secondary dari tema
                                shape = RoundedCornerShape(10.dp)
                            )
                            .padding(16.dp)
                            .clickable {
                                onListNoteClicked()
                            }
                    )
                    Text(
                        text = "Catatan Kesehatan",
                        color = MaterialTheme.colorScheme.onBackground, // Warna teks dari tema
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .padding(top = 8.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun BottomNavigationBar(
    navController: NavHostController,
    onHomeClicked: () -> Unit,
    onProfileClicked: () -> Unit,
    onSettingsClicked: () -> Unit,
    onSearchClicked: () -> Unit
) {
    val bottoms = listOf(
        BottomNavigation("Dasbor", Icons.Default.Home, "home"),
        BottomNavigation("Profil", Icons.Default.AccountCircle, "profile"),
        BottomNavigation("Pengaturan", Icons.Default.Settings, "settings"),
        BottomNavigation("Pencarian", Icons.Default.Search, "search")
    )
    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route
    NavigationBar(
        containerColor = MaterialTheme.colorScheme.surface // Warna container dari tema
    ) {
        Row(
            modifier = Modifier.background(MaterialTheme.colorScheme.surface) // Latar belakang bar
        ) {
            bottoms.forEach { item ->
                NavigationBarItem(
                    selected = currentRoute == item.route,
                    onClick = {
                        when (item.route){
                            "home" -> onHomeClicked()
                            "profile" -> onProfileClicked()
                            "settings" -> onSettingsClicked()
                            "search" -> onSearchClicked()
                        }
                    },
                    icon = {
                        Icon(
                            imageVector = item.icon,
                            contentDescription = item.name,
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    },
                    label = {
                        Text(
                            text = item.name,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                )
            }
        }
    }
}

@Composable
fun NewsSection(
    newsState: NewsState,
    onNewsMoreClicked: () -> Unit,
    onArticleClick: (Article) -> Unit
) {
    val uriHandler = LocalUriHandler.current // Untuk membuka URL artikel

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 24.dp, start = 24.dp, end = 24.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Berita Terbaru",
                color = MaterialTheme.colorScheme.onBackground, // Warna teks
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Berita Lainnya",
                color = MaterialTheme.colorScheme.primary, // Warna primary untuk link
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.clickable { onNewsMoreClicked() } // Klik untuk ke NewsScreen
            )
        }
        Spacer(modifier = Modifier.height(16.dp))

        if (newsState.isLoading) {
            LinearProgressIndicator(modifier = Modifier.fillMaxWidth()) // Indikator loading
        } else if (newsState.error != null) {
            Text(
                text = "Gagal memuat berita: ${newsState.error}",
                color = MaterialTheme.colorScheme.error, // Warna error
                modifier = Modifier.fillMaxWidth()
            )
        } else if (newsState.articles.isEmpty()) {
            Text(
                text = "Tidak ada berita terbaru yang tersedia.",
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f), // Warna teks
                modifier = Modifier.fillMaxWidth()
            )
        } else {
            LazyRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                contentPadding = PaddingValues(horizontal = 4.dp)
            ) {
                items(newsState.articles) { article ->
                    NewsCardItem(article = article) {
                        onArticleClick(article) // Teruskan artikel yang diklik
                        uriHandler.openUri(article.url) // Buka URL langsung
                    }
                }
            }
        }
    }
}

// Komponen NewsCardItem juga perlu di update untuk menampilkan gambar
@Composable
fun NewsCardItem(article: Article, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .width(280.dp) // Lebar tetap untuk setiap kartu berita horizontal
            .height(220.dp) // Sesuaikan tinggi agar gambar muat
            .clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant) // Warna permukaan dari tema
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
            // Gambar artikel di bagian atas kartu
            article.urlToImage?.let { imageUrl ->
                AsyncImage(
                    model = imageUrl,
                    contentDescription = article.title,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp) // Tinggi gambar di kartu kecil
                        .clip(RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp)), // Sudut membulat hanya di atas
                    contentScale = ContentScale.Crop // Agar gambar mengisi area tanpa terdistorsi
                )
            }
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp)
            ) {
                Text(
                    text = article.title,
                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.onSurfaceVariant, // Warna teks
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(4.dp))
                article.source?.name?.let { sourceName ->
                    Text(
                        text = "Sumber: $sourceName",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
                Spacer(modifier = Modifier.weight(1f)) // Dorong konten ke atas
                Text(
                    text = article.publishedAt.substringBefore("T"), // Hanya tanggal
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
                )
            }
        }
    }
}


@Preview(showBackground = true, showSystemUi = true)
@Composable
fun DashboardPreview() {
    RemainderKesehatanPasienTheme(darkTheme = false) {
        DashboardScreen(
            navController = rememberNavController(),
            onHomeClicked = {},
            onProfileClicked = {},
            onSettingsClicked = {},
            onSearchClicked = {},
            onListNoteClicked = {},
            newsViewModel = hiltViewModel(), // Injeksi ViewModel untuk preview juga
            dashboardViewModel = hiltViewModel(), // <--- Untuk preview, bisa gunakan viewModel() atau buat dummy
            onNewsMoreClicked = {},
            onCheckListClicked = {},
            onDrinkMedicineClicked = {},
            onConsultationClicked = {}
        )
    }
}
