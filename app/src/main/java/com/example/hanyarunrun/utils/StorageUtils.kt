package com.example.hanyarunrun.utils
import android.content.Context
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

object ImageUtils {
    fun saveImageToInternalStorage(context: Context, uri: Uri): String? {
        try {
            val bitmap: Bitmap = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                val source = ImageDecoder.createSource(context.contentResolver, uri)
                ImageDecoder.decodeBitmap(source)
            } else {
                MediaStore.Images.Media.getBitmap(context.contentResolver, uri)
            }

            val file = File(context.filesDir, "profile_image.jpg")
            val outputStream = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, outputStream)
            outputStream.flush()
            outputStream.close()

            return file.absolutePath // Kembalikan path gambar yang disimpan
        } catch (e: IOException) {
            e.printStackTrace()
            return null
        }
    }
}
