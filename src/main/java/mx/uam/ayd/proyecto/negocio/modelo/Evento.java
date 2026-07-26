package mx.uam.ayd.proyecto.negocio.modelo;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import javax.naming.directory.InvalidAttributeIdentifierException;

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
 * Entidad de negocio Evento
 */
@Entity
public class Evento {
    // ID
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idEvento;

    // Se hacen los enum necesarios
    public enum TipoEvento {
        BODA, ESCOLAR, CUMPLEANOS
    }
    public enum EstadoEvento {
        BORRADOR, CONFIRMADO, FINALIZADO
    }
    public enum EstadoPago {
        PENDIENTE, LIQUIDADO
    }

    // Atributos de la entidad
    @Enumerated(EnumType.STRING)
    private TipoEvento tipoEvento;
    private LocalDate fecha;
    private LocalTime hora;
    @Column(nullable = true)
    private String lugar;
    @Column(nullable = true)
    private String referencias;
    private String direccion;
    private float totalPagado;
    @Enumerated(EnumType.STRING)
    private EstadoEvento estadoEvento;
    @Column(nullable = true)
    private String detalles;
    @Column(nullable = true)
    private String visualRecinto;
    @Enumerated(EnumType.STRING)
    private EstadoPago estadoPago;
    @Column(nullable = true)
    private String clausulasExtras;
    private boolean contratoFirmado;
    
    /**
     * Permite la creación de la columna idCotizacion en la tabla Evento
     * Se crea cotizacion para dar esa relación a la entidad Cotizacion
     */
    @OneToOne
    @JoinColumn(name = "idCotizacion")
    private Cotizacion cotizacion;

    /**
     * Enlaza a Evento con sus pagos por medio del id establecido en Pago
     */
    @OneToMany(mappedBy = "evento", targetEntity = Pago.class, fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Pago> pagos;

    /**
     * Crea en Evento el idCliente para relacionarlo con Cliente
     */
    @ManyToOne
    @JoinColumn(name = "idCliente")
    private Cliente cliente;


    // ----------- MÉTODOS -----------
    // Constructor
    public Evento(){}
    public Evento(TipoEvento tipoEvento, LocalDate fecha, LocalTime hora, String lugar, String referencias, String direccion, String detalles, String visualRecinto, Cotizacion cotizacion, Cliente cliente) {
        this.fecha = fecha;
        this.tipoEvento = tipoEvento;
        this.hora = hora;
        this.lugar = lugar;
        this.direccion = direccion;
        this.referencias = referencias;
        this.visualRecinto = visualRecinto;
        this.detalles = detalles;
        this.estadoEvento = EstadoEvento.BORRADOR;
        this.estadoPago = EstadoPago.PENDIENTE;
        this.totalPagado = 0;
        this.contratoFirmado = false;

        this.cotizacion = cotizacion;
        this.cliente = cliente;
    }

    // getters
    public Long getIdEvento() {
        return this.idEvento;
    }
    public EstadoEvento getEstadoEvento(){
        return this.estadoEvento;
    }public Cotizacion getCotizacion(){
        return cotizacion;
    }public TipoEvento getTipoEvento(){
        return tipoEvento;
    }public LocalDate getFecha(){
        return fecha;
    }public LocalTime getHora(){
        return hora;
    }public String getLugar(){
        return lugar;
    }public String getReferencias(){
        return referencias;
    }public String getDireccion(){
        return direccion;
    }public float getTotalPagado(){
        return totalPagado;
    }public String getDetalles(){
        return detalles;
    }public String getVisualRecinto(){
        return visualRecinto;
    }public EstadoPago getEstadoPago(){
        return estadoPago;
    }public String getClausulasExtras(){
        return clausulasExtras;
    }public boolean getContratoFirmado(){
        return contratoFirmado;
    }public Cliente getCliente(){
        return cliente; 
    }

    // setters
    public void setEstadoEvento(EstadoEvento edoEvento){
        this.estadoEvento = edoEvento;
    }public void setCotizacion(Cotizacion cotizacion) throws Exception{
        if(cotizacion.getIdCotizacion() == null) throw new InvalidAttributeIdentifierException("La cotización ingresada no tiene id lleno (no existe en su repositorio");
        this.cotizacion = cotizacion;
    }public void setTipoEvento(TipoEvento tipoEvento){
        this.tipoEvento = tipoEvento;
    }public void setFecha(LocalDate fecha){
        this.fecha = fecha;
    }public void setHora(LocalTime hora){
        this.hora = hora;
    }public void setLugar(String lugar){
        this.lugar = lugar;
    }public void setReferencias(String referencias){
        this.referencias = referencias;
    }public void setDireccion(String direccion){
        this.direccion = direccion;
    }public void setTotalPagado(float totalPagado){
        this.totalPagado = totalPagado;
    }public void setDetalles(String detalles){
        this.detalles = detalles;
    }public void setVisualRecinto(String visualRecinto){
        this.visualRecinto = visualRecinto;
    }public void setEstadoPago(EstadoPago estadoPago){
        this.estadoPago = estadoPago;
    }public void setClausulasExtras(String clausulas){
        this.clausulasExtras = clausulas;
    }public void setContratoFirmado(boolean firma){
        this.contratoFirmado = firma;
    }public void setCliente(Cliente cliente){
        this.cliente = cliente;
    }

    @Override
    public String toString(){
        return fecha+" "+tipoEvento;
    }
}
