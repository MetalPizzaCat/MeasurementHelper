package com.metalpizzacat.measurementhelper

import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.foundation.Canvas
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.metalpizzacat.measurementhelper.ui.theme.MeasurmentHelperTheme
import kotlin.math.ceil

@Composable
fun HeightChart(
    height: List<HeightChartData>,
    modifier: Modifier = Modifier,
    barPadding: Dp = 16.dp,
    additionalHeightMultiplier: Double = 0.2,
    lineColor: Color = Color.White,
    showHorizontalLines: Boolean = true
) {
    val textMeasurer = rememberTextMeasurer()
    val color: Color = colorResource(id = R.color.teal_200)

    val padding: Float = LocalDensity.current.run { barPadding.toPx() }
    val maxHeight: Double = if (height.isNotEmpty()) {
        height.maxBy { it.height.cm }.height.cm + (additionalHeightMultiplier * height.maxBy { it.height.cm }.height.cm)
    } else {
        180.0
    }
    val tickCount = ceil(maxHeight / 10.0).toInt()
    Canvas(modifier = modifier.aspectRatio(1f)) {
        if (showHorizontalLines) {
            for (i in 0..tickCount) {
                val lineHeight: Float = (size.height * (10.0 * i) / maxHeight).toFloat()
                drawText(
                    textMeasurer,
                    "${i * 10}cm",
                    topLeft = Offset(
                        0f,
                        size.height - (size.height * (10.0 * (i + 1)) / maxHeight).toFloat()
                    ),
                )
                drawLine(
                    color = lineColor,
                    start = Offset(0f, size.height - lineHeight),
                    end = Offset(size.width, size.height - lineHeight)
                )
            }
        }
        val startOffset = textMeasurer.measure("$maxHeight cm").size.width
        val barWidth = (size.width - (padding * height.size + startOffset)) / height.size
        height.forEachIndexed { i, height ->
            val barHeight = size.height * (height.height.cm / maxHeight).toFloat()
            drawRect(
                color = color,
                topLeft = Offset(startOffset + (barWidth + padding) * i, size.height - barHeight),
                size = Size(barWidth, barHeight)
            )
        }
    }
}

@Preview
@Composable
fun ChartPreview() {
    MeasurmentHelperTheme {
        HeightChart(
            height = listOf(
                HeightChartData(Length(1.7)),
                HeightChartData(Length(1.52)),
                HeightChartData(Length(1.8))
            )
        )
    }
}