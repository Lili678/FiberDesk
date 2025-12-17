const mongoose = require('mongoose');

const MaterialSchema = new mongoose.Schema({
    nombre: {
        type: String,
        required: true,
        trim: true
    },
    cantidad: {
        type: Number,
        required: true,
        default: 0
    },
    descripcion: {
        type: String,
        required: false,
        trim: true
    },
    fechaRegistro: {
        type: Date,
        default: Date.now
    }
});

module.exports = mongoose.model('Material', MaterialSchema);