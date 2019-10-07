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

        Thread {
            for (i in 3300 until 3304) {

                println("i : ${formatPngName(i, true)}")

                val id = resources
                    .getIdentifier("photo64_${formatPngName(i, false)}", "drawable", packageName)

                val bitmap = BitmapFactory.decodeResource(resources, id)

                println("widhts: ${bitmap.width} ${bitmap.height}")
                //512 512

                val argbBitmap = bitmap.getPixel(0, 1)

                val redValue = Color.red(argbBitmap)
                val blueValue = Color.blue(argbBitmap)
                val greenValue = Color.green(argbBitmap)

                println("R: $redValue B: $blueValue G: $greenValue")

                val bigCeil = BigCeil(64, bitmap)

                bigCeil.findBestPermutation()
                val resultCeil = bigCeil.getResultCeil()

                println("Result ceil : $resultCeil")

                showImage(resultCeil)

                val ans = formatPngName(i, true) + '\n'
                var ans1 = ""

                for (i1 in 0 until bigCeil.result.size) {
                    for (j1 in 0 until bigCeil.result[i1].size) {
                        ans1 += bigCeil.result[i1][j1].ind.toString() + " "
                    }
                }

                ans1 += '\n'
                save(ans + ans1, resultCeil, i)
            }
        }.start()

        /*Thread {
            for (i in 3150 until 3300) {

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

                val bigCeil = BigCeil(32, bitmap)

                bigCeil.findBestPermutation()
                val resultCeil = bigCeil.getResultCeil()

                println("Result ceil : $resultCeil")

                showImage(resultCeil)

                val ans = formatPngName(i, true) + '\n'
                var ans1 = ""

                for (i1 in 0 until bigCeil.result.size) {
                    for (j1 in 0 until bigCeil.result[i1].size) {
                        ans1 += bigCeil.result[i1][j1].ind.toString() + " "
                    }
                }

                ans1 += '\n'
                save(ans + ans1, resultCeil, i)
            }
        }.start()
         */

    }

    @SuppressLint("SetTextI18n")
    private fun save(text: String, bitmap: Bitmap, ind: Int) {
        var fos: FileOutputStream? = null
        var fbos: FileOutputStream? = null
        try {
            fos = openFileOutput(FILE_NAME, Context.MODE_APPEND)
            fos?.write(text.toByteArray())
            fbos = openFileOutput("photo64_$ind", Context.MODE_PRIVATE)
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
        const val FILE_NAME = "answers_for_64_ans.txt"
    }

}
