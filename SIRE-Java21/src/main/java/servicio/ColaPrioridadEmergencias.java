package servicio;
import modelo.Emergencia;
import java.util.*;
/** Mayor prioridad primero; si empatan, se atiende primero la registrada antes. */
public class ColaPrioridadEmergencias {
 private final PriorityQueue<Emergencia> cola=new PriorityQueue<>(
  Comparator.<Emergencia>comparingInt(e->e.getPrioridad().getPeso()).reversed().thenComparingLong(Emergencia::getSecuencia));
 private final Map<String,Emergencia> registradas=new LinkedHashMap<>();
 public void agregar(Emergencia e){if(registradas.containsKey(e.getCodigo()))throw new IllegalArgumentException("Código de emergencia duplicado.");registradas.put(e.getCodigo(),e);cola.offer(e);}
 public Emergencia consultarSiguiente(){return cola.peek();}
 public Emergencia extraerSiguiente(){return cola.poll();}
 public boolean estaVacia(){return cola.isEmpty();}
 public int size(){return cola.size();}
 public List<Emergencia> obtenerOrdenadas(){List<Emergencia> l=new ArrayList<>(cola);l.sort(Comparator.<Emergencia>comparingInt(e->e.getPrioridad().getPeso()).reversed().thenComparingLong(Emergencia::getSecuencia));return l;}
 public Collection<Emergencia> todas(){return Collections.unmodifiableCollection(registradas.values());}
 public void marcarAsignada(Emergencia e){cola.remove(e);}
}