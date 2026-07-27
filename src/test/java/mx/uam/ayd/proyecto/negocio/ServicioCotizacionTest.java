package mx.uam.ayd.proyecto.negocio;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import mx.uam.ayd.proyecto.datos.RepositorioCliente;
import mx.uam.ayd.proyecto.datos.RepositorioCotizacion;
import mx.uam.ayd.proyecto.datos.RepositorioEvento;
import mx.uam.ayd.proyecto.negocio.modelo.Cotizacion.Tamano;
import mx.uam.ayd.proyecto.negocio.modelo.Cliente;
import mx.uam.ayd.proyecto.negocio.modelo.Cotizacion;
import mx.uam.ayd.proyecto.negocio.modelo.DetalleCotizacion;
import mx.uam.ayd.proyecto.negocio.modelo.Evento;
import mx.uam.ayd.proyecto.negocio.modelo.Material;

@ExtendWith(MockitoExtension.class)
class ServicioCotizacionTest {
    
    @Mock
    private RepositorioCotizacion repositorioCotizacion;
    @Mock
    private RepositorioEvento repositorioEvento;
    @Mock
    private RepositorioCliente repositorioCliente;
    @InjectMocks
    private ServicioCotizacion servicioCotizacion;

    // actualizaDetallesCotizacion
    @Test
    void actualizaDetallesCotizacionConEventoNulo(){
        // Given 
        Evento evento = null;
        List<DetalleCotizacion> detalle = new ArrayList<>();

        // When Then
        assertThrows(IllegalArgumentException.class, () -> {
            servicioCotizacion.actualizaDetallesCotizacion(null, null, null, null, null, detalle, null, evento);
        });
    }
    @Test
    void actualizaDetallesCotizacionTamanoPequeno() {
        // Given
        Cotizacion cotizacion = new Cotizacion();
        Evento evento = new Evento();

        Material material1 = new Material();
        material1.setPrecio(150f);
        DetalleCotizacion detalle1 = new DetalleCotizacion();
        detalle1.setCantidad(2);
        detalle1.setMaterial(material1);

        List<DetalleCotizacion> listaMateriales = new ArrayList<>();
        listaMateriales.add(detalle1);

        Float transporte = 100f;
        Float personalizado = 50f;
        Float materialCliente = 50f;

        // When
        Cotizacion resultado = servicioCotizacion.actualizaDetallesCotizacion(transporte, personalizado, materialCliente, Tamano.PEQUENO, "Fiesta infantil", listaMateriales, cotizacion, evento);

        // Then
        assertEquals(100f, resultado.getTransporte());
        assertEquals(50f, resultado.getMaterialPersonalizado());
        assertEquals(50f, resultado.getMaterialCliente());
        assertEquals(Tamano.PEQUENO, resultado.getTamano());
        assertEquals("Fiesta infantil", evento.getDetalles());

        // Then
        // Cálculos internos esperados
        assertEquals(300f, resultado.getTotalMaterial()); // 2 * 150
        assertEquals(200f, resultado.getExtra()); // 100 + 50 + 50
        assertEquals(206f, resultado.getConsumibles()); // Por ser PEQUEÑO
        assertEquals(1000f, resultado.getManoObra()); // Por ser PEQUEÑO
        assertEquals(1000f, resultado.getGanancia()); // Por ser PEQUEÑO
        assertEquals(2706f, resultado.getTotal()); // 300 + 200 + 206 + 1000 + 1000
    }
    @Test
    void actualizaDetallesCotizacionTamanoMediano() {
        // Given
        Cotizacion cotizacion = new Cotizacion();
        Evento evento = new Evento();
        List<DetalleCotizacion> listaVacia = new ArrayList<>();

        // When
        Cotizacion resultado = servicioCotizacion.actualizaDetallesCotizacion(
            0f, 0f, 0f, Tamano.MEDIANO, "Boda mediana", listaVacia, cotizacion, evento
        );

        // Then
        // Cálculos internos esperados para MEDIANO
        assertEquals(0f, resultado.getTotalMaterial());
        assertEquals(0f, resultado.getExtra());
        assertEquals(402f, resultado.getConsumibles()); // Por ser MEDIANO
        assertEquals(1500f, resultado.getManoObra()); // Por ser MEDIANO
        assertEquals(1500f, resultado.getGanancia()); // Por ser MEDIANO
        assertEquals(3402f, resultado.getTotal()); // 402 + 1500 + 1500
    }
    @Test
    void actualizaDetallesCotizacionTamanoGrande() {
        // Given
        Cotizacion cotizacion = new Cotizacion();
        Evento evento = new Evento();
        List<DetalleCotizacion> listaVacia = new ArrayList<>();

        // When
        Cotizacion resultado = servicioCotizacion.actualizaDetallesCotizacion(
            0f, 0f, 0f, Tamano.GRANDE, "Evento corporativo", listaVacia, cotizacion, evento
        );

        // Then - Cálculos internos esperados para GRANDE
        assertEquals(0f, resultado.getTotalMaterial());
        assertEquals(0f, resultado.getExtra());
        assertEquals(620f, resultado.getConsumibles()); // Por ser GRANDE
        assertEquals(2400f, resultado.getManoObra()); // Por ser GRANDE
        assertEquals(2400f, resultado.getGanancia()); // Por ser GRANDE
        assertEquals(5420f, resultado.getTotal()); // 620 + 2400 + 2400
    }


    // copiaTotal
    @Test
    void copiaTotalCotizacionNoNula(){
        // Given
        Cotizacion cotizacion = new Cotizacion();
        cotizacion.setTotal(500f);

        // When
        Float total = servicioCotizacion.copiaTotal(cotizacion);

        // Then
        assertEquals(500f, total);
    }
    @Test
    void copiaTotalCotizacionNula(){
        // Given
        Cotizacion cotizacion = null;

        // When Then
        assertThrows(IllegalArgumentException.class, () -> {
            servicioCotizacion.copiaTotal(cotizacion);
        });
    }


    // aprobarCotizacion
    @Test
    void aprobarCotizacionConCotizacionNula(){
        // Given
        Cotizacion cotizacion = null;

        // When
        boolean aprobado = servicioCotizacion.aprobarCotizacion(cotizacion, false);

        // Then
        assertFalse(aprobado);
    }
    @Test
    void aprobarCotizacionConEstadoTrue(){
        // Given
        Cotizacion cotizacion = new Cotizacion();
        boolean estado = true;

        // When
        boolean aprobado = servicioCotizacion.aprobarCotizacion(cotizacion, estado);

        // Then
        assertTrue(aprobado);
    }
    @Test
    void aprobarCotizacionConEstadoFalse(){
        // Given
        Cotizacion cotizacion = new Cotizacion();
        boolean estado = false;

        // When
        boolean aprobado = servicioCotizacion.aprobarCotizacion(cotizacion, estado);

        // Then
        assertFalse(aprobado);
    }


    // borraCotizacion
    @Test
    void borraCotizacionConCotizacionNula(){
        // Given
        Cotizacion cotizacion = null;

        // When
        boolean eliminado = servicioCotizacion.borraCotizazion(cotizacion);

        // Then
        assertFalse(eliminado);
    }
    @Test
    void borraCotizacionConCotizacionEnRepositorio(){
        // Given
        Cotizacion cotizacion = new Cotizacion();
        // No se especifica sobre el delete porque no arroja nada al hacerse exitosamente

        // When
        boolean eliminado = servicioCotizacion.borraCotizazion(cotizacion);

        // Then
        assertTrue(eliminado);
    }
    @Test
    void borraCotizacionConCotizacionSinEstarEnRepositorio() {
        // Given
        Cotizacion cotizacion = new Cotizacion();
        // Simulamos que el repositorio falla al intentar eliminar (lanza una excepción)
        doThrow(new RuntimeException("Error en base de datos")).when(repositorioCotizacion).delete(cotizacion);
        
        // When
        boolean eliminado = servicioCotizacion.borraCotizazion(cotizacion);

        // When Then
        assertFalse(eliminado);
    }


    // recuperaInfo
    @Test
    void recuperaInfoConCotizacionNula(){
        // Given
        Cotizacion cotizacion = null;
        
        // When Then
        assertThrows(IllegalArgumentException.class, () -> {
            servicioCotizacion.recuperaInfo(cotizacion);
        });
    }
    @Test
    void recuperaInfoConCotizacionConEvento(){
        // Given
        Cotizacion cotizacion = new Cotizacion();
        cotizacion.setIdCotizacion(1L);
        Evento evento = new Evento();
        try {
            evento.setCotizacion(cotizacion);
        } catch (Exception e) {}
        when(repositorioEvento.findByCotizacion(cotizacion)).thenReturn(evento);

        // Then
        Evento eventoObtenido = servicioCotizacion.recuperaInfo(cotizacion);

        // Then
        assertEquals(evento, eventoObtenido);
    }
    @Test
    void recuperaInfoConCotizacionSinEvento(){
        // Guven
        Cotizacion cotizacion = new Cotizacion();
        cotizacion.setIdCotizacion(1L);
        when(repositorioEvento.findByCotizacion(cotizacion)).thenReturn(null);

        // Then
        Evento eventoObtenido = servicioCotizacion.recuperaInfo(cotizacion);

        // Then
        assertNull(eventoObtenido);
    }


    // recuperaEvento
    @Test
    void recuperaEventoConCotizacionNula(){
        // Given
        Cotizacion cotizacion = null;
        
        // When Then
        assertThrows(IllegalArgumentException.class, () -> {
            servicioCotizacion.recuperaEvento(cotizacion);
        });
    }
    @Test
    void recuperaEventoConCotizacionConEvento(){
        // Given
        Cotizacion cotizacion = new Cotizacion();
        cotizacion.setIdCotizacion(1L);
        Evento evento = new Evento();
        try {
            evento.setCotizacion(cotizacion);
        } catch (Exception e) {}
        when(repositorioEvento.findByCotizacion(cotizacion)).thenReturn(evento);

        // Then
        Evento eventoObtenido = servicioCotizacion.recuperaEvento(cotizacion);

        // Then
        assertEquals(evento, eventoObtenido);
    }
    @Test
    void recuperaEventoConCotizacionSinEvento(){
        // Given
        Cotizacion cotizacion = new Cotizacion();
        cotizacion.setIdCotizacion(1L);
        when(repositorioEvento.findByCotizacion(cotizacion)).thenReturn(null);

        // Then
        Evento eventoObtenido = servicioCotizacion.recuperaEvento(cotizacion);

        // Then
        assertNull(eventoObtenido);
    }


    // recuperaCliente
    @Test
    void recuperaClienteConCotizacionNula(){
        // Given
        Cotizacion cotizacion = null;
        
        // When Then
        assertThrows(IllegalArgumentException.class, () -> {
            servicioCotizacion.recuperaCliente(cotizacion);
        });
    }
    @Test
    void recuperaClienteConCotizacionConCliente(){
        // Given
        Cotizacion cotizacion = new Cotizacion();
        cotizacion.setIdCotizacion(1L);
        Cliente cliente = new Cliente();
        cliente.setIdCliente(1L);
        cotizacion.setCliente(cliente);
        when(repositorioCliente.findByCotizaciones(cotizacion)).thenReturn(cliente);

        // Then
        Cliente clienteObtenido = servicioCotizacion.recuperaCliente(cotizacion);

        // Then
        assertEquals(cliente, clienteObtenido);
    }
    @Test
    void recuperaClienteConCotizacionSinCliente(){
        // Given
        Cotizacion cotizacion = new Cotizacion();
        cotizacion.setIdCotizacion(1L);
        when(repositorioCliente.findByCotizaciones(cotizacion)).thenReturn(null);

        // Then
        Cliente clienteObtenido = servicioCotizacion.recuperaCliente(cotizacion);

        // Then
        assertNull(clienteObtenido);
    }
}
