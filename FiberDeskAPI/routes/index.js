const express = require('express');
const router = express.Router();

router.use('/auth', require('./auth'));
router.use('/clientes', require('./clientes'));

module.exports = router;