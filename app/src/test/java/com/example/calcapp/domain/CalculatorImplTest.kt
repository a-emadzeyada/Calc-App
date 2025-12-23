package com.example.calcapp.domain

import org.junit.Assert.assertEquals
import org.junit.Test

class CalculatorImplTest {

    private val calculator = CalculatorImpl()

    @Test
    fun `simple addition works`() {
        calculator.clear()
        calculator.input("1")
        calculator.input("+")
        calculator.input("2")
        assertEquals("3", calculator.evaluate())
    }

    @Test
    fun `operator replacement works`() {
        calculator.clear()
        calculator.input("5")
        calculator.input("+")
        calculator.input("-") // should replace +
        calculator.input("2")
        assertEquals("3", calculator.evaluate())
    }

    @Test
    fun `single decimal per number`() {
        calculator.clear()
        calculator.input("1")
        calculator.input(".")
        calculator.input(".") // ignored
        calculator.input("5")
        assertEquals("1.5", calculator.evaluate())
    }

    @Test
    fun `implicit multiplication with function`() {
        calculator.clear()
        calculator.input("2")
        calculator.input("sin(")
        calculator.input("0")
        calculator.input(")")
        // 2 * sin(0) = 0
        assertEquals("0", calculator.evaluate())
    }

    @Test
    fun `power operator works`() {
        calculator.clear()
        calculator.input("2")
        calculator.input("^")
        calculator.input("3")
        assertEquals("8", calculator.evaluate())
    }

    @Test
    fun `parentheses and precedence work`() {
        calculator.clear()
        calculator.input("(")
        calculator.input("1")
        calculator.input("+")
        calculator.input("2")
        calculator.input(")")
        calculator.input("×")
        calculator.input("3")
        assertEquals("9", calculator.evaluate())
    }

    @Test
    fun `clear resets expression`() {
        calculator.clear()
        calculator.input("1")
        calculator.input("+")
        calculator.input("2")
        calculator.clear()
        assertEquals("0", calculator.evaluate())
    }
}


