const express = require('express');
const router = express.Router();

const clientController = require('../controllers/clientController');

// Rutas de Clientes
router.get('/clientes', clientController.getClients);
router.post('/clientes', clientController.createClient);
router.get('/clientes/:id', clientController.getClientById); // Ver detalle
router.put('/clientes/:id', clientController.updateClient);  // Editar
router.delete('/clientes/:id', clientController.deleteClient); // Eliminar

module.exports = router;