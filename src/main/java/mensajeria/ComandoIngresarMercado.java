package mensajeria;

import java.io.Serializable;

import cliente.EscuchaMensajes;

public class ComandoIngresarMercado extends Comando implements Serializable {

	private int idPersonaje;
	
	public ComandoIngresarMercado(int idPersonaje)
	{
		this.idPersonaje = idPersonaje;
	}
	
	@Override
	public void resolver(Object argumento) {
		EscuchaMensajes escuchaMensaje = (EscuchaMensajes) argumento;
		// Obtengo el paquete personaje de la lista de usuarios conectados con el id del persona que ingresó al mercado
		PaquetePersonaje paquetePersonaje = escuchaMensaje.getPersonajesConectados().get(idPersonaje);
		// Actualizo que el personaje está en mercado ( para la parte grafica y no mostrarlo )
		paquetePersonaje.setComerciando(true);
		
		// Si no soy el personaje que acaba de ingresar al mercado	
		if( escuchaMensaje.getJuego().getPersonaje().getId() != idPersonaje)
			// Le digo a la ventana de MenuMercado que agrege al personaje ( el se ocupa de si lo agrega o no... )
			escuchaMensaje.getJuego().getEstadoJuego().getMenuMercado().añadirPersonaje(paquetePersonaje);
	}
}