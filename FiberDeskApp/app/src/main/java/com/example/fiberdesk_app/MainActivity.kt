package com.example.fiberdesk_app

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.fiberdesk_app.ui.pagos.PagosFragment

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        
        // Cargar el fragment de pagos al iniciar
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, PagosFragment())
                .commit()
        }
    }
}