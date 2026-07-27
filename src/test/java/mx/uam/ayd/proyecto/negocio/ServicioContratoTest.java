package mx.uam.ayd.proyecto.negocio;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import mx.uam.ayd.proyecto.datos.RepositorioEvento;
import mx.uam.ayd.proyecto.negocio.modelo.Evento;

@ExtendWith(MockitoExtension.class)
class ServicioContratoTest {

    @Mock
    private RepositorioEvento repositorioEvento;

    @Mock
    private Evento evento;

    private ServicioContrato servicioContrato;

    @BeforeEach
    void setUp() {
        servicioContrato = new ServicioContrato(repositorioEvento);
    }

    /*
     * Pruebas para obtenerClausulas()
     */

    @Test
    void obtenerClausulasDebeDevolverClausulasDelEvento() {

        when(evento.getClausulasExtras())
            .thenReturn("Cláusulas particulares del evento");

        String resultado =
            servicioContrato.obtenerClausulas(evento);

        assertEquals(
            "Cláusulas particulares del evento",
            resultado
        );
    }

    @Test
    void obtenerClausulasDebeDevolverPlantillaCuandoEventoNoTieneClausulas() {

        servicioContrato.actualizarPlantilla(
            "Plantilla general del contrato"
        );

        when(evento.getClausulasExtras())
            .thenReturn(null);

        String resultado =
            servicioContrato.obtenerClausulas(evento);

        assertEquals(
            "Plantilla general del contrato",
            resultado
        );
    }

    @Test
    void obtenerClausulasDebeDevolverPlantillaCuandoClausulasEstanVacias() {

        servicioContrato.actualizarPlantilla(
            "Plantilla general"
        );

        when(evento.getClausulasExtras())
            .thenReturn("   ");

        String resultado =
            servicioContrato.obtenerClausulas(evento);

        assertEquals(
            "Plantilla general",
            resultado
        );
    }

    @Test
    void obtenerClausulasDebeDevolverTextoVacioCuandoNoExistePlantilla() {

        when(evento.getClausulasExtras())
            .thenReturn(null);

        String resultado =
            servicioContrato.obtenerClausulas(evento);

        assertEquals("", resultado);
    }

    @Test
    void obtenerClausulasDebeLanzarExcepcionCuandoEventoEsNulo() {

        IllegalArgumentException excepcion =
            assertThrows(
                IllegalArgumentException.class,
                () -> servicioContrato.obtenerClausulas(null)
            );

        assertEquals(
            "El evento no puede ser nulo.",
            excepcion.getMessage()
        );
    }

    /*
     * Pruebas para actualizarClausulas()
     */

    @Test
    void actualizarClausulasDebeGuardarEvento() {

        servicioContrato.actualizarClausulas(
            evento,
            "  Nuevas cláusulas  "
        );

        verify(evento)
            .setClausulasExtras("Nuevas cláusulas");

        verify(repositorioEvento)
            .save(evento);
    }

    @Test
    void actualizarClausulasDebeLanzarExcepcionCuandoEventoEsNulo() {

        IllegalArgumentException excepcion =
            assertThrows(
                IllegalArgumentException.class,
                () -> servicioContrato.actualizarClausulas(
                    null,
                    "Cláusulas válidas"
                )
            );

        assertEquals(
            "El evento no puede ser nulo.",
            excepcion.getMessage()
        );

        verify(repositorioEvento, never())
            .save(evento);
    }

    @Test
    void actualizarClausulasDebeLanzarExcepcionCuandoClausulasSonNulas() {

        IllegalArgumentException excepcion =
            assertThrows(
                IllegalArgumentException.class,
                () -> servicioContrato.actualizarClausulas(
                    evento,
                    null
                )
            );

        assertEquals(
            "Las cláusulas no pueden estar vacías.",
            excepcion.getMessage()
        );

        verify(repositorioEvento, never())
            .save(evento);
    }

    @Test
    void actualizarClausulasDebeLanzarExcepcionCuandoClausulasEstanVacias() {

        assertThrows(
            IllegalArgumentException.class,
            () -> servicioContrato.actualizarClausulas(
                evento,
                "   "
            )
        );

        verify(repositorioEvento, never())
            .save(evento);
    }

    @Test
    void actualizarClausulasDebeLanzarIllegalStateCuandoRepositorioFalla() {

        doThrow(new RuntimeException("Error de base de datos"))
            .when(repositorioEvento)
            .save(evento);

        IllegalStateException excepcion =
            assertThrows(
                IllegalStateException.class,
                () -> servicioContrato.actualizarClausulas(
                    evento,
                    "Cláusulas válidas"
                )
            );

        assertEquals(
            "No fue posible guardar las cláusulas.",
            excepcion.getMessage()
        );

        assertTrue(excepcion.getCause()
            instanceof RuntimeException);
    }

    /*
     * Pruebas para actualizarPlantilla()
     */

    @Test
    void actualizarPlantillaDebeGuardarTextoRecortado() {

        servicioContrato.actualizarPlantilla(
            "  Plantilla actualizada  "
        );

        when(evento.getClausulasExtras())
            .thenReturn(null);

        String resultado =
            servicioContrato.obtenerClausulas(evento);

        assertEquals(
            "Plantilla actualizada",
            resultado
        );
    }

    @Test
    void actualizarPlantillaDebeLanzarExcepcionCuandoEsNula() {

        IllegalArgumentException excepcion =
            assertThrows(
                IllegalArgumentException.class,
                () -> servicioContrato.actualizarPlantilla(null)
            );

        assertEquals(
            "La plantilla no puede estar vacía.",
            excepcion.getMessage()
        );
    }

    @Test
    void actualizarPlantillaDebeLanzarExcepcionCuandoEstaVacia() {

        assertThrows(
            IllegalArgumentException.class,
            () -> servicioContrato.actualizarPlantilla("   ")
        );
    }

    /*
     * Pruebas para generarPDF()
     */

    @Test
    void generarPDFDebeCrearArchivo(
            @TempDir Path directorioTemporal)
            throws Exception {

        Path archivo =
            directorioTemporal.resolve("contrato.pdf");

        when(evento.getClausulasExtras())
            .thenReturn("Primera clausula\nSegunda clausula");

        when(evento.getTipoEvento())
            .thenReturn(null);

        when(evento.getFecha())
            .thenReturn(null);

        when(evento.getHora())
            .thenReturn(null);

        when(evento.getLugar())
            .thenReturn(null);

        when(evento.getDireccion())
            .thenReturn(null);

        servicioContrato.generarPDF(
            evento,
            archivo.toString()
        );

        assertTrue(Files.exists(archivo));
        assertTrue(Files.size(archivo) > 0);
    }

    @Test
    void generarPDFDebeAceptarDatosNulosDelEvento(
            @TempDir Path directorioTemporal)
            throws Exception {

        Path archivo =
            directorioTemporal.resolve(
                "contrato-datos-nulos.pdf"
            );

        when(evento.getClausulasExtras())
            .thenReturn("Clausulas del contrato");

        servicioContrato.generarPDF(
            evento,
            archivo.toString()
        );

        assertTrue(Files.exists(archivo));
        assertFalse(Files.size(archivo) == 0);
    }

    @Test
    void generarPDFDebeLanzarExcepcionCuandoEventoEsNulo() {

        IllegalArgumentException excepcion =
            assertThrows(
                IllegalArgumentException.class,
                () -> servicioContrato.generarPDF(
                    null,
                    "contrato.pdf"
                )
            );

        assertEquals(
            "El evento no puede ser nulo.",
            excepcion.getMessage()
        );
    }

    @Test
    void generarPDFDebeLanzarExcepcionCuandoRutaEsNula() {

        IllegalArgumentException excepcion =
            assertThrows(
                IllegalArgumentException.class,
                () -> servicioContrato.generarPDF(
                    evento,
                    null
                )
            );

        assertEquals(
            "La ruta del archivo no puede estar vacía.",
            excepcion.getMessage()
        );
    }

    @Test
    void generarPDFDebeLanzarExcepcionCuandoRutaEstaVacia() {

        assertThrows(
            IllegalArgumentException.class,
            () -> servicioContrato.generarPDF(
                evento,
                "   "
            )
        );
    }

    @Test
    void generarPDFDebeLanzarExcepcionCuandoNoHayClausulas(
            @TempDir Path directorioTemporal) {

        Path archivo =
            directorioTemporal.resolve(
                "contrato-sin-clausulas.pdf"
            );

        when(evento.getClausulasExtras())
            .thenReturn(null);

        IllegalStateException excepcion =
            assertThrows(
                IllegalStateException.class,
                () -> servicioContrato.generarPDF(
                    evento,
                    archivo.toString()
                )
            );

        assertEquals(
            "El contrato no contiene cláusulas.",
            excepcion.getMessage()
        );

        assertFalse(Files.exists(archivo));
    }

    @Test
    void generarPDFDebeLanzarIllegalStateCuandoNoPuedeGuardarArchivo(
            @TempDir Path directorioTemporal) {

        when(evento.getClausulasExtras())
            .thenReturn("Cláusulas válidas");

        /*
         * Se proporciona la ruta de un directorio en lugar
         * de la ruta de un archivo PDF.
         */
        IllegalStateException excepcion =
            assertThrows(
                IllegalStateException.class,
                () -> servicioContrato.generarPDF(
                    evento,
                    directorioTemporal.toString()
                )
            );

        assertEquals(
            "No fue posible generar el archivo PDF.",
            excepcion.getMessage()
        );
    }
}