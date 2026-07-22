package mx.uam.ayd.proyecto.presentacion.calendario;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.springframework.stereotype.Component;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import mx.uam.ayd.proyecto.negocio.modelo.Evento;
import mx.uam.ayd.proyecto.negocio.modelo.Evento.EstadoEvento;

@Component
public class VentanaCalendario {
    
    private Stage stage;
    private ControlCalendario control;
    private boolean initialized = false;
	private DateTimeFormatter formatoMes = DateTimeFormatter.ofPattern("MMMM, yyyy", new Locale("es", "MX"));
	private DateTimeFormatter formatoDia = DateTimeFormatter.ofPattern("dd, MMMM, yyyy", new Locale("es", "MX"));

	// Elaboración del Mes
	@FXML
	private Label mesAnio;
	@FXML
	private ToggleButton lunes1;
	@FXML
	private ToggleButton martes1;
    @FXML
	private ToggleButton miercoles1;
    @FXML
	private ToggleButton jueves1;
    @FXML
	private ToggleButton viernes1;
    @FXML
	private ToggleButton sabado1;
    @FXML
	private ToggleButton domingo1;
    @FXML
	private ToggleButton lunes2;
    @FXML
	private ToggleButton martes2;
    @FXML
	private ToggleButton miercoles2;
    @FXML
	private ToggleButton jueves2;
    @FXML
	private ToggleButton viernes2;
    @FXML
	private ToggleButton sabado2;
    @FXML
	private ToggleButton domingo2;
    @FXML
	private ToggleButton lunes3;
    @FXML
	private ToggleButton martes3;
    @FXML
	private ToggleButton miercoles3;
    @FXML
	private ToggleButton jueves3;
    @FXML
	private ToggleButton viernes3;
    @FXML
	private ToggleButton sabado3;
    @FXML
	private ToggleButton domingo3;
    @FXML
	private ToggleButton lunes4;
    @FXML
	private ToggleButton martes4;
    @FXML
	private ToggleButton miercoles4;
    @FXML
	private ToggleButton jueves4;
    @FXML
	private ToggleButton viernes4;
    @FXML
	private ToggleButton sabado4;
    @FXML
	private ToggleButton domingo4;
    @FXML
	private ToggleButton lunes5;
    @FXML
	private ToggleButton martes5;
    @FXML
	private ToggleButton miercoles5;
    @FXML
	private ToggleButton jueves5;
    @FXML
	private ToggleButton viernes5;
    @FXML
	private ToggleButton sabado5;
    @FXML
	private ToggleButton domingo5;
	@FXML
	private ToggleButton lunes6;
    @FXML
	private ToggleButton martes6;
    @FXML
	private ToggleButton miercoles6;
    @FXML
	private ToggleButton jueves6;
    @FXML
	private ToggleButton viernes6;
    @FXML
	private ToggleButton sabado6;
    @FXML
	private ToggleButton domingo6;
    
	// Mostrar los tres próximos eventos
	@FXML
	private Label proxFecha1;
	@FXML
	private Label proxNombre1;
	@FXML
	private Rectangle proxEstado1;
	@FXML
	private Label proxFecha2;
	@FXML
	private Label proxNombre2;
	@FXML
	private Rectangle proxEstado2;
	@FXML
	private Label proxFecha3;
	@FXML
	private Label proxNombre3;
	@FXML
	private Rectangle proxEstado3;

	// Información sobre el evento seleccionado
	@FXML
	private VBox detallesEvento;
	@FXML
	private Label eventoSeleccionado;
	@FXML
	private Label horaSeleccionado;
	@FXML
	private Label estadoSeleccionado;
	@FXML
	private Label ubicacionSeleccionado;
	@FXML
	private Label montoSeleccionado;
	@FXML
	private Rectangle rectanguloSeleccionado;
	@FXML
	private Label clienteSeleccionado;
	@FXML
	private Label pagoSeleccionado;

	// Información sobre el evento finalizado seleccionado
	@FXML
	private VBox detallesFinalizado;
	@FXML
	private Label eventoFinalizado;
	@FXML
	private Label horaFinalizado;
	@FXML
	private Label ubicacionFinalizado;
	@FXML
	private Label clienteFinalizado;
	@FXML
	private Label montoFinalizado;
	@FXML
	private VBox proximosEventos;

	private Label errorFechaOcupada;

	// Botones
	@FXML
	private Button antMes;
	@FXML
	private Button sigMes;
	@FXML
	private Button continuar;
	@FXML
	private Button cotizacion;
	@FXML
	private Button gestion;
	@FXML
	private Button pagos;
	@FXML
	private Button compartir;
	@FXML
	private Button liquidacion;
	@FXML
	private Button mobiliario;


    public VentanaCalendario(){}

    /**
	 * Initialize UI components on the JavaFX application thread
	 */
	private void initializeUI() {
		if (initialized) {
			return;
		}
		
		// Create UI only if we're on JavaFX thread
		if (!Platform.isFxApplicationThread()) {
			Platform.runLater(this::initializeUI);
			return;
		}
		
		try {
			stage = new Stage();
			stage.setTitle("ColiWeb");
			stage.getIcons().add(new javafx.scene.image.Image(getClass().getResourceAsStream("/img/logo.jpeg")));
			
			// Load FXML
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/ventana-calendario.fxml"));
			loader.setController(this);
			Scene scene = new Scene(loader.load(), 1024, 768);
			scene.getStylesheets().add(getClass().getResource("/css/estilos.css").toExternalForm());
			
			stage.setScene(scene);
			stage.setMaximized(true);
			
			initialized = true;
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

    /**
	 * Establece el controlador asociado a esta ventana
	 * 
	 * @param control El controlador asociado
	 */
	public void setControlCalendario(ControlCalendario control) {
		this.control = control;
	}

	/**
	 * Muestra la pantalla principal del calendario, llamando los métodos de los respectivos elementos, el calendario, eventos próximos y detalles sobre los eventos
	 * @param eventos Es la lista de eventos que existen en el mes, utilizado por el calendario
	 * @param eventosTotales Es la lista de eventos totales, utilizado por los proximos eventos
	 * @param diaActual Es el día del mes en el que estamos parados, utilizado por el calendario
	 * @param diaLimite Es el día límite del que ya no se pueden hacer eventos antes del mismo, utilizado por el calendario
	 */
    public void muestra(List<Evento> eventos, List<Evento> eventosTotales, LocalDate diaActual, LocalDate diaLimite){
        if (!Platform.isFxApplicationThread()) {
			Platform.runLater(() -> this.muestra(eventos, eventosTotales, diaActual, diaLimite));
			return;
		}
		initializeUI();

		//-----------MUESTREO MENU LATERAL-----------\\
		muestraProximosEventos(eventosTotales);

		//-------------MUESTREO CALENDARIO-------------\\
		List<Evento> listaEventos = new ArrayList<>(eventos); // Se crea una lista provisional en otro apartado de memoria puesto que se modificará dentro de la función y no queremos que la mutabilidad de las listas nos perjudique
		muestraCalendario(listaEventos, diaActual, diaLimite);

		stage.show();
    }

	/**
	 * Es el encargado de evaluar fecha por fecha y evento por evento para determinar cómo se debe de mostrar en la pantalla del calendario
	 * @param eventos son los eventos que pertenecen al mes que estamos visualizando
	 * @param diaActual es el día que actualmente es
	 * @param diaLimite es el día límite para reservar eventos
	 */
	private void muestraCalendario(List<Evento> eventos, LocalDate diaActual, LocalDate diaLimite) {
		// Se revisa a partir de qué día de la semana inicia el mes
		// Dependiendo del día, se inicia en cierto Toggle Button y los anteriores a él son invisibilisados
		
		// Se crea un arreglo que guarde todos los Toggle que hay para poder recorrerlos en for
		ToggleButton[] diasCalendario = new ToggleButton[]{
			lunes1, martes1, miercoles1, jueves1, viernes1, sabado1, domingo1,
			lunes2, martes2, miercoles2, jueves2, viernes2, sabado2, domingo2,
			lunes3, martes3, miercoles3, jueves3, viernes3, sabado3, domingo3,
			lunes4, martes4, miercoles4, jueves4, viernes4, sabado4, domingo4,
			lunes5, martes5, miercoles5, jueves5, viernes5, sabado5, domingo5,
			lunes6, martes6, miercoles6, jueves6, viernes6, sabado6, domingo6
		};

		// Resetea todos los valores de los toggle para cuando se cambia entre meses, no guarde ningun estilo recesivo de anteriormente y lo haga desde cero
		for (ToggleButton diaCalendario : diasCalendario) {
			diaCalendario.setVisible(true);
			diaCalendario.setDisable(false);
			diaCalendario.setSelected(false);
			diaCalendario.setUserData(null);
			diaCalendario.getStyleClass().removeAll("dia-base", "dia-calendario-confirmado", "dia-calendario-finalizado", "dia-calendario-borrador");
			diaCalendario.getStyleClass().addAll("toggle-button", "dia-base");
		}


		// Se da el formato para el texto que da el mes. utilizando uno de los declarados al inicio de la ventana
		String mes = diaActual.format(formatoMes);
		mesAnio.setText(mes.substring(0,1).toUpperCase()+mes.substring(1).toLowerCase()); // Se toma el texto y se pone la primera letra mayúscula y el resto minúsulas
		mesAnio.setUserData(diaActual);

		LocalDate dia = diaActual.withDayOfMonth(1); // Regresa el primer día del mismo mes que diaActual, se irá incrementando conforme se recorren los días

		// Obtengo el valor del número que corresponde al nombre del primer día del mes, esto aprovechando que getDayOfWeek es un ENUM
		// Lunes = 0, Martes = 1, Miercoles = 2, Jueves = 3, Viernes = 4, Sabado = 5, Domingo = 6
		int inicioPrimerDiaMes = dia.getDayOfWeek().ordinal();

		// Se recorre el arreglo de los botones del calendario
		int i = 0; // Es una variable que se asegurará de inhabilitar los días justos para que el mes inicie en el día correcto
		Evento eventoEncontradoFecha = null; // Es el espacio que guardará al evento que se encuentre en la fecha que se asigna al toggle
		for(ToggleButton diaCalendario : diasCalendario){
			
			// Evalúa que i no sea menor al día que debe iniciar y que el mes no haya cambiado por aumentar demasiados días
			if(i<inicioPrimerDiaMes || dia.getMonthValue()!=diaActual.getMonthValue()){
				System.out.println("Toggle vacío "+i);
				diaCalendario.setDisable(true);
				diaCalendario.setVisible(false);
				i++; // Aumenta i, una vez se deje de cumplir la condición, se deja de aumentar (es innecesario)
			}
			else{
				System.out.println("Entra al else de los toggle vacíos, empieza a pintar");
				// Siendo que ya estamos en un día que sí se mostrará, se toma el día del mes en el que nos encontramos y se muestra
				diaCalendario.setText(String.valueOf(dia.getDayOfMonth()));
				
				eventoEncontradoFecha = null;
				// Evalúamos si algún evento está en la fecha que estamos evaluando
				for(Evento evento : eventos){
					if(evento.getFecha().equals(dia)){
						eventoEncontradoFecha = evento; // En caso de que se encuentre, se da el valor a eventoEcontradoFecha
						eventos.remove(evento); // Eliminamos el valor de evento para evitar hacer una iteración sobre un evento que ya tuvo su fecha
						break;
					}
				}

				if(eventoEncontradoFecha != null){
					// En caso de que un evento sí sea en la fecha, se da el formato al toogle:
					// Se da el formato específico en caso de que haya habido evento en esa fecha
					diaCalendario.getStyleClass().remove("dia-base");
					if(eventoEncontradoFecha.getEstadoEvento()==EstadoEvento.CONFIRMADO){
						diaCalendario.getStyleClass().add("dia-calendario-confirmado");
					}else if(eventoEncontradoFecha.getEstadoEvento()==EstadoEvento.FINALIZADO){
						diaCalendario.getStyleClass().add("dia-calendario-finalizado");
					}else if(eventoEncontradoFecha.getEstadoEvento()==EstadoEvento.BORRADOR){
						diaCalendario.getStyleClass().add("dia-calendario-borrador");
					}
					// Se dan también datos para cómo debe actuar el toogle
					diaCalendario.setUserData(eventoEncontradoFecha); // Se asigna que ese toggle guardará el valor del evento que se encontró en esa fecha
					System.out.println("Fecha con evento: "+dia+" evento: "+eventoEncontradoFecha);
				}else if(dia.isBefore(diaLimite)){ // Si no había evento, se evalúa si es una fecha disponible, si no, se bloquea
					System.out.println("Fecha deshabilitada: "+dia+". El día límite es: "+diaLimite);
					diaCalendario.setDisable(true); // Se deshabilita el toogle en esa posición
				}else{ // En caso de que no haya evento, y el día no esté antes de la fecha límite, entonces es un día disponible, no se deshabilita y su estética no cambia
					System.out.println("Fecha disponible: "+dia);
					diaCalendario.setUserData(dia); // Si no hay ni evento ni está bloqueado, entonces el toggle guardará la fecha
				}
				dia = dia.plusDays(1); // Aumentamos el valor de dia para evaluar el siguiente junto al siguiente espacio de toggle
			}
		}
	}

	/**
	 * Es el método encargado de tomar los datos y mostrarlos para la ventana de próximos eventos
	 * @author JLCB
	 * @param eventos Son todos los eventos que están en el repositorio ya ordenados del más próximo al menos
	 */
	private void muestraProximosEventos(List<Evento> eventos) {
		int i = 0; // Se prepara un contador para solamente mostrar los tres eventos más próximos
		// Se revisa evento por evento
		for(Evento evento : eventos)
			if(!evento.getEstadoEvento().toString().equals("FINALIZADO")) // No se muestran los eventos Finalizados
				if(i==0){
					proxFecha1.setText(evento.getFecha().format(formatoDia));
					proxNombre1.setText(evento.getTipoEvento().toString());
					// Dependiendo del estado del evento se pone un color distinto
					if(evento.getEstadoEvento().toString().equals("BORRADOR"))
						proxEstado1.setStyle("-fx-fill: #67ebf5");
					else
						proxEstado1.setStyle("-fx-fill: #1a9cd4");
					i++; // Si se guardó, se agumenta el valor de i para ir al siguiente espacio disponible
				}else if(i==1){
					proxFecha2.setText(evento.getFecha().format(formatoDia));
					proxNombre2.setText(evento.getTipoEvento().toString());
					if(evento.getEstadoEvento().toString().equals("BORRADOR"))
						proxEstado2.setStyle("-fx-fill: #67ebf5");
					else
						proxEstado2.setStyle("-fx-fill: #1a9cd4");
					i++;
				}else if(i==2){
					proxFecha3.setText(evento.getFecha().format(formatoDia));
					proxNombre3.setText(evento.getTipoEvento().toString());
					if(evento.getEstadoEvento().toString().equals("BORRADOR"))
						proxEstado3.setStyle("-fx-fill: #67ebf5");
					else
						proxEstado3.setStyle("-fx-fill: #1a9cd4");
					return; // Debido a que los tres espacios ya están llenos, se rompe sale del método
				}
	}
	public void muestraDetallesEvento(List<Object> datos, Evento evento){
		proximosEventos.setVisible(false);
		proximosEventos.setManaged(false);
		detallesFinalizado.setVisible(false);
		detallesFinalizado.setVisible(false);
		continuar.setDisable(true);

		detallesEvento.setVisible(true);
		detallesEvento.setManaged(true);

		cotizacion.setUserData(evento);
		gestion.setUserData(evento);
		pagos.setUserData(evento);

		eventoSeleccionado.setText(datos.get(1).toString());
		horaSeleccionado.setText(datos.get(2).toString());
		ubicacionSeleccionado.setText(datos.get(3).toString());
		clienteSeleccionado.setText(datos.get(4).toString());
		montoSeleccionado.setText((String) datos.get(5).toString());
		pagoSeleccionado.setText(datos.get(6).toString());

		if(datos.get(0).toString().equals("BORRADOR")){
			rectanguloSeleccionado.setStyle("-fx-fill: #67ebf5");
			estadoSeleccionado.setStyle("-fx-fill: #67ebf5");
			estadoSeleccionado.setText("BORRADOR");
		}else{
			rectanguloSeleccionado.setStyle("-fx-fill: #1a9cd4");
			estadoSeleccionado.setStyle("-fx-fill: #1a9cd4");
			estadoSeleccionado.setText("CONFIRMADO");
		}

	}
	public void muestraDetallesFinalizado(List<Object> datos, Evento evento){
		proximosEventos.setVisible(false);
		proximosEventos.setManaged(false);
		detallesEvento.setVisible(false);
		detallesEvento.setManaged(false);
		continuar.setDisable(true);

		detallesFinalizado.setVisible(true);
		detallesFinalizado.setVisible(true);

		compartir.setUserData(evento);
		liquidacion.setUserData(evento);
		mobiliario.setUserData(evento);

		eventoFinalizado.setText(datos.get(1).toString());
		horaFinalizado.setText(datos.get(2).toString());
		ubicacionFinalizado.setText(datos.get(3).toString());
		clienteFinalizado.setText(datos.get(4).toString());
		montoFinalizado.setText((String) datos.get(5).toString());
	}

	/**
	 * Método que recibe lo que guarda el día en el que el usuario haya presionado el botón
	 * @param event
	 */
	@FXML
	private void botonDia(ActionEvent event){
		ToggleButton botonPresionado = (ToggleButton) event.getSource();

		// Evitamos problemas si el usuario deselecciona el botón
		if (!botonPresionado.isSelected()) {
			System.out.println("Botón deseleccionado.");
			detallesEvento.setVisible(false);
			detallesEvento.setManaged(false);
			detallesFinalizado.setVisible(false);
			detallesFinalizado.setVisible(false);
			proximosEventos.setVisible(true);
			proximosEventos.setManaged(true);
			continuar.setDisable(true);
			continuar.setUserData(null);
			cotizacion.setUserData(null);
			gestion.setUserData(null);
			pagos.setUserData(null);
			compartir.setUserData(null);
			liquidacion.setUserData(null);
			mobiliario.setUserData(null);
			return;
		}
		Object dato = botonPresionado.getUserData();
		if(dato == null){
			return;
		}

		if(dato instanceof LocalDate fecha){
			detallesEvento.setVisible(false);
			detallesEvento.setManaged(false);
			detallesFinalizado.setVisible(false);
			detallesFinalizado.setVisible(false);
			proximosEventos.setVisible(true);
			proximosEventos.setManaged(true);
			
			control.seleccionaFecha(fecha);
		}else if(dato instanceof Evento evento)
			control.eventoPresionado(evento);
	}
	public void muestraErrorFechaOcupada(){
		errorFechaOcupada.setManaged(true);
		errorFechaOcupada.setVisible(true);
	}
	public void habilitaContinuar(LocalDate fecha){
		continuar.setUserData(fecha);
		continuar.setDisable(false);
	}


	@FXML
	private void botonContinuar(ActionEvent event){
		Button botonPresionado = (Button) event.getSource();
		Object dato = botonPresionado.getUserData();
		if(dato instanceof LocalDate fecha){
			control.abrirCreacionEvento(fecha);
		}
	}



	@FXML
	private void botonAntMes(){
		System.out.println("Presionó Anterior Mes");
		LocalDate mesActual = (LocalDate) mesAnio.getUserData();
		control.anteriorMes(mesActual);
	}
	@FXML
	private void botonSigMes(){
		System.out.println("Presionó Siguiente Mes");
		LocalDate mesActual = (LocalDate) mesAnio.getUserData();
		control.siguienteMes(mesActual);
	}

	
	@FXML
	private void botonPublicar(ActionEvent event){
		Button botonPresionado = (Button) event.getSource();
		Object dato = botonPresionado.getUserData();
		if(dato==null) return;
		if(dato instanceof Evento evento)
			control.verPublicar(evento);
	}
	@FXML
	private void botonLiquidacion(ActionEvent event){
		Button botonPresionado = (Button) event.getSource();
		Object dato = botonPresionado.getUserData();
		if(dato==null) return;
		if(dato instanceof Evento evento)
			control.verLiquidacion(evento);
	}
	@FXML
	private void botonMobiliario(ActionEvent event){
		Button botonPresionado = (Button) event.getSource();
		Object dato = botonPresionado.getUserData();
		if(dato==null) return;
		if(dato instanceof Evento evento)
			control.verMobiliario(evento);
	}
	@FXML
	private void botonCotizacion(ActionEvent event){
		Button botonPresionado = (Button) event.getSource();
		Object dato = botonPresionado.getUserData();
		if(dato==null) return;
		if(dato instanceof Evento evento)
			control.verCotizacion(evento);
	}
	@FXML
	private void botonGestion(ActionEvent event){
		Button botonPresionado = (Button) event.getSource();
		Object dato = botonPresionado.getUserData();
		if(dato==null) return;
		if(dato instanceof Evento evento){
			control.verGestion(evento);
		}
	}
	@FXML
	private void botonPagos(ActionEvent event){
		Button botonPresionado = (Button) event.getSource();
		Object dato = botonPresionado.getUserData();
		if(dato==null) return;
		if(dato instanceof Evento evento){
			control.verPagos(evento);
		}
	}

	public void cierra(){
		stage.close();
	}
}
