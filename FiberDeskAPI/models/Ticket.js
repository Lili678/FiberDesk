const mongoose = require("mongoose");

const TicketSchema = new mongoose.Schema({
    folio: { type: String, required: true, unique: true },
    cliente: String,
    prioridad: String,
    asunto: String,
    tecnico: String,
    creadoPor: String,
    estado: { type: String, default: "Pendiente" },
    fecha: String,
    descripcion: String,
    archivado: { type: Boolean, default: false } 
});

module.exports = mongoose.model("Ticket", TicketSchema);
 