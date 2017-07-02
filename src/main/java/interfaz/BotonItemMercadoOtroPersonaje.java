package interfaz;

import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;

import juego.Juego;
import mensajeria.PaqueteItem;

public class BotonItemMercadoOtroPersonaje extends JButton {
	
	private Juego juego;
	private JFrame ventanaMercado;
	private PaqueteItem item;

	public BotonItemMercadoOtroPersonaje(Juego juego, JFrame ventanaMercado) {
		super();
		this.juego = juego;
		this.ventanaMercado = ventanaMercado;	
		this.item = null;		
		inicializarPopups();
	}

	private void inicializarPopups() {
		
		JButton boton = this;		
		JPopupMenu popupIntercambiar = new JPopupMenu();
		
		popupIntercambiar.add(new JMenuItem(new AbstractAction("Intercambiar") {
			
			public void actionPerformed(ActionEvent e) {							
				
				int opcionSeleccionada = 0;
				
				if (boton.isEnabled() == true) {
				
					opcionSeleccionada = JOptionPane.showConfirmDialog(ventanaMercado, "¿Quieres intercambiar por este item?",
							"Intercambiar Item", JOptionPane.YES_NO_OPTION);
				}
				
				if (opcionSeleccionada == JOptionPane.YES_OPTION) {
											
					// Envio el comando para indicarle al servidor que actualice los items ofertados de mi personaje

						
				}
			}
		
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
