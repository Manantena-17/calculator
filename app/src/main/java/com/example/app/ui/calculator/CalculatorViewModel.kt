package com.example.app.ui.calculator

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.app.data.HistoryRepository
import com.example.app.domain.ERROR
import com.example.app.domain.HistoryItem
import com.example.app.domain.appendToInput
import com.example.app.domain.evaluateExpression
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update

class CalculatorViewModel(
    private val repository: HistoryRepository = HistoryRepository()
) : ViewModel() {


    private val _state = MutableStateFlow(CalculatorUiState())


    val uiState: StateFlow<CalculatorUiState> =
        combine(_state, repository.history) { state, history ->
            state.copy(history = history)
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = CalculatorUiState()
        )


    fun onKey(key: String) {
        when (key) {
            "C" -> _state.update {
                it.copy(input = "", result = "0", justEvaluated = false)
            }
            "⌫" -> _state.update {
                it.copy(input = it.input.dropLast(1), justEvaluated = false)
            }
            "=" -> evaluate()
            else -> _state.update { current ->
                val startsNewNumber = key.single().isDigit() || key == "."

                val base = if (current.justEvaluated && startsNewNumber) "" else current.input
                current.copy(input = appendToInput(base, key), justEvaluated = false)
            }
        }
    }

    private fun evaluate() {
        val expression = _state.value.input
        if (expression.isEmpty()) return

        val result = evaluateExpression(expression)
        val success = result != ERROR

        _state.update {
            it.copy(
                result = result,
                input = if (success) result else "",
                justEvaluated = success
            )
        }


        if (success && expression != result) {
            repository.add(expression, result)
        }
    }



    fun showHistory() = _state.update { it.copy(showHistory = true) }

    fun hideHistory() = _state.update { it.copy(showHistory = false) }

    /** Reprend le résultat d'un ancien calcul pour continuer à calculer. */
    fun onHistoryItemClick(item: HistoryItem) {
        _state.update {
            it.copy(
                input = item.result,
                result = item.result,
                justEvaluated = true,
                showHistory = false
            )
        }
    }

    fun deleteHistoryItem(item: HistoryItem) = repository.delete(item.id)

    fun clearHistory() = repository.clear()
}