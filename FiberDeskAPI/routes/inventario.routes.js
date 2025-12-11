const express = require('express');
const {
  getMateriales,
  addMaterial,
  updateMaterial,
  deleteMaterial
} = require('../controllers/inventario.controller');

const router = express.Router();

router.get('/materiales', getMateriales);
router.post('/materiales', addMaterial);
router.put('/materiales/:id', updateMaterial);
router.delete('/materiales/:id', deleteMaterial);

module.exports = router;
