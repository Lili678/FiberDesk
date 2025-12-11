require('dotenv').config();
const express = require('express');
const mongoose = require('mongoose');
const cors = require('cors');

const routes = require('./routes');

const app = express();
app.use(cors());
app.use(express.json());

const MONGO_URI = process.env.MONGO_URI || 'mongodb://127.0.0.1:27017/fiberdesk';

mongoose.connect(MONGO_URI)
    .catch(err => {
        console.error("Error al conectar Mongo:", err);
        console.error("Asegúrese de tener MongoDB en ejecución y/o definir MONGO_URI en un archivo .env");
        process.exit(1);
    });

app.use('/api', routes);

const PORT = process.env.PORT || 3000;
app.listen(PORT, () => {
    console.log(`Servidor backend corriendo en puerto ${PORT}`);
});