const { exec } = require('child_process');
const fs = require('fs');
const path = require('path');

console.log('🚀 Configurando acceso remoto para FiberDesk API...\n');

// Verificar si ngrok está instalado
exec('ngrok version', (error) => {
    if (error) {
        console.log('❌ ngrok no está instalado.');
        console.log('\n📦 Para instalar ngrok:');
        console.log('1. Opción npm (recomendado):');
        console.log('   npm install -g ngrok');
        console.log('\n2. O descarga desde: https://ngrok.com/download');
        console.log('\nDespués de instalar, ejecuta este script nuevamente.');
        process.exit(1);
    }

    console.log('✅ ngrok está instalado\n');
    console.log('🔧 Iniciando servidor y túnel...\n');
    
    // Iniciar el servidor en segundo plano
    const serverProcess = exec('node server.js', (error, stdout, stderr) => {
        if (error) {
            console.error(`Error en servidor: ${error.message}`);
            return;
        }
    });

    serverProcess.stdout.on('data', (data) => {
        console.log(`[SERVER] ${data}`);
        
        // Cuando el servidor esté listo, iniciar ngrok
        if (data.includes('Servidor backend corriendo')) {
            setTimeout(() => {
                console.log('\n🌐 Iniciando túnel ngrok...\n');
                
                const ngrokProcess = exec('ngrok http 3000', (error) => {
                    if (error) {
                        console.error(`Error en ngrok: ${error.message}`);
                        return;
                    }
                });

                ngrokProcess.stdout.on('data', (data) => {
                    console.log(data);
                });

                // Obtener la URL pública después de 3 segundos
                setTimeout(async () => {
                    try {
                        const response = await fetch('http://127.0.0.1:4040/api/tunnels');
                        const data = await response.json();
                        
                        if (data.tunnels && data.tunnels.length > 0) {
                            const publicUrl = data.tunnels[0].public_url;
                            console.log('\n✅ ¡Túnel creado exitosamente!');
                            console.log('━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━');
                            console.log(`📡 URL Pública: ${publicUrl}`);
                            console.log(`📱 Usa esta URL en tu app Android`);
                            console.log('━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━\n');
                            
                            // Guardar la URL en un archivo
                            const configPath = path.join(__dirname, 'remote-config.json');
                            fs.writeFileSync(configPath, JSON.stringify({
                                publicUrl: publicUrl,
                                apiUrl: publicUrl + '/api/',
                                timestamp: new Date().toISOString()
                            }, null, 2));
                            
                            console.log(`💾 Configuración guardada en: remote-config.json`);
                            console.log('\n🔥 Mantén esta terminal abierta mientras uses la app');
                            console.log('⚠️  Presiona Ctrl+C para detener\n');
                        }
                    } catch (error) {
                        console.error('Error al obtener URL de ngrok:', error.message);
                        console.log('\n💡 Abre http://127.0.0.1:4040 en tu navegador para ver la URL');
                    }
                }, 3000);
            }, 2000);
        }
    });

    serverProcess.stderr.on('data', (data) => {
        console.error(`[SERVER ERROR] ${data}`);
    });

    // Manejar Ctrl+C
    process.on('SIGINT', () => {
        console.log('\n\n⏹️  Deteniendo servidor y túnel...');
        serverProcess.kill();
        exec('taskkill /F /IM ngrok.exe', () => {
            console.log('✅ Detenido correctamente');
            process.exit(0);
        });
    });
});
