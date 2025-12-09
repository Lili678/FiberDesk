const express = require('express');
const router = express.Router();
const pagosController = require('../controllers/pagos');
const { validarCrearPago, validarActualizarPago } = require('../middleware/pagos_middleware');

// Rutas
router.post('/', validarCrearPago, pagosController.crearPago);
router.get('/', pagosController.obtenerPagos);
router.get('/:id', pagosController.obtenerPagoPorId);
router.get('/usuario/:usuarioId', pagosController.obtenerPagosPorUsuario);
router.put('/:id', validarActualizarPago, pagosController.actualizarPago);
router.delete('/:id', pagosController.eliminarPago);

module.exports = router;