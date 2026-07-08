package mx.uam.ayd.proyecto.negocio.modelo;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

/**
 * Entidad de negocio Cliente
 */
public class Cliente {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long idCliente;

    private String nombre;
    private String numTelefono;

    @OneToMany(targetEntity = Evento.class, fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    private final List <Evento> eventos = new ArrayList <> ();
    
    // Métodos como getters, setters, etc.

    
}
