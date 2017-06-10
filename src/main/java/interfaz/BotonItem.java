package interfaz;

import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.HashMap;

import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;

import dominio.Item;
import estados.Estado;
import mensajeria.Comando;
import mensajeria.PaqueteMovimiento;
import mensajeria.PaquetePersonaje;
import recursos.Recursos;

public class BotonItem extends JButton {

	private Item item;
	private JPopupMenu popup;
	private boolean posicionItem;
	private MenuInventario menuInventario;

	public BotonItem(Item item, MenuInventario menuInventario, JFrame ventanaJuego) {
		super();
		this.item = item;
		this.menuInventario = menuInventario;
		this.actualizarItem(item);
		this.inicializarPopup(ventanaJuego);
	}

	private void inicializarPopup(JFrame ventanaJuego) {
		popup = new JPopupMenu();
		popup.add(new JMenuItem(new AbstractAction("Descartar Item") {
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showConfirmDialog(ventanaJuego, "¿Estás seguro que deseas eliminar este ítem?",
						"Descartar Item", JOptionPane.YES_NO_OPTION);
				if (0 == JOptionPane.YES_OPTION) {
					menuInventario.eliminarItem(item);
				}
			}
		}));

		this.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				JButton b = (JButton) e.getSource();
				if (b.getIcon() != null)
					popup.show(e.getComponent(), e.getX(), e.getY());
			}
		});
	}

	public void actualizarItem(Item item) {
		this.item = item;
		if (item != null && item.getId() > 0) {
			this.setIcon(new ImageIcon(Recursos.items.get(item.getNombre())));
			this.setEnabled(true);
			this.setToolTipText(item.getNombre());
		} else {
			this.setIcon(null);
			this.setEnabled(posicionItem);
			this.setToolTipText(null);
		}
	}

	public void setPosicionItem(boolean posicionItem) {
		this.posicionItem = posicionItem;
		this.setEnabled(posicionItem);
	}
}
