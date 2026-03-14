package com.example.sampleapp

import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import io.github.cerrativan.composebook.annotations.Page

@Page(name = "Circular Progress", group = "Indicators")
@Composable
fun CircularProgressSample() {
    CircularProgressIndicator()
}

@Page(name = "Linear Progress", group = "Indicators")
@Composable
fun LinearProgressSample() {
    LinearProgressIndicator()
}

@Page(name = "Slider", group = "Inputs")
@Composable
fun SliderSample() {
    Slider(value = 0.5f, onValueChange = {})
}

@Page(name = "Label", group = "Text")
@Composable
fun LabelSample() {
    Text(text = "This is a label")
}

@Page(name = "Label 2", group = "Text")
@Composable
fun LabelSample2() {
    Text(text = "This is a label 2")
}

//@Page(name = "Error Sample", group = "Text")
//@Composable
//fun ErrorSample(text: String = "This should not work") {
//    Text(text = text)
//}