package com.example.app

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.clickable
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.graphics.Color
data class Task(
    val id: Int,
    val title: String
)

@Composable
fun TodoScreen(modifier: Modifier = Modifier) {
    val tasks = remember { mutableStateListOf<Task>() }
    var nextId by remember { mutableStateOf(0) }

    fun addTask(title: String) {
        if (title.isNotBlank()) {
            tasks.add(Task(id = nextId, title = title.trim()))
            nextId++
        }
    }

    Column(
        modifier = modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        TodoInput(onAddTask = ::addTask)

        if (tasks.isEmpty()) {
            Text(text = "No tasks yet")
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                items(items = tasks, key = { task -> task.id }) { task ->
                    Text(text = "• ${task.title}")
                }
            }
        }
    }
}

@Composable
fun TodoInput(
    onAddTask: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var todoTitle by remember { mutableStateOf("") }

    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        OutlinedTextField(
            value = todoTitle,
            onValueChange = { todoTitle = it },
            label = { Text(text = "New task") },
            modifier = Modifier.weight(1f)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Button(
            onClick = {
                onAddTask(todoTitle)
                todoTitle = ""
            }
        ) {
            Text(text = "Add")
        }
    }
}
@Composable
fun TodoItem(
    task: Task,
    modifier: Modifier = Modifier
) {
    var isCompleted by remember { mutableStateOf(false) }

    Text(
        text = "• ${task.title}",
        color = Color.Red,
        modifier = modifier
    )
}