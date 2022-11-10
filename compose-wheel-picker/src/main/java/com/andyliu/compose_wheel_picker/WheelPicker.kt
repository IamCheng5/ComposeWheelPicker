package com.andyliu.compose_wheel_picker

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.flow.filter

@Composable
fun VerticalWheelPicker(
    modifier: Modifier = Modifier,
    count: Int,
    state: LazyListState = rememberLazyListState(),
    itemHeight: Dp,
    visibleItemCount: Int,
    onScrollFinish: (index: Int) -> Unit,
    content: @Composable BoxScope.(index: Int) -> Unit
) {
    val itemHalfHeightToPx = with(LocalDensity.current) { itemHeight.toPx() / 2 }

    val currentOnScrollFinish by rememberUpdatedState(onScrollFinish)

    LaunchedEffect(state.isScrollInProgress) {
        if (!state.isScrollInProgress && state.firstVisibleItemScrollOffset != 0) {
            if (state.firstVisibleItemScrollOffset < itemHalfHeightToPx) {
                state.animateScrollToItem(state.firstVisibleItemIndex)
            } else if (state.firstVisibleItemScrollOffset > itemHalfHeightToPx) {
                state.animateScrollToItem(state.firstVisibleItemIndex + 1)
            }
        }
    }

    LaunchedEffect(state) {
        snapshotFlow { state.isScrollInProgress }
            .filter { !it && state.firstVisibleItemScrollOffset == 0 }
            .drop(1)
            .collect { currentOnScrollFinish(state.firstVisibleItemIndex) }
    }

    LazyColumn(
        modifier = modifier.height(itemHeight * visibleItemCount),
        state = state,
        contentPadding = PaddingValues(vertical = itemHeight * (visibleItemCount / 2))
    ) {
        items(count = count, key = { it }) { index ->
            Box(
                modifier = Modifier
                    .height(itemHeight),
                contentAlignment = Alignment.Center
            ) {
                content(index)
            }
        }
    }
}

@Composable
fun HorizontalWheelPicker(
    modifier: Modifier = Modifier,
    count: Int,
    state: LazyListState = rememberLazyListState(),
    itemWidth: Dp,
    visibleItemCount: Int,
    onScrollFinish: (index: Int) -> Unit,
    content: @Composable BoxScope.(index: Int) -> Unit
) {
    val itemHalfWidthToPx = with(LocalDensity.current) { itemWidth.toPx() / 2 }

    val currentOnScrollFinish by rememberUpdatedState(onScrollFinish)

    LaunchedEffect(state.isScrollInProgress) {
        if (!state.isScrollInProgress && state.firstVisibleItemScrollOffset != 0) {
            if (state.firstVisibleItemScrollOffset < itemHalfWidthToPx) {
                state.animateScrollToItem(state.firstVisibleItemIndex)
            } else if (state.firstVisibleItemScrollOffset > itemHalfWidthToPx) {
                state.animateScrollToItem(state.firstVisibleItemIndex + 1)
            }
        }
    }

    LaunchedEffect(state) {
        snapshotFlow { state.isScrollInProgress }
            .filter {
                !it && state.firstVisibleItemScrollOffset == 0
            }
            .drop(1)
            .collect {
                currentOnScrollFinish(state.firstVisibleItemIndex)
            }
    }

    LazyRow(
        modifier = modifier.width(itemWidth * visibleItemCount),
        state = state,
        contentPadding = PaddingValues(horizontal = itemWidth * (visibleItemCount / 2))
    ) {
        items(count = count, key = { it }) { index ->
            Box(
                modifier = Modifier
                    .width(itemWidth),
                contentAlignment = Alignment.Center
            ) {
                content(index)
            }
        }
    }
}
