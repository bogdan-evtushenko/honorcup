package com.example.honorcup

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Bundle
import android.widget.Toast
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
            for (i in 1200 until 1800) {

                println("i : $i.png")

                val id = resources
                    .getIdentifier("photo$i", "drawable", packageName)

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

                val ans = formatPngName(i) + '\n'
                var ans1 = ""

                for (i1 in 0 until bigCeil.result.size) {
                    for (j1 in 0 until bigCeil.result[i1].size) {
                        ans1 += bigCeil.result[i1][j1].ind.toString() + " "
                    }
                }

                ans1 += '\n'
                save(ans + ans1)
            }
        }.start()
    }

    private fun save(text: String) {
        var fos: FileOutputStream? = null

        try {
            fos = openFileOutput(FILE_NAME, Context.MODE_APPEND)
            fos?.write(text.toByteArray())

            runOnUiThread {
                Toast.makeText(
                    this, "Saved to $filesDir/$FILE_NAME",
                    Toast.LENGTH_LONG
                ).show()
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
        }
    }

    private fun formatPngName(num: Int): String {
        return "$num.png"
    }

    private fun showImage(bitmap: Bitmap) {
        runOnUiThread { ivMain.setImageBitmap(bitmap) }
    }

    companion object {
        const val FILE_NAME = "answers_for_64_train.txt"
    }

}
