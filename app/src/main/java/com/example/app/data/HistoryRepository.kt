package com.example.app.data

import com.example.app.domain.HistoryItem
import java.util.concurrent.atomic.AtomicLong
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class HistoryRepository(private val maxSize: Int = 50) {

   // cache lit vie en memoire
    private val _history = MutableStateFlow<List<HistoryItem>>(emptyList())
    val history: StateFlow<List<HistoryItem>> = _history.asStateFlow()

    private val nextId = AtomicLong(0)

    fun add(expression: String, result: String) {
        val item = HistoryItem(
            id = nextId.getAndIncrement(),
            expression = expression,
            result = result,
            timestamp = System.currentTimeMillis()
        )
       //plu recent en premier
        _history.update { (listOf(item) + it).take(maxSize) }
    }

    fun delete(id: Long) {
        _history.update { list -> list.filterNot { it.id == id } }
    }

    fun clear() {
        _history.value = emptyList()
    }
}