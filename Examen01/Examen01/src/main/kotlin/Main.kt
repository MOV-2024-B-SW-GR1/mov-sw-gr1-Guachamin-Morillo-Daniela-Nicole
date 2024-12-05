package org.example

import java.io.File
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException

fun main() {
    val archivoRenos = "data/renos.txt"
    val archivoPapaNoel = "data/papa_noel.txt"

    var papaNoel = cargarPapaNoelDesdeArchivo(archivoPapaNoel)?.apply {
        this.renos.addAll(cargarRenosDesdeArchivo(archivoRenos))
    }

    while (true) {
        mostrarMenu()
        when (leerOpcion()) {
            1 -> {
                if (papaNoel == null) {
                    papaNoel = agregarPapaNoel(archivoPapaNoel, archivoRenos)
                } else {
                    println("Ya existe un Papá Noel registrado. Elimínelo para crear uno nuevo.")
                }
            }
            2 -> mostrarInfoPapaNoel(papaNoel)
            3 -> papaNoel?.let { actualizarPapaNoel(it, archivoPapaNoel) } ?: println("No hay Papá Noel registrado.")
            4 -> {
                if (papaNoel != null) {
                    eliminarPapaNoel(papaNoel, archivoPapaNoel, archivoRenos)
                    papaNoel = null
                } else {
                    println("No hay Papá Noel registrado para eliminar.")
                }
            }
            5 -> papaNoel?.let { agregarReno(it, archivoRenos) } ?: println("Primero crea a Papá Noel.")
            6 -> papaNoel?.let { listarRenos(it) } ?: println("Primero crea a Papá Noel.")
            7 -> papaNoel?.let { actualizarReno(it, archivoRenos) } ?: println("Primero crea a Papá Noel.")
            8 -> papaNoel?.let { eliminarReno(it, archivoRenos) } ?: println("Primero crea a Papá Noel.")
            9 -> papaNoel?.let { filtrarRenos(it) } ?: println("Primero crea a Papá Noel.")
            10 -> papaNoel?.let { promocionarLider(it, archivoRenos) } ?: println("Primero crea a Papá Noel.")
            11 -> {
                mostrarDespedida()
                break
            }
            else -> println("Opción no válida. Intente de nuevo.")
        }
    }
}

// --- Menú ---
fun mostrarMenu() {
    println("\u001B[34m") // Color azul
    println("╔════════════════════════════════╗")
    println("║       🎄 Menú Principal 🎅     ║")
    println("╠════════════════════════════════╣")
    println("║ 1. Crear Papá Noel             ║")
    println("║ 2. Ver información de Papá Noel║")
    println("║ 3. Actualizar Papá Noel        ║")
    println("║ 4. Eliminar Papá Noel          ║")
    println("║ 5. Agregar Reno                ║")
    println("║ 6. Listar Renos                ║")
    println("║ 7. Actualizar Reno             ║")
    println("║ 8. Retirar Reno                ║")
    println("║ 9. Filtrar Renos               ║")
    println("║ 10. Promocionar Líder          ║")
    println("║ 11. Salir                      ║")
    println("╚════════════════════════════════╝")
    println("\u001B[0m") // Restablecer color
    print("Seleccione una opción: ")
}

fun mostrarDespedida() {
    println("""
        ${"=".repeat(40)}
         🎅 ¡Adiós! ¡Feliz Navidad y un próspero Año Nuevo! 🎄✨
        ${"=".repeat(40)}
         Que todos tus deseos se hagan realidad. 🦌❄️
    """.trimIndent())
}

fun leerOpcion(): Int = readLine()?.toIntOrNull() ?: -1

// --- Entidades ---
data class Reno(
    val nombre: String,
    var edad: Int,
    var lider: Boolean,
    var peso: Double,
    val fechaIncorporacion: LocalDate
)

data class PapaNoel(
    val nombrePapa: String,
    var edadPapa: Int,
    var pesoPapa: Double,
    var paisResidencia: String,
    val renos: ArrayList<Reno> = arrayListOf()
)

// ---Funciones Papá Noel ---

fun agregarPapaNoel(archivoPapaNoel: String, archivoRenos: String): PapaNoel? {
    val file = File(archivoPapaNoel)
    if (file.exists()) {
        println("Ya existe un Papá Noel registrado. No se puede agregar otro.")
        return null
    }

    println("\n--- Crear Papá Noel ---")
    print("Nombre: ")
    val nombrePapa = readLine().orEmpty()

    var edadPapa: Int
    do{
        print("Edad: ")
        edadPapa = readLine()?.toIntOrNull() ?: -1
        if (edadPapa < 0) println("La edad no puede ser negativa")
    }while (edadPapa < 0)


    var pesoPapa: Double
    do {
        print("Peso: ")
        pesoPapa = readLine()?.toDoubleOrNull() ?: -1.0
        if (pesoPapa < 0) println("El peso no puede ser negativo")
    }while (pesoPapa < 0)

    print("País de Residencia: ")
    var paisResidencia = readLine().orEmpty()

    val papaNoel = PapaNoel(nombrePapa, edadPapa, pesoPapa, paisResidencia)
    guardarPapaNoelEnArchivo(papaNoel, archivoPapaNoel)
    println("¡Papá Noel creado exitosamente!")
    return papaNoel
}

fun mostrarInfoPapaNoel(papaNoel: PapaNoel?) {
    if (papaNoel == null) {
        println("No hay un Papá Noel registrado.")
        return
    }

    println("\n--- Información de Papá Noel ---")
    println("Nombre: ${papaNoel.nombrePapa}")
    println("Edad: ${papaNoel.edadPapa}")
    println("Peso: ${papaNoel.pesoPapa}")
    println("País de Residencia: ${papaNoel.paisResidencia}")
    println("Número de Renos: ${papaNoel.renos.size}")
}

fun actualizarPapaNoel(papaNoel: PapaNoel?, archivoPapaNoel: String) {
    if (papaNoel == null) {
        println("No hay un Papá Noel registrado para actualizar.")
        return
    }

    println("\n--- Actualizar Papá Noel ---")
    print("Nueva edad (actual: ${papaNoel.edadPapa}): ")

    var nuevaEdadP: Int
    do{
        nuevaEdadP = readLine()?.toIntOrNull() ?: -1
        if (nuevaEdadP < 0) println("La edad no puede ser negativa")
    }while (nuevaEdadP < 0)

    print("Nuevo peso (actual: ${papaNoel.pesoPapa}): ")
    var pesoP: Double
    do {
        pesoP = readLine()?.toDoubleOrNull() ?: -1.0
        if (pesoP < 0) println("El peso no puede ser negativo")
    }while (pesoP < 0)

    papaNoel.edadPapa = nuevaEdadP
    papaNoel.pesoPapa = pesoP

    guardarPapaNoelEnArchivo(papaNoel, archivoPapaNoel)
    println("¡Papá Noel actualizado exitosamente!")
}

fun eliminarPapaNoel(papaNoel: PapaNoel?, archivoPapaNoel: String, archivoRenos: String) {
    if (papaNoel == null) {
        println("No hay un Papá Noel registrado para eliminar.")
        return
    }

    println("Eliminando Papá Noel y todos sus renos...")
    File(archivoPapaNoel).delete()
    File(archivoRenos).delete()
    println("¡Papá Noel eliminado exitosamente!")
}

// --- Manejo de archivo de Papá Noel ---
fun guardarPapaNoelEnArchivo(papaNoel: PapaNoel, archivo: String) {
    val file = File(archivo)
    try {
        file.parentFile?.mkdirs()
        file.writeText("${papaNoel.nombrePapa},${papaNoel.edadPapa},${papaNoel.pesoPapa},${papaNoel.paisResidencia}")
    } catch (e: Exception) {
        println("Error al guardar el archivo de Papá Noel: ${e.message}")
    }
}

fun cargarPapaNoelDesdeArchivo(archivo: String): PapaNoel? {
    val file = File(archivo)
    return if (file.exists()) {
        try {
            val partes = file.readText().split(",")
            PapaNoel(partes[0], partes[1].toInt(), partes[2].toDouble(), partes[3])
        } catch (e: Exception) {
            println("Error al cargar el archivo de Papá Noel: ${e.message}")
            null
        }
    } else {
        null
    }
}

// --- Funciones reno---
fun agregarReno(papaNoel: PapaNoel, archivo: String) {
    println("\n--- Agregar Reno ---")
    print("Nombre: ")
    val nombre = readLine().orEmpty()

    var edad: Int
    do{
        print("Edad: ")
        edad = readLine()?.toIntOrNull() ?: -1
        if (edad < 0) println("La edad no puede ser negativa")
    }while (edad < 0)

    var peso: Double
    do {
        print("Peso: ")
        peso = readLine()?.toDoubleOrNull() ?: -1.0
        if (peso < 0) println("El peso no puede ser negativo")
    }while (peso < 0)


    print("Fecha de Incorporación (YYYY-MM-DD): ")
    val fechaIncorporacion = try {
        LocalDate.parse(readLine().orEmpty(), DateTimeFormatter.ISO_LOCAL_DATE)
    } catch (e: DateTimeParseException) {
        println("Fecha inválida, usando fecha actual.")
        LocalDate.now()
    }

    print("¿Es líder? (true/false): ")
    val lider = readLine()?.toBoolean() ?: false

    if (lider && papaNoel.renos.any { it.lider }) {
        println("Ya hay un líder. No se puede agregar otro.")
        return
    }

    val nuevoReno = Reno(nombre, edad, lider, peso, fechaIncorporacion)
    papaNoel.renos.add(nuevoReno)
    guardarRenosEnArchivo(papaNoel.renos, archivo)
    println("¡Reno agregado exitosamente!")
}

fun listarRenos(papaNoel: PapaNoel) {
    if (papaNoel.renos.isEmpty()) {
        println("No hay renos registrados.")
        return
    }

    println("═════════════════════════════════════════════════════")
    println("| Nº | Nombre      | Edad | Líder  | Peso  | Fecha     |")
    println("═════════════════════════════════════════════════════")
    papaNoel.renos.forEachIndexed { index, reno ->
        println(
            "| ${index + 1}  | ${reno.nombre.padEnd(10)} | ${reno.edad}   | ${if (reno.lider) "Sí" else "No"}    | ${reno.peso} | ${reno.fechaIncorporacion} |"
        )
    }
    println("═════════════════════════════════════════════════════")
}

fun actualizarReno(papaNoel: PapaNoel, archivo: String) {
    listarRenos(papaNoel)
    print("Seleccione el número del Reno a actualizar: ")
    val indice = readLine()?.toIntOrNull()?.minus(1) ?: return

    if (indice in papaNoel.renos.indices) {
        val reno = papaNoel.renos[indice]
        println("Actualizando a ${reno.nombre}:")

        var nuevaEdad: Int
        do {
            print("Nueva edad (actual: ${reno.edad}): ")
            nuevaEdad= readLine()?.toIntOrNull() ?: reno.edad
            if (nuevaEdad < 0) println("La edad no puede ser negativa")
        }while (nuevaEdad < 0)
        reno.edad = nuevaEdad

        var nuevoPeso: Double
        do {
            print("Nuevo peso (actual: ${reno.peso}): ")
            nuevoPeso = readLine()?.toDoubleOrNull() ?: reno.peso
            if (nuevoPeso < 0) println("El peso no puede ser negativo")
        }while (nuevoPeso < 0)
        reno.peso = nuevoPeso

        print("¿Es líder? (actual: ${reno.lider}): ")
        val nuevoLider = readLine()?.toBoolean() ?: reno.lider
        if (nuevoLider && papaNoel.renos.any { it.lider && it != reno }) {
            println("Ya hay un líder. No se puede asignar liderazgo.")
        } else {
            reno.lider = nuevoLider
        }
        guardarRenosEnArchivo(papaNoel.renos, archivo)
        println("¡Reno actualizado!")
    } else {
        println("Número inválido.")
    }
}

fun eliminarReno(papaNoel: PapaNoel, archivo: String) {
    listarRenos(papaNoel)
    print("Seleccione el número del Reno a eliminar: ")
    val indice = readLine()?.toIntOrNull()?.minus(1) ?: return

    if (indice in papaNoel.renos.indices) {
        println("Eliminando a ${papaNoel.renos[indice].nombre}...")
        papaNoel.renos.removeAt(indice)
        guardarRenosEnArchivo(papaNoel.renos, archivo)
        println("¡Reno eliminado!")
    } else {
        println("Número inválido.")
    }
}


fun filtrarRenos(papaNoel: PapaNoel) {
    println("\n--- Filtro de Renos ---")
    println("1. Filtrar por edad")
    println("2. Filtrar por peso")
    println("3. Filtrar por líderes")
    print("Seleccione una opción: ")

    val renosFiltrados = when (readLine()?.toIntOrNull()) {
        1 -> {
            print("Ingrese la edad mínima: ")
            val edadMin = readLine()?.toIntOrNull() ?: return
            papaNoel.renos.filter { it.edad >= edadMin }
        }
        2 -> {
            print("Ingrese el peso máximo: ")
            val pesoMax = readLine()?.toDoubleOrNull() ?: return
            papaNoel.renos.filter { it.peso <= pesoMax }
        }
        3 -> {
            papaNoel.renos.filter { it.lider }
        }
        else -> {
            println("Opción no válida.")
            return
        }
    }

    if (renosFiltrados.isEmpty()) {
        println("No se encontraron renos que cumplan con el criterio.")
    } else {
        println("═════════════════════════════════════════════════════")
        println("| Nº | Nombre      | Edad | Líder  | Peso  | Fecha     |")
        println("═════════════════════════════════════════════════════")
        renosFiltrados.forEachIndexed { index, reno ->
            println(
                "| ${index + 1}  | ${reno.nombre.padEnd(10)} | ${reno.edad.toString().padEnd(4)} | ${if (reno.lider) "Sí" else "No"}    | ${reno.peso.toString().padEnd(5)} | ${reno.fechaIncorporacion} |"
            )
        }
        println("═════════════════════════════════════════════════════")
    }
}


fun promocionarLider(papaNoel: PapaNoel, archivo: String) {
    listarRenos(papaNoel)
    print("Seleccione el número del Reno a promover a líder: ")
    val indice = readLine()?.toIntOrNull()?.minus(1) ?: return

    if (indice in papaNoel.renos.indices) {
        papaNoel.renos.forEach { it.lider = false } // Retirar liderazgo a todos
        papaNoel.renos[indice].lider = true
        guardarRenosEnArchivo(papaNoel.renos, archivo)
        println("¡${papaNoel.renos[indice].nombre} ha sido promovido a líder!")
    } else {
        println("Número inválido.")
    }
}

// --- Manejo de archivo ---
fun guardarRenosEnArchivo(renos: List<Reno>, archivo: String) {
    val file = File(archivo)
    try {
        file.parentFile?.mkdirs()
        file.writeText(
            renos.joinToString("\n") { "${it.nombre},${it.edad},${it.lider},${it.peso},${it.fechaIncorporacion}" }
        )
    } catch (e: Exception) {
        println("Error al guardar el archivo: ${e.message}")
    }
}

fun cargarRenosDesdeArchivo(archivo: String): ArrayList<Reno> {
    val file = File(archivo)
    return try {
        if (!file.exists()) ArrayList<Reno>()
        ArrayList(file.readLines().map { linea ->
            val partes = linea.split(",")
            Reno(
                nombre = partes[0],
                edad = partes[1].toInt(),
                lider = partes[2].toBoolean(),
                peso = partes[3].toDouble(),
                fechaIncorporacion = LocalDate.parse(partes[4])
            )
        })
    } catch (e: Exception) {
        println("Error al cargar el archivo: ${e.message}")
        ArrayList()
    }
}

