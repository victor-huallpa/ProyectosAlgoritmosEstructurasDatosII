# Estructuras de Datos y Algoritmos II (EDA2)

Este repositorio contiene una colección de aplicaciones y proyectos desarrollados en **Java (Swing)** que implementan y demuestran el uso práctico de estructuras de datos fundamentales y algoritmos avanzados estudiados en la asignatura de Estructuras de Datos y Algoritmos II.

---

## 📋 Resumen de Módulos y Proyectos

| Proyecto / Programa | Estructura de Datos | Algoritmo / Lógica Principal | Descripción del Problema Real |
| :--- | :--- | :--- | :--- |
| **1. RutaMasCorta** | Grafos (Listas de adyacencia) | Algoritmo de Dijkstra (`PriorityQueue`) | Sistema GPS para encontrar la ruta más corta (en kilómetros) entre ciudades. |
| **2. DirectorioBST** | Árbol Binario de Búsqueda (BST) | Búsqueda, Inserción, Eliminación (3 casos), Recorridos | Agenda telefónica ordenada alfabéticamente con visualización gráfica y búsqueda interactiva. |
| **3. SimuladorBanco** | Colas (`Queue` / FIFO) | Simulación por eventos discretos (Colas Normal y Preferencial) | Simulación de atención al cliente en ventanillas bancarias con estadísticas de tiempo de espera. |
| **4. EditorConPilas** | Pilas (`Deque` / LIFO) | Manejo de estados (`Ctrl+Z`/`Ctrl+Y`) y validación de sintaxis | Editor de texto inteligente con pila de historial y validador de paréntesis/llaves. |
| **5. SIRE (Sistema Inteligente de Respuesta)** | Grafos, Árbol de Decisión, Cola de Prioridad, Pilas | Dijkstra, Clasificación por Árbol, Gestión LIFO | Plataforma integrada avanzada (Java 21, Maven) para la gestión y respuesta ante emergencias urbanas con bitácora CSV y reportes operativos. |

---

## 🚀 Detalle de los Programas

### 1. RutaMasCorta (Grafos y Dijkstra)
- **Ubicación:** `RutaMasCorta/`
- **Concepto:** Modela un mapa geográfico como un **grafo ponderado y no dirigido**, donde las ciudades son los vértices y las carreteras son las aristas con un peso asociado (distancia en kilómetros).
- **Algoritmo:** Utiliza una cola de prioridad (`PriorityQueue`) para calcular eficientemente el camino mínimo desde una ciudad origen a cualquier destino, reconstruyendo y resaltando la ruta óptima en el mapa.

### 2. DirectorioBST (Árbol Binario de Búsqueda)
- **Ubicación:** `DirectorioBST/`
- **Concepto:** Implementa un **Árbol Binario de Búsqueda (BST)** ordenado alfabéticamente por el nombre del contacto.
- **Operaciones:** Inserción ordenada, búsqueda con resaltado gráfico, eliminación de nodos (3 casos con sucesor inorden), y recorridos (Inorden A-Z, Preorden, Postorden).

### 3. SimuladorBanco (Colas FIFO)
- **Ubicación:** `SimuladorBanco/`
- **Concepto:** Simula un sistema de atención en un banco con múltiples ventanillas activas y gestión de filas de espera independientes (cola preferencial y normal).
- **Métricas:** Tiempo de espera promedio, longitud máxima de las colas y clientes atendidos.

### 4. EditorConPilas (Pilas LIFO y Validador)
- **Ubicación:** `EditorConPilas/`
- **Concepto:** Editor de texto interactivo con dos aplicaciones clásicas de Pilas:
  1. **Deshacer / Rehacer (Undo / Redo):** Historial LIFO con visualización en tiempo real de las pilas.
  2. **Validador de Símbolos:** Verificación de balanceo de llaves `{ }`, corchetes `[ ]` y paréntesis `( )`.

### 5. SIRE (Sistema Inteligente de Respuesta ante Emergencias)
- **Ubicación:** `SIRE-Java21/`
- **Tecnología:** Java 21, Swing, Maven.
- **Concepto:** Aplicación robusta e integrada que unifica todos los conceptos clave de EDA2 en un entorno operativo de emergencias:
  - **Red Vial (Grafos & Dijkstra):** Gestión de intersecciones, hospitales, bomberos, comisarías, tramos viales y bloqueo/habilitación de vías con recálculo automático de rutas.
  - **Clasificación (Árbol de Decisión):** Evaluación jerárquica de factores de riesgo, peligro activo y afectación para determinar prioridades (Crítica, Alta, Media, Baja).
  - **Atención (Cola de Prioridad):** Gestión de incidentes pendientes priorizados por gravedad y orden de llegada (FIFO).
  - **Bitácora y Auditoría (Pilas y CSV):** Registro visual LIFO en panel y persistencia en `datos/sire-eventos.csv`.
  - **Mejoras Operativas (Fase 4):** Cierre de emergencias, liberación de unidades, estimación de tiempo de llegada a 30 km/h y exportación de reportes operativos en CSV.

---

## 🛠️ Cómo Compilar y Ejecutar

### Proyectos Individuales (Swing estándar)
Para compilar y ejecutar proyectos como *EditorConPilas*, *RutaMasCorta*, *DirectorioBST* o *SimuladorBanco*:
```bash
cd EditorConPilas/src
javac Main.java EditorConPilas.java
java Main
```

### Proyecto Integrado SIRE (Maven / Java 21)
Para compilar y ejecutar **SIRE**:
```bash
cd SIRE-Java21
mvn clean compile
mvn exec:java
```
O abrir la carpeta `SIRE-Java21` directamente en **IntelliJ IDEA** como proyecto Maven y ejecutar `pe.unaj.sire.Main`.
