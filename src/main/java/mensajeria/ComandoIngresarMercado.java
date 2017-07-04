package mensajeria;

import java.io.Serializable;
import java.util.Map;
import java.util.Map.Entry;

import cliente.EscuchaMensajes;

public class ComandoIngresarMercado extends Comando implements Serializable {

	private int idPersonaje;
	
	public ComandoIngresarMercado(int idPersonaje) {
		this.idPersonaje = idPersonaje;
	}
	
	@Override
	public void resolver(Object argumento) {
		EscuchaMensajes escuchaMensaje = (EscuchaMensajes) argumento;
		
		// Obtengo el paquete personaje de la lista de usuarios conectados con el id del persona que ingresó al mercado
		PaquetePersonaje ingresante = escuchaMensaje.getPersonajesConectados().get(idPersonaje);
		
		// Actualizo que el personaje está en mercado ( para la parte grafica y no mostrarlo )
		ingresante.setComerciando(true);
		
		// Si no soy el personaje que acaba de ingresar al mercado	
		if ( escuchaMensaje.getJuego().getPersonaje().getId() != idPersonaje )
			
			// Le digo a la ventana de MenuMercado que agrege al personaje ( el se ocupa de si lo agrega o no... )
			escuchaMensaje.getJuego().getEstadoJuego().getMenuMercado().agregarPersonaje(ingresante);
		
		else {
			
			// Obtengo todos los personajes que ya se encuentran en el mercado y los agrego a la lista
			Map<Integer, PaquetePersonaje> personajesConectados = escuchaMensaje.getPersonajesConectados();
			
			for (Entry<Integer, PaquetePersonaje> entry : personajesConectados.entrySet()) {
				
				// Si no es el mismo y ademas se encuentra en estado comerciando, entonces se lo agrego
				if (entry.getValue().getId() != idPersonaje && entry.getValue().getComerciando() == true)
					escuchaMensaje.getJuego().getEstadoJuego().getMenuMercado().agregarPersonaje(entry.getValue());
			}
			
		}
	}
}