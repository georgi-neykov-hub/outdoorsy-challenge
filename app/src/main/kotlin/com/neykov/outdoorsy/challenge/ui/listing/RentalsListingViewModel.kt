package com.neykov.outdoorsy.challenge.ui.listing

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.rental.data.RentalsRepository
import com.rental.domain.newListPagingSource
import com.rental.domain.splitToKeywords
import com.rental.model.RentalEntry
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.onStart
import kotlin.time.Duration.Companion.milliseconds

/**
 * ViewModel for managing the state of the rentals listing screen
 * @see RentalsListingScreen
 */
@OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)
internal class RentalsListingViewModel(private val rentalsRepository: RentalsRepository) :
    ViewModel() {

    companion object {
        const val DefaultPageSize = 50
        private val TextInputDebouncePeriod = 500.milliseconds
    }

    /**
     * Container for search text state
     *
     * [pagingDataStream] will emit new [PagingData] reflecting value changes to this flow.
     * @see [pagingDataStream]
     */
    val searchTextState = MutableStateFlow("")

    /**
     * Container for filtering by a given address, town, county,state or country
     *
     * [pagingDataStream] will emit new [PagingData] reflecting value changes to this flow.
     * @see [pagingDataStream]
     */
    //TODO: Remove hardcoded address used to load non-empty listing results.
    val addressState = MutableStateFlow("Yellowstone park")

    /**
     * A container for the preffred result page size.
     * Default value is [DefaultPageSize]
     */
    val pageSize = MutableStateFlow(DefaultPageSize)

    /**
     * A [Flow]-based stream of [PagingData] that can be used to load
     *  [RentalEntry] results in an efficient manner.
     *  The stream will emit a new [PagingData] isntance upon each change of
     *  either [searchTextState], [addressState] or [pageSize]
     *
     *  @see PagingData
     *  @see Pager
     */
    val pagingDataStream: Flow<PagingData<RentalEntry>>

    init {
        // Convert the raw text to a tokenized collections of keywords
        // Debounce to reduce stream emissions during rapid user input
        val keywordsStream = searchTextState
            .debounce(TextInputDebouncePeriod)
            .onStart { emit(searchTextState.value) }
            .mapLatest(String::splitToKeywords)

        // Debounce to reduce stream emissions during rapid user input
        val addressStream = addressState
            .debounce(TextInputDebouncePeriod)
            .onStart { emit(addressState.value) }

        // Combine all states to produce a fresh `Pager` instance on each change,
        pagingDataStream = combine(
            keywordsStream,
            addressStream,
            pageSize
        ) { keywords, address, pageSize ->
            Pager(
                config = PagingConfig(pageSize = pageSize, enablePlaceholders = false),
                initialKey = null
            ) {
                rentalsRepository.newListPagingSource(
                    address = address,
                    keywords = keywords
                )
            }
        }
            // After a switch to a new `Pager` also switch the `Flow<PagingData<RentalEntry>>` being collected.
            .flatMapLatest { it.flow }
            .cachedIn(viewModelScope)
    }

}