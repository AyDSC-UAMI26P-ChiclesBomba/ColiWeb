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

import mx.uam.ayd.proyecto.datos.RepositorioMaterial;
import mx.uam.ayd.proyecto.negocio.modelo.Material;

@ExtendWith(MockitoExtension.class)
class ServicioMaterialTest {

    @Mock
    private RepositorioMaterial repositorioMaterial;

    @InjectMocks
    private ServicioMaterial servicioMaterial;

    @Test
    @DisplayName("Deberia verificar que se recuperen los materiales correctamente")
    void recuperaTodoMaterialConListaNoVacia(){
        // Given
        Material material1 = new Material();
        Material material2 = new Material();
        List<Material> lista = new ArrayList<>();
        lista.add(material1);
        lista.add(material2);
        when(repositorioMaterial.findAll()).thenReturn(lista);

        // When
        List<Material> materiales = servicioMaterial.recuperaTodoMaterial();

        // Then
        assertEquals(2, materiales.size());
    }
    @Test
    @DisplayName("Deberia regresar una lista vacia")
    void recuperaTodoMaterialConListaVacia() {
        // Given
        List<Material> lista = new ArrayList<>();
        when(repositorioMaterial.findAll()).thenReturn(lista);

        // When
        List<Material> materialesObtenidos = servicioMaterial.recuperaTodoMaterial();
        
        // Then
        assertEquals(0, materialesObtenidos.size());
    }
    
}
