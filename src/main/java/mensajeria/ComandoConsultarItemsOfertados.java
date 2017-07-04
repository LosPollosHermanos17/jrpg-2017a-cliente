package mensajeria;

import java.io.Serializable;

import cliente.EscuchaMensajes;

public class ComandoConsultarItemsOfertados extends Comando implements Serializable {
	
	private int idPersonajeSeleccionado;
	private PaquetePersonaje personajeActualizado;
	
	public ComandoConsultarItemsOfertados(int idPersonaje) {
		this.idPersonajeSeleccionado = idPersonaje;
	}
	
	@Override
	public void resolver(Object argumento) {
		EscuchaMensajes escuchaMensaje = (EscuchaMensajes) argumento;
		
		// En esta instancia, el servidor me devolvio en la variable personajeSeleccionado los
		// datos justamente del personaje seleccionado, ahora tengo que llamar al metodo
		// correspondiente de la clase MenuMercado para mostrar los items ofertados en la grilla
		
		escuchaMensaje.getJuego().getEstadoJuego().getMenuMercado().mostrarItemsOfertados(personajeActualizado);
		
	}

}
