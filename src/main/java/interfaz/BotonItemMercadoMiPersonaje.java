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

import com.google.gson.Gson;

import dominio.Item;
import juego.Juego;
import mensajeria.Comando;
import mensajeria.ComandoActualizarPersonaje;
import mensajeria.PaquetePersonaje;

public class BotonItemMercadoMiPersonaje extends JButton {
	
	private Juego juego;
	private JFrame ventanaMercado;
	private Item item;

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
					item.setOfertado(true);					
					
					// Obtengo Mi Personaje
					PaquetePersonaje paquetePersonaje = juego.getPersonaje();
					
					// Envio el comando para indicarle al servidor que actualice mi personaje en los demas clientes
					try {
						
						juego.getCliente().enviarComando(new ComandoActualizarPersonaje(juego.getPersonaje()));
						
					} catch (IOException ioe) {
						
						ioe.printStackTrace();
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
					item.setOfertado(false);
										
					// Obtengo Mi Personaje
					PaquetePersonaje paquetePersonaje = juego.getPersonaje();
										
					// Envio el comando para indicarle al servidor que actualice mi personaje en los demas clientes
					try {
						
						juego.getCliente().enviarComando(new ComandoActualizarPersonaje(juego.getPersonaje()));
						
					} catch (IOException ioe) {
						
						ioe.printStackTrace();
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

	public Item getItem() {
		return item;
	}

	public void setItem(Item item) {
		this.item = item;
	}
	
	

}
