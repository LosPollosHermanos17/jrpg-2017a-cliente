package cliente;

import java.util.HashMap;
import java.util.Map;

import javax.swing.JOptionPane;
import juego.Juego;
import mensajeria.Comando;
import mensajeria.PaqueteMovimiento;
import mensajeria.PaquetePersonaje;

public class EscuchaMensajes extends Thread {

	private Juego juego;

	private Map<Integer, PaqueteMovimiento> ubicacionPersonajes;
	private Map<Integer, PaquetePersonaje> personajesConectados;

	public EscuchaMensajes(Juego juego) {
		this.juego = juego;	
		this.personajesConectados = new HashMap<>();
		this.ubicacionPersonajes = new HashMap<>();	
	}

	public void run() {

		try {
			while (true) {
				Comando comando = this.juego.getCliente().recibirComando();
				comando.resolver(this);
			}
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Fallo la conexión con el servidor.");
			e.printStackTrace();
		}
	}

	public Map<Integer, PaqueteMovimiento> getUbicacionPersonajes() {
		return ubicacionPersonajes;
	}

	public void setUbicacionPersonajes(Map<Integer, PaqueteMovimiento> ubicaciones) {
		this.ubicacionPersonajes = ubicaciones;
	}

	public Map<Integer, PaquetePersonaje> getPersonajesConectados() {
		return personajesConectados;
	}

	public void setPersonajesConectados(Map<Integer, PaquetePersonaje> personajes) {
		this.personajesConectados = personajes;
	}

	public Juego getJuego() {
		return this.juego;
	}
}