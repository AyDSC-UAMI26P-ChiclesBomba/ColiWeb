package mx.uam.ayd.proyecto.negocio.modelo;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

/**
 * Entidad de negocio para Globo
 * Se utiliza el método Tabla por Tipo (Table per Type, TPT)
 */
@Entity
public class Globo extends Material {
    // Gracias al inheritance ya está automático el id del material

    // Se hacen los enum necesarios
    public enum Color{
        ROJO, AZUL, VERDE, AMARILLO, ROSA, DORADO, PLATEADO, BLANCO, NEGRO, MORADO //...
    }public enum TipoGlobo{
        ESTANDAR, CROMADO, METALICO, BURBUJA, ESTAMPADO, NUMEROS, LATEX, MOLDEAR, FIGURAS
    }public enum Marca{
        QiAU, SEMPERTEX, GLOMEX, CIELO, PANDA, PAYASO, BALLONZONE, PARTYSTAR, NUEVAERAPARY, SHUAIANBALLONS
    }

    // Atritbutos de la entidad
    @Enumerated(EnumType.STRING)
    private Color color;
    private int medida;
    @Enumerated(EnumType.STRING)
    private TipoGlobo tipoGlobo;
    @Enumerated(EnumType.STRING)
    private Marca marca;

    // Métodos de la cotización como getters y setters
    
}
