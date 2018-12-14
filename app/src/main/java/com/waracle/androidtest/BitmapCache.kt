package com.waracle.androidtest

import android.graphics.Bitmap
import android.util.LruCache


object BitmapCache : LruCache<String, Bitmap>((Runtime.getRuntime().maxMemory() / 8192).toInt()) {
    override fun sizeOf(key: String?, value: Bitmap): Int = value.byteCount / 1024
}