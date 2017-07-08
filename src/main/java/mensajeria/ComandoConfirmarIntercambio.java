package mensajeria;

import java.io.IOException;
import java.io.Serializable;

import javax.swing.JOptionPane;

import cliente.EscuchaMensajes;

public class ComandoConfirmarIntercambio extends Comando implements Serializable {

	private int idPersonajeSolicitante;
	private int idPersonajeSolicitado;
	private int idItemPersonajeSolicitante;
	private int idItemPersonajeSolicitado;
	private PaquetePersonaje personajeSolicitanteActualizado;
	private PaquetePersonaje personajeSolicitadoActualizado;
	
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
		
		// Si soy el personaje solicitante...
		if (escuchaMensaje.getJuego().getPersonaje().getId() == idPersonajeSolicitante) {
			
			escuchaMensaje.getJuego().getPersonaje().setIntercambiando(false);
			
			// Si no se toco el boton Cancelar...
			if (idItemPersonajeSolicitante != 0 && idItemPersonajeSolicitado != 0) {
			
				try {
					
					escuchaMensaje.getJuego().getCliente().enviarComando(new ComandoActualizarPersonaje(personajeSolicitanteActualizado));
					
					escuchaMensaje.getJuego().setPersonaje(personajeSolicitanteActualizado);
					
					escuchaMensaje.getJuego().getEstadoJuego().getMenuMercado().restaurarMenuMercado();				
					
					JOptionPane.showMessageDialog(escuchaMensaje.getJuego().getEstadoJuego().getMenuMercado().getVentanaMercado(), "¡Has realizado el intercambio con exito!", 
							"Intercambio exitoso", JOptionPane.INFORMATION_MESSAGE);
					
				} catch (IOException e) {
					
					e.printStackTrace();
					
				}

			} else {
				
				JOptionPane.showMessageDialog(escuchaMensaje.getJuego().getEstadoJuego().getMenuMercado().getVentanaMercado(), "<html><i>" + personajeSolicitado.getNombre() + "</i> ha rechazado la solicitud de intercambio.</html>", 
						"Intercambio rechazado", JOptionPane.WARNING_MESSAGE);
								
			}
				
			// Desbloqueo la lista de usuarios
			escuchaMensaje.getJuego().getEstadoJuego().getMenuMercado().getListaUsuarios().setEnabled(true);
						
			
		} else {
			
			// Si soy el personaje solicitado...
			if (escuchaMensaje.getJuego().getPersonaje().getId() == idPersonajeSolicitado) {
				
				escuchaMensaje.getJuego().getPersonaje().setIntercambiando(false);
				
				// Si no se toco el boton Cancelar...
				if (idItemPersonajeSolicitante != 0 && idItemPersonajeSolicitado != 0) {
					
					try {
						
						escuchaMensaje.getJuego().getCliente().enviarComando(new ComandoActualizarPersonaje(personajeSolicitadoActualizado));
						
						escuchaMensaje.getJuego().setPersonaje(personajeSolicitadoActualizado);
						
						escuchaMensaje.getJuego().getEstadoJuego().getMenuMercado().restaurarMenuMercado();				
						
						JOptionPane.showMessageDialog(escuchaMensaje.getJuego().getEstadoJuego().getMenuMercado().getVentanaMercado(), "¡Has realizado el intercambio con exito!", 
								"Intercambio exitoso", JOptionPane.INFORMATION_MESSAGE);
						
					} catch (IOException e) {
						
						e.printStackTrace();
					}
					
				}
					
				// Desbloqueo la lista de usuarios
				escuchaMensaje.getJuego().getEstadoJuego().getMenuMercado().getListaUsuarios().setEnabled(true);
				
				// Quito el boton Cancelar
				escuchaMensaje.getJuego().getEstadoJuego().getMenuMercado().quitarBotonCancelar();
				
			} else {
				
				// Si no soy el personaje solicitante ni el solicitado, debo actualizarlos en la lista
				
				// Si no se toco el boton Cancelar...
				if (idItemPersonajeSolicitante != 0 && idItemPersonajeSolicitado != 0) {
					
					escuchaMensaje.getJuego().getEstadoJuego().getMenuMercado().quitarPersonaje(personajeSolicitante);
//					escuchaMensaje.getJuego().getEstadoJuego().getMenuMercado().agregarPersonaje(personajeSolicitanteActualizado);
					
					escuchaMensaje.getJuego().getEstadoJuego().getMenuMercado().quitarPersonaje(personajeSolicitado);
//					escuchaMensaje.getJuego().getEstadoJuego().getMenuMercado().agregarPersonaje(personajeSolicitadoActualizado);	
					
				} else {
										
					escuchaMensaje.getJuego().getEstadoJuego().getMenuMercado().agregarPersonaje(personajeSolicitante);
					escuchaMensaje.getJuego().getEstadoJuego().getMenuMercado().agregarPersonaje(personajeSolicitado);					
					
				}
				
								
				
				
			}
		
		}

	}	

}
