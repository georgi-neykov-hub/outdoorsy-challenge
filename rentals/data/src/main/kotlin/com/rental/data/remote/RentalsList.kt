package com.rental.data.remote

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class RentalsListResult(@SerialName("data") val entries: List<RemoteRentalEntry>)

