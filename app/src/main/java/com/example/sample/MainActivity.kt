package com.example.sample

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.snapping.SnapLayoutInfoProvider
import androidx.compose.foundation.gestures.snapping.SnapPositionInLayout
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.sample.ui.theme.SampleTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SampleTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Listing()
                }
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun Listing() {
    var scrollToItem by remember {
        mutableIntStateOf(0)
    }
    val state = rememberLazyListState()
    val itemsWithDecoration = 0..12
    val offsets by remember {
        derivedStateOf {
            itemsWithDecoration.map {
                index ->
                state.layoutInfo.visibleItemsInfo.find { it.index == index }?.offset
            }
        }
    }
    val snappingLayout = remember(state) { SnapLayoutInfoProvider(state, SnapPositionInLayout.CenterToCenter) }
    val flingBehavior = rememberSnapFlingBehavior(snappingLayout)

    LaunchedEffect(scrollToItem) {
        state.animateScrollToItem(scrollToItem, -200)
    }

    Box(modifier = Modifier.fillMaxSize()) {

        LazyRow(
            state = state,
            flingBehavior = flingBehavior,
            modifier = Modifier.fillMaxSize()
        ){
            items(20) {
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .width(100.dp)
                        .clickable { scrollToItem = it }
                        .background(if (it.mod(2) == 0) Color.DarkGray else Color.Gray),
                    contentAlignment = Alignment.Center,
                    ) {
                    Text("item $it ",
                        color = Color.White,
                        fontSize = 20.sp,
                        textAlign = TextAlign.Center,
                    )
                }
            }
        }
        offsets.forEachIndexed {
            index, offset ->
            offset?.let { Decoration(index, it) }
        }
    }
}


@Composable
fun Decoration(index: Int, offset: Int) {
    Box(
        modifier = Modifier
            .offset {
                IntOffset(offset, 0)
            }
    ) {
        Box(modifier = Modifier
            .background(Color.Red)
            .width(1.dp)
            .fillMaxHeight()) {

        }
        Text("deco $index")
    }
}