package io.github.cerrativan.composebook.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import io.github.cerrativan.composebook.ui.model.Page
import io.github.cerrativan.composebook.ui.ui.composables.PageDetailScreen
import io.github.cerrativan.composebook.ui.ui.composables.PageListScreen

@Composable
fun ComposeBookApp(pagesList: List<Page>) {
    var selectedPage by remember { mutableStateOf<Page?>(null) }

    if (selectedPage == null) {
        PageListScreen(
            pagesList = pagesList,
            onPageClick = { selectedPage = it }
        )
    } else {
        PageDetailScreen(
            page = selectedPage!!,
            onBack = { selectedPage = null }
        )
    }
}
