package com.neykov.outdoorsy.challenge.ui.listing

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemContentType
import androidx.paging.compose.itemKey
import coil.ImageLoader
import coil.compose.AsyncImage
import com.neykov.outdoorsy.challenge.R
import com.neykov.outdoorsy.challenge.ui.utils.bottom
import com.neykov.outdoorsy.challenge.ui.utils.end
import com.neykov.outdoorsy.challenge.ui.utils.hasError
import com.neykov.outdoorsy.challenge.ui.utils.rememberUpdated
import com.neykov.outdoorsy.challenge.ui.utils.start
import com.neykov.outdoorsy.challenge.ui.utils.top
import com.rental.model.RentalEntry
import org.koin.androidx.compose.koinViewModel
import org.koin.compose.koinInject

@Composable
internal fun RentalsListingScreen(
    viewModel: RentalsListingViewModel = koinViewModel(),
    imageLoader: ImageLoader = koinInject()
) {
    val lazyPagingItems = viewModel.pagingDataStream.collectAsLazyPagingItems()
    val searchText by viewModel.searchTextState.collectAsState()

    RentalsListingScreen(
        lazyPagingItems = lazyPagingItems,
        searchText = searchText,
        imageLoader = imageLoader,
        onSearchTextChange = {
            viewModel.searchTextState.value = it
        },
        onVoiceSearchClick = { /*TODO: Add handling */ },
        onRetryLoadClick = { lazyPagingItems.retry() })
}

@Composable
fun RentalsListingScreen(
    lazyPagingItems: LazyPagingItems<RentalEntry>,
    searchText: String,
    imageLoader: ImageLoader,
    onSearchTextChange: (String) -> Unit,
    onVoiceSearchClick: () -> Unit,
    onRetryLoadClick: () -> Unit,
) {
    Scaffold(
        topBar = {
            RentalsSearchAppBar(
                modifier = Modifier.fillMaxWidth(),
                searchText = searchText,
                onVoiceSearchClick = onVoiceSearchClick,
                onSearchTextChange = onSearchTextChange
            )
        }
    ) { insets ->
        RentalsListContent(insets, lazyPagingItems, onRetryLoadClick, imageLoader)
    }
}

@Composable
private fun RentalsListContent(
    insets: PaddingValues,
    lazyPagingItems: LazyPagingItems<RentalEntry>,
    onRetryLoadClick: () -> Unit,
    imageLoader: ImageLoader
) {
    LazyColumn(
        contentPadding = insets.rememberUpdated { direction ->
            PaddingValues(
                start = start(direction) + 8.dp,
                end = end(direction) + 8.dp,
                top = top + 12.dp,
                bottom = bottom + 12.dp
            )
        },
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        when (lazyPagingItems.loadState.refresh) {
            is LoadState.Error -> {
                item {
                    LoadingErrorItem(onRetryLoadClick)
                }
            }

            LoadState.Loading -> {
                item {
                    LoadingItem(modifier = Modifier.fillParentMaxSize())
                }
            }

            is LoadState.NotLoading -> {
                if (lazyPagingItems.itemCount == 0) {
                    item {
                        NoResultsItem()
                    }
                }
            }
        }

        if (lazyPagingItems.loadState.hasError) {
            item { LoadingErrorItem(onRetryLoadClick) }
        } else {
            if (lazyPagingItems.loadState.prepend is LoadState.Loading) {
                item {
                    LoadingItem()
                }
            }

            items(
                lazyPagingItems.itemCount,
                lazyPagingItems.itemKey(),
                lazyPagingItems.itemContentType()
            ) { index ->
                val item =
                    checkNotNull(lazyPagingItems[index]) { "Unexpected placeholder item." }
                RentalEntryCard(
                    name = item.name,
                    imageUrl = item.imageUrl,
                    imageLoader = imageLoader
                )
            }

            if (lazyPagingItems.loadState.append is LoadState.Loading) {
                item {
                    LoadingItem()
                }
            }
        }
    }
}

@Composable
private fun LazyItemScope.LoadingErrorItem(onRetryLoadClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillParentMaxSize()
            .wrapContentHeight(Alignment.CenterVertically)
            .wrapContentWidth(Alignment.CenterHorizontally),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            modifier = Modifier
                .padding(bottom = 16.dp)
                .size(96.dp),
            imageVector = Icons.Default.Warning,
            contentDescription = stringResource(R.string.content_description_warning_icon)
        )

        Text(
            modifier = Modifier.padding(bottom = 24.dp),
            text = stringResource(R.string.title_rental_listing_load_error),
            style = MaterialTheme.typography.titleMedium
        )

        FilledTonalButton(onClick = onRetryLoadClick) {
            Text(text = stringResource(R.string.action_retry))
        }
    }
}

@Composable
private fun LazyItemScope.NoResultsItem() {
    Column(
        modifier = Modifier
            .fillParentMaxSize()
            .wrapContentHeight(Alignment.CenterVertically)
            .wrapContentWidth(Alignment.CenterHorizontally),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            modifier = Modifier
                .padding(bottom = 16.dp)
                .size(96.dp),
            imageVector = Icons.Default.Search,
            contentDescription = stringResource(R.string.content_description_warning_icon)
        )

        Text(
            modifier = Modifier.padding(bottom = 24.dp),
            text = stringResource(R.string.message_no_results_found),
            style = MaterialTheme.typography.titleMedium
        )
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun RentalsSearchAppBar(
    modifier: Modifier = Modifier,
    searchText: String,
    onVoiceSearchClick: () -> Unit,
    onSearchTextChange: (String) -> Unit
) {
    CompositionLocalProvider(LocalTextStyle provides MaterialTheme.typography.bodyLarge) {
        Surface(
            modifier = modifier,
            color = MaterialTheme.colorScheme.primary,
            shadowElevation = 1.dp
        ) {
            SearchBar(
                modifier = Modifier
                    .padding(start = 8.dp, end = 8.dp, top = 8.dp, bottom = 12.dp)
                    .fillMaxWidth(),
                shape = MaterialTheme.shapes.extraSmall,
                placeholder = {
                    Text(text = stringResource(R.string.message_filter_results))
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = stringResource(
                            R.string.content_description_search_icon
                        )
                    )
                },
                trailingIcon = {
                    IconButton(onClick = onVoiceSearchClick) {
                        Icon(
                            painter = painterResource(id = R.drawable.baseline_mic_24),
                            contentDescription = stringResource(
                                R.string.content_description_search_icon
                            )
                        )
                    }
                },
                query = searchText,
                onQueryChange = onSearchTextChange,
                onSearch = onSearchTextChange,
                active = false,
                onActiveChange = {/* No-op */ }
            ) {}
        }
    }
}

@Composable
private fun LoadingItem(modifier: Modifier = Modifier) {
    // There's a critical binary compatibility bug in the latest stable
    // Compose BOM (2024.01.00) which breaks animations (and indeterminate progress indicators)
    // https://issuetracker.google.com/issues/322214617#comment5
    //TODO: Remove the progress value once the BOM gets bugfixes published.
    CircularProgressIndicator(
        modifier = modifier
            .defaultMinSize(minHeight = 72.dp)
            .wrapContentWidth(Alignment.CenterHorizontally),
        progress = 0.5f
    )
}


@Preview
@Composable
private fun RentalEntryCardPreview() {
    val imageLoader = ImageLoader.Builder(LocalContext.current).build()
    RentalEntryCard(
        name = "Big Foot",
        imageUrl = "https:/big.foot/image.jpg",
        imageLoader = imageLoader
    )
}

@Composable
private fun RentalEntryCard(name: String, imageUrl: String?, imageLoader: ImageLoader) {
    Card(
        shape = MaterialTheme.shapes.extraSmall,
        elevation = CardDefaults.elevatedCardElevation()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            val placeholderColor = MaterialTheme.colorScheme.onSurfaceVariant
            val placeholderPainter = remember(placeholderColor) { ColorPainter(placeholderColor) }
            AsyncImage(
                modifier = Modifier
                    .padding(8.dp)
                    .width(100.dp)
                    .aspectRatio(1.5f)
                    .clip(MaterialTheme.shapes.extraSmall),
                imageLoader = imageLoader,
                contentScale = ContentScale.Crop,
                alignment = Alignment.Center,
                model = imageUrl,
                placeholder = placeholderPainter,
                error = placeholderPainter,
                contentDescription = stringResource(R.string.content_description_rental_primary_image)
            )
            Text(
                modifier = Modifier
                    .padding(all = 10.dp)
                    .weight(1f),
                text = name,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}