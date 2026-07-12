package mx.uam.ayd.proyecto.negocio.modelo;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;

/**
 * Entidad de negocio Material
 */
@Entity
@Inheritance(strategy = InheritanceType.JOINED) // Es para crear una conversión para la herencia TPT automáticamente
public class Material {
    // ID
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idMaterial;

    // Atributos de la entidad y de todas sus entidades hijas
    private String nombre;
    private float precio;
    private int cantInventario;

    // Métodos de la cotización como getters y setters
    // ...
}
