package com.example.honorcup

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.honorcup.models.BigCeil
import kotlinx.android.synthetic.main.activity_main.*
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val good = listOf(
            3002,
            3005,
            3016,
            3018,
            3029,
            3030,
            3035,
            3036,
            3037,
            3042,
            3045,
            3054,
            3055,
            3056,
            3057,
            3064,
            3070,
            3073,
            3075,
            3076,
            3077,
            3084,
            3089,
            3090,
            3091,
            3094,
            3098,
            3099,
            3104,
            3106,
            3109,
            3111,
            3112,
            3115,
            3117,
            3125,
            3127,
            3141,
            3143,
            3148,
            3151,
            3161,
            3167,
            3168,
            3173,
            3174,
            3178,
            3179,
            3180,
            3187,
            3188,
            3189,
            3190,
            3194,
            3195,
            3198,
            3199,
            3200,
            3205,
            3207,
            3209,
            3210,
            3217,
            3218,
            3219,
            3222,
            3227,
            3229,
            3231,
            3234,
            3238,
            3239,
            3241,
            3244,
            3245,
            3246,
            3250,
            3251,
            3253,
            3257,
            3276,
            3277,
            3288,
            3295,
            3299
        )

        var all = (3000..3299).toList()

        all = all.minus(good)
        println("Size : ${all.size}")
        println(all)

        Thread {
            for (sc in 0 until 5) {
                val i = all[sc]
                println("i : ${formatPngName(i, true)}")

                val id = resources
                    .getIdentifier("photo32_${formatPngName(i, false)}", "drawable", packageName)

                val bitmap = BitmapFactory.decodeResource(resources, id)

                println("widhts: ${bitmap.width} ${bitmap.height}")
                //512 512

                val argbBitmap = bitmap.getPixel(0, 1)

                val redValue = Color.red(argbBitmap)
                val blueValue = Color.blue(argbBitmap)
                val greenValue = Color.green(argbBitmap)

                println("R: $redValue B: $blueValue G: $greenValue")


                var ans = Double.MAX_VALUE
                var prans = ""
                var ansResultCeil: Bitmap? = null
                val bigCeil = BigCeil(32, bitmap)
                bigCeil.precalcDistances()
                for (cnt in 0 until 3) {
                    bigCeil.findBestPermutation()
                    val resultCeil = bigCeil.getResultCeil()

                    val curans = formatPngName(i, true) + '\n'
                    var curans1 = ""

                    for (i1 in 0 until bigCeil.result.size) {
                        for (j1 in 0 until bigCeil.result[i1].size) {
                            curans1 += bigCeil.result[i1][j1].ind.toString() + " "
                        }
                    }

                    curans1 += '\n'
                    if (bigCeil.res < ans) {
                        ans = bigCeil.res
                        prans = curans + curans1
                        ansResultCeil = resultCeil

                    }
                }
                showImage(ansResultCeil!!)
                save(prans, ansResultCeil, i)
            }
        }.start()

    }

    @SuppressLint("SetTextI18n")
    private fun save(text: String, bitmap: Bitmap, ind: Int) {
        var fos: FileOutputStream? = null
        var fbos: FileOutputStream? = null
        try {
            fos = openFileOutput(FILE_NAME, Context.MODE_APPEND)
            fos?.write(text.toByteArray())
            fbos = openFileOutput("photo32_$ind", Context.MODE_PRIVATE)
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fbos)

            runOnUiThread {
                tvTitle.text = "Uploaded : $ind"
            }
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        } finally {
            if (fos != null) {
                try {
                    fos.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                }

            }
            if (fbos != null) {
                try {
                    fbos.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                }

            }
        }
    }

    private fun formatPngName(num: Int, withPng: Boolean): String {
        val res = when {
            num < 10 -> "000$num"
            num < 100 -> "00$num"
            num < 1000 -> "0$num"
            else -> "$num"
        }
        return if (withPng) "$res.png" else res
    }

    private fun showImage(bitmap: Bitmap) {
        runOnUiThread { ivMain.setImageBitmap(bitmap) }
    }

    companion object {
        const val FILE_NAME = "answers_for_32_ans.txt"
    }

}
