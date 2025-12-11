const express = require('express');
const router = express.Router();

router.use('/auth', require('./auth'));
router.use("/tickets", require("./ticketRoutes"));


module.exports = router;