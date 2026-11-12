package com.example.app

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


@Composable
fun CalculatorApp(modifier: Modifier = Modifier) {
    var currentInput by remember { mutableStateOf("") }
    var currentResult by remember { mutableStateOf("0") }

    val numbers = listOf("7", "8", "9", "4", "5", "6", "1", "2", "3", "0", ".")
    val calculatorSymbols = listOf("+", "-", "*", "/", "C", "=")

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp),
        verticalArrangement = Arrangement.Bottom
    ) {
        CalculatorDisplay(
            input = currentInput.ifEmpty { "0" },
            result = currentResult
        )

        Spacer(modifier = Modifier.height(24.dp))

        CalculatorKeypad(
            numbers = numbers,
            symbols = calculatorSymbols,
            onItemClick = { value ->
                when (value) {
                    "C" -> {
                        currentInput = ""
                        currentResult = "0"
                    }
                    "=" -> {
                        if (currentInput.isNotEmpty()) {
                            val result = evaluateExpression(currentInput)
                            currentResult = result
                            currentInput = if (result == "Erreur") "" else result
                        }
                    }
                    else -> {
                        currentInput += value
                    }
                }
            }
        )
    }
}


@Composable
fun CalculatorDisplay(input: String, result: String) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(140.dp),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.End
        ) {
            Text(
                text = input,
                fontSize = 24.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
                textAlign = TextAlign.End,
                maxLines = 2
            )

            Text(
                text = result,
                fontSize = 42.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.End,
                maxLines = 1
            )
        }
    }
}


@Composable
fun CalculatorKeypad(
    numbers: List<String>,
    symbols: List<String>,
    onItemClick: (String) -> Unit
) {
    val allItems = numbers + symbols

    LazyVerticalGrid(
        columns = GridCells.Fixed(4),
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        items(allItems) { item ->
            CalculatorButton(text = item, onClick = { onItemClick(item) })
        }
    }
}

@Composable
fun CalculatorButton(text: String, onClick: () -> Unit) {
    val isOperator = text in listOf("+", "-", "*", "/", "C", "=")
    val buttonColor = if (isOperator) {
        MaterialTheme.colorScheme.primaryContainer
    } else {
        MaterialTheme.colorScheme.surface
    }

    Box(
        modifier = Modifier
            .aspectRatio(1f)
            .clip(RoundedCornerShape(16.dp))
            .background(buttonColor)
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            fontSize = 24.sp,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}


fun evaluateExpression(expression: String): String {
    return try {
        val result = ExpressionParser(expression).parse()
        if (result.isNaN() || result.isInfinite()) {
            "Erreur"
        } else {
            formatResult(result)
        }
    } catch (e: Exception) {
        "Erreur"
    }
}

private fun formatResult(value: Double): String {
    return if (value == value.toLong().toDouble()) {
        value.toLong().toString()
    } else {

        "%.10f".format(value).trimEnd('0').trimEnd('.')
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