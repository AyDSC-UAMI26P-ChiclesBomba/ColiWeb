package mx.uam.ayd.proyecto.negocio;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import mx.uam.ayd.proyecto.datos.RepositorioComestible;
import mx.uam.ayd.proyecto.negocio.modelo.Comestible;
import mx.uam.ayd.proyecto.negocio.modelo.Globo;

@ExtendWith(MockitoExtension.class)
public class ServicioComestibleTest {
    
    @Mock
    private RepositorioComestible repositorioComestible;

    @InjectMocks
    private ServicioComestible servicioComestible;

    @Test
    void recuperaTodoGloboConListaNoVacia(){
        // Given
        Comestible globo1 = new Comestible();
        Comestible globo2 = new Comestible();
        List<Comestible> lista = new ArrayList<>();
        lista.add(globo1);
        lista.add(globo2);
        when(repositorioComestible.findAll()).thenReturn(lista);

        // When
        List<Comestible> comestibles = servicioComestible.recuperaTodoComestible();

        // Then
        assertEquals(2, comestibles.size());
    }
    @Test
    void recuperaTodoGloboConListaVacia() {
        // Given
        List<Comestible> lista = new ArrayList<>();
        when(repositorioComestible.findAll()).thenReturn(lista);

        // When
        List<Comestible> comestibles = servicioComestible.recuperaTodoComestible();
        
        // Then
        assertEquals(0, comestibles.size());
    }
}
