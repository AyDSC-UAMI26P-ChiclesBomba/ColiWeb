package mx.uam.ayd.proyecto.presentacion.Publicacion;

import org.springframework.stereotype.Component;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * Ventana que muestra una publicación ya realizada.
 *
 * El contenido presentado en esta ventana es únicamente
 * de lectura y no puede ser modificado por el usuario.
 */
@Component
public class VentanaPublicacionFinal {

    /**
     * Escenario utilizado para mostrar la ventana.
     */
    private Stage escenario;

    /**
     * Muestra la publicación final en modo de solo lectura.
     *
     * @param mensaje contenido publicado
     */
    public void muestra(String mensaje) {

        escenario = new Stage();

        escenario.setTitle("Publicación realizada");

        /*
         * La ventana se abre como modal.
         */
        escenario.initModality(Modality.APPLICATION_MODAL);

        Label titulo = new Label("PUBLICACIÓN REALIZADA");

        titulo.setStyle(
                "-fx-font-size: 20px;"
                + "-fx-font-weight: bold;"
        );

        Label estado = new Label("Publicado internamente");

        estado.setStyle(
                "-fx-font-size: 13px;"
                + "-fx-font-weight: bold;"
                + "-fx-text-fill: #2e7d32;"
        );

        TextArea contenidoPublicacion = new TextArea();

        contenidoPublicacion.setText(
                mensaje != null ? mensaje : ""
        );

        /*
         * El usuario no puede modificar el contenido.
         */
        contenidoPublicacion.setEditable(false);
        contenidoPublicacion.setWrapText(true);
        contenidoPublicacion.setPrefSize(450, 260);

        Button botonCerrar = new Button("Cerrar");

        botonCerrar.setOnAction(
                evento -> escenario.close()
        );

        VBox contenedor = new VBox(
                15,
                titulo,
                estado,
                contenidoPublicacion,
                botonCerrar
        );

        contenedor.setAlignment(Pos.CENTER);
        contenedor.setPadding(new Insets(25));

        Scene escena = new Scene(
                contenedor,
                520,
                400
        );

        escenario.setScene(escena);
        escenario.setResizable(false);
        escenario.showAndWait();
    }
}