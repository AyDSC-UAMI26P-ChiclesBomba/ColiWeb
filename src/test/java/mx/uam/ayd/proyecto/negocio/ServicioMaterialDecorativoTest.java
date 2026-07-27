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

import mx.uam.ayd.proyecto.datos.RepositorioMaterialDecorativo;
import mx.uam.ayd.proyecto.negocio.modelo.MaterialDecorativo;

@ExtendWith(MockitoExtension.class)
class ServicioMaterialDecorativoTest {
    
    @Mock
    private RepositorioMaterialDecorativo repositorioMaterialDecorativo;

    @InjectMocks
    private ServicioMaterialDecorativo servicioMaterialDecorativo;

    @Test
    void recuperaTodoMaterialDecorativoConListaNoVacia(){
        // Given
        MaterialDecorativo materialDecorativo1 = new MaterialDecorativo();
        MaterialDecorativo materialDecorativo2 = new MaterialDecorativo();
        List<MaterialDecorativo> lista = new ArrayList<>();
        lista.add(materialDecorativo1);
        lista.add(materialDecorativo2);
        when(repositorioMaterialDecorativo.findAll()).thenReturn(lista);

        // When
        List<MaterialDecorativo> materialesDecorativos = servicioMaterialDecorativo.recuperaTodoMaterialDecorativo();

        // Then
        assertEquals(2, materialesDecorativos.size());
    }
    @Test
    void recuperaTodoMaterialDecorativoConListaVacia() {
        // Given
        List<MaterialDecorativo> lista = new ArrayList<>();
        when(repositorioMaterialDecorativo.findAll()).thenReturn(lista);

        // When
        List<MaterialDecorativo> materialesDecorativos = servicioMaterialDecorativo.recuperaTodoMaterialDecorativo();
        
        // Then
        assertEquals(0, materialesDecorativos.size());
    }
}
