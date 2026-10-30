package servicio;
import modelo.*;
import modelo.enu.NivelPrioridad;
import modelo.enu.TipoEmergencia;

import java.util.function.Predicate;
/** Árbol binario de decisión demostrativo. No sustituye protocolos oficiales de emergencia. */
public class ArbolDecisionEmergencia {
 private static final class Nodo {
  final Predicate<Emergencia> condicion; final Nodo si,no; final NivelPrioridad resultado;
  Nodo(NivelPrioridad r){condicion=null;si=null;no=null;resultado=r;}
  Nodo(Predicate<Emergencia> c,Nodo s,Nodo n){condicion=c;si=s;no=n;resultado=null;}
  boolean esHoja(){return resultado!=null;}
 }
 private final Nodo raiz=new Nodo(
  e->e.isRiesgoVital()||(e.getTipo()== TipoEmergencia.INCENDIO&&e.isPeligroActivo()&&e.getPersonasAfectadas()>0),
  new Nodo(NivelPrioridad.CRITICA),
  new Nodo(e->e.isPeligroActivo()||e.getPersonasAfectadas()>=3||e.getTipo()==TipoEmergencia.RESCATE,
   new Nodo(NivelPrioridad.ALTA),
   new Nodo(e->e.getPersonasAfectadas()>0||e.getTipo()==TipoEmergencia.ACCIDENTE,
    new Nodo(NivelPrioridad.MEDIA),new Nodo(NivelPrioridad.BAJA))));
 public NivelPrioridad clasificar(Emergencia e){Nodo n=raiz;while(!n.esHoja())n=n.condicion.test(e)?n.si:n.no;return n.resultado;}
 public String describirReglas(){return "1. Riesgo vital, o incendio activo con personas afectadas → CRÍTICA.\n2. Si no: peligro activo, 3 o más afectados o rescate → ALTA.\n3. Si no: personas afectadas o accidente → MEDIA.\n4. En los demás casos → BAJA.";}
}