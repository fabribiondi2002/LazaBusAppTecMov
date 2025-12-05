**Trabajo Práctico Integrador - Tecnologías Móviles 2025**

LazaBus es una aplicación móvil desarrollada en Kotlin con Jetpack Compose, diseñada para asistir a personas con discapacidad visual ―y a cualquier usuario― para encontrar la mejor ruta de colectivo en la ciudad.
La app ofrece navegación guiada por voz, selección accesible de ubicaciones, historial de viajes y asistencia por geolocalización en tiempo real.

📱 **Funcionalidades principales**

  - Búsqueda accesible de rutas mediante comandos de voz.
  - Mapa interactivo con ubicación actual, paradas y rutas.
  - Geocoder para convertir direcciones en coordenadas.
  - Historial de viajes almacenado localmente con Room.
  - Opciones de accesibilidad (idioma de voz, velocidad de lectura).
  - Sistema de fallbacks si no hay geolocalización disponible.
  - Interfaz diseñada para personas con discapacidad visual.

🧱** Tecnologías principales utilizadas**

  🎨 Jetpack Compose

    - UI declarativa moderna, accesible y optimizada.
    
  🧭 OSMdroid
  Permite visualizar:
  
    - Mapa offline/online con Capas de puntos (paradas) y marcadores personalizados
    - Zoom, scroll y ubicación actual
      
  📍 Location Services (Fused Location Provider):
  
    - Para obtener la ubicación en tiempo real con alta precisión
    - Se usa para determinar el origen del viaje y sugerir rutas.
  
  🗺️ Geocoder (Android)
  
    - Convierte direcciones habladas a coordenadas GPS.
    - Se utiliza para transformar el destino dicho por voz en un punto en el mapa.
  
  🗣️ STT – Speech To Text (Reconocimiento de voz)
  
    - Convierte la voz del usuario en texto.
    - Usado para captura del destino
  
  🔊 TTS – Text To Speech (Síntesis de voz)
  
    - Convierte texto a voz para guiar al usuario. 
  
  🌐 Retrofit
  
    - Cliente HTTP usado para consultar la ruta optima al backend.
  
  🗃️ Room Database
  
    - Almacena el historial de viajes localmente.

**🗺️ Flujo general de la aplicación**

  1. El usuario presiona el botón accesible.
  2. TTS le da la bienvenida y solicita destino.
  3. El usuario responde por voz → STT lo convierte en texto.
  4. Se normaliza el texto y Geocoder obtiene coordenadas.
  5. Se obtiene la ubicación actual por Location Services.
  6. Se envía origen/destino al backend mediante Retrofit.
  7. El backend devuelve la ruta óptima.
  8. Se marca en el mapa OSMdroid.
  9. TTS anuncia la ruta, paradas y distancias.
  10. Se guarda el viaje en Room.
  11. El usuario puede ver su historial en la pantalla correspondiente.

**👨‍💻 Autores**

Leandro Biondi - lbiondi733@qalumnos.iua.edu.ar

**👨‍💻 Colaboradores**

Benjamín Vargas - bvargas161@alumnos.iua.edu.ar
Antonella Badami - cbadami845@alumnos.iua.edu.ar
