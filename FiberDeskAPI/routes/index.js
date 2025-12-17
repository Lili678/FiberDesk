const express = require('express');
const router = express.Router();

<<<<<<< HEAD
router.use('/auth', require('./auth'));
router.use('/clientes', require('./clientes'));
=======
router.use('/auth', require('../middleware/auth'));
router.use('/pagos', require('./rutas'));
router.use('/tickets', require('./ticketRoutes'));

module.exports = router;
