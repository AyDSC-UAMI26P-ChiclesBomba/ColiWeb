package mx.uam.ayd.proyecto.negocio.modelo;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;

/**
 * Entidad de Negocio Mobiliario
 * Se utiliza el método Tabla por Tipo (Table per Type, TPT)
*/
@Entity
public class Mobiliario extends Material {

    // Gracias al inheritance ya está automático el id del material

    private enum tipoMobiliario{

    }private enum tipoDano{
        TOTAL, PARCIAL, NINGUNO
    }

    private tipoMobiliario tipoMobiliario;
    private boolean estadoMobiliario;
    private float precioCompra;
    private float costoDanoTotal;
    private float costoDanoParcial;
    private tipoDano tipoDano;
    
}
