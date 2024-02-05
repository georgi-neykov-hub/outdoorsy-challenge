package com.neykov.outdoorsy.challenge

import com.neykov.outdoorsy.challenge.images.CoilImageLoaderModule
import com.neykov.outdoorsy.challenge.networking.NetworkingModule
import com.neykov.outdoorsy.challenge.ui.listing.RentalsListingUIModule
import com.rental.data.RentalsDataModule
import org.koin.core.KoinApplication

/**
 *  A helper function to add all necessary KOin modules to Koin's graph
 *
 *  @see OutdoorsyChallengeApplication
 */
fun KoinApplication.addAllModules() {
    modules(
        RentalsDataModule,
        RentalsListingUIModule,
        CoilImageLoaderModule,
        NetworkingModule
    )
}