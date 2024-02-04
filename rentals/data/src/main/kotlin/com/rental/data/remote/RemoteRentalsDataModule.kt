package com.rental.data.remote

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.rental.model.RentalEntry
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.plus
import kotlinx.serialization.modules.polymorphic
import kotlinx.serialization.modules.subclass
import okhttp3.Call
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit

private val Outdoorly = named("Outdoorly")

val RemoteRentalsDataModule = module {
    single<RentalsApi> {
        get<Retrofit>(Outdoorly).create(RentalsApi::class.java)
    }

    single<Retrofit>(Outdoorly) {
        val serializationFormat = get<Json>(Outdoorly)
        val converterFactory =
            serializationFormat.asConverterFactory("application/json".toMediaType())
        Retrofit.Builder()
            .baseUrl("https://search.outdoorsy.com/")
            .addConverterFactory(converterFactory)
            .callFactory(get())
            .build()
    }

    single(Outdoorly) {
        Json {
            // Map of `RentalEntry` to be serialized as `RemoteRentalEntry`.
            this.serializersModule += SerializersModule {
                polymorphic(RentalEntry::class) {
                    subclass(RemoteRentalEntry::class)
                }
            }
        }
    }

    single<Call.Factory> {
        OkHttpClient.Builder()
            .cache(getOrNull()/* Optional cache configured outside */)
            .build()
    }
}