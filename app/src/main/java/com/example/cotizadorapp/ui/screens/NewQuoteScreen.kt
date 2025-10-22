package com.example.cotizadorapp.ui.screens

// --- IMPORTS BASE DE DATOS ---
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.cotizadorapp.viewmodel.ServicioViewModel
import com.example.cotizadorapp.model.ServicioEntity
import com.example.cotizadorapp.model.DetalleServicioEntity

// --- GENERAR PDF ---
import com.example.cotizadorapp.pdf.PdfGenerator

//scope para pdf
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.launch


// --- IMPORTS UI ---
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import android.widget.Toast
import com.example.cotizadorapp.data.db.AppDatabase


/**
 * Pantalla: Nueva Cotización
 *
 * Esta pantalla permite:
 * - Ingresar los datos generales del cliente y del servicio.
 * - Agregar múltiples detalles de servicios (con su costo).
 * - Guardar todo en la base de datos Room.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewQuoteScreen(navController: NavController) {

    val coroutineScope = rememberCoroutineScope()


    // 🔹 ViewModel que maneja la lógica de base de datos
    val viewModel: ServicioViewModel = viewModel()

    // 🔹 Contexto para mostrar Toasts (mensajes)
    val context = LocalContext.current

    // --- VARIABLES PRINCIPALES ---
    var cliente by remember { mutableStateOf("") }
    var descripcion by remember { mutableStateOf("") }
    var costoTotal by remember { mutableStateOf("") }

    // --- CAMPOS PARA AÑADIR DETALLES ---
    var servicioDescripcion by remember { mutableStateOf("") }
    var servicioCosto by remember { mutableStateOf("") }

    // --- LISTA DE DETALLES ---
    var detalles by remember { mutableStateOf(mutableListOf<Pair<String, Double>>()) }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Nueva Cotización") })
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.Top
        ) {

            // 🧾 --- CAMPOS GENERALES ---
            OutlinedTextField(
                value = cliente,
                onValueChange = { cliente = it },
                label = { Text("Nombre del cliente") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = descripcion,
                onValueChange = { descripcion = it },
                label = { Text("Descripción general") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = costoTotal,
                onValueChange = { costoTotal = it },
                label = { Text("Costo total") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            // 🧩 --- SECCIÓN DE DETALLES ---
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                OutlinedTextField(
                    value = servicioDescripcion,
                    onValueChange = { servicioDescripcion = it },
                    label = { Text("Servicio") },
                    modifier = Modifier.weight(1f)
                )
                Spacer(modifier = Modifier.width(8.dp))
                OutlinedTextField(
                    value = servicioCosto,
                    onValueChange = { servicioCosto = it },
                    label = { Text("Costo") },
                    modifier = Modifier.width(120.dp),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number
                    )
                )
                Spacer(modifier = Modifier.width(8.dp))

                // ➕ BOTÓN AGREGAR DETALLE
                Button(
                    onClick = {
                        val costo = servicioCosto.toDoubleOrNull()
                        if (servicioDescripcion.isNotBlank() && costo != null) {
                            detalles = detalles.toMutableList().apply {
                                add(servicioDescripcion to costo)
                            }
                            servicioDescripcion = ""
                            servicioCosto = ""
                        } else {
                            Toast.makeText(context, "Complete los campos correctamente", Toast.LENGTH_SHORT).show()
                        }
                    }
                ) {
                    Text("+")
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // 🧮 --- LISTA DE DETALLES ---
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) {
                items(detalles) { (desc, cost) ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(desc, style = MaterialTheme.typography.bodyLarge)
                                Text("Costo: $${cost}", style = MaterialTheme.typography.bodyMedium)
                            }

                            // ❌ ELIMINAR DETALLE
                            IconButton(
                                onClick = {
                                    detalles = detalles.toMutableList().apply {
                                        remove(desc to cost)
                                    }
                                }
                            ) {
                                Icon(Icons.Default.Delete, contentDescription = "Eliminar")
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // 💾 --- BOTÓN GUARDAR COTIZACIÓN ---
            Button(
                onClick = {
                    if (cliente.isBlank() || descripcion.isBlank() || costoTotal.isBlank()) {
                        Toast.makeText(context, "Complete todos los campos", Toast.LENGTH_SHORT).show()
                        return@Button
                    }

                    val servicio = ServicioEntity(
                        perfilId = 1L,
                        cliente = cliente,
                        descripcion = descripcion,
                        costoTotal = costoTotal.toDoubleOrNull() ?: 0.0
                    )

                    val listaDetalles = detalles.map {
                        DetalleServicioEntity(
                            servicioId = 0L,
                            descripcion = it.first,
                            costo = it.second
                        )
                    }

                    coroutineScope.launch {
                        // Guardar servicio y detalles en la DB
                        viewModel.guardarServicioConDetalles(servicio, listaDetalles)

                        // Obtener perfil (suspend)
                        val db = AppDatabase.getInstance(context)
                        val perfilDao = db.perfilDao()
                        val perfil = perfilDao.getAll().firstOrNull()

                        if (perfil != null) {
                            val pdfGenerator = PdfGenerator()
                            val archivo = pdfGenerator.generarPdf(context, perfil, servicio, listaDetalles)

                            if (archivo != null) {
                                Toast.makeText(context, "PDF guardado en: ${archivo.absolutePath}", Toast.LENGTH_LONG).show()
                            } else {
                                Toast.makeText(context, "Error al crear el PDF", Toast.LENGTH_LONG).show()
                            }
                        } else {
                            Toast.makeText(context, "No hay perfil registrado", Toast.LENGTH_SHORT).show()
                        }

                        // Limpiar campos
                        cliente = ""
                        descripcion = ""
                        costoTotal = ""
                        detalles = mutableListOf()
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Guardar Cotización")
            }


        }
    }
}
