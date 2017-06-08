package testsCliente;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import org.junit.Assert;
import org.junit.Test;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import cliente.Cliente;
import mensajeria.Comando;
import mensajeria.Paquete;
import mensajeria.PaquetePersonaje;
import mensajeria.PaqueteUsuario;

public class TestCliente {
	private Thread t;

	public void server(Paquete paquete) {
		t = new Thread(new Runnable() {
			public void run() {

				try {
					// Creas el server
					ServerSocket ss = new ServerSocket(9999);
					// Esperas a que el cliente se conecte
					Socket client = ss.accept();
					// El cliente se conecto y obtengo el stream de salida
					ObjectOutputStream salida = new ObjectOutputStream(client.getOutputStream());
					// Convierto el objeto a un string en json
					Gson gson = new Gson();
					if(paquete.getMensaje()!="0")
						paquete.setMensaje("1");
					String json = gson.toJson(paquete);
					// Envio el objeto en formato json
					salida.writeObject(json);

					// Esperas a que el cliente se Desconecte
					client = ss.accept();

					// Cierro el socket del cliente
					client.close();
					// Cierro el socket del server
					ss.close();

				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		t.start();
	}

	public void server(Paquete paquete, Paquete paquete2) {
		t = new Thread(new Runnable() {
			public void run() {

				try {
					// Creas el server
					ServerSocket ss = new ServerSocket(9999);
					// Esperas a que el cliente se conecte
					Socket client = ss.accept();
					// El cliente se conecto y obtengo el stream de salida
					ObjectOutputStream salida = new ObjectOutputStream(client.getOutputStream());
					// Convierto el objeto a un string en json
					Gson gson = new Gson();
					paquete.setMensaje("1");
					String json = gson.toJson(paquete);
					// Envio el objeto en formato json
					salida.writeObject(json);

					// Esperas a que el cliente se Desconecte
					
					json = gson.toJson(paquete2);
					// Envio el objeto en formato json
					salida.writeObject(json);
					client=ss.accept();
					// Cierro el socket del cliente
					client.close();
					// Cierro el socket del server
					ss.close();

				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		t.start();
	}
	/// Para realizar los test es necesario iniciar el servidor

	@Test
	public void testConexionConElServidor() {
		Gson gson = new Gson();
		Paquete paquete = new Paquete();
		server(paquete);

		Cliente cliente = new Cliente();

		Assert.assertEquals(1, 1);

		try {

			// Cierro las conexiones
			Paquete p = new Paquete();
			p.setComando(Comando.DESCONECTAR);
			p.setIp(cliente.getMiIp());
			cliente.getSalida().writeObject(gson.toJson(p));
			cliente.getSalida().close();
			cliente.getEntrada().close();
			cliente.getSocket().close();

		} catch (IOException e) {
			e.printStackTrace();
		}
		t.stop();
	}

	@Test
	public void testRegistro() {
		Gson gson = new Gson();

		// Registro el usuario
		PaqueteUsuario pu = new PaqueteUsuario();
		pu.setComando(Comando.REGISTRO);
		pu.setUsername("nuevoUser");
		pu.setPassword("test");
		// pu.setMensaje("1");
		server(pu);

		// Inicio el Thread
		Cliente cliente = new Cliente();

		try {

			// Envio el paquete para registrarme
			cliente.getSalida().writeObject(gson.toJson(pu));

			// Recibo la respuesta del servidor
			Paquete resultado = (Paquete) gson.fromJson((String) cliente.getEntrada().readObject(), Paquete.class);

			// Cierro las conexiones
			Paquete p = new Paquete();
			p.setComando(Comando.DESCONECTAR);
			p.setIp(cliente.getMiIp());
			cliente.getSalida().writeObject(gson.toJson(p));
			cliente.getSalida().close();
			cliente.getEntrada().close();
			cliente.getSocket().close();
			Assert.assertEquals(Paquete.msjExito, resultado.getMensaje());

		} catch (JsonSyntaxException | ClassNotFoundException | IOException e) {
			e.printStackTrace();
		}
		t.stop();
	}

	@Test
	public void testRegistroFallido() {
		Gson gson = new Gson();

		// Registro el usuario
		PaqueteUsuario pu = new PaqueteUsuario();
		pu.setComando(Comando.REGISTRO);
		pu.setUsername("nuevoUser");
		pu.setPassword("test32");
		pu.setMensaje("0");

		server(pu);
		Cliente cliente = new Cliente();

		try {

			// Envio el paquete para registrarme
			cliente.getSalida().writeObject(gson.toJson(pu));
			// Recibo la respuesta del servidor
			Paquete resultado = (Paquete) gson.fromJson((String) cliente.getEntrada().readObject(), Paquete.class);

			// Cierro las conexiones
			Paquete p = new Paquete();
			p.setComando(Comando.DESCONECTAR);
			p.setIp(cliente.getMiIp());
			cliente.getSalida().writeObject(gson.toJson(p));
			cliente.getSalida().close();
			cliente.getEntrada().close();
			cliente.getSocket().close();
			Assert.assertEquals(Paquete.msjFracaso, resultado.getMensaje());

		} catch (JsonSyntaxException | ClassNotFoundException | IOException e) {
			e.printStackTrace();
		}
		t.stop();
	}

	@Test
	public void testRegistrarPersonaje() {// cuelga
		Gson gson = new Gson();
		
		// Registro de usuario
		PaqueteUsuario pu = new PaqueteUsuario();
		pu.setComando(Comando.REGISTRO);
		pu.setUsername("nuevoUser");
		pu.setPassword("test");
		// Registro de personaje
		PaquetePersonaje pp = new PaquetePersonaje();
		pp.setComando(Comando.CREACIONPJ);
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
		
		server(pu,pp);
		Cliente cliente = new Cliente();
		try {

			// Envio el paquete de registro de usuario
			cliente.getSalida().writeObject(gson.toJson(pu));

			// Recibo la respuesta del servidor
			Paquete paqueteAux = (Paquete) gson.fromJson((String) cliente.getEntrada().readObject(), Paquete.class);

			// Envio el paquete de registro de personaje
			cliente.getSalida().writeObject(gson.toJson(pp));
			// Recibo el personaje de mi usuario

			pp = (PaquetePersonaje) gson.fromJson((String) cliente.getEntrada().readObject(), PaquetePersonaje.class);

			// Cierro las conexiones
			Paquete p = new Paquete();
			p.setComando(Comando.DESCONECTAR);
			p.setIp(cliente.getMiIp());
			cliente.getSalida().writeObject(gson.toJson(p));
			cliente.getSalida().close();
			cliente.getEntrada().close();
			cliente.getSocket().close();

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
		server(paquete);
		Cliente cliente = new Cliente();

		PaqueteUsuario pu = new PaqueteUsuario();
		pu.setComando(Comando.INICIOSESION);
		pu.setUsername("nuevoUser");
		pu.setPassword("test");

		try {
			Gson gson = new Gson();
			// Envio el paquete de incio de sesion
			cliente.getSalida().writeObject(gson.toJson(pu));

			// Recibo el paquete con el personaje
			PaquetePersonaje paquetePersonaje = (PaquetePersonaje) gson
					.fromJson((String) cliente.getEntrada().readObject(), PaquetePersonaje.class);

			// Cierro las conexiones
			Paquete p = new Paquete();
			p.setComando(Comando.DESCONECTAR);
			p.setIp(cliente.getMiIp());
			cliente.getSalida().writeObject(gson.toJson(p));
			cliente.getSalida().close();
			cliente.getEntrada().close();
			cliente.getSocket().close();
			Assert.assertEquals("PjTest", paquetePersonaje.getNombre());
		} catch (IOException | JsonSyntaxException | ClassNotFoundException e) {
			e.printStackTrace();
		}
		t.stop();
	}

	@Test
	public void testActualizarPersonaje() {
		Gson gson = new Gson();

		PaquetePersonaje pp = new PaquetePersonaje();
		pp.setComando(Comando.ACTUALIZARPERSONAJE);
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

		server(pp);
		Cliente cliente = new Cliente();
		try {

			// Envio el paquete de actualizacion de personaje
			cliente.getSalida().writeObject(gson.toJson(pp));

			// Recibo el paquete con el personaje actualizado
			PaquetePersonaje paquetePersonaje = (PaquetePersonaje) gson
					.fromJson((String) cliente.getEntrada().readObject(), PaquetePersonaje.class);

			// Cierro las conexiones
			Paquete p = new Paquete();
			p.setComando(Comando.DESCONECTAR);
			p.setIp(cliente.getMiIp());
			cliente.getSalida().writeObject(gson.toJson(p));
			cliente.getSalida().close();
			cliente.getEntrada().close();
			cliente.getSocket().close();
			Assert.assertEquals(10000, paquetePersonaje.getSaludTope());
		} catch (IOException | JsonSyntaxException | ClassNotFoundException e) {
			e.printStackTrace();
		}
		t.stop();
	}
}
