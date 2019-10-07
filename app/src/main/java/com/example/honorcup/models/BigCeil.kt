package com.example.honorcup.models

import android.graphics.Bitmap
import android.graphics.Color
import com.example.honorcup.CIELab
import com.example.honorcup.CombinerImages
import java.util.*
import kotlin.math.min
import kotlin.math.sqrt

data class Side(var left: Double?, var right: Double?, var top: Double?, var bottom: Double?)

//ceilSz : 64 32 16
class BigCeil(private val ceilSz: Int, bitmap: Bitmap) {
    private val arr: MutableList<MutableList<Ceil>> = mutableListOf()
    private val used: MutableList<MutableList<Boolean>> = mutableListOf()
    var result: MutableList<MutableList<Ceil>> = mutableListOf()
    private var dist: MutableList<MutableList<Side>> = mutableListOf()
    private val sz = N / ceilSz

    init {
        for (i in 0 until sz) {
            arr.add(mutableListOf())
            used.add(mutableListOf())
            result.add(mutableListOf())
            for (j in 0 until sz) {
                arr[i].add(Ceil(null, -1))
                used[i].add(false)
                result[i].add(Ceil(null, -1))
            }
        }

        for (i in 0 until sz) {
            for (j in 0 until sz) {

                val cropBitmap = Bitmap.createBitmap(
                    bitmap, j * ceilSz, i * ceilSz, ceilSz, ceilSz
                )

                arr[i][j] = Ceil(cropBitmap, i * sz + j)
            }
        }
    }

    fun getResultCeil(): Bitmap {
        var resultBitmap: Bitmap? = null
        for (i in 0 until sz) {
            var horBitmap: Bitmap? = null
            for (j in 0 until sz) {
                horBitmap = horBitmap?.let {
                    CombinerImages.combineHorizontally(it, result[i][j].bitmap!!)
                } ?: result[i][j].bitmap
            }
            resultBitmap = if (resultBitmap == null) {
                horBitmap
            } else {
                CombinerImages.combineVertically(resultBitmap, horBitmap!!)
            }
        }
        return resultBitmap!!
    }

    fun findBestPermutation() {
        var res: Double = Double.MAX_VALUE
        precalcDistances()

        for (i in 0 until sz) {
            for (j in 0 until sz) {

                val cur = findResult(arr[i][j])

                println("Res for : $i $j : ${cur.first}")
                if (cur.first < res) {
                    res = cur.first
                    result = cur.second
                }
                //println("Res $res")
            }
        }

        println("Res $res")
    }

    private fun precalcDistances() {

        for (i in 0 until sz * sz) {
            dist.add(mutableListOf())
            for (j in 0 until sz * sz) {
                dist[i].add(Side(null, null, null, null))
            }
        }

        for (i in 0 until sz) {
            for (j in 0 until sz) {
                //println("Main $i $j")
                for (i1 in 0 until sz) {
                    for (j1 in 0 until sz) {
                        dist[arr[i][j].ind][arr[i1][j1].ind].left =
                            connectHorizontally(arr[i1][j1].bitmap!!, arr[i][j].bitmap!!)
                        dist[arr[i][j].ind][arr[i1][j1].ind].right =
                            connectHorizontally(arr[i][j].bitmap!!, arr[i1][j1].bitmap!!)
                        dist[arr[i][j].ind][arr[i1][j1].ind].top =
                            connectVertically(arr[i1][j1].bitmap!!, arr[i][j].bitmap!!)
                        dist[arr[i][j].ind][arr[i1][j1].ind].bottom =
                            connectVertically(arr[i][j].bitmap!!, arr[i1][j1].bitmap!!)
                    }
                }

            }
        }
    }

    data class Node(val i: Int, val j: Int)

    private fun findResult(stCeil: Ceil): Pair<Double, MutableList<MutableList<Ceil>>> {
        val tempRes: MutableList<MutableList<Ceil>> = mutableListOf()
        var res: Double = 0.toDouble()

        val set: TreeSet<Ceil> = sortedSetOf()

        for (i in 0 until sz) {
            tempRes.add(mutableListOf())
            for (j in 0 until sz) {
                tempRes[i].add(Ceil(null, -1))
                used[i][j] = false
                set.add(arr[i][j])
            }
        }

        val stI = sz / 2
        val stJ = sz / 2

        //println("ST : $stI $stJ")

        val queue: Queue<Node> = LinkedList()

        queue.add(Node(stI, stJ))
        set.remove(stCeil)
        used[stI][stJ] = true
        tempRes[stI][stJ] = stCeil

        /*
        var sum = Double.MAX_VALUE
        var bestCeil: Ceil? = null

        for (el in set) {
            var cursum: Double = 0.toDouble()
            cursum = connectVertically(stCeil.bitmap!!, el.bitmap!!)
            if (cursum < sum) {
                sum = cursum
                bestCeil = el
            }
        }
        println("Best Ceil for ${stCeil.ind} is ${bestCeil!!.ind}")
        */

        while (queue.isNotEmpty()) {
            val i = queue.element().i
            val j = queue.element().j
            //println("${queue.element().sum}")

            queue.remove()

            //println("SzSet: ${set.size} Set: ${set.map { it.ind }}")
            //println("Point : $i $j")
            for (cf1 in -1 until 2) {
                for (cf2 in -1 until 2) {
                    if (cf1 * cf1 + cf2 * cf2 == 1) {
                        val ni = i + cf1
                        val nj = j + cf2
                        if (ni >= 0 && ni < sz && nj >= 0 && nj < sz && !used[ni][nj]) {
                            val left = if (nj - 1 >= 0) tempRes[ni][nj - 1].ind else -1
                            val right = if (nj + 1 < sz) tempRes[ni][nj + 1].ind else -1
                            val top = if (ni - 1 >= 0) tempRes[ni - 1][nj].ind else -1
                            val bottom = if (ni + 1 < sz) tempRes[ni + 1][nj].ind else -1

                            var sum = Double.MAX_VALUE
                            var bestCeil: Ceil? = null
                            for (el in set) {
                                var cursum: Double = 0.toDouble()
                                if (left != -1) {
                                    cursum += dist[el.ind][left].left!!
                                }
                                if (right != -1) {
                                    cursum += dist[el.ind][right].right!!

                                    //connectHorizontally(el.bitmap!!, right)
                                }
                                if (bottom != -1) {
                                    cursum += dist[el.ind][bottom].bottom!!
                                    //connectVertically(el.bitmap!!, bottom)
                                }
                                if (top != -1) {
                                    cursum += dist[el.ind][top].top!!
                                    //cursum += connectVertically(top, el.bitmap!!)
                                }
                                if (cursum < sum) {
                                    sum = cursum
                                    bestCeil = el
                                }
                            }

                            tempRes[ni][nj] = bestCeil!!
                            used[ni][nj] = true
                            set.remove(bestCeil)
                            queue.add(Node(ni, nj))
                        }
                    }
                }
            }

        }

        for (i in 0 until sz) {
            for (j in 0 until sz) {
                if (i - 1 >= 0) {
                    res += connectVertically(
                        tempRes[i - 1][j].bitmap!!,
                        tempRes[i][j].bitmap!!
                    )
                }
                if (j - 1 >= 0) {
                    res += connectHorizontally(
                        tempRes[i][j - 1].bitmap!!,
                        tempRes[i][j].bitmap!!
                    )
                }
            }
        }

        return Pair(res, tempRes)
    }

    private fun connectHorizontally(left: Bitmap, right: Bitmap): Double {
        var ans: Double = 0.toDouble()
        for (i in 0 until ceilSz) {
            val pixelLeft = left.getPixel(ceilSz - 1, i)
            val pixelRight = right.getPixel(0, i)
            ans += comparePixels(pixelLeft, pixelRight)
        }
        return ans
    }

    private fun connectVertically(top: Bitmap, bottom: Bitmap): Double {
        var ans: Double = 0.toDouble()
        for (i in 0 until ceilSz) {
            val pixelTop = top.getPixel(i, ceilSz - 1)
            val pixelBottom = bottom.getPixel(i, 0)
            ans += comparePixels(pixelTop, pixelBottom)
        }
        return ans
    }

    private fun comparePixels(pixel1: Int, pixel2: Int): Double {
        val r1 = Color.red(pixel1)
        val g1 = Color.green(pixel1)
        val b1 = Color.blue(pixel1)

        val r2 = Color.red(pixel2)
        val g2 = Color.green(pixel2)
        val b2 = Color.blue(pixel2)

        val cur1: DoubleArray = CIELab().rgbToLab(r1, g1, b1)
        val cur2: DoubleArray = CIELab().rgbToLab(r2, g2, b2)

        return sqrt(
            (cur2[0] - cur1[0]) * (cur2[0] - cur1[0]) +
                    (cur2[1] - cur1[1]) * (cur2[1] - cur1[1]) +
                    (cur2[2] - cur1[2]) * (cur2[2] - cur1[2])
        )
    }

    companion object {
        const val N = 512
    }
}
