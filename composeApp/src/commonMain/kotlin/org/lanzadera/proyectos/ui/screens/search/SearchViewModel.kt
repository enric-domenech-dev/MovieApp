package org.lanzadera.proyectos.ui.screens.search

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SearchViewModel(
    private val coroutineScope: CoroutineScope = CoroutineScope(Dispatchers.Default)
) {

    private val _query = MutableStateFlow("")
    val query: StateFlow<String> = _query.asStateFlow()

    private val _results = MutableStateFlow<List<String>>(emptyList())
    val results: StateFlow<List<String>> = _results.asStateFlow()

    // Actualizar el texto de búsqueda
    fun onQueryChanged(newQuery: String) {
        _query.value = newQuery
    }

    // Función placeholder para realizar la búsqueda (futura integración con repositorio)
    fun performSearch() {
        val q = _query.value.trim()
        if (q.isEmpty()) {
            _results.value = emptyList()
            return
        }

        // Simular búsqueda rápida: devuelve una lista de strings con el término
        coroutineScope.launch {
            // En una implementación real aquí se llamaría a un repositorio/servicio
            _results.value = listOf(
                "Resultado para: \"$q\" - Ejemplo 1",
                "Resultado para: \"$q\" - Ejemplo 2",
                "Resultado para: \"$q\" - Ejemplo 3"
            )
        }
    }

    // Limpiar resultados y query
    fun clear() {
        _query.value = ""
        _results.value = emptyList()
    }

}