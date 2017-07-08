package mensajeria;

import java.io.Serializable;
import java.util.Map;

import cliente.EscuchaMensajes;

public class ComandoConexiones extends Comando implements Serializable, Cloneable {

	private Map<Integer, PaquetePersonaje> personajes;

	public ComandoConexiones(Map<Integer, PaquetePersonaje> personajes) {
		this.personajes = personajes;
	}

	public Map<Integer, PaquetePersonaje> getPersonajes() {
		return personajes;
	}

	@Override
	public void resolver(Object argumento) {
		EscuchaMensajes escuchaMensajes = ((EscuchaMensajes) argumento);
		escuchaMensajes.setPersonajesConectados(this.getPersonajes());
		escuchaMensajes.getJuego().getMenuClientesChat().actualizarUsuarios(this.getPersonajes());
	}
}