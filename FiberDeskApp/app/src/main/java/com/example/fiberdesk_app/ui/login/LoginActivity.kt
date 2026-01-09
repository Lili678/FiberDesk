package com.example.fiberdesk_app.ui.login

// ============================================
// IMPORTS - Librerías necesarias para el Login
// ============================================
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

/**
 * ============================================
 * LoginActivity - Pantalla de Inicio de Sesión
 * ============================================
 * Esta clase maneja el login de usuarios en FiberDesk
 * Permite autenticarse con correo y contraseña
 * Si ya hay sesión activa, redirige directamente al Home
 */
class LoginActivity : AppCompatActivity() {

    // ============================================
    // VARIABLES - Declaración de componentes UI
    // ============================================
    private lateinit var viewModel: LoginViewModel      // Maneja la lógica de negocio del login
    private lateinit var txtCorreo: TextInputEditText   // Campo de texto para el correo
    private lateinit var txtContrasena: TextInputEditText // Campo de texto para la contraseña
    private lateinit var btnLogin: Button               // Botón para iniciar sesión
    private lateinit var txtIrRegistro: TextView        // Link para ir a registro
    private lateinit var txtOlvidaste: TextView         // Link "¿Olvidaste tu contraseña?"
    private lateinit var checkRecordarme: CheckBox      // Checkbox "Recordarme"
    private lateinit var progressBar: ProgressBar       // Indicador de carga
    private lateinit var btnServerConfig: android.widget.ImageButton // Botón de configuración del servidor

    /**
     * ============================================
     * onCreate - Método principal al crear la actividad
     * ============================================
     * Se ejecuta cuando se abre la pantalla de login
     * Verifica si hay sesión activa y configura toda la UI
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // ============================================
        // TEMA - Forzar tema claro en la barra de estado
        // ============================================
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q) {
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        }
        
        try {
            Log.d("LoginActivity", "onCreate iniciado")
            
            // ============================================
            // VERIFICACIÓN DE SESIÓN - Auto-login si hay token guardado
            // ============================================
            val sharedPref = getSharedPreferences("AuthPrefs", MODE_PRIVATE)
            val token = sharedPref.getString("token", null)
            Log.d("LoginActivity", "Token encontrado: ${token?.take(10)}")
            
            if (!token.isNullOrEmpty()) {
                // Ya tiene sesión activa, ir directamente al Home
                Log.d("LoginActivity", "Navegando a Home")
                navegarAHome()
                return
            }
            
            // ============================================
            // CONFIGURACIÓN INICIAL - Cargar layout y componentes
            // ============================================
            Log.d("LoginActivity", "Cargando layout de login")
            setContentView(R.layout.activity_login)

            // Inicializar ViewModel (maneja la lógica de login)
            viewModel = ViewModelProvider(this)[LoginViewModel::class.java]

            // Inicializar todos los elementos visuales
            initViews()

            // Configurar observadores para el ViewModel
            setupObservers()

            // Configurar eventos de click y acciones
            setupListeners()

            // ============================================
            // BOTÓN ATRÁS - Cerrar app al presionar atrás
            // ============================================
            onBackPressedDispatcher.addCallback(this) {
                finish()
            }
        } catch (e: Exception) {
            Toast.makeText(this, "Error al iniciar: ${e.message}", Toast.LENGTH_LONG).show()
            e.printStackTrace()
        }
    }

    /**
     * ============================================
     * initViews - Inicializar todos los componentes visuales
     * ============================================
     * Conecta las variables con los elementos del XML
     * Cada findViewById busca un elemento por su ID
     */
    private fun initViews() {
        try {
            txtCorreo = findViewById(R.id.txtCorreo)              // Campo de correo electrónico
            txtContrasena = findViewById(R.id.txtContrasena)      // Campo de contraseña
            btnLogin = findViewById(R.id.btnLogin)                // Botón "Entrar"
            txtIrRegistro = findViewById(R.id.txtIrRegistro)      // Link "Regístrate"
            txtOlvidaste = findViewById(R.id.txtOlvidaste)        // Link "¿Olvidaste tu contraseña?"
            checkRecordarme = findViewById(R.id.checkRecordarme)  // Checkbox "Recordarme"
            progressBar = findViewById(R.id.progressBar)          // Spinner de carga
            btnServerConfig = findViewById(R.id.btnServerConfig)  // Botón de configuración
        } catch (e: Exception) {
            Toast.makeText(this, "Error al inicializar vistas: ${e.message}", Toast.LENGTH_LONG).show()
            e.printStackTrace()
        }
    }

    /**
     * ============================================
     * setupObservers - Configurar observadores del ViewModel
     * ============================================
     * Escucha cambios en el estado del login:
     * - Cuando está cargando
     * - Cuando hay un error
     * - Cuando el login es exitoso
     */
    private fun setupObservers() {
        // ============================================
        // OBSERVADOR DE CARGA - Mostrar/ocultar spinner
        // ============================================
        viewModel.loading.observe(this) { isLoading ->
            progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
            btnLogin.isEnabled = !isLoading           // Deshabilitar botón mientras carga
            txtCorreo.isEnabled = !isLoading          // Deshabilitar campo correo
            txtContrasena.isEnabled = !isLoading      // Deshabilitar campo contraseña
        }

        // ============================================
        // OBSERVADOR DE ERRORES - Mostrar mensajes de error
        // ============================================
        viewModel.error.observe(this) { errorMessage ->
            errorMessage?.let {
                Toast.makeText(this, it, Toast.LENGTH_LONG).show()
            }
        }

        // ============================================
        // OBSERVADOR DE USUARIO - Login exitoso
        // ============================================
        viewModel.usuario.observe(this) { usuario ->
            usuario?.let {
                // Mostrar mensaje de bienvenida
                Toast.makeText(this, "Bienvenido ${it.nombre ?: it.correo}", Toast.LENGTH_SHORT).show()
                
                // Guardar token y datos en almacenamiento local
                guardarDatosUsuario(it)
                
                // Redirigir a la pantalla principal
                navegarAHome()
            }
        }
    }

    /**
     * ============================================
     * setupListeners - Configurar eventos de click
     * ============================================
     * Define qué pasa cuando el usuario interactúa con cada elemento
     */
    private fun setupListeners() {
        // ============================================
        // TECLA ENTER EN CORREO - Ir al campo de contraseña
        // ============================================
        txtCorreo.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == android.view.inputmethod.EditorInfo.IME_ACTION_NEXT) {
                txtContrasena.requestFocus()  // Mover el cursor al campo de contraseña
                true
            } else {
                false
            }
        }

        // ============================================
        // TECLA ENTER EN CONTRASEÑA - Ejecutar login
        // ============================================
        txtContrasena.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == android.view.inputmethod.EditorInfo.IME_ACTION_DONE) {
                val correo = txtCorreo.text.toString().trim()
                val contrasena = txtContrasena.text.toString().trim()

                if (validarCampos(correo, contrasena)) {
                    viewModel.login(correo, contrasena)  // Iniciar sesión
                }
                true
            } else {
                false
            }
        }

        // ============================================
        // BOTÓN LOGIN - Validar y autenticar usuario
        // ============================================
        btnLogin.setOnClickListener {
            val correo = txtCorreo.text.toString().trim()
            val contrasena = txtContrasena.text.toString().trim()

            if (validarCampos(correo, contrasena)) {
                viewModel.login(correo, contrasena)  // Llamar al ViewModel para hacer login
            }
        }

        // ============================================
        // LINK REGISTRO - Ir a pantalla de registro
        // ============================================
        txtIrRegistro.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)  // Abrir RegisterActivity
            finish()               // Cerrar LoginActivity
        }

        // ============================================
        // RECUPERAR CONTRASEÑA - Función deshabilitada
        // ============================================
        txtOlvidaste.setOnClickListener {
            Toast.makeText(this, "Contacta al administrador para recuperar tu contraseña", Toast.LENGTH_SHORT).show()
        }
        
        // ============================================
        // CONFIGURACIÓN SERVIDOR - Abrir configuración de IP
        // ============================================
        btnServerConfig.setOnClickListener {
            val intent = Intent(this, com.example.fiberdesk_app.ui.settings.ServerConfigActivity::class.java)
            startActivity(intent)  // Abrir pantalla de configuración del servidor
        }
    }

    /**
     * ============================================
     * validarCampos - Validar correo y contraseña
     * ============================================
     * Verifica que:
     * - El correo no esté vacío
     * - El correo tenga formato válido
     * - La contraseña no esté vacía
     * - La contraseña tenga al menos 6 caracteres
     */
    private fun validarCampos(correo: String, contrasena: String): Boolean {
        // Validar que el correo no esté vacío
        if (correo.isEmpty()) {
            txtCorreo.error = "El correo es requerido"
            return false
        }

        // Validar formato de correo electrónico
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(correo).matches()) {
            txtCorreo.error = "Ingrese un correo válido"
            return false
        }

        // Validar que la contraseña no esté vacía
        if (contrasena.isEmpty()) {
            txtContrasena.error = "La contraseña es requerida"
            return false
        }

        // Validar longitud mínima de contraseña
        if (contrasena.length < 6) {
            txtContrasena.error = "La contraseña debe tener al menos 6 caracteres"
            return false
        }

        return true  // Todos los campos son válidos
    }

    /**
     * ============================================
     * guardarDatosUsuario - Guardar sesión en SharedPreferences
     * ============================================
     * Almacena localmente:
     * - Token de autenticación
     * - Nombre del usuario
     * - Correo del usuario
     * - ID del usuario
     */
    private fun guardarDatosUsuario(usuario: com.example.fiberdesk_app.models.UsuarioData) {
        val sharedPref = getSharedPreferences("AuthPrefs", MODE_PRIVATE)
        with(sharedPref.edit()) {
            putString("token", usuario.token)        // Token JWT para autenticación
            putString("userName", usuario.nombre)    // Nombre del usuario
            putString("userEmail", usuario.correo)   // Correo del usuario
            putString("userId", usuario._id)         // ID único del usuario
            apply()  // Guardar cambios
        }
    }

    /**
     * ============================================
     * navegarAHome - Ir a la pantalla principal
     * ============================================
     * Abre HomeActivity y limpia el stack de navegación
     * para que no se pueda volver atrás al login
     */
    private fun navegarAHome() {
        val intent = Intent(this, com.example.fiberdesk_app.HomeActivity::class.java)
        // Limpiar stack de navegación
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)  // Abrir Home
        finish()               // Cerrar Login
    }
}
