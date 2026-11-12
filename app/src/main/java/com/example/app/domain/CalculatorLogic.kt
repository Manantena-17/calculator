package com.example.app.domain

import java.util.Locale

const val ERROR = "Erreur"
private val OPERATORS = setOf('+', '-', '*', '/')
// empecher doublemnt de arthmetic
fun appendToInput(input: String, key: String): String {
    val c = key.single()
    return when {
        c.isDigit() -> input + c

        c == '.' -> {
            val lastNumber = input.takeLastWhile { it.isDigit() || it == '.' }
            when {
                '.' in lastNumber -> input          // déjà un point
                lastNumber.isEmpty() -> input + "0."
                else -> input + "."
            }
        }

        c in OPERATORS -> {
            when {
                input.isEmpty() -> if (c == '-') "-" else input
                input.last() in OPERATORS -> {
                    if (c == '-' && input.last() in "*/") {
                        input + c                   // permet 5*-3
                    } else {
                        val trimmed = input.dropLastWhile { it in OPERATORS }
                        if (trimmed.isEmpty()) input else trimmed + c
                    }
                }
                else -> input + c
            }
        }

        else -> input
    }
}

// calcul
fun evaluateExpression(expression: String): String {
    return try {
        val result = ExpressionParser(expression).parse()
        if (result.isNaN() || result.isInfinite()) ERROR else formatResult(result)
    } catch (e: Exception) {
        ERROR
    }
}

private fun formatResult(value: Double): String {
    return if (value == value.toLong().toDouble()) {
        value.toLong().toString()
    } else {

        String.format(Locale.US, "%.10f", value).trimEnd('0').trimEnd('.')
    }
}

private class ExpressionParser(private val input: String) {
    private var pos = 0

    fun parse(): Double {
        val value = parseExpression()
        if (pos < input.length) throw IllegalArgumentException("Caractère inattendu")
        return value
    }


    private fun parseExpression(): Double {
        var result = parseTerm()
        while (pos < input.length) {
            when (input[pos]) {
                '+' -> { pos++; result += parseTerm() }
                '-' -> { pos++; result -= parseTerm() }
                else -> break
            }
        }
        return result
    }


    private fun parseTerm(): Double {
        var result = parseFactor()
        while (pos < input.length) {
            when (input[pos]) {
                '*' -> { pos++; result *= parseFactor() }
                '/' -> {
                    pos++
                    val divisor = parseFactor()
                    if (divisor == 0.0) throw ArithmeticException("Division par zéro")
                    result /= divisor
                }
                else -> break
            }
        }
        return result
    }

    private fun parseFactor(): Double {
        if (pos < input.length && input[pos] == '-') {
            pos++
            return -parseFactor()
        }
        val start = pos
        while (pos < input.length && (input[pos].isDigit() || input[pos] == '.')) pos++
        if (start == pos) throw IllegalArgumentException("Nombre attendu")
        return input.substring(start, pos).toDouble()
    }
}