package com.example.squadhub

import android.graphics.*
import com.squareup.picasso.Transformation

class RoundedCornersTransformation(
    private val radius: Float,
    private val targetWidth: Int,
    private val targetHeight: Int
) : Transformation {

    override fun transform(source: Bitmap): Bitmap {
        // Calcula o scale para o efeito centerCrop
        val scale: Float
        val dx: Float
        val dy: Float

        if (source.width * targetHeight > targetWidth * source.height) {
            scale = targetHeight.toFloat() / source.height.toFloat()
            dx = (targetWidth - source.width * scale) / 2
            dy = 0f
        } else {
            scale = targetWidth.toFloat() / source.width.toFloat()
            dx = 0f
            dy = (targetHeight - source.height * scale) / 2
        }

        val matrix = Matrix().apply {
            setScale(scale, scale)
            postTranslate(dx, dy)
        }

        // Cria um bitmap redimensionado para centerCrop
        val croppedBitmap = Bitmap.createBitmap(targetWidth, targetHeight, Bitmap.Config.ARGB_8888)
        val croppedCanvas = Canvas(croppedBitmap)
        val paint = Paint(Paint.ANTI_ALIAS_FLAG)
        croppedCanvas.drawBitmap(source, matrix, paint)

        // Agora aplica os cantos arredondados
        val output = Bitmap.createBitmap(targetWidth, targetHeight, Bitmap.Config.ARGB_8888)
        val roundedCanvas = Canvas(output)
        val rectF = RectF(0f, 0f, targetWidth.toFloat(), targetHeight.toFloat())

        val roundedPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        roundedCanvas.drawRoundRect(rectF, radius, radius, roundedPaint)

        roundedPaint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
        roundedCanvas.drawBitmap(croppedBitmap, 0f, 0f, roundedPaint)

        // Liberta os recursos
        source.recycle()
        croppedBitmap.recycle()

        return output
    }

    override fun key(): String {
        return "rounded_corners_with_center_crop_${radius}_${targetWidth}x$targetHeight"
    }
}