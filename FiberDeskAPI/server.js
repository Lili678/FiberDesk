const express = require('express');
const mongoose = require('mongoose');
require('dotenv').config();
const app = express();
const port = process.env.PORT || 3000;

// Permite al servidor entender JSON (lo que envía Android)
app.use(express.json());

// Conexión a MongoDB
mongoose.connect(process.env.MONGODB_URI)
    .then(() => console.log('Conectado a MongoDB'))
    .catch(err => console.error('Error conectando a MongoDB:', err));

// Definir el Esquema (Estructura de tus datos)
const clienteSchema = new mongoose.Schema({
    nombre: String,
    apellidos: String,
    telefono: String,
    correo: String,
    calle: String,
    numExterior: String,
    numInterior: String,
    colonia: String,
    municipio: String,
    estado: String,
    cp: String,
    latitud: Number,
    longitud: Number,
    fechaRegistro: { type: Date, default: Date.now }
});

// Crear el Modelo
const Cliente = mongoose.model('Cliente', clienteSchema);

// ENDPOINT: Debe coincidir con lo que pusiste en ApiService.kt (@POST("api/clientes"))
app.post('/api/clientes', async (req, res) => {
    const datosCliente = req.body;

    console.log("--- SOLICITUD RECIBIDA DESDE ANDROID ---");
    console.log("Datos del cliente:", datosCliente);

    // Validación simple
    if (!datosCliente.nombre || !datosCliente.telefono) {
        return res.status(400).send({ error: "Faltan datos obligatorios" });
    }

    try {
        // Crear y guardar en MongoDB
        const nuevoCliente = new Cliente(datosCliente);
        await nuevoCliente.save();

        res.status(201).send({ 
            mensaje: "Cliente guardado exitosamente en MongoDB",
            id: nuevoCliente._id
        });
    } catch (error) {
        console.error("Error al guardar:", error);
        res.status(500).send({ error: "Error interno del servidor" });
    }
});

// NUEVO: Endpoint para ver la lista de clientes desde el navegador (GET)
app.get('/api/clientes', async (req, res) => {
    try {
        const clientes = await Cliente.find();
        res.status(200).json(clientes);
    } catch (error) {
        res.status(500).send({ error: "Error al obtener clientes" });
    }
});

// Iniciar el servidor
app.listen(port, () => {
    console.log(`Servidor FiberDesk corriendo en http://localhost:${port}`);
});
