package com.example.remainderkesehatanpasien.presentation.settings

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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
    darkMode: Boolean,
    onToggleDarkMode: (Boolean) -> Unit,
    onLoginSecurityClicked: () -> Unit,
    onNotificationsEnabled: (Boolean) -> Unit,
    onLogoutClicked: () -> Unit,
){
    var notificationsEnabled by rememberSaveable { mutableStateOf(true) }

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
                }.background(MaterialTheme.colorScheme.primary), // Warna utama dari tema

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
                        color = MaterialTheme.colorScheme.onPrimary, // Warna teks di atas primary
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

        Spacer(modifier = Modifier.height(24.dp))

        // Bagian Akun
        SettingCategoryHeader("Akun")
        SettingsItem(
            icon = Icons.Default.AccountCircle,
            mainText = "Info Akun",
            onClick = onProfileClicked // Navigasi ke halaman profil
        )
        SettingsItem(
            icon = Icons.Default.Lock,
            mainText = "Login dan Keamanan",
            onClick = onLoginSecurityClicked // Navigasi ke halaman ubah password
        )

        // Bagian Preferensi
        SettingCategoryHeader("Preferensi")
        SettingsSwitchItem(
            icon = Icons.Default.Palette,
            mainText = "Mode Gelap",
            checked = darkMode,
            onCheckedChange = { isChecked -> onToggleDarkMode(isChecked) }
        )

        // Bagian Keluar
        Spacer(modifier = Modifier.height(24.dp)) // Tambahkan jarak
        SettingCategoryHeader("Lainnya") // Kategori baru untuk Logout
        SettingsItem(
            icon = Icons.Default.ExitToApp, // Ikon keluar
            mainText = "Keluar Akun",
            onClick = onLogoutClicked // <-- Panggil fungsi logout
        )

        Button(
            onClick = { throw RuntimeException("Test Crash dari Tombol Pengaturan") },
            modifier = Modifier.padding(16.dp),
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error) // Warna error dari tema
        ) {
            Text("Tes Crash Aplikasi", color = MaterialTheme.colorScheme.onError)
        }
    }
}

@Composable
fun SettingCategoryHeader(title: String) {
    Text(
        text = title,
        color = MaterialTheme.colorScheme.onBackground, // Warna teks dari tema
        fontSize = 20.sp,
        fontStyle = FontStyle.Normal,
        fontWeight = FontWeight.Bold,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 14.dp, vertical = 16.dp)
    )
}

@Composable
fun SettingsItem(
    icon: ImageVector,
    mainText: String,
    onClick:()->Unit
){
    Card(
        onClick = onClick,
        modifier = Modifier
            .padding(bottom = 8.dp, start = 14.dp, end = 14.dp) // Sesuaikan padding
            .fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant), // Warna surface variant dari tema
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp), // Beri sedikit elevasi
        shape = RoundedCornerShape(8.dp) // Sudut membulat
    ) {
        Row(
            modifier = Modifier
                .padding(vertical = 14.dp, horizontal = 16.dp), // Sesuaikan padding
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ){
            Row(verticalAlignment = Alignment.CenterVertically) {
                // Gunakan Icon langsung dari ImageVector
                Icon(
                    imageVector = icon,
                    contentDescription = null, // Content description for accessibility
                    tint = MaterialTheme.colorScheme.primary, // Warna ikon
                    modifier = Modifier.size(24.dp)
                )

                Spacer(modifier = Modifier.width(16.dp))

                Text(
                    text = mainText,
                    style = MaterialTheme.typography.bodyLarge, // Gaya teks
                    color = MaterialTheme.colorScheme.onSurfaceVariant, // Warna teks di atas surface variant
                    fontWeight = FontWeight.Normal
                )
            }
            Icon(
                painter = painterResource(id = R.drawable.ic_arrow_forward), // Asumsi Anda punya ikon panah maju
                contentDescription = "Forward",
                tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f),
                modifier = Modifier.size(20.dp)
            )
        }
    }
}

@Composable
fun SettingsSwitchItem(
    icon: ImageVector,
    mainText: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Card(
        modifier = Modifier
            .padding(bottom = 8.dp, start = 14.dp, end = 14.dp)
            .fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = RoundedCornerShape(8.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(vertical = 14.dp, horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ){
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(24.dp)
                )

                Spacer(modifier = Modifier.width(16.dp))

                Text(
                    text = mainText,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontWeight = FontWeight.Normal
                )
            }
            Switch(
                checked = checked,
                onCheckedChange = onCheckedChange,
                modifier = Modifier.padding(start = 8.dp)
            )
        }
    }
}


@Preview(showBackground = true, showSystemUi = true)
@Composable
fun SettingsPreview(){
    var previewDarkMode by rememberSaveable { mutableStateOf(false) }
    RemainderKesehatanPasienTheme(darkTheme = previewDarkMode) {
        SettingsScreen(
            onProfileClicked = {},
            onHomeClicked = {},
            darkMode = previewDarkMode,
            onToggleDarkMode = { isChecked -> previewDarkMode = isChecked }, // <-- PERBAIKI DI SINI
            onLoginSecurityClicked = {},
            onNotificationsEnabled = { isEnabled -> /* do nothing for preview */ }, // <-- PERBAIKI DI SINI
            onLogoutClicked = {}
        )
    }
}