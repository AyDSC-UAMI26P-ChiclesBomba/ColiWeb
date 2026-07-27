package mx.uam.ayd.proyecto.negocio;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import mx.uam.ayd.proyecto.datos.RepositorioGlobo;
import mx.uam.ayd.proyecto.negocio.modelo.Globo;

@ExtendWith(MockitoExtension.class)
class ServicioGloboTest  {
    
    @Mock
    private RepositorioGlobo repositorioGlobo;

    @InjectMocks
    private ServicioGlobo servicioGlobo;

    @Test
    @DisplayName("Deberia verificar que se recuperen los globos correctamente")
    void recuperaTodoGloboConListaNoVacia(){
        // Given
        Globo globo1 = new Globo();
        Globo globo2 = new Globo();
        List<Globo> lista = new ArrayList<>();
        lista.add(globo1);
        lista.add(globo2);
        when(repositorioGlobo.findAll()).thenReturn(lista);

        // When
        List<Globo> globos = servicioGlobo.recuperaTodoGlobo();

        // Then
        assertEquals(2, globos.size());
    }
    @Test
    @DisplayName("Deberia regresar una lista vacia")
    void recuperaTodoGloboConListaVacia() {
        // Given
        List<Globo> lista = new ArrayList<>();
        when(repositorioGlobo.findAll()).thenReturn(lista);

        // When
        List<Globo> globosObtenidos = servicioGlobo.recuperaTodoGlobo();
        
        // Then
        assertEquals(0, globosObtenidos.size());
    }

}
