package com.example.sw2024bgr1_dngm

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.snackbar.Snackbar

class ACicloVida : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_aciclo_vida)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main_ciclo_vida)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            mostrarSnackBar("onCreate")
            insets
        }
    }

    override fun onStart() {
        super.onStart()
        mostrarSnackBar("OnStart")
    }

    override fun onResume() {
        super.onResume()
        mostrarSnackBar("OnResume")
    }

    override fun onRestart() {
        super.onRestart()
        mostrarSnackBar("OnRestart")
    }

    override fun onPause() {
        super.onPause()
        mostrarSnackBar("OnPause")

    }

    override fun onStop() {
        super.onStop()
        mostrarSnackBar("OnStop")
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.run {
            //Guardar las variables
            putString("Variable de texto guardada", textoGlobal)
        }
        super.onSaveInstanceState(outState)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        // Recuperar las variables
        val textoRecuperado: String? = savedInstanceState
            .getString("Variable de texto guardada")
        if (textoRecuperado != null){
            // texto global = texto recuperado
            mostrarSnackBar(textoRecuperado) //guarda el texto global
        }
    }

    var textoGlobal = ""
    fun mostrarSnackBar(text: String){
        textoGlobal += text
        val snack = Snackbar.make(
            findViewById(R.id.cl_ciclo_vida),
            textoGlobal,
            Snackbar.LENGTH_INDEFINITE
        )
        snack.show()
    }
}