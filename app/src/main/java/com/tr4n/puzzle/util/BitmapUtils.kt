package com.tr4n.puzzle.util

import android.content.ContentResolver
import android.content.res.Resources
import android.graphics.*
import android.net.Uri
import android.provider.MediaStore
import androidx.core.graphics.get
import androidx.exifinterface.media.ExifInterface
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min


/** Utils functions for bitmap conversions.  */
object BitmapUtils {
    const val CAPTURE_QUALITY_JPEG = 80
    const val QUALITY_JPEG_75 = 75
    private const val DEFAULT_SIZE_IMAGE = 4
    private const val VALUE_R = 0.2126
    private const val VALUE_G = 0.7152
    private const val VALUE_B = 0.0722

    /**
     * rotate image from path image
     * return Bitmap image
     */
    fun rotateBitmap(bitmap: Bitmap, rotate: Float): Bitmap? {
        val matrix = Matrix().apply {
            postRotate(rotate)
        }
        return Bitmap.createBitmap(
            bitmap,
            0,
            0,
            bitmap.width,
            bitmap.height,
            matrix,
            true
        ).also {
            //   bitmap.autoRecycle()
        }
    }

    /**
     * Save bitmap into file type JPEG
     */
    fun compressBitmapToFile(bitmap: Bitmap, file: File, quality: Int = QUALITY_JPEG_75) {
        try {
            val outputStream = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream)
            outputStream.flush()
            outputStream.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /** Rotates a bitmap if it is converted from a bytebuffer.  */
    private fun rotateBitmap(
        bitmap: Bitmap, rotationDegrees: Int, flipX: Boolean, flipY: Boolean,
    ): Bitmap {
        val matrix = Matrix()

        // Rotate the image back to straight.
        matrix.postRotate(rotationDegrees.toFloat())

        // Mirror the image along the X or Y axis.
        matrix.postScale(if (flipX) -1.0f else 1.0f, if (flipY) -1.0f else 1.0f)
        val rotatedBitmap =
            Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)

        // Recycle the old bitmap if it has changed.
        if (rotatedBitmap != bitmap) {
            bitmap.recycle()
        }
        return rotatedBitmap
    }

    fun getResizedBitmap(bitmap: Bitmap, newWidth: Int, newHeight: Int): Bitmap {
        val width = bitmap.width
        val height = bitmap.height
        val scaleWidth = newWidth.toFloat() / width
        val scaleHeight = newHeight.toFloat() / height
        val matrix = Matrix().apply {
            postScale(scaleWidth, scaleHeight)
        }
        return Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, false)
    }

    fun decodeSampledBitmapFromResource(
        res: Resources,
        resId: Int,
        reqWidth: Int,
        reqHeight: Int
    ): Bitmap {
        // First decode with inJustDecodeBounds=true to check dimensions
        return BitmapFactory.Options().run {
            inJustDecodeBounds = true
            BitmapFactory.decodeResource(res, resId, this)

            // Calculate inSampleSize
            inSampleSize = calculateInSampleSize(this, reqWidth, reqHeight)

            // Decode bitmap with inSampleSize set
            inJustDecodeBounds = false

            BitmapFactory.decodeResource(res, resId, this)
        }
    }

    fun decodeSampledBitmapFromFile(file: File, reqWidth: Int, reqHeight: Int): Bitmap {
        // First decode with inJustDecodeBounds=true to check dimensions
        return BitmapFactory.Options().run {
            inJustDecodeBounds = true
            BitmapFactory.decodeFile(file.path, this)

            // Calculate inSampleSize
            inSampleSize = calculateInSampleSize(this, reqWidth, reqHeight)

            // Decode bitmap with inSampleSize set
            inJustDecodeBounds = false

            BitmapFactory.decodeFile(file.path, this)
        }
    }

    private fun calculateInSampleSize(
        options: BitmapFactory.Options,
        reqWidth: Int,
        reqHeight: Int
    ): Int {
        // Raw height and width of image
        val (height: Int, width: Int) = options.run { outHeight to outWidth }
        var inSampleSize = 1

        if (height > reqHeight || width > reqWidth) {

            val halfHeight: Int = height / 2
            val halfWidth: Int = width / 2

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) >= reqHeight && (halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 2
            }
        }

        return inSampleSize
    }

    fun getResizedLimitWidthBitmap(bitmap: Bitmap, maxWidth: Int): Bitmap {
        val width = min(maxWidth, bitmap.width)
        val height = (width * bitmap.height) / bitmap.width
        return getResizedBitmap(bitmap, width, height)
    }

    fun mergeHorizontalBitmap(left: Bitmap, right: Bitmap): Bitmap {
        val width = left.width + right.width
        val height = max(left.height, right.height)
        val comboBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val comboImage = Canvas(comboBitmap)
        comboImage.drawBitmap(left, 0f, 0f, null)
        comboImage.drawBitmap(right, left.width.toFloat(), 0f, null)
        return comboBitmap
    }

    fun mergeVerticalBitmap(top: Bitmap, bottom: Bitmap): Bitmap {
        val width = max(top.width, bottom.width)
        val height = top.height + bottom.height
        val comboBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        Canvas(comboBitmap).apply {
            drawBitmap(top, 0f, 0f, null)
            drawBitmap(bottom, 0f, top.height.toFloat(), null)
        }
        return comboBitmap
    }

    fun drawBitmap(bitmap: Bitmap, rect: Rect): Bitmap {
        val paint = Paint().apply {
            strokeWidth = 8f
            style = Paint.Style.STROKE
            isAntiAlias = true
            isFilterBitmap = true
            isDither = true
            color = Color.YELLOW
        }
        Canvas(bitmap).drawRect(rect, paint)
        return bitmap
    }

    fun drawBitmap(bitmap: Bitmap, points: List<Point>): Bitmap {
        if (points.isEmpty() || points.singleOrNull() != null) return bitmap
        val paint = Paint().apply {
            strokeWidth = 5f
            style = Paint.Style.STROKE
            isAntiAlias = true
            isFilterBitmap = true
            isDither = true
            color = Color.YELLOW
        }

        Canvas(bitmap).run {
            if (points.size > 2) {
                for (i in 0..points.size - 2) {
                    drawPoint2Point(paint, points[i], points[i + 1])
                }
            }
            drawPoint2Point(paint, points.first(), points.last())
        }
        return bitmap
    }

    private fun Canvas.drawPoint2Point(paint: Paint, start: Point, stop: Point) = apply {
        drawLine(start.x.toFloat(), start.y.toFloat(), stop.x.toFloat(), stop.y.toFloat(), paint)
    }

    fun getHistogramDiagram(bitmap: Bitmap): IntArray {
        val histogram = IntArray(256) { 0 }
        for (x in 0 until bitmap.width) {
            for (y in 0 until bitmap.height) {
                val color = bitmap[x, y]
                val r = Color.red(color)
                val g = Color.green(color)
                val b = Color.blue(color)

                val brightness = (VALUE_R * r + VALUE_G * g + VALUE_B * b).toInt()
                histogram[brightness]++
            }
        }
        return histogram
    }

    @Throws(IOException::class)
    fun getBitmapFromContentUri(contentResolver: ContentResolver, imageUri: Uri): Bitmap? {
        val decodedBitmap =
            MediaStore.Images.Media.getBitmap(contentResolver, imageUri) ?: return null
        val orientation = getExifOrientationTag(contentResolver, imageUri)
        var rotationDegrees = 0
        var flipX = false
        var flipY = false
        when (orientation) {
            ExifInterface.ORIENTATION_FLIP_HORIZONTAL -> flipX = true
            ExifInterface.ORIENTATION_ROTATE_90 -> rotationDegrees = 90
            ExifInterface.ORIENTATION_TRANSPOSE -> {
                rotationDegrees = 90
                flipX = true
            }
            ExifInterface.ORIENTATION_ROTATE_180 -> rotationDegrees = 180
            ExifInterface.ORIENTATION_FLIP_VERTICAL -> flipY = true
            ExifInterface.ORIENTATION_ROTATE_270 -> rotationDegrees = 270
            ExifInterface.ORIENTATION_TRANSVERSE -> {
                rotationDegrees = 270
                flipX = true
            }
            ExifInterface.ORIENTATION_UNDEFINED, ExifInterface.ORIENTATION_NORMAL -> {
            }
            else -> {
                // Do Nothing
            }
        }
        return rotateBitmap(decodedBitmap, rotationDegrees, flipX, flipY)
    }

    private fun getExifOrientationTag(resolver: ContentResolver, imageUri: Uri): Int {
        // We only support parsing EXIF orientation tag from local file on the device.
        // See also:
        // https://android-developers.googleblog.com/2016/12/introducing-the-exifinterface-support-library.html
        if (ContentResolver.SCHEME_CONTENT == imageUri.scheme || ContentResolver.SCHEME_FILE == imageUri.scheme) {
            try {
                resolver.openInputStream(imageUri).use { inputStream ->
                    inputStream?.let {
                        val exif = ExifInterface(it)
                        return exif.getAttributeInt(
                            ExifInterface.TAG_ORIENTATION,
                            ExifInterface.ORIENTATION_NORMAL
                        )
                    }
                }
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
        return 0
    }

    fun Bitmap?.autoRecycle() {
        if (this?.isRecycled == false) {
            this.recycle()
        }
    }

    fun Bitmap.cropCenter(): Bitmap {
        val position = abs(width * 0.5 - height * 0.5).toInt()
        val (startX, startY) = if (width > height) {
            position to 0
        } else {
            0 to position
        }
        val size = min(width, height)
        return Bitmap.createBitmap(this, startX, startY, size, size)
    }

    fun Bitmap.split(size: Int): List<Bitmap> {
        val partWidth = width / size
        val partHeight = height / size
        val bitmaps = mutableListOf<Bitmap>()

        for (x in 0 until size) {
            for (y in 0 until size) {
                val bitmap = Bitmap.createBitmap(
                    this,
                    x * partWidth, y * partHeight,
                    partWidth,
                    partHeight
                )
                bitmaps.add(bitmap)
            }
        }
        return bitmaps
    }
}
