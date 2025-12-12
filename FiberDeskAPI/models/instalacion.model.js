const mongoose = require('mongoose');

const InstalacionSchema = new mongoose.Schema({
    cliente: {
        type: String,
        required: true
    },
    direccion: {
        type: String,
        required: true
    },
    estado: {
        type: String,
        enum: ['pendiente', 'en_progreso', 'completada'],
        default: 'pendiente'
    },
    materialesUsados: [
        {
            materialId: {
                type: mongoose.Schema.Types.ObjectId,
                ref: 'Material',
                required: true
            },
            cantidad: {
                type: Number,
                required: true
            }
        }
    ],
    fechaCreacion: {
        type: Date,
        default: Date.now
    },
    fechaCompletado: {
        type: Date,
        default: null
    }
});

module.exports = mongoose.model('Instalacion', InstalacionSchema);
