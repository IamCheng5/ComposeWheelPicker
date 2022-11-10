package com.andyliu.composewheelpicker

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.andyliu.compose_wheel_picker.VerticalWheelPicker
import kotlinx.coroutines.launch

const val loopingRealCount = 1000000000
const val halfLoopingRealCount = loopingRealCount / 2
fun getLoopingStartIndex(startIndex: Int, count: Int) =
    halfLoopingRealCount - halfLoopingRealCount % count + startIndex

@Composable
internal fun TextPicker() {
    val startIndex = 2
    val state = rememberLazyListState(startIndex)
    val scope = rememberCoroutineScope()
    var currentIndex by remember { mutableStateOf(startIndex) }
    VerticalWheelPicker(
        state = state,
        count = 10,
        itemHeight = 44.dp,
        visibleItemCount = 3,
        onScrollFinish = { currentIndex = it }) { index ->
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .clickable { scope.launch { state.animateScrollToItem(index) } },
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Text $index",
                color = if (index == currentIndex) Color.Black else Color.Gray
            )
        }
    }
}

@Composable
internal fun LoopingTextPicker() {
    val startIndex = 3
    val count = 10
    val realStartIndex = getLoopingStartIndex(startIndex, count)
    val state = rememberLazyListState(realStartIndex)
    val scope = rememberCoroutineScope()
    var currentIndex by remember { mutableStateOf(realStartIndex) }
    VerticalWheelPicker(
        state = state,
        count = loopingRealCount,
        itemHeight = 44.dp,
        visibleItemCount = 5,
        onScrollFinish = { currentIndex = it }) { index ->
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .clickable { scope.launch { state.animateScrollToItem(index) } },
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Text ${index % count}",
                color = if (index == currentIndex) Color.Black else Color.Gray
            )
        }
    }
}

@Composable
internal fun HoursPicker1() {
    var hour by remember { mutableStateOf(8) }
    var minute by remember { mutableStateOf(0) }
    val itemHeight = 44.dp
    Box(modifier = Modifier.fillMaxWidth(0.8F), contentAlignment = Alignment.Center) {
        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            LoopingNumberPicker(
                modifier = Modifier.weight(1F),
                range = 0..23,
                currentNumber = hour,
                itemHeight = itemHeight,
                onCurrentNumberChange = { hour = it }
            )
            Text(
                text = ":",
                style = TextStyle(
                    fontSize = 18.sp,
                    fontWeight = FontWeight(500),
                    color = Color.Black
                )
            )
            LoopingNumberPicker(
                modifier = Modifier.weight(1F),
                range = 0..59,
                currentNumber = minute,
                itemHeight = itemHeight,
                onCurrentNumberChange = { minute = it }
            )
        }
        HoursBackground(modifier = Modifier
            .fillMaxWidth()
            .height(itemHeight))
    }
}

enum class DayTime { AM, PM }

@Composable
internal fun HoursPicker2() {
    var hour by remember { mutableStateOf(8) }
    var minute by remember { mutableStateOf(0) }
    var dayTime by remember { mutableStateOf(DayTime.AM) }
    val itemHeight = 44.dp
    Box(modifier = Modifier.fillMaxWidth(0.8F), contentAlignment = Alignment.Center) {
        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            DayTimePicker(
                modifier = Modifier.weight(1F),
                itemHeight = itemHeight,
                currentDayTime = dayTime,
                onDayTimeChange = { dayTime = it }

            )
            LoopingNumberPicker(
                modifier = Modifier.weight(1F),
                range = 1..12,
                currentNumber = hour,
                itemHeight = itemHeight,
                onCurrentNumberChange = { hour = it }
            )
            Text(
                text = ":",
                style = TextStyle(
                    fontSize = 18.sp,
                    fontWeight = FontWeight(500),
                    color = Color.Black
                )
            )
            LoopingNumberPicker(
                modifier = Modifier.weight(1F),
                range = 0..59,
                currentNumber = minute,
                itemHeight = itemHeight,
                onCurrentNumberChange = { minute = it }
            )
        }
        HoursBackground(modifier = Modifier
            .fillMaxWidth()
            .height(itemHeight))
    }
}

@Composable
internal fun HoursBackground(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(Color(0f, 0f, 0f, 0.1f))
    )
}

@Composable
internal fun DayTimePicker(
    modifier: Modifier = Modifier,
    itemHeight: Dp,
    currentDayTime: DayTime,
    onDayTimeChange: (DayTime) -> Unit
) {
    val state = rememberLazyListState(currentDayTime.ordinal)
    val scope = rememberCoroutineScope()
    VerticalWheelPicker(
        modifier = modifier,
        state = state,
        count = 2,
        itemHeight = itemHeight,
        visibleItemCount = 3,
        onScrollFinish = { index ->
            onDayTimeChange(DayTime.values()[index % 2])
        }
    ) { index ->
        val dayTime = DayTime.values()[index % 2]
        val text = when (dayTime) {
            DayTime.AM -> "AM"
            DayTime.PM -> "PM"
        }
        Box(
            modifier = Modifier
                .fillMaxSize()
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null,
                    onClick = { scope.launch { state.animateScrollToItem(index) } }
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = text,
                style = TextStyle(
                    fontSize = 18.sp,
                    fontWeight = if (dayTime == currentDayTime) FontWeight(500) else FontWeight(
                        400
                    ),
                    color = if (dayTime == currentDayTime) Color.Black else Color.Gray
                )
            )
        }
    }
}

@Composable
internal fun LoopingNumberPicker(
    modifier: Modifier = Modifier,
    range: IntRange,
    currentNumber: Int,
    itemHeight: Dp,
    onCurrentNumberChange: (Int) -> Unit
) {
    require(currentNumber in range)
    val rangeList = range.toList()
    val startIndex = remember { rangeList.indexOf(currentNumber) }
    val count = rangeList.size
    val realStartIndex = getLoopingStartIndex(startIndex, count)
    val state = rememberLazyListState(realStartIndex)
    val scope = rememberCoroutineScope()
    var currentIndex by remember { mutableStateOf(realStartIndex) }
    VerticalWheelPicker(
        modifier = modifier,
        state = state,
        count = loopingRealCount,
        itemHeight = itemHeight,
        visibleItemCount = 3,
        onScrollFinish = { index ->
            currentIndex = index
            onCurrentNumberChange(rangeList[index % count])
        },
        content = { index ->
            val number = rangeList[index % count]
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null,
                        onClick = { scope.launch { state.animateScrollToItem(index) } }
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = String.format("%02d", number),
                    style = TextStyle(
                        fontSize = 18.sp,
                        fontWeight = if (currentIndex == index) FontWeight(500) else FontWeight(400),
                        color = if (currentIndex == index) Color.Black else Color.Gray
                    )
                )
            }
        }
    )
}