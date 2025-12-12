const express = require("express");
const router = express.Router();
const { registrarUsuario, loginUsuario, obtenerUsuario } = require('../controllers/auth');
const { proteger } = require('../middleware/auth_middleware');

// Rutas públicas
router.post("/registro", registrarUsuario);
router.post("/login", loginUsuario);

// Rutas protegidas (requieren autenticación)
router.get("/me", proteger, obtenerUsuario);

module.exports = router;
