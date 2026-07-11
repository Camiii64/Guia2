package com.example.guia2

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import java.util.Calendar

class MainActivity : AppCompatActivity() {

    // 1. Variables
    lateinit var btnSaludar: Button
    lateinit var btnLimpiar: Button
    lateinit var etNombre: EditText
    lateinit var etApellido: EditText
    lateinit var tvSaludo: TextView
    lateinit var tvMensaje: TextView

    // 2. Metodo del ciclo de vida del activity
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // 3. Establecer archivo layout
        setContentView(R.layout.activity_main)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // 4. Referencias a elementos de la UI
        btnSaludar = findViewById(R.id.btnSaludar)
        btnLimpiar = findViewById(R.id.btnLimpiar)
        etNombre = findViewById(R.id.etNombre)
        etApellido = findViewById(R.id.etApellido)
        tvSaludo = findViewById(R.id.tvSaludo)
        tvMensaje = findViewById(R.id.tvMensaje)

        tvSaludo.text = ""
        tvMensaje.text = ""

        // 5. Listeners de los botones
        btnSaludar.setOnClickListener {
            val nombre = etNombre.text.toString().trim()
            val apellido = etApellido.text.toString().trim()

            when {
                nombre.isEmpty() -> mostrarToast("Error, el campo nombre esta vacio.")
                apellido.isEmpty() -> mostrarToast("Error, el campo apellido esta vacio.")
                else -> {
                    val calendar = Calendar.getInstance()
                    val hora = calendar.get(Calendar.HOUR_OF_DAY)
                    val esDia = hora in 6..17
                    val saludoDia = if (esDia) "Hola buenos dias" else "Hola buenas noches"

                    tvSaludo.text = "$saludoDia, $nombre $apellido"
                    tvMensaje.text = "Bienvenido al laboratorio 2 de DSM441."

                    mostrarToast("Saludo generado exitosamente")
                }
            }
        }

        btnLimpiar.setOnClickListener {
            etNombre.text.clear()
            etApellido.text.clear()

            tvSaludo.text = ""
            tvMensaje.text = ""

            etNombre.requestFocus()

            mostrarToast("Pantalla reiniciada")
        }
    }

    // Funcion auxiliar para mostrar mensaje emergente
    private fun mostrarToast(
        mensaje: String,
        duracion: Int = Toast.LENGTH_SHORT
    ) {
        Toast.makeText(
            this,
            mensaje,
            duracion
        ).show()
    }
}
