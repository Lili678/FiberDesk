require('dotenv').config();
const express = require('express');
const mongoose = require('mongoose');
const cors = require('cors');

const inventarioRoutes = require('./routes/inventario.routes');
const authRoutes = require('./middleware/auth');

const app = express();
app.use(cors());
app.use(express.json());

mongoose.connect(process.env.MONGO_URI)
  .then(() => console.log("Mongo conectado correctamente"))
  .catch(err => console.error("Error al conectar Mongo:", err));

app.use('/api/auth', authRoutes);
app.use('/api/inventario', inventarioRoutes);

app.listen(3000, () => {
  console.log("Servidor backend corriendo en puerto 3000");
});
