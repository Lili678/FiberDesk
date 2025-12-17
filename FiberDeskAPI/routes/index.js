const express = require('express');
const router = express.Router();

// Rutas de autenticación
router.use('/auth', require('../middleware/auth'));

// Rutas de clientes
router.use('/clientes', require('./clientes'));

// Rutas de pagos
router.use('/pagos', require('./rutas'));

// Rutas de tickets
router.use('/tickets', require('./ticketRoutes'));

module.exports = router;
