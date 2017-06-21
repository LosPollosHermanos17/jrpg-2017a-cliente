package mensajeria;

import java.io.Serializable;

public class ComandoConexion extends Comando implements Serializable {

	private PaquetePersonaje paquetePersonaje;

	public ComandoConexion(PaquetePersonaje paquetePersonaje) {
		this.paquetePersonaje = paquetePersonaje;
	}

	@Override
	public void resolver(Object argumento) {
	}
}