package interfaz;

import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import mensajeria.PaquetePersonaje;
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
	private JDialog ventanaInventario;
	private JPanel panelInventario;
	private JButton[] items;
	
	private int cantidadItems;
	
	
	public MenuInventario(JFrame ventanaJuego, PaquetePersonaje paquete) {
		
		botonInventario = Recursos.botonInventario;
		ventanaInventario = new JDialog(ventanaJuego, "WOME - Inventario " + paquete.getNombre());
		panelInventario = new JPanel(new GridLayout(CANT_FILAS, CANT_COLUMNAS));
		items = new JButton[CANT_FILAS * CANT_COLUMNAS];
		
		for (int i = 0 ; i < CANT_FILAS * CANT_COLUMNAS ; i++) {
			
			JButton boton = new JButton("Vacio");
	
			boton.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {					
//					removerItem(boton);				
				}
				
			});
			
			panelInventario.add(boton);
			items[i] = boton;
		}
			
		ventanaInventario.setPreferredSize(new Dimension(VENTANA_ANCHO, VENTANA_ALTO));
		ventanaInventario.setResizable(false);
		ventanaInventario.setModalityType(Dialog.DEFAULT_MODALITY_TYPE);
		ventanaInventario.add(panelInventario);
		ventanaInventario.pack();
		ventanaInventario.setLocationRelativeTo(ventanaJuego);
		
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
	
	
	public void agregarItem(int idItem) {		
		if (cantidadItems == MAX_CANT_ITEMS)
			JOptionPane.showMessageDialog(null, "Debes descartar un item para agregarlo a tu inventario", "Inventario lleno!", JOptionPane.WARNING_MESSAGE);
		else
			cantidadItems++;		
	}
	
	private void removerItem(int Iditem) {
		cantidadItems--;
	}
	

}
