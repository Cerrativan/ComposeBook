package io.github.cerrativan.composebook.ui.model

import androidx.compose.runtime.Composable

data class Page(
    val name: String,
    val group: String,
    val content: @Composable () -> Unit
)
