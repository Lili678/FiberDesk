
const express = require("express");
const router = express.Router();

router.use("/tickets", require("./ticketRoutes"));

module.exports = router;
