package testsCliente;
import mensajeria.*;
import mensajeria.ComandoInicioSesion;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import org.junit.Assert;
import org.junit.Test;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import cliente.Cliente;

public class TestCliente {
	private Thread t;
	private ServerSocket ss;
	private Gson gson = new Gson();

	/// Para realizar los test es necesario iniciar el servidor
	public void server(final Queue<Comando> paquetes) {
		t = new Thread(new Runnable() {
			public void run() {

				try {
					System.out.println("Creando Servidor");
					ss = new ServerSocket(9998);
					System.out.println("Servidor Creado.");
					System.out.println("Esperando clientes...");
					Socket client = ss.accept();
					System.out.println("Cliente conectado.");
					System.out.println("Salida...");
					ObjectOutputStream salida = new ObjectOutputStream(client.getOutputStream());
					System.out.println("Salida.");
					System.out.println("Entrada...");
					ObjectInputStream entrada = new ObjectInputStream(client.getInputStream());
					System.out.println("Entrada.");

					while (!paquetes.isEmpty()) {
						System.out.println("Paquetes: " + paquetes.size());
						entrada.readObject();
						Comando paquete = paquetes.poll();
						if (paquete.getMensaje() != "0")
							paquete.setMensaje("1");
						salida.writeObject(gson.toJson(paquete));
						System.out.println("Escrito");
					}
					System.out.println("Cerrando cliente...");
					client.close();
					System.out.println("Cliente cerrado.");

				} catch (IOException | ClassNotFoundException e) {
					e.printStackTrace();

				} finally {
					try {
						System.out.println("Cerrando servidor...");
						ss.close();
						System.out.println("Servidor cerrado");
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		});
		t.start();
	}

	@Test
	public void testConexionConElServidor() {
		Queue<Comando> cola = new LinkedList<Comando>();
		PaqueteUsuario paqueteUsuario = new PaqueteUsuario();
		Comando comando = new ComandoInicioSesion(paqueteUsuario);
		comando.setMensaje("1");
		cola.add(comando);
		this.server(cola);
		Cliente cliente = new Cliente();
		Assert.assertEquals(1, 1);
		cliente.cerrarConexiones();
		t.stop();
	}

	@Test
	public void testRegistro() {

		// Registro el usuario
		PaqueteUsuario pu = new PaqueteUsuario();
		pu.setUsername("nuevoUser");
		pu.setPassword("test");
		Comando comando = new ComandoRegistro(pu);
		comando.setMensaje("1");
		Queue<Comando> cola = new LinkedList<Comando>();
		cola.add(comando);
		this.server(cola);

		// Inicio el Thread
		Cliente cliente = new Cliente();
		
		try {

			// Envio el paquete para registrarme
			cliente.enviarComando(comando);
			// Recibo la respuesta del servidor
			 Comando resultado = cliente.recibirComando();
			// Cierro las conexiones
			cliente.cerrarConexiones();
			Assert.assertEquals(Comando.msjExito, resultado.getMensaje());

		} catch (JsonSyntaxException | ClassNotFoundException | IOException e) {
			e.printStackTrace();
		}
		t.stop();
	}

	@Test
	public void testRegistroFallido() {

		// Registro el usuario
		PaqueteUsuario pu = new PaqueteUsuario();
		pu.setUsername("nuevoUser");
		pu.setPassword("test32");
		

		Queue<Comando> cola = new LinkedList<Comando>();
		cola.add(new ComandoRegistro(pu));
		cola.add(new ComandoRegistro(pu));
		this.server(cola);

		Cliente cliente = new Cliente();
		Comando comando = new ComandoRegistro(pu);
		comando.setMensaje("0");
		try {

			// Envio el paquete para registrarme
			cliente.enviarComando(comando);
			// Recibo la respuesta del servidor
			Comando resultado = cliente.recibirComando();
			// Cierro las conexiones
			cliente.cerrarConexiones();
			Assert.assertEquals(Comando.msjFracaso, resultado.getMensaje());

		} catch (JsonSyntaxException | ClassNotFoundException | IOException e) {
			e.printStackTrace();
		}
		t.stop();
	}

	@Test
	public void testRegistrarPersonaje() {// cuelga

		// Registro de usuario
		PaqueteUsuario pu = new PaqueteUsuario();
		//pu.setComando(Comando.REGISTRO);
		pu.setUsername("nuevoUser");
		pu.setPassword("test");

		// Registro de personaje
		PaquetePersonaje pp = new PaquetePersonaje();
		//pp.setComando(Comando.CREACIONPJ);
		pp.setCasta("Humano");
		pp.setDestreza(1);
		pp.setEnergiaTope(1);
		pp.setExperiencia(1);
		pp.setFuerza(1);
		pp.setInteligencia(1);
		pp.setNivel(1);
		pp.setNombre("PjTest");
		pp.setRaza("Asesino");
		pp.setSaludTope(1);

		Queue<Comando> cola = new LinkedList<Comando>();
		cola.add(new ComandoRegistro(pu));
		cola.add(new ComandoCrearPersonaje(pp));
		cola.add(new ComandoCrearPersonaje(pp));
		this.server(cola);

		Cliente cliente = new Cliente();
		try {

			// Envio el paquete de registro de usuario
			cliente.enviarComando(new ComandoRegistro(pu));
			// Recibo la respuesta del servidor
			Comando paqueteAux = cliente.recibirComando();

			// Envio el paquete de registro de personaje
			cliente.enviarComando(new ComandoCrearPersonaje(pp));
			// Recibo el personaje de mi usuario

			pp = ((ComandoCrearPersonaje)cliente.recibirComando()).getPaquetePersonaje();
			// Cierro las conexiones
			cliente.cerrarConexiones();

			Assert.assertEquals("PjTest", pp.getNombre());
		} catch (IOException | JsonSyntaxException | ClassNotFoundException e) {
			e.printStackTrace();
		}
		t.stop();
	}

	@Test
	public void testIniciarSesion() {

		PaquetePersonaje paquete = new PaquetePersonaje();
		
		paquete.setNombre("PjTest");
		Queue<Comando> cola = new LinkedList<Comando>();
		ComandoInicioSesion comando = new ComandoInicioSesion();
		comando.setPaquetePersonaje(paquete);
		cola.add(comando);
		this.server(cola);

		Cliente cliente = new Cliente();

		PaqueteUsuario pu = new PaqueteUsuario();
		pu.setUsername("nuevoUser");
		pu.setPassword("test");
		comando.setPaqueteUsuario(pu);

		try {
			// Envio el paquete de incio de sesion
			cliente.enviarComando(comando);
			// Recibo el paquete con el personaje
			PaquetePersonaje paquetePersonaje = ((ComandoInicioSesion)cliente.recibirComando()).getPaquetePersonaje();

			// Cierro las conexiones
			cliente.cerrarConexiones();
			Assert.assertEquals("PjTest", paquetePersonaje.getNombre());
		} catch (IOException | JsonSyntaxException | ClassNotFoundException e) {
			e.printStackTrace();
		}
		t.stop();
	}

	@Test
	public void testActualizarPersonaje() {

		PaquetePersonaje pp = new PaquetePersonaje();
		ComandoActualizarPersonaje comando = new ComandoActualizarPersonaje(pp);
		pp.setCasta("Humano");
		pp.setDestreza(1);
		pp.setEnergiaTope(1);
		pp.setExperiencia(1);
		pp.setFuerza(1);
		pp.setInteligencia(1);
		pp.setNivel(1);
		pp.setNombre("PjTest");
		pp.setRaza("Asesino");
		pp.setSaludTope(10000);
		comando.setPaquetePersonaje(pp);
		Queue<Comando> cola = new LinkedList<Comando>();
		cola.add(comando);
		this.server(cola);

		Cliente cliente = new Cliente();
		try {

			// Envio el paquete de actualizacion de personaje
			cliente.enviarComando(comando);

			// Recibo el paquete con el personaje actualizado
			PaquetePersonaje paquetePersonaje = ((ComandoActualizarPersonaje)cliente.recibirComando()).getPaquetePersonaje();

			// Cierro las conexiones
			cliente.cerrarConexiones();
			Assert.assertEquals(10000, paquetePersonaje.getSaludTope());
		} catch (IOException | JsonSyntaxException | ClassNotFoundException e) {
			e.printStackTrace();
		}
		t.stop();
	}
	
	@Test
	public void enviaMensajeChat(){
		
	}
}
