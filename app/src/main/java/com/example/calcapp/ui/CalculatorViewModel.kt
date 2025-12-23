package com.example.calcapp.ui

import androidx.lifecycle.ViewModel
import com.example.calcapp.domain.Calculator
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class CalculatorViewModel(
    private val calculator: Calculator
) : ViewModel() {

    private val _display = MutableStateFlow("0")
    val display: StateFlow<String> = _display.asStateFlow()

    fun onSymbolClick(symbol: String) {
        _display.value = calculator.input(symbol)
    }

    fun onClear() {
        _display.value = calculator.clear()
    }

    fun onDelete() {
        _display.value = calculator.delete()
    }

    fun onEquals() {
        _display.value = calculator.evaluate()
    }
}


