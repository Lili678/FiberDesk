const Cliente = require('../models/clientes_schema');

// CREATE - Crear nuevo cliente
exports.crearCliente = async(req, res) => {
    try {
        const {
            Name,
            LastName,
            PhoneNumber,
            Email,
            Location,
            Paquete
        } = req.body;

        const nuevoCliente = new Cliente({
            Name,
            LastName,
            PhoneNumber,
            Email,
            Location,
            Paquete,
            Archived: false
        });

        const clienteGuardado = await nuevoCliente.save();

        res.status(201).json({
            mensaje: 'Cliente creado exitosamente',
            cliente: clienteGuardado
        });
    } catch (error) {
        res.status(500).json({
            error: 'Error al crear cliente',
            detalle: error.message
        });
    }
};

// READ - Obtener todos los clientes (no archivados por defecto)
exports.obtenerClientes = async(req, res) => {
    try {
        const { incluirArchivados } = req.query;

        const filtro = incluirArchivados === 'true' ? {} : { Archived: false };

        const clientes = await Cliente.find(filtro)
            .sort({ CreateDate: -1 });

        res.status(200).json(clientes);
    } catch (error) {
        res.status(500).json({
            error: 'Error al obtener clientes',
            detalle: error.message
        });
    }
};

// READ - Obtener cliente por ID
exports.obtenerClientePorId = async(req, res) => {
    try {
        const cliente = await Cliente.findById(req.params.id);

        if (!cliente) {
            return res.status(404).json({
                mensaje: 'Cliente no encontrado'
            });
        }

        res.status(200).json(cliente);
    } catch (error) {
        res.status(500).json({
            error: 'Error al obtener cliente',
            detalle: error.message
        });
    }
};

// READ - Buscar clientes por nombre, email o teléfono
exports.buscarClientes = async(req, res) => {
    try {
        const { query } = req.query;

        if (!query) {
            return res.status(400).json({
                error: 'Se requiere un término de búsqueda'
            });
        }

        const clientes = await Cliente.find({
            $and: [
                { Archived: false },
                {
                    $or: [
                        { 'Name.FirstName': { $regex: query, $options: 'i' } },
                        { 'LastName.PaternalLastName': { $regex: query, $options: 'i' } },
                        { Email: { $regex: query, $options: 'i' } },
                        { PhoneNumber: { $in: [new RegExp(query, 'i')] } }
                    ]
                }
            ]
        });

        res.status(200).json(clientes);
    } catch (error) {
        res.status(500).json({
            error: 'Error al buscar clientes',
            detalle: error.message
        });
    }
};

// UPDATE - Actualizar cliente
exports.actualizarCliente = async(req, res) => {
    try {
        const {
            Name,
            LastName,
            PhoneNumber,
            Email,
            Location,
            Paquete
        } = req.body;

        const clienteActual = await Cliente.findById(req.params.id);

        if (!clienteActual) {
            return res.status(404).json({
                mensaje: 'Cliente no encontrado'
            });
        }

        const clienteActualizado = await Cliente.findByIdAndUpdate(
            req.params.id, {
                Name: Name || clienteActual.Name,
                LastName: LastName || clienteActual.LastName,
                PhoneNumber: PhoneNumber || clienteActual.PhoneNumber,
                Email: Email || clienteActual.Email,
                Location: Location || clienteActual.Location,
                Paquete: Paquete || clienteActual.Paquete,
                UpdateDate: Date.now()
            }, { new: true, runValidators: true }
        );

        res.status(200).json({
            mensaje: 'Cliente actualizado exitosamente',
            cliente: clienteActualizado
        });
    } catch (error) {
        res.status(500).json({
            error: 'Error al actualizar cliente',
            detalle: error.message
        });
    }
};

// UPDATE - Agregar documento a cliente
exports.agregarDocumento = async(req, res) => {
    try {
        const { Tipo, Nombre, URL } = req.body;

        const cliente = await Cliente.findById(req.params.id);

        if (!cliente) {
            return res.status(404).json({
                mensaje: 'Cliente no encontrado'
            });
        }

        cliente.Documentos.push({
            Tipo,
            Nombre,
            URL,
            FechaSubida: new Date()
        });

        await cliente.save();

        res.status(200).json({
            mensaje: 'Documento agregado exitosamente',
            cliente
        });
    } catch (error) {
        res.status(500).json({
            error: 'Error al agregar documento',
            detalle: error.message
        });
    }
};

// UPDATE - Actualizar paquete del cliente
exports.actualizarPaquete = async(req, res) => {
    try {
        const { NombrePaquete, Velocidad, Precio } = req.body;

        const cliente = await Cliente.findByIdAndUpdate(
            req.params.id, {
                Paquete: {
                    NombrePaquete,
                    Velocidad,
                    Precio,
                    FechaContratacion: new Date()
                },
                UpdateDate: Date.now()
            }, { new: true }
        );

        if (!cliente) {
            return res.status(404).json({
                mensaje: 'Cliente no encontrado'
            });
        }

        res.status(200).json({
            mensaje: 'Paquete actualizado exitosamente',
            cliente
        });
    } catch (error) {
        res.status(500).json({
            error: 'Error al actualizar paquete',
            detalle: error.message
        });
    }
};

// UPDATE - Archivar/Desarchivar cliente
exports.archivarCliente = async(req, res) => {
    try {
        const { archivar } = req.body; // true o false

        const cliente = await Cliente.findByIdAndUpdate(
            req.params.id, {
                Archived: archivar !== undefined ? archivar : true,
                UpdateDate: Date.now()
            }, { new: true }
        );

        if (!cliente) {
            return res.status(404).json({
                mensaje: 'Cliente no encontrado'
            });
        }

        res.status(200).json({
            mensaje: `Cliente ${cliente.Archived ? 'archivado' : 'desarchivado'} exitosamente`,
            cliente
        });
    } catch (error) {
        res.status(500).json({
            error: 'Error al archivar cliente',
            detalle: error.message
        });
    }
};

// DELETE - Eliminar cliente permanentemente
exports.eliminarCliente = async(req, res) => {
    try {
        const cliente = await Cliente.findByIdAndDelete(req.params.id);

        if (!cliente) {
            return res.status(404).json({
                mensaje: 'Cliente no encontrado'
            });
        }

        res.status(200).json({
            mensaje: 'Cliente eliminado exitosamente'
        });
    } catch (error) {
        res.status(500).json({
            error: 'Error al eliminar cliente',
            detalle: error.message
        });
    }
};

// READ - Obtener información completa del cliente (para vista detallada)
exports.obtenerInfoCompleta = async(req, res) => {
    try {
        const cliente = await Cliente.findById(req.params.id);

        if (!cliente) {
            return res.status(404).json({
                mensaje: 'Cliente no encontrado'
            });
        }

        // Aquí puedes agregar lógica para obtener pagos y tickets relacionados
        // Por ahora solo devolvemos la info del cliente

        res.status(200).json({
            InfoCliente: {
                _id: cliente._id,
                NombreCompleto: `${cliente.Name.FirstName} ${cliente.Name.MiddleName || ''} ${cliente.LastName.PaternalLastName} ${cliente.LastName.MaternalLastName || ''}`.trim(),
                Email: cliente.Email,
                Telefonos: cliente.PhoneNumber,
                CreateDate: cliente.CreateDate
            },
            Documentos: cliente.Documentos,
            Ubicacion: cliente.Location,
            Paquete: cliente.Paquete,
            // TODO: Agregar consultas a colecciones de Pagos y Tickets
            Pagos: [], // Se llenará cuando implementes la relación
            Tickets: [] // Se llenará cuando implementes la relación
        });
    } catch (error) {
        res.status(500).json({
            error: 'Error al obtener información completa',
            detalle: error.message
        });
    }
};