package com.example.rebu.presentation

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.net.URL

class PaqueteActivoViewModel : ViewModel() {
    var cliente by mutableStateOf("Sin pedido activo")
        private set
    var direccion by mutableStateOf("")
        private set
    var lat by mutableStateOf(0.0)
        private set
    var lng by mutableStateOf(0.0)
        private set
    var hayPedido by mutableStateOf(false)
        private set

    fun iniciarPolling() {
        viewModelScope.launch {
            while (true) {
                try {
                    val json = withContext(Dispatchers.IO) {
                        // ⚠️ Asegúrate de que esta IP sea la de tu PC en tu red WiFi actual
                        val texto = URL("http://192.168.100.100:5000/paquete-activo").readText()
                        JSONObject(texto)
                    }

                    // Verificamos que tenga cliente y que no esté vacío
                    if (json.has("cliente") && json.optString("cliente").isNotEmpty()) {
                        cliente = json.optString("cliente")
                        direccion = json.optString("direccion")
                        lat = json.optDouble("lat", 0.0)
                        lng = json.optDouble("lng", 0.0)
                        hayPedido = true
                    } else {
                        // Si el JSON viene vacío, reseteamos la pantalla
                        limpiarPedido()
                    }
                } catch (e: Exception) {
                    Log.e("PaqueteActivo", "Error al obtener paquete activo", e)

                    // ¡TRUCO DE DEBUGGING!
                    // Mostramos el error real en la pantalla del reloj
                    cliente = "Error: ${e.localizedMessage}"
                    hayPedido = true // Forzamos a que la pantalla se actualice
                }
                delay(5000)
            }
        }
    }

    // Función auxiliar para resetear los valores
    private fun limpiarPedido() {
        hayPedido = false
        cliente = "Sin pedido activo"
        direccion = ""
        lat = 0.0
        lng = 0.0
    }
}