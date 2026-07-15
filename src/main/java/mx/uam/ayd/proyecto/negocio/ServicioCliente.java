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

    public List<Cliente> obtenerClientes(){
        ArrayList<Cliente> clientes = new ArrayList<>();
        for(Cliente cliente:repositorioCliente.findAll()){
            clientes.add(cliente);
        }
        return clientes;
    }
    public Object[] obtieneInfoCliente(Cliente cliente) throws Exception {
        if(cliente == null) throw new IllegalArgumentException("El cliente no puede ser nulo");
        return new Object[] {cliente.getNombre(), cliente.getNumTelefono()};
    }
    
}
