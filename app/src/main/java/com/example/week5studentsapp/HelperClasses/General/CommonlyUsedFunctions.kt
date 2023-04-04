package com.example.week5studentsapp.HelperClasses.General

import android.graphics.Bitmap
import android.os.Build
import java.io.ByteArrayOutputStream
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class CommonlyUsedFunctions {
    companion object{

        fun bitmapToByteArray(bitmapImage: Bitmap): ByteArray {
            val byteArrayStream = ByteArrayOutputStream()
            bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, byteArrayStream)
            return byteArrayStream.toByteArray()
        }

        // Change size of bitmap image.
        fun resizeBitmap(source: Bitmap, maxLength: Int = 400): Bitmap {
            return try {
                if (source.height >= source.width) {
                    // if image already smaller than the required height.
                    if (source.height <= maxLength)
                        return source

                    val aspectRatio = source.width.toDouble() / source.height.toDouble()
                    val targetWidth = (maxLength * aspectRatio).toInt()
                    Bitmap.createScaledBitmap(source, targetWidth, maxLength, false)
                } else {
                    // if image already smaller than the required height.
                    if (source.width <= maxLength)
                        return source

                    val aspectRatio = source.height.toDouble() / source.width.toDouble()
                    val targetHeight = (maxLength * aspectRatio).toInt()
                    Bitmap.createScaledBitmap(source, maxLength, targetHeight, false)
                }
            } catch (e: Exception) {
                source
            }
        }

        fun currentDateTime(): String {
            // Get current date and time.
            val current = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                LocalDateTime.now()
            } else {
                TODO("VERSION.SDK_INT < O")
            }
            // Change the format of date and time.
            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS")
            return current.format(formatter)
        }

    }
}