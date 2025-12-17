const Material = require('../models/material.model');

exports.getMateriales = async (req, res) => {
    try {
        const materiales = await Material.find();
        res.json(materiales);
    } catch (error) {
        console.error("Error al obtener materiales:", error);
        res.status(500).json({ error: "Error en el servidor" });
    }
};

exports.addMaterial = async (req, res) => {
    try {
        const nuevo = new Material(req.body);
        await nuevo.save();
        res.json(nuevo);
    } catch (error) {
        res.status(500).json({ error: error.message });
    }
};

exports.updateMaterial = async (req, res) => {
    try {
        const actualizado = await Material.findByIdAndUpdate(
            req.params.id,
            req.body,
            { new: true }
        );
        res.json(actualizado);
    } catch (error) {
        res.status(500).json({ error: error.message });
    }
};

exports.deleteMaterial = async (req, res) => {
    try {
        await Material.findByIdAndDelete(req.params.id);
        res.json({ message: "Material eliminado" });
    } catch (error) {
        res.status(500).json({ error: error.message });
    }
};
