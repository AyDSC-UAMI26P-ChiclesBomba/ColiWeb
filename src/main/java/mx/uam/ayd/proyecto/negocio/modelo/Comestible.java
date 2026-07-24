package mx.uam.ayd.proyecto.negocio.modelo;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

/**
 * Entidad de negocio para Comestible
 * Se utiliza el método Tabla por Tipo (Table per Type, TPT)
 */
@Entity
public class Comestible extends Material{
    
    // Gracias al inheritance ya está automático el id del material

    // Se hacen los enum necesarios
    public enum TipoComestible{
        DULCE, SALADO, BOTANAS
    }

    // Atributos de la entidad
    @Enumerated(EnumType.STRING) // Se utiliza para que la BD guarde el valor en lugar de algún número que haga referencia al valor del enum
    private TipoComestible tipoComestible;

    // Métodos de la cotización como getters y setters
    // ...
}
