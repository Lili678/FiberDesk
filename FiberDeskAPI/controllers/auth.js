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
