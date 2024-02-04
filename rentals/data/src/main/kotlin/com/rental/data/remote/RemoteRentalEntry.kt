package com.rental.data.remote

import com.rental.model.RentalEntry
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class RemoteRentalEntry(
    @SerialName("id") override val id: Long,
    @SerialName("name") override val name: String,
    @SerialName("primary_image_url") override val imageUrl: String?
) : RentalEntry