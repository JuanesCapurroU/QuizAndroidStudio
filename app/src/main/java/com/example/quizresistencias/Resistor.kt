package com.example.quizresistencias

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
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
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    listOf(Color(0xFFE8F5E9), Color(0xFFC8E6C9))
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .padding(24.dp)
                .fillMaxWidth()
                .shadow(4.dp, shape = RoundedCornerShape(16.dp))
                .background(Color.White, RoundedCornerShape(16.dp))
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                "Calculadora de Resistencias",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF2E7D32)
            )

            Spacer(modifier = Modifier.height(24.dp))

            Dropdown("Banda 1", colores.take(10)) { banda1 = it }
            Dropdown("Banda 2", colores.take(10)) { banda2 = it }
            Dropdown("Multiplicador", colores.subList(0, 5)) { banda3 = it }
            Dropdown("Tolerancia", colores.takeLast(3)) { tolerancia = it }

            Spacer(modifier = Modifier.height(30.dp))

            Button(
                onClick = {
                    if (banda1 == null || banda2 == null || banda3 == null || tolerancia == null) {
                        resultado = "Falta elegir algún color."
                    } else {
                        val valor = ((banda1!!.valor!! * 10 + banda2!!.valor!!) * banda3!!.multiplicador!!).toInt()
                        val toleranciaTexto = tolerancia?.tolerancia ?: ""
                        resultado = "Valor: $valor Ω $toleranciaTexto"
                    }
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF4CAF50),
                    contentColor = Color.White
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .animateContentSize(),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Calcular", fontSize = 18.sp)
            }

            Spacer(modifier = Modifier.height(20.dp))

            resultado?.let {
                Text(
                    it,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF1B5E20),
                    modifier = Modifier.animateContentSize()
                )
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

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
    ) {
        OutlinedButton(
            onClick = { expandido = true },
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp)
                .animateContentSize(),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.outlinedButtonColors(
                containerColor = Color(0xFFDCEDC8),
                contentColor = Color.Black
            )
        ) {
            Text(seleccionado, fontSize = 16.sp, modifier = Modifier.weight(1f))
            Icon(
                imageVector = Icons.Default.ArrowDropDown,
                contentDescription = "Dropdown icon"
            )
        }

        DropdownMenu(
            expanded = expandido,
            onDismissRequest = { expandido = false },
            modifier = Modifier.background(Color.White)
        ) {
            opciones.forEach { color ->
                DropdownMenuItem(
                    onClick = {
                        seleccionado = color.nombre
                        onColorSeleccionado(color)
                        expandido = false
                    },
                    text = {
                        Text(color.nombre, fontSize = 15.sp)
                    }
                )
            }
        }
    }
}
