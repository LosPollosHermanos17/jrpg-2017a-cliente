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
import recursos.Recursos;

public class BotonItem extends JButton {

	private Item item;
	private HashMap<Integer, BufferedImage> tipoImagenes;
	private JPopupMenu popup;
	private boolean posicionItem;

	public BotonItem(Item item, JFrame ventanaJuego) {
		super();
		this.item = item;
		this.actualizarItem(item);
		this.inicializarPopup(ventanaJuego);

		this.tipoImagenes = new HashMap<Integer, BufferedImage>();
		this.tipoImagenes.put(1, Recursos.espadaPlata);
		this.tipoImagenes.put(2, Recursos.espadaPlata);
		this.tipoImagenes.put(3, Recursos.espadaPlata);
		this.tipoImagenes.put(4, Recursos.espadaPlata);
		this.tipoImagenes.put(5, Recursos.espadaPlata);
	}

	private void inicializarPopup(JFrame ventanaJuego) {
		popup = new JPopupMenu();
		popup.add(new JMenuItem(new AbstractAction("Descartar Item") {
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showConfirmDialog(ventanaJuego, "¿Estás seguro que deseas eliminar este ítem?",
						"Descartar Item", JOptionPane.YES_NO_OPTION);
				if (0 == JOptionPane.YES_OPTION) {

					JMenuItem mi = (JMenuItem) e.getSource();
					JPopupMenu p = (JPopupMenu) mi.getParent();
					JButton b = (JButton) p.getInvoker();
					b.setIcon(null);
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
		if (item != null) {
			this.setIcon(new ImageIcon(this.tipoImagenes.get(item.getTipo())));
			this.setEnabled(true);
			this.setToolTipText(item.getNombre());
		} else {
			this.setIcon(null);
			this.setEnabled(posicionItem);
			this.setToolTipText(null);
		}
	}
	
	public void setPosicionItem(boolean posicionItem)
	{
		this.posicionItem = posicionItem;
		this.setEnabled(posicionItem);
	}
}
