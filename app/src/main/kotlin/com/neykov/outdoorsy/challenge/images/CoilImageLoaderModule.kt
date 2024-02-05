package com.neykov.outdoorsy.challenge.images

import android.app.Application
import coil.ImageLoader
import coil.disk.DiskCache
import okhttp3.Call
import org.koin.dsl.module

val CoilImageLoaderModule = module {
    single<ImageLoader> {
        val context = get<Application>()
        val callFactory: Call.Factory = get()
        ImageLoader.Builder(context)
            .callFactory(callFactory)
            .diskCache {
                DiskCache.Builder()
                    .directory(context.cacheDir.resolve("image_cache"))
                    .maxSizeBytes(50 * 1024 * 1024/* 50MB */)
                    .build()
            }
            .build()
    }
}