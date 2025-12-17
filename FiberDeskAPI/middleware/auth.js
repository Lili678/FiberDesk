const express = require("express");
const router = express.Router();
const { registrarUsuario, loginUsuario, obtenerUsuario, actualizarUsuario, cambiarContrasena, solicitarRecuperacion, restablecerContrasena } = require('../controllers/auth');
const { proteger } = require('../middleware/auth_middleware');

// Rutas públicas
router.post("/registro", registrarUsuario);
router.post("/login", loginUsuario);
router.post("/recuperar-password", solicitarRecuperacion);
router.post("/restablecer-password", restablecerContrasena);

// Rutas protegidas (requieren autenticación)
router.get("/me", proteger, obtenerUsuario);
router.put("/usuario/:id", actualizarUsuario);
router.put("/usuario/:id/password", cambiarContrasena);

module.exports = router;
