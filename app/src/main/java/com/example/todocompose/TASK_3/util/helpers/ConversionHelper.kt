package com.example.todocompose.TASK_3.util.helpers

import android.content.Context
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import androidx.core.net.toUri

object ConversionHelper {

    fun uriToBitmap(context: Context, uri: Uri): Bitmap {
        return if (Build.VERSION.SDK_INT < 28) {
            MediaStore.Images.Media.getBitmap(
                context.contentResolver,
                uri
            )
        } else {
            ImageDecoder.decodeBitmap(
                ImageDecoder.createSource(
                    context.contentResolver,
                    uri
                )
            )
        }
    }

    fun sessionToLinkURI(session: String): Uri = "${session.substring(0, session.indexOf("&uploadType"))}&alt=media".toUri()

}