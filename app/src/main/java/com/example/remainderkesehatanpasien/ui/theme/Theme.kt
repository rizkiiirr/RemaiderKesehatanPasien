package com.example.remainderkesehatanpasien.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

// Definisikan Light Color Scheme menggunakan warna kustom Anda
private val LightColorScheme = lightColorScheme(
    primary = PrimaryLight,
    onPrimary = OnPrimaryLight,
    secondary = SecondaryLight,
    onSecondary = OnSecondaryLight,
    tertiary = SecondaryLight, // Menggunakan secondary sebagai tertiary jika tidak ada tertiary khusus
    onTertiary = OnSecondaryLight,
    background = BackgroundLight,
    onBackground = OnBackgroundLight,
    surface = SurfaceLight,
    onSurface = OnSurfaceLight,
    error = ErrorLight,
    onError = OnErrorLight,
    // Jika Anda memiliki warna tambahan seperti surfaceVariant, outline, dll.
    surfaceVariant = BackgroundLight, // Memberikan sedikit variasi pada permukaan
    outline = OnSurfaceLight.copy(alpha = 0.3f) // Warna untuk border atau outline
)

// Definisikan Dark Color Scheme menggunakan warna kustom Anda
private val DarkColorScheme = darkColorScheme(
    primary = PrimaryDark,
    onPrimary = OnPrimaryDark,
    secondary = SecondaryDark,
    onSecondary = OnSecondaryDark,
    tertiary = SecondaryDark,
    onTertiary = OnSecondaryDark,
    background = BackgroundDark,
    onBackground = OnBackgroundDark,
    surface = SurfaceDark,
    onSurface = OnSurfaceDark,
    error = ErrorDark,
    onError = OnErrorDark,
    surfaceVariant = SurfaceDark,
    outline = OnSurfaceDark.copy(alpha = 0.3f)
)

@Composable
fun RemainderKesehatanPasienTheme(
    darkTheme: Boolean = isSystemInDarkTheme(), // Gunakan darkTheme yang diteruskan dari MainActivity
    // Dynamic color is available only on Android 12+
    dynamicColor: Boolean = false, // Atur ke false karena kita menggunakan palet kustom
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.primary.toArgb() // Mengubah warna status bar
            // Mengubah ikon status bar agar sesuai dengan tema terang/gelap
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography, // Pastikan Typography Anda sudah didefinisikan atau gunakan default
        content = content
    )
}