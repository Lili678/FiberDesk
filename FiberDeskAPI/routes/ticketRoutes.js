const express = require("express");
const router = express.Router();
const Ticket = require("../models/Ticket");

// GET - Listar todos
router.get("/", async (req, res) => {
    const tickets = await Ticket.find({ archivado: false });
    res.json(tickets);
});

// GET - Buscar tickets
router.get("/search", async (req, res) => {
    const query = req.query.query || "";
    const tickets = await Ticket.find({
        $or: [
            { folio: { $regex: query, $options: "i" } },
            { cliente: { $regex: query, $options: "i" } },
            { asunto: { $regex: query, $options: "i" } }
        ],
        archivado: false
    });
    res.json(tickets);
});

// POST - Crear ticket
router.post("/", async (req, res) => {
    try {
        const newTicket = await Ticket.create(req.body);
        res.json({ message: "Ticket creado", ticket: newTicket });
    } catch (error) {
        console.log(error);
        res.status(400).json({ error: "Error al crear ticket" });
    }
});

// PUT - Archivar ticket
router.put("/archive/:folio", async (req, res) => {
    try {
        const { folio } = req.params;

        await Ticket.findOneAndUpdate(
            { folio },
            { archivado: true }
        );

        res.json({ message: "Ticket archivado" });
    } catch (error) {
        res.status(400).json({ error: "Error al archivar ticket" });
    }
});

// PUT - Desarchivar ticket
router.put("/unarchive/:folio", async (req, res) => {
    try {
        const { folio } = req.params;

        await Ticket.findOneAndUpdate(
            { folio },
            { archivado: false }
        );

        res.json({ message: "Ticket desarchivado" });
    } catch (error) {
        res.status(400).json({ error: "Error al desarchivar ticket" });
    }
});


// GET - Tickets archivados
router.get("/archived", async (req, res) => {
    try {
        const tickets = await Ticket.find({ archivado: true });
        res.json(tickets);
    } catch (error) {
        res.status(500).json({ error: "Error al obtener archivados" });
    }
});


module.exports = router;
