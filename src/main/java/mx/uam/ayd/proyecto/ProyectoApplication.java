package mx.uam.ayd.proyecto;

import java.time.LocalDate;
import java.time.LocalTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;

import mx.uam.ayd.proyecto.datos.RepositorioCliente;
import mx.uam.ayd.proyecto.datos.RepositorioCotizacion;
import mx.uam.ayd.proyecto.datos.RepositorioEvento;
import mx.uam.ayd.proyecto.negocio.modelo.Cliente;
import mx.uam.ayd.proyecto.negocio.modelo.Cotizacion;
import mx.uam.ayd.proyecto.negocio.modelo.Evento;
import mx.uam.ayd.proyecto.negocio.modelo.Evento.EstadoEvento;
import mx.uam.ayd.proyecto.negocio.modelo.Evento.EstadoPago;
import mx.uam.ayd.proyecto.negocio.modelo.Evento.TipoEvento;
import mx.uam.ayd.proyecto.presentacion.calendario.ControlCalendario;

/**
 * 
 * Clase principal que arranca la aplicación 
 * construida usando el principio de 
 * inversión de control
 * Adaptada para usar JavaFX
 * 
 * @author Humberto Cervantes (c) 21 Nov 2022
 */
@SpringBootApplication
public class ProyectoApplication {

	private final ControlCalendario controlCalendario;
	private final RepositorioEvento repositorioEvento;
	private final RepositorioCotizacion repositorioCotizacion;
	private final RepositorioCliente repositorioCliente;
	
	@Autowired
	public ProyectoApplication(ControlCalendario controlCalendario, RepositorioEvento repositorioEvento, RepositorioCotizacion repositorioCotizacion, RepositorioCliente repositorioCliente) {
		this.controlCalendario = controlCalendario;
		this.repositorioEvento = repositorioEvento;
		this.repositorioCotizacion = repositorioCotizacion;
		this.repositorioCliente = repositorioCliente;
	}

	/**
	 * Método principal
	 *
	 * @param args argumentos de la línea de comando
	 */
	public static void main(String[] args) {
		// Launch JavaFX application
		Application.launch(JavaFXApplication.class, args);
	}
	
	/**
	 * Clase interna para manejar la inicialización de JavaFX
	 */
	public static class JavaFXApplication extends Application {
		
		private static ConfigurableApplicationContext applicationContext;
		
		@Override
		public void init() throws Exception {
			// Create Spring application context
			SpringApplicationBuilder builder = new SpringApplicationBuilder(ProyectoApplication.class);
			builder.headless(false);
			applicationContext = builder.run(getParameters().getRaw().toArray(new String[0]));
		}
		
		@Override
		public void start(Stage primaryStage) {
			// Initialize the application on the JavaFX thread
			Platform.runLater(() -> {
				applicationContext.getBean(ProyectoApplication.class).inicia();
			});
		}
		
		@Override
		public void stop() throws Exception {
			applicationContext.close();
			Platform.exit();
		}
	}
	
	/**
	 * Metodo que arranca la aplicacion
	 * inicializa la bd y arranca el controlador
	 */
	public void inicia() {
		inicializaBD();
		
		// Make sure controllers are created on JavaFX thread
		Platform.runLater(() -> {
			controlCalendario.iniciaCalendario();
		});
	}
	
	/**
	 * Inicializa la BD con datos
	 */
	public void inicializaBD() {
		Cotizacion cotizacion1 = new Cotizacion();
		repositorioCotizacion.save(cotizacion1);
		Cotizacion cotizacion2 = new Cotizacion();
		repositorioCotizacion.save(cotizacion2);
		Cotizacion cotizacion3 = new Cotizacion();
		repositorioCotizacion.save(cotizacion3);
		Cotizacion cotizacion4 = new Cotizacion();
		repositorioCotizacion.save(cotizacion4);
		Cotizacion cotizacion5 = new Cotizacion();
		repositorioCotizacion.save(cotizacion5);
		Cliente cliente1 = new Cliente("Juan", "5546803266");
		repositorioCliente.save(cliente1);
		Cliente cliente2 = new Cliente("María Rodríguez", "5512345678");
		repositorioCliente.save(cliente2);
		Cliente cliente3 = new Cliente("Carlos Gómez", "5587654321");
		repositorioCliente.save(cliente3);
		Cliente cliente4 = new Cliente("Ana Martínez", "5533221100");
		repositorioCliente.save(cliente4);
		Cliente cliente5 = new Cliente("Luis Hernández", "5599887766");
		repositorioCliente.save(cliente5);

		// --- EVENTO 1 ---
		Evento evento1 = new Evento();
		evento1.setTipoEvento(TipoEvento.BODA);
		evento1.setFecha(LocalDate.of(2026, 7, 20));
		evento1.setHora(LocalTime.of(18, 0));
		evento1.setLugar("Hacienda Los Morales");
		evento1.setReferencias("A dos cuadras del parque central, portón de madera alto");
		evento1.setDireccion("Av. Principal #123, Col. Centro");
		evento1.setTotalPagado(15000.00f);
		evento1.setEstadoEvento(EstadoEvento.CONFIRMADO);
		evento1.setDetalles("Música en vivo, montaje de mesa de postres");
		evento1.setVisualRecinto("https://storage.miservicio.com/recintos/hacienda_morales.jpg");
		evento1.setEstadoPago(EstadoPago.LIQUIDADO);
		evento1.setClausulasExtras("Se requiere acceso al recinto 3 horas antes para montaje");
		evento1.setContratoFirmado(true);
		try { evento1.setCotizacion(cotizacion1); } catch (Exception e) {}
		try { evento1.setCliente(cliente1); } catch (Exception e) {}

		// --- EVENTO 2 ---
		Evento evento2 = new Evento();
		evento2.setTipoEvento(TipoEvento.ESCOLAR);
		evento2.setFecha(LocalDate.of(2026, 7, 10));
		evento2.setHora(LocalTime.of(9, 30));
		evento2.setLugar("Auditorio Colegio Americano");
		evento2.setReferencias(null); // Opcional nulo
		evento2.setDireccion("Calle de la Educación #45, Zona Escolar");
		evento2.setTotalPagado(5000.50f);
		evento2.setEstadoEvento(EstadoEvento.FINALIZADO);
		evento2.setDetalles("Graduación de secundaria, sonido e iluminación básica");
		evento2.setVisualRecinto(null); // Opcional nulo
		evento2.setEstadoPago(EstadoPago.LIQUIDADO);
		evento2.setClausulasExtras(null); // Opcional nulo
		evento2.setContratoFirmado(true);
		try { evento2.setCotizacion(cotizacion2); } catch (Exception e) {}
		try { evento2.setCliente(cliente2); } catch (Exception e) {}

		// --- EVENTO 3 ---
		Evento evento3 = new Evento();
		evento3.setTipoEvento(TipoEvento.ESCOLAR);
		evento3.setFecha(LocalDate.of(2026, 7, 31));
		evento3.setHora(LocalTime.of(11, 0));
		evento3.setLugar(null); // Opcional nulo (por definir)
		evento3.setReferencias(null); // Opcional nulo
		evento3.setDireccion("Por definir");
		evento3.setTotalPagado(0.00f);
		evento3.setEstadoEvento(EstadoEvento.BORRADOR);
		evento3.setDetalles(null); // Opcional nulo
		evento3.setVisualRecinto(null); // Opcional nulo
		evento3.setEstadoPago(EstadoPago.PENDIENTE);
		evento3.setClausulasExtras(null); // Opcional nulo
		evento3.setContratoFirmado(false);
		try { evento3.setCotizacion(cotizacion3); } catch (Exception e) {}
		try { evento3.setCliente(cliente3); } catch (Exception e) {}

		// --- EVENTO 4 ---
		Evento evento4 = new Evento();
		evento4.setTipoEvento(TipoEvento.CUMPLEANOS);
		evento4.setFecha(LocalDate.of(2026, 8, 10));
		evento4.setHora(LocalTime.of(15, 0));
		evento4.setLugar("Salón de Fiestas 'Fiesta Mágica'");
		evento4.setReferencias("Frente a la gasolinera PEMEX");
		evento4.setDireccion("Calle Las Flores #78");
		evento4.setTotalPagado(1500.00f);
		evento4.setEstadoEvento(EstadoEvento.BORRADOR);
		evento4.setDetalles("Temática de superhéroes");
		evento4.setVisualRecinto(null); // Opcional nulo
		evento4.setEstadoPago(EstadoPago.PENDIENTE);
		evento4.setClausulasExtras("No se permite pirotecnia dentro del salón");
		evento4.setContratoFirmado(false);
		try { evento4.setCotizacion(cotizacion4); } catch (Exception e) {}
		try { evento4.setCliente(cliente4); } catch (Exception e) {}

		// --- EVENTO 5 ---
		Evento evento5 = new Evento();
		evento5.setTipoEvento(TipoEvento.BODA);
		evento5.setFecha(LocalDate.of(2026, 6, 1));
		evento5.setHora(LocalTime.of(19, 0));
		evento5.setLugar("Jardín Botánico");
		evento5.setReferencias("Entrada por el acceso oriente");
		evento5.setDireccion("Camino Real s/n");
		evento5.setTotalPagado(22000.00f);
		evento5.setEstadoEvento(EstadoEvento.FINALIZADO);
		evento5.setDetalles("Boda civil y recepción al aire libre");
		evento5.setVisualRecinto("https://storage.miservicio.com/recintos/jardin_botanico.jpg");
		evento5.setEstadoPago(EstadoPago.LIQUIDADO);
		evento5.setClausulasExtras(null); // Opcional nulo
		evento5.setContratoFirmado(true);
		try { evento5.setCotizacion(cotizacion5); } catch (Exception e) {}
		try { evento5.setCliente(cliente5); } catch (Exception e) {}
		

		repositorioEvento.save(evento1);
		repositorioEvento.save(evento2);
		repositorioEvento.save(evento3);
		repositorioEvento.save(evento4);
		repositorioEvento.save(evento5);
		System.out.println(repositorioEvento.findByOrderByFechaAsc());
	}
}
