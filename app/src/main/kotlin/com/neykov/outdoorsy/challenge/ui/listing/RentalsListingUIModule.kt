package com.neykov.outdoorsy.challenge.ui.listing

import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

val RentalsListingUIModule = module {
    viewModelOf(::RentalsListingViewModel)
}