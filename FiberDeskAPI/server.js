// Suprimir mensajes de dotenv capturando console.log temporalmente
const originalLog = console.log;
console.log = () => {};
require('dotenv').config();
console.log = originalLog;

const express = require('express');
const mongoose = require('mongoose');
const cors = require('cors');

const inventarioRoutes = require('./routes/inventario.routes');

const app = express();
app.use(cors());
app.use(express.json());

const MONGO_URI = process.env.MONGO_URI || 'mongodb://127.0.0.1:27017/fiberdesk';

mongoose.connect(MONGO_URI)
  .then(() => console.log("Mongo conectado correctamente"))
  .catch(err => {
    console.error("Error al conectar Mongo:", err);
    console.error("Asegúrese de tener MongoDB en ejecución y/o definir MONGO_URI en un archivo .env");
    process.exit(1);
  });

mongoose.connection.on('error', err => {
  console.error("Error en tiempo de ejecución de Mongo:", err);
});

// Ruta de health check (sin autenticación)
app.get('/api/health', (req, res) => {
  res.status(200).json({ 
    status: 'ok', 
    message: 'FiberDesk API funcionando correctamente',
    timestamp: new Date().toISOString()
  });
});

// Rutas (combinando ambas versiones)
app.use('/api', require('./routes'));
app.use('/api/inventario', inventarioRoutes);

const PORT = process.env.PORT || 3000;
const HOST = '0.0.0.0'; // Escuchar en todas las interfaces de red
app.listen(PORT, HOST, () => {
  console.log(`Servidor backend corriendo en ${HOST}:${PORT}`);
});
