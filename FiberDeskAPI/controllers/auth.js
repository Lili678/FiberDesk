const Usuario = require('../models/usuario_schema');
const jwt = require('jsonwebtoken');

// Generar token JWT
const generarToken = (id) => {
  return jwt.sign({ id }, process.env.JWT_SECRET, {
    expiresIn: '30d'
  });
};

// @desc    Registrar nuevo usuario
// @route   POST /api/auth/registro
// @access  Public
exports.registrarUsuario = async (req, res) => {
  try {
    console.log('Datos recibidos en registro:', req.body);
    const { correo, contraseña, nombre } = req.body;

    // Validar que vengan los datos requeridos
    if (!correo || !contraseña) {
      console.log('Validación fallida - correo:', correo, 'contraseña:', contraseña);
      return res.status(400).json({
        success: false,
        message: 'Por favor proporcione correo y contraseña'
      });
    }

    // Verificar si el usuario ya existe
    const usuarioExiste = await Usuario.findOne({ correo });
    if (usuarioExiste) {
      return res.status(400).json({
        success: false,
        message: 'El correo ya está registrado'
      });
    }

    // Crear usuario
    const usuario = await Usuario.create({
      correo,
      contraseña,
      nombre
    });

    // Generar token
    const token = generarToken(usuario._id);

    res.status(201).json({
      success: true,
      message: 'Usuario registrado exitosamente',
      data: {
        _id: usuario._id,
        correo: usuario.correo,
        nombre: usuario.nombre,
        token
      }
    });
  } catch (error) {
    console.error('Error en registro:', error);
    res.status(500).json({
      success: false,
      message: 'Error al registrar usuario',
      error: error.message
    });
  }
};

// @desc    Login de usuario
// @route   POST /api/auth/login
// @access  Public
exports.loginUsuario = async (req, res) => {
  try {
    const { correo, contraseña } = req.body;

    // Validar que vengan los datos
    if (!correo || !contraseña) {
      return res.status(400).json({
        success: false,
        message: 'Por favor proporcione correo y contraseña'
      });
    }

    // Buscar usuario
    const usuario = await Usuario.findOne({ correo });
    if (!usuario) {
      return res.status(401).json({
        success: false,
        message: 'Credenciales incorrectas'
      });
    }

    // Verificar contraseña
    const contraseñaCorrecta = await usuario.compararContraseña(contraseña);
    if (!contraseñaCorrecta) {
      return res.status(401).json({
        success: false,
        message: 'Credenciales incorrectas'
      });
    }

    // Verificar que el usuario esté activo
    if (!usuario.activo) {
      return res.status(401).json({
        success: false,
        message: 'Usuario desactivado'
      });
    }

    // Generar token
    const token = generarToken(usuario._id);

    res.status(200).json({
      success: true,
      message: 'Login exitoso',
      data: {
        _id: usuario._id,
        correo: usuario.correo,
        nombre: usuario.nombre,
        token
      }
    });
  } catch (error) {
    console.error('Error en login:', error);
    res.status(500).json({
      success: false,
      message: 'Error al iniciar sesión',
      error: error.message
    });
  }
};

// @desc    Obtener usuario actual
// @route   GET /api/auth/me
// @access  Private
exports.obtenerUsuario = async (req, res) => {
  try {
    const usuario = await Usuario.findById(req.usuario._id).select('-contraseña');
    
    res.status(200).json({
      success: true,
      data: usuario
    });
  } catch (error) {
    console.error('Error al obtener usuario:', error);
    res.status(500).json({
      success: false,
      message: 'Error al obtener información del usuario',
      error: error.message
    });
  }
};

// @desc    Actualizar usuario
// @route   PUT /api/auth/usuario/:id
// @access  Private
exports.actualizarUsuario = async (req, res) => {
  try {
    const { nombre, email } = req.body;
    const usuario = await Usuario.findById(req.params.id);

    if (!usuario) {
      return res.status(404).json({
        success: false,
        message: 'Usuario no encontrado'
      });
    }

    // Actualizar solo los campos permitidos
    if (nombre) usuario.nombre = nombre;
    if (email) usuario.correo = email;

    await usuario.save();

    res.status(200).json({
      success: true,
      message: 'Usuario actualizado correctamente',
      data: {
        _id: usuario._id,
        nombre: usuario.nombre,
        correo: usuario.correo
      }
    });
  } catch (error) {
    console.error('Error al actualizar usuario:', error);
    res.status(500).json({
      success: false,
      message: 'Error al actualizar usuario',
      error: error.message
    });
  }
};

// @desc    Cambiar contraseña
// @route   PUT /api/auth/usuario/:id/password
// @access  Private
exports.cambiarContrasena = async (req, res) => {
  try {
    const { passwordActual, passwordNueva } = req.body;
    const usuario = await Usuario.findById(req.params.id);

    if (!usuario) {
      return res.status(404).json({
        success: false,
        message: 'Usuario no encontrado'
      });
    }

    // Verificar contraseña actual
    const contraseñaCorrecta = await usuario.compararContraseña(passwordActual);
    if (!contraseñaCorrecta) {
      return res.status(401).json({
        success: false,
        message: 'Contraseña actual incorrecta'
      });
    }

    // Actualizar contraseña
    usuario.contraseña = passwordNueva;
    await usuario.save();

    res.status(200).json({
      success: true,
      message: 'Contraseña actualizada correctamente'
    });
  } catch (error) {
    console.error('Error al cambiar contraseña:', error);
    res.status(500).json({
      success: false,
      message: 'Error al cambiar contraseña',
      error: error.message
    });
  }
};

// @desc    Solicitar recuperación de contraseña
// @route   POST /api/auth/recuperar-password
// @access  Public
exports.solicitarRecuperacion = async (req, res) => {
  try {
    const { correo } = req.body;

    // Validar correo
    if (!correo) {
      return res.status(400).json({
        success: false,
        message: 'El correo es requerido'
      });
    }

    // Buscar usuario
    const usuario = await Usuario.findOne({ correo });
    if (!usuario) {
      return res.status(404).json({
        success: false,
        message: 'No existe un usuario con ese correo'
      });
    }

    // Generar código de 6 dígitos
    const codigo = Math.floor(100000 + Math.random() * 900000).toString();
    
    // Guardar código y fecha de expiración (15 minutos)
    usuario.codigoRecuperacion = codigo;
    usuario.codigoRecuperacionExpira = Date.now() + 15 * 60 * 1000; // 15 minutos
    await usuario.save();

    // En producción, aquí enviarías un email
    // Por ahora, devolvemos el código en la respuesta (solo para desarrollo)
    console.log(`Código de recuperación para ${correo}: ${codigo}`);

    res.status(200).json({
      success: true,
      mensaje: 'Código de recuperación generado',
      codigo: codigo // Solo para desarrollo, remover en producción
    });
  } catch (error) {
    console.error('Error al solicitar recuperación:', error);
    res.status(500).json({
      success: false,
      message: 'Error al solicitar recuperación',
      error: error.message
    });
  }
};

// @desc    Restablecer contraseña con código
// @route   POST /api/auth/restablecer-password
// @access  Public
exports.restablecerContrasena = async (req, res) => {
  try {
    const { correo, codigo, nuevaContrasena } = req.body;

    // Validar campos
    if (!correo || !codigo || !nuevaContrasena) {
      return res.status(400).json({
        success: false,
        message: 'Todos los campos son requeridos'
      });
    }

    // Buscar usuario
    const usuario = await Usuario.findOne({ 
      correo,
      codigoRecuperacion: codigo,
      codigoRecuperacionExpira: { $gt: Date.now() }
    });

    if (!usuario) {
      return res.status(400).json({
        success: false,
        message: 'Código inválido o expirado'
      });
    }

    // Actualizar contraseña
    usuario.contraseña = nuevaContrasena;
    usuario.codigoRecuperacion = undefined;
    usuario.codigoRecuperacionExpira = undefined;
    await usuario.save();

    res.status(200).json({
      success: true,
      message: 'Contraseña restablecida exitosamente'
    });
  } catch (error) {
    console.error('Error al restablecer contraseña:', error);
    res.status(500).json({
      success: false,
      message: 'Error al restablecer contraseña',
      error: error.message
    });
  }
};
