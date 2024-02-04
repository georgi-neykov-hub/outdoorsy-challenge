package com.rental.domain

import androidx.annotation.VisibleForTesting


/**
 * Regex for matching consecutive sequences of common punctuation symbols
 */
private val KeywordDelimitersPattern: Regex = "[\\s,;:.!?()]+".toRegex()

@VisibleForTesting
internal const val DefaultKeywordLimit = 25 // The maximum number of keywords, selected arbitrarily.

fun String?.splitToKeywordSequence(limit: Int = DefaultKeywordLimit): Sequence<String> =
    when {
        this.isNullOrEmpty() -> emptySequence()
        else -> splitToSequence(KeywordDelimitersPattern, limit)
            .filter(String::isNotEmpty)
    }

fun String?.splitToKeywords(limit: Int = DefaultKeywordLimit): Set<String> =
    splitToKeywordSequence(limit).toSet()