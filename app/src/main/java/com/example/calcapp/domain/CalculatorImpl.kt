package com.example.calcapp.domain

import java.math.BigDecimal
import java.math.RoundingMode

class CalculatorImpl : Calculator {

    private var expression: String = "0"

    override fun input(symbol: String): String {
        if (expression == "0" && symbol != "." && !isOperator(symbol)) {
            expression = symbol
        } else {
            expression += symbol
        }
        return expression
    }

    override fun clear(): String {
        expression = "0"
        return expression
    }

    override fun delete(): String {
        expression = if (expression.length <= 1) "0" else expression.dropLast(1)
        return expression
    }

    override fun evaluate(): String {
        return try {
            val result = evaluateExpression(expression)
            expression = result
            result
        } catch (e: Exception) {
            "Error"
        }
    }

    private fun isOperator(symbol: String): Boolean =
        symbol in listOf("+", "-", "×", "÷", "*", "/")

    /**
     * Very small and naive expression evaluator for +, -, ×, ÷
     * Not for production use, but good enough for a demo calculator.
     */
    private fun evaluateExpression(expr: String): String {
        // Normalize operators
        val normalized = expr.replace("×", "*").replace("÷", "/")

        // Tokenize
        val tokens = mutableListOf<String>()
        var current = StringBuilder()
        for (ch in normalized) {
            if (ch in listOf('+', '-', '*', '/')) {
                if (current.isNotEmpty()) {
                    tokens.add(current.toString())
                    current = StringBuilder()
                }
                tokens.add(ch.toString())
            } else {
                current.append(ch)
            }
        }
        if (current.isNotEmpty()) tokens.add(current.toString())

        // Handle * and / first
        val multDivReduced = mutableListOf<String>()
        var i = 0
        while (i < tokens.size) {
            val token = tokens[i]
            if (token == "*" || token == "/") {
                val left = BigDecimal(multDivReduced.removeLast())
                val right = BigDecimal(tokens[i + 1])
                val res = if (token == "*") {
                    left.multiply(right)
                } else {
                    left.divide(right, 10, RoundingMode.HALF_UP)
                }
                multDivReduced.add(res.stripTrailingZeros().toPlainString())
                i += 2
            } else {
                multDivReduced.add(token)
                i++
            }
        }

        // Now handle + and -
        var result = BigDecimal(multDivReduced[0])
        i = 1
        while (i < multDivReduced.size) {
            val op = multDivReduced[i]
            val value = BigDecimal(multDivReduced[i + 1])
            result = when (op) {
                "+" -> result.add(value)
                "-" -> result.subtract(value)
                else -> result
            }
            i += 2
        }

        return result.stripTrailingZeros().toPlainString()
    }
}


