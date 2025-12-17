const express = require('express');
const {
  getMateriales,
  addMaterial,
  updateMaterial,
  deleteMaterial
} = require('../controllers/inventario.controller');

const {
  getInstalaciones,
  createInstalacion,
  updateEstadoInstalacion,
  addMaterialToInstalacion,
  deleteInstalacion
} = require('../controllers/instalacion.controller');

const router = express.Router();

// Rutas de materiales
router.get('/materiales', getMateriales);
router.post('/materiales', addMaterial);
router.put('/materiales/:id', updateMaterial);
router.delete('/materiales/:id', deleteMaterial);

// Rutas de instalaciones
router.get('/instalaciones', getInstalaciones);
router.post('/instalaciones', createInstalacion);
router.put('/instalaciones/:id/estado', updateEstadoInstalacion);
router.post('/instalaciones/:id/materiales', addMaterialToInstalacion);
router.delete('/instalaciones/:id', deleteInstalacion);

module.exports = router;
