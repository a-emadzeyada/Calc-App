package com.example.calcapp.ui

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@SuppressLint("UnusedBoxWithConstraintsScope")
@Composable
fun CalculatorScreen(
    viewModel: CalculatorViewModel
) {
    val display by viewModel.display.collectAsState()

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        BoxWithConstraints(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            val isLandscape = maxWidth > maxHeight

            if (isLandscape) {
                Row(
                    modifier = Modifier.fillMaxSize(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    DisplayArea(
                        display = display,
                        modifier = Modifier
                            .weight(1f)
                            .align(Alignment.CenterVertically)
                    )
                    KeypadArea(
                        viewModel = viewModel,
                        modifier = Modifier.weight(1.2f)
                    )
                }
            } else {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    DisplayArea(
                        display = display,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                    )
                    KeypadArea(
                        viewModel = viewModel,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun DisplayArea(
    display: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
    ) {
        Text(
            text = "Calculator",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(24.dp))
        Text(
            text = display,
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    color = MaterialTheme.colorScheme.surface,
                    shape = RoundedCornerShape(16.dp)
                )
                .padding(24.dp)
                .testTag("display"),
            fontSize = 36.sp,
            textAlign = TextAlign.End,
            maxLines = 3
        )
    }
}

@Composable
private fun KeypadArea(
    viewModel: CalculatorViewModel,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // Scientific row 1
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            CalcButton("sin", Modifier.weight(1f).testTag("btn_sin")) { viewModel.onSymbolClick("sin(") }
            CalcButton("cos", Modifier.weight(1f).testTag("btn_cos")) { viewModel.onSymbolClick("cos(") }
            CalcButton("tan", Modifier.weight(1f).testTag("btn_tan")) { viewModel.onSymbolClick("tan(") }
            CalcButton("√", Modifier.weight(1f).testTag("btn_sqrt")) { viewModel.onSymbolClick("sqrt(") }
        }

        // Scientific row 2
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            CalcButton("ln", Modifier.weight(1f).testTag("btn_ln")) { viewModel.onSymbolClick("ln(") }
            CalcButton("log", Modifier.weight(1f).testTag("btn_log")) { viewModel.onSymbolClick("log(") }
            CalcButton("(", Modifier.weight(1f).testTag("btn_open_paren")) { viewModel.onSymbolClick("(") }
            CalcButton(")", Modifier.weight(1f).testTag("btn_close_paren")) { viewModel.onSymbolClick(")") }
        }

        // Scientific row 3
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            CalcButton("π", Modifier.weight(1f).testTag("btn_pi")) { viewModel.onSymbolClick("π") }
            CalcButton("xʸ", Modifier.weight(1f).testTag("btn_pow")) { viewModel.onSymbolClick("^") }
            CalcButton("C", Modifier.weight(1f).testTag("btn_clear")) { viewModel.onClear() }
            CalcButton("DEL", Modifier.weight(1f).testTag("btn_del")) { viewModel.onDelete() }
        }

        // Digits and basic operators
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            CalcButton("7", Modifier.weight(1f).testTag("btn_7")) { viewModel.onSymbolClick("7") }
            CalcButton("8", Modifier.weight(1f).testTag("btn_8")) { viewModel.onSymbolClick("8") }
            CalcButton("9", Modifier.weight(1f).testTag("btn_9")) { viewModel.onSymbolClick("9") }
            CalcButton("÷", Modifier.weight(1f).testTag("btn_div")) { viewModel.onSymbolClick("÷") }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            CalcButton("4", Modifier.weight(1f).testTag("btn_4")) { viewModel.onSymbolClick("4") }
            CalcButton("5", Modifier.weight(1f).testTag("btn_5")) { viewModel.onSymbolClick("5") }
            CalcButton("6", Modifier.weight(1f).testTag("btn_6")) { viewModel.onSymbolClick("6") }
            CalcButton("×", Modifier.weight(1f).testTag("btn_mul")) { viewModel.onSymbolClick("×") }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            CalcButton("1", Modifier.weight(1f).testTag("btn_1")) { viewModel.onSymbolClick("1") }
            CalcButton("2", Modifier.weight(1f).testTag("btn_2")) { viewModel.onSymbolClick("2") }
            CalcButton("3", Modifier.weight(1f).testTag("btn_3")) { viewModel.onSymbolClick("3") }
            CalcButton("-", Modifier.weight(1f).testTag("btn_minus")) { viewModel.onSymbolClick("-") }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            CalcButton("0", Modifier.weight(2f).testTag("btn_0")) { viewModel.onSymbolClick("0") }
            CalcButton(".", Modifier.weight(1f).testTag("btn_dot")) { viewModel.onSymbolClick(".") }
            CalcButton("+", Modifier.weight(1f).testTag("btn_plus")) { viewModel.onSymbolClick("+") }
            CalcButton("=", Modifier.weight(1f).testTag("btn_equals")) { viewModel.onEquals() }
        }
    }
}

@Composable
private fun CalcButton(
    text: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        modifier = modifier.height(52.dp),
        shape = RoundedCornerShape(16.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.primary
        )
    ) {
        Text(
            text = text,
            fontSize = 18.sp,
            fontWeight = FontWeight.SemiBold
        )
    }
}


