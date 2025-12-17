const express = require('express');
const router = express.Router();

<<<<<<< HEAD
router.use('/auth', require('./auth'));
router.use('/clientes', require('./clientes'));
=======
router.use('/auth', require('../middleware/auth'));
router.use('/pagos', require('./rutas'));
>>>>>>> 7b9fc1e44c725c2df985340b3f31291463e6f3df

module.exports = router;