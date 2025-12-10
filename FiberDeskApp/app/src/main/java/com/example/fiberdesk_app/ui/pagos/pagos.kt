import android.os.Bundle
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.ui.layout.layout
import androidx.compose.ui.semantics.text
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

// Asegúrate de que tu clase herede de AppCompatActivity
class PagosActivity : AppCompatActivity() {

    // 1. Declara las vistas que vas a usar
    private lateinit var searchBar: EditText
    private lateinit var saldoPendiente: TextView
    private lateinit var recyclerActividad: RecyclerView
    private lateinit var fabAddPago: androidx.compose.material3.FloatingActionButton
    // Agrega el adapter (lo crearemos después)
    // private lateinit var pagosAdapter: PagosAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // 2. Conecta tu clase con el nuevo archivo XML
        setContentView(R.layout.dashboard_pago) // ¡Asegúrate de que el nombre coincida!

        // 3. Inicializa las vistas usando sus IDs del XML
        searchBar = findViewById(R.id.searchBar)
        saldoPendiente = findViewById(R.id.txtSaldoPendiente) // Asumiendo que este ID existe dentro de la CardView
        recyclerActividad = findViewById(R.id.recyclerActividadReciente)
        fabAddPago = findViewById(R.id.fab_add_pago)

        // 4. Configura el RecyclerView
        // Esto requiere un Adapter, que es el siguiente paso lógico
        recyclerActividad.layoutManager = LinearLayoutManager(this)
        // pagosAdapter = PagosAdapter(listaDePagos) // 'listaDePagos' vendrá de tu API
        // recyclerActividad.adapter = pagosAdapter

        // 5. Configura los listeners (la funcionalidad de los botones)
        fabAddPago.setOnClickListener {
            // Lógica para agregar un nuevo pago
            // Por ejemplo, abrir una nueva actividad o un diálogo
            Toast.makeText(this, "Agregar nuevo pago", Toast.LENGTH_SHORT).show()
        }

        // 6. Llama a la función para cargar los datos desde tu backend
        cargarDatosDePagos()
    }

    private fun cargarDatosDePagos() {
        // Aquí es donde usarás Retrofit o Volley para:
        // a) Obtener el saldo pendiente y ponerlo en el TextView `saldoPendiente`.
        // b) Obtener la lista de actividad reciente.
        // c) Entregar esa lista a tu `PagosAdapter` para que el RecyclerView la muestre.

        // Ejemplo (simulado):
        saldoPendiente.text = "$ 1,150.00 MXN"
        // val listaFalsa = listOf(Pago(...), Pago(...))
        // pagosAdapter.actualizarLista(listaFalsa)
    }
}
