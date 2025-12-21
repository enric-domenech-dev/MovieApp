package org.lanzadera.proyectos.ui.components.dialogs

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties

@Composable
fun FavoritesFilterDialog(
    showAvailableSeries: Boolean,
    showUpcomingSeries: Boolean,
    showInProductionSeries: Boolean,
    showEndedSeries: Boolean,
    showAvailableMovies: Boolean,
    showUpcomingMovies: Boolean,
    onAvailableSeriesToggle: () -> Unit,
    onUpcomingSeriesToggle: () -> Unit,
    onInProductionSeriesToggle: () -> Unit,
    onEndedSeriesToggle: () -> Unit,
    onAvailableMoviesToggle: () -> Unit,
    onUpcomingMoviesToggle: () -> Unit,
    onDismiss: () -> Unit
) {
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            usePlatformDefaultWidth = false,
            dismissOnClickOutside = false,
            dismissOnBackPress = true
        ),
        content = {
            Card(
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .fillMaxHeight(0.94f)
                    .padding(horizontal = 24.dp, vertical = 28.dp),
                shape = CardDefaults.shape,
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "Filtros de favoritos",
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.SemiBold,
                                color = MaterialTheme.colorScheme.primary
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Filtra tu lista por estado de series y películas",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }

                        IconButton(
                            onClick = onDismiss,
                            modifier = Modifier.padding(8.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Close,
                                contentDescription = "Cerrar",
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .verticalScroll(rememberScrollState()),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        // Series section
                        Text(
                            text = "Series",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.primary
                        )

                        FilterCheckboxItem(
                            checked = showAvailableSeries,
                            onCheckedChange = { onAvailableSeriesToggle() },
                            label = "Disponibles"
                        )

                        FilterCheckboxItem(
                            checked = showUpcomingSeries,
                            onCheckedChange = { onUpcomingSeriesToggle() },
                            label = "En emisión"
                        )

                        FilterCheckboxItem(
                            checked = showInProductionSeries,
                            onCheckedChange = { onInProductionSeriesToggle() },
                            label = "En producción"
                        )

                        FilterCheckboxItem(
                            checked = showEndedSeries,
                            onCheckedChange = { onEndedSeriesToggle() },
                            label = "Finalizadas"
                        )

                        Spacer(modifier = Modifier.height(8.dp))
                        HorizontalDivider()
                        Spacer(modifier = Modifier.height(8.dp))

                        // Movies section
                        Text(
                            text = "Películas",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.primary
                        )

                        FilterCheckboxItem(
                            checked = showAvailableMovies,
                            onCheckedChange = { onAvailableMoviesToggle() },
                            label = "Disponibles"
                        )

                        FilterCheckboxItem(
                            checked = showUpcomingMovies,
                            onCheckedChange = { onUpcomingMoviesToggle() },
                            label = "Próximas"
                        )
                    }
                }
            }
        }
    )
}


@Composable
private fun FilterCheckboxItem(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    label: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(
            checked = checked,
            onCheckedChange = onCheckedChange
        )
        Text(
            text = label,
            style = MaterialTheme.typography.bodyLarge
        )
    }
}
