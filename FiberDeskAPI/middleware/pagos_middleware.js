const { body, validationResult } = require('express-validator');

// Validar crear pago
const validarCrearPago = [
    body('usuarioId').notEmpty().withMessage('Usuario ID es requerido'),
    body('monto').isFloat({ min: 0.01 }).withMessage('Monto debe ser mayor a 0'),
    body('abono').isFloat({ min: 0 }).withMessage('Abono debe ser 0 o mayor'),
    body('metodoPago').isIn(['efectivo', 'transferencia', 'tarjeta', 'cheque']).withMessage('Método de pago inválido'),
    body('fechaPago').isISO8601().withMessage('Fecha inválida'),

    (req, res, next) => {
        const errors = validationResult(req);
        if (!errors.isEmpty()) {
            return res.status(400).json({ errores: errors.array() });
        }
        next();
    }
];

// Validar actualizar pago
const validarActualizarPago = [
    body('monto').optional().isFloat({ min: 0.01 }).withMessage('Monto debe ser mayor a 0'),
    body('abono').optional().isFloat({ min: 0 }).withMessage('Abono debe ser 0 o mayor'),
    body('metodoPago').optional().isIn(['efectivo', 'transferencia', 'tarjeta', 'cheque']).withMessage('Método de pago inválido'),
    body('estado').optional().isIn(['pendiente', 'pagado', 'parcial']).withMessage('Estado inválido'),

    (req, res, next) => {
        const errors = validationResult(req);
        if (!errors.isEmpty()) {
            return res.status(400).json({ errores: errors.array() });
        }
        next();
    }
];

module.exports = {
    validarCrearPago,
    validarActualizarPago
};