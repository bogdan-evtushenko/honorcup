package com.example.honorcup

import androidx.core.graphics.ColorUtils

//https://stackoverflow.com/questions/49114582/rgb-to-xyz-and-then-xyz-to-lab-in-androidjava
//http://colormine.org/delta-e-calculator
class CIELab {
    fun rgbToLab(R: Int, G: Int, B: Int): DoubleArray {
        val lab1 = DoubleArray(3)
        var r: Double
        var g: Double
        var b: Double
        val X: Double
        val Y: Double
        val Z: Double
        var xr: Double
        var yr: Double
        var zr: Double
        ColorUtils.RGBToLAB(R, G, B, lab1)
        //Core.absdiff();

        // D65/2°
        val Xr = 95.047
        val Yr = 100.0
        val Zr = 108.883


        // --------- RGB to XYZ ---------//

        r = R / 255.0
        g = G / 255.0
        b = B / 255.0

        if (r > 0.04045)
            r = Math.pow((r + 0.055) / 1.055, 2.4)
        else
            r = r / 12.92

        if (g > 0.04045)
            g = Math.pow((g + 0.055) / 1.055, 2.4)
        else
            g = g / 12.92

        if (b > 0.04045)
            b = Math.pow((b + 0.055) / 1.055, 2.4)
        else
            b = b / 12.92

        r *= 100.0
        g *= 100.0
        b *= 100.0
        //Log.d(TAG, "R:$r G:$g B:$b")
        X = 0.4124 * r + 0.3576 * g + 0.1805 * b
        Y = 0.2126 * r + 0.7152 * g + 0.0722 * b
        Z = 0.0193 * r + 0.1192 * g + 0.9505 * b


        // --------- XYZ to Lab --------- //

        xr = X / Xr
        yr = Y / Yr
        zr = Z / Zr

        if (xr > 0.008856)
            xr = Math.pow(xr, 1 / 3.0).toFloat().toDouble()
        else
            xr = (7.787 * xr + 16 / 116.0).toFloat().toDouble()

        if (yr > 0.008856)
            yr = Math.pow(yr, 1 / 3.0).toFloat().toDouble()
        else
            yr = (7.787 * yr + 16 / 116.0).toFloat().toDouble()

        if (zr > 0.008856)
            zr = Math.pow(zr, 1 / 3.0).toFloat().toDouble()
        else
            zr = (7.787 * zr + 16 / 116.0).toFloat().toDouble()


        val lab = DoubleArray(3)

        lab[0] = 116 * yr - 16
        lab[1] = 500 * (xr - yr)
        lab[2] = 200 * (yr - zr)

        return lab

    }
}