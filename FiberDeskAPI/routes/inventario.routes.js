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
    usarMaterial,
    usarMateriales,
    removerMaterial,
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
router.put('/instalaciones/:instalacionId/estado', updateEstadoInstalacion);

// Usar materiales en instalación (individual o múltiple)
router.post('/instalaciones/:instalacionId/materiales/usar', usarMaterial);
router.post('/instalaciones/:instalacionId/materiales/batch', usarMateriales);
router.delete('/instalaciones/:instalacionId/materiales/:materialId', removerMaterial);

router.delete('/instalaciones/:instalacionId', deleteInstalacion);

module.exports = router;