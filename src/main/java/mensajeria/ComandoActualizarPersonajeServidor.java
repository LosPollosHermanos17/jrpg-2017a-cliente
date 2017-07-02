package mensajeria;

import java.io.Serializable;

import cliente.EscuchaMensajes;
import juego.Juego;

public class ComandoActualizarPersonajeServidor extends Comando implements Serializable{
	
	private PaquetePersonaje paquetePersonaje;
	
	public  ComandoActualizarPersonajeServidor(PaquetePersonaje paquetePersonaje) { 
		this.paquetePersonaje = paquetePersonaje; 
	} 

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

}
