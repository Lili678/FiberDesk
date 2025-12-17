package com.example.fiberdesk_app.ui.settings

import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.fiberdesk_app.databinding.ActivityServerConfigBinding
import com.example.fiberdesk_app.network.NetworkPreferences
import com.example.fiberdesk_app.network.ServerDetector
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ServerConfigActivity : AppCompatActivity() {
    
    private lateinit var binding: ActivityServerConfigBinding
    private var isDetecting = false
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityServerConfigBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            title = "Configuración del Servidor"
        }
        
        loadCurrentConfig()
        setupListeners()
    }
    
    private fun loadCurrentConfig() {
        // Cargar configuración guardada
        val savedIP = NetworkPreferences.getServerIP(this)
        val savedPort = NetworkPreferences.getServerPort(this)
        val autoDetect = NetworkPreferences.isAutoDetectEnabled(this)
        val remoteUrl = NetworkPreferences.getRemoteUrl(this)
        val useRemoteOnMobile = NetworkPreferences.isUseRemoteOnMobileEnabled(this)
        
        binding.editTextServerIP.setText(savedIP ?: "")
        binding.editTextServerPort.setText(savedPort)
        binding.switchAutoDetect.isChecked = autoDetect
        binding.editTextRemoteUrl.setText(remoteUrl ?: "")
        binding.switchUseRemoteOnMobile.isChecked = useRemoteOnMobile
        
        // Mostrar IP local del dispositivo
        val localIP = ServerDetector.getLocalIPAddress(this)
        binding.textViewDeviceIP.text = "IP de este dispositivo: ${localIP ?: "No disponible"}"
        
        updateUIState(autoDetect)
    }
    
    private fun setupListeners() {
        binding.switchAutoDetect.setOnCheckedChangeListener { _, isChecked ->
            updateUIState(isChecked)
        }
        
        binding.buttonDetectServer.setOnClickListener {
            detectServer()
        }
        
        binding.buttonSaveConfig.setOnClickListener {
            saveConfiguration()
        }
        
        binding.buttonTestConnection.setOnClickListener {
            testConnection()
        }
    }
    
    private fun updateUIState(autoDetect: Boolean) {
        binding.editTextServerIP.isEnabled = !autoDetect
        binding.buttonDetectServer.isEnabled = autoDetect
        
        if (autoDetect) {
            binding.textViewConfigInfo.text = 
                "La aplicación detectará automáticamente el servidor en la red local cada vez que se inicie."
        } else {
            binding.textViewConfigInfo.text = 
                "Ingresa manualmente la IP del servidor. Asegúrate de que tu dispositivo y el servidor estén en la misma red WiFi."
        }
    }
    
    private fun detectServer() {
        if (isDetecting) return
        
        if (!ServerDetector.isConnectedToWiFi(this)) {
            Toast.makeText(this, "Debes estar conectado a WiFi para detectar el servidor", Toast.LENGTH_LONG).show()
            return
        }
        
        isDetecting = true
        binding.buttonDetectServer.isEnabled = false
        binding.buttonDetectServer.text = "Detectando..."
        binding.progressBar.visibility = android.view.View.VISIBLE
        
        val port = binding.editTextServerPort.text.toString()
        
        lifecycleScope.launch {
            try {
                val detectedIP = ServerDetector.detectServerIP(this@ServerConfigActivity, port)
                
                if (detectedIP != null) {
                    binding.editTextServerIP.setText(detectedIP)
                    Toast.makeText(
                        this@ServerConfigActivity,
                        "¡Servidor encontrado en: $detectedIP!",
                        Toast.LENGTH_LONG
                    ).show()
                } else {
                    Toast.makeText(
                        this@ServerConfigActivity,
                        "No se pudo detectar el servidor. Verifica que esté ejecutándose y que ambos dispositivos estén en la misma red WiFi.",
                        Toast.LENGTH_LONG
                    ).show()
                }
            } catch (e: Exception) {
                Toast.makeText(
                    this@ServerConfigActivity,
                    "Error al detectar servidor: ${e.message}",
                    Toast.LENGTH_LONG
                ).show()
            } finally {
                isDetecting = false
                binding.buttonDetectServer.isEnabled = true
                binding.buttonDetectServer.text = "Detectar Servidor"
                binding.progressBar.visibility = android.view.View.GONE
            }
        }
    }
    
    private fun saveConfiguration() {
        val autoDetect = binding.switchAutoDetect.isChecked
        val ip = binding.editTextServerIP.text.toString().trim()
        val port = binding.editTextServerPort.text.toString().trim()
        val remoteUrl = binding.editTextRemoteUrl.text.toString().trim()
        val useRemoteOnMobile = binding.switchUseRemoteOnMobile.isChecked
        
        if (!autoDetect && ip.isEmpty()) {
            Toast.makeText(this, "Debes ingresar una IP o activar la detección automática", Toast.LENGTH_SHORT).show()
            return
        }
        
        if (port.isEmpty()) {
            Toast.makeText(this, "Debes ingresar un puerto", Toast.LENGTH_SHORT).show()
            return
        }
        
        // Validar URL remota si está habilitada
        if (useRemoteOnMobile && remoteUrl.isNotEmpty()) {
            if (!remoteUrl.startsWith("http://") && !remoteUrl.startsWith("https://")) {
                Toast.makeText(this, "La URL remota debe comenzar con http:// o https://", Toast.LENGTH_LONG).show()
                return
            }
        }
        
        // Guardar configuración
        NetworkPreferences.setAutoDetect(this, autoDetect)
        NetworkPreferences.saveServerIP(this, ip)
        NetworkPreferences.saveServerPort(this, port)
        NetworkPreferences.saveRemoteUrl(this, remoteUrl)
        NetworkPreferences.setUseRemoteOnMobile(this, useRemoteOnMobile)
        
        // Invalidar caché de NetworkConfig para que recargue la URL
        com.example.fiberdesk_app.network.NetworkConfig.invalidateCache()
        
        Toast.makeText(this, "✅ Configuración guardada correctamente", Toast.LENGTH_SHORT).show()
        finish()
    }
    
    private fun testConnection() {
        val ip = binding.editTextServerIP.text.toString().trim()
        val port = binding.editTextServerPort.text.toString().trim()
        
        if (ip.isEmpty()) {
            Toast.makeText(this, "Debes ingresar una IP", Toast.LENGTH_SHORT).show()
            return
        }
        
        binding.buttonTestConnection.isEnabled = false
        binding.buttonTestConnection.text = "Probando..."
        
        lifecycleScope.launch {
            try {
                withContext(kotlinx.coroutines.Dispatchers.IO) {
                    val url = java.net.URL("http://$ip:$port/api/health")
                    val connection = url.openConnection() as java.net.HttpURLConnection
                    connection.connectTimeout = 3000
                    connection.readTimeout = 3000
                    connection.requestMethod = "GET"
                    
                    val responseCode = connection.responseCode
                    val responseMessage = try {
                        connection.inputStream.bufferedReader().readText()
                    } catch (e: Exception) {
                        ""
                    }
                    connection.disconnect()
                    
                    withContext(kotlinx.coroutines.Dispatchers.Main) {
                        if (responseCode == 200) {
                            Toast.makeText(
                                this@ServerConfigActivity,
                                "✅ ¡Conexión exitosa! El servidor FiberDesk está disponible.",
                                Toast.LENGTH_LONG
                            ).show()
                        } else {
                            Toast.makeText(
                                this@ServerConfigActivity,
                                "⚠️ El servidor respondió con código: $responseCode",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }
                }
            } catch (e: java.net.ConnectException) {
                Toast.makeText(
                    this@ServerConfigActivity,
                    "❌ No se pudo conectar. Verifica que el servidor esté ejecutándose en $ip:$port",
                    Toast.LENGTH_LONG
                ).show()
            } catch (e: java.net.SocketTimeoutException) {
                Toast.makeText(
                    this@ServerConfigActivity,
                    "⏱️ Tiempo de espera agotado. El servidor no responde.",
                    Toast.LENGTH_LONG
                ).show()
            } catch (e: Exception) {
                Toast.makeText(
                    this@ServerConfigActivity,
                    "❌ Error: ${e.message ?: "Desconocido"}",
                    Toast.LENGTH_LONG
                ).show()
            } finally {
                binding.buttonTestConnection.isEnabled = true
                binding.buttonTestConnection.text = "Probar Conexión"
            }
        }
    }
    
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
