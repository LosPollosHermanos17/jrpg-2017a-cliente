package mensajeria;

import java.io.Serializable;

public class ComandoMovimiento extends Comando implements Serializable {

	private PaqueteMovimiento paqueteMovimiento;

	public ComandoMovimiento(PaqueteMovimiento paqueteMovimiento) {
		this.paqueteMovimiento = paqueteMovimiento;
	}

	@Override
	public void resolver(Object argumento) {
	}
}