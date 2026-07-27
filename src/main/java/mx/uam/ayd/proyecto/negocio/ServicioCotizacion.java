package mx.uam.ayd.proyecto.negocio;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import mx.uam.ayd.proyecto.datos.RepositorioCliente;
import mx.uam.ayd.proyecto.datos.RepositorioCotizacion;
import mx.uam.ayd.proyecto.datos.RepositorioEvento;
import mx.uam.ayd.proyecto.negocio.modelo.Cliente;
import mx.uam.ayd.proyecto.negocio.modelo.Cotizacion;
import mx.uam.ayd.proyecto.negocio.modelo.DetalleCotizacion;
import mx.uam.ayd.proyecto.negocio.modelo.Evento;
import mx.uam.ayd.proyecto.negocio.modelo.Cotizacion.Tamano;

@Service
public class ServicioCotizacion {
    
    RepositorioCotizacion repositorioCotizacion;
    RepositorioEvento repositorioEvento;
    RepositorioCliente repositorioCliente;

    @Autowired
    public ServicioCotizacion(RepositorioCotizacion repositorioCotizacion,RepositorioCliente repositorioCliente, RepositorioEvento repositorioEvento ){
        this.repositorioCotizacion = repositorioCotizacion;
        this.repositorioCliente = repositorioCliente;
        this.repositorioEvento = repositorioEvento;
    }

    public Cotizacion actualizaDetallesCotizacion(Float transporte, Float materialPersonalizado, Float materialCliente, Tamano tamano, String detalles, List<DetalleCotizacion> listaMaterialSeleccionado, Cotizacion cotizacion, Evento evento){

        cotizacion.setTransporte(transporte);
        cotizacion.setMaterialPersonalizado(materialPersonalizado);
        cotizacion.setMaterialCliente(materialCliente);
        cotizacion.setTamano(tamano);
        evento.setDetalles(detalles);
        float costototal = costoTotalMaterial(listaMaterialSeleccionado);
        cotizacion.setTotalMaterial(costototal);
        float costoextra = costoExtra(transporte, materialPersonalizado, materialCliente);
        cotizacion.setExtra(costoextra);
        float costoconsumibles = costoConsumibles(tamano);
        cotizacion.setConsumibles(costoconsumibles);
        float costomanoora = costoManoObra(tamano);
        cotizacion.setManoObra(costomanoora);
        float costoganancia = costoGanancia(tamano);
        cotizacion.setGanancia(costoganancia);
        float total = costoTotal(costototal, costoextra, costoconsumibles, costomanoora, costoganancia);
        cotizacion.setTotal(total);

        return cotizacion;
    }

    private float costoTotalMaterial(List<DetalleCotizacion> listaMaterialSeleccionado){
        
        float totalMaterial=0;
        for (DetalleCotizacion detalle : listaMaterialSeleccionado){
            int cantidad = detalle.getCantidad();
            float precio = detalle.getMaterial().getPrecio();
            totalMaterial += (cantidad * precio);
        }
        return totalMaterial;
    }

    private float costoExtra(Float transporte, Float materialPersonalizado, Float materialCliente){
        return transporte+materialPersonalizado+materialCliente;
    }

    private float costoConsumibles(Tamano tamano){
        if (tamano == Tamano.PEQUENO){
            return 206;
        }else if(tamano == Tamano.MEDIANO){
            return 402;
        }
        return 620;
    }

    private float costoManoObra(Tamano tamano){
        if(tamano == Tamano.GRANDE){
            return 2400;
        }else if(tamano == Tamano.MEDIANO){
            return 1500;
        }
        return 1000;
    }

    private float costoGanancia(Tamano tamano){
        if (tamano == Tamano.GRANDE){
            return 2400;
        }else if(tamano == Tamano.MEDIANO){
            return 1500;
        }
            return 1000;
    }

    private float costoTotal(float costototal, float costoextra, float costoconsumibles, float costomanoora, float costoganancia){
            return costototal+costoextra+costoconsumibles+costomanoora+costoganancia;
    }

    public float copiaTotal(Cotizacion cotizacion){
        return cotizacion.getTotal();
    }

    public boolean aprobarCotizacion(Cotizacion cotizacion, boolean estado){
        cotizacion.setAprobada(estado);
        if(estado == true){
            return true;
        }
        return false;
    }

    public boolean borraCotizazion(Cotizacion cotizacion){
        if(cotizacion == null){
            System.err.println("La cotizacion no existe ");
            return false;
        }
        try {
            repositorioCotizacion.delete(cotizacion);
            return true;
        } catch (Exception e) {
            System.err.println("No se encontró la cotizacion. "+e.getMessage());
            return false;
        }
    }


    public Evento recuperaEvento(Cotizacion cotizacion){
        return repositorioEvento.findByCotizacion(cotizacion);
    }
    public Cliente recuperaCliente(Cotizacion cotizacion){
        return repositorioCliente.findByCotizaciones(cotizacion);
    }
}