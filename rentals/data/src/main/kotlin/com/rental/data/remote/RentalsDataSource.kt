package com.rental.data.remote

import com.rental.model.RentalEntry

/**
 * A contract for loading [RentalEntry] with support for optional filters and pagination or results.
 * @see
 */
internal interface RentalsDataSource {
    suspend operator fun invoke(
        pageLimit: Int? = null,
        pageOffset: Int? = null,
        address: String? = null,
        keywords: Collection<String>? = null
    ): List<RentalEntry>
}