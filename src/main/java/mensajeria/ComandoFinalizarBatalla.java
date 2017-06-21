package mensajeria;

import java.io.Serializable;

import cliente.EscuchaMensajes;
import estados.Estado;
import juego.Juego;

public class ComandoFinalizarBatalla extends Comando implements Serializable {

	private int id;
	private int idEnemigo;

	public ComandoFinalizarBatalla(int id, int idEnemigo) {
		this.id = id;
		this.idEnemigo = idEnemigo;
	}

	@Override
	public void resolver(Object argumento) {
		Juego juego = ((EscuchaMensajes) argumento).getJuego();
		juego.getPersonaje().setEstado(Estado.estadoJuego);
		Estado.setEstado(juego.getEstadoJuego());
	}
}
