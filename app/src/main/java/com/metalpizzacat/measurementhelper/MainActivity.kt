package com.metalpizzacat.measurementhelper

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.metalpizzacat.measurementhelper.ui.theme.MeasurmentHelperTheme
import java.util.Locale


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MeasurmentHelperTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    ComparisonComponent(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun ComparisonComponent(
    modifier: Modifier = Modifier,
    viewModel: HeightViewModel = viewModel()
) {
    Column(modifier) {
        LengthInput(accepted = {
            viewModel.heights.add(HeightChartData(it))
        })
        HeightChart(
            height = viewModel.heights,
            modifier = Modifier
                .padding(10.dp)
                .verticalScroll(rememberScrollState())
        )

    }
}


@Composable
fun LengthInput(modifier: Modifier = Modifier, accepted: (Length) -> Unit = {}) {
    var cm by remember { mutableStateOf("") }
    var feet by remember { mutableStateOf("") }
    var inch by remember { mutableStateOf("") }
    Row(modifier = modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier.weight(0.7f),
        ) {
            Row(modifier = Modifier.padding(1.dp)) {
                Text(text = "Cm: ", modifier = Modifier.weight(0.2f))
                TextField(
                    value = cm,
                    onValueChange = {
                        cm = it
                        val len = Length((it.toDoubleOrNull() ?: 0.0) / 100.0)
                        feet = len.feet.toInt().toString()
                        inch = String.format(
                            Locale.getDefault(),
                            "%.2f",
                            len.inch
                        )
                    },
                    modifier = Modifier.weight(0.8f),
                    keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
                )
            }
            Row(modifier = Modifier.padding(1.dp)) {
                Row(modifier = Modifier.weight(0.5f)) {
                    Text(text = "Feet: ")
                    TextField(
                        value = feet,
                        onValueChange = {
                            feet = it
                            val len =
                                Length(it.toDoubleOrNull() ?: 0.0, inch.toDoubleOrNull() ?: 0.0)
                            cm = len.cm.toString()
                        },
                        keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
                    )
                }
                Row(modifier = Modifier.weight(0.5f)) {
                    Text(text = "Inch: ")
                    TextField(
                        value = inch,
                        onValueChange = {
                            inch = it
                            val len =
                                Length(feet.toDoubleOrNull() ?: 0.0, it.toDoubleOrNull() ?: 0.0)
                            cm = len.cm.toString()
                        },
                        keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
                    )
                }
            }

        }
        Button(onClick = { accepted(Length((cm.toDoubleOrNull() ?: 0.0) / 100.0)) }, modifier = Modifier.weight(0.2f)) {
            Text("Add")
        }
    }
}

@Composable
fun MeasurementDisplay(length: Length, displayInCm: Boolean) {
    Column {
        if (displayInCm) {
            Text(text = "${length.cm} cm")
            Text(text = "${length.totalInch}")
        } else {
            Text(text = "${length.meters} m")

            Text(
                text = "${length.feet.toInt()}ft ${
                    String.format(
                        Locale.getDefault(),
                        "%.2f",
                        length.inch
                    )
                }in"
            )
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun LengthPreview() {
    MeasurmentHelperTheme {
        MeasurementDisplay(Length(1.7), displayInCm = false)
    }
}

@Preview(showBackground = true)
@Composable
fun InputPreview() {
    MeasurmentHelperTheme {
        LengthInput()
    }
}