// Validar crear pago
const validarCrearPago = (req, res, next) => {
    const { usuarioId, monto, abono, metodoPago, fechaPago, prioridad } = req.body;

    const errores = [];

    if (!usuarioId) {
        errores.push({ campo: 'usuarioId', mensaje: 'Usuario ID es requerido' });
    }

    if (!monto || monto <= 0) {
        errores.push({ campo: 'monto', mensaje: 'Monto debe ser mayor a 0' });
    }

    if (abono !== undefined && abono < 0) {
        errores.push({ campo: 'abono', mensaje: 'Abono debe ser 0 o mayor' });
    }

    if (!metodoPago || !['efectivo', 'transferencia', 'tarjeta', 'cheque'].includes(metodoPago)) {
        errores.push({ campo: 'metodoPago', mensaje: 'Método de pago inválido' });
    }

    if (!fechaPago) {
        errores.push({ campo: 'fechaPago', mensaje: 'Fecha es requerida' });
    }

    if (prioridad && !['bajo', 'medio', 'alto', 'urgente'].includes(prioridad)) {
        errores.push({ campo: 'prioridad', mensaje: 'Prioridad inválida. Debe ser: bajo, medio, alto o urgente' });
    }

    if (errores.length > 0) {
        return res.status(400).json({ errores });
    }

    next();
};

// Validar actualizar pago
const validarActualizarPago = (req, res, next) => {
    const { monto, abono, metodoPago, estado, prioridad } = req.body;

    const errores = [];

    if (monto !== undefined && monto <= 0) {
        errores.push({ campo: 'monto', mensaje: 'Monto debe ser mayor a 0' });
    }

    if (abono !== undefined && abono < 0) {
        errores.push({ campo: 'abono', mensaje: 'Abono debe ser 0 o mayor' });
    }

    if (metodoPago && !['efectivo', 'transferencia', 'tarjeta', 'cheque'].includes(metodoPago)) {
        errores.push({ campo: 'metodoPago', mensaje: 'Método de pago inválido' });
    }

    if (estado && !['pendiente', 'pagado', 'parcial'].includes(estado)) {
        errores.push({ campo: 'estado', mensaje: 'Estado inválido' });
    }

    if (prioridad && !['bajo', 'medio', 'alto', 'urgente'].includes(prioridad)) {
        errores.push({ campo: 'prioridad', mensaje: 'Prioridad inválida. Debe ser: bajo, medio, alto o urgente' });
    }

    if (errores.length > 0) {
        return res.status(400).json({ errores });
    }

    next();
};

module.exports = {
    validarCrearPago,
    validarActualizarPago
};