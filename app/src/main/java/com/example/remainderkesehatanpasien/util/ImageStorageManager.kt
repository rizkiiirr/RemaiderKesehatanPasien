package com.example.remainderkesehatanpasien.util

import android.content.Context
import android.net.Uri
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ImageStorageManager @Inject constructor(
    @ApplicationContext private val context: Context
) {
    // Fungsi untuk menyalin gambar dari Uri ke penyimpanan internal aplikasi
    // Mengembalikan Uri dari gambar yang disimpan secara internal
    fun saveImageFromUri(uri: Uri): Uri? {
        // Buat nama file unik
        val fileName = "profile_${System.currentTimeMillis()}.jpg"
        val outputDir = File(context.filesDir, "profile_images") // Direktori kustom di penyimpanan internal
        if (!outputDir.exists()) {
            outputDir.mkdirs() // Buat direktori jika belum ada
        }
        val outputFile = File(outputDir, fileName)

        var inputStream: InputStream? = null
        var outputStream: FileOutputStream? = null

        try {
            inputStream = context.contentResolver.openInputStream(uri)
            outputStream = FileOutputStream(outputFile)

            inputStream?.copyTo(outputStream) // Salin data gambar

            return Uri.fromFile(outputFile) // Mengembalikan Uri dari file yang disimpan secara internal
        } catch (e: Exception) {
            e.printStackTrace()
            return null
        } finally {
            inputStream?.close()
            outputStream?.close()
        }
    }

    // Fungsi opsional untuk menghapus gambar profil lama
    fun deleteImage(uriString: String?) {
        if (uriString == null) return
        try {
            val file = File(Uri.parse(uriString).path ?: return)
            if (file.exists()) {
                file.delete()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}