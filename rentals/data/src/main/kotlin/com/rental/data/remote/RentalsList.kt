package com.rental.data.remote

import com.rental.model.RentalEntry
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class RentalsListResult(@SerialName("data") val entries: List<RentalEntry>)

