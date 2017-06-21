package mensajeria;

import java.io.Serializable;

public class ComandoMostrarMapa extends Comando implements Serializable {

	PaquetePersonaje paquetePersonaje;

	public ComandoMostrarMapa(PaquetePersonaje paquetePersonaje) {
		this.paquetePersonaje = paquetePersonaje;
	}

	@Override
	public void resolver(Object argumento) {
	}
}
