const mongoose = require('mongoose');

const pagosSchema = new mongoose.Schema({
    id: {
        type: String,
        unique: true,
        required: true
    },
    usuarioId: {
        type: mongoose.Schema.Types.ObjectId,
        ref: 'Usuario',
        required: true
    },
    monto: {
        type: Number,
        required: true,
        min: 0
    },
    abono: {
        type: Number,
        default: 0,
        min: 0
    },
    metodoPago: {
        type: String,
        enum: ['efectivo', 'transferencia', 'tarjeta', 'cheque'],
        required: true
    },
    fechaPago: {
        type: Date,
        default: Date.now,
        required: true
    },
    descripcion: {
        type: String,
        default: ''
    },
    estado: {
        type: String,
        enum: ['pendiente', 'pagado', 'parcial'],
        default: 'pendiente'
    },
    createdAt: {
        type: Date,
        default: Date.now
    },
    updatedAt: {
        type: Date,
        default: Date.now
    }
});

module.exports = mongoose.model('Pago', pagosSchema);