const express = require('express');
const router = express.Router();
const clientesController = require('../controllers/clientes');
const {
    validarCrearCliente,
    validarActualizarCliente,
    validarAgregarDocumento,
    validarActualizarPaquete
} = require('../middleware/clientes_middleware');

// CREATE - Crear nuevo cliente
router.post('/', validarCrearCliente, clientesController.crearCliente);

// READ - Obtener todos los clientes
router.get('/', clientesController.obtenerClientes);

// READ - Buscar clientes
router.get('/buscar', clientesController.buscarClientes);

// READ - Obtener cliente por ID
router.get('/:id', clientesController.obtenerClientePorId);

// READ - Obtener información completa del cliente (Info, Documentos, Ubicación, Pagos, Tickets)
router.get('/:id/completo', clientesController.obtenerInfoCompleta);

// UPDATE - Actualizar cliente
router.put('/:id', validarActualizarCliente, clientesController.actualizarCliente);

// UPDATE - Agregar documento a cliente
router.post('/:id/documentos', validarAgregarDocumento, clientesController.agregarDocumento);

// UPDATE - Actualizar paquete del cliente
router.put('/:id/paquete', validarActualizarPaquete, clientesController.actualizarPaquete);

// UPDATE - Archivar/Desarchivar cliente
router.patch('/:id/archivar', clientesController.archivarCliente);

// DELETE - Eliminar cliente permanentemente
router.delete('/:id', clientesController.eliminarCliente);

module.exports = router;