const express = require('express');
const router = express.Router();

router.use('/auth', require('../middleware/auth'));
router.use('/pagos', require('./rutas'));

module.exports = router;