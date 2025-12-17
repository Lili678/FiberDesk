const Instalacion = require('../models/instalacion.model');
const Material = require('../models/material.model');

// Obtener todas las instalaciones
exports.getInstalaciones = async (req, res) => {
    try {
        const instalaciones = await Instalacion.find()
            .populate('materialesUsados.material')
            .sort({ fechaCreacion: -1 });
        res.json(instalaciones);
    } catch (error) {
        console.error('Error al obtener instalaciones:', error);
        res.status(500).json({ error: 'Error al obtener instalaciones' });
    }
};

// Crear una nueva instalación
exports.createInstalacion = async (req, res) => {
    try {
        const { cliente, direccion, estado } = req.body;
        
        const nuevaInstalacion = new Instalacion({
            cliente,
            direccion,
            estado: estado || 'pendiente',
            materialesUsados: [],
            fechaCreacion: new Date(),
            fechaActualizacion: new Date()
        });

        await nuevaInstalacion.save();
        console.log('Instalación creada exitosamente:', nuevaInstalacion._id);
        res.status(201).json(nuevaInstalacion);
    } catch (error) {
        console.error('Error al crear instalación:', error);
        res.status(500).json({ error: 'Error al crear instalación' });
    }
};

// Actualizar estado de instalación
exports.updateEstadoInstalacion = async (req, res) => {
    try {
        const { id } = req.params;
        const { estado } = req.body;

        const instalacion = await Instalacion.findByIdAndUpdate(
            id,
            { estado, fechaActualizacion: new Date() },
            { new: true }
        ).populate('materialesUsados.material');

        if (!instalacion) {
            return res.status(404).json({ error: 'Instalación no encontrada' });
        }

        res.json(instalacion);
    } catch (error) {
        console.error('Error al actualizar estado:', error);
        res.status(500).json({ error: 'Error al actualizar estado' });
    }
};

// Agregar material a instalación
exports.addMaterialToInstalacion = async (req, res) => {
    try {
        const { id } = req.params;
        const { materialId, cantidad } = req.body;

        const material = await Material.findById(materialId);
        if (!material) {
            return res.status(404).json({ error: 'Material no encontrado' });
        }

        if (material.cantidad < cantidad) {
            return res.status(400).json({ error: 'Stock insuficiente' });
        }

        // Actualizar stock del material
        material.cantidad -= cantidad;
        await material.save();

        // Agregar material a la instalación
        const instalacion = await Instalacion.findById(id);
        if (!instalacion) {
            return res.status(404).json({ error: 'Instalación no encontrada' });
        }

        instalacion.materialesUsados.push({ material: materialId, cantidad });
        instalacion.fechaActualizacion = new Date();
        await instalacion.save();

        const instalacionActualizada = await Instalacion.findById(id)
            .populate('materialesUsados.material');

        res.json(instalacionActualizada);
    } catch (error) {
        console.error('Error al agregar material:', error);
        res.status(500).json({ error: 'Error al agregar material' });
    }
};

// Eliminar instalación
exports.deleteInstalacion = async (req, res) => {
    try {
        const { id } = req.params;
        
        const instalacion = await Instalacion.findByIdAndDelete(id);
        if (!instalacion) {
            return res.status(404).json({ error: 'Instalación no encontrada' });
        }

        res.json({ message: 'Instalación eliminada exitosamente' });
    } catch (error) {
        console.error('Error al eliminar instalación:', error);
        res.status(500).json({ error: 'Error al eliminar instalación' });
    }
};
