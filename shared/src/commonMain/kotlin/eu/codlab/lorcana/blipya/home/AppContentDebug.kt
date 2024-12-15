package eu.codlab.lorcana.blipya.home

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import eu.codlab.viewpager.pagination.HorizontalDotPagerIndicator
import eu.codlab.viewpager.pagination.HorizontalPagerIndicator
import eu.codlab.viewpager.pagination.PaginationView

@Suppress("MagicNumber")
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun AppContentDebug(
    modifier: Modifier = Modifier
) {
    val state = rememberPagerState(
        initialPage = 0,
        initialPageOffsetFraction = 0f
    ) {
        // provide pageCount
        5
    }

    Column(
        modifier = modifier
    ) {
        PaginationView(
            state = state,
            pagerModifier = Modifier.height(200.dp),
            pagerIndicatorContent = { pageCount, currentPage, targetPage, currentPageOffsetFraction ->
                HorizontalPagerIndicator(
                    pageCount = pageCount,
                    currentPage = currentPage,
                    targetPage = targetPage,
                    currentPageOffsetFraction = currentPageOffsetFraction
                )

                HorizontalDotPagerIndicator(
                    pageCount = pageCount,
                    currentPage = currentPage,
                    targetPage = targetPage,
                    currentPageOffsetFraction = currentPageOffsetFraction
                )
            }
        ) { page ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        color = background(page)
                    )
            ) {
                // nothing
            }
        }
    }
}

@Suppress("MagicNumber")
fun background(page: Int) = when (page) {
    0 -> Color.Black
    1 -> Color.White
    2 -> Color.Yellow
    3 -> Color.Blue
    4 -> Color.Red
    else -> Color.DarkGray
}
