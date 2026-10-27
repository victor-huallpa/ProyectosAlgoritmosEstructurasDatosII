# Estructuras de Datos y Algoritmos II (EDA2)

Este repositorio contiene una colección de aplicaciones interactivas desarrolladas en **Java (Swing)** que implementan y demuestran el uso práctico de estructuras de datos fundamentales y algoritmos avanzados estudiados en la asignatura de Estructuras de Datos y Algoritmos II.

---

## 📋 Resumen de Programas

| Programa | Estructura de Datos | Algoritmo / Lógica Principal | Descripción del Problema Real |
| :--- | :--- | :--- | :--- |
| **1. RutaMasCorta** | Grafos (Listas de adyacencia) | Algoritmo de Dijkstra (`PriorityQueue`) | Sistema GPS para encontrar la ruta más corta (en kilómetros) entre ciudades. |
| **2. DirectorioBST** | Árbol Binario de Búsqueda (BST) | Búsqueda, Inserción, Eliminación (3 casos), Recorridos (Inorden, Preorden, Postorden) | Agenda telefónica ordenada alfabéticamente con visualización gráfica y búsqueda interactiva. |
| **3. SimuladorBanco** | Colas (`Queue` / FIFO) | Simulación por eventos discretos (Colas Normal y Preferencial) | Simulación de atención al cliente en ventanillas bancarias con estadísticas de tiempo de espera. |
| **4. EditorConPilas** | Pilas (`Deque` / LIFO) | Manejo de estados para Deshacer/Rehacer (`Ctrl+Z`/`Ctrl+Y`) y validación de paréntesis/llaves | Editor de texto inteligente con pila de historial y validador de sintaxis de símbolos. |

---

## 🚀 Detalle de los Programas

### 1. RutaMasCorta (Grafos y Dijkstra)
- **Ubicación:** `RutaMasCorta/`
- **Concepto:** Modela un mapa geográfico como un **grafo ponderado y no dirigido**, donde las ciudades son los vértices y las carreteras son las aristas con un peso asociado (distancia en kilómetros).
- **Algoritmo de Dijkstra:** Utiliza una cola de prioridad (`PriorityQueue`) para calcular eficientemente el camino mínimo desde una ciudad origen a cualquier destino, reconstruyendo y resaltando la ruta óptima en el mapa.
- **Interfaz y Uso:**
  - *Modo Agregar ciudad:* Clic en cualquier espacio vacío del lienzo para fundar una nueva ciudad.
  - *Modo Conectar ciudades:* Clic en dos ciudades consecutivas para establecer una carretera y definir su distancia en kilómetros.
  - Selección de Origen y Destino mediante menús desplegables para calcular la ruta óptima.

### 2. DirectorioBST (Árbol Binario de Búsqueda)
- **Ubicación:** `DirectorioBST/`
- **Concepto:** Implementa un **Árbol Binario de Búsqueda (BST)** ordenado alfabéticamente por el nombre del contacto.
- **Operaciones implementadas:**
  - **Inserción:** Ubica el contacto en la posición correcta según el orden lexicográfico.
  - **Búsqueda:** Localiza un contacto resaltando gráficamente el camino exacto recorrido desde la raíz.
  - **Eliminación:** Soporta los 3 casos clásicos de eliminación en un BST (nodo hoja, nodo con un hijo, y nodo con dos hijos utilizando su sucesor inorden).
  - **Recorridos:** Genera y muestra los recorridos **Inorden** (alfabético A-Z), **Preorden** y **Postorden**.
  - **Métricas:** Calcula en tiempo real la altura del árbol y el número total de nodos.

### 3. SimuladorBanco (Colas FIFO)
- **Ubicación:** `SimuladorBanco/`
- **Concepto:** Simula un sistema de atención en un banco con múltiples ventanillas activas y gestión de filas de espera.
- **Estructuras:** Utiliza dos **Colas (Queue)** independientes: una cola **preferencial** (prioritaria) y una cola **normal**. Cuando una ventanilla queda libre, atiende primero a los clientes preferenciales.
- **Simulación temporal:** Cada "tick" del reloj simulado avanza 1 minuto, generando llegadas aleatorias de clientes y procesando tiempos de servicio.
- **Métricas calculadas:**
  - Tiempo de espera promedio de los clientes.
  - Longitud máxima alcanzada por las colas.
  - Número total de clientes atendidos.

### 4. EditorConPilas (Pilas LIFO y Validador de Símbolos)
- **Ubicación:** `EditorConPilas/`
- **Concepto:** Un editor de texto interactivo enfocado en dos aplicaciones clásicas de las **Pilas (Stack / Deque)**:
  1. **Deshacer / Rehacer (Undo / Redo):** Cada modificación en el texto guarda el estado anterior en una pila de deshacer. Al pulsar `Ctrl+Z` o el botón correspondiente, se extrae el estado anterior y se mueve el estado actual a la pila de rehacer (`Ctrl+Y`). El panel lateral muestra visualmente el contenido de ambas pilas en tiempo real.
  2. **Validador de Símbolos:** Analiza el texto en busca de llaves `{ }`, corchetes `[ ]` y paréntesis `( )` balanceados mediante el uso de una pila de control de apertura y cierre, detectando errores de sintaxis y su posición exacta.

---

## 🛠️ Cómo Compilar y Ejecutar

Cada proyecto es independiente y está estructurado para abrirse directamente en entornos de desarrollo modernos como **IntelliJ IDEA**, Eclipse o NetBeans.

### Ejecución mediante línea de comandos (Terminal):
Para compilar y ejecutar cualquiera de los proyectos (por ejemplo, *EditorConPilas*):

```bash
cd EditorConPilas/src
javac Main.java EditorConPilas.java
java Main
```
*(Lo mismo aplica para `RutaMasCorta`, `DirectorioBST` y `SimuladorBanco`)*.
