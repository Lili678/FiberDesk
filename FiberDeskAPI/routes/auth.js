const express = require("express");
const router = express.Router();

router.get("/", (req, res) => {
  res.json({ message: "Ruta auth funcionando" });
});

module.exports = router;
