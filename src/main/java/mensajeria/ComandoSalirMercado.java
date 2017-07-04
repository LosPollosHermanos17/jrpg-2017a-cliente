package mensajeria;

import java.io.Serializable;

import cliente.EscuchaMensajes;

public class ComandoSalirMercado extends Comando implements Serializable {
	
	private int idPersonaje;
	
	public ComandoSalirMercado(int idPersonaje)
	{
		this.idPersonaje = idPersonaje;
	}
	
	@Override
	public void resolver(Object argumento) {
		EscuchaMensajes escuchaMensaje = (EscuchaMensajes) argumento;
		
		// Obtengo el paquete personaje de la lista de usuarios conectados con el id del persona que salió del mercado
		PaquetePersonaje paquetePersonaje = escuchaMensaje.getPersonajesConectados().get(idPersonaje);
		
		// Actualizo que el personaje salió del mercado ( para mostrarlo nuevamente en el mapa )
		paquetePersonaje.setComerciando(false);
			
		// Le digo a la ventana de MenuMercado que quite el personaje ( el se ocupa de si lo quita o no... )
		escuchaMensaje.getJuego().getEstadoJuego().getMenuMercado().quitarPersonaje(paquetePersonaje);
	}

}
