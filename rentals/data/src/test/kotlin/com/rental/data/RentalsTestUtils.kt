package com.rental.data

import org.junit.Assert.assertThrows


inline fun assertThrowsOnInvalidPageLimitParameter(
    pageLimit: Int?,
    crossinline action: (pageLimit: Int?) -> Any?
) {
    if (pageLimit != null && pageLimit < 0) {
        assertThrows(IllegalArgumentException::class.java) {
            action(pageLimit)
        }
    }
}


inline fun assertThrowsOnInvalidPageOffsetParameter(
    pageLimit: Int?,
    pageOffset: Int?,
    crossinline action: (pageLimit: Int?, pageOffset: Int?) -> Any?
) {
    if (pageLimit != null && (
                pageOffset == null ||
                        pageOffset < 0
                )
    ) {
        assertThrows(IllegalArgumentException::class.java) {
            action(pageLimit, pageOffset)
        }
    }
}