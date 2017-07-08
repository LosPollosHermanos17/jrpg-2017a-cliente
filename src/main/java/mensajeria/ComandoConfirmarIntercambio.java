package mensajeria;

import java.io.IOException;
import java.io.Serializable;

import cliente.EscuchaMensajes;

public class ComandoConfirmarIntercambio extends Comando implements Serializable {

	private int idPersonajeSolicitante;
	private int idPersonajeSolicitado;
	private int idItemPersonajeSolicitante;
	private int idItemPersonajeSolicitado;
	
	public ComandoConfirmarIntercambio(int idPersonajeSolicitante, int idPersonajeSolicitado, int idItemPersonajeSolicitante, int idItemPersonajeSolicitado) {
		this.idPersonajeSolicitante = idPersonajeSolicitante;
		this.idPersonajeSolicitado = idPersonajeSolicitado;
		this.idItemPersonajeSolicitante = idItemPersonajeSolicitante;
		this.idItemPersonajeSolicitado = idItemPersonajeSolicitado;
	}
	
	@Override
	public void resolver(Object argumento) {
		
		// Los personajes participantes deben desbloquear la lista
		// Los otros personajes deben volver a agregarlos.
		// Dentro del metodo mostrar que llaman los personajes intervinientes, se llama al comando
		// ingresarMercado que ya se encarga de avisarle a los demas que los vuelvan a agregar en la lista
		
		EscuchaMensajes escuchaMensaje = (EscuchaMensajes) argumento;
		
		PaquetePersonaje personajeSolicitante = escuchaMensaje.getPersonajesConectados().get(idPersonajeSolicitante);
		PaquetePersonaje personajeSolicitado = escuchaMensaje.getPersonajesConectados().get(idPersonajeSolicitado);
		
		// Si soy el personaje solicitante, debo cambiar mi estado y refrescar el solicitado
		if (escuchaMensaje.getJuego().getPersonaje().getId() == idPersonajeSolicitante) {
			
			escuchaMensaje.getJuego().getPersonaje().setIntercambiando(false);
			
			// Si no se toco el boton Cancelar...
			if (idItemPersonajeSolicitante != 0 && idItemPersonajeSolicitado != 0) {
			
				try {
					escuchaMensaje.getJuego().getCliente().enviarComando(new ComandoActualizarPersonaje(personajeSolicitante));
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}				

			}
									
			escuchaMensaje.getJuego().getEstadoJuego().getMenuMercado().mostrar(escuchaMensaje.getJuego());
			
		} 
			
		if (escuchaMensaje.getJuego().getPersonaje().getId() == idPersonajeSolicitado) {
			
			escuchaMensaje.getJuego().getPersonaje().setIntercambiando(false);
			
			// Si no se toco el boton Cancelar...
			if (idItemPersonajeSolicitante != 0 && idItemPersonajeSolicitado != 0) {
				
				try {
					escuchaMensaje.getJuego().getCliente().enviarComando(new ComandoActualizarPersonaje(personajeSolicitado));
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
			
			escuchaMensaje.getJuego().getEstadoJuego().getMenuMercado().mostrar(escuchaMensaje.getJuego());
			
		} 		

	}	

}
