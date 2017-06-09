package interfaz;

import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Map;

import javax.swing.AbstractAction;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;

import dominio.Inventario;
import dominio.Item;
import recursos.Recursos;

public class MenuInventario {
	
	private final int BOTON_POS_X = 580; 
	private final int BOTON_POS_Y = 0;
	
	private final int BOTON_ANCHO = 180;
	private final int BOTON_ALTO = 120;
	
	private final int CANT_FILAS = 3;
	private final int CANT_COLUMNAS = 3;
	
	private final int VENTANA_ANCHO = 400;
	private final int VENTANA_ALTO = 400;
	
	private final int POS_GRILLA_ACCESORIO = 0;
	private final int POS_GRILLA_CABEZA = 1;
	private final int POS_GRILLA_MANOS1 = 3;
	private final int POS_GRILLA_PECHO = 4;
	private final int POS_GRILLA_MANOS2 = 5;
	private final int POS_GRILLA_PIES = 7;
	
	private JFrame ventanaJuego;
	private JDialog ventanaInventario;
	private JPanel panelInventario;
	private Map<Integer, Item> items;
	private Inventario inventario;
	
	
	public MenuInventario(JFrame v, Inventario i) {
		
		ventanaJuego = v;
		inventario = i;
		panelInventario = new JPanel(new GridLayout(CANT_FILAS, CANT_COLUMNAS));
				
		inicializarInventario();
		inicializarVentana();		
					
	}
	
	private void inicializarVentana() {
		
		ventanaInventario = new JDialog(ventanaJuego, "WOME - Inventario");
		ventanaInventario.setPreferredSize(new Dimension(VENTANA_ANCHO, VENTANA_ALTO));
		ventanaInventario.setResizable(false);
		ventanaInventario.setModalityType(Dialog.DEFAULT_MODALITY_TYPE);
		ventanaInventario.add(panelInventario);
		ventanaInventario.pack();
		ventanaInventario.setLocationRelativeTo(ventanaJuego);
	}
	

	private void inicializarInventario() {
		
		items = inventario.getItems();
		JButton botonItem;		
		
		JPopupMenu popup = new JPopupMenu();
		
		popup.add(new JMenuItem(new AbstractAction("Descartar Item") {
			
			int opcion = 0;
			
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showConfirmDialog(ventanaJuego, "¿Estás seguro que deseas eliminar este ítem?",  
                		"Descartar Item", JOptionPane.YES_NO_OPTION);
                
                if (opcion == JOptionPane.YES_OPTION) {
                
	                JMenuItem mi = (JMenuItem)e.getSource();
	                JPopupMenu p = (JPopupMenu)mi.getParent();
	                JButton b = (JButton)p.getInvoker();
	                
	                b.setIcon(null);
                
                }
                	
            }
        }));
		
		for (int i = 0 ; i < CANT_FILAS * CANT_COLUMNAS ; i++) {
			
			botonItem = new JButton();
				
			// si es una parte del cuerpo o accesorio en la grilla...
			if (i == POS_GRILLA_ACCESORIO || i == POS_GRILLA_CABEZA || i == POS_GRILLA_MANOS1 || i == POS_GRILLA_PECHO || i == POS_GRILLA_MANOS2 || i == POS_GRILLA_PIES) {
				
				if (i == 3)
					botonItem.setIcon(new ImageIcon(Recursos.espadaPlata));
				
				botonItem.addMouseListener(new MouseAdapter() {
					public void mousePressed(MouseEvent e) {
						
						JButton b = (JButton)e.getSource();
						
						if (b.getIcon() != null)
							popup.show(e.getComponent(), e.getX(), e.getY());
		            }
		        });
			
			} else {
				
				botonItem.setEnabled(false);				
			}
			
			panelInventario.add(botonItem);
		
		}
	
	}
	
	
	public void graficarBoton(Graphics g) {
		
		g.drawImage(Recursos.botonInventario, BOTON_POS_X, BOTON_POS_Y, BOTON_ANCHO, BOTON_ALTO, null);
		
	}
	
	
	public boolean botonClickeado(int mouseX, int mouseY){
		
		if ( mouseX >= BOTON_POS_X && mouseX <= ( BOTON_POS_X + BOTON_ANCHO )  &&
		     mouseY >= BOTON_POS_Y && mouseY <= ( BOTON_POS_Y + BOTON_ALTO ) )
			return true;
		
		return false;
	}
	
	
	public void mostrarInventario() {
		
		ventanaInventario.setVisible(true);
		
	}
	
	
	public void actualizarInventario() {
		
		for (Item item : items.values()) {
			
		}
		
		
	}
	
	
	

}
