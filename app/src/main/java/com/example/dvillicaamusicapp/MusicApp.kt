package com.example.dvillicaamusicapp

import android.content.Context
import coil.ImageLoader
import com.example.dvillicaamusicapp.data.RetrofitInstance

object ImageLoaderProvider {
    private var imageLoader: ImageLoader? = null

    fun get(context: Context): ImageLoader {
        return imageLoader ?: ImageLoader.Builder(context.applicationContext)
            .okHttpClient(RetrofitInstance.okHttpClient)
            .crossfade(true)
            .build()
            .also { imageLoader = it }
    }
}
