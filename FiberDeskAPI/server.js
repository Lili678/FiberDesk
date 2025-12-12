require('dotenv').config();
const express = require('express');
const mongoose = require('mongoose');
const cors = require('cors');

const app = express();
app.use(cors());
app.use(express.json());

// Conexión Mongo Atlas
mongoose.connect(process.env.MONGO_URI)
  .then(() => console.log("Mongo conectado correctamente"))
  .catch(err => console.error("Error al conectar Mongo:", err));

// Rutas principales (/api)
app.use('/api', require('./routes'));    

// Puerto desde .env
const PORT = process.env.PORT || 3000;

app.listen(PORT, () => {
  console.log(`Servidor backend corriendo en puerto ${PORT}`);
});
 