package com.rental.data

import com.rental.data.remote.RemoteRentalsDataModule
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module


val RentalsDataModule = module {
    includes(RemoteRentalsDataModule)

    singleOf(::DefaultRentalsRepository) {
        bind<RentalsRepository>()
    }
}