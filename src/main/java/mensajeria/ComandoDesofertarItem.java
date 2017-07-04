package mensajeria;

import java.io.Serializable;

import cliente.EscuchaMensajes;

public class ComandoDesofertarItem extends Comando implements Serializable {
	
	private int idPersonaje;
	private int idItem;
	private PaquetePersonaje personajeActualizado;
	
	public ComandoDesofertarItem(int idPersonaje, int idItem) {
		this.idPersonaje = idPersonaje;
		this.idItem = idItem;
	}

	@Override
	public void resolver(Object argumento) {
		EscuchaMensajes escuchaMensaje = (EscuchaMensajes) argumento;
		
		// Tengo que ver si casualmente yo tengo al usuario que actualizo el estado del item seleccionado
		
		// El metodo se encargara de corroborar si justo esta seleccionado el ofertante
		escuchaMensaje.getJuego().getEstadoJuego().getMenuMercado().mostrarItemsOfertados(personajeActualizado);			
		
	}

}
