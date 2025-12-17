const mongoose = require('mongoose');

const clientesSchema = new mongoose.Schema({
    Name: {
        FirstName: {
            type: String,
            required: true,
            trim: true
        },
        MiddleName: {
            type: String,
            trim: true,
            default: ''
        }
    },
    LastName: {
        PaternalLastName: {
            type: String,
            required: true,
            trim: true
        },
        MaternalLastName: {
            type: String,
            trim: true,
            default: ''
        }
    },
    PhoneNumber: [{
        type: String,
        required: true
    }],
    Email: {
        type: String,
        required: true,
        lowercase: true,
        trim: true
    },
    Location: {
        Address: {
            Street: { type: String, default: '' },
            ExteriorNumber: { type: String, default: '' },
            InteriorNumber: { type: String, default: '' },
            Neighborhood: { type: String, default: '' },
            City: { type: String, default: '' },
            State: { type: String, default: '' },
            ZipCode: { type: String, default: '' }
        },
        Coordinates: {
            Latitude: {
                type: Number,
                required: true,
                min: -90,
                max: 90
            },
            Longitude: {
                type: Number,
                required: true,
                min: -180,
                max: 180
            }
        }
    },
    Documentos: [{
        Tipo: {
            type: String,
            enum: ['INE', 'Comprobante Domicilio', 'Contrato', 'Otro'],
            default: 'Otro'
        },
        Nombre: String,
        URL: String,
        FechaSubida: {
            type: Date,
            default: Date.now
        }
    }],
    Paquete: {
        NombrePaquete: {
            type: String,
            default: ''
        },
        Velocidad: {
            type: String,
            default: ''
        },
        Precio: {
            type: Number,
            default: 0
        },
        FechaContratacion: {
            type: Date,
            default: Date.now
        }
    },
    Archived: {
        type: Boolean,
        default: false
    },
    CreateDate: {
        type: Date,
        default: Date.now
    },
    UpdateDate: {
        type: Date,
        default: Date.now
    }
}, {
    timestamps: { createdAt: 'CreateDate', updatedAt: 'UpdateDate' }
});

// Índices para búsquedas rápidas
clientesSchema.index({ Email: 1 });
clientesSchema.index({ 'Name.FirstName': 1, 'LastName.PaternalLastName': 1 });
clientesSchema.index({ Archived: 1 });

// Método virtual para obtener nombre completo
clientesSchema.virtual('NombreCompleto').get(function() {
    const middleName = this.Name.MiddleName ? ` ${this.Name.MiddleName}` : '';
    const maternalLastName = this.LastName.MaternalLastName ? ` ${this.LastName.MaternalLastName}` : '';
    return `${this.Name.FirstName}${middleName} ${this.LastName.PaternalLastName}${maternalLastName}`;
});

module.exports = mongoose.model('Cliente', clientesSchema);