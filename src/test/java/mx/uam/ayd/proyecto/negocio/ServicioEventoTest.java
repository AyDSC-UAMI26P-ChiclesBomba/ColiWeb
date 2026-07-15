package mx.uam.ayd.proyecto.negocio;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import mx.uam.ayd.proyecto.datos.RepositorioEvento;
import mx.uam.ayd.proyecto.negocio.modelo.Evento;

@ExtendWith(MockitoExtension.class)
class ServicioEventoTest {

    @Mock
    private RepositorioEvento repositorioEvento;

    @InjectMocks
    private ServicioEvento servicioEvento;
    
    // recuperaEventosTest
    @Test
    void recuperaEventosConListaConMesIgual() {
        // Given
        Evento evento1 = new Evento();
        evento1.setFecha(LocalDate.of(2026, 01, 26));
        ArrayList<Evento> lista = new ArrayList<>();
        lista.add(evento1);
        LocalDate fecha = LocalDate.of(2026,01,24);
        Month mes = fecha.getMonth();

        when(repositorioEvento.findByMes(mes)).thenReturn(lista);

        // When
        List<Evento> eventos = servicioEvento.recuperaEventos(fecha);

        // Then
        assertNotEquals(0, eventos.size());
    }
    @Test
    void recuperaEventosConListaSinMesIgual() {
        // Given
        ArrayList<Evento> lista = new ArrayList<>();
        LocalDate fecha = LocalDate.of(2026,03,24);
        Month mes = fecha.getMonth();
        when(repositorioEvento.findByMes(mes)).thenReturn(lista);
        // When
        List<Evento> eventos = servicioEvento.recuperaEventos(fecha);
        // Then
        assertEquals(0, eventos.size());
    }
    @Test
    void recuperaEventosConFechaNull() {
        // Given
        LocalDate fecha = null;

        // When, Then
        assertThrowsExactly(IllegalArgumentException.class, () -> {
            servicioEvento.recuperaEventos(fecha);
        });
    }

    // fechasBloqueadasTest
    @Test
    void fechasBloqueadasTest() {
        // When
        LocalDate resultado = servicioEvento.fechasBloqueadas();
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
    }
}
