package org.lanzadera.proyectos.ui.components.dialogs

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Checkbox
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

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
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "Filtros de contenido",
                style = MaterialTheme.typography.titleLarge
            )
        },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
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
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("Aplicar")
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
