require('dotenv').config();
const express = require('express');
const mongoose = require('mongoose');
const cors = require('cors');

const app = express();
app.use(cors());
app.use(express.json());

// Conexión Mongo
mongoose.connect(process.env.MONGO_URI)
  .then(() => console.log("Mongo conectado correctamente"))
  .catch(err => console.error("Error al conectar Mongo:", err));

// Rutas
app.use('/api', require('./routes'));

// Iniciar servidor
const PORT = process.env.PORT || 3000;
app.listen(PORT, '0.0.0.0', () => {
  console.log(`Servidor backend corriendo en puerto ${PORT}`);
  console.log(`Accesible desde: http://192.168.1.99:${PORT}/api/`);
});
//192.168.1.99 - Wifimex
//10.54.133.141 - Emulador Virtual