# SIRE — Fase 4: operación y seguimiento

## Mejoras incluidas

1. **Bitácora CSV persistente:** se registra fecha, hora y descripción de cada evento en `datos/sire-eventos.csv`. Si no se puede escribir, se informa el problema en la salida de la aplicación y el sistema sigue funcionando.
2. **Finalización de emergencias:** el operador selecciona una emergencia asignada, confirma el cierre y el sistema cambia su estado a `ATENDIDA`; la unidad asignada vuelve a `DISPONIBLE`.
3. **Estimación orientativa de llegada:** el sistema calcula minutos como distancia de la ruta / 30 km/h. Es un supuesto demostrativo, no un pronóstico de tránsito.
4. **Reporte de emergencias:** exportación CSV de código, tipo, prioridad, estado, ubicación, descripción, personas afectadas, unidad, distancia y hora de registro.
5. **Panel de indicadores:** recuento de emergencias pendientes, asignadas y atendidas, unidades disponibles y total registrado.

## Pruebas recomendadas

- Registrar una emergencia con descripción vacía y verificar que se rechaza.
- Asignar una emergencia y comprobar que la unidad cambia a ocupada.
- Finalizar esa emergencia y verificar que pasa a atendida y la unidad queda disponible.
- Exportar el reporte CSV y abrirlo en una hoja de cálculo.
- Reiniciar la aplicación y comprobar que `datos/sire-eventos.csv` conserva los eventos anteriores.

## Alcance y limitaciones

Los datos del escenario y el estado actual de las emergencias no se restauran automáticamente desde disco. La bitácora es persistente, pero no equivale a persistencia completa de la aplicación. La velocidad de 30 km/h es fija y no incluye tráfico, clima ni condiciones reales. SIRE es un prototipo académico y no sustituye protocolos oficiales de emergencia.
