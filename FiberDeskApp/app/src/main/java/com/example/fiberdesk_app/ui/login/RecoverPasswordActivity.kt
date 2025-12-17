package com.example.fiberdesk_app.ui.login

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.fiberdesk_app.R
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.launch

class RecoverPasswordActivity : AppCompatActivity() {

    private lateinit var txtCorreo: TextInputEditText
    private lateinit var btnRecuperar: Button
    private lateinit var txtVolver: TextView
    private lateinit var progressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recover_password)

        // Inicializar vistas
        initViews()

        // Configurar listeners
        setupListeners()
    }

    private fun initViews() {
        txtCorreo = findViewById(R.id.txtCorreo)
        btnRecuperar = findViewById(R.id.btnRecuperar)
        txtVolver = findViewById(R.id.txtVolver)
        progressBar = findViewById(R.id.progressBar)
    }

    private fun setupListeners() {
        // Botón enviar código
        btnRecuperar.setOnClickListener {
            val correo = txtCorreo.text.toString().trim()

            if (validarCorreo(correo)) {
                solicitarCodigoRecuperacion(correo)
            }
        }

        // Volver al login
        txtVolver.setOnClickListener {
            finish()
        }

        // Enter en el campo de correo
        txtCorreo.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == android.view.inputmethod.EditorInfo.IME_ACTION_DONE) {
                btnRecuperar.performClick()
                true
            } else {
                false
            }
        }
    }

    private fun validarCorreo(correo: String): Boolean {
        if (correo.isEmpty()) {
            txtCorreo.error = "El correo es requerido"
            return false
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(correo).matches()) {
            txtCorreo.error = "Ingrese un correo válido"
            return false
        }

        return true
    }

    private fun solicitarCodigoRecuperacion(correo: String) {
        progressBar.visibility = View.VISIBLE
        btnRecuperar.isEnabled = false

        lifecycleScope.launch {
            try {
                val response = com.example.fiberdesk_app.network.ApiClient.apiService.solicitarRecuperacion(
                    mapOf("correo" to correo)
                )

                progressBar.visibility = View.GONE
                btnRecuperar.isEnabled = true

                if (response.isSuccessful && response.body() != null) {
                    val codigo = response.body()?.codigo ?: ""
                    
                    // Mostrar el código al usuario (para desarrollo)
                    Toast.makeText(
                        this@RecoverPasswordActivity, 
                        "Código de verificación: $codigo\n(Guárdalo para el siguiente paso)", 
                        Toast.LENGTH_LONG
                    ).show()
                    
                    // Mostrar el diálogo para ingresar código y nueva contraseña
                    mostrarDialogoVerificarCodigo(correo)
                } else {
                    Toast.makeText(
                        this@RecoverPasswordActivity, 
                        "Error: No se encontró un usuario con ese correo", 
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } catch (e: Exception) {
                progressBar.visibility = View.GONE
                btnRecuperar.isEnabled = true
                Toast.makeText(
                    this@RecoverPasswordActivity, 
                    "Error: ${e.message}", 
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun mostrarDialogoVerificarCodigo(correo: String) {
        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_verificar_codigo, null)
        val etCodigo = dialogView.findViewById<TextInputEditText>(R.id.etCodigo)
        val etNuevaContrasena = dialogView.findViewById<TextInputEditText>(R.id.etNuevaContrasena)
        val etConfirmarContrasena = dialogView.findViewById<TextInputEditText>(R.id.etConfirmarContrasena)

        val dialog = AlertDialog.Builder(this)
            .setView(dialogView)
            .setCancelable(true)
            .create()

        dialogView.findViewById<android.widget.Button>(R.id.btnCancelar).setOnClickListener {
            dialog.dismiss()
            finish()
        }

        dialogView.findViewById<android.widget.Button>(R.id.btnRestablecer).setOnClickListener {
            val codigo = etCodigo.text.toString().trim()
            val nuevaContrasena = etNuevaContrasena.text.toString().trim()
            val confirmarContrasena = etConfirmarContrasena.text.toString().trim()

            if (codigo.isEmpty()) {
                etCodigo.error = "El código es requerido"
                return@setOnClickListener
            }

            if (codigo.length != 6) {
                etCodigo.error = "El código debe tener 6 dígitos"
                return@setOnClickListener
            }

            if (nuevaContrasena.isEmpty()) {
                etNuevaContrasena.error = "La contraseña es requerida"
                return@setOnClickListener
            }

            if (nuevaContrasena.length < 6) {
                etNuevaContrasena.error = "La contraseña debe tener al menos 6 caracteres"
                return@setOnClickListener
            }

            if (nuevaContrasena != confirmarContrasena) {
                etConfirmarContrasena.error = "Las contraseñas no coinciden"
                return@setOnClickListener
            }

            // Restablecer contraseña
            restablecerContrasena(correo, codigo, nuevaContrasena, dialog)
        }

        dialog.setOnDismissListener {
            finish()
        }

        dialog.show()
    }

    private fun restablecerContrasena(correo: String, codigo: String, nuevaContrasena: String, dialog: AlertDialog) {
        lifecycleScope.launch {
            try {
                val response = com.example.fiberdesk_app.network.ApiClient.apiService.restablecerContrasena(
                    mapOf(
                        "correo" to correo,
                        "codigo" to codigo,
                        "nuevaContrasena" to nuevaContrasena
                    )
                )

                if (response.isSuccessful && response.body() != null) {
                    dialog.dismiss()
                    Toast.makeText(
                        this@RecoverPasswordActivity, 
                        "Contraseña restablecida exitosamente", 
                        Toast.LENGTH_SHORT
                    ).show()
                    finish()
                } else {
                    Toast.makeText(
                        this@RecoverPasswordActivity, 
                        "Error: ${response.message()}", 
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } catch (e: Exception) {
                Toast.makeText(
                    this@RecoverPasswordActivity, 
                    "Error: ${e.message}", 
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
}
