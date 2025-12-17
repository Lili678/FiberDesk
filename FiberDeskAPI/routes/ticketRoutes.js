const express = require("express");
const router = express.Router();
const Ticket = require("../models/Ticket");

// GET - Listar todos (incluyendo archivados)
router.get("/", async (req, res) => {
    const tickets = await Ticket.find({});
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

// PUT - Actualizar estado del ticket
router.put("/:folio/estado", async (req, res) => {
    try {
        const { folio } = req.params;
        const { estado } = req.body;

        console.log("=== ACTUALIZAR ESTADO ===");
        console.log("Folio recibido:", folio);
        console.log("Estado recibido:", estado);
        console.log("Body completo:", req.body);

        // Validar que el estado sea válido
        const estadosValidos = ["Pendiente", "En Espera", "En Progreso", "Realizado"];
        if (!estadosValidos.includes(estado)) {
            console.log("Estado no válido:", estado);
            return res.status(400).json({ error: "Estado no válido", estadoRecibido: estado });
        }

        const ticket = await Ticket.findOne({ folio });
        console.log("Ticket encontrado:", ticket ? "Sí" : "No");
        
        if (!ticket) {
            console.log("Ticket no encontrado con folio:", folio);
            return res.status(404).json({ error: "Ticket no encontrado", folio });
        }

        console.log("Estado actual del ticket:", ticket.estado);

        // Si el ticket ya está en estado "Realizado", no permitir cambios
        if (ticket.estado === "Realizado") {
            console.log("Ticket ya finalizado:", folio);
            return res.status(400).json({ error: "No se puede cambiar el estado de un ticket finalizado" });
        }

        // Actualizar el estado
        const updated = await Ticket.findOneAndUpdate(
            { folio },
            { estado },
            { new: true }
        );

        console.log("Estado actualizado exitosamente a:", updated.estado);
        console.log("=== FIN ACTUALIZAR ESTADO ===");
        res.json({ success: true, message: "Estado actualizado", estado: updated.estado });
    } catch (error) {
        console.error("ERROR al actualizar estado:", error);
        res.status(500).json({ error: "Error al actualizar estado", detalle: error.message });
    }
});

module.exports = router;
