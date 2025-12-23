package com.example.calcapp.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun CalculatorScreen(
    viewModel: CalculatorViewModel
) {
    val display by viewModel.display.collectAsState()

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
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
                        .padding(24.dp),
                    fontSize = 36.sp,
                    textAlign = TextAlign.End,
                    maxLines = 2
                )
            }

            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    CalcButton("C", Modifier.weight(1f)) { viewModel.onClear() }
                    CalcButton("DEL", Modifier.weight(1f)) { viewModel.onDelete() }
                    CalcButton("÷", Modifier.weight(1f)) { viewModel.onSymbolClick("÷") }
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    CalcButton("7", Modifier.weight(1f)) { viewModel.onSymbolClick("7") }
                    CalcButton("8", Modifier.weight(1f)) { viewModel.onSymbolClick("8") }
                    CalcButton("9", Modifier.weight(1f)) { viewModel.onSymbolClick("9") }
                    CalcButton("×", Modifier.weight(1f)) { viewModel.onSymbolClick("×") }
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    CalcButton("4", Modifier.weight(1f)) { viewModel.onSymbolClick("4") }
                    CalcButton("5", Modifier.weight(1f)) { viewModel.onSymbolClick("5") }
                    CalcButton("6", Modifier.weight(1f)) { viewModel.onSymbolClick("6") }
                    CalcButton("-", Modifier.weight(1f)) { viewModel.onSymbolClick("-") }
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    CalcButton("1", Modifier.weight(1f)) { viewModel.onSymbolClick("1") }
                    CalcButton("2", Modifier.weight(1f)) { viewModel.onSymbolClick("2") }
                    CalcButton("3", Modifier.weight(1f)) { viewModel.onSymbolClick("3") }
                    CalcButton("+", Modifier.weight(1f)) { viewModel.onSymbolClick("+") }
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    CalcButton("0", Modifier.weight(2f)) { viewModel.onSymbolClick("0") }
                    CalcButton(".", Modifier.weight(1f)) { viewModel.onSymbolClick(".") }
                    CalcButton("=", Modifier.weight(1f)) { viewModel.onEquals() }
                }
            }
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
        modifier = modifier.height(64.dp),
        shape = RoundedCornerShape(16.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.primary
        )
    ) {
        Text(
            text = text,
            fontSize = 20.sp,
            fontWeight = FontWeight.SemiBold
        )
    }
}


