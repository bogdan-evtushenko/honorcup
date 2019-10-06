package com.example.honorcup.models

import android.graphics.Bitmap

data class Ceil(val bitmap: Bitmap?, val ind: Int) : Comparable<Ceil> {

    override operator fun compareTo(other: Ceil): Int {
        if (this.ind > other.ind) return 1
        if (this.ind < other.ind) return -1
        return 0
    }

}