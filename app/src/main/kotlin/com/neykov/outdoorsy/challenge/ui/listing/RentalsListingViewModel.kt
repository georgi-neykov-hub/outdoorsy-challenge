package com.neykov.outdoorsy.challenge.ui.listing

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.rental.data.RentalsRepository
import com.rental.domain.newListPagingSource
import com.rental.domain.splitToKeywords
import com.rental.model.RentalEntry
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlin.time.Duration.Companion.milliseconds

@OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)
internal class RentalsListingViewModel(private val rentalsRepository: RentalsRepository) :
    ViewModel() {

    companion object {
        const val DefaultPageSize = 50
        private val TextInputDebouncePeriod = 250.milliseconds
    }

    val queryState = MutableStateFlow("")
    val addressState = MutableStateFlow("")
    val pageSize = MutableStateFlow(DefaultPageSize)

    val pagingDataStream: Flow<PagingData<RentalEntry>>

    init {
        // Convert the raw text to a tokenized collections of keywords
        // Debounce to reduce stream emissions during rapid user input
        val keywordsStream = queryState
            .debounce(TextInputDebouncePeriod)
            .onStart { emit(queryState.value) }
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
            .stateIn(
                viewModelScope,
                SharingStarted.WhileSubscribed(stopTimeoutMillis = 5000),
                PagingData.empty()
            )
    }

}