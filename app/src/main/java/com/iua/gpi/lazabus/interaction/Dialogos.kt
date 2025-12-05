package com.iua.gpi.lazabus.interaction

/**
 * Contiene todos los textos usados por la app en español e inglés.
 */
object Dialogos {

    enum class Idioma { ES, EN }

    private var idiomaActual: Idioma = Idioma.ES

    fun setIdioma(langCode: String) {
        idiomaActual = when (langCode) {
            "en" -> Idioma.EN
            else -> Idioma.ES
        }
    }

    private fun t(es: String, en: String): String {
        return if (idiomaActual == Idioma.ES) es else en
    }

    val bienvenida: String
        get() = t(
            "Bienvenido a LázaBus, presiona la parte inferior de tu pantalla para comenzar",
            "Welcome to LazaBus, press the bottom of your screen to begin"
        )

    val pedirDestino: String
        get() = t(
            "¿A dónde te interesaría ir hoy?",
            "Where would you like to go today?"
        )

    val repetirDestino: String
        get() = t(
            "Disculpa, no pude entenderte bien. ¿Podrías repetir?",
            "Sorry, I could not understand you. Could you repeat?"
        )

    fun confirmarDestino(destino: String) = t(
        "Perfecto, entendí que te interesa ir a $destino",
        "Perfect, I understood that you want to go to $destino"
    )

    fun anunciarRuta(
        ruta: String,
        empresa: String,
        distanciaOrigen: String,
        inicio: String,
        final: String,
        distanciaDestino: String,
        destinoTexto: String
    ): String {
        return t(
            "La mejor ruta según tu ubicación actual es la ruta $ruta de la empresa $empresa. " +
                    "La parada más cercana está a $distanciaOrigen metros en $inicio. " +
                    "Debes bajarte en $final, que está a $distanciaDestino metros de $destinoTexto. " +
                    "Para realizar una nueva consulta presiona la parte inferior de tu pantalla.",

            "The best route based on your current location is route $ruta from the company $empresa. " +
                    "The closest stop is $distanciaOrigen meters away at $inicio. " +
                    "You should get off at $final, which is $distanciaDestino meters from $destinoTexto. " +
                    "To start a new search, press the bottom of your screen."
        )
    }
}