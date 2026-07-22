package mx.uam.ayd.proyecto.negocio;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import mx.uam.ayd.proyecto.datos.RepositorioCliente;
import mx.uam.ayd.proyecto.datos.RepositorioCotizacion;
import mx.uam.ayd.proyecto.datos.RepositorioDetalleCotizacion;
import mx.uam.ayd.proyecto.datos.RepositorioEvento;
import mx.uam.ayd.proyecto.negocio.modelo.Cliente;
import mx.uam.ayd.proyecto.negocio.modelo.Cotizacion;
import mx.uam.ayd.proyecto.negocio.modelo.DetalleCotizacion;
import mx.uam.ayd.proyecto.negocio.modelo.Evento;
import mx.uam.ayd.proyecto.negocio.modelo.Evento.EstadoEvento;
import mx.uam.ayd.proyecto.negocio.modelo.Evento.TipoEvento;

@Service
/**
 * Servicio relacionado con los eventos
 * @author JLCB
*/
public class ServicioEvento {

    private final RepositorioEvento repositorioEvento;
    private final RepositorioDetalleCotizacion repositorioDetalleCotizacion;
    private final RepositorioCliente repositorioCliente;
    private final RepositorioCotizacion repositorioCotizacion;

    @Autowired
    public ServicioEvento(RepositorioEvento repositorioEvento, RepositorioCotizacion repositorioCotizacion, RepositorioDetalleCotizacion repositorioDetalleCotizacion, RepositorioCliente repositorioCliente){
        this.repositorioEvento = repositorioEvento;
        this.repositorioDetalleCotizacion = repositorioDetalleCotizacion;
        this.repositorioCliente = repositorioCliente;
        this.repositorioCotizacion = repositorioCotizacion;
    }

    /**
     * Es una función que busca los eventos habidos en el repositorio y los regresa en una lista
     * @author JLCB
     * @param fecha Es la fecha de la que se saca el mes para encontrar a los eventos
     * @return Lista con los eventos que coinciden en el mes
     * @throws IllegalArgumentException
     */
    public List<Evento> recuperaEventosPorMes(LocalDate fecha) throws IllegalArgumentException{
        if(fecha==null) throw new IllegalArgumentException("La fecha no es válida.");

        Month mes = fecha.getMonth();
        return repositorioEvento.findByMesOrderByFecha(mes);
    }

    public List<Evento> recupera(){
        return repositorioEvento.findByOrderByFechaAsc();
    }
    
    /**
     * Regla de negocio
     * Una función que calcula el primer día a partir del cual las fechas están disponibles
     * @author JLCB
     * @return día a partir del cual se pueden reservar fechas
     */
    public LocalDate obtenerDiaLimite(){
        LocalDate hoy = LocalDate.now();
        return hoy.plusDays(16);
    }

    /**
     * Toma el Evento que le es dado y regresa el valor del atributo estadoEvento
     * @param evento Es el evento del que se coonseguirá el atributo buscado
     * @return Es el valor del atributo estadoEvento
     */
    public EstadoEvento determinaEstado(Evento evento) throws IllegalArgumentException {
        if(evento == null) throw new IllegalArgumentException("El evento no puede ser nulo.");
        return evento.getEstadoEvento();
    }

    /**
     * La función recibe un evento y por medio del mismo, encuentra la cotización asociada y encuentra los detalles asociados a esa cotización
     * @param evento Es el evento del que se quieren obtener Cotizacion y DetalleCotizacion
     * @return Regresa un arreglo que contiene al objeto cotizacion y la lista con los DetallesCotizacion
     */
    public Object[] obtenerCotizacionDetalles(Evento evento) throws IllegalArgumentException {
        if(evento == null) throw new IllegalArgumentException("El evento no puede ser nulo.");

        Cotizacion cotizacion = repositorioCotizacion.findByEvento(evento);
        List<DetalleCotizacion> detalles = repositorioDetalleCotizacion.findByCotizacion(cotizacion);

        return new Object[] {cotizacion, detalles};
    }

    /**
     * Función que realiza cambios necesarios al objeto Evento y lo manda a actualizar en el repositorio. Recibe el evento a modificar junto a todos los nuevos valores de cada atributo, en caso de eliminar algún valor, es null
     * @author JLCB
     * @param evento Es el evento que se modificará
     * @param fecha Nueva fecha del evento que se modificará (obligatorio).
     * @param tipoEvento Nuevo tipo del evento (obligatorio).
     * @param hora Nueva hora del evento (obligatorio)
     * @param lugar Nuevo lugar/sede del evento (opcional).
     * @param direccion Nueva direción del evento (obligatorio).
     * @param referencias Nuevas referencias del evento (opcional).
     * @param imagen Nueva imágen de referencia del lugar (opcional).
     * @param notas Nuevas notas sobre el evento (opcional).
     * @param estadoEvento Estado en el que se encuentra el evento (obligatorio).
     * @return boolean. Si se logró hacer la actualización, de lo contrario, false.
     * @throws Exception
     */
    public boolean modificaEvento(Evento evento, LocalDate fecha, TipoEvento tipoEvento, LocalTime hora, String lugar, String direccion, String referencias, String imagen, String notas, EstadoEvento estadoEvento) {
        if(evento == null || fecha == null || tipoEvento == null || hora == null || direccion == null || estadoEvento == null) throw new IllegalArgumentException("Algunos datos son obligatorios");
        modificaObjEvento(evento, fecha, tipoEvento, hora, lugar, direccion, referencias, imagen, notas, estadoEvento);
        try {
            repositorioEvento.save(evento);
            return true;
        } catch (Exception e) {
            System.err.println("No se pudo guardar. "+e.getMessage());
            return false;
        }
    }
    
    /** Función encargada de específicamente actualizar el objeto Evento con todos los nuevos datos haciendo set por set
     * 
     * @param evento Es el evento que se modificará
     * @param fecha Nueva fecha del evento que se modificará (obligatorio).
     * @param tipoEvento Nuevo tipo del evento (obligatorio).
     * @param hora Nueva hora del evento (obligatorio)
     * @param lugar Nuevo lugar/sede del evento (opcional).
     * @param direccion Nueva direción del evento (obligatorio).
     * @param referencias Nuevas referencias del evento (opcional).
     * @param imagen Nueva imágen de referencia del lugar (opcional).
     * @param notas Nuevas notas sobre el evento (opcional).
     * @param estadoEvento Estado en el que se encuentra el evento (obligatorio).
     * @return Evento. Si se logró hacer la actualización, de lo contrario, false.
     */
    private Evento modificaObjEvento(Evento evento, LocalDate fecha, TipoEvento tipoEvento, LocalTime hora, String lugar, String direccion, String referencias, String imagen, String notas, EstadoEvento estadoEvento) {
        evento.setFecha(fecha);
        evento.setTipoEvento(tipoEvento);
        evento.setHora(hora);
        evento.setLugar(lugar);
        evento.setDireccion(direccion);
        evento.setReferencias(referencias);
        evento.setVisualRecinto(imagen);
        evento.setDetalles(notas);
        evento.setEstadoEvento(estadoEvento); 
        return evento;
    }

    /**
     * Elimina en el repositorio el evento que le es ingresado
     * @author JLCB
     * @param evento Es el evento a eliminar
     * @return Regresa verdadero en caso de que se haya logrado eliminar el evento, regresa falso en caso de que no se haya logrado eliminar
     */
    public boolean eliminaEvento(Evento evento){
        if(evento == null){
            System.err.println("El evento no existe");
            return false;
        }
        try {
            repositorioEvento.delete(evento);
            return true;
        } catch (Exception e) {
            System.err.println("No se encontró al evento. "+e.getMessage());
            return false;
        }
    }

    /**
     * Recibe todos los datos necesarios para guardar un evento y los guarda en un objeto al que le asocia un cliente y una cotizacion vacía
     * @author JLCB
     * @param nombre Es el nombre del cliente que solicita el evento (obligatorio)
     * @param num Número de teléfono del cliente que solicita el evento (obligatorio)
     * @param fecha Fecha en la que se guardaŕa el evento (obligatorio)
     * @param tipoEvento Tipo de evento que se solicita (obligatorio)
     * @param hora Hora del evento (obligatorio)
     * @param lugar Lugar/Sede del evento (opcional)
     * @param direccion Dirección del evento (obligatorio)
     * @param referencias Referencias del lugar del evento (opcional)
     * @param imagen Visualización del lugar donde irá la decoración (opcional)
     * @param notas Notas extra sobre el evento (opcional)
     * @return Regresa true si se logró guardar el evento, false si ocurrió algún error
     */
    public boolean guardaEvento(String nombre, String num, LocalDate fecha, TipoEvento tipoEvento, LocalTime hora, String lugar, String direccion, String referencias, String imagen, String notas){
        // Verifica si el cliente existe, si no, lo registra
        Cliente cliente = repositorioCliente.findByNombreAndNumTelefono(nombre, num);
        if(cliente == null){
            cliente = new Cliente(nombre, num);
            repositorioCliente.save(cliente);
        }
        
        // Verifica que no exista en el repositorio otro evento con la misma fecha
        if(!fechaDisponible(fecha)) return false;

        // Crea a la cotización
        Cotizacion cotizacion = new Cotizacion();
        cotizacion = repositorioCotizacion.save(cotizacion);
        
        // Crea al evento
        Evento evento = new Evento(tipoEvento, fecha, hora, lugar, referencias, direccion, referencias, imagen, cotizacion, cliente);
        evento = repositorioEvento.save(evento);
        
        return true;
    }

    public Cliente obtenerCliente(Evento evento){
        return repositorioCliente.findByEventosContains(evento);
    }

    public List<Object> diaPresionado(Evento evento){
        List<Object> datos = new ArrayList<>();
        String estadoEvento = evento.getEstadoEvento().toString();
        datos.add(estadoEvento.toString());
        datos.add(evento.toString());
        datos.add(evento.getHora().format(DateTimeFormatter.ofPattern("HH:mm")));   
        if(evento.getLugar() != null) datos.add(evento.getLugar());
        else datos.add(evento.getDireccion());
        datos.add(evento.getCliente().toString());
        datos.add(evento.getTotalPagado());
        
        if(!estadoEvento.equals("FINALIZADO"))
            datos.add(evento.getEstadoPago().toString());

        return datos;
    }

    /**
     * Dada una fecha, revisa si en esa fecha existe algún evento o si es una fecha pasada
     * @param fecha Es la fecha que se da a evaluar
     * @return Regresa true si la fecha está disponible, de lo contrario, false
     */
    public boolean fechaDisponible(LocalDate fecha){
        if(fecha == null){
            System.err.println("La fecha no existe");
            return false;
        }
        LocalDate fechaLimite = obtenerDiaLimite();
        if(repositorioEvento.findByFecha(fecha) != null && fecha.isBefore(fechaLimite)) return false;
        return true;
    }

    /**
     * Busca en LocalDate cuál es el valor del día actual
     * @return Regresa el día actual en un LocalDate
     */
    public LocalDate obtenerDiaActual(){
        return LocalDate.now();
    }
    
    public LocalDate aumentarMes(LocalDate fecha){
        fecha = fecha.withDayOfMonth(1);
        fecha = fecha.plusMonths(1);
        return fecha;
    }
    public LocalDate disminuirMes(LocalDate fecha){
        fecha = fecha.withDayOfMonth(1);
        fecha = fecha.plusMonths(-1);
        return fecha;
    }
}