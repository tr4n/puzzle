package com.tr4n.puzzle.util

import android.graphics.Bitmap
import android.net.Uri
import android.util.Log
import com.tr4n.puzzle.di.App
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

object FileUtils {
    private const val FILENAME_FORMAT = "yyyyMMddHHmmssSSS"
    private const val TEMPORARY = "TEMPORARY"
    private const val IMAGES = "images"
    private const val TEMPORARY_IMAGE = "temporary.JPG"

    private val context by lazy { App.context }

    private val folderTEMPORARY get() = folder(context.cacheDir, TEMPORARY)

    private val folderIMAGES get() = folder(context.filesDir, IMAGES)

    val temporaryImageFile
        get(): File {
            return File(folderTEMPORARY, TEMPORARY_IMAGE).apply {
                createNewFile()
            }
        }

    fun getImageFile(fileName: String): File {
        return File(folderIMAGES, fileName)
    }

    /**
     * The photo has already been taken
     */

    private fun folder(parent: File, name: String): File = File(parent, name).apply {
        if (!exists()) mkdirs()
    }

    /**
     * The photo has already taken will be saved into draft file before consignment's finish
     */
    suspend fun savePuzzlePhoto(): String? = withContext(Dispatchers.IO) {
        val dateTime = SimpleDateFormat(FILENAME_FORMAT, Locale.getDefault())
            .format(System.currentTimeMillis())
        val name = "${dateTime}.JPG"
        val file = File(folderIMAGES, name)
        val temporaryFile = temporaryImageFile
        val orientation = BitmapUtils.getCameraPhotoOrientation(temporaryFile.path)
        val bitmap =
            BitmapUtils.rotateBitmap(temporaryFile.path, orientation) ?: return@withContext null
        BitmapUtils.compressBitmapToFile(bitmap, file, 100)
        return@withContext name
    }

    suspend fun getRotatedTemporaryImageBitmap(): Bitmap? = withContext(Dispatchers.IO) {
        val temporaryFile = temporaryImageFile
        if (!temporaryFile.exists()) return@withContext null
        return@withContext try {
            val rotate = BitmapUtils.getCameraPhotoOrientation(temporaryFile.path)
            val bm = BitmapUtils.getBitmapFromFile(temporaryFile) ?: return@withContext null
            val result = BitmapUtils.rotateBitmap(bm, rotate)
            result?.also { bitmap ->
                BitmapUtils.compressBitmapToFile(
                    bitmap,
                    temporaryFile,
                    BitmapUtils.CAPTURE_QUALITY_JPEG
                )
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    /**
     * @return Bitmap of the photo has already been taken
     * Run in background thread
     */
    suspend fun getTemporaryImageBitmap(): Bitmap? {
        return withContext(Dispatchers.IO) {
            val temporaryFile = temporaryImageFile
            if (!temporaryFile.exists()) return@withContext null
            return@withContext try {
                BitmapUtils.getBitmapFromFile(temporaryFile)
            } catch (e: Exception) {
                Log.e("TAG", e.message.toString())
                null
            }
        }
    }

    suspend fun saveTemporaryFileFromUri(uri: Uri?) = withContext(Dispatchers.IO) {
        uri ?: return@withContext
        runCatching {
            val inputStream = context.contentResolver.openInputStream(uri) ?: return@withContext
            temporaryImageFile.outputStream().use { output ->
                inputStream.copyTo(output)
            }
        }.getOrThrow()
    }
}

