# Documentación de MoviesMatch

## Estructura de paquetes
- `com.example.moviematch`
  - `ActividadPrincipal`: decide el flujo inicial entre configuración y pantalla de inicio.
  - `interfaz`
    - `inicio`: `InicioFragment` controla la búsqueda principal y el menú de accesos rápidos.
    - `configuracion`: `ConfiguracionInicialFragment` recoge géneros, plataformas y etiquetas a evitar.
    - `recomendaciones`: `RecomendacionesFragment` muestra la lista de resultados y `AdaptadorPeliculas` pinta cada tarjeta.
    - `detalle`: `DetallePeliculaFragment` enseña la ficha y permite guardar/descartar.
    - `guardadas`: `VerDespuesFragment` lista y gestiona la watchlist.
    - `grupo`: `ModoGrupoFragment` genera/valida códigos y muestra una recomendación de grupo.
    - `comun`: `TextoDegradadoView` para títulos con degradado.
  - `datos`
    - `PreferenciasUsuario`: gestiona SharedPreferences con Gson para onboarding y selección actual.
    - `RepositorioPeliculas`: lee `assets/peliculas.json` y entrega el catálogo local.
    - `MotorRecomendaciones`: calcula los puntajes y devuelve el top 10.
    - `db`: `BaseDatosApp` (Room) con DAOs y entidades para watchlist y descartadas.
  - `modelo`: `Pelicula` representa cada película del catálogo.

## Flujo de pantallas
1. **Primera vez**: `ActividadPrincipal` consulta `PreferenciasUsuario.onboardingCompletado()`. Si es falso, abre `ConfiguracionInicialFragment`; de lo contrario, `InicioFragment`.
2. **Configuración**: el usuario marca géneros/plataformas/evitar y guarda; se marca onboarding completado y se navega a Inicio.
3. **Inicio**: el usuario elige tiempo, estado de ánimo y compañía. Botón "Buscar película" navega a `RecomendacionesFragment`. El menú flotante abre Ver después, Configurar u opciones de Modo grupo.
4. **Resultados**: se muestra el RecyclerView; al tocar una película se abre `DetallePeliculaFragment`. El back stack permite regresar.
5. **Detalle**: botones "Ver después" (guarda en Room) y "No me interesa" (marca descartada y vuelve atrás).
6. **Ver después**: lista persistente editable con pulsación larga para eliminar.
7. **Modo grupo**: genera/valida códigos offline y muestra una recomendación basada en preferencias o aleatoria.

## Motor de recomendaciones
- Fuente de datos: `assets/peliculas.json` con al menos 20 entradas locales (sin API externa).
- Puntaje por película:
  - `+3` si comparte género con favoritos.
  - `+2` si `moodSugerido` coincide con el estado de ánimo.
  - `+2` si la compañía elegida encaja con géneros favorecidos (Familia: Familia/Animación/Comedia; Pareja: Romance/Comedia/Drama; Amigos: Acción/Comedia/Sci-Fi/Terror).
  - `+2` si hay coincidencia de plataforma seleccionada.
  - `-5` si contiene una etiqueta marcada para evitar.
  - `-3` si no respeta el rango de duración elegido (30: ≤60, 1h: 60-90, 1.5h: 90-120, 2h+: ≥120).
- Tras calcular, ordena por puntaje y devuelve el top 10 excluyendo descartadas. Si no hay resultados, devuelve un conjunto básico sin descartadas. `recomendarAleatoria` permite una sugerencia simple para modo grupo.

## Persistencia
- **Preferencias de usuario y bandera de onboarding**: `PreferenciasUsuario` guarda un JSON en `SharedPreferences` (Gson) con géneros, plataformas, etiquetas a evitar y la última selección de tiempo/estado/compañía.
- **Watchlist (Ver después)**: `PeliculaGuardadaEntity` en Room (`BaseDatosApp.watchlistDao`). Se guarda desde el detalle y se muestra/elimina en `VerDespuesFragment`.
- **Descartadas**: `PeliculaDescartadaEntity` en Room (`BaseDatosApp.descartadasDao`). Se agrega desde el detalle y se usa para filtrar futuras recomendaciones, incluso tras cerrar la app.
