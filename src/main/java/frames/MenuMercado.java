package frames;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.Map.Entry;

import javax.swing.BorderFactory;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.ListSelectionModel;
import javax.swing.border.Border;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import interfaz.BotonItemMercadoMiPersonaje;
import juego.Juego;
import mensajeria.ComandoActualizarPersonaje;
import mensajeria.ComandoObtenerPersonajeActualizado;
import mensajeria.PaqueteItem;
import mensajeria.PaquetePersonaje;
import recursos.Recursos;

public class MenuMercado {
	
	private final int VENTANA_ANCHO = 800;
	private final int VENTANA_ALTO = 600;

	private final int PANEL_ANCHO = 800;
	private final int PANEL_ALTO = 600;
	
	private final int PANEL_USUARIOS_ANCHO = 200;
	private final int PANEL_USUARIOS_ALTO = 600;
	
	private final int PANEL_ITEMS_ANCHO = 600;
	private final int PANEL_ITEMS_ALTO = 600;
	
	private final int CANT_FILAS_GRILLA_ITEMS = 5;
	private final int CANT_COLUMNAS_GRILLA_ITEMS = 4;
	
	private Juego juego;
	
	// ventana principal
	private JFrame ventanaMercado;
	
	// panel principal
	private JSplitPane panelMercado; 
	
	// panel seccion izquierda
	private JPanel panelUsuarios;
	private JLabel tituloPanelUsuarios;
	private JScrollPane scrollPanelUsuarios;
	private JList<PaquetePersonaje> listaUsuarios;
	
	// panel seccion derecha
	private JSplitPane panelItems;
	
	private JPanel panelItemsOtroPersonaje;
	private JLabel tituloPanelItemsOtroPersonaje;
	private JPanel grillaItemsOtroPersonaje;	
	private ArrayList<JButton> slotsItemsOtroPersonaje;
	private JScrollPane scrollPanelItemsOtroPersonaje;
	
	private JPanel panelItemsMiPersonaje;
	private JLabel tituloPanelItemsMiPersonaje;
	private JPanel grillaItemsMiPersonaje;		
	private ArrayList<BotonItemMercadoMiPersonaje> slotsItemsMiPersonaje;
	private JScrollPane scrollPanelItemsMiPersonaje;
	
	
	
	
	public MenuMercado(Juego juego) {

		this.juego = juego;
		
		// Panel de Usuarios (seccion izquierda de la ventana)
		
		listaUsuarios = new JList<PaquetePersonaje>(new DefaultListModel<PaquetePersonaje>());
		listaUsuarios.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		listaUsuarios.setSelectedIndex(0);
		listaUsuarios.setCellRenderer(new DefaultListCellRenderer() {
			@Override
			public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
				Component renderer = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
				if (renderer instanceof JLabel && value instanceof PaquetePersonaje) {
					((JLabel) renderer).setText(((PaquetePersonaje) value).getNombre());
				}
				return renderer;
			}
		});
		listaUsuarios.addListSelectionListener(new ListSelectionListener() {
			
			@Override
			public void valueChanged(ListSelectionEvent e) {
				
				PaquetePersonaje personajeSeleccionado = listaUsuarios.getSelectedValue();
								
				// Solo realizo los siguientes pasos si la opcion seleccionada corresponde a un Personaje
				if (personajeSeleccionado != null) {
					
					try {
						
						juego.getCliente().enviarComando(new ComandoObtenerPersonajeActualizado(personajeSeleccionado));
						
					} catch (IOException e1) {
						
						e1.printStackTrace();
					}
					
					Map<Integer, PaqueteItem> itemsPersonaje = personajeSeleccionado.getPaqueteInventario().getItems();
					int cantItemsEnGrilla = 0;
					
					// Saco de la grilla de Otro Personaje los items y los tooltip del otro jugador
					for (JButton slotItem : slotsItemsOtroPersonaje) {			
						slotItem.setIcon(null);
						slotItem.setToolTipText(null);
					}
						
					// Muestro los items del jugador seleccionado en la grilla
					for (Entry<Integer, PaqueteItem> entry : itemsPersonaje.entrySet()) {
						
						PaqueteItem item = entry.getValue();
						
						if (item != null && item.getId() > 0) {
							
							// Si el item esta ofertado...
							if (item.getEstaOfertado() == true) {
								
								String descripcion = obtenerDescripcionItem(item);
								slotsItemsOtroPersonaje.get(cantItemsEnGrilla).setIcon(new ImageIcon(Recursos.items.get(item.getNombre())));
								slotsItemsOtroPersonaje.get(cantItemsEnGrilla).setToolTipText(descripcion);
								cantItemsEnGrilla++;
								
							}
							
						}
						
					}
				
					ventanaMercado.revalidate(); // Esto es para refrescar las imagenes
					
				}
				
			}
			
		});
		
		tituloPanelUsuarios = new JLabel("Jugadores");
		tituloPanelUsuarios.setFont(new Font("Rockwell Extra Bold", Font.PLAIN, 24));
	    Border border = BorderFactory.createLineBorder(Color.BLACK);
	    tituloPanelUsuarios.setBorder(border);	    
	    tituloPanelUsuarios.setPreferredSize(new Dimension(PANEL_USUARIOS_ANCHO, 50));
	    tituloPanelUsuarios.setHorizontalAlignment(JLabel.CENTER);
	    tituloPanelUsuarios.setVerticalAlignment(JLabel.CENTER);
		
		panelUsuarios = new JPanel(new BorderLayout());		
		panelUsuarios.add(tituloPanelUsuarios, BorderLayout.NORTH);
		panelUsuarios.add(listaUsuarios, BorderLayout.CENTER);

		scrollPanelUsuarios = new JScrollPane(panelUsuarios, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scrollPanelUsuarios.setPreferredSize(new Dimension(PANEL_USUARIOS_ANCHO, PANEL_USUARIOS_ALTO));
		
		
		
		// Panel de Items (seccion derecha de la ventana)
		
		//// Panel de Items Otro Personaje
		
		tituloPanelItemsOtroPersonaje = new JLabel("Items Ofertados");
		tituloPanelItemsOtroPersonaje.setFont(new Font("Rockwell Extra Bold", Font.PLAIN, 24));
		tituloPanelItemsOtroPersonaje.setBorder(border);
		tituloPanelItemsOtroPersonaje.setPreferredSize(new Dimension(PANEL_ITEMS_ANCHO, 50));
		tituloPanelItemsOtroPersonaje.setHorizontalAlignment(JLabel.CENTER);
		tituloPanelItemsOtroPersonaje.setVerticalAlignment(JLabel.CENTER);
		
		grillaItemsOtroPersonaje = new JPanel(new GridLayout(CANT_FILAS_GRILLA_ITEMS, CANT_COLUMNAS_GRILLA_ITEMS));
				
		slotsItemsOtroPersonaje = new ArrayList<JButton>(); 		
		for (int i = 0 ; i < CANT_FILAS_GRILLA_ITEMS * CANT_COLUMNAS_GRILLA_ITEMS ; i++) {			
			JButton itemDisplay = new JButton();
			slotsItemsOtroPersonaje.add(itemDisplay); 
			grillaItemsOtroPersonaje.add(itemDisplay);			
		}
					
		panelItemsOtroPersonaje = new JPanel(new BorderLayout());		
		panelItemsOtroPersonaje.add(tituloPanelItemsOtroPersonaje, BorderLayout.NORTH);
		panelItemsOtroPersonaje.add(grillaItemsOtroPersonaje, BorderLayout.CENTER);
		panelItemsOtroPersonaje.setPreferredSize(new Dimension(PANEL_ITEMS_ANCHO, PANEL_ITEMS_ALTO));
		
		scrollPanelItemsOtroPersonaje = new JScrollPane(panelItemsOtroPersonaje, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		
		
		//// Panel de Items Mi Personaje
		
		tituloPanelItemsMiPersonaje = new JLabel("Mis Items");
		tituloPanelItemsMiPersonaje.setFont(new Font("Arial Black", Font.BOLD, 24));
		tituloPanelItemsMiPersonaje.setBorder(border);
		tituloPanelItemsMiPersonaje.setPreferredSize(new Dimension(PANEL_ITEMS_ANCHO, 50));
		tituloPanelItemsMiPersonaje.setHorizontalAlignment(JLabel.CENTER);
		tituloPanelItemsMiPersonaje.setVerticalAlignment(JLabel.CENTER);
		
		grillaItemsMiPersonaje = new JPanel(new GridLayout(CANT_FILAS_GRILLA_ITEMS, CANT_COLUMNAS_GRILLA_ITEMS));
		
		slotsItemsMiPersonaje = new ArrayList<BotonItemMercadoMiPersonaje>(); 		
		for (int i = 0 ; i < CANT_FILAS_GRILLA_ITEMS * CANT_COLUMNAS_GRILLA_ITEMS ; i++) {
			BotonItemMercadoMiPersonaje itemDisplay = new BotonItemMercadoMiPersonaje(juego, ventanaMercado);			
			slotsItemsMiPersonaje.add(itemDisplay); 
			grillaItemsMiPersonaje.add(itemDisplay);			
		}
		
		panelItemsMiPersonaje = new JPanel(new BorderLayout());
		panelItemsMiPersonaje.add(tituloPanelItemsMiPersonaje, BorderLayout.NORTH);
		panelItemsMiPersonaje.add(grillaItemsMiPersonaje, BorderLayout.CENTER);
		panelItemsMiPersonaje.setPreferredSize(new Dimension(PANEL_ITEMS_ANCHO, PANEL_ITEMS_ALTO));
		
		scrollPanelItemsMiPersonaje = new JScrollPane(panelItemsMiPersonaje, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		
		
		//// Panel de Items
		
		panelItems = new JSplitPane(JSplitPane.VERTICAL_SPLIT, scrollPanelItemsOtroPersonaje, scrollPanelItemsMiPersonaje);
		panelItems.setDividerLocation(PANEL_ITEMS_ALTO/2);
		panelItems.setEnabled(false); // esto es para evitar que se pueda mover el divisor
		
		
		// Panel del Mercado (panel principal)
		
		panelMercado = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, scrollPanelUsuarios, panelItems);
		panelMercado.setPreferredSize(new Dimension(PANEL_ANCHO, PANEL_ALTO));
		panelMercado.setDividerLocation(PANEL_USUARIOS_ANCHO);
		panelMercado.setEnabled(false); // esto es para evitar que se pueda mover el divisor
		
		inicializarVentana();
	}

	private void inicializarVentana() {

		ventanaMercado = new JFrame("WOME - Mercado de Intercambios");
		ventanaMercado.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		ventanaMercado.setPreferredSize(new Dimension(VENTANA_ANCHO, VENTANA_ALTO));
		ventanaMercado.setResizable(false);
		
		ventanaMercado.addWindowListener(new WindowAdapter() {
			
			public void windowClosing(WindowEvent e) {
				
				int opcionSeleccionada = JOptionPane.showConfirmDialog(null, "¿Estás seguro que deseas salir del Mercado de Intercambios?",
						"Salir", JOptionPane.YES_NO_OPTION);
			
				if (opcionSeleccionada == JOptionPane.YES_OPTION) {				
					ventanaMercado.setVisible(false); // oculto la ventana del mercado
					juego.getPantalla().getFrame().setVisible(true); // vuelvo a mostrar la ventana principal del juego
					
					// Indico que mi personaje ya no se encuentra en el mercado
					PaquetePersonaje personaje = juego.getPersonaje();
					personaje.setSeEncuentraEnMercado(false);
					
					// Envío a actualizar al servidor
					try {
						juego.getCliente().enviarComando(new ComandoActualizarPersonaje(personaje));
					} catch (IOException ioe) {
						ioe.printStackTrace();
					}
					
				}
								
			}
		});
		
		ventanaMercado.add(panelMercado);
		ventanaMercado.pack();
		ventanaMercado.setLocationRelativeTo(null);
	}
	
	public void mostrar(Juego juego) {
		
		// Indico que mi personaje se encuentra en el mercado
		PaquetePersonaje personaje = juego.getPersonaje();
		personaje.setSeEncuentraEnMercado(true);
		
		// Envío a actualizar al servidor
		try {
			this.juego.getCliente().enviarComando(new ComandoActualizarPersonaje(personaje));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		Map<Integer, PaquetePersonaje> personajesConectados;
		
		juego.getPantalla().getFrame().setVisible(false); // oculto la ventana principal del juego
		ventanaMercado.setVisible(true); // muestro la ventana correspondiente al mercado
		
		// saco de la lista los nombres de los personajes que hayan quedado de antes
		((DefaultListModel<PaquetePersonaje>)listaUsuarios.getModel()).clear();
		
		// saco de la grilla de Otro Personaje los items y los tooltips que hayan quedado de antes
		for (JButton slotItem : slotsItemsOtroPersonaje) {			
			slotItem.setIcon(null);
			slotItem.setToolTipText(null);
		}
		
		// saco de la grilla de Mi Personaje los items y los tooltips que hayan quedado de antes
		for (JButton slotItem : slotsItemsMiPersonaje) {			
			slotItem.setIcon(null);
			slotItem.setToolTipText(null);
		}
		
		// obtengo los personajes conectados
		personajesConectados = juego.getEscuchaMensajes().getPersonajesConectados();
		
		// recorro todo el map que contiene los jugadores conectados para agregar los nombres a la lista
		for (Entry<Integer, PaquetePersonaje> entry : personajesConectados.entrySet()) {
			
			PaquetePersonaje miPersonaje = juego.getPersonaje();
			PaquetePersonaje otroPersonaje = entry.getValue();
			
			if (  miPersonaje.getId() != otroPersonaje.getId() && otroPersonaje.getSeEncuentraEnMercado() == true)
				((DefaultListModel<PaquetePersonaje>)listaUsuarios.getModel()).addElement(otroPersonaje);
						
		}
		
		// muestro mis items
		Map<Integer, PaqueteItem> itemsMiPersonaje = juego.getPersonaje().getPaqueteInventario().getItems();
		int cantItemsEnGrillaMiPersonaje = 0;
		
		for (Entry<Integer, PaqueteItem> entry : itemsMiPersonaje.entrySet()) {
			
			PaqueteItem item = entry.getValue();
			
			if (item != null && item.getId() > 0) {

				String descripcion = obtenerDescripcionItem(item);
				slotsItemsMiPersonaje.get(cantItemsEnGrillaMiPersonaje).setItem(item);
				slotsItemsMiPersonaje.get(cantItemsEnGrillaMiPersonaje).setIcon(new ImageIcon(Recursos.items.get(item.getNombre())));
				slotsItemsMiPersonaje.get(cantItemsEnGrillaMiPersonaje).setToolTipText(descripcion);
				cantItemsEnGrillaMiPersonaje++;
				
			}
			
		}		

	}
	
	
	private String obtenerDescripcionItem(PaqueteItem item) {
		
		String descripcion = "<html>";
		
		descripcion = descripcion.concat("<b>" + item.getNombre() + "</b>");
					
		if (item.getBonoAtaque() > 0)
			descripcion = descripcion.concat("<br><i> + " + item.getBonoAtaque() + " Ataque</i>");
		
		if (item.getBonoDefensa() > 0)
			descripcion = descripcion.concat("<br><i> + " + item.getBonoDefensa() + " Defensa</i>");
		
		if (item.getBonoMagia() > 0)
			descripcion = descripcion.concat("<br><i> + " + item.getBonoMagia() + " Magia</i>");
		
		if (item.getBonoSalud() > 0)
			descripcion = descripcion.concat("<br><i> + " + item.getBonoSalud() + " Salud</i>");
		
		if (item.getBonoEnergia() > 0)
			descripcion = descripcion.concat("<br><i> + " + item.getBonoEnergia() + " Energia</i>");

		
		descripcion = descripcion.concat("</html>");
		
		return descripcion;
		
	}
	

}