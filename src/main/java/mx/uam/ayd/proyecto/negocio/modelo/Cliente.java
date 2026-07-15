package mx.uam.ayd.proyecto.negocio.modelo;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

/**
 * Entidad de negocio Cliente
 */
@Entity
public class Cliente {
    // ID
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idCliente;

    // Atributos de la entidad
    private String nombre;
    private String numTelefono;

    // Relación con Evento, se crea como un idCliente en Evento (Cliente no guarda columna)
    @OneToMany(mappedBy = "cliente", targetEntity = Evento.class, fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private final List <Evento> eventos = new ArrayList <> ();

    // Relación con Cotización, se crea como un idCliente en Cotizacion (Cliente no guarda llave foránea)
    @OneToMany(mappedBy = "cliente", targetEntity = Cotizacion.class, fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private final List <Cotizacion> cotizaciones = new ArrayList <> ();
    
    // Métodos como getters, setters, etc.
    // Constructor
    public Cliente(){}
    public Cliente(String nombre, String num){
        this.nombre = nombre;
        this.numTelefono = num;
    }
    
    public String getNombre(){
        return nombre;
    }public String getNumTelefono(){
        return numTelefono;
    }
}
