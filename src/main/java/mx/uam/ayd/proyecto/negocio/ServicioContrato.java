package mx.uam.ayd.proyecto.negocio;

import java.io.IOException;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.Standard14Fonts;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import mx.uam.ayd.proyecto.datos.RepositorioEvento;
import mx.uam.ayd.proyecto.negocio.modelo.Evento;

/**
 * Servicio encargado de la lógica de negocio de la HU-6.
 *
 * Permite:
 * - Obtener las cláusulas de un contrato.
 * - Actualizar las cláusulas de un evento específico.
 * - Actualizar temporalmente la plantilla general.
 * - Generar un archivo PDF real.
 *
 * La firma física del contrato no se administra aquí.
 */
@Service
public class ServicioContrato {

    /**
     * Repositorio utilizado para guardar cambios en Evento.
     */
    private final RepositorioEvento repositorioEvento;

    /**
     * Plantilla general conservada únicamente en memoria.
     *
     * Su contenido existe mientras la aplicación permanece abierta.
     * Al cerrar y volver a iniciar la aplicación, vuelve a estar vacía.
     */
    private String plantillaTemporal = "";

    /**
     * Constructor utilizado por Spring para inyectar
     * el repositorio de eventos.
     *
     * @param repositorioEvento repositorio de Evento
     */
    @Autowired
    public ServicioContrato(RepositorioEvento repositorioEvento) {
        this.repositorioEvento = repositorioEvento;
    }

    /**
     * Obtiene las cláusulas del contrato de un evento.
     *
     * Si el evento todavía no tiene cláusulas propias,
     * devuelve la plantilla general.
     *
     * @param evento evento cuyo contrato se desea consultar
     * @return cláusulas del evento o plantilla general
     */
    public String obtenerClausulas(Evento evento) {

        /*
         * Se comprueba que el evento exista.
         */
        if (evento == null) {
            throw new IllegalArgumentException(
                "El evento no puede ser nulo."
            );
        }

        /*
         * Se obtienen las cláusulas almacenadas en Evento.
         */
        String clausulas = evento.getClausulasExtras();

        /*
         * Si el evento no tiene cláusulas propias,
         * se devuelve la plantilla general.
         */
        if (clausulas == null || clausulas.isBlank()) {
            return obtenerPlantilla();
        }

        /*
         * Si ya existen cláusulas, se devuelven.
         */
        return clausulas;
    }

    /**
     * Actualiza las cláusulas del contrato de un evento específico.
     *
     * Este método no modifica la plantilla general.
     *
     * @param evento evento que será actualizado
     * @param clausulas nuevas cláusulas
     */
    public void actualizarClausulas(
            Evento evento,
            String clausulas) {

        /*
         * Se comprueba que exista el evento.
         */
        if (evento == null) {
            throw new IllegalArgumentException(
                "El evento no puede ser nulo."
            );
        }

        /*
         * Se comprueba que las cláusulas sean válidas.
         */
        if (!validarClausulas(clausulas)) {
            throw new IllegalArgumentException(
                "Las cláusulas no pueden estar vacías."
            );
        }

        /*
         * Se guardan las cláusulas en el objeto Evento.
         */
        evento.setClausulasExtras(clausulas.trim());

        try {

            /*
             * save() actualiza el evento en la base de datos.
             */
            repositorioEvento.save(evento);

        } catch (Exception e) {

            /*
             * Se informa que ocurrió un error de persistencia.
             */
            throw new IllegalStateException(
                "No fue posible guardar las cláusulas.",
                e
            );
        }
    }

    /**
     * Actualiza la plantilla general de contratos.
     *
     * La plantilla se utilizará en los contratos siguientes
     * que todavía no tengan cláusulas propias.
     *
     * Los contratos anteriores no se modifican.
     *
     * @param nuevaPlantilla nuevo contenido de la plantilla
     */
    public void actualizarPlantilla(String nuevaPlantilla) {

        /*
         * Se valida el contenido antes de conservarlo.
         */
        if (!validarClausulas(nuevaPlantilla)) {
            throw new IllegalArgumentException(
                "La plantilla no puede estar vacía."
            );
        }

        /*
         * La plantilla se guarda solamente en memoria.
         *
         * No se crea ni se modifica ningún archivo del proyecto.
         * Cuando la aplicación se cierre, este valor se perderá.
         */
        plantillaTemporal = nuevaPlantilla.trim();
    }

    /**
     * Obtiene la plantilla general almacenada en el archivo.
     *
     * @return contenido de la plantilla o texto vacío si aún no existe
     */
    private String obtenerPlantilla() {

        /*
         * Devuelve la plantilla almacenada durante la ejecución actual.
         *
         * Si todavía no se ha guardado una plantilla, devuelve texto vacío.
         */
        return plantillaTemporal;
    }

    /**
     * Genera un archivo PDF real con los datos del contrato.
     *
     * @param evento evento cuyo contrato se desea generar
     * @param rutaArchivo ruta donde se guardará el PDF
     */
    public void generarPDF(
            Evento evento,
            String rutaArchivo) {

        /*
         * Se valida el evento.
         */
        if (evento == null) {
            throw new IllegalArgumentException(
                "El evento no puede ser nulo."
            );
        }

        /*
         * Se valida la ruta del archivo.
         */
        if (rutaArchivo == null || rutaArchivo.isBlank()) {
            throw new IllegalArgumentException(
                "La ruta del archivo no puede estar vacía."
            );
        }

        /*
         * Se obtienen las cláusulas que aparecerán en el PDF.
         */
        String clausulas = obtenerClausulas(evento);

        /*
         * No se puede generar un contrato sin cláusulas.
         */
        if (!validarClausulas(clausulas)) {
            throw new IllegalStateException(
                "El contrato no contiene cláusulas."
            );
        }

        /*
         * try-with-resources cierra automáticamente el documento
         * cuando termina la operación.
         */
        try (PDDocument documento = new PDDocument()) {

            /*
             * Se crea una página nueva y se agrega al documento.
             */
            PDPage pagina = new PDPage();
            documento.addPage(pagina);

            /*
             * PDPageContentStream permite escribir dentro de la página.
             */
            try (PDPageContentStream contenido =
                    new PDPageContentStream(documento, pagina)) {

                /*
                 * Fuente estándar de PDFBox.
                 */
                PDType1Font fuenteTitulo = new PDType1Font(
                    Standard14Fonts.FontName.HELVETICA_BOLD
                );

                PDType1Font fuenteNormal = new PDType1Font(
                    Standard14Fonts.FontName.HELVETICA
                );

                /*
                 * Inicia la escritura del encabezado.
                 */
                contenido.beginText();
                contenido.setFont(fuenteTitulo, 16);
                contenido.newLineAtOffset(70, 750);
                contenido.showText("CONTRATO DE EVENTO");
                contenido.endText();

                /*
                 * Escribe la información básica del evento.
                 */
                contenido.beginText();
                contenido.setFont(fuenteNormal, 11);
                contenido.setLeading(16);
                contenido.newLineAtOffset(70, 715);

                contenido.showText(
                    "Tipo de evento: " + valorSeguro(evento.getTipoEvento())
                );
                contenido.newLine();

                contenido.showText(
                    "Fecha: " + valorSeguro(evento.getFecha())
                );
                contenido.newLine();

                contenido.showText(
                    "Hora: " + valorSeguro(evento.getHora())
                );
                contenido.newLine();

                contenido.showText(
                    "Lugar: " + valorSeguro(evento.getLugar())
                );
                contenido.newLine();

                contenido.showText(
                    "Dirección: " + valorSeguro(evento.getDireccion())
                );
                contenido.endText();

                /*
                 * Escribe el título de la sección de cláusulas.
                 */
                contenido.beginText();
                contenido.setFont(fuenteTitulo, 13);
                contenido.newLineAtOffset(70, 610);
                contenido.showText("CLÁUSULAS");
                contenido.endText();

                /*
                 * Escribe las cláusulas línea por línea.
                 *
                 * Se separa el texto por saltos de línea para evitar
                 * intentar escribir todo en una sola línea.
                 */
                contenido.beginText();
                contenido.setFont(fuenteNormal, 10);
                contenido.setLeading(14);
                contenido.newLineAtOffset(70, 585);

                String[] lineas = clausulas.split("\\R");

                for (String linea : lineas) {

                    /*
                     * PDFBox no ajusta automáticamente el texto.
                     * Esta versión escribe cada salto de línea recibido.
                     */
                    contenido.showText(limpiarTextoPDF(linea));
                    contenido.newLine();
                }

                contenido.endText();
            }

            /*
             * Guarda el documento en la ruta seleccionada.
             */
            documento.save(rutaArchivo);

        } catch (IOException e) {

            /*
             * Se lanza una excepción si el PDF no puede crearse.
             */
            throw new IllegalStateException(
                "No fue posible generar el archivo PDF.",
                e
            );
        }
    }

    /**
     * Comprueba que las cláusulas tengan contenido.
     *
     * @param clausulas texto que se desea validar
     * @return true si el texto es válido; false en caso contrario
     */
    private boolean validarClausulas(String clausulas) {

        return clausulas != null && !clausulas.isBlank();
    }

    /**
     * Convierte un valor nulo en texto vacío.
     *
     * Esto evita que aparezca la palabra null dentro del PDF.
     *
     * @param valor dato que será convertido a texto
     * @return representación segura del dato
     */
    private String valorSeguro(Object valor) {

        if (valor == null) {
            return "";
        }

        return valor.toString();
    }

    /**
     * Limpia caracteres que las fuentes estándar de PDFBox
     * podrían no mostrar correctamente.
     *
     * @param texto texto original
     * @return texto preparado para escribirse en el PDF
     */
    private String limpiarTextoPDF(String texto) {

        if (texto == null) {
            return "";
        }

        return texto
            .replace("á", "a")
            .replace("é", "e")
            .replace("í", "i")
            .replace("ó", "o")
            .replace("ú", "u")
            .replace("Á", "A")
            .replace("É", "E")
            .replace("Í", "I")
            .replace("Ó", "O")
            .replace("Ú", "U")
            .replace("ñ", "n")
            .replace("Ñ", "N");
    }
}