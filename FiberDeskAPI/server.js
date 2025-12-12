const express = require('express');
const app = express();
const port = 3000;

// Permite al servidor entender JSON (lo que envía Android)
app.use(express.json());

// Base de datos simulada (Aquí conectarías MySQL o MongoDB real)
const baseDeDatosClientes = [];

// ENDPOINT: Debe coincidir con lo que pusiste en ApiService.kt (@POST("api/clientes"))
app.post('/api/clientes', (req, res) => {
    const nuevoCliente = req.body;

    console.log("--- SOLICITUD RECIBIDA DESDE ANDROID ---");
    console.log("Datos del cliente:", nuevoCliente);

    // Validación simple
    if (!nuevoCliente.nombre || !nuevoCliente.telefono) {
        return res.status(400).send({ error: "Faltan datos obligatorios" });
    }

    // Guardar en la "Base de Datos"
    baseDeDatosClientes.push(nuevoCliente);

    // Responder a Android que todo salió bien (Código 201 = Creado)
    res.status(201).send({ 
        mensaje: "Cliente guardado exitosamente en el servidor",
        totalClientes: baseDeDatosClientes.length
    });
});

// NUEVO: Endpoint para ver la lista de clientes desde el navegador (GET)
app.get('/api/clientes', (req, res) => {
    res.status(200).json(baseDeDatosClientes);
});

// Iniciar el servidor
app.listen(port, () => {
    console.log(`Servidor FiberDesk corriendo en http://localhost:${port}`);
});
