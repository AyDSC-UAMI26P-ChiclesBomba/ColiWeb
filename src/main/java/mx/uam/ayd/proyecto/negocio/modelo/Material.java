package mx.uam.ayd.proyecto.negocio.modelo;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

/**
 * Entidad de negocio Material
 */
@Entity
@Inheritance(strategy = InheritanceType.JOINED) // Es para crear una relación TPT automáticamente
public class Material {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long idMaterial;

    private String nombre;
    private float precio;
    private int cantInventario;

    
}
