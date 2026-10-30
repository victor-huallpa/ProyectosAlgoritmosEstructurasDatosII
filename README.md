# Estructuras de Datos y Algoritmos II (EDA2)

Este repositorio contiene aplicaciones de escritorio interactivas desarrolladas en **Java (Swing)** que implementan y demuestran el uso práctico de estructuras de datos fundamentales y algoritmos avanzados de la asignatura de **Estructuras de Datos y Algoritmos II**.

---

## 📋 Resumen de Programas y Módulos

| Proyecto | Estructura de Datos / Tecnología | Algoritmo / Lógica Principal | Descripción del Problema Real |
| :--- | :--- | :--- | :--- |
| **1. SimuladorBanco** | Colas (`Queue` / FIFO) | Simulación por eventos discretos (Colas Normal y Preferencial) | Simulación de atención al cliente en ventanillas bancarias con cálculo de tiempos de espera y estadísticas. |
| **2. SIRE (Sistema Inteligente de Respuesta)** | Grafos, Árbol de Decisión, Cola de Prioridad, Pilas | Dijkstra, Clasificación por Árbol, Gestión LIFO (Java 21 / Maven) | Plataforma integrada avanzada para la gestión y respuesta ante emergencias urbanas con bitácora CSV y reportes operativos. |

---

## 🚀 Detalle de los Proyectos

### 1. SimuladorBanco (Colas FIFO)
- **Ubicación:** `SimuladorBanco/`
- **Concepto:** Modela un sistema de atención en ventanillas bancarias con múltiples puestos activos y gestión de filas de espera independientes.
- **Estructuras:** Utiliza dos **Colas (Queue)** separadas: cola **preferencial** y cola **normal**. Las ventanillas libres atienden prioritariamente a los clientes preferenciales para garantizar el nivel de servicio.
- **Simulación:** Cada ciclo de reloj avanza de forma discreta (1 minuto simulado), generando llegadas aleatorias y calculando el tiempo de espera promedio, la longitud máxima de la cola y el total de clientes atendidos.

### 2. SIRE (Sistema Inteligente de Respuesta ante Emergencias)
- **Ubicación:** `SIRE-Java21/`
- **Tecnología:** Java 21, Swing, Maven.
- **Concepto:** Plataforma académica integral que unifica las principales estructuras de datos de EDA2 en un escenario de respuesta a emergencias:
  - **Red Vial (Grafos & Dijkstra):** Modelo ponderado no dirigido para calcular la ruta más corta entre ubicaciones (hospitales, bomberos, comisarías, intersecciones), con soporte para bloqueo y habilitación de tramos viales.
  - **Clasificación (Árbol de Decisión):** Evaluación de factores de riesgo, peligro activo y afectación para priorizar incidentes (Crítica, Alta, Media, Baja).
  - **Despacho (Cola de Prioridad):** Ordenamiento de emergencias pendientes por prioridad y llegada (FIFO).
  - **Bitácora y Auditoría (Pilas y CSV):** Historial visual LIFO de eventos recientes en panel y persistencia automática en `datos/sire-eventos.csv`.
  - **Mejoras Operativas (Fase 4):** Cierre de emergencias, liberación de unidades, estimación orientativa de tiempo de llegada (30 km/h) y exportación de reportes operativos en CSV.

---

## 🛠️ Cómo Compilar y Ejecutar

### SimuladorBanco
Para compilar y ejecutar el simulador de banco:
```bash
cd SimuladorBanco/src
javac Main.java SimuladorBanco.java
java Main
```

### SIRE (Proyecto Integrado Maven)
Para compilar y ejecutar **SIRE**:
```bash
cd SIRE-Java21
mvn clean compile
mvn exec:java
```
O bien, abrir el proyecto en **IntelliJ IDEA** y ejecutar `pe.unaj.sire.Main`.
