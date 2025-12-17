package com.example.fiberdesk_app.ui.login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.CheckBox
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.activity.addCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.fiberdesk_app.HomeActivity
import com.example.fiberdesk_app.R
import com.example.fiberdesk_app.viewmodel.LoginViewModel
import com.google.android.material.textfield.TextInputEditText

class LoginActivity : AppCompatActivity() {

    private lateinit var viewModel: LoginViewModel
    private lateinit var txtCorreo: TextInputEditText
    private lateinit var txtContrasena: TextInputEditText
    private lateinit var btnLogin: Button
    private lateinit var txtIrRegistro: TextView
    private lateinit var txtOlvidaste: TextView
    private lateinit var checkRecordarme: CheckBox
    private lateinit var progressBar: ProgressBar
    private lateinit var btnServerConfig: android.widget.ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Forzar tema claro
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q) {
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        }
        
        try {
            Log.d("LoginActivity", "onCreate iniciado")
            
            // Verificar si el usuario ya está logueado
            val sharedPref = getSharedPreferences("AuthPrefs", MODE_PRIVATE)
            val token = sharedPref.getString("token", null)
            Log.d("LoginActivity", "Token encontrado: ${token?.take(10)}")
            
            if (!token.isNullOrEmpty()) {
                // Ya tiene sesión, ir directamente a Home
                Log.d("LoginActivity", "Navegando a Home")
                navegarAHome()
                return
            }
            
            Log.d("LoginActivity", "Cargando layout de login")
            setContentView(R.layout.activity_login)

            // Inicializar ViewModel
            viewModel = ViewModelProvider(this)[LoginViewModel::class.java]

            // Inicializar vistas
            initViews()

            // Configurar observadores
            setupObservers()

            // Configurar listeners
            setupListeners()

            // Manejar el botón de atrás - en LoginActivity, cerrar la app
            onBackPressedDispatcher.addCallback(this) {
                finish()
            }
        } catch (e: Exception) {
            Toast.makeText(this, "Error al iniciar: ${e.message}", Toast.LENGTH_LONG).show()
            e.printStackTrace()
        }
    }

    private fun initViews() {
        try {
            txtCorreo = findViewById(R.id.txtCorreo)
            txtContrasena = findViewById(R.id.txtContrasena)
            btnLogin = findViewById(R.id.btnLogin)
            txtIrRegistro = findViewById(R.id.txtIrRegistro)
            txtOlvidaste = findViewById(R.id.txtOlvidaste)
            checkRecordarme = findViewById(R.id.checkRecordarme)
            progressBar = findViewById(R.id.progressBar)
            btnServerConfig = findViewById(R.id.btnServerConfig)
        } catch (e: Exception) {
            Toast.makeText(this, "Error al inicializar vistas: ${e.message}", Toast.LENGTH_LONG).show()
            e.printStackTrace()
        }
    }

    private fun setupObservers() {
        // Observar estado de carga
        viewModel.loading.observe(this) { isLoading ->
            progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
            btnLogin.isEnabled = !isLoading
            txtCorreo.isEnabled = !isLoading
            txtContrasena.isEnabled = !isLoading
        }

        // Observar errores
        viewModel.error.observe(this) { errorMessage ->
            errorMessage?.let {
                Toast.makeText(this, it, Toast.LENGTH_LONG).show()
            }
        }

        // Observar usuario autenticado
        viewModel.usuario.observe(this) { usuario ->
            usuario?.let {
                // Login exitoso - guardar token y navegar a Home
                Toast.makeText(this, "Bienvenido ${it.nombre ?: it.correo}", Toast.LENGTH_SHORT).show()
                
                // Guardar token y datos de usuario en SharedPreferences
                guardarDatosUsuario(it)
                
                // Navegar a la pantalla principal
                navegarAHome()
            }
        }
    }

    private fun setupListeners() {
        // Cuando presiona Enter en email, va al campo de contraseña
        txtCorreo.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == android.view.inputmethod.EditorInfo.IME_ACTION_NEXT) {
                txtContrasena.requestFocus()
                true
            } else {
                false
            }
        }

        // Cuando presiona Enter en contraseña, inicia sesión
        txtContrasena.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == android.view.inputmethod.EditorInfo.IME_ACTION_DONE) {
                val correo = txtCorreo.text.toString().trim()
                val contrasena = txtContrasena.text.toString().trim()

                if (validarCampos(correo, contrasena)) {
                    viewModel.login(correo, contrasena)
                }
                true
            } else {
                false
            }
        }

        // Botón de login
        btnLogin.setOnClickListener {
            val correo = txtCorreo.text.toString().trim()
            val contrasena = txtContrasena.text.toString().trim()

            if (validarCampos(correo, contrasena)) {
                viewModel.login(correo, contrasena)
            }
        }

        // Ir a registro
        txtIrRegistro.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
            finish()
        }

        // Recuperar contraseña
        txtOlvidaste.setOnClickListener {
            val intent = Intent(this, RecoverPasswordActivity::class.java)
            startActivity(intent)
        }
        
        // Configuración del servidor
        btnServerConfig.setOnClickListener {
            val intent = Intent(this, com.example.fiberdesk_app.ui.settings.ServerConfigActivity::class.java)
            startActivity(intent)
        }
    }

    private fun validarCampos(correo: String, contrasena: String): Boolean {
        if (correo.isEmpty()) {
            txtCorreo.error = "El correo es requerido"
            return false
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(correo).matches()) {
            txtCorreo.error = "Ingrese un correo válido"
            return false
        }

        if (contrasena.isEmpty()) {
            txtContrasena.error = "La contraseña es requerida"
            return false
        }

        if (contrasena.length < 6) {
            txtContrasena.error = "La contraseña debe tener al menos 6 caracteres"
            return false
        }

        return true
    }

    private fun guardarDatosUsuario(usuario: com.example.fiberdesk_app.models.UsuarioData) {
        val sharedPref = getSharedPreferences("AuthPrefs", MODE_PRIVATE)
        with(sharedPref.edit()) {
            putString("token", usuario.token)
            putString("userName", usuario.nombre)
            putString("userEmail", usuario.correo)
            putString("userId", usuario._id)
            apply()
        }
    }

    private fun navegarAHome() {
        val intent = Intent(this, com.example.fiberdesk_app.HomeActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }
}
