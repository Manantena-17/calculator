package com.example.app.domain

data class HistoryItem(
    val id: Long,
    val expression: String,
    val result: String,
    val timestamp: Long
)