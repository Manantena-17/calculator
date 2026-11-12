package com.example.app.ui.calculator

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.app.ui.history.HistorySheet

private val keypadRows = listOf(
    listOf("C", "⌫", "/", "*"),
    listOf("7", "8", "9", "-"),
    listOf("4", "5", "6", "+"),
    listOf("1", "2", "3", "."),
    listOf("0", "=")
)

// liaon de viemodel ecran
@Composable
fun CalculatorRoute(
    modifier: Modifier = Modifier,
    viewModel: CalculatorViewModel = viewModel()
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    CalculatorScreen(
        state = state,
        onKey = viewModel::onKey,
        onHistoryClick = viewModel::showHistory,
        modifier = modifier
    )

    if (state.showHistory) {
        HistorySheet(
            history = state.history,
            onSelect = viewModel::onHistoryItemClick,
            onDelete = viewModel::deleteHistoryItem,
            onClear = viewModel::clearHistory,
            onDismiss = viewModel::hideHistory
        )
    }
}


// ecran
@Composable
fun CalculatorScreen(
    state: CalculatorUiState,
    onKey: (String) -> Unit,
    onHistoryClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    BoxWithConstraints(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        val isLandscape = maxWidth > maxHeight
        val displayInput = state.input.ifEmpty { "0" }

        if (isLandscape) {
          // affcihage tablette a gauche et largment
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .widthIn(max = 900.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                CalculatorDisplay(
                    input = displayInput,
                    result = state.result,
                    onHistoryClick = onHistoryClick,
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                )
                CalculatorKeypad(
                    onKey = onKey,
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                )
            }
        } else {
         // claveir en haut et en ba
            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .widthIn(max = 480.dp)
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                CalculatorDisplay(
                    input = displayInput,
                    result = state.result,
                    onHistoryClick = onHistoryClick,
                    modifier = Modifier
                        .weight(0.28f)
                        .fillMaxWidth()
                )
                CalculatorKeypad(
                    onKey = onKey,
                    modifier = Modifier
                        .weight(0.72f)
                        .fillMaxWidth()
                )
            }
        }
    }
}

// affichage
@Composable
fun CalculatorDisplay(
    input: String,
    result: String,
    onHistoryClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    // diminution de taille depent a la longeur de nombre
    val resultSize = when {
        result.length > 12 -> 28.sp
        result.length > 8 -> 36.sp
        else -> 48.sp
    }

    Card(
        modifier = modifier,
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 12.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.End
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.Top
            ) {
                TextButton(onClick = onHistoryClick) {
                    Text("Historique")
                }
                Text(
                    text = input,
                    modifier = Modifier
                        .weight(1f)
                        .padding(top = 8.dp, end = 8.dp),
                    fontSize = 24.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
                    textAlign = TextAlign.End,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }
            Text(
                text = result,
                modifier = Modifier.padding(end = 8.dp, bottom = 8.dp),
                fontSize = resultSize,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.End,
                maxLines = 1,
                softWrap = false
            )
        }
    }
}


@Composable
fun CalculatorKeypad(
    onKey: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        keypadRows.forEach { row ->
            Row(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                row.forEach { key ->
                    val weight = if (key == "0" || key == "=") 2f else 1f
                    CalculatorButton(
                        text = key,
                        onClick = { onKey(key) },
                        modifier = Modifier
                            .weight(weight)
                            .fillMaxHeight()
                    )
                }
            }
        }
    }
}
// bouton
@Composable
fun CalculatorButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = MaterialTheme.colorScheme

    val (background, content) = when (text) {
        "C", "⌫" -> colors.errorContainer to colors.onErrorContainer
        "=" -> colors.primary to colors.onPrimary
        "+", "-", "*", "/" -> colors.secondaryContainer to colors.onSecondaryContainer
        else -> colors.surfaceVariant.copy(alpha = 0.6f) to colors.onSurface
    }

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(20.dp))
            .background(background)
            .clickable(role = Role.Button, onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            fontSize = 26.sp,
            fontWeight = FontWeight.Medium,
            color = content
        )
    }
}