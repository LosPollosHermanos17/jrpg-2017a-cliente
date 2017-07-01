package mensajeria;

import java.io.Serializable;

import cliente.Cliente;

public class ComandoCrearPersonaje extends Comando implements Serializable {

	private PaquetePersonaje paquetePersonaje;

	public ComandoCrearPersonaje(PaquetePersonaje personaje) {
		this.paquetePersonaje = personaje;
	}
	
	public PaquetePersonaje getPaquetePersonaje(){
		return paquetePersonaje;
	}

	@Override
	public void resolver(Object argumento) {
		Cliente cliente = (Cliente) argumento;
		cliente.setPaquetePersonaje(this.paquetePersonaje);
		cliente.getPaqueteUsuario().setInicioSesion(true);
	}
}