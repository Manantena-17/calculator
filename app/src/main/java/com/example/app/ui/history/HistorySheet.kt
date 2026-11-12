package com.example.app.ui.history

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.app.domain.HistoryItem
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistorySheet(
    history: List<HistoryItem>,
    onSelect: (HistoryItem) -> Unit,
    onDelete: (HistoryItem) -> Unit,
    onClear: () -> Unit,
    onDismiss: () -> Unit
) {
    ModalBottomSheet(onDismissRequest = onDismiss) {
        Column(modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)) {

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Historique", style = MaterialTheme.typography.titleLarge)
                if (history.isNotEmpty()) {
                    TextButton(onClick = onClear) { Text("Tout effacer") }
                }
            }

            if (history.isEmpty()) {
                Text(
                    text = "Aucun calcul pour le moment",
                    modifier = Modifier.padding(24.dp),
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            } else {
                LazyColumn {
                    items(history, key = { it.id }) { item ->
                        ListItem(
                            modifier = Modifier.clickable { onSelect(item) },
                            headlineContent = {
                                Text("${item.expression} = ${item.result}")
                            },
                            supportingContent = { Text(formatDate(item.timestamp)) },
                            trailingContent = {
                                TextButton(onClick = { onDelete(item) }) { Text("✕") }
                            }
                        )
                    }
                }
            }
        }
    }
}

private fun formatDate(timestamp: Long): String =
    SimpleDateFormat("dd/MM HH:mm", Locale.getDefault()).format(Date(timestamp))