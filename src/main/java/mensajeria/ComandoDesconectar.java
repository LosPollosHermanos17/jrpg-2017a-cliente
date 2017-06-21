package mensajeria;

import java.io.Serializable;
import java.util.HashMap;

import cliente.EscuchaMensajes;
import juego.Juego;

public class ComandoDesconectar extends Comando implements Serializable {

	public ComandoDesconectar(String ip) {
		this.setIp(ip);
	}

	@Override
	public void resolver(Object argumento) {
	}
}
