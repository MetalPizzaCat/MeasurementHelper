package com.metalpizzacat.measurementhelper

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import java.util.Locale

@Composable
fun LengthInput(
    modifier: Modifier = Modifier,
    data: Length = Length(0.0),
    accepted: (Length) -> Unit = {}
) {
    var cm by remember { mutableStateOf(data.cm.toString()) }
    var feet by remember { mutableStateOf(data.feet.toString()) }
    var inch by remember { mutableStateOf(data.feet.toString()) }
    Column(modifier = modifier.fillMaxWidth()) {
        Column {
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
        Button(
            onClick = { accepted(Length((cm.toDoubleOrNull() ?: 0.0) / 100.0)) },
            enabled = (cm.toDoubleOrNull() ?: 0.0) > 0.0
        ) {
            Text("Add")
        }
    }
}