const Pago = require('../models/pagos_schema');

// CREATE - Crear nuevo pago
exports.crearPago = async(req, res) => {
    try {
        const { usuarioId, monto, abono, metodoPago, fechaPago, descripcion, prioridad } = req.body;

        const nuevoPago = new Pago({
            usuarioId,
            monto,
            abono,
            metodoPago,
            fechaPago,
            descripcion,
            prioridad: prioridad || 'medio',
            estado: abono >= monto ? 'pagado' : abono > 0 ? 'parcial' : 'pendiente'
        });

        const pagGuardado = await nuevoPago.save();
        res.status(201).json({
            mensaje: 'Pago creado exitosamente',
            pago: pagGuardado
        });
    } catch (error) {
        res.status(500).json({ error: error.message });
    }
};

// READ - Obtener todos los pagos
exports.obtenerPagos = async(req, res) => {
    try {
        const pagos = await Pago.find();
        res.status(200).json(pagos);
    } catch (error) {
        res.status(500).json({ error: error.message });
    }
};

// READ - Obtener pago por ID
exports.obtenerPagoPorId = async(req, res) => {
    try {
        const pago = await Pago.findById(req.params.id);
        if (!pago) {
            return res.status(404).json({ mensaje: 'Pago no encontrado' });
        }
        res.status(200).json(pago);
    } catch (error) {
        res.status(500).json({ error: error.message });
    }
};

// UPDATE - Actualizar pago
exports.actualizarPago = async(req, res) => {
    try {
        const { monto, abono, metodoPago, estado, descripcion, prioridad } = req.body;

        // Primero obtener el pago actual
        const pagoActual = await Pago.findById(req.params.id);
        if (!pagoActual) {
            return res.status(404).json({ mensaje: 'Pago no encontrado' });
        }

        // Luego actualizar con los nuevos valores
        const pago = await Pago.findByIdAndUpdate(
            req.params.id, {
                monto: monto !== undefined ? monto : pagoActual.monto,
                abono: abono !== undefined ? abono : pagoActual.abono,
                metodoPago: metodoPago || pagoActual.metodoPago,
                estado: estado || pagoActual.estado,
                prioridad: prioridad || pagoActual.prioridad,
                descripcion: descripcion !== undefined ? descripcion : pagoActual.descripcion,
                updatedAt: Date.now()
            }, { new: true }
        );

        res.status(200).json({
            mensaje: 'Pago actualizado exitosamente',
            pago
        });
    } catch (error) {
        res.status(500).json({ error: error.message });
    }
};

// DELETE - Eliminar pago
exports.eliminarPago = async(req, res) => {
    try {
        const pago = await Pago.findByIdAndDelete(req.params.id);
        if (!pago) {
            return res.status(404).json({ mensaje: 'Pago no encontrado' });
        }
        res.status(200).json({ mensaje: 'Pago eliminado exitosamente' });
    } catch (error) {
        res.status(500).json({ error: error.message });
    }
};

// Obtener pagos por usuario
exports.obtenerPagosPorUsuario = async(req, res) => {
    try {
        const pagos = await Pago.find({ usuarioId: req.params.usuarioId });
        res.status(200).json(pagos);
    } catch (error) {
        res.status(500).json({ error: error.message });
    }
};