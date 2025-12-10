package com.example.fiberdesk_app.ui.login

import android.content.Intent
import android.os.Bundle
import android.graphics.Rect
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.core.widget.NestedScrollView
import androidx.activity.addCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.fiberdesk_app.R
import com.example.fiberdesk_app.MainActivity
import com.example.fiberdesk_app.viewmodel.RegisterViewModel
import com.google.android.material.textfield.TextInputEditText

class RegisterActivity : AppCompatActivity() {

    private lateinit var viewModel: RegisterViewModel
    private lateinit var txtNombre: TextInputEditText
    private lateinit var txtCorreo: TextInputEditText
    private lateinit var txtContrasena: TextInputEditText
    private lateinit var txtConfirmarContrasena: TextInputEditText
    private lateinit var btnRegistrar: Button
    private lateinit var txtIrLogin: TextView
    private lateinit var progressBar: ProgressBar
    private lateinit var scrollView: NestedScrollView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        // Inicializar ViewModel
        viewModel = ViewModelProvider(this)[RegisterViewModel::class.java]

        // Inicializar vistas
        initViews()

        // Configurar observadores
        setupObservers()

        // Configurar listeners
        setupListeners()

        // Manejar el botón de atrás - regresar a LoginActivity
        onBackPressedDispatcher.addCallback(this) {
            val intent = Intent(this@RegisterActivity, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun initViews() {
        scrollView = findViewById(R.id.registerScroll)
        txtNombre = findViewById(R.id.txtNombre)
        txtCorreo = findViewById(R.id.txtCorreo)
        txtContrasena = findViewById(R.id.txtContrasena)
        txtConfirmarContrasena = findViewById(R.id.txtConfirmarContrasena)
        btnRegistrar = findViewById(R.id.btnRegistrar)
        txtIrLogin = findViewById(R.id.txtIrLogin)
        progressBar = findViewById(R.id.progressBar)
    }

    private fun setupObservers() {
        // Observar estado de carga
        viewModel.loading.observe(this) { isLoading ->
            progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
            btnRegistrar.isEnabled = !isLoading
            txtNombre.isEnabled = !isLoading
            txtCorreo.isEnabled = !isLoading
            txtContrasena.isEnabled = !isLoading
            txtConfirmarContrasena.isEnabled = !isLoading
        }

        // Observar errores
        viewModel.error.observe(this) { errorMessage ->
            errorMessage?.let {
                Toast.makeText(this, it, Toast.LENGTH_LONG).show()
            }
        }

        // Observar usuario registrado
        viewModel.usuario.observe(this) { usuario ->
            usuario?.let {
                Toast.makeText(this, "¡Cuenta creada exitosamente!", Toast.LENGTH_SHORT).show()
                guardarDatosUsuario(it.token, it.nombre ?: it.correo)
                navegarAHome()
            }
        }
    }

    private fun setupListeners() {
        // Cuando presiona Enter en nombre, va al campo de correo
        txtNombre.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == android.view.inputmethod.EditorInfo.IME_ACTION_NEXT) {
                txtCorreo.requestFocus()
                true
            } else {
                false
            }
        }

        // Cuando presiona Enter en correo, va al campo de contraseña
        txtCorreo.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == android.view.inputmethod.EditorInfo.IME_ACTION_NEXT) {
                txtContrasena.requestFocus()
                true
            } else {
                false
            }
        }

        // Cuando presiona Enter en contraseña, va a confirmar contraseña
        txtContrasena.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == android.view.inputmethod.EditorInfo.IME_ACTION_NEXT) {
                txtConfirmarContrasena.requestFocus()
                true
            } else {
                false
            }
        }

        // Cuando presiona Enter en confirmar contraseña, registra
        txtConfirmarContrasena.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == android.view.inputmethod.EditorInfo.IME_ACTION_DONE) {
                val nombre = txtNombre.text.toString().trim()
                val correo = txtCorreo.text.toString().trim()
                val contrasena = txtContrasena.text.toString().trim()
                val confirmarContrasena = txtConfirmarContrasena.text.toString().trim()

                if (validarCampos(nombre, correo, contrasena, confirmarContrasena)) {
                    viewModel.registro(correo, contrasena, nombre)
                }
                true
            } else {
                false
            }
        }

        // Asegurar visibilidad de campos inferiores al enfocar
        val focusScrollListener = View.OnFocusChangeListener { view, hasFocus ->
            if (hasFocus) {
                scrollToView(view)
            }
        }
        txtContrasena.onFocusChangeListener = focusScrollListener
        txtConfirmarContrasena.onFocusChangeListener = focusScrollListener

        // Botón de registro
        btnRegistrar.setOnClickListener {
            val nombre = txtNombre.text.toString().trim()
            val correo = txtCorreo.text.toString().trim()
            val contrasena = txtContrasena.text.toString().trim()
            val confirmarContrasena = txtConfirmarContrasena.text.toString().trim()

            if (validarCampos(nombre, correo, contrasena, confirmarContrasena)) {
                viewModel.registro(correo, contrasena, nombre)
            }
        }

        // Ir a login
        txtIrLogin.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun scrollToView(view: View) {
        scrollView.post {
            val rect = Rect()
            view.getDrawingRect(rect)
            scrollView.offsetDescendantRectToMyCoords(view, rect)
            scrollView.smoothScrollTo(0, rect.top)
        }
    }

    private fun validarCampos(nombre: String, correo: String, contrasena: String, confirmarContrasena: String): Boolean {
        if (nombre.isEmpty()) {
            txtNombre.error = "El nombre es requerido"
            return false
        }

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

        if (confirmarContrasena.isEmpty()) {
            txtConfirmarContrasena.error = "Debe confirmar la contraseña"
            return false
        }

        if (contrasena != confirmarContrasena) {
            txtConfirmarContrasena.error = "Las contraseñas no coinciden"
            return false
        }

        return true
    }

    private fun guardarDatosUsuario(token: String, nombre: String) {
        val sharedPref = getSharedPreferences("AuthPrefs", MODE_PRIVATE)
        with(sharedPref.edit()) {
            putString("token", token)
            putString("userName", nombre)
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
