package interfaz;

import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;

import juego.Juego;
import mensajeria.ComandoDesofertarItem;
import mensajeria.ComandoOfertarItem;
import mensajeria.PaqueteItem;

public class BotonItemMercadoMiPersonaje extends JButton {
	
	private Juego juego;
	private JFrame ventanaMercado;
	private PaqueteItem item;

	public BotonItemMercadoMiPersonaje(Juego juego, JFrame ventanaMercado) {
		super();
		this.juego = juego;
		this.ventanaMercado = ventanaMercado;	
		this.item = null;		
		inicializarPopups();
	}

	private void inicializarPopups() {
		
		JButton boton = this;		
		JPopupMenu popupOfertar = new JPopupMenu();
		JPopupMenu popupDesofertar = new JPopupMenu();
		
		popupOfertar.add(new JMenuItem(new AbstractAction("Ofertar") {
			
			public void actionPerformed(ActionEvent e) {							
				
				int opcionSeleccionada = 0;
				
				if (boton.isEnabled() == true) {
				
					opcionSeleccionada = JOptionPane.showConfirmDialog(ventanaMercado, "¿Quieres ofertar este Item?",
							"Ofertar Item", JOptionPane.YES_NO_OPTION);
				}
				
				if (opcionSeleccionada == JOptionPane.YES_OPTION) {
						
					boton.setEnabled(false);
					
					// Le digo al servidor que cambie el estado del item
					try {
						
						juego.getCliente().enviarComando(new ComandoOfertarItem(juego.getPersonaje().getId(), item.getId()));
						
					} catch (IOException ioe) {
						
						JOptionPane.showMessageDialog(ventanaMercado, "Error al ofertar item, intentalo de nuevo.", 
								"Error al ofertar", JOptionPane.ERROR_MESSAGE);
					}
						
				}
			}
		
		}));
		
		popupDesofertar.add(new JMenuItem(new AbstractAction("Desofertar") {
			
			public void actionPerformed(ActionEvent e) {							
				
				int opcionSeleccionada = 0;
									
				opcionSeleccionada = JOptionPane.showConfirmDialog(ventanaMercado, "¿Quieres desofertar este Item?",
						"Desofertar Item", JOptionPane.YES_NO_OPTION);
				
				if (opcionSeleccionada == JOptionPane.YES_OPTION) {
						
					boton.setEnabled(true);
					
					// Le digo al servidor que cambie el estado del item
					try {
						
						juego.getCliente().enviarComando(new ComandoDesofertarItem(juego.getPersonaje().getId(), item.getId()));
						
					} catch (IOException ioe) {
						
						JOptionPane.showMessageDialog(ventanaMercado, "Error al desofertar item, intentalo de nuevo.", 
								"Error al desofertar", JOptionPane.ERROR_MESSAGE);
					}
										

				}
			}
		
		}));
		

		this.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				JButton b = (JButton) e.getSource();
				
				if (b.getIcon() != null && b.isEnabled() == true)
					popupOfertar.show(e.getComponent(), e.getX(), e.getY());
				
				if (b.getIcon() != null && b.isEnabled() == false)
					popupDesofertar.show(e.getComponent(), e.getX(), e.getY());
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
