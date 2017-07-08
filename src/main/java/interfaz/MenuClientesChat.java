package interfaz;

import java.awt.Color;
import java.awt.Component;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import cliente.Cliente;
import mensajeria.ComandoMensajeChat;
import mensajeria.PaquetePersonaje;

import java.awt.BorderLayout;
import javax.swing.BoxLayout;

public class MenuClientesChat extends JFrame {

	private JPanel panel;
	private JList<String> listaUsuarios;

	private JMenuItem mntmSalir;
	private JMenuItem mntmSesionPublica;
	private JMenuItem mntmSesionPrivada;

	private Cliente cliente;
	private MenuVentanaChat chatPublico;
	private HashMap<String, MenuVentanaChat> chatPrivados;

	public MenuClientesChat(Cliente cliente) {
		this.cliente = cliente;
		initialize();
		initializeEvents();
	}

	public void initialize() {

		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			e.printStackTrace();
		}

		setTitle("Chat");
		setResizable(false);
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		setBounds(100, 100, 258, 285);

		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);

		JMenu mnArchivo = new JMenu("Archivo");
		menuBar.add(mnArchivo);

		mntmSalir = new JMenuItem("Salir");
		mnArchivo.add(mntmSalir);

		JMenu mnChat = new JMenu("Chat");
		menuBar.add(mnChat);

		mntmSesionPublica = new JMenuItem("Sala de Chat");
		mnChat.add(mntmSesionPublica);

		mntmSesionPrivada = new JMenuItem("Sesión privada");
		mntmSesionPrivada.setEnabled(false);
		mnChat.add(mntmSesionPrivada);
		panel = new JPanel();
		panel.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(panel);
		panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		panel.add(scrollPane);

		chatPrivados = new HashMap<String, MenuVentanaChat>();

		listaUsuarios = new JList<String>();
		listaUsuarios.setCellRenderer(new DefaultListCellRenderer() {
			@Override
			public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected,
					boolean cellHasFocus) {
				Component renderer = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
				if (renderer instanceof JLabel && value instanceof String) {
					((JLabel) renderer).setText(((String) value));
				}
				return renderer;
			}
		});
		scrollPane.setViewportView(listaUsuarios);
	}

	public void initializeEvents() {
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				openExitWindowConfirmation();
			}
		});
		mntmSalir.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				openExitWindowConfirmation();
			}
		});

		mntmSesionPublica.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				abrirVentanaChatPrivada();
			}
		});

		mntmSesionPrivada.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				abrirVentanaChatPublica();
			}
		});

		listaUsuarios.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2)
					abrirVentanaChatPublica();
			}
		});
		listaUsuarios.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent e) {
				mntmSesionPrivada.setEnabled(!listaUsuarios.isSelectionEmpty());
			}
		});
	}

	/*
	 * Abre una ventana emergente preguntando si se quiere salir de la
	 * aplicacion
	 */
	private void openExitWindowConfirmation() {
		// int opcion = JOptionPane.showConfirmDialog(this, "Desea salir del
		// Chat", "Confirmación",
		// JOptionPane.YES_NO_OPTION);
		// if (opcion == JOptionPane.YES_OPTION) {
		this.setVisible(false);
		// }
	}

	/*
	 * Abre una ventana publica de chat
	 */
	private void abrirVentanaChatPrivada() {
		if (this.chatPublico == null)
			this.chatPublico = new MenuVentanaChat("Sala", this, false);
		this.chatPublico.setVisible(true);
	}

	/*
	 * Abre una ventana privada de chat
	 */
	private void abrirVentanaChatPublica() {
		if (!listaUsuarios.isSelectionEmpty()) {
			MenuVentanaChat uiChat = this.seleccionarVentanaChat(listaUsuarios.getSelectedValue());
		} else
			JOptionPane.showMessageDialog(this, "Seleccione un elemento de la lista", "Seleccionar Usuario",
					JOptionPane.INFORMATION_MESSAGE);
	}

	/*
	 * Envia el mensaje usando el cliente
	 */
	public void enviarMensaje(String para, String mensaje, boolean esPrivado) throws IOException {
		if (this.cliente != null) {
			String desde = this.cliente.getPaquetePersonaje().getNombre();
			ComandoMensajeChat comando = new ComandoMensajeChat(desde, para, mensaje, esPrivado);
			this.cliente.enviarComando(comando);
		}
	}

	/*
	 * Recibe un mensaje a mostrar, abre el chat correspondiente y lo muestra
	 */
	public void recibirMensaje(String desde, String para, String mensaje, boolean esPrivado) {
		if (esPrivado) {
			String username = desde.equals(this.cliente.getPaquetePersonaje().getNombre()) ? para : desde;
			this.seleccionarVentanaChat(username).receiveMessage(desde, mensaje);
		} else if (this.chatPublico != null) {
			this.chatPublico.receiveMessage(desde, mensaje);
		}
	}

	/*
	 * Selecciona o crea la ventana chat del cliente
	 */
	public MenuVentanaChat seleccionarVentanaChat(String usuario) {
		MenuVentanaChat uiChat;
		if (chatPrivados.containsKey(usuario)) {
			uiChat = chatPrivados.get(usuario);
		} else {
			uiChat = new MenuVentanaChat(usuario, this, true);
			chatPrivados.put(usuario, uiChat);
		}
		uiChat.setVisible(true);
		return uiChat;
	}

	/*
	 * Elimina la ventana chat del map
	 */
	public void eliminarVentanChat(String usuario) {
		if (chatPrivados.containsKey(usuario)) {
			MenuVentanaChat uiChat = chatPrivados.get(usuario);
			chatPrivados.remove(usuario);
			uiChat.dispose();
		}
	}

	/*
	 * Actualiza la lista de usuarios conectados en la UI
	 */
	public void actualizarUsuarios(Map<Integer, PaquetePersonaje> personajes) {
		// Actualizo la lista
		DefaultListModel<String> modeloLista = new DefaultListModel<String>();
		if (personajes == null || personajes.size() == 0) {
			this.listaUsuarios.setModel(modeloLista);
		} else {
			for (PaquetePersonaje usuario : personajes.values()) {
				// Si el usuario soy "yo" no lo agrego a la lista
				if (!this.cliente.getPaquetePersonaje().getNombre().equals(usuario.getNombre()))
					modeloLista.addElement(usuario.getNombre());
			}
			this.listaUsuarios.setModel(modeloLista);
		}

		// Cierro la ventana de los usuarios deconectados
		for (String username : this.chatPrivados.keySet()) {
			if (!modeloLista.contains(username))
				this.eliminarVentanChat(username);
		}
	}

	/*
	 * Muestra un cuadro de dialogo
	 */
	public void mostrarMensajeDialogo(String mensaje, String titulo, int tipoDeMensaje) {
		JOptionPane.showMessageDialog(this, mensaje, titulo, tipoDeMensaje);
	}
}
