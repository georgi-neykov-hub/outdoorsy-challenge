package com.rental.data

import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.times
import com.nhaarman.mockitokotlin2.verifyBlocking
import com.rental.data.remote.RentalsDataSource
import com.rental.model.RentalEntry
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertSame
import org.junit.Before
import org.junit.Test

class DefaultRentalsRepositoryTest {

    private lateinit var repository: DefaultRentalsRepository
    private lateinit var dataSource: RentalsDataSource

    @Before
    fun setUp() {
        dataSource = mock()
        repository = DefaultRentalsRepository(dataSource)
    }

    @Test
    fun list_Delegates_Execution_To_RentalsDataSource() = runTest {
        val query = "Something"
        val pageLimit = 12
        val pageOffset = 5
        val address = "North Pole"
        val expectedEntries = mutableListOf<RentalEntry>()

        val actualEntries = repository.list(pageLimit, pageOffset, address, query)

        assertSame(expectedEntries, actualEntries)
        verifyBlocking(dataSource, times(1)) { invoke(pageLimit, pageOffset, address, query) }
    }

    @Test
    fun list_Throws_On_Negative_PageLimit_Parameter() = runTest {
        assertThrowsOnInvalidPageLimitParameter(-1) { runBlocking { repository.list(pageLimit = it) } }
    }

    @Test
    fun list_Throws_On_NonNull_PageLimit_But_Null_PageOffset_Parameter() = runTest {
        assertThrowsOnInvalidPageOffsetParameter(
            20,
            null
        ) { limit, offset ->
            runBlocking {
                repository.list(
                    pageLimit = limit,
                    pageOffset = offset
                )
            }
        }
    }

    @Test
    fun list_Throws_On_NonNull_PageLimit_And_Negative_PageOffset_Parameter() = runTest {
        assertThrowsOnInvalidPageOffsetParameter(
            20,
            -1
        ) { limit, offset ->
            runBlocking {
                repository.list(
                    pageLimit = limit,
                    pageOffset = offset
                )
            }
        }
    }
}