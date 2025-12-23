package com.example.calcapp.domain

import kotlin.math.E
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.ln
import kotlin.math.log10
import kotlin.math.pow
import kotlin.math.sin
import kotlin.math.sqrt
import kotlin.math.tan
import java.math.BigDecimal
import java.math.RoundingMode

class CalculatorImpl : Calculator {

    private var expression: String = "0"

    override fun input(symbol: String): String {
        // If last result was an error, start fresh on next input
        if (expression == "Error") {
            expression = "0"
        }

        val trimmed = symbol.trim()
        if (trimmed.isEmpty()) return expression

        val lastChar = expression.lastOrNull()

        fun append(text: String) {
            expression = if (expression == "0") text else expression + text
        }

        // Prevent multiple operators in a row (replace the previous one)
        if (isOperator(trimmed) && lastChar != null && isOperator(lastChar.toString())) {
            expression = expression.dropLast(1) + trimmed
            return expression
        }

        // Smart decimal: avoid multiple dots in the same number token
        if (trimmed == ".") {
            val lastNumber = expression.takeLastWhile { it.isDigit() || it == '.' }
            if (lastNumber.contains('.')) {
                return expression // ignore extra dot
            }
        }

        // Smart implicit multiplication:
        // number / ) / constant followed by function or '(' becomes: "2" + "*" + "sin(" etc.
        val isLastTokenMultipliable =
            lastChar != null && (lastChar.isDigit() || lastChar == ')' || lastChar == 'π')

        val isFunctionStart = trimmed in listOf("sin(", "cos(", "tan(", "ln(", "log(", "sqrt(")
        val isOpenParen = trimmed == "("

        if (isLastTokenMultipliable && (isFunctionStart || isOpenParen)) {
            append("*$trimmed")
        } else {
            if (expression == "0" && trimmed != "." && !isOperator(trimmed)) {
                expression = trimmed
            } else {
                expression += trimmed
            }
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
        } catch (_: Exception) {
            "Error"
        }
    }

    private fun isOperator(symbol: String): Boolean =
        symbol in listOf("+", "-", "×", "÷", "*", "/", "^")

    /**
     * Expression evaluator supporting:
     * - Operators: +, -, ×, ÷, *, /, ^ (power)
     * - Parentheses: ( )
     * - Constants: π, e
     * - Functions: sin, cos, tan, log (base 10), ln, sqrt
     *
     * Trigonometric functions expect input in **radians**.
     */
    private fun evaluateExpression(expr: String): String {
        if (expr.isBlank()) return "0"

        val normalized = expr
            .replace("×", "*")
            .replace("÷", "/")
            .replace("π", PI.toString())
            .replace("e", E.toString())

        val tokens = tokenize(normalized)
        val rpn = toRpn(tokens)
        val value = evalRpn(rpn)

        val bd = BigDecimal(value)
            .setScale(10, RoundingMode.HALF_UP)
            .stripTrailingZeros()

        return bd.toPlainString()
    }

    private fun tokenize(expr: String): List<String> {
        val tokens = mutableListOf<String>()
        var i = 0
        while (i < expr.length) {
            val c = expr[i]
            when {
                c.isWhitespace() -> i++

                c.isDigit() || c == '.' -> {
                    val sb = StringBuilder()
                    while (i < expr.length && (expr[i].isDigit() || expr[i] == '.')) {
                        sb.append(expr[i])
                        i++
                    }
                    tokens.add(sb.toString())
                }

                c.isLetter() -> {
                    val sb = StringBuilder()
                    while (i < expr.length && expr[i].isLetter()) {
                        sb.append(expr[i])
                        i++
                    }
                    tokens.add(sb.toString())
                }

                c == '+' || c == '-' || c == '*' || c == '/' || c == '^' -> {
                    // Handle unary minus (negative numbers)
                    if (c == '-' && (tokens.isEmpty() || tokens.last() in listOf("+", "-", "*", "/", "^", "(", ","))) {
                        tokens.add("u-")
                    } else {
                        tokens.add(c.toString())
                    }
                    i++
                }

                c == '(' || c == ')' || c == ',' -> {
                    tokens.add(c.toString())
                    i++
                }

                else -> throw IllegalArgumentException("Invalid character: $c")
            }
        }
        return tokens
    }

    private fun toRpn(tokens: List<String>): List<String> {
        val output = mutableListOf<String>()
        val stack = ArrayDeque<String>()

        val precedence = mapOf(
            "sin" to 5, "cos" to 5, "tan" to 5,
            "log" to 5, "ln" to 5, "sqrt" to 5,
            "u-" to 4,
            "^" to 3,
            "*" to 2, "/" to 2,
            "+" to 1, "-" to 1
        )

        fun isFunction(token: String) =
            token in listOf("sin", "cos", "tan", "log", "ln", "sqrt")

        fun isOperatorToken(token: String) =
            token in listOf("+", "-", "*", "/", "^", "u-")

        fun isRightAssociative(token: String) =
            token == "^" || token == "u-"

        for (token in tokens) {
            when {
                token.toDoubleOrNull() != null -> output.add(token)

                isFunction(token) -> stack.addLast(token)

                isOperatorToken(token) -> {
                    while (stack.isNotEmpty()) {
                        val top = stack.last()
                        if ((isOperatorToken(top) || isFunction(top)) &&
                            (precedence.getValue(top) > precedence.getValue(token) ||
                                    (precedence.getValue(top) == precedence.getValue(token) && !isRightAssociative(token)))
                        ) {
                            output.add(stack.removeLast())
                        } else {
                            break
                        }
                    }
                    stack.addLast(token)
                }

                token == "(" -> stack.addLast(token)

                token == ")" -> {
                    while (stack.isNotEmpty() && stack.last() != "(") {
                        output.add(stack.removeLast())
                    }
                    if (stack.isEmpty() || stack.last() != "(") {
                        throw IllegalArgumentException("Mismatched parentheses")
                    }
                    stack.removeLast() // '('
                    if (stack.isNotEmpty() && isFunction(stack.last())) {
                        output.add(stack.removeLast())
                    }
                }

                token == "," -> {
                    while (stack.isNotEmpty() && stack.last() != "(") {
                        output.add(stack.removeLast())
                    }
                }

                else -> throw IllegalArgumentException("Unknown token: $token")
            }
        }

        while (stack.isNotEmpty()) {
            val top = stack.removeLast()
            if (top == "(" || top == ")") {
                throw IllegalArgumentException("Mismatched parentheses")
            }
            output.add(top)
        }

        return output
    }

    private fun evalRpn(rpn: List<String>): Double {
        val stack = ArrayDeque<Double>()

        fun applyFunction(func: String, arg: Double): Double =
            when (func) {
                "sin" -> sin(arg)
                "cos" -> cos(arg)
                "tan" -> tan(arg)
                "log" -> log10(arg)
                "ln" -> ln(arg)
                "sqrt" -> sqrt(arg)
                else -> throw IllegalArgumentException("Unknown function: $func")
            }

        for (token in rpn) {
            when {
                token.toDoubleOrNull() != null -> stack.addLast(token.toDouble())

                token == "u-" -> {
                    val v = stack.removeLast()
                    stack.addLast(-v)
                }

                token in listOf("+", "-", "*", "/", "^") -> {
                    val b = stack.removeLast()
                    val a = stack.removeLast()
                    val res = when (token) {
                        "+" -> a + b
                        "-" -> a - b
                        "*" -> a * b
                        "/" -> a / b
                        "^" -> a.pow(b)
                        else -> 0.0
                    }
                    stack.addLast(res)
                }

                token in listOf("sin", "cos", "tan", "log", "ln", "sqrt") -> {
                    val v = stack.removeLast()
                    stack.addLast(applyFunction(token, v))
                }

                else -> throw IllegalArgumentException("Invalid RPN token: $token")
            }
        }

        if (stack.size != 1) {
            throw IllegalArgumentException("Invalid expression")
        }
        return stack.last()
    }
}



