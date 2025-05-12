package com.example.remainderkesehatanpasien.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
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
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.remainderkesehatanpasien.R
import com.example.remainderkesehatanpasien.data.BottomNavigation
import com.example.remainderkesehatanpasien.ui.theme.RemainderKesehatanPasienTheme

@Composable
fun DashboardScreen(
    navController: NavHostController,
    darkMode: Boolean,
    onToggleDarkMode: () -> Unit,
    onHomeClicked: () -> Unit,
    onProfileClicked: () -> Unit,
    onSettingsClicked: () -> Unit,
    onSearchClicked: () -> Unit,
    onListNoteClicked: () -> Unit
){
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
                }.background(Color.Red)
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
                        color = Color.White,
                        fontSize = 30.sp,
                        fontWeight = FontWeight.Bold,
                        fontStyle = FontStyle.Normal
                    )
                }

                Column(modifier = Modifier
                    .height(50.dp)
                    .padding(start = 90.dp)
                    .weight(0.7f),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.Start
                ) {
                    Switch(
                        checked = darkMode,
                        onCheckedChange = { onToggleDarkMode() }
                    )
                }

                Image(
                    painter = painterResource(id = R.drawable.account),
                    contentDescription = null,
                    modifier = Modifier.width(50.dp)
                        .height(50.dp)
                        .clickable {
                            onProfileClicked()
                        }
                )
            }
        }

        var searchText by rememberSaveable{
            mutableStateOf("")
        }
        TextField(
            value = searchText,
            onValueChange = {searchText = it},
            label = {
                Text(
                    text = "Pencarian...")
                    },
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
            ),
            modifier = Modifier
                .fillMaxWidth()
                .height(70.dp)
                .padding(top = 24.dp, end = 24.dp, start = 24.dp)
                .shadow(3.dp, shape = RoundedCornerShape(50.dp))
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
                    fontWeight = FontWeight.Bold,
                )
            }
            Image(
                painter = painterResource(id = R.drawable.account_box),
                contentDescription = null,
                modifier = Modifier
                    .width(100.dp)
                    .padding(start = 10.dp, top = 25.dp)
                    .height(100.dp)
                    .clickable {
                        onProfileClicked()
                    }
            )
        }

        Text(
            text = "KATEGORI",
            color = Color.Black,
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
                            color = Color.Red,
                            shape = RoundedCornerShape(10.dp)
                        )
                        .padding(16.dp)
                )
                Text(
                    text = "Minum Obat",
                    color = Color.Black,
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
                            color = Color.Red,
                            shape = RoundedCornerShape(10.dp)
                        )
                        .padding(16.dp)
                )
                Text(
                    text = "Jadwal Konsultasi",
                    color = Color.Black,
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
                            color = Color.Red,
                            shape = RoundedCornerShape(10.dp)
                        )
                        .padding(16.dp)
                )
                Text(
                    text = "Cek Riwayat",
                    color = Color.Black,
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
                            color = Color.Red,
                            shape = RoundedCornerShape(10.dp)
                        )
                        .padding(16.dp)
                        .clickable {
                            onListNoteClicked()
                        }
                )
                Text(
                    text = "Catatan Kesehatan",
                    color = Color.Black,
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
    NavigationBar {
        Row(
            modifier = Modifier.background(MaterialTheme.colorScheme.inverseOnSurface)
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

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun DashboardPreview() {
    var previewDarkMode by rememberSaveable { mutableStateOf(false) }

    RemainderKesehatanPasienTheme(darkTheme = false) {
        DashboardScreen(
            navController = rememberNavController(),
            darkMode = previewDarkMode,
            onToggleDarkMode = { previewDarkMode = !previewDarkMode },
            onHomeClicked = {},
            onProfileClicked = {},
            onSettingsClicked = {},
            onSearchClicked = {},
            onListNoteClicked = {}
        )
    }
}
