package com.rental.data

import com.rental.data.remote.RentalsDataSource
import com.rental.model.RentalEntry

/**
 * An entry point to business logic related to rentals
 *
 * @see RentalEntry
 */
interface RentalsRepository {
    /**
     * Load [RentalEntry] elements based on optional filters
     *
     * @param pageLimit optional, number of elements per page,(enables pagination of results)
     * @param pageOffset optional, the zero-based index of the first element to be loaded
     * @param address optional, filter results close to a location given via an address
     * @param keywords optional, filter results by keywords
     * @return results as a [List<RentalEntry>]
     * @throws IllegalArgumentException when [pageLimit] != `null`, but is < 0
     * @throws IllegalArgumentException when [pageLimit] != `null`, but [pageOffset] == `null`
     * @throws IllegalArgumentException when [pageOffset] != `null`, but is < 0
     *
     */
    suspend fun list(
        pageLimit: Int? = null,
        pageOffset: Int? = null,
        address: String? = null,
        keywords: Collection<String>? = null
    ): List<RentalEntry>
}

/**
 * A [RentalsRepository] implementation delegating data loads to a [RentalsDataSource].
 *
 * @property rentalsDataSource to be used for rentals listing
 * @see RentalsRepository
 * @see RentalsDataSource
 */
internal class DefaultRentalsRepository(private val rentalsDataSource: RentalsDataSource) :
    RentalsRepository {
    override suspend fun list(
        pageLimit: Int?,
        pageOffset: Int?,
        address: String?,
        keywords: Collection<String>?
    ): List<RentalEntry> = rentalsDataSource.invoke(pageLimit, pageOffset, address, keywords)
}