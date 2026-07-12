package mx.uam.ayd.proyecto.negocio.modelo;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

/**
 * Entidad de negocio Pago
 */
@Entity
public class Pago {
    // ID
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idPago;

    // Se hacen los enum necesarios
    private enum metodoPago{
        EFECTIVO, TARJETA
    }

    // Atributos de la entidad
    private float monto;
    private LocalDateTime fechaPago;
    @Enumerated(EnumType.STRING)
    private metodoPago metodoPago;

    // Relación con Evento (sí guarda columna)
    @ManyToOne
    @JoinColumn(name = "idEvento")
    private Evento evento;
    
    // Métodos como setters y getters
    // ...
}
