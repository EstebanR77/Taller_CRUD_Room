package com.ejemplo.crudroom

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { MaterialTheme { PantallaProductos() } }
    }
}

@Composable
fun PantallaProductos() {
    val contexto = LocalContext.current
    val dao = remember { AppDatabase.obtener(contexto).productoDao() }
    val productos by dao.observarProductos().collectAsState(initial = emptyList())
    val scope = rememberCoroutineScope()
    var nombre by remember { mutableStateOf("") }
    var precio by remember { mutableStateOf("") }
    var editando by remember { mutableStateOf<Producto?>(null) }
    var error by remember { mutableStateOf("") }

    fun limpiar() {
        nombre = ""
        precio = ""
        editando = null
        error = ""
    }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("CRUD de productos", style = MaterialTheme.typography.headlineMedium)
        Spacer(Modifier.height(12.dp))
        OutlinedTextField(
            value = nombre, onValueChange = { nombre = it },
            label = { Text("Nombre del producto") },
            modifier = Modifier.fillMaxWidth(), singleLine = true
        )
        Spacer(Modifier.height(8.dp))
        OutlinedTextField(
            value = precio, onValueChange = { precio = it },
            label = { Text("Precio") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
            modifier = Modifier.fillMaxWidth(), singleLine = true
        )
        if (error.isNotEmpty()) Text(error, color = MaterialTheme.colorScheme.error)
        Spacer(Modifier.height(8.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Button(onClick = {
                val numero = precio.replace(',', '.').toDoubleOrNull()
                if (nombre.isBlank() || numero == null || !numero.isFinite() || numero < 0) {
                    error = "Escribe un nombre y un precio válido."
                } else {
                    val producto = Producto(editando?.id ?: 0, nombre.trim(), numero)
                    scope.launch {
                        if (editando == null) dao.insertar(producto)
                        else dao.actualizar(producto)
                        limpiar()
                    }
                }
            }) { Text(if (editando == null) "Guardar" else "Actualizar") }
            if (editando != null) OutlinedButton(onClick = { limpiar() }) {
                Text("Cancelar")
            }
        }
        Spacer(Modifier.height(16.dp))
        Text("Productos registrados: ${productos.size}")
        Spacer(Modifier.height(8.dp))
        LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            items(productos, key = { it.id }) { producto ->
                Card(modifier = Modifier.fillMaxWidth()) {
                    Column(Modifier.padding(12.dp)) {
                        Text(producto.nombre, style = MaterialTheme.typography.titleMedium)
                        Text("Precio: $${"%.2f".format(producto.precio)}")
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            TextButton(onClick = {
                                editando = producto
                                nombre = producto.nombre
                                precio = producto.precio.toString()
                                error = ""
                            }) { Text("Editar") }
                            TextButton(onClick = {
                                scope.launch {
                                    dao.eliminar(producto)
                                    if (editando?.id == producto.id) limpiar()
                                }
                            }) { Text("Eliminar") }
                        }
                    }
                }
            }
        }
    }
}
