package com.example.miperfil

import android.Manifest
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Patterns
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class RegisterProfileActivity : AppCompatActivity() {

    private lateinit var etNombreCompleto: TextInputEditText
    private lateinit var etCorreo: TextInputEditText
    private lateinit var etTelefono: TextInputEditText
    private lateinit var etFechaNacimiento: TextInputEditText
    private lateinit var etDireccion: TextInputEditText
    private lateinit var tvEstadoCamara: TextView

    private var permisoCamaraConcedido = false

    private val solicitarPermisoCamara = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { concedido ->
        permisoCamaraConcedido = concedido
        if (concedido) {
            tvEstadoCamara.text = "El usuario concedio el permiso para la camara."
            mostrarMensaje("Permiso de camara concedido")
        } else {
            tvEstadoCamara.text = "No se puede acceder a la camara porque el permiso fue denegado."
            mostrarMensaje("No se puede acceder a la camara")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register_profile)

        etNombreCompleto = findViewById(R.id.etNombreCompleto)
        etCorreo = findViewById(R.id.etCorreo)
        etTelefono = findViewById(R.id.etTelefono)
        etFechaNacimiento = findViewById(R.id.etFechaNacimiento)
        etDireccion = findViewById(R.id.etDireccion)
        tvEstadoCamara = findViewById(R.id.tvEstadoCamara)

        configurarFormatoFecha()

        findViewById<MaterialButton>(R.id.btnTomarFoto).setOnClickListener {
            solicitarPermisoCamara.launch(Manifest.permission.CAMERA)
        }

        findViewById<MaterialButton>(R.id.btnGuardar).setOnClickListener {
            guardarPerfil()
        }
    }

    private fun configurarFormatoFecha() {
        etFechaNacimiento.addTextChangedListener(object : TextWatcher {
            private var editando = false

            override fun beforeTextChanged(texto: CharSequence?, start: Int, count: Int, after: Int) {
                // No se necesita accion antes del cambio.
            }

            override fun onTextChanged(texto: CharSequence?, start: Int, before: Int, count: Int) {
                // El formato se aplica despues del cambio.
            }

            override fun afterTextChanged(texto: Editable?) {
                if (editando || texto == null) return

                val soloNumeros = texto.toString()
                    .filter { it.isDigit() }
                    .take(8)
                val fechaFormateada = buildString {
                    soloNumeros.forEachIndexed { index, caracter ->
                        if (index == 2 || index == 4) append("/")
                        append(caracter)
                    }
                }

                editando = true
                etFechaNacimiento.setText(fechaFormateada)
                etFechaNacimiento.setSelection(fechaFormateada.length)
                editando = false
            }
        })
    }

    private fun guardarPerfil() {
        val nombre = etNombreCompleto.text.toString().trim()
        val correo = etCorreo.text.toString().trim()
        val telefono = etTelefono.text.toString().trim()
        val fecha = etFechaNacimiento.text.toString().trim()
        val direccion = etDireccion.text.toString().trim()

        limpiarErrores()

        when {
            nombre.isEmpty() -> marcarError(etNombreCompleto, "Ingresa nombres y apellidos")
            correo.isEmpty() -> marcarError(etCorreo, "Ingresa el correo electronico")
            !Patterns.EMAIL_ADDRESS.matcher(correo).matches() ->
                marcarError(etCorreo, "Correo electronico no valido")
            telefono.isEmpty() -> marcarError(etTelefono, "Ingresa el numero de telefono")
            !telefonoEsValido(telefono) ->
                marcarError(etTelefono, "Telefono no valido. Usa de 8 a 15 digitos")
            fecha.isEmpty() -> marcarError(etFechaNacimiento, "Ingresa la fecha de nacimiento")
            !fechaEsValida(fecha) ->
                marcarError(etFechaNacimiento, "Formato valido: dd/MM/yyyy")
            direccion.isEmpty() -> marcarError(etDireccion, "Ingresa la direccion")
            else -> abrirResumen(nombre, correo, telefono, fecha, direccion)
        }
    }

    private fun abrirResumen(
        nombre: String,
        correo: String,
        telefono: String,
        fecha: String,
        direccion: String
    ) {
        val intent = Intent(this, SavedProfileActivity::class.java).apply {
            // Datos enviados a la pantalla de resumen mediante putExtra.
            putExtra(ProfileKeys.NOMBRE, nombre)
            putExtra(ProfileKeys.CORREO, correo)
            putExtra(ProfileKeys.TELEFONO, telefono)
            putExtra(ProfileKeys.FECHA, fecha)
            putExtra(ProfileKeys.DIRECCION, direccion)
            putExtra(ProfileKeys.CAMARA_CONCEDIDA, permisoCamaraConcedido)
        }
        startActivity(intent)
    }

    private fun limpiarErrores() {
        etNombreCompleto.error = null
        etCorreo.error = null
        etTelefono.error = null
        etFechaNacimiento.error = null
        etDireccion.error = null
    }

    private fun marcarError(campo: TextInputEditText, mensaje: String) {
        campo.error = mensaje
        campo.requestFocus()
        mostrarMensaje(mensaje)
    }

    private fun telefonoEsValido(telefono: String): Boolean {
        val soloDigitos = telefono.filter { it.isDigit() }
        return soloDigitos.length in 8..15 && telefono.all {
            it.isDigit() || it == ' ' || it == '-' || it == '+'
        }
    }

    private fun fechaEsValida(fecha: String): Boolean {
        val formato = SimpleDateFormat("dd/MM/yyyy", Locale.US).apply {
            isLenient = false
        }

        return try {
            val fechaIngresada = formato.parse(fecha)
            fechaIngresada != null && !fechaIngresada.after(Date())
        } catch (error: ParseException) {
            false
        }
    }

    private fun mostrarMensaje(mensaje: String) {
        Toast.makeText(this, mensaje, Toast.LENGTH_SHORT).show()
    }
}
