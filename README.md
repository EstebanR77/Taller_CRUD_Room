# CRUD de productos Android

Aplicación sencilla en **Kotlin y Jetpack Compose** que utiliza **Room sobre SQLite**.

## Operaciones
- **Crear:** registrar nombre y precio de un producto.
- **Leer:** consultar los productos guardados.
- **Actualizar:** cambiar el nombre o precio.
- **Eliminar:** borrar un producto.

## Abrir
1. Abre la carpeta `CRUD_Room_Android` en Android Studio con **File > Open**.
2. Sincroniza Gradle e instala los componentes que solicite Android Studio (JDK 17 y SDK 36).
3. Selecciona un emulador y ejecuta la configuración `app`.

## Archivos
- `Producto.kt`: tabla de productos.
- `ProductoDao.kt`: funciones CRUD.
- `AppDatabase.kt`: base de datos Room.
- `MainActivity.kt`: pantalla Jetpack Compose.

El proyecto se entrega como código fuente para subir a un repositorio de GitHub. No incluye APK y su compilación no se verificó en este entorno.
