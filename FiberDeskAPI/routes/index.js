const express = require('express');
const router = express.Router();

router.use('/auth', require('./auth'));
router.use('/pagos', require('./rutas'));

module.exports = router;