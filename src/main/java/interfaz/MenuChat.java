package interfaz;

import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map.Entry;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;

import chat.client.UIClients;
import juego.Juego;
import mensajeria.ComandoActualizarPersonaje;
import mensajeria.PaqueteItem;
import mensajeria.PaquetePersonaje;
import recursos.Recursos;

public class MenuChat {

	private final int BOTON_POS_X = 580;
	private final int BOTON_POS_Y = 200;

	private final int BOTON_ANCHO = 180;
	private final int BOTON_ALTO = 120;

	
	private UIClients interfazChat;
	
	
	
	

	

	/**
	 * @wbp.parser.entryPoint
	 */
	public MenuChat(Juego juego) {
		this.interfazChat = juego.getInterfazChat();
	}

	
	public void graficarBoton(Graphics g) {
		g.drawImage(Recursos.botonChat, BOTON_POS_X, BOTON_POS_Y, BOTON_ANCHO, BOTON_ALTO, null);
	}

	/**
	 * @wbp.parser.entryPoint
	 */
	public boolean botonClickeado(int mouseX, int mouseY) {
		if (mouseX >= BOTON_POS_X && mouseX <= (BOTON_POS_X + BOTON_ANCHO) && mouseY >= BOTON_POS_Y
				&& mouseY <= (BOTON_POS_Y + BOTON_ALTO))
			return true;
		return false;
	}

	/**
	 * @wbp.parser.entryPoint
	 */
	public void mostrarChat() {
		interfazChat.setVisible(true);
	}
}
