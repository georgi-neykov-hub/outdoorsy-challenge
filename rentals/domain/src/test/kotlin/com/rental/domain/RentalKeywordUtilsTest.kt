package com.rental.domain

import org.junit.Assert.assertEquals
import org.junit.Test

class RentalKeywordUtilsTest {
    @Test
    fun query_With_Common_Punctuation_Marks_Is_Accordingly() = querySplitToKeywordsTest(
        "Something, went; \r down!\n \tApostrophe's own sign?To an* End.",
        "Something", "went", "down", "Apostrophe's", "own", "sign", "To", "an*", "End"
    )

    //TODO: Add more cases with repeating words
}

private fun querySplitToKeywordsTest(
    query: String?,
    vararg keywords: String,
    limit: Int = DefaultKeywordLimit,
) {
    val expected = keywords.toSet()
    val actual = query.splitToKeywords(limit)

    assertEquals(expected, actual)
}