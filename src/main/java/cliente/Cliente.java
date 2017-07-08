package cliente;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Scanner;
import javax.swing.JOptionPane;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;

import frames.*;
import interfaz.MenuClientesChat;
import juego.Juego;
import mensajeria.AdaptadorComando;
import mensajeria.Comando;
import mensajeria.ComandoMostrarMapa;
import mensajeria.PaquetePersonaje;
import mensajeria.PaqueteUsuario;

public class Cliente extends Thread {

	private Socket socket;
	private String miIp;
	private ObjectInputStream entrada;
	private ObjectOutputStream salida;

	private Gson gson;

	private PaqueteUsuario paqueteUsuario;
	private PaquetePersonaje paquetePersonaje;

	private Comando comando;

	private Juego wome;
	private MenuCarga menuCarga;

	public Cliente() {
		try {
			Scanner sc = new Scanner(new File("config.txt"));
			socket = new Socket(sc.nextLine(), sc.nextInt());
			miIp = socket.getInetAddress().getHostAddress();
			sc.close();

			entrada = new ObjectInputStream(socket.getInputStream());
			salida = new ObjectOutputStream(socket.getOutputStream());

			GsonBuilder gsonBilder = new GsonBuilder().registerTypeAdapter(Comando.class, new AdaptadorComando());
			this.gson = gsonBilder.create();

		} catch (FileNotFoundException e) {
			JOptionPane.showMessageDialog(null, "No se ha encontrado el archivo de configuración config.txt");
			e.printStackTrace();
			System.exit(1);
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, "Fallo al iniciar la aplicación. Revise la conexión con el servidor.");
			e.printStackTrace();
			System.exit(1);
		}
	}

	public void run() {
		synchronized (this) {
			try {

				// Creo el paquete que le voy a enviar al servidor
				paqueteUsuario = new PaqueteUsuario();

				while (!paqueteUsuario.isInicioSesion()) {

					// Muestro el menú principal
					new MenuJugar(this).setVisible(true);

					// Creo los paquetes que le voy a enviar al servidor
					paqueteUsuario = new PaqueteUsuario();
					paquetePersonaje = new PaquetePersonaje();

					// Espero a que el usuario seleccione algun comando ( Registrarse o Loguearse)
					wait();

					// Cargo la ip
					this.comando.setIp(getMiIp());

					// Le envio el comando al servidor
					this.enviarComando(this.comando);

					// Recibo el comando desde el servidor
					this.comando = this.recibirComando();

					// Resuelvo el comando recibido
					this.comando.resolver(this);
				}

				// Abro el menu de eleccion del mapa
				MenuMapas menuElegirMapa = new MenuMapas(this);
				menuElegirMapa.setVisible(true);

				// Espero a que el usuario elija el mapa
				wait();

				// Establezco el mapa en el paquete personaje
				Comando comando = new ComandoMostrarMapa(this.paquetePersonaje);
				comando.setIp(miIp);
				// Le envio el paquete con el mapa seleccionado
				this.enviarComando(comando);

				// Instancio el juego y cargo los recursos
				wome = new Juego("World Of the Middle Earth", 800, 600, this, paquetePersonaje);

				// Muestro el menu de carga
				menuCarga = new MenuCarga(this);
				menuCarga.setVisible(true);

				// Espero que se carguen todos los recursos
				wait();

				// Inicio el juego
				wome.start();

				// Finalizo el menu de carga
				menuCarga.dispose();		

			} catch (IOException | InterruptedException | ClassNotFoundException e) {
				JOptionPane.showMessageDialog(null, "Fallo la conexi�n con el servidor durante el inicio de sesi�n.");
				System.exit(1);
				e.printStackTrace();
			}
		}

	}

	public void setComando(Comando comando) {
		this.comando = comando;
	}

	public void cerrarConexiones() {
		try {
			this.entrada.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			this.salida.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			this.socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public String getMiIp() {
		return miIp;
	}

	public PaqueteUsuario getPaqueteUsuario() {
		return paqueteUsuario;
	}

	public PaquetePersonaje getPaquetePersonaje() {
		return paquetePersonaje;
	}

	public void setPaquetePersonaje(PaquetePersonaje paquetePersonaje) {
		this.paquetePersonaje = paquetePersonaje;
	}

	public Juego getJuego() {
		return wome;
	}

	public MenuCarga getMenuCarga() {
		return menuCarga;
	}

	public void enviarComando(Comando comando) throws IOException {
		this.salida.writeObject(gson.toJson(comando, Comando.class));
	}

	public Comando recibirComando() throws JsonSyntaxException, ClassNotFoundException, IOException {
		return gson.fromJson((String) this.entrada.readObject(), Comando.class);
	}
}
