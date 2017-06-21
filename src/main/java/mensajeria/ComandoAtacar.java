package mensajeria;

import java.io.Serializable;
import java.util.HashMap;

import cliente.EscuchaMensajes;
import juego.Juego;

public class ComandoAtacar extends Comando implements Serializable, Cloneable {
	
	private HashMap<String, Integer> atributosPersonaje = new HashMap<String, Integer>();
	private HashMap<String, Integer> atributosEnemigo = new HashMap<String, Integer>();
	
	public ComandoAtacar(int id, int idEnemigo, int nuevaSalud, int nuevaEnergia, int nuevaSaludEnemigo, int nuevaEnergiaEnemigo) {		
		atributosPersonaje.put("id", id);
		atributosPersonaje.put("salud", nuevaSalud);
		atributosPersonaje.put("energia", nuevaEnergia);
		atributosEnemigo.put("id", idEnemigo);
		atributosEnemigo.put("salud", nuevaSaludEnemigo);
		atributosEnemigo.put("energia", nuevaEnergiaEnemigo);
	}

	public HashMap<String, Integer> getAtributosPersonaje() {
		return this.atributosPersonaje;
	}

	public HashMap<String, Integer> getAtributosEnemigo() {
		return this.atributosEnemigo;
	}

	@Override
	public void resolver(Object argumento) {
		Juego juego = ((EscuchaMensajes)argumento).getJuego();
		juego.getEstadoBatalla().getEnemigo().actualizarAtributos(this.getAtributosPersonaje());
		juego.getEstadoBatalla().getPersonaje().actualizarAtributos(this.getAtributosEnemigo());
		juego.getEstadoBatalla().setMiTurno(true);
	}
}
