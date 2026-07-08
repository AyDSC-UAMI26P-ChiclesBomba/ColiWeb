package mx.uam.ayd.proyecto.negocio.modelo;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;

@Entity
public class DetalleCotizacion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long idDetalle;

    private int cantidad;
    private float costo;
    private boolean preciosCompletos;

    // Relación con Cotizacion, donde se guarda la columna "idCotizacion"
    @ManyToOne
    @JoinColumn(name="idCotizacion")
    private Cotizacion cotizacion;

    @ManyToOne
    @JoinColumn(name="idMaterial")
    private Material material;
}
