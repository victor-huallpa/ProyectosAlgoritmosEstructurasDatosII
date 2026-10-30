# SIRE — Sistema Inteligente de Respuesta ante Emergencias

Aplicación académica de escritorio desarrollada con **Java 21, Swing y Maven**, preparada para abrirse en IntelliJ IDEA.

## Requisitos y ejecución

- JDK 21
- IntelliJ IDEA con soporte Maven

1. Descomprime el proyecto y abre la carpeta `SIRE-Java21` en IntelliJ IDEA.
2. Espera a que Maven importe `pom.xml`.
3. Configura el Project SDK en JDK 21.
4. Ejecuta `src/main/java/pe/unaj/sire/Main.java`.

Desde una terminal con Maven instalado:
```bash
mvn clean compile
mvn exec:java
```

## Qué incluye la versión integrada

### 1. Grafos: red vial de la ciudad
- Grafo no dirigido ponderado implementado mediante listas de adyacencia.
- Nodos para intersecciones, hospitales, estación de bomberos y comisaría.
- Vías con distancias en kilómetros.
- Escenario ficticio precargado y edición desde la interfaz: agregar ubicaciones y vías.
- Bloqueo/habilitación de vías.
- Algoritmo de Dijkstra que calcula la ruta de menor distancia e ignora vías bloqueadas.
- Ruta calculada resaltada sobre el grafo.

### 2. Árbol binario de decisión: clasificación de emergencias
El árbol examina riesgo vital, peligro activo, cantidad de personas afectadas y tipo de incidente. Clasifica cada emergencia como crítica, alta, media o baja. La interfaz permite consultar las reglas.

**Importante:** las reglas son demostrativas para el trabajo académico; no constituyen protocolos médicos ni operativos oficiales.

### 3. Cola de prioridad: orden de atención
Las emergencias pendientes se ordenan por gravedad, de mayor a menor. Cuando dos incidentes tienen la misma prioridad, se conserva el orden de llegada (FIFO entre prioridades iguales).

### 4. Pila: historial de eventos
La pestaña del centro de emergencias registra eventos en una pila (`Deque`): el evento más reciente se muestra arriba. El historial de esta fase está limitado a los últimos 100 eventos.

### 5. Despacho simulado
- Selecciona la emergencia pendiente con mayor prioridad.
- Busca una unidad disponible del tipo requerido.
- Calcula la ruta de menor distancia desde la unidad hasta el incidente con Dijkstra.
- Si encuentra ruta, asigna la unidad, la marca como ocupada y resalta la ruta.
- Si no hay unidad compatible disponible o no existe una ruta abierta, informa que no puede realizar el despacho.

## Escenario inicial

El grafo usa ubicaciones, distancias y posiciones sintéticas con nombres como Plaza Central, Mercado, Terminal, Hospital 1, Hospital 2, Bomberos y Comisaría. Las unidades y las tres emergencias de demostración también son datos ficticios. No es cartografía oficial ni un sistema conectado con servicios de emergencia reales.

## Estructura del código

```text
src/main/java/pe/unaj/sire/
├── Main.java
├── modelo/
│   ├── Ubicacion.java
│   ├── TipoUbicacion.java
│   ├── Tramo.java
│   ├── Grafo.java
│   ├── UnidadEmergencia.java
│   ├── TipoUnidad.java
│   ├── EstadoUnidad.java
│   ├── Emergencia.java
│   ├── TipoEmergencia.java
│   ├── NivelPrioridad.java
│   └── EstadoEmergencia.java
├── servicio/
│   ├── DatosIniciales.java
│   ├── Dijkstra.java
│   ├── ResultadoRuta.java
│   ├── ArbolDecisionEmergencia.java
│   └── ColaPrioridadEmergencias.java
└── vista/
    ├── FrmPrincipal.java
    ├── PanelGrafo.java
    ├── DialogoUbicacion.java
    └── PanelEmergencias.java
```

## Inspiración de EDA2 integrada

Se reutilizaron y adaptaron ideas didácticas de los ejemplos enviados:
- `RutaMasCorta`: grafo ponderado, Dijkstra y ruta resaltada.
- `DirectorioBST`: idea de representar y recorrer decisiones mediante árboles; SIRE usa un árbol binario de decisión especializado.
- `SimuladorBanco`: prioridad y orden de atención; SIRE utiliza una cola de prioridad para incidentes.
- `EditorConPilas`: estructura LIFO; SIRE muestra el historial de eventos con el último evento arriba.

Las aplicaciones de EDA2 no se ejecutan por separado: los conceptos relevantes se integran en una sola aplicación SIRE.

## Alcance y limitaciones actuales

- Los cambios se mantienen en memoria mientras la aplicación está abierta; todavía no hay persistencia en archivos o base de datos.
- La asignación y los tiempos son simulados; no se usan GPS, tráfico en vivo, geolocalización ni servicios reales.
- El algoritmo optimiza distancia en kilómetros, no tiempo de llegada.
- En esta fase una unidad asignada queda ocupada; falta una interfaz para registrar la finalización de atención y liberarla.
- Para uso real se necesitarían validación de protocolos, datos geográficos verificados, seguridad, auditoría, pruebas extensas y supervisión profesional.

## Fase 4 — mejoras operativas

Esta fase añade cinco mejoras sobre el sistema integrado:

1. **Bitácora persistente de eventos:** los eventos se agregan a `datos/sire-eventos.csv` con fecha y hora. El archivo se crea automáticamente en el directorio de ejecución.
2. **Cierre de emergencias:** permite marcar como atendida una emergencia previamente asignada y devuelve la unidad a estado disponible.
3. **Tiempo de llegada orientativo:** el despacho muestra distancia de ruta y un cálculo estimado usando una velocidad académica fija de 30 km/h. No representa tráfico real ni debe usarse para decisiones reales.
4. **Exportación de reportes:** permite guardar el listado de emergencias en CSV para abrirlo con una hoja de cálculo.
5. **Resumen operativo mejorado:** muestra pendientes, asignadas, atendidas, unidades disponibles y total de emergencias.

**Importante:** la bitácora y los reportes sí se guardan en archivos, pero el estado completo de la ciudad, unidades y emergencias todavía se mantiene en memoria. Al reiniciar, el escenario de demostración vuelve a cargarse; no hay recuperación automática de sesiones.
