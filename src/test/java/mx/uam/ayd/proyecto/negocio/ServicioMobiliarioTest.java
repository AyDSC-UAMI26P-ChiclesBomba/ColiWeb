package mx.uam.ayd.proyecto.negocio;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import mx.uam.ayd.proyecto.datos.RepositorioMobiliario;
import mx.uam.ayd.proyecto.negocio.modelo.Mobiliario;
import mx.uam.ayd.proyecto.negocio.modelo.Mobiliario.TipoDano;

@ExtendWith(MockitoExtension.class)
class ServicioMobiliarioTest {

    @Mock
    private RepositorioMobiliario repositorioMobiliario;

    @InjectMocks ServicioMobiliario servicioMobiliario;

    // recuperaTodoMobiliario
    @Test
    void recuperaTodoMobiliarioConListaNoVacia(){
        // Given
        Mobiliario mobiliario1 = new Mobiliario();
        Mobiliario mobiliario2 = new Mobiliario();
        List<Mobiliario> lista = new ArrayList<>();
        lista.add(mobiliario1);
        lista.add(mobiliario2);
        when(repositorioMobiliario.findAll()).thenReturn(lista);

        // When
        List<Mobiliario> mobiliarios = servicioMobiliario.recuperaTodoMobiliario();

        // Then
        assertEquals(2, mobiliarios.size());
    }
    @Test
    void recuperaTodoMobiliarioConListaVacia() {
        // Given
        List<Mobiliario> lista = new ArrayList<>();
        when(repositorioMobiliario.findAll()).thenReturn(lista);

        // When
        List<Mobiliario> mobiliarios = servicioMobiliario.recuperaTodoMobiliario();
        
        // Then
        assertEquals(0, mobiliarios.size());
    }
    

    // verificarNoDanoTotal
    // tieneDanoTotal
    // Debido a que tieneDanoTotal es un método privado, entonces se hace la prueba dentro del mismo verificarNoDanoTotal
    @Test
    void verificarNoDanoTotalConDanoTotal() {
        // Given
        Mobiliario mobiliario = new Mobiliario();
        mobiliario.setTipoDano(TipoDano.TOTAL);
        
        // When
        boolean verificar = servicioMobiliario.verificarNoDanoTotal(mobiliario);

        // Then
        assertTrue(verificar);
    }
    @Test
    void verificarNoDanoTotalSinDanoTotal() {
        // Given
        Mobiliario mobiliario1 = new Mobiliario();
        mobiliario1.setTipoDano(TipoDano.PARCIAL);
        Mobiliario mobiliario2 = new Mobiliario();
        mobiliario2.setTipoDano(TipoDano.NINGUNO);
        
        // When
        boolean verificar1 = servicioMobiliario.verificarNoDanoTotal(mobiliario1);
        boolean verificar2 = servicioMobiliario.verificarNoDanoTotal(mobiliario2);

        // Then
        assertFalse(verificar1);
        assertFalse(verificar2);
    }
}
