package modelo;
import modelo.enu.EstadoEmergencia;
import modelo.enu.NivelPrioridad;
import modelo.enu.TipoEmergencia;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
public class Emergencia {
 private final String codigo, descripcion, ubicacionId;
 private final TipoEmergencia tipo;
 private final int personasAfectadas;
 private final boolean peligroActivo, riesgoVital;
 private final long secuencia;
 private final LocalDateTime registrada=LocalDateTime.now();
 private NivelPrioridad prioridad;
 private EstadoEmergencia estado=EstadoEmergencia.PENDIENTE;
 private String unidadAsignada="Sin asignar";
 private double distanciaRespuestaKm=-1;
 public Emergencia(String codigo,TipoEmergencia tipo,String descripcion,String ubicacionId,int personasAfectadas,boolean peligroActivo,boolean riesgoVital,long secuencia){
  if(codigo==null||codigo.isBlank())throw new IllegalArgumentException("El código es obligatorio.");
  if(descripcion==null||descripcion.isBlank())throw new IllegalArgumentException("Describe brevemente la emergencia.");
  if(ubicacionId==null||ubicacionId.isBlank())throw new IllegalArgumentException("La ubicación es obligatoria.");
  if(personasAfectadas<0)throw new IllegalArgumentException("Las personas afectadas no pueden ser negativas.");
  this.codigo=codigo;this.tipo=tipo;this.descripcion=descripcion.trim();this.ubicacionId=ubicacionId;
  this.personasAfectadas=personasAfectadas;this.peligroActivo=peligroActivo;this.riesgoVital=riesgoVital;this.secuencia=secuencia;
 }
 public String getCodigo(){return codigo;} public TipoEmergencia getTipo(){return tipo;}
 public String getDescripcion(){return descripcion;} public String getUbicacionId(){return ubicacionId;}
 public int getPersonasAfectadas(){return personasAfectadas;} public boolean isPeligroActivo(){return peligroActivo;}
 public boolean isRiesgoVital(){return riesgoVital;} public long getSecuencia(){return secuencia;}
 public LocalDateTime getRegistrada(){return registrada;} public String getHora(){return registrada.format(DateTimeFormatter.ofPattern("HH:mm:ss"));}
 public NivelPrioridad getPrioridad(){return prioridad;} public void setPrioridad(NivelPrioridad p){prioridad=p;}
 public EstadoEmergencia getEstado(){return estado;} public void setEstado(EstadoEmergencia e){estado=e;}
 public String getUnidadAsignada(){return unidadAsignada;} public void setUnidadAsignada(String u){unidadAsignada=u;}
 public double getDistanciaRespuestaKm(){return distanciaRespuestaKm;} public void setDistanciaRespuestaKm(double d){distanciaRespuestaKm=d;}
 @Override public String toString(){return codigo+" | "+prioridad+" | "+tipo+" | "+descripcion+" | "+estado;}
}