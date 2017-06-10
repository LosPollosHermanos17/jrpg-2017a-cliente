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

import com.google.gson.Gson;

import dominio.Inventario;
import dominio.Item;
import juego.Juego;
import mensajeria.Comando;
import mensajeria.PaqueteItem;
import mensajeria.PaquetePersonaje;
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

	private final int PANEL_INVENTARIO_ANCHO = 400;
	private final int PANEL_INVENTARIO_ALTO = 400;

	private final int POS_GRILLA_ACCESORIO = 0;
	private final int POS_GRILLA_CABEZA = 1;
	private final int POS_GRILLA_MANOS1 = 3;
	private final int POS_GRILLA_PECHO = 4;
	private final int POS_GRILLA_MANOS2 = 5;
	private final int POS_GRILLA_PIES = 7;

	private Juego juego;
	private JFrame ventanaJuego;
	private JDialog ventanaInventario;
	private JPanel panelInventario;

	private HashMap<Integer, Integer> itemIdTipoPosicion;
	private HashMap<Integer, BotonItem> itemPosicionBoton;

	public MenuInventario(Juego juego) {

		this.juego = juego;
		this.ventanaJuego = juego.getPantalla().getFrame();

		this.panelInventario = new JPanel(new GridLayout(CANT_FILAS, CANT_COLUMNAS));
		this.panelInventario.setPreferredSize(new Dimension(PANEL_INVENTARIO_ANCHO, PANEL_INVENTARIO_ALTO));

		this.itemPosicionBoton = new HashMap<Integer, BotonItem>();
		this.itemIdTipoPosicion = new HashMap<Integer, Integer>();

		this.itemIdTipoPosicion.put(1, POS_GRILLA_MANOS1);
		this.itemIdTipoPosicion.put(2, POS_GRILLA_MANOS2);
		this.itemIdTipoPosicion.put(3, POS_GRILLA_PIES);
		this.itemIdTipoPosicion.put(4, POS_GRILLA_CABEZA);
		this.itemIdTipoPosicion.put(5, POS_GRILLA_PECHO);
		this.itemIdTipoPosicion.put(6, POS_GRILLA_ACCESORIO);

		inicializarInventario();
		inicializarVentana();
		actualizarInventario(this.juego.getPersonaje().getPaqueteInventario().getInventario());
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
		for (int i = 0; i < CANT_FILAS * CANT_COLUMNAS; i++) {
			BotonItem botonItem = new BotonItem(null, this, ventanaJuego);
			botonItem.setPosicionItem(i == POS_GRILLA_ACCESORIO || i == POS_GRILLA_CABEZA || i == POS_GRILLA_MANOS1
					|| i == POS_GRILLA_PECHO || i == POS_GRILLA_MANOS2 || i == POS_GRILLA_PIES);
			this.itemPosicionBoton.put(i, botonItem);
			panelInventario.add(botonItem);

		}
	}

	public void graficarBoton(Graphics g) {
		g.drawImage(Recursos.botonInventario, BOTON_POS_X, BOTON_POS_Y, BOTON_ANCHO, BOTON_ALTO, null);
	}

	public boolean botonClickeado(int mouseX, int mouseY) {
		if (mouseX >= BOTON_POS_X && mouseX <= (BOTON_POS_X + BOTON_ANCHO) && mouseY >= BOTON_POS_Y
				&& mouseY <= (BOTON_POS_Y + BOTON_ALTO))
			return true;

		return false;
	}

	public void mostrarInventario() {
		actualizarInventario(this.juego.getPersonaje().getPaqueteInventario().getInventario());
		ventanaInventario.setVisible(true);
	}

	public void actualizarInventario(Inventario inventario) {
		for (Entry<Integer, Item> entry : inventario.getItems().entrySet()) {
			int posicion = this.itemIdTipoPosicion.get(entry.getKey());
			BotonItem boton = this.itemPosicionBoton.get(posicion);
			boton.actualizarItem(entry.getValue());
		}
	}

	public void eliminarItem(Item item) {
		// quitar el item del personaje
		PaquetePersonaje paquetePersonaje = this.juego.getPersonaje();

		// Creo un item vacío para actualizar
		paquetePersonaje.getPaqueteInventario().getItems().put(item.getIdTipo(), new PaqueteItem(-1));
		paquetePersonaje.setComando(Comando.ACTUALIZARPERSONAJE);

		// Envío a actualizar al servidor
		try {
			this.juego.getCliente().getSalida()
					.writeObject(new Gson().toJson(paquetePersonaje, PaquetePersonaje.class));
		} catch (IOException e) {
			e.printStackTrace();
		}

		// Actualizo el inventario con el paquete que acabo de enviar
		this.actualizarInventario(paquetePersonaje.getPaqueteInventario().getInventario());
	}
}
