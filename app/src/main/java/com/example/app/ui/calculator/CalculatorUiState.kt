package com.example.app.ui.calculator

import com.example.app.domain.HistoryItem

data class CalculatorUiState(
    val input: String = "",
    val result: String = "0",
    val justEvaluated: Boolean = false,
    val history: List<HistoryItem> = emptyList(),
    val showHistory: Boolean = false
)