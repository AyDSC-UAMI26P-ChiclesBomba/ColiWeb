package mx.uam.ayd.proyecto.negocio;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import mx.uam.ayd.proyecto.datos.RepositorioCliente;
import mx.uam.ayd.proyecto.datos.RepositorioCotizacion;
import mx.uam.ayd.proyecto.datos.RepositorioDetalleCotizacion;
import mx.uam.ayd.proyecto.datos.RepositorioEvento;
import mx.uam.ayd.proyecto.negocio.modelo.Cliente;
import mx.uam.ayd.proyecto.negocio.modelo.Cotizacion;
import mx.uam.ayd.proyecto.negocio.modelo.DetalleCotizacion;
import mx.uam.ayd.proyecto.negocio.modelo.Evento;
import mx.uam.ayd.proyecto.negocio.modelo.Evento.EstadoEvento;
import mx.uam.ayd.proyecto.negocio.modelo.Evento.EstadoPago;
import mx.uam.ayd.proyecto.negocio.modelo.Evento.TipoEvento;

@ExtendWith(MockitoExtension.class)
class ServicioEventoTest {

    // Es un sustituto de lo que realmente es el repositorio
    @Mock
    private RepositorioEvento repositorioEvento;
    @Mock
    private RepositorioCotizacion repositorioCotizacion;
    @Mock
    private RepositorioDetalleCotizacion repositorioDetalleCotizacion;
    @Mock
    private RepositorioCliente repositorioCliente;

    @InjectMocks
    private ServicioEvento servicioEvento;

    // -------------------- recuperaEventosPorMesTest --------------------
    @Test
    void recuperaEventosFechaNull(){
        // Given
        LocalDate fecha = null;
        // When, Then
        assertThrowsExactly(IllegalArgumentException.class, () -> {
            servicioEvento.recuperaEventosPorMes(fecha);
        });
    }
    @Test
    void recuperaEventosFechaConEventosExistentes(){
        // Given
        Evento evento1 = new Evento();
        Evento evento2 = new Evento();
        LocalDate fecha1 = LocalDate.of(2026, 12, 1);
        LocalDate fecha2 = LocalDate.of(2026, 12, 31);
        evento1.setFecha(fecha1);
        evento2.setFecha(fecha2);
        List<Evento> lista = new ArrayList<>();
        lista.add(evento1);
        lista.add(evento2);
        when(repositorioEvento.findByMesOrderByFecha(fecha1.getMonth())).thenReturn(lista);

        // When
        List<Evento> eventos = servicioEvento.recuperaEventosPorMes(fecha2);

        // Then
        assertNotEquals(0, eventos.size());
    }
    @Test
    void recuperaEventosFechaConEventosNoExistentes(){
        // Given
        LocalDate fecha = LocalDate.of(2026, 12, 1);
        List<Evento> lista = new ArrayList<>();
        when(repositorioEvento.findByMesOrderByFecha(fecha.getMonth())).thenReturn(lista);

        // When
        List<Evento> eventos = servicioEvento.recuperaEventosPorMes(fecha);

        // Then
        assertEquals(0, eventos.size());
    }


    // -------------------- recuperaTest --------------------
    @Test
    void recuperaConRepositorioVacío(){
        // Given
        List<Evento> lista = new ArrayList<>();
        when(repositorioEvento.findByOrderByFechaAsc()).thenReturn(lista);

        // When
        List<Evento> eventos = servicioEvento.recupera();

        // Then
        assertEquals(0, eventos.size());
    }
    @Test
    void recuperaConRepositorioNoVacío(){
        // Given
        List<Evento> lista = new ArrayList<>();
        Evento evento1 = new Evento();
        lista.add(evento1);
        when(repositorioEvento.findByOrderByFechaAsc()).thenReturn(lista);

        // When
        List<Evento> eventos = servicioEvento.recupera();

        // Then
        assertNotEquals(0, eventos.size());
    }


    // -------------------- obtenerDiaLimiteTest --------------------
    @Test
    void obtenerDiaLimiteTest(){
        // Given
        LocalDate hoy = LocalDate.now();
        hoy = hoy.plusDays(16);
        
        // When
        LocalDate resultado = servicioEvento.obtenerDiaLimite();
        
        // Then
        assertEquals(hoy, resultado);
    }


    // -------------------- obtenerCotizacionDetallesTest --------------------
    @Test
    void obtenerCotizacionDeltallesEventoConCotizacionYDetalles(){
        // Given
        Evento evento = new Evento();
        Cotizacion cotizacion = new Cotizacion();
        cotizacion.setIdCotizacion(1L);
        DetalleCotizacion detalle = new DetalleCotizacion();
        try{
            evento.setCotizacion(cotizacion);
        } catch(Exception e){
            System.out.println("No se pudo asociar la cotización al evento");
        }
        detalle.setCotizacion(cotizacion);
        List<DetalleCotizacion> detalles = new ArrayList<>();
        detalles.add(detalle);
        when(repositorioCotizacion.findByEvento(evento)).thenReturn(cotizacion);
        when(repositorioDetalleCotizacion.findByCotizacion(cotizacion)).thenReturn(detalles);

        // When
        Object[] datos = servicioEvento.obtenerCotizacionDetalles(evento);

        // Then
        assertEquals(2, datos.length);
        assertEquals(cotizacion, datos[0]);
        assertEquals(detalles, datos[1]);
    }
    @Test
    void obtenerCotizacionDeltallesEventoNulo(){
        // Given
        Evento evento = null;
    
        // When, Then
        assertThrowsExactly(IllegalArgumentException.class, () ->{
            servicioEvento.obtenerCotizacionDetalles(evento);
        });
    }
    @Test
    void obtenerCotizacionDeltallesEventoSinCotizacionYODetalles(){
        // Given
        Evento evento = new Evento();
        when(repositorioCotizacion.findByEvento(evento)).thenReturn(null);

        // When, Then
        assertThrowsExactly(IllegalArgumentException.class, () -> {
            servicioEvento.obtenerCotizacionDetalles(evento);
        });
    }


    // -------------------- modificaEventoTest --------------------
    @Test
    void modificaEventoConTodosLosDatos(){
        // Given
        Evento evento = new Evento();

        LocalDate fecha = LocalDate.of(2026,1,1);
        TipoEvento tipo = TipoEvento.BODA;
        LocalTime hora = LocalTime.of(00, 00);
        String lugar = "Lugar";
        String direccion = "Dirección";
        String referencias = "Referencias";
        String imagen = "Imagen";
        String notas = "Notas";
        EstadoEvento estado = EstadoEvento.BORRADOR;

        when(repositorioEvento.save(evento)).thenReturn(evento);

        // When
        boolean exito = servicioEvento.modificaEvento(evento, fecha, tipo, hora, lugar, direccion, referencias, imagen, notas, estado);

        // Then
        assertTrue(exito);

        // PRueba para el método privado que se utiliza ModificaObjEvento
        assertEquals(fecha, evento.getFecha());
        assertEquals(tipo, evento.getTipoEvento());
        assertEquals(hora, evento.getHora());
        assertEquals(direccion, evento.getDireccion());
        assertEquals(estado, evento.getEstadoEvento());
    }
    @Test
    void modificaEventoConDatosMinimo(){
        Evento evento = new Evento();

        LocalDate fecha = LocalDate.of(2026,1,1);
        TipoEvento tipo = TipoEvento.BODA;
        LocalTime hora = LocalTime.of(00, 00);
        String direccion = "Dirección";
        EstadoEvento estado = EstadoEvento.BORRADOR;

        when(repositorioEvento.save(evento)).thenReturn(evento);

        // When
        boolean exito = servicioEvento.modificaEvento(evento, fecha, tipo, hora, null, direccion, null, null, null, estado);

        // Then
        assertTrue(exito);
    }
    @Test
    void modificaEventoSinAlgunDatoObligatorio(){
        // Given
        Evento evento = new Evento();
        // When, Then
        assertThrowsExactly(IllegalArgumentException.class, () -> {
            servicioEvento.modificaEvento(evento, null, null, null, null, null, null, null, null, null);
        });
    }
    @Test
    void modificaEventoConEventoNulo(){
        // Given
        Evento evento = null;
        // When, Then
        assertThrowsExactly(IllegalArgumentException.class, () -> {
            servicioEvento.modificaEvento(evento, null, null, null, null, null, null, null, null, null);
        });
    }


    // -------------------- eliminaEventoTest --------------------
    @Test
    void eliminaEventoConEventoBien(){
        // Given
        Cotizacion cotizacion = new Cotizacion();
        cotizacion.setIdCotizacion(1L);
        Evento evento = new Evento();
        try {
            evento.setCotizacion(cotizacion);
        } catch (Exception e) {}

        // When
        boolean exito = servicioEvento.eliminaEvento(evento);

        // Then
        assertTrue(exito);
    }
    @Test
    void eliminaEventoConEventoNulo(){
        // Given
        Evento evento = null;

        // When
        boolean exito = servicioEvento.eliminaEvento(evento);
        
        // Then
        assertFalse(exito);
    }
    @Test
    void eliminaEventoConEventoSinCotizacion(){
        // Given
        Evento evento = new Evento();

        // When
        boolean exito = servicioEvento.eliminaEvento(evento);
        
        // Then
        assertFalse(exito);
    }


    // -------------------- guardaEventoTest --------------------
    @Test
    void guardaEventoConFechaLibre(){
        // Given
        LocalDate fecha = LocalDate.of(4000,1,1);
        TipoEvento tipo = TipoEvento.BODA;
        LocalTime hora = LocalTime.of(00, 00);
        String lugar = "Lugar";
        String direccion = "Dirección";
        String referencias = "Referencias";
        String imagen = "Imagen";
        String notas = "Notas";
        String nombre = "Nombre";
        String num = "Numero";

        Cotizacion cotizacionIdLleno = new Cotizacion();
        cotizacionIdLleno.setIdCotizacion(1L);
        Evento eventoIdLleno = new Evento();
        eventoIdLleno.setIdEvento(1L);
        Cliente cliente = new Cliente(nombre, num);
        cliente.setIdCliente(1L);

        when(repositorioCliente.findByNombreAndNumTelefono(nombre, num)).thenReturn(null);
        
        when(repositorioCliente.save(any(Cliente.class))).thenReturn(cliente);
        when(repositorioCotizacion.save(any(Cotizacion.class))).thenReturn(cotizacionIdLleno);
        when(repositorioEvento.save(any(Evento.class))).thenReturn(eventoIdLleno);

        // When
        Cotizacion cotizacionFuncion = servicioEvento.guardaEvento(nombre, num, fecha, tipo, hora, lugar, direccion, referencias, imagen, notas);
        
        // Then
        assertEquals(cotizacionIdLleno, cotizacionFuncion);
    }
    @Test
    void guardaEventoConFechaOcupada(){
        // Given
        LocalDate fecha = LocalDate.of(4000,1,1);
        TipoEvento tipo = TipoEvento.BODA;
        LocalTime hora = LocalTime.of(00, 00);
        String lugar = "Lugar";
        String direccion = "Dirección";
        String referencias = "Referencias";
        String imagen = "Imagen";
        String notas = "Notas";
        String nombre = "Nombre";
        String num = "Numero";

        Evento eventoIdLleno = new Evento();
        eventoIdLleno.setIdEvento(1L);
        Cliente cliente = new Cliente(nombre, num);
        cliente.setIdCliente(1L);

        when(repositorioCliente.findByNombreAndNumTelefono(nombre, num)).thenReturn(null);
        when(repositorioCliente.save(any(Cliente.class))).thenReturn(cliente);
        when(repositorioEvento.findByFecha(fecha)).thenReturn(eventoIdLleno);

        // When
        Cotizacion cotizacionFuncion = servicioEvento.guardaEvento(nombre, num, fecha, tipo, hora, lugar, direccion, referencias, imagen, notas);
        
        // Then
        assertEquals(null, cotizacionFuncion);
    }


    // -------------------- diaPresionadoTest --------------------
    @Test
    void diaPresionadoConEventoConDatosLleno(){
        // Given
        Evento evento = new Evento();

        LocalDate fecha = LocalDate.of(2026,1,1);
        TipoEvento tipo = TipoEvento.BODA;
        LocalTime hora = LocalTime.of(00, 00);
        String lugar = "Lugar";
        String direccion = "Dirección";
        String referencias = "Referencias";
        String imagen = "Imagen";
        String notas = "Notas";
        EstadoEvento estado = EstadoEvento.BORRADOR;
        Cliente cliente = new Cliente("Nombre", "1234567890");
        Float pagado = 100f;
        EstadoPago pago = EstadoPago.PENDIENTE;
        
        evento.setFecha(fecha);
        evento.setTipoEvento(tipo);
        evento.setFecha(fecha);
        evento.setLugar(lugar);
        evento.setDireccion(direccion);
        evento.setHora(hora);
        evento.setReferencias(referencias);
        evento.setVisualRecinto(imagen);
        evento.setDetalles(notas);
        evento.setEstadoEvento(estado);
        evento.setCliente(cliente);
        evento.setTotalPagado(pagado);
        evento.setEstadoPago(pago);

        // When
        List<Object> datos = servicioEvento.diaPresionado(evento);

        // Then
        assertEquals(estado.toString(), datos.get(0));
        assertEquals(evento.toString(), datos.get(1));
        assertEquals(hora.format(DateTimeFormatter.ofPattern("HH:mm")), datos.get(2));
        assertEquals(lugar, datos.get(3));
        assertEquals(cliente.toString(), datos.get(4));
        assertEquals(pagado, datos.get(5));
        assertEquals(pago.toString(), datos.get(6));
        assertEquals(7, datos.size());
    }
    @Test
    void diaPresionadoConEventoConDatosVacios(){
        // Given
        Evento evento = new Evento();

        LocalDate fecha = null;
        TipoEvento tipo = null;
        LocalTime hora = null;
        String lugar = null;
        String direccion = null;
        String referencias = null;
        String imagen = null;
        String notas = null;
        EstadoEvento estado = null;
        Cliente cliente = null;
        Float pagado = 0f;
        EstadoPago pago = null;
        
        evento.setFecha(fecha);
        evento.setTipoEvento(tipo);
        evento.setFecha(fecha);
        evento.setLugar(lugar);
        evento.setDireccion(direccion);
        evento.setHora(hora);
        evento.setReferencias(referencias);
        evento.setVisualRecinto(imagen);
        evento.setDetalles(notas);
        evento.setEstadoEvento(estado);
        evento.setCliente(cliente);
        evento.setTotalPagado(pagado);
        evento.setEstadoPago(pago);

        // When, Then
        assertThrowsExactly(IllegalArgumentException.class, () -> {
            servicioEvento.diaPresionado(evento);
        });
    }
    @Test
    void diaPresionadoConEventoNulo(){
        // Given
        Evento evento = null;

        // When, Then
        assertThrowsExactly(IllegalArgumentException.class, () -> {
            servicioEvento.diaPresionado(evento);
        });
    }


    // -------------------- fechaDisponibleTest --------------------
    @Test
    void fechaDisponibleConFechaDisponible(){
        // Given
        LocalDate fecha = LocalDate.of(3000, 1, 1);
        when(repositorioEvento.findByFecha(fecha)).thenReturn(null);

        // When
        boolean disponible = servicioEvento.fechaDisponible(fecha);

        // Then
        assertTrue(disponible);
    }
    @Test
    void fechaDisponibleConFechaOcupada(){
        // Given
        Evento evento = new Evento();
        LocalDate fecha = LocalDate.of(3000, 1, 1);
        evento.setFecha(fecha);
        when(repositorioEvento.findByFecha(fecha)).thenReturn(evento);

        // When
        boolean disponible = servicioEvento.fechaDisponible(fecha);

        // Then
        assertFalse(disponible);
    }
    @Test
    void fechaDisponibleConFechaPasada(){
        // Given
        LocalDate fecha = LocalDate.now().plusDays(15);
        when(repositorioEvento.findByFecha(fecha)).thenReturn(null);

        // When
        boolean disponible = servicioEvento.fechaDisponible(fecha);

        // Then
        assertFalse(disponible);
    }


    // -------------------- obtenerDiaActualTest --------------------
    @Test
    void obtenerDiaActualTest(){
        // Given
        // When
        LocalDate fecha = servicioEvento.obtenerDiaActual();

        // Then
        assertEquals(LocalDate.now(), fecha);
    }


    // -------------------- aumentarMesTest --------------------
    @Test
    void aumentarMesTest(){
        // Give
        LocalDate fecha = LocalDate.of(2025, 12, 31);
        LocalDate fechaEsperada = LocalDate.of(2026, 1, 1);

        // When
        LocalDate fechaObtenida = servicioEvento.aumentarMes(fecha);

        // Then
        assertEquals(fechaEsperada, fechaObtenida);
    }
    @Test
    void aumentarMesConFechaNula(){
        // Give
        LocalDate fecha = null;

        // When, Then
        assertThrowsExactly(IllegalArgumentException.class, () -> {
            servicioEvento.aumentarMes(fecha);
        });
    }


    // -------------------- disminuirMesTest --------------------
    @Test
    void dismiuirMesTest(){
        // Give
        LocalDate fecha = LocalDate.of(2026, 1, 31);
        LocalDate fechaEsperada = LocalDate.of(2025, 12, 1);

        // When
        LocalDate fechaObtenida = servicioEvento.disminuirMes(fecha);

        // Then
        assertEquals(fechaEsperada, fechaObtenida);
    }
    @Test
    void disminuirMesConFechaNula(){
        // Give
        LocalDate fecha = null;

        // When, Then
        assertThrowsExactly(IllegalArgumentException.class, () -> {
            servicioEvento.disminuirMes(fecha);
        });
    }












    /*
    // ---------- recuperaEventosTest ----------
    @Test
    void recuperaEventosConListaConMesIgual() {
        // Given
        Evento evento1 = new Evento();
        evento1.setFecha(LocalDate.of(2026, 01, 26));
        ArrayList<Evento> lista = new ArrayList<>();
        lista.add(evento1);
        LocalDate fecha = LocalDate.of(2026,01,24);
        Month mes = fecha.getMonth();

        when(repositorioEvento.findByMesOrderByFecha(mes)).thenReturn(lista);

        // When
        List<Evento> eventos = servicioEvento.recuperaEventosPorMes(fecha);

        // Then
        assertNotEquals(0, eventos.size());
    }
    @Test
    void recuperaEventosConListaSinMesIgual() {
        // Given
        ArrayList<Evento> lista = new ArrayList<>();
        LocalDate fecha = LocalDate.of(2026,03,24);
        Month mes = fecha.getMonth();
        when(repositorioEvento.findByMesOrderByFecha(mes)).thenReturn(lista);
        // When
        List<Evento> eventos = servicioEvento.recuperaEventosPorMes(fecha);
        // Then
        assertEquals(0, eventos.size());
    }
    @Test
    void recuperaEventosConFechaNull() {
        // Given
        LocalDate fecha = null;

        // When, Then
        assertThrowsExactly(IllegalArgumentException.class, () -> {
            servicioEvento.recuperaEventosPorMes(fecha);
        });
    }

    // fechasBloqueadasTest
    @Test
    void fechasBloqueadasTest() {
        // When
        LocalDate resultado = servicioEvento.obtenerDiaActual();
        // Then
        LocalDate fechaDespues = LocalDate.now().plusDays(16);
        assertEquals(fechaDespues, resultado);
    }


    // eliminaEventoTest
    @Test
    void eliminaEventoNoExistente(){
        // Given
        Evento evento = new Evento();

        doThrow(new RuntimeException("No existe el evento")).when(repositorioEvento).delete(evento);

        // When
        boolean resultado = servicioEvento.eliminaEvento(evento);

        // Then
        assertFalse(resultado);
        verify(repositorioEvento).delete(evento); // Se comprueba que se haya intentado borrar del repositorio;
    }
    @Test
    void eliminaEventoNull(){
        // Given
        Evento evento = null;
        // When
        boolean resultado = servicioEvento.eliminaEvento(evento);
        // Then
        assertFalse(resultado);
        verifyNoInteractions(repositorioEvento); // Se verifica que nunca se llegó a tocar a repositorioEvento
    }*/
}
