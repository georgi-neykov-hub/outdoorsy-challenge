package com.rental.data.remote

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit

private val OutdoorlyAPI = named("Outdoorly API")

val RemoteRentalsDataModule = module {
    singleOf(::RemoteRentalsDataSource) {
        bind<RentalsDataSource>()
    }

    single<RentalsApi> {
        get<Retrofit>(OutdoorlyAPI).create(RentalsApi::class.java)
    }

    single<Retrofit>(OutdoorlyAPI) {
        val serializationFormat = get<Json>(OutdoorlyAPI)
        val converterFactory =
            serializationFormat.asConverterFactory("application/json".toMediaType())
        Retrofit.Builder()
            .baseUrl("https://search.outdoorsy.com/")
            .addConverterFactory(converterFactory)
            .callFactory(get())
            .build()
    }

    single(OutdoorlyAPI) {
        Json {
            // Keep to `true` to allow skipping unwanted data
            ignoreUnknownKeys = true
        }
    }
}