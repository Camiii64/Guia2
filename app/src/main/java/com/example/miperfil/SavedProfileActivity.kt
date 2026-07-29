package com.example.miperfil

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton

class SavedProfileActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_saved_profile)

        val nombre = intent.getStringExtra(ProfileKeys.NOMBRE).orEmpty()
        val correo = intent.getStringExtra(ProfileKeys.CORREO).orEmpty()
        val telefono = intent.getStringExtra(ProfileKeys.TELEFONO).orEmpty()
        val fecha = intent.getStringExtra(ProfileKeys.FECHA).orEmpty()
        val direccion = intent.getStringExtra(ProfileKeys.DIRECCION).orEmpty()
        val camaraConcedida = intent.getBooleanExtra(ProfileKeys.CAMARA_CONCEDIDA, false)
        val estadoCamara = if (camaraConcedida) {
            "Permiso de camara concedido"
        } else {
            "Permiso de camara no concedido"
        }

        findViewById<TextView>(R.id.tvResumenNombre).text = "Nombres y apellidos:\n$nombre"
        findViewById<TextView>(R.id.tvResumenCorreo).text = "Correo electronico:\n$correo"
        findViewById<TextView>(R.id.tvResumenTelefono).text = "Numero de telefono:\n$telefono"
        findViewById<TextView>(R.id.tvResumenFecha).text = "Fecha de nacimiento:\n$fecha"
        findViewById<TextView>(R.id.tvResumenDireccion).text = "Direccion de residencia:\n$direccion"
        findViewById<TextView>(R.id.tvResumenCamara).text = "Fotografia:\n$estadoCamara"

        findViewById<MaterialButton>(R.id.btnRegresarInicio).setOnClickListener {
            val intentInicio = Intent(this, WelcomeActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
            }
            startActivity(intentInicio)
            finish()
        }

        findViewById<MaterialButton>(R.id.btnNuevoPerfil).setOnClickListener {
            val intentNuevo = Intent(this, RegisterProfileActivity::class.java)
            startActivity(intentNuevo)
            finish()
        }
    }
}
