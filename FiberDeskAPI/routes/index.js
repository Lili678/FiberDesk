const express = require("express");
const router = express.Router();
const authRoutes = require("./auth");

// Rutas de autenticación
router.use("/auth", authRoutes);

module.exports = router;
