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
app.listen(3000, () => {
  console.log("Servidor backend corriendo en puerto 3000");
});
 