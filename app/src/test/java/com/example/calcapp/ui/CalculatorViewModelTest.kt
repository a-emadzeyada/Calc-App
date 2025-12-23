package com.example.calcapp.ui

import com.example.calcapp.domain.Calculator
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

private class FakeCalculator : Calculator {
    var lastInput: String? = null
    var clearCalled = false
    var deleteCalled = false
    var evaluateCalled = false

    override fun input(symbol: String): String {
        lastInput = symbol
        return "input:$symbol"
    }

    override fun clear(): String {
        clearCalled = true
        return "cleared"
    }

    override fun delete(): String {
        deleteCalled = true
        return "deleted"
    }

    override fun evaluate(): String {
        evaluateCalled = true
        return "evaluated"
    }
}

class CalculatorViewModelTest {

    private lateinit var fakeCalculator: FakeCalculator
    private lateinit var viewModel: CalculatorViewModel

    @Before
    fun setup() {
        fakeCalculator = FakeCalculator()
        viewModel = CalculatorViewModel(fakeCalculator)
    }

    @Test
    fun `initial display is zero`() {
        assertEquals("0", viewModel.display.value)
    }

    @Test
    fun `symbol click updates display from calculator`() {
        viewModel.onSymbolClick("7")
        assertEquals("input:7", viewModel.display.value)
        assertEquals("7", fakeCalculator.lastInput)
    }

    @Test
    fun `clear updates display and calls calculator`() {
        viewModel.onClear()
        assertEquals("cleared", viewModel.display.value)
        assertEquals(true, fakeCalculator.clearCalled)
    }

    @Test
    fun `delete updates display and calls calculator`() {
        viewModel.onDelete()
        assertEquals("deleted", viewModel.display.value)
        assertEquals(true, fakeCalculator.deleteCalled)
    }

    @Test
    fun `equals updates display and calls calculator`() {
        viewModel.onEquals()
        assertEquals("evaluated", viewModel.display.value)
        assertEquals(true, fakeCalculator.evaluateCalled)
    }
}



