package mx.uam.ayd.proyecto.negocio.modelo;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

/**
 * Entidad de negocio para MaterialDecorativo
 * Se utiliza el método Tabla por Tipo (Table per Type, TPT)
 */
@Entity
public class MaterialDecorativo extends Material {
    // Gracias al inheritance ya está automático el id del material

    // Se hacen los enum necesarios
    public enum TipoDecoracion{
        MARIPOSAS, LENTEJUELAS, PAPELCHINA, CONFETIS, BOLSASDECELOFAN, VASODULCERO, PALITOSDEMADERA, LISTONPLASTICO
    }

    // Atributos de la entidad
    @Enumerated(EnumType.STRING)
    private TipoDecoracion tipoDecoracion;

    // Métodos de la cotización como getters y setters
    // ...
}
