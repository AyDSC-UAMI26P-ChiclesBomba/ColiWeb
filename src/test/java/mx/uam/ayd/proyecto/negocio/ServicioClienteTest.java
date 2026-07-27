package mx.uam.ayd.proyecto.negocio;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import mx.uam.ayd.proyecto.datos.RepositorioCliente;
import mx.uam.ayd.proyecto.negocio.modelo.Cliente;

@ExtendWith(MockitoExtension.class)
public class ServicioClienteTest {
    
    @Mock
    private RepositorioCliente repositorioCliente;

    @InjectMocks
    private ServicioCliente servicioCliente;

    // ------------------- recupera -------------------
    @Test
    void recuperaConListaConClientes(){
        // Given
        Cliente cliente1 = new Cliente();
        Cliente cliente2 = new Cliente();
        List<Cliente> clientes = new ArrayList<>();
        clientes.add(cliente1);
        clientes.add(cliente2);
        when(repositorioCliente.findAll()).thenReturn(clientes);

        // When
        List<Cliente> clientesObtenidos = servicioCliente.recupera();

        // Then
        assertEquals(2, clientesObtenidos.size());
    }
    @Test
    void recuperaConListaSinClientes(){
        // Given
        List<Cliente> clientes = new ArrayList<>();
        when(repositorioCliente.findAll()).thenReturn(clientes);

        // When
        List<Cliente> clientesObtenidos = servicioCliente.recupera();

        // Then
        assertEquals(0, clientesObtenidos.size());
    }


    // ------------------- obtieneNumCliente -------------------
    @Test
    void obtieneNumClienteConNombreNulo(){
        // Given
        String nombre = null;

        // When Then
        assertThrowsExactly(IllegalArgumentException.class, () -> {
            servicioCliente.obtieneNumCliente(nombre);
        });
    }
    @Test
    void obtieneNumClienteConNingunClienteConEseNombre(){
        // Given
        String nombre = "nombre";
        when(repositorioCliente.findByNombre(nombre)).thenReturn(null);

        // When Then
        assertThrowsExactly(IllegalArgumentException.class, () -> {
            servicioCliente.obtieneNumCliente(nombre);
        });
    }
    @Test
    void obtieneNumClienteConClienteConEseNombre(){
        // Given
        String nombre = "nombre";
        String numero = "1234567890";
        Cliente cliente = new Cliente(nombre, numero);
        when(repositorioCliente.findByNombre(nombre)).thenReturn(cliente);
        String numeroObtenido = "";

        // When
        try{
            numeroObtenido = servicioCliente.obtieneNumCliente(nombre);
        }catch(Exception e){}

        // Then
        assertEquals(numeroObtenido, numero);
    }
}
