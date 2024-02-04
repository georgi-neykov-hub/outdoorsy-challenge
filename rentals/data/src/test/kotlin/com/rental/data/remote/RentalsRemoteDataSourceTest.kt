package com.rental.data.remote

import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.times
import com.nhaarman.mockitokotlin2.verifyBlocking
import com.rental.model.RentalEntry
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertSame
import org.junit.Before
import org.junit.Test

class RentalsRemoteDataSourceTest {

    private lateinit var dataSource: RemoteRentalsDataSource
    private lateinit var rentalsApi: RentalsApi

    @Before
    fun setUp() {
        rentalsApi = mock()
        dataSource = RemoteRentalsDataSource(rentalsApi)
    }

    @Test
    fun invoke_Delegates_To_RentalsApi_listRentals() = runTest {
        val query = "Something"
        val pageLimit = 12
        val pageOffset = 5
        val address = "North Pole"
        val expectedEntries = mutableListOf<RentalEntry>()

        val actualEntries = dataSource.invoke(pageLimit, pageOffset, address, query)

        assertSame(expectedEntries, actualEntries)
        verifyBlocking(rentalsApi, times(1)) { listRentals(pageLimit, pageOffset, address, query) }
    }
}