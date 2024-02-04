package com.rental.data

internal fun requireValidPaginationParameters(pageLimit: Int?, pageOffset: Int?) {
    require(pageLimit == null || pageLimit >= 0) { "Invalid pageLimit value, must be >=0." }
    require((pageLimit == null) == (pageOffset == null)) { "Invalid pageOffset value, cannot be null when pageLimit != null." }
    require(pageOffset == null || pageOffset >= 0) { "Invalid page offset `${pageLimit}` must >= 0." }
}