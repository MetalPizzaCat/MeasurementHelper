package com.metalpizzacat.measurementhelper

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.focusModifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.metalpizzacat.measurementhelper.ui.theme.MeasurmentHelperTheme
import java.util.Locale
import kotlin.math.ceil


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
fun ChartEntryEdit(
    modifier: Modifier = Modifier,
    id: Int,
    entry: HeightChartData,
    editRequested: (Int) -> Unit
) {
    ElevatedCard(
        onClick = { editRequested(id) },
        modifier = Modifier
            .fillMaxWidth()
            .padding(5.dp)
    ) {
        Column(modifier.padding(5.dp)) {
            Text(text = "${entry.height.cm}cm")
            Text(
                "${entry.height.feet}ft ${
                    String.format(
                        Locale.getDefault(),
                        "%.2f",
                        entry.height.inch
                    )
                }in"
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ComparisonComponent(
    modifier: Modifier = Modifier,
    viewModel: HeightViewModel = viewModel()
) {
    val sheetState = rememberModalBottomSheetState()
    var currentlyEditedEntry by remember { mutableStateOf<Int?>(null) }
    Column(modifier.padding(5.dp)) {
        LengthInput(accepted = {
            viewModel.heights.add(HeightChartData(it))
        })
        Column(
            modifier = Modifier
                .padding(10.dp)
                .verticalScroll(rememberScrollState())
        ) {
            HeightChart(
                height = viewModel.heights,
                backgroundColor = colorResource(id = R.color.teal_700)
            )
            Column(modifier = Modifier.padding(5.dp)) {
                viewModel.heights.forEachIndexed { index, heightChartData ->
                    ChartEntryEdit(id = index, entry = heightChartData) {
                        currentlyEditedEntry = it
                    }
                }
            }
        }
    }
    if(currentlyEditedEntry != null) {
        ModalBottomSheet(
            onDismissRequest = { currentlyEditedEntry = null },
            sheetState = sheetState
        ) {
            val entry = viewModel.heights[currentlyEditedEntry!!]
            Column(modifier = Modifier.padding(10.dp)) {
                Text(text = "Color")
                ColorPicker(color = entry.colors) {
                    viewModel.heights.removeAt(currentlyEditedEntry!!)
                    viewModel.heights.add(currentlyEditedEntry!!, entry.copy(colors = it))
                }
                LengthInput(data = viewModel.heights[currentlyEditedEntry!!].height, accepted = {
                    viewModel.heights.removeAt(currentlyEditedEntry!!)
                    viewModel.heights.add(currentlyEditedEntry!!, entry.copy(height = it))
                })
                Button(onClick = {
                    viewModel.heights.removeAt(currentlyEditedEntry!!)
                    currentlyEditedEntry = null
                }) {
                    Text(text = "Remove")
                }
            }
        }
    }
}

@Composable
fun ColorPicker(
    modifier: Modifier = Modifier,
    color: ChartColors,
    colorSelected: (ChartColors) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    TextButton(
        onClick = { expanded = true },
        modifier
            .fillMaxWidth()
            .background(color = color.color)
    ) {
        Text(text = "")
    }
    Box(modifier = modifier.fillMaxWidth()) {
        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            for (colorOption in ChartColors.entries) {
                TextButton(
                    onClick = { colorSelected(colorOption) },
                    modifier
                        .fillMaxWidth()
                        .background(color = colorOption.color)
                ) {
                    Text(text = "")
                }
            }
        }
    }
}




