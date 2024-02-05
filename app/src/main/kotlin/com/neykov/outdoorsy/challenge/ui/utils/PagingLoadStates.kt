package com.neykov.outdoorsy.challenge.ui.utils

import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState

val CombinedLoadStates.hasError: Boolean
    get() = anyErrorOrNull != null

val CombinedLoadStates.anyErrorOrNull: Throwable?
    get() = refresh.errorOrNull ?: prepend.errorOrNull ?: append.errorOrNull

val LoadState.errorOrNull: Throwable?
    get() = (this as? LoadState.Error)?.error