package mx.uam.ayd.proyecto.negocio.modelo;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;

/**
 * Entidad de negocio que representa una Cotización en el sistema.
 * Contiene la información técnica, financiera, los desglose de costos (materiales,
 * mano de obra, consumibles, etc.) y las relaciones con el cliente, evento y
 * detalles de materiales.
 */
@Entity
public class Cotizacion {

    /**
     * Identificador único de la cotización 
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idCotizacion;

    /**
     * Enumeración que define las categorías de tamaño del evento para la cotización.
     */
    public enum Tamano {
        /** Evento de escala pequeña. */
        PEQUENO, 
        /** Evento de escala mediana. */
        MEDIANO, 
        /** Evento de escala grande. */
        GRANDE
    }

    /**
     * Clasificación del tamaño del evento asociado a la cotización.
     */
    @Enumerated(EnumType.STRING)
    private Tamano tamano;

    /**
     * Monto del anticipo 
     */
    @Column(nullable = true)
    private float anticipo;

    /**
     * Monto total final calculado para la cotización
     */
    @Column(nullable = true)
    private float total;

    /**
     * Costo total costo de los materiales
     */
    @Column(nullable = true)
    private float totalMaterial;

    /**
     * Costo correspondiente al transporte o traslado.
     */
    @Column(nullable = true)
    private float transporte;

    /**
     * Monto adicional por costos extra imprevistos o especiales.
     */
    @Column(nullable = true)
    private float extra;

    /**
     * Costo asociado a materiales personalizados para el evento.
     */
    @Column(nullable = true)
    private float materialPersonalizado;

    /**
     * Costo o valor de los materiales proporcionados directamente al cliente.
     */
    @Column(nullable = true)
    private float materialCliente;

    /**
     * Costo asignado a la mano de obra requerida.
     */
    @Column(nullable = true)
    private float manoDeObra;

    /**
     * Porcentaje o monto de ganancia estimado para la cotización.
     */
    @Column(nullable = true)
    private float ganancia;

    /**
     * Costo calculado para insumos consumibles.
     */
    @Column(nullable = true)
    private float consumibles;

    /**
     * Estado de aprobación de la cotización por parte del cliente o administrador.
     */
    @Column(nullable = true)
    private boolean aprobada;

    /**
     * Evento asociado a esta cotización (Relación uno a uno bidireccional).
     */
    @OneToOne(mappedBy = "cotizacion", targetEntity = Evento.class, cascade = CascadeType.ALL, orphanRemoval = true)
    private Evento evento;

    /**
     * Lista de desglose de materiales o servicios asociados a esta cotización.
     */
    @OneToMany(mappedBy = "cotizacion", targetEntity = DetalleCotizacion.class, fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DetalleCotizacion> detalles = new ArrayList<>();

    /**
     * Cliente que solicita o es propietario de esta cotización.
     */
    @ManyToOne
    @JoinColumn(name = "idCliente")
    private Cliente cliente;

    /**
     * Constructor por defecto requerido por JPA.
     */
    public Cotizacion() {
    }

    // ----------- GETTERS Y SETTERS -----------

    /**
     * Obtiene la lista de detalles/materiales vinculados a la cotización.
     */
    public List<DetalleCotizacion> getDetalles() {
        return detalles;
    }

    /**
     * Obtiene el identificador único de la cotización.
     * 
     * @return ID de la cotización.
     */
    public Long getIdCotizacion() {
        return idCotizacion;
    }

    /**
     * Obtiene el monto total final de la cotización.
     * 
     * @return El total acumulado.
     */
    public float getTotal() {
        return total;
    }

    /**
     * Obtiene el subtotal por concepto de materiales.
     * 
     * @return El costo total de materiales.
     */
    public float getTotalMaterial() {
        return totalMaterial;
    }

    /**
     * Obtiene el costo extra de la cotización.
     * 
     * @return El monto extra.
     */
    public float getExtra() {
        return extra;
    }

    /**
     * Obtiene el costo asignado a consumibles.
     * 
     * @return El costo de consumibles.
     */
    public float getConsumibles() {
        return consumibles;
    }

    /**
     * Obtiene la clasificación de tamaño del evento.
     * 
     * @return Tamaño del evento de tipo 
     */
    public Tamano getTamano() {
        return tamano;
    }

    /**
     * Obtiene el costo por concepto de transporte.
     * 
     * @return El costo de transporte.
     */
    public float getTransporte() {
        return transporte;
    }

    /**
     * Obtiene el costo de materiales personalizados.
     * 
     * @return El costo de materiales personalizados.
     */
    public float getMaterialPersonalizado() {
        return materialPersonalizado;
    }

    /**
     * Obtiene el costo o valor de materiales aportados por el cliente.
     * 
     * @return El valor de los materiales del cliente.
     */
    public float getMaterialCliente() {
        return materialCliente;
    }

    /**
     * Obtiene el costo por mano de obra.
     * 
     * @return El valor de mano de obra.
     */
    public float getManoObra() {
        return manoDeObra;
    }

    /**
     * Obtiene el valor estimado de ganancia.
     * 
     * @return La ganancia de la cotización.
     */
    public float getGanancia() {
        return ganancia;
    }

    /**
     * Obtiene el estado de aprobación de la cotización.
     * 
     * @return {@code true} si está aprobada, {@code false} en caso contrario.
     */
    public boolean getAprobada() {
        return aprobada;
    }

    /**
     * Obtiene el monto del anticipo registrado.
     * 
     * @return El monto del anticipo.
     */
    public float getAnticipo() {
        return anticipo;
    }

    /**
     * Obtiene el evento asociado a la cotización.
     * 
     * @return Objeto {@link Evento}.
     */
    public Evento getEvento() {
        return evento;
    }

    /**
     * Obtiene el cliente asociado a la cotización.
     * 
     * @return Objeto {@link Cliente}.
     */
    public Cliente getCliente() {
        return cliente;
    }

    /**
     * Establece el costo de transporte.
     * 
     * @param transporte Costo de transporte.
     */
    public void setTransporte(float transporte) {
        this.transporte = transporte;
    }

    /**
     * Establece el costo de material personalizado.
     * 
     * @param materialPersonalizado Costo del material personalizado.
     */
    public void setMaterialPersonalizado(float materialPersonalizado) {
        this.materialPersonalizado = materialPersonalizado;
    }

    /**
     * Establece el costo de material proporcionado por el cliente.
     * 
     * @param materialCliente Valor del material del cliente.
     */
    public void setMaterialCliente(float materialCliente) {
        this.materialCliente = materialCliente;
    }

    /**
     * Establece el tamaño del evento.
     * 
     * @param tamano Tamaño del evento de tipo {@link Tamano}.
     */
    public void setTamano(Tamano tamano) {
        this.tamano = tamano;
    }

    /**
     * Establece el monto de cobros o costos extras.
     * 
     * @param extra Monto extra.
     */
    public void setExtra(float extra) {
        this.extra = extra;
    }

    /**
     * Establece el monto total final de la cotización.
     * 
     * @param total Monto total.
     */
    public void setTotal(float total) {
        this.total = total;
    }

    /**
     * Establece el subtotal por concepto de materiales.
     * 
     * @param totalMaterial Subtotal de materiales.
     */
    public void setTotalMaterial(float totalMaterial) {
        this.totalMaterial = totalMaterial;
    }

    /**
     * Establece el valor asignado a la ganancia.
     * 
     * @param ganancia Valor de la ganancia.
     */
    public void setGanancia(float ganancia) {
        this.ganancia = ganancia;
    }

    /**
     * Establece el costo asignado a consumibles.
     * 
     * @param consumibles Costo de insumos consumibles.
     */
    public void setConsumibles(float consumibles) {
        this.consumibles = consumibles;
    }

    /**
     * Establece el costo asignado a mano de obra.
     * 
     * @param manoDeObra Costo de mano de obra.
     */
    public void setManoObra(float manoDeObra) {
        this.manoDeObra = manoDeObra;
    }

    /**
     * Establece el estado de aprobación de la cotización.
     * 
     * @param aprobada {@code true} para aprobar la cotización, {@code false} para no aprobarla.
     */
    public void setAprobada(boolean aprobada) {
        this.aprobada = aprobada;
    }

    /**
     * Establece el identificador de la cotización.
     * 
     * @param idCotizacion ID único.
     */
    public void setIdCotizacion(Long idCotizacion) {
        this.idCotizacion = idCotizacion;
    }

    /**
     * Establece el cliente asociado a la cotización.
     * 
     * @param cliente Instancia de {@link Cliente}.
     */
    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    /**
     * Establece el monto del anticipo.
     * 
     * @param anticipo Monto del anticipo.
     */
    public void setAnticipo(float anticipo) {
        this.anticipo = anticipo;
    }

    /**
     * Establece la lista de detalles o materiales de la cotización.
     * 
     * @param detalles Lista de {@link DetalleCotizacion}.
     */
    public void setDetalles(List<DetalleCotizacion> detalles) {
        this.detalles = detalles;
    }

    /**
     * Establece el evento vinculado a esta cotización.
     * 
     * @param evento Instancia de {@link Evento}.
     */
    public void setEvento(Evento evento) {
        this.evento = evento;
    }
}