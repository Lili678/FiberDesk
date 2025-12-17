// Validar crear cliente
const validarCrearCliente = (req, res, next) => {
    const { Name, LastName, PhoneNumber, Email, Location } = req.body;

    const errores = [];

    // Validar Name
    if (!Name || !Name.FirstName) {
        errores.push({ campo: 'Name.FirstName', mensaje: 'El nombre es requerido' });
    }

    // Validar LastName
    if (!LastName || !LastName.PaternalLastName) {
        errores.push({ campo: 'LastName.PaternalLastName', mensaje: 'El apellido paterno es requerido' });
    }

    // Validar PhoneNumber
    if (!PhoneNumber || !Array.isArray(PhoneNumber) || PhoneNumber.length === 0) {
        errores.push({ campo: 'PhoneNumber', mensaje: 'Debe proporcionar al menos un número de teléfono' });
    } else {
        // Validar formato de teléfonos (10 dígitos para México)
        PhoneNumber.forEach((phone, index) => {
            if (!/^\d{10}$/.test(phone.replace(/\s/g, ''))) {
                errores.push({
                    campo: `PhoneNumber[${index}]`,
                    mensaje: 'Número de teléfono inválido (debe tener 10 dígitos)'
                });
            }
        });
    }

    // Validar Email
    if (!Email) {
        errores.push({ campo: 'Email', mensaje: 'El email es requerido' });
    } else if (!/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(Email)) {
        errores.push({ campo: 'Email', mensaje: 'Email inválido' });
    }

    // Validar Location
    if (!Location) {
        errores.push({ campo: 'Location', mensaje: 'La ubicación es requerida' });
    } else {
        if (!Location.Coordinates) {
            errores.push({ campo: 'Location.Coordinates', mensaje: 'Las coordenadas son requeridas' });
        } else {
            if (Location.Coordinates.Latitude === undefined || Location.Coordinates.Latitude === null) {
                errores.push({ campo: 'Location.Coordinates.Latitude', mensaje: 'Latitud es requerida' });
            } else if (Location.Coordinates.Latitude < -90 || Location.Coordinates.Latitude > 90) {
                errores.push({ campo: 'Location.Coordinates.Latitude', mensaje: 'Latitud inválida (debe estar entre -90 y 90)' });
            }

            if (Location.Coordinates.Longitude === undefined || Location.Coordinates.Longitude === null) {
                errores.push({ campo: 'Location.Coordinates.Longitude', mensaje: 'Longitud es requerida' });
            } else if (Location.Coordinates.Longitude < -180 || Location.Coordinates.Longitude > 180) {
                errores.push({ campo: 'Location.Coordinates.Longitude', mensaje: 'Longitud inválida (debe estar entre -180 y 180)' });
            }
        }
    }

    if (errores.length > 0) {
        return res.status(400).json({ errores });
    }

    next();
};

// Validar actualizar cliente
const validarActualizarCliente = (req, res, next) => {
    const { Email, PhoneNumber, Location } = req.body;

    const errores = [];

    // Validar Email si se proporciona
    if (Email && !/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(Email)) {
        errores.push({ campo: 'Email', mensaje: 'Email inválido' });
    }

    // Validar PhoneNumber si se proporciona
    if (PhoneNumber) {
        if (!Array.isArray(PhoneNumber) || PhoneNumber.length === 0) {
            errores.push({ campo: 'PhoneNumber', mensaje: 'Debe proporcionar al menos un número de teléfono' });
        } else {
            PhoneNumber.forEach((phone, index) => {
                if (!/^\d{10}$/.test(phone.replace(/\s/g, ''))) {
                    errores.push({
                        campo: `PhoneNumber[${index}]`,
                        mensaje: 'Número de teléfono inválido (debe tener 10 dígitos)'
                    });
                }
            });
        }
    }

    // Validar coordenadas si se proporciona Location
    if (Location && Location.Coordinates) {
        if (Location.Coordinates.Latitude !== undefined) {
            if (Location.Coordinates.Latitude < -90 || Location.Coordinates.Latitude > 90) {
                errores.push({ campo: 'Location.Coordinates.Latitude', mensaje: 'Latitud inválida' });
            }
        }
        if (Location.Coordinates.Longitude !== undefined) {
            if (Location.Coordinates.Longitude < -180 || Location.Coordinates.Longitude > 180) {
                errores.push({ campo: 'Location.Coordinates.Longitude', mensaje: 'Longitud inválida' });
            }
        }
    }

    if (errores.length > 0) {
        return res.status(400).json({ errores });
    }

    next();
};

// Validar agregar documento
const validarAgregarDocumento = (req, res, next) => {
    const { Tipo, Nombre, URL } = req.body;

    const errores = [];

    if (!Tipo) {
        errores.push({ campo: 'Tipo', mensaje: 'El tipo de documento es requerido' });
    } else if (!['INE', 'Comprobante Domicilio', 'Contrato', 'Otro'].includes(Tipo)) {
        errores.push({
            campo: 'Tipo',
            mensaje: 'Tipo de documento inválido. Debe ser: INE, Comprobante Domicilio, Contrato, u Otro'
        });
    }

    if (!Nombre) {
        errores.push({ campo: 'Nombre', mensaje: 'El nombre del documento es requerido' });
    }

    if (!URL) {
        errores.push({ campo: 'URL', mensaje: 'La URL del documento es requerida' });
    }

    if (errores.length > 0) {
        return res.status(400).json({ errores });
    }

    next();
};

// Validar actualizar paquete
const validarActualizarPaquete = (req, res, next) => {
    const { NombrePaquete, Velocidad, Precio } = req.body;

    const errores = [];

    if (!NombrePaquete) {
        errores.push({ campo: 'NombrePaquete', mensaje: 'El nombre del paquete es requerido' });
    }

    if (!Velocidad) {
        errores.push({ campo: 'Velocidad', mensaje: 'La velocidad es requerida' });
    }

    if (Precio === undefined || Precio === null) {
        errores.push({ campo: 'Precio', mensaje: 'El precio es requerido' });
    } else if (Precio < 0) {
        errores.push({ campo: 'Precio', mensaje: 'El precio debe ser mayor o igual a 0' });
    }

    if (errores.length > 0) {
        return res.status(400).json({ errores });
    }

    next();
};

module.exports = {
    validarCrearCliente,
    validarActualizarCliente,
    validarAgregarDocumento,
    validarActualizarPaquete
};