package mx.uam.ayd.proyecto.negocio;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import mx.uam.ayd.proyecto.datos.RepositorioEvento;
import mx.uam.ayd.proyecto.negocio.modelo.Evento;

@Service
public class ServicioContrato {

    private final RepositorioEvento repositorioEvento;

    @Autowired
    public ServicioContrato(RepositorioEvento repositorioEvento) {
        this.repositorioEvento = repositorioEvento;
    }

    /**
     * Obtiene las cláusulas del contrato.
     */
    public String obtenerClausulas(Evento evento) {

        if (evento == null) {
            throw new IllegalArgumentException("El evento no puede ser nulo.");
        }

        return evento.getClausulasExtras();
    }

    /**
     * Actualiza las cláusulas del contrato.
     */
    public boolean actualizarClausulas(Evento evento, String clausulas) {

        if (evento == null) {
            throw new IllegalArgumentException("El evento no puede ser nulo.");
        }

        evento.setClausulasExtras(clausulas);

        try {
            repositorioEvento.save(evento);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Marca el contrato como firmado.
     */
    public boolean firmarContrato(Evento evento) {

        if (evento == null) {
            throw new IllegalArgumentException("El evento no puede ser nulo.");
        }

        evento.setContratoFirmado(true);

        try {
            repositorioEvento.save(evento);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}