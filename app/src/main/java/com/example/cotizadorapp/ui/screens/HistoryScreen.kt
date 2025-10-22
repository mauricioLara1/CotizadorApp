package com.example.cotizadorapp.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.cotizadorapp.viewmodel.ServicioViewModel
import com.example.cotizadorapp.model.ServicioEntity
import com.example.cotizadorapp.model.DetalleServicioEntity
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoryScreen(
    navController: NavController,
    viewModel: ServicioViewModel = viewModel()
) {
    // 🔹 Estados locales para manejar los datos
    var clienteBusqueda by remember { mutableStateOf("") }
    var resultados by remember { mutableStateOf(listOf<ServicioEntity>()) }
    var servicioSeleccionado by remember { mutableStateOf<ServicioEntity?>(null) }
    var detallesSeleccionados by remember { mutableStateOf(listOf<DetalleServicioEntity>()) }

    // 🔹 Necesitamos un scope para lanzar coroutines desde Compose
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        resultados = viewModel.obtenerServiciosPorCliente("") // <- vacío devuelve todos
    }

    Scaffold(
        topBar = { TopAppBar(title = { Text("Historial de Cotizaciones") }) }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.Top
        ) {

            // 🟦 BARRA DE BÚSQUEDA
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = clienteBusqueda,
                    onValueChange = { clienteBusqueda = it },
                    label = { Text("Buscar cliente") },
                    modifier = Modifier.weight(1f),
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Text
                    )
                )
                Spacer(modifier = Modifier.width(8.dp))
                Button(onClick = {
                    // 🔸 Se lanza la búsqueda en una coroutine
                    scope.launch {
                        resultados = viewModel.obtenerServiciosPorCliente(clienteBusqueda)
                        servicioSeleccionado = null
                        detallesSeleccionados = emptyList()
                    }
                }) {
                    Icon(Icons.Default.Search, contentDescription = "Buscar")
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 🟩 TABLA DE RESULTADOS
            if (resultados.isNotEmpty()) {
                Text(
                    text = "Resultados para \"$clienteBusqueda\"",
                    style = MaterialTheme.typography.titleMedium
                )

                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(max = 250.dp)
                        .padding(vertical = 8.dp)
                ) {
                    items(resultados) { servicio ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                                .clickable {
                                    // 🔸 Carga los detalles del servicio seleccionado
                                    scope.launch {
                                        servicioSeleccionado = servicio
                                        detallesSeleccionados =
                                            viewModel.obtenerDetallesPorServicio(servicio.servicioId)
                                    }
                                },
                            colors = CardDefaults.cardColors(
                                containerColor = if (servicioSeleccionado?.servicioId == servicio.servicioId)
                                    MaterialTheme.colorScheme.primaryContainer
                                else
                                    MaterialTheme.colorScheme.surfaceVariant
                            )
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(12.dp),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(servicio.cliente)
                                Text("$${servicio.costoTotal}")
                            }
                        }
                    }
                }
            }

            // 🟨 DETALLE DEL SERVICIO SELECCIONADO
            servicioSeleccionado?.let { servicio ->
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Descripción: ${servicio.descripcion}",
                    style = MaterialTheme.typography.bodyLarge
                )
                Text(
                    text = "Costo total: $${servicio.costoTotal}",
                    style = MaterialTheme.typography.bodyMedium
                )

                Spacer(modifier = Modifier.height(12.dp))

                // 🟧 LISTA DE DETALLES
                if (detallesSeleccionados.isNotEmpty()) {
                    Text(
                        text = "Detalles del servicio:",
                        style = MaterialTheme.typography.titleMedium
                    )
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f, fill = false)
                    ) {
                        items(detallesSeleccionados) { detalle ->
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp),
                                colors = CardDefaults.cardColors(
                                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                                )
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(12.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(detalle.descripcion)
                                    Text("$${detalle.costo}")
                                }
                            }
                        }
                    }
                } else {
                    Text(
                        text = "Este servicio no tiene detalles registrados.",
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        }
    }
}
