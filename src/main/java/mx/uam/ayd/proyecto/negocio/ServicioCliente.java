package mx.uam.ayd.proyecto.negocio;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import mx.uam.ayd.proyecto.datos.RepositorioCliente;
import mx.uam.ayd.proyecto.negocio.modelo.Cliente;

/**
 * Servicio relacionado con los clientes
 * @author JLCB
 */
@Service
public class ServicioCliente {

    RepositorioCliente repositorioCliente;

    @Autowired
    ServicioCliente(RepositorioCliente repositorioCliente){
        this.repositorioCliente = repositorioCliente;
    }

    public List<String> obtenerNombresClientes(){
        ArrayList<String> nombresClientes = new ArrayList<>();
        for(Cliente cliente:repositorioCliente.findAll()){
            nombresClientes.add(cliente.getNombre());
        }
        return nombresClientes;
    }
    public List<String> obtenerNumerosClientes(){
        ArrayList<String> numerosClientes = new ArrayList<>();
        for(Cliente cliente:repositorioCliente.findAll()){
            numerosClientes.add(cliente.getNumTelefono());
        }
        return numerosClientes;
    }
    public Object[] obtieneInfoCliente(Cliente cliente) throws Exception {
        if(cliente == null) throw new IllegalArgumentException("El cliente no puede ser nulo");
        return new Object[] {cliente.getNombre(), cliente.getNumTelefono()};
    }
    
    public Cliente encontrarPorNombre(String nombre){
        return repositorioCliente.findByNombre(nombre);
    }
}
