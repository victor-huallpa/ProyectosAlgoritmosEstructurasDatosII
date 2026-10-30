package vista;

import modelo.*;
import modelo.enu.EstadoEmergencia;
import modelo.enu.EstadoUnidad;
import modelo.enu.TipoEmergencia;
import modelo.enu.TipoUnidad;
import servicio.*;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.Deque;
import java.util.ArrayDeque;
import java.util.function.Consumer;

/** Panel de registro, clasificación y asignación de emergencias. */
public class PanelEmergencias extends JPanel {
 private final Grafo grafo;
 private final List<UnidadEmergencia> unidades;
 private final Consumer<List<String>> mostrarRuta;
 private final ArbolDecisionEmergencia arbol = new ArbolDecisionEmergencia();
 private final ColaPrioridadEmergencias cola = new ColaPrioridadEmergencias();
 private final DefaultListModel<String> modelo = new DefaultListModel<>();
 private final JList<String> lista = new JList<>(modelo);
 private final DefaultListModel<String> modeloHistorial = new DefaultListModel<>();
 private final JComboBox<Emergencia> emergenciaCierre = new JComboBox<>();
 private final Deque<String> pilaHistorial = new ArrayDeque<>();
 private final JComboBox<TipoEmergencia> tipo = new JComboBox<>(TipoEmergencia.values());
 private final JComboBox<Ubicacion> ubicacion = new JComboBox<>();
 private final JTextField descripcion = new JTextField(24);
 private final JSpinner afectados = new JSpinner(new SpinnerNumberModel(0,0,1000,1));
 private final JCheckBox peligro = new JCheckBox("Peligro activo (fuego, derrumbe, etc.)");
 private final JCheckBox riesgoVital = new JCheckBox("Riesgo vital inmediato");
 private final JLabel resumen = new JLabel("Registra una emergencia para comenzar.");
 private long secuencia=1;
 private int siguienteCodigo=1;

 public PanelEmergencias(Grafo grafo,List<UnidadEmergencia> unidades,Consumer<List<String>> mostrarRuta){
  this.grafo=grafo;this.unidades=unidades;this.mostrarRuta=mostrarRuta;
  setLayout(new BorderLayout(12,12));setBorder(new EmptyBorder(14,14,14,14));
  setBackground(new Color(242,246,250));
  add(crearFormulario(),BorderLayout.NORTH);
  lista.setFont(new Font("SansSerif",Font.PLAIN,13));
  JScrollPane pendientes=new JScrollPane(lista);pendientes.setBorder(BorderFactory.createTitledBorder("Cola de emergencias (mayor prioridad arriba)"));
  JList<String> historialLista=new JList<>(modeloHistorial);
  historialLista.setFont(new Font("Monospaced",Font.PLAIN,12));
  JScrollPane historialScroll=new JScrollPane(historialLista);historialScroll.setBorder(BorderFactory.createTitledBorder("Pila de eventos (último evento arriba)"));
  JSplitPane centro=new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,pendientes,historialScroll);
  centro.setResizeWeight(0.65);add(centro,BorderLayout.CENTER);
  JPanel abajo=new JPanel(new BorderLayout(8,8));abajo.setOpaque(false);
  resumen.setFont(new Font("SansSerif",Font.BOLD,13));abajo.add(resumen,BorderLayout.NORTH);
  JPanel acciones=new JPanel(new FlowLayout(FlowLayout.LEFT));
  JButton asignar=new JButton("Asignar siguiente emergencia");
  JButton reglas=new JButton("Ver reglas del árbol");
  JButton actualizar=new JButton("Actualizar lista");
  JButton finalizar=new JButton("Finalizar emergencia");

  acciones.add(asignar);acciones.add(reglas);acciones.add(actualizar);
  acciones.add(new JLabel("Cerrar:")); acciones.add(emergenciaCierre);
  acciones.add(finalizar);
  finalizar.addActionListener(e->finalizarEmergencia());
  abajo.add(acciones,BorderLayout.SOUTH);add(abajo,BorderLayout.SOUTH);
  asignar.addActionListener(e->asignarSiguiente());
  reglas.addActionListener(e->JOptionPane.showMessageDialog(this,arbol.describirReglas(),
    "Árbol de decisión (reglas académicas)",JOptionPane.INFORMATION_MESSAGE));
  actualizar.addActionListener(e->refrescar());
  cargarUbicaciones();
  cargarEmergenciasDemo();
  refrescar();
 }

 private JPanel crearFormulario(){
  JPanel p=new JPanel(new GridBagLayout());p.setBackground(Color.WHITE);
  p.setBorder(BorderFactory.createTitledBorder("Registrar incidente"));
  GridBagConstraints c=new GridBagConstraints();c.insets=new Insets(5,7,5,7);c.anchor=GridBagConstraints.WEST;
  c.gridx=0;c.gridy=0;p.add(new JLabel("Tipo:"),c);c.gridx=1;p.add(tipo,c);
  c.gridx=2;p.add(new JLabel("Ubicación:"),c);c.gridx=3;ubicacion.setPreferredSize(new Dimension(210,27));p.add(ubicacion,c);
  c.gridx=0;c.gridy=1;p.add(new JLabel("Descripción:"),c);c.gridx=1;c.gridwidth=3;c.fill=GridBagConstraints.HORIZONTAL;p.add(descripcion,c);
  c.gridwidth=1;c.fill=GridBagConstraints.NONE;c.gridx=0;c.gridy=2;p.add(new JLabel("Personas afectadas:"),c);c.gridx=1;p.add(afectados,c);
  c.gridx=2;p.add(peligro,c);c.gridx=3;p.add(riesgoVital,c);
  JButton registrar=new JButton("Clasificar y registrar");
  c.gridx=0;c.gridy=3;c.gridwidth=4;c.anchor=GridBagConstraints.CENTER;p.add(registrar,c);
  registrar.addActionListener(e->registrarEmergencia());
  return p;
 }

 public void actualizarUbicaciones(){ cargarUbicaciones(); }

 private void cargarUbicaciones(){
  Object seleccionado=ubicacion.getSelectedItem();
  ubicacion.removeAllItems();
  for(Ubicacion u:grafo.getUbicaciones())ubicacion.addItem(u);
  if(seleccionado instanceof Ubicacion anterior){
   for(int i=0;i<ubicacion.getItemCount();i++)if(ubicacion.getItemAt(i).getId().equals(anterior.getId()))ubicacion.setSelectedIndex(i);
  }
  ubicacion.setRenderer(new DefaultListCellRenderer(){
   @Override public Component getListCellRendererComponent(JList<?> l,Object v,int i,boolean s,boolean f){
    super.getListCellRendererComponent(l,v,i,s,f);if(v instanceof Ubicacion u)setText(u.getNombre()+" — "+u.getTipo());return this;
   }
  });
 }

 private void cargarEmergenciasDemo(){
  crearDemo(TipoEmergencia.MEDICA,"Persona con posible riesgo vital","U03",1,false,true);
  crearDemo(TipoEmergencia.INCENDIO,"Humo reportado en una vivienda","U05",2,true,false);
  crearDemo(TipoEmergencia.ACCIDENTE,"Choque menor sin peligro activo","U11",0,false,false);
 }
 private void crearDemo(TipoEmergencia t,String d,String lugar,int n,boolean p,boolean r){
  Emergencia e=new Emergencia(String.format("EMG-%03d",siguienteCodigo++),t,d,lugar,n,p,r,secuencia++);
  e.setPrioridad(arbol.clasificar(e));cola.agregar(e);
  registrarEvento("Precargada "+e.getCodigo()+" · prioridad "+e.getPrioridad());
 }

 private void registrarEmergencia(){
  Ubicacion u=(Ubicacion)ubicacion.getSelectedItem();
  if(u==null){error("Agrega o selecciona una ubicación.");return;}
  String texto=descripcion.getText().trim();
  if(texto.isEmpty()){error("Escribe una descripción breve del incidente.");return;}
  try{
   Emergencia e=new Emergencia(String.format("EMG-%03d",siguienteCodigo++),
    (TipoEmergencia)tipo.getSelectedItem(),texto,u.getId(),(Integer)afectados.getValue(),
    peligro.isSelected(),riesgoVital.isSelected(),secuencia++);
   e.setPrioridad(arbol.clasificar(e));cola.agregar(e);
   registrarEvento("Registrada "+e.getCodigo()+" · prioridad "+e.getPrioridad());
   descripcion.setText("");afectados.setValue(0);peligro.setSelected(false);riesgoVital.setSelected(false);
   refrescar();
   JOptionPane.showMessageDialog(this,"Emergencia "+e.getCodigo()+" clasificada como "+e.getPrioridad()+".\nSe agregó a la cola de atención.",
    "Emergencia registrada",JOptionPane.INFORMATION_MESSAGE);
  }catch(IllegalArgumentException ex){error(ex.getMessage());}
 }

 private void asignarSiguiente(){
  Emergencia e=cola.consultarSiguiente();
  if(e==null){error("No hay emergencias pendientes.");return;}
  TipoUnidad requerida=(e.getTipo()==TipoEmergencia.INCENDIO||e.getTipo()==TipoEmergencia.RESCATE)
    ?TipoUnidad.BOMBEROS:TipoUnidad.AMBULANCIA;
  UnidadEmergencia mejor=null;ResultadoRuta mejorRuta=null;
  Dijkstra dijkstra=new Dijkstra();
  for(UnidadEmergencia unidad:unidades){
   if(unidad.getTipo()!=requerida||unidad.getEstado()!= EstadoUnidad.DISPONIBLE)continue;
   Optional<ResultadoRuta> r=dijkstra.calcular(grafo,unidad.getUbicacionId(),e.getUbicacionId());
   if(r.isPresent()&&(mejorRuta==null||r.get().distanciaTotalKm()<mejorRuta.distanciaTotalKm())){
    mejor=unidad;mejorRuta=r.get();
   }
  }
  if(mejor==null){error("No hay una unidad disponible del tipo requerido con una ruta abierta hasta el incidente.");return;}
  cola.marcarAsignada(e);
  e.setEstado(EstadoEmergencia.ASIGNADA);e.setUnidadAsignada(mejor.getCodigo());
  e.setDistanciaRespuestaKm(mejorRuta.distanciaTotalKm());mejor.setEstado(EstadoUnidad.OCUPADA);
  registrarEvento("Despacho "+e.getCodigo()+" → "+mejor.getCodigo()+" ("+String.format(Locale.US,"%.2f km",mejorRuta.distanciaTotalKm())+")");
  mostrarRuta.accept(mejorRuta.ubicaciones());
  refrescar();
  String camino=mejorRuta.ubicaciones().stream().map(id->grafo.obtenerUbicacion(id).getNombre())
   .collect(java.util.stream.Collectors.joining(" → "));
  JOptionPane.showMessageDialog(this,"Emergencia: "+e.getCodigo()+" ("+e.getPrioridad()+")\nUnidad asignada: "+mejor.getCodigo()
   +"\nDistancia estimada: "+String.format(Locale.US,"%.2f km",mejorRuta.distanciaTotalKm())
   +"\nTiempo orientativo (a 30 km/h): "+Math.max(1,(int)Math.ceil(mejorRuta.distanciaTotalKm()/30.0*60))+" min"
   +"\nRuta: "+camino+"\n\nLa unidad queda marcada como ocupada.",
   "Despacho simulado",JOptionPane.INFORMATION_MESSAGE);
 }
 private void finalizarEmergencia(){
  Emergencia e=(Emergencia)emergenciaCierre.getSelectedItem();
  if(e==null){error("No hay emergencias registradas.");return;}
  if(e.getEstado()!=EstadoEmergencia.ASIGNADA){error("Solo se puede finalizar una emergencia que tenga una unidad asignada.");return;}
  int ok=JOptionPane.showConfirmDialog(this,"¿Confirmas que "+e.getCodigo()+" ya fue atendida? La unidad "+e.getUnidadAsignada()+" quedará disponible.","Confirmar cierre",JOptionPane.YES_NO_OPTION);
  if(ok!=JOptionPane.YES_OPTION)return;
  e.setEstado(EstadoEmergencia.ATENDIDA);
  for(UnidadEmergencia u:unidades)if(u.getCodigo().equals(e.getUnidadAsignada()))u.setEstado(EstadoUnidad.DISPONIBLE);
  registrarEvento("Finalizada "+e.getCodigo()+"; unidad liberada "+e.getUnidadAsignada());
  mostrarRuta.accept(List.of());
  refrescar();
  JOptionPane.showMessageDialog(this,"Emergencia finalizada y unidad liberada.");
 }
 private void exportarReporte(){
  JFileChooser chooser=new JFileChooser(); chooser.setSelectedFile(new java.io.File("reporte-sire.csv"));
  if(chooser.showSaveDialog(this)!=JFileChooser.APPROVE_OPTION)return;
  java.io.File archivo=chooser.getSelectedFile();
  try(java.io.PrintWriter out=new java.io.PrintWriter(new java.io.OutputStreamWriter(new java.io.FileOutputStream(archivo),java.nio.charset.StandardCharsets.UTF_8))){
   out.println("codigo;tipo;prioridad;estado;ubicacion;descripcion;personas_afectadas;unidad;distancia_km;hora_registro");
   for(Emergencia e:cola.todas())out.println(csv(e.getCodigo())+";"+csv(e.getTipo().toString())+";"+csv(String.valueOf(e.getPrioridad()))+";"+csv(e.getEstado().toString())+";"+csv(e.getUbicacionId())+";"+csv(e.getDescripcion())+";"+e.getPersonasAfectadas()+";"+csv(e.getUnidadAsignada())+";"+(e.getDistanciaRespuestaKm()<0?"":String.format(Locale.US,"%.2f",e.getDistanciaRespuestaKm()))+";"+csv(e.getHora()));
   registrarEvento("Reporte CSV exportado: "+archivo.getName());
   JOptionPane.showMessageDialog(this,"Reporte exportado correctamente:\n"+archivo.getAbsolutePath());
  }catch(java.io.IOException ex){error("No se pudo exportar el reporte: "+ex.getMessage());}
 }
 private String csv(String valor){return "\""+(valor==null?"":valor.replace("\"","\"\""))+"\"";}
 private void registrarEvento(String evento){
  String marca=java.time.LocalDateTime.now().format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
  pilaHistorial.push(marca.substring(11)+" · "+evento);
  while(pilaHistorial.size()>100)pilaHistorial.removeLast();
  modeloHistorial.clear();for(String item:pilaHistorial)modeloHistorial.addElement(item);
  try{
   java.nio.file.Path archivo=java.nio.file.Path.of("datos","sire-eventos.csv");
   java.nio.file.Files.createDirectories(archivo.getParent());
   boolean nuevo=!java.nio.file.Files.exists(archivo);
   try(java.io.BufferedWriter out=java.nio.file.Files.newBufferedWriter(archivo,java.nio.charset.StandardCharsets.UTF_8,java.nio.file.StandardOpenOption.CREATE,java.nio.file.StandardOpenOption.APPEND)){
    if(nuevo)out.write("fecha_hora;evento"+System.lineSeparator());
    out.write(csv(marca)+";"+csv(evento.replace("\n"," "))+System.lineSeparator());
   }
  }catch(java.io.IOException ex){System.err.println("No se pudo guardar el registro de eventos: "+ex.getMessage());}
 }
 private void refrescar(){
  Object seleccionado=emergenciaCierre.getSelectedItem();
  emergenciaCierre.removeAllItems(); for(Emergencia e:cola.todas())emergenciaCierre.addItem(e);
  if(seleccionado instanceof Emergencia anterior)emergenciaCierre.setSelectedItem(anterior);
  emergenciaCierre.setRenderer(new DefaultListCellRenderer(){
   @Override public Component getListCellRendererComponent(JList<?> l,Object v,int i,boolean s,boolean f){
    super.getListCellRendererComponent(l,v,i,s,f);
    if(v instanceof Emergencia e)setText(e.getCodigo()+" · "+e.getEstado()+" · "+e.getUnidadAsignada());
    return this;
   }
  });
  modelo.clear();
  for(Emergencia e:cola.obtenerOrdenadas()){
   Ubicacion u=grafo.obtenerUbicacion(e.getUbicacionId());
   modelo.addElement("["+e.getPrioridad()+"] "+e.getCodigo()+" · "+e.getTipo()+" · "+(u==null?e.getUbicacionId():u.getNombre())+" · "+e.getDescripcion()+" · Registrada "+e.getHora());
  }
  for(Emergencia e:cola.todas())if(e.getEstado()!=EstadoEmergencia.PENDIENTE)
   modelo.addElement("["+e.getPrioridad()+"] "+e.getCodigo()+" · "+e.getEstado()+" · Unidad "+e.getUnidadAsignada()+" · "+(e.getDistanciaRespuestaKm()<0?"sin ruta":String.format(Locale.US,"%.2f km",e.getDistanciaRespuestaKm())));
  long atendidas=cola.todas().stream().filter(e->e.getEstado()==EstadoEmergencia.ATENDIDA).count();
  long asignadas=cola.todas().stream().filter(e->e.getEstado()==EstadoEmergencia.ASIGNADA).count();
  resumen.setText("Pendientes: "+cola.size()+" | Asignadas: "+asignadas+" | Atendidas: "+atendidas+" | Unidades disponibles: "+unidades.stream().filter(u->u.getEstado()==EstadoUnidad.DISPONIBLE).count()+" | Total: "+cola.todas().size());
 }
 private void error(String m){JOptionPane.showMessageDialog(this,m,"No se pudo completar",JOptionPane.WARNING_MESSAGE);}
}
