package mensajeria;

import java.io.Serializable;
import java.util.Map;
import java.util.Map.Entry;

import cliente.EscuchaMensajes;

public class ComandoSolicitarIntercambio extends Comando implements Serializable {
	
	private int idPersonajeSolicitante;
	private int idPersonajeSolicitado;
	private int idItemSolicitado;
	
	public ComandoSolicitarIntercambio(int idPersonajeSolicitante, int idPersonajeSolicitado, int idItemSolicitado) {
		this.idPersonajeSolicitante = idPersonajeSolicitante;
		this.idPersonajeSolicitado = idPersonajeSolicitado;
		this.idItemSolicitado = idItemSolicitado;
	}
	
	@Override
	public void resolver(Object argumento) {
		EscuchaMensajes escuchaMensaje = (EscuchaMensajes) argumento;		
			
		// Si soy el personaje solicitado, se debe llamar al metodo recibirSolicitudIntercambio del mercado,
		// de lo contrario, se deben bloquear en mi lista los personajes solicitante y solicitado
		if ( escuchaMensaje.getJuego().getPersonaje().getId() == idPersonajeSolicitado ) {
		
			// En esta instancia, estoy recibiendo la solicitud de intercambio por
			// parte de otro usuario. En mi lista se me tiene que seleccionar automaticamente
			// el usuario solicitante, y me tiene que aparecer la opcion de rechazar solicitud
			// Sino la rechazo, tengo que seleccionar uno de sus items y clickear en completar
			// intercambio
			
			Map<Integer, PaqueteItem> itemsPersonajeSolicitado = escuchaMensaje.getJuego().getPersonaje().getPaqueteInventario().getItems();
			for (Entry<Integer, PaqueteItem> entry : itemsPersonajeSolicitado.entrySet()) {
				
				if (entry.getValue().getId() == idItemSolicitado)
					entry.getValue().setIntercambiar(true);
			}

			escuchaMensaje.getJuego().getEstadoJuego().getMenuMercado().recibirSolicitudIntercambio(idPersonajeSolicitante, idItemSolicitado);
		
		} else {
			
				// Cambio el estado de los personajes localmente
				escuchaMensaje.getPersonajesConectados().get(idPersonajeSolicitante).setIntercambiando(true);
				escuchaMensaje.getPersonajesConectados().get(idPersonajeSolicitado).setIntercambiando(true);
					
				PaquetePersonaje personajeSolicitante = escuchaMensaje.getPersonajesConectados().get(idPersonajeSolicitante);
				PaquetePersonaje personajeSolicitado = escuchaMensaje.getPersonajesConectados().get(idPersonajeSolicitado);
					
				escuchaMensaje.getJuego().getEstadoJuego().getMenuMercado().quitarPersonaje(personajeSolicitante);
				escuchaMensaje.getJuego().getEstadoJuego().getMenuMercado().quitarPersonaje(personajeSolicitado);					
			
		}
			
		
	}

}
