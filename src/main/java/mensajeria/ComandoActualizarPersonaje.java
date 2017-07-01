package mensajeria;

import java.io.Serializable;

import cliente.EscuchaMensajes;
import juego.Juego;

public class ComandoActualizarPersonaje extends Comando implements Serializable{

	private PaquetePersonaje paquetePersonaje;

	@Override
	public void resolver(Object argumento) {
		EscuchaMensajes escuchaMensajes = (EscuchaMensajes)argumento;
		escuchaMensajes.getPersonajesConectados().remove(this.paquetePersonaje.getId());
		escuchaMensajes.getPersonajesConectados().put(this.paquetePersonaje.getId(), paquetePersonaje);
		Juego juego = escuchaMensajes.getJuego();
		if (juego.getPersonaje().getId() == paquetePersonaje.getId()) {
			juego.actualizarPersonaje();
			juego.getEstadoJuego().actualizarPersonaje();
		}
	}

	public PaquetePersonaje getPaquetePersonaje() {
		return paquetePersonaje;
	}

	public void setPaquetePersonaje(PaquetePersonaje paquetePersonaje) {
		this.paquetePersonaje = paquetePersonaje;
	}
	
	
}
