package com.serenity.serenityapp.helper

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import java.io.File
import java.io.FileOutputStream

fun Context.compressImage(imageUri: Uri?): File? {
    if (imageUri == null) return null

    val originalBitmap: Bitmap? = BitmapFactory.decodeStream(this.contentResolver.openInputStream(imageUri))

    originalBitmap?.let { bitmap ->
        val outputFile = File(this.cacheDir, "compressed_image.jpg")

        var quality = 100

        while (quality > 0) {
            FileOutputStream(outputFile).use { fos ->
                bitmap.compress(Bitmap.CompressFormat.JPEG, quality, fos)
            }

            if (outputFile.length() <= 1_000_000) {
                if (!bitmap.isRecycled) {
                    bitmap.recycle()
                }
                return outputFile
            }
            quality -= 10
        }

        if (!bitmap.isRecycled) bitmap.recycle()
    }

    return null
}