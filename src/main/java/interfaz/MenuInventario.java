package interfaz;

import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;

import mensajeria.PaqueteInventario;
import recursos.Recursos;

public class MenuInventario {
	
	private final int BOTON_POS_X = 580; 
	private final int BOTON_POS_Y = 0;
	
	private final int BOTON_ANCHO = 180;
	private final int BOTON_ALTO = 120;
	
	private final int CANT_FILAS = 2;
	private final int CANT_COLUMNAS = 3;
	
	private final int VENTANA_ANCHO = 400;
	private final int VENTANA_ALTO = 300;
	
	private final int MAX_CANT_ITEMS = 6;
	
	private BufferedImage botonInventario;
	private JFrame ventanaJuego;
	private JDialog ventanaInventario;
	private JPanel panelInventario;
	private JButton[] items;
	private PaqueteInventario inventario;
	
	private int cantidadItems;
	
	
	public MenuInventario(JFrame ventana, PaqueteInventario inv) {
		
		botonInventario = Recursos.botonInventario;
		panelInventario = new JPanel(new GridLayout(CANT_FILAS, CANT_COLUMNAS));
		items = new JButton[CANT_FILAS * CANT_COLUMNAS];
		inventario = inv;
		ventanaJuego = ventana;
				
		inicializarVentana();
		inicializarInventario();
					
	}
	
	private void inicializarVentana() {
		
		ventanaInventario = new JDialog(ventanaJuego);
		ventanaInventario.setPreferredSize(new Dimension(VENTANA_ANCHO, VENTANA_ALTO));
		ventanaInventario.setResizable(false);
		ventanaInventario.setModalityType(Dialog.DEFAULT_MODALITY_TYPE);
		ventanaInventario.setUndecorated(true);
		ventanaInventario.add(panelInventario);
		ventanaInventario.pack();
		ventanaInventario.setLocationRelativeTo(ventanaJuego);
	}
	

	private void inicializarInventario() {
		
		JButton botonItem;
		
		for (int i = 0 ; i < MAX_CANT_ITEMS ; i++) {
			
			botonItem = new JButton("Vacio");
			JPopupMenu popup = new JPopupMenu();
	        popup.add(new JMenuItem(new AbstractAction("Descartar Item") {
	            public void actionPerformed(ActionEvent e) {
	                JOptionPane.showConfirmDialog(ventanaJuego, "¿Seguro que deseas eliminar este item?");
	            }
	        }));
			
			botonItem.addMouseListener(new MouseAdapter() {
				public void mousePressed(MouseEvent e) {
	                popup.show(e.getComponent(), e.getX(), e.getY());
	            }
	        });
			
			// agrego el boton al panel (el layout se encargara de acomodarlo como corresponda)
			panelInventario.add(botonItem);
		}
			
		
	}
	
	
	public void graficarBoton(Graphics g) {
		
		g.drawImage(botonInventario, BOTON_POS_X, BOTON_POS_Y, BOTON_ANCHO, BOTON_ALTO, null);
		
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
	
	
	
	

}
