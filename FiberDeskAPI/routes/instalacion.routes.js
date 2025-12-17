const express = require('express');
const {
  getInstalaciones,
  createInstalacion,
  usarMaterial,
  usarMateriales,
  removerMaterial,
  updateEstadoInstalacion,
  deleteInstalacion
} = require('../controllers/instalacion.controller');

const router = express.Router();

router.get('/', getInstalaciones);
router.post('/', createInstalacion);
router.post('/:instalacionId/usar-material', usarMaterial);
router.post('/:instalacionId/usar-materiales', usarMateriales);
router.delete('/:instalacionId/material/:materialId', removerMaterial);
router.put('/:instalacionId/estado', updateEstadoInstalacion);
router.delete('/:instalacionId', deleteInstalacion);

module.exports = router;
