const Material = require('../models/material.model');
const Instalacion = require('../models/instalacion.model');

// Obtener todas las instalaciones
exports.getInstalaciones = async (req, res) => {
    try {
        const instalaciones = await Instalacion.find().populate('materialesUsados.material');
        res.json(instalaciones);
    } catch (error) {
        console.error("Error al obtener instalaciones:", error);
        res.status(500).json({ error: "Error en el servidor" });
    }
};

// Crear instalación (sin materiales aún)
exports.createInstalacion = async (req, res) => {
    try {
        const nueva = new Instalacion(req.body);
        await nueva.save();
        res.json(nueva);
    } catch (error) {
        res.status(500).json({ error: error.message });
    }
};

// Usar material en instalación (disminuye stock) - Individual
exports.usarMaterial = async (req, res) => {
    try {
        const { instalacionId } = req.params;
        const { materialId, cantidad } = req.body;

        const instalacion = await Instalacion.findById(instalacionId);
        if (!instalacion) {
            return res.status(404).json({ error: "Instalación no encontrada" });
        }

        // Validar que la instalación no esté completada
        if (instalacion.estado === 'completada') {
            return res.status(400).json({ error: "No se puede modificar una instalación completada" });
        }

        const material = await Material.findById(materialId);
        if (!material) {
            return res.status(404).json({ error: "Material no encontrado" });
        }

        if (material.cantidad < cantidad) {
            return res.status(400).json({ error: `Stock insuficiente para ${material.nombre}` });
        }

        material.cantidad -= cantidad;
        await material.save();

        instalacion.materialesUsados.push({
            material: materialId,
            cantidad: cantidad
        });

        await instalacion.save();
        await instalacion.populate('materialesUsados.material');

        res.json(instalacion);
    } catch (error) {
        console.error("usarMaterial error:", error);
        res.status(500).json({ error: error.message });
    }
};

// Usar múltiples materiales en instalación - Batch
exports.usarMateriales = async (req, res) => {
    try {
        const { instalacionId } = req.params;
        const { materiales } = req.body; // [{ materialId, cantidad }]
        
        console.log('=== USAR MATERIALES REQUEST ===');
        console.log('instalacionId:', instalacionId);
        console.log('materiales:', JSON.stringify(materiales, null, 2));

        const instalacion = await Instalacion.findById(instalacionId);
        if (!instalacion) {
            console.log('ERROR: Instalación no encontrada');
            return res.status(404).json({ error: "Instalación no encontrada" });
        }

        // Validar que la instalación no esté completada
        if (instalacion.estado === 'completada') {
            return res.status(400).json({ error: "No se puede modificar una instalación completada" });
        }

        // LIMPIAR MATERIALES CORRUPTOS (con material undefined)
        instalacion.materialesUsados = instalacion.materialesUsados.filter(m => m.material != null);
        console.log('Materiales válidos después de limpieza:', instalacion.materialesUsados.length);

        // Validar todos los materiales antes de hacer cambios
        for (const item of materiales) {
            const material = await Material.findById(item.materialId);
            if (!material) {
                return res.status(404).json({ error: "Material no encontrado" });
            }

            if (material.cantidad < item.cantidad) {
                return res.status(400).json({ error: `Stock insuficiente para ${material.nombre}` });
            }
        }

        // Procesar todos los materiales
        for (const item of materiales) {
            const material = await Material.findById(item.materialId);
            material.cantidad -= item.cantidad;
            await material.save();

            // Buscar si ya existe este material en materialesUsados
            const existente = instalacion.materialesUsados.find(
                m => m.material.toString() === item.materialId
            );

            if (existente) {
                // Si existe, incrementar la cantidad
                existente.cantidad += item.cantidad;
            } else {
                // Si no existe, agregarlo
                instalacion.materialesUsados.push({
                    material: item.materialId,
                    cantidad: item.cantidad
                });
            }
        }

        await instalacion.save();
        await instalacion.populate('materialesUsados.material');
        
        console.log('ÉXITO: Materiales guardados. Total materiales usados:', instalacion.materialesUsados.length);
        res.json(instalacion);
    } catch (error) {
        console.error("usarMateriales error:", error);
        console.error("Stack trace:", error.stack);
        res.status(500).json({ error: error.message });
    }
};


// Cambiar estado de instalación
exports.updateEstadoInstalacion = async (req, res) => {
    try {
        const { instalacionId } = req.params;
        const { estado } = req.body;

        const instalacion = await Instalacion.findByIdAndUpdate(
            instalacionId,
            {
                estado,
                fechaCompletado: estado === 'completada' ? new Date() : null
            },
            { new: true }
        ).populate('materialesUsados.material');

        res.json(instalacion);
    } catch (error) {
        res.status(500).json({ error: error.message });
    }
};
// Remover un material específico de una instalación
exports.removerMaterial = async (req, res) => {
    try {
        const { instalacionId, materialId } = req.params;

        const instalacion = await Instalacion.findById(instalacionId);
        if (!instalacion) {
            return res.status(404).json({ error: "Instalación no encontrada" });
        }

        // Validar que la instalación no esté completada
        if (instalacion.estado === 'completada') {
            return res.status(400).json({ error: "No se puede modificar una instalación completada" });
        }

        // Encontrar el índice del material a remover
        const index = instalacion.materialesUsados.findIndex(
            item => item.material.toString() === materialId
        );

        if (index === -1) {
            return res.status(404).json({ error: "Material no encontrado en esta instalación" });
        }

        // Obtener cantidad antes de remover para devolver al stock
        const cantidadUsada = instalacion.materialesUsados[index].cantidad;
        
        // Remover el material del array
        instalacion.materialesUsados.splice(index, 1);

        // Devolver material al stock
        const material = await Material.findById(materialId);
        if (material) {
            material.cantidad += cantidadUsada;
            await material.save();
        }

        await instalacion.save();
        await instalacion.populate('materialesUsados.material');

        res.json(instalacion);
    } catch (error) {
        console.error("removerMaterial error:", error);
        res.status(500).json({ error: error.message });
    }
};
// Alias para compatibilidad
exports.addMaterialToInstalacion = exports.usarMateriales;

// Eliminar instalación
exports.deleteInstalacion = async (req, res) => {
    try {
        const { instalacionId } = req.params;
        await Instalacion.findByIdAndDelete(instalacionId);
        res.json({ message: "Instalación eliminada" });
    } catch (error) {
        res.status(500).json({ error: error.message });
    }
};
