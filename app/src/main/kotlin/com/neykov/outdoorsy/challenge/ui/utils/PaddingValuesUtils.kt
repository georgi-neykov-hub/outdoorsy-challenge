package com.neykov.outdoorsy.challenge.ui.utils

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisallowComposableCalls
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection

inline val PaddingValues.top: Dp
    get() = calculateTopPadding()
inline val PaddingValues.bottom: Dp
    get() = calculateTopPadding()

fun PaddingValues.start(layoutDirection: LayoutDirection) =
    calculateStartPadding(layoutDirection)

fun PaddingValues.end(layoutDirection: LayoutDirection) =
    calculateEndPadding(layoutDirection)

inline fun PaddingValues.update(
    direction: LayoutDirection,
    crossinline action: PaddingValues.(LayoutDirection) -> PaddingValues
): PaddingValues =
    action(this, direction)

fun PaddingValues.update(
    direction: LayoutDirection,
    start: Dp = this.start(direction),
    top: Dp = this.top,
    end: Dp = this.end(direction),
    bottom: Dp = this.bottom
) = PaddingValues(start, top, end, bottom)

@Composable
inline fun PaddingValues.rememberUpdated(crossinline action: @DisallowComposableCalls PaddingValues.(LayoutDirection) -> PaddingValues): PaddingValues {
    val direction = LocalLayoutDirection.current
    return remember(direction, this) {
        this.update(direction, action)
    }
}