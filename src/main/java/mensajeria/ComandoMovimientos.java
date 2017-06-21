package mensajeria;

import java.io.Serializable;
import java.util.Map;

import cliente.EscuchaMensajes;

public class ComandoMovimientos extends Comando implements Serializable {

	private Map<Integer, PaqueteMovimiento> movimientos;

	public ComandoMovimientos(Map<Integer, PaqueteMovimiento> movimientos) {
		this.movimientos = movimientos;
	}

	public Map<Integer, PaqueteMovimiento> getMovimientos() {
		return movimientos;
	}

	@Override
	public void resolver(Object argumento) {
		((EscuchaMensajes) argumento).setUbicacionPersonajes(this.movimientos);
	}
}