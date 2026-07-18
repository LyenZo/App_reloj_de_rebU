# rebU - App del Reloj (Wear OS)

App para Wear OS (Galaxy Watch 5) desarrollada en **Kotlin + Jetpack Compose**. Muestra la ruta activa del repartidor en un mapa (OSMDroid/OpenStreetMap) y emula la tarjeta NFC del cliente mediante Host Card Emulation (HCE) para que el celular del repartidor la escanee y confirme la entrega.

## Tecnologías

- Kotlin
- Jetpack Compose para Wear OS (Compose Navigation, Wear Compose Material3)
- OSMDroid (mapas basados en OpenStreetMap, sin API key)
- Android NFC Host Card Emulation (HCE)

## Estructura del proyecto

```
app/
├── manifests/
│   └── AndroidManifest.xml
├── kotlin+java/com.example.rebu/
│   ├── ClienteHceService.kt        # Servicio HCE: emula la tarjeta del cliente
│   ├── complication/
│   ├── presentation/
│   │   ├── MainActivity.kt          # Navegación (SwipeDismissableNavHost)
│   │   ├── MapaScreen.kt            # Mapa con la ruta activa
│   │   └── PaqueteActivoViewModel.kt # Polling a la API para obtener el pedido activo
│   └── tile/
├── res/
│   └── xml/
│       └── apduservice.xml          # Declaración del servicio HCE (AID)
```

## Pantallas / Funcionalidad

| Componente | Descripción |
|---|---|
| `InicioScreen` | Pantalla de bienvenida con botón para ver el mapa |
| `MapaScreen` | Consulta `/paquete-activo` por polling (cada 5 seg) y muestra el punto del cliente en OSMDroid |
| `ClienteHceService` | Responde con el ID del cliente cuando el celular del repartidor lo escanea vía NFC (AID personalizado) |

## Requisitos previos

- Android Studio
- Galaxy Watch 5 (o cualquier reloj Wear OS con Google Pay habilitado, requerido para HCE) conectado por Wi-Fi para instalación inalámbrica, o el emulador de Wear OS para probar el mapa (el HCE requiere hardware físico)
- Backend Flask corriendo y accesible en la misma red

## Configuración antes de compilar

En `PaqueteActivoViewModel.kt`, actualiza la IP de la API:

```kotlin
val texto = URL("http://TU_IP_LOCAL:5000/paquete-activo").readText()
```

## Cómo correrlo

1. Empareja el reloj con Android Studio (Wi-Fi debugging, vía Developer Options del reloj + Wear OS app en el celular emparejado).
2. Selecciona el dispositivo del reloj en la barra de Android Studio.
3. `Run ▶` o desde terminal:
   ```bash
   ./gradlew installDebug
   ```

## Notas técnicas

- El AID del servicio HCE (`F0010203040506`, declarado en `apduservice.xml`) debe coincidir exactamente con el que usa `EscaneoNFCScreen.js` del lado de la app móvil.
- Google/Samsung no permiten que apps de terceros lean tags NFC en Wear OS — por eso el reloj solo emite (HCE), nunca lee.
- El reloj necesita internet (Wi-Fi propio o passthrough por Bluetooth con el celular) para el polling del mapa.

---

## Subir el proyecto a GitHub

### 1. Si el proyecto todavía no es un repositorio Git

Desde la carpeta raíz del proyecto (donde está `settings.gradle`):

```bash
git init
git add .
git commit -m "Proyecto inicial - app Wear OS rebU"
```

### 2. Agrega un `.gitignore` para Android antes del primer commit (si no lo tienes)

Android Studio normalmente ya lo genera, pero verifica que exista y contenga al menos:

```
*.iml
.gradle
/local.properties
/.idea/
.DS_Store
/build
/captures
.externalNativeBuild
.cxx
```

Si ya hiciste `git add .` sin esto, corre:
```bash
git rm -r --cached .
git add .
git commit -m "Ignorar archivos de build"
```

### 3. Crea el repositorio en GitHub

En [github.com/new](https://github.com/new), crea un repo vacío (sin README, sin licencia, para evitar conflictos) — por ejemplo `rebu-wearos`.

### 4. Conecta tu repo local con GitHub y sube el código

```bash
git branch -M main
git remote add origin https://github.com/LyenZo/rebu-wearos.git
git push -u origin main
```

Si te pide autenticación y falla con password, GitHub ya no acepta contraseña normal por HTTPS — necesitas un [Personal Access Token](https://github.com/settings/tokens) y usarlo como password, o configurar SSH.

### 5. Para subir cambios después

```bash
git add .
git commit -m "Descripción del cambio"
git push
```
