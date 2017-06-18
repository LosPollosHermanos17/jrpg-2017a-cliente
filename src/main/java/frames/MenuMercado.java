package frames;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
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

import dominio.Item;
import interfaz.MenuInventario;
import juego.Juego;
import mensajeria.PaqueteItem;
import mensajeria.PaquetePersonaje;
import recursos.Recursos;

public class MenuMercado implements ListSelectionListener {
	
	private final int VENTANA_ANCHO = 800;
	private final int VENTANA_ALTO = 600;

	private final int PANEL_ANCHO = 800;
	private final int PANEL_ALTO = 600;
	
	private final int PANEL_USUARIOS_ANCHO = 300;
	private final int PANEL_USUARIOS_ALTO = 600;
	
	private final int PANEL_ITEMS_ANCHO = 500;
	private final int PANEL_ITEMS_ALTO = 600;
	
	private final int CANT_FILAS_GRILLA_ITEMS = 5;
	private final int CANT_COLUMNAS_GRILLA_ITEMS = 4;
	
	private Juego juego;
	private MenuInventario menuInventario;
	private JFrame ventanaMercado;		
	private JSplitPane panelMercado;
	private JPanel panelUsuarios;
	private JLabel tituloPanelUsuarios;
	private JScrollPane scrollPanelUsuarios;
	private JPanel panelItems;
	private JPanel grillaItems;
	private JLabel tituloPanelItems;
	private ArrayList<JButton> slotsItems;
	private JList<PaquetePersonaje> listaUsuarios;
	private JButton botonOfertar;
	
	
	public MenuMercado(Juego juego, MenuInventario menuInventario) {

		this.juego = juego;
		this.menuInventario = menuInventario;
		
		// Panel de Usuarios (seccion izquierda de la ventana)
		
		listaUsuarios = new JList<PaquetePersonaje>(new DefaultListModel<PaquetePersonaje>());
		listaUsuarios.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		listaUsuarios.setSelectedIndex(0);
		listaUsuarios.addListSelectionListener(this);
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
		
		tituloPanelUsuarios = new JLabel("Ofertantes");
		tituloPanelUsuarios.setFont(new Font("Arial Black", Font.BOLD, 24));
	    Border border = BorderFactory.createLineBorder(Color.BLACK);
	    tituloPanelUsuarios.setBorder(border);
	    tituloPanelUsuarios.setPreferredSize(new Dimension(PANEL_USUARIOS_ANCHO, 50));
	    tituloPanelUsuarios.setHorizontalAlignment(JLabel.CENTER);
	    tituloPanelUsuarios.setVerticalAlignment(JLabel.CENTER);
	    
		botonOfertar = new JButton("Ofertar");
		botonOfertar.setFont(new Font("Arial Black", Font.BOLD, 24));
		botonOfertar.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {				
				menuInventario.mostrarInventario();				
			}
			
		});
		
		panelUsuarios = new JPanel(new BorderLayout());		
		panelUsuarios.add(tituloPanelUsuarios, BorderLayout.NORTH);
		panelUsuarios.add(listaUsuarios, BorderLayout.CENTER);
		panelUsuarios.add(botonOfertar, BorderLayout.SOUTH);
		scrollPanelUsuarios = new JScrollPane(panelUsuarios, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scrollPanelUsuarios.setPreferredSize(new Dimension(PANEL_USUARIOS_ANCHO, PANEL_USUARIOS_ALTO));
		
		
		
		// Panel de Items (seccion derecha de la ventana)
		
		tituloPanelItems = new JLabel("Items Ofertados");
		tituloPanelItems.setFont(new Font("Arial Black", Font.BOLD, 24));
		tituloPanelItems.setBackground(new Color(Color.TRANSLUCENT));
		tituloPanelItems.setBorder(border);
		tituloPanelItems.setPreferredSize(new Dimension(PANEL_ITEMS_ANCHO, 50));
		tituloPanelItems.setHorizontalAlignment(JLabel.CENTER);
		tituloPanelItems.setVerticalAlignment(JLabel.CENTER);
		
		grillaItems = new JPanel(new GridLayout(CANT_FILAS_GRILLA_ITEMS, CANT_COLUMNAS_GRILLA_ITEMS));
		
		slotsItems = new ArrayList<JButton>(); 
		
		for (int i = 0 ; i < CANT_FILAS_GRILLA_ITEMS * CANT_COLUMNAS_GRILLA_ITEMS ; i++) {
			
			JButton itemDisplay = new JButton();
			slotsItems.add(itemDisplay);
			grillaItems.add(itemDisplay);
		}
			
		
		panelItems = new JPanel(new BorderLayout());		
		panelItems.add(tituloPanelItems, BorderLayout.NORTH);
		panelItems.add(grillaItems, BorderLayout.CENTER);
		panelItems.setPreferredSize(new Dimension(PANEL_ITEMS_ANCHO, PANEL_ITEMS_ALTO));
		
		
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
				}
								
			}
		});
		
		ventanaMercado.add(panelMercado);
		ventanaMercado.pack();
		ventanaMercado.setLocationRelativeTo(null);
	}
	
	public void mostrar(Juego juego) {
		
		Map<Integer, PaquetePersonaje> personajesConectados;
		
		juego.getPantalla().getFrame().setVisible(false); // oculto la ventana principal del juego
		ventanaMercado.setVisible(true); // muestro la ventana correspondiente al mercado
		
		// saco de la lista los nombres de los personajes que hayan quedado de antes
		((DefaultListModel<PaquetePersonaje>)listaUsuarios.getModel()).clear();
		
		// saco de la grilla los items y los tooltips que hayan quedado de antes
		for (JButton slotItem : slotsItems) {			
			slotItem.setIcon(null);
			slotItem.setToolTipText(null);
		}
		
		// obtengo los personajes conectados
		personajesConectados = juego.getEscuchaMensajes().getPersonajesConectados();
		
		// recorro todo el map que contiene los jugadores conectados para agregar los nombres a la lista
		for (Entry<Integer, PaquetePersonaje> entry : personajesConectados.entrySet()) {
			
			PaquetePersonaje miPersonaje = juego.getPersonaje();
			PaquetePersonaje otroPersonaje = entry.getValue();
			
			if (  miPersonaje.getId() != otroPersonaje.getId() )
				((DefaultListModel<PaquetePersonaje>)listaUsuarios.getModel()).addElement(otroPersonaje);
						
		}

	}

	@Override
	public void valueChanged(ListSelectionEvent e) {
		
		PaquetePersonaje personaje = listaUsuarios.getSelectedValue();
		
		// Solo realizo los siguientes pasos si la opcion seleccionada corresponde a un Personaje
		if (personaje != null) {
			
			Map<Integer, PaqueteItem> itemsPersonaje = personaje.getPaqueteInventario().getItems();
			int cantItemsEnGrilla = 0;
			
			// Saco de la grilla los items y los tooltip del otro jugador
			for (JButton slotItem : slotsItems) {			
				slotItem.setIcon(null);
				slotItem.setToolTipText(null);
			}
				
			// Muestro los items del jugador seleccionado en la grilla
			for (Entry<Integer, PaqueteItem> entry : itemsPersonaje.entrySet()) {
			
				String nombreItem = entry.getValue().getNombre();
				
				if (nombreItem != null) {
					String descripcion = obtenerDescripcionItem(entry.getValue().getItem());
					slotsItems.get(cantItemsEnGrilla).setIcon(new ImageIcon(Recursos.items.get(nombreItem)));
					slotsItems.get(cantItemsEnGrilla).setToolTipText(descripcion);
					cantItemsEnGrilla++;
				}
			}
		
			ventanaMercado.revalidate(); // Esto es para refrescar las imagenes
			
		}
		
	}
	
	
	private String obtenerDescripcionItem(Item item) {
		
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
