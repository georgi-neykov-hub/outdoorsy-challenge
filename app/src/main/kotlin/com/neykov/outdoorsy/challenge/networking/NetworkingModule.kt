package com.neykov.outdoorsy.challenge.networking

import android.app.Application
import okhttp3.Cache
import okhttp3.Call
import okhttp3.OkHttpClient
import org.koin.dsl.module

val NetworkingModule = module {
    single<Call.Factory> {
        val context = get<Application>()
        OkHttpClient.Builder()
            .cache(Cache(context.cacheDir.resolve("http_cache"), 50 * 1024 * 1024/* 50MB */))
            .build()
    }
}