package com.example.quizresistencias

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

data class BandaColor(
    val nombre: String,
    val valor: Int? = null,
    val multiplicador: Double? = null,
    val tolerancia: String? = null
)

val colores = listOf(
    BandaColor("Negro", 0, 1.0),
    BandaColor("Marrón", 1, 10.0, "±1%"),
    BandaColor("Rojo", 2, 100.0, "±2%"),
    BandaColor("Naranja", 3, 1000.0),
    BandaColor("Amarillo", 4, 10000.0),
    BandaColor("Verde", 5, 100000.0, "±0.5%"),
    BandaColor("Azul", 6, 1000000.0, "±0.25%"),
    BandaColor("Violeta", 7, 10000000.0, "±0.1%"),
    BandaColor("Gris", 8, 100000000.0, "±0.05%"),
    BandaColor("Blanco", 9, 1000000000.0),
    BandaColor("Dorado", null, 0.1, "±5%"),
    BandaColor("Plateado", null, 0.01, "±10%"),
    BandaColor("Ninguno", null, null, "±20%")
)

@Composable
fun Resistor() {
    var banda1 by remember { mutableStateOf<BandaColor?>(null) }
    var banda2 by remember { mutableStateOf<BandaColor?>(null) }
    var banda3 by remember { mutableStateOf<BandaColor?>(null) }
    var tolerancia by remember { mutableStateOf<BandaColor?>(null) }

    var resultado by remember { mutableStateOf<String?>(null) }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Elige los colores para calcular los datos de la resistencia.:", fontSize = 18.sp)

            Spacer(modifier = Modifier.height(40.dp))

            Dropdown("Banda 1", colores.take(10)) { banda1 = it }
            Spacer(modifier = Modifier.height(10.dp))
            Dropdown("Banda 2", colores.take(10)) { banda2 = it }
            Spacer(modifier = Modifier.height(10.dp))
            Dropdown("Banda 3", colores.subList(0, 5)) { banda3 = it }
            Spacer(modifier = Modifier.height(10.dp))
            Dropdown("Banda 4", colores.takeLast(3)) { tolerancia = it }

            Spacer(modifier = Modifier.height(25.dp))

            Button(
                onClick = {

                    if (banda1 == null || banda2 == null || banda3 == null || tolerancia == null) {
                        resultado = "Falta elegir algun color"
                    } else if (banda1 != null && banda2 != null && banda3 != null) {
                        val valor = ((banda1!!.valor!! * 10 + banda2!!.valor!!) * banda3!!.multiplicador!!).toInt()
                        val toleranciaTexto = tolerancia?.tolerancia ?: ""
                        resultado = "Valor: $valor Ω $toleranciaTexto"
                    } else {
                        resultado = "Intenta de nuevo"
                    }
                },

                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFFF9800),
                    contentColor = Color.White
                ),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Calcular")
            }

            Spacer(modifier = Modifier.height(16.dp))

            resultado?.let {
                Text(it, fontSize = 20.sp)
            }
        }
    }
}


@Composable
fun Dropdown(
    titulo: String,
    opciones: List<BandaColor>,
    onColorSeleccionado: (BandaColor) -> Unit
) {
    var expandido by remember { mutableStateOf(false) }
    var seleccionado by remember { mutableStateOf(titulo) }

    Box {
        OutlinedButton(
            onClick = { expandido = true },
            colors = ButtonDefaults.outlinedButtonColors(
                containerColor = Color(0xFF8BC34A), // limeGreen
                contentColor = Color.White
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp)
        ) {
            Text(seleccionado)
            Icon(
                imageVector = Icons.Default.ArrowDropDown,
                contentDescription = "Dropdown icon"
            )
        }

        DropdownMenu(
            expanded = expandido,
            onDismissRequest = { expandido = false }
        ) {
            opciones.forEach { color ->
                DropdownMenuItem(
                    onClick = {
                        seleccionado = color.nombre
                        onColorSeleccionado(color)
                        expandido = false
                    },
                    text = { Text(color.nombre) }
                )
            }
        }
    }
}