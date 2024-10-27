package com.metalpizzacat.measurementhelper

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel

class HeightViewModel : ViewModel() {
    val heights = mutableStateListOf<HeightChartData>()
}