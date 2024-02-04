package com.rental.domain

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.rental.data.RentalsRepository
import com.rental.model.RentalEntry
import kotlinx.coroutines.CancellationException
import kotlin.math.roundToInt

fun RentalsRepository.newListPagingSource(
    address: String? = null,
    keywords: Collection<String>? = null
): PagingSource<out Any, RentalEntry> {
    return RentalEntryPagingSource(repository = this, address = address, keywords = keywords)
}

internal class RentalEntryPagingSource(
    private val repository: RentalsRepository,
    private val address: String?,
    private val keywords: Collection<String>?
) : PagingSource<Int, RentalEntry>() {
    override fun getRefreshKey(state: PagingState<Int, RentalEntry>): Int {
        return when (val anchorPosition = state.anchorPosition) {
            null -> 0 // Begin at the start of data.
            else -> {
                // Calculate a start position that will result in the current anchor
                // position being in the middle of a loaded chink (or at the start of data).
                (anchorPosition - (state.config.initialLoadSize * .5f).roundToInt()).coerceAtLeast(0)
            }
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, RentalEntry> {
        val positionOffset = params.key ?: 0
        val pageSize = params.loadSize

        val loadOffset = if (positionOffset > 0) positionOffset - 1 else positionOffset
        val loadSize = if (positionOffset != 0) pageSize + 1 else pageSize
        return try {
            val data = repository.list(loadSize, loadOffset, address, keywords)
            val prevKey = positionOffset.takeIf { it != 0 }
            val nextKey = if (data.size == pageSize) positionOffset + data.size else null
            LoadResult.Page(data = data, prevKey = prevKey, nextKey = nextKey)
        } catch (e: Exception) {
            when (e) {
                is CancellationException -> throw e
                else -> LoadResult.Error(e)
            }
        }
    }
}

