package com.example.honorcup

import android.graphics.Bitmap
import android.graphics.Canvas

object CombinerImages {

    fun combineHorizontally(
        c: Bitmap,
        s: Bitmap
    ): Bitmap? {
        //https://stackoverflow.com/questions/4863518/combining-two-bitmap-image-side-by-side
        //c + s
        val cs: Bitmap?

        val width: Int = c.width + s.width
        val height = c.height

        cs = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)

        val comboImage = Canvas(cs)

        comboImage.drawBitmap(c, 0f, 0f, null)
        comboImage.drawBitmap(s, c.width.toFloat(), 0f, null)

        return cs
    }

    fun combineVertically(
        c: Bitmap,
        s: Bitmap
    ): Bitmap? {
        //https://stackoverflow.com/questions/4863518/combining-two-bitmap-image-side-by-side
        //c + s
        val cs: Bitmap?

        val width: Int = c.width
        val height = c.height + s.height

        cs = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)

        val comboImage = Canvas(cs)

        comboImage.drawBitmap(c, 0f, 0f, null)
        comboImage.drawBitmap(s, 0f, c.height.toFloat(), null)

        return cs
    }

}