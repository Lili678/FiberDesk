package com.example.fiberdesk_app.ui.login

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.activity.addCallback
import androidx.appcompat.app.AppCompatActivity
import com.example.fiberdesk_app.R
import com.google.android.material.textfield.TextInputEditText

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

        // Manejar el botón de atrás - regresar a LoginActivity
        onBackPressedDispatcher.addCallback(this) {
            val intent = Intent(this@RecoverPasswordActivity, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun initViews() {
        txtCorreo = findViewById(R.id.txtCorreo)
        btnRecuperar = findViewById(R.id.btnRecuperar)
        txtVolver = findViewById(R.id.txtVolver)
        progressBar = findViewById(R.id.progressBar)
    }

    private fun setupListeners() {
        // Botón recuperar contraseña
        btnRecuperar.setOnClickListener {
            val correo = txtCorreo.text.toString().trim()

            if (validarCorreo(correo)) {
                // Mostrar progreso
                progressBar.visibility = View.VISIBLE
                btnRecuperar.isEnabled = false

                // Simular envío de correo (en producción, llamar a la API)
                android.os.Handler(android.os.Looper.getMainLooper()).postDelayed({
                    progressBar.visibility = View.GONE
                    btnRecuperar.isEnabled = true
                    
                    Toast.makeText(
                        this,
                        "Se ha enviado un correo de recuperación a $correo",
                        Toast.LENGTH_LONG
                    ).show()
                    
                    // Volver al login después de 2 segundos
                    android.os.Handler(android.os.Looper.getMainLooper()).postDelayed({
                        volverAlLogin()
                    }, 2000)
                }, 1500)
            }
        }

        // Acción de teclado en correo
        txtCorreo.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                btnRecuperar.performClick()
                true
            } else {
                false
            }
        }

        // Volver al login
        txtVolver.setOnClickListener {
            volverAlLogin()
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

    private fun volverAlLogin() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }
}
