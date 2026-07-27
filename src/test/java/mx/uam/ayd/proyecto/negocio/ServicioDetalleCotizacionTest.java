package mx.uam.ayd.proyecto.negocio;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import mx.uam.ayd.proyecto.datos.RepositorioDetalleCotizacion;
import mx.uam.ayd.proyecto.negocio.modelo.Cotizacion;
import mx.uam.ayd.proyecto.negocio.modelo.DetalleCotizacion;
import mx.uam.ayd.proyecto.negocio.modelo.Material;

@ExtendWith(MockitoExtension.class)
class ServicioDetalleCotizacionTest {

    @Mock
    private RepositorioDetalleCotizacion repositorioDetalleCotizacion;

    @InjectMocks
    private ServicioDetalleCotizacion servicioDetalleCotizacion;

    private Cotizacion cotizacion;
    private Material materialConPrecio;
    private Material materialSinPrecio;
    private DetalleCotizacion detalle;

    @BeforeEach
    void setUp() {
        cotizacion = new Cotizacion();

        materialConPrecio = new Material();
        materialConPrecio.setPrecio(50.0f);

        materialSinPrecio = new Material();
        materialSinPrecio.setPrecio(null);

        detalle = new DetalleCotizacion();
        detalle.setMaterial(materialConPrecio);
        detalle.setCotizacion(cotizacion);
        detalle.setCantidad(2);
        detalle.setCosto(100.0f);
    }

    @Test
    @DisplayName("Debería retornar los detalles asociados a la cotización")
    void verificarListaMaterialExitoso() {
        // Given
        List<DetalleCotizacion> listaEsperada = List.of(detalle);
        when(repositorioDetalleCotizacion.findByCotizacion(cotizacion)).thenReturn(listaEsperada);

        // When
        List<DetalleCotizacion> resultado = servicioDetalleCotizacion.verificarListaMaterial(cotizacion);

        // Then
        assertEquals(1, resultado.size());
        assertEquals(detalle, resultado.get(0));
        verify(repositorioDetalleCotizacion, times(1)).findByCotizacion(cotizacion);
    }

    @Test
    @DisplayName( "Debería crear el detalle, calcular cantidad/costo y guardar")
    void agregaMaterialListaExitoso() {
        // Given
        when(repositorioDetalleCotizacion.findAll()).thenReturn(List.of(detalle));

        // When
        List<DetalleCotizacion> resultado = servicioDetalleCotizacion.agregaMaterialLista(materialConPrecio, cotizacion);

        // Then
        assertEquals(1, resultado.size());

        // Capturamos el objeto que se guardó para verificar lo que hicieron los métodos privados
        ArgumentCaptor<DetalleCotizacion> captor = ArgumentCaptor.forClass(DetalleCotizacion.class);
        verify(repositorioDetalleCotizacion, times(2)).save(captor.capture());

        DetalleCotizacion detalleGuardado = captor.getValue(); // Obtiene la última invocación de save()
        assertEquals(1, detalleGuardado.getCantidad()); // aumentarCantidadUno (0 + 1)
        assertEquals(50.0f, detalleGuardado.getCosto()); // calcularCostoMaterialElegido (1 * 50)
        assertTrue(detalleGuardado.getPreciosCompletos()); // validarTienePrecio
    }

    @Test
    @DisplayName("Debería incrementar la cantidad en 1 y actualizar el costo total")
    void aumentaCantidadExitoso() {
        // Given
        when(repositorioDetalleCotizacion.findAll()).thenReturn(List.of(detalle));

        // When
        servicioDetalleCotizacion.aumentaCantidad(detalle);

        // Then - Evaluamos los cambios realizados por los métodos privados indirectamente
        assertEquals(3, detalle.getCantidad()); // 2 + 1
        assertEquals(150.0f, detalle.getCosto()); // 3 * 50.0f
        verify(repositorioDetalleCotizacion, times(1)).save(detalle);
    }

    @Test
    @DisplayName("Debería decrementar la cantidad en 1 y actualizar el costo total")
    void disminuyeCantidadExitoso() {
        // Given
        when(repositorioDetalleCotizacion.findAll()).thenReturn(List.of(detalle));

        // When
        servicioDetalleCotizacion.disminuyeCantidad(detalle);

        // Then
        assertEquals(1, detalle.getCantidad()); // 2 - 1
        assertEquals(50.0f, detalle.getCosto()); // 1 * 50.0f
        verify(repositorioDetalleCotizacion, times(1)).save(detalle);
    }

    @Test
    @DisplayName("borraMaterialLista: Elimina el material existente mediante el repositorio")
    void borraMaterialListaExitoso() {
        // Given
        when(repositorioDetalleCotizacion.findAll()).thenReturn(new ArrayList<>());

        // When
        List<DetalleCotizacion> resultado = servicioDetalleCotizacion.borraMaterialLista(detalle);

        // Then
        assertEquals(0, resultado.size());
        verify(repositorioDetalleCotizacion, times(1)).delete(detalle);
    }

    @Test
    @DisplayName("Cuando es nulo, no llama al repositorio delete")
    void borraMaterialListaElementoNulo() {
        // Given
        when(repositorioDetalleCotizacion.findAll()).thenReturn(new ArrayList<>());

        // When
        servicioDetalleCotizacion.borraMaterialLista(null);

        // Then
        verify(repositorioDetalleCotizacion, never()).delete(any());
    }

    @Test
    @DisplayName("Regresa true si todos los elementos tienen precio")
    void validaPreciosTodosConPrecio() {
        // When
        boolean resultado = servicioDetalleCotizacion.validaPrecios(List.of(detalle));

        // Then
        assertTrue(resultado);
    }

    @Test
    @DisplayName("Regresafalse si al menos un elemento tiene precio nulo")
    void validaPreciosConPrecioFaltante() {
        // Given
        DetalleCotizacion detalleSinPrecio = new DetalleCotizacion();
        detalleSinPrecio.setMaterial(materialSinPrecio);

        // When
        boolean resultado = servicioDetalleCotizacion.validaPrecios(List.of(detalle, detalleSinPrecio));

        // Then
        assertFalse(resultado);
    }

    @Test
    @DisplayName("Cambia el precio del material y recalculado del costo total")
    void actualizaPrecioExitoso() {
        // When
        DetalleCotizacion resultado = servicioDetalleCotizacion.actualizaPrecio(75.0f, detalle);

        // Then
        assertEquals(75.0f, resultado.getMaterial().getPrecio());
        assertEquals(150.0f, resultado.getCosto()); // 2 * 75.0f
        verify(repositorioDetalleCotizacion, times(1)).save(detalle);
    }
}