package mensajeria;

import java.io.Serializable;
import java.util.List;

import cliente.EscuchaMensajes;
import estados.Estado;
import estados.EstadoBatalla;
import juego.Juego;

public class ComandoBatalla extends Comando implements Serializable, Cloneable {

	private int id;
	private int idEnemigo;
	private boolean miTurno;
	private List<PaqueteItem> items;

	public ComandoBatalla(int id, int idEnemigo) {
		this.id = id;
		this.idEnemigo = idEnemigo;
	}

	public int getId() {
		return id;
	}

	public int getIdEnemigo() {
		return idEnemigo;
	}

	public boolean isMiTurno() {
		return miTurno;
	}

	public List<PaqueteItem> getItems() {
		return this.items;
	}

	@Override
	public void resolver(Object argumento) {
		Juego juego = ((EscuchaMensajes) argumento).getJuego();
		juego.getPersonaje().setEstado(Estado.estadoBatalla);
		Estado.setEstado(null);
		juego.setEstadoBatalla(new EstadoBatalla(juego, this));
		Estado.setEstado(juego.getEstadoBatalla());
	}
}
