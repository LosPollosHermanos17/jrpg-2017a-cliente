package interfaz;

import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.Map;
import java.util.Map.Entry;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;

import juego.Juego;
import mensajeria.ComandoConfirmarIntercambio;
import mensajeria.ComandoSolicitarIntercambio;
import mensajeria.PaqueteItem;
import mensajeria.PaquetePersonaje;

public class BotonItemMercadoOtroPersonaje extends JButton {
	
	private Juego juego;
	private JFrame ventanaMercado;
	private PaqueteItem item;
	private JList<PaquetePersonaje> listaUsuarios;

	public BotonItemMercadoOtroPersonaje(Juego juego, JFrame ventanaMercado, JList<PaquetePersonaje> listaUsuarios) {
		super();
		this.juego = juego;
		this.ventanaMercado = ventanaMercado;
		this.listaUsuarios = listaUsuarios;
		this.item = null;		
		inicializarPopups();
	}

	private void inicializarPopups() {
			
		JPopupMenu popupIntercambiar = new JPopupMenu();
		
		popupIntercambiar.add(new JMenuItem(new AbstractAction("Intercambiar") {
			
			public void actionPerformed(ActionEvent e) {							
				
				// Tengo que ver si el boton intercambiar se clickeo estando bloqueada la lista de usuarios.
				// Si la lista de usuarios esta bloqueada significa que debo enviar un comando de confirmacion
				// de intercambio, sino debo enviar un comando de solicitud de intercambio.
				if ( listaUsuarios.isEnabled() == true ) {
												
					// Me tengo que fijar dos cosas:
					// En primer lugar, que yo tenga por lo menos un item ofertado
					// En segundo lugar, que tenga por lo menos un item que sea compatible (que tenga el mismo idTipo)
					// Si no se cumple alguna de las dos condiciones, aparece un cartel rechazando el intercambio.
					
					boolean intercambioOK = false;
					
					Map<Integer, PaqueteItem> misItems = juego.getPersonaje().getPaqueteInventario().getItems();				
					
					// Me tengo que fijar si yo tengo un item que sea compatible con el item que clickee
					for (Entry<Integer, PaqueteItem> entry : misItems.entrySet()) {
						
						if (entry.getValue().getOfertado() == true && entry.getValue().getIdTipo() == item.getIdTipo()) 
									intercambioOK = true;
							
					}
					
						
					if (intercambioOK) {
						
						int opcionSeleccionada = JOptionPane.showConfirmDialog(ventanaMercado, "¿Quieres solicitar un intercambio para este item?",
								"Solicitud de Intercambio", JOptionPane.YES_NO_OPTION);
					
						if (opcionSeleccionada == JOptionPane.YES_OPTION) {
							
							// Bloqueo la lista de usuarios
							listaUsuarios.setEnabled(false);
							
							// Cambio mi estado a "intercambiando"
							juego.getPersonaje().setIntercambiando(true);
						
							// Envio el comando avisando a todos los usuarios de la solicitud
							try {
								int idUsuarioSolicitante = juego.getPersonaje().getId();
								int idUsuarioSolicitado = listaUsuarios.getSelectedValue().getId();
								int idItem = item.getId();

								juego.getCliente().enviarComando(new ComandoSolicitarIntercambio(idUsuarioSolicitante, idUsuarioSolicitado, idItem));
								
								
								
							} catch (IOException ioe) {
								
								JOptionPane.showMessageDialog(ventanaMercado, "Error al solicitar intercambio de item, intentalo de nuevo.", 
										"Error al solicitar intercambio", JOptionPane.ERROR_MESSAGE);
							}
						}
					} else {
						
						JOptionPane.showMessageDialog(ventanaMercado, "Debes ofertar por lo menos un item compatible con el que deseas.", 
								"Aviso", JOptionPane.WARNING_MESSAGE);
					}
					
				} else {
					
					boolean intercambioOK = false;
					
					PaqueteItem itemPersonajeSolicitante = item;
					PaqueteItem itemPersonajeSolicitado = null;
					
					// Obtengo el item mio que quiere el otro jugador
					Map<Integer, PaqueteItem> misItems = juego.getPersonaje().getPaqueteInventario().getItems();						
					for (Entry<Integer, PaqueteItem> entry : misItems.entrySet()) {
														
						if (entry.getValue().getIntercambiar() == true)
							itemPersonajeSolicitado = entry.getValue();							
					}
					
					if (itemPersonajeSolicitante.getIdTipo() == itemPersonajeSolicitado.getIdTipo())
						intercambioOK = true;
					
					if (intercambioOK) {
					
						int opcionSeleccionada = JOptionPane.showConfirmDialog(ventanaMercado, "¿Quieres confirmar el intercambio por este item?",
								"Confirmacion de Intercambio", JOptionPane.YES_NO_OPTION);
					
						if (opcionSeleccionada == JOptionPane.YES_OPTION) {
						
							int idUsuarioSolicitante = listaUsuarios.getSelectedValue().getId();
							int idUsuarioSolicitado = juego.getPersonaje().getId();
						
							// Envio el comando para confirmar el intercambio
							try {
											
								juego.getCliente().enviarComando(new ComandoConfirmarIntercambio(idUsuarioSolicitante, idUsuarioSolicitado, itemPersonajeSolicitante.getId(), itemPersonajeSolicitado.getId()));
							
							} catch (IOException ioe) {
							
								JOptionPane.showMessageDialog(ventanaMercado, "Error al confirmar intercambio de item, intentalo de nuevo.", 
									"Error al confirmar intercambio", JOptionPane.ERROR_MESSAGE);
							}
						
						}
						
					} else {
						
						JOptionPane.showMessageDialog(ventanaMercado, "Debes seleccionar uno que sea del mismo tipo que el item <html><i>" + itemPersonajeSolicitado + "</i></html>.", 
								"Aviso", JOptionPane.WARNING_MESSAGE);													
					}
					
				}
				
			} // END actionPerformed
		
		}));
		

		this.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				JButton b = (JButton) e.getSource();
				
				if (b.getIcon() != null && b.isEnabled() == true)
					popupIntercambiar.show(e.getComponent(), e.getX(), e.getY());
				
			}
		});
	}

	public PaqueteItem getItem() {
		return item;
	}

	public void setItem(PaqueteItem item) {
		this.item = item;
	}

}
