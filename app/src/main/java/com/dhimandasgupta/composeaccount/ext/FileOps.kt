package com.dhimandasgupta.composeaccount.ext

import android.content.Context
import android.net.Uri
import android.os.Build
import android.os.ParcelFileDescriptor
import androidx.core.content.FileProvider
import java.io.BufferedOutputStream
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileInputStream
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.text.SimpleDateFormat
import java.util.Date

private const val FOLDER_CAMERA = "Camera"

fun Context.getCameraFileUri(): Uri {
    return FileProvider.getUriForFile(
        this,
        "$packageName.fileProvider",
        getOutputMediaFile()
    )
}

fun Context.getTargetDirectory() = File(cacheDir, FOLDER_CAMERA)

fun Context.getOutputMediaFile(): File {
    val mediaStorageDir = File(cacheDir, FOLDER_CAMERA)
    if (!mediaStorageDir.exists()) {
        mediaStorageDir.mkdir()
    }
    val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
    return File(
        mediaStorageDir.path + File.separator.toString() +
            "IMG_" + timeStamp + ".jpg"
    )
}

fun Context.copyToCacheDirectory(inputFile: Uri): Uri? {
    val fileDescriptor: ParcelFileDescriptor?
    try {
        fileDescriptor = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            contentResolver.openFile(inputFile, "r", null)
        } else {
            contentResolver.openFileDescriptor(inputFile, "r")
        }

        if (fileDescriptor != null) {
            val input = FileInputStream(fileDescriptor.fileDescriptor)
            val byteArray = readBinaryStream(input, fileDescriptor.statSize.toInt())
            val cachedFile = getOutputMediaFile()
            val fileSaved = writeFile(cachedFile, byteArray)
            if (fileSaved) {
                return FileProvider.getUriForFile(
                    this,
                    "$packageName.fileProvider",
                    cachedFile
                )
            }
        }
    } catch (e: FileNotFoundException) {
        // handle error
    }

    return null
}

fun Context.deleteCacheDirectory() {
    val mediaStorageDir = File(cacheDir, FOLDER_CAMERA)
    mediaStorageDir.listFiles()?.map { if (!it.isDirectory) it.delete() }
}

private fun readBinaryStream(
    stream: InputStream,
    byteCount: Int
): ByteArray {
    val output = ByteArrayOutputStream()
    try {
        val buffer = ByteArray(if (byteCount > 0) byteCount else 4096)
        var read: Int
        while (stream.read(buffer).also { read = it } >= 0) {
            output.write(buffer, 0, read)
        }
    } catch (e: IOException) {
        e.printStackTrace()
    } finally {
        try {
            stream.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
    return output.toByteArray()
}

private fun writeFile(cachedFile: File, data: ByteArray): Boolean {
    return try {
        var output: BufferedOutputStream? = null
        try {
            output = BufferedOutputStream(FileOutputStream(cachedFile))
            output.write(data)
            output.flush()
            true
        } finally {
            output?.close()
        }
    } catch (ex: Exception) {
        false
    }
}
