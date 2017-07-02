package client;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.border.EmptyBorder;
import java.awt.GridBagLayout;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import java.awt.GridLayout;
import java.awt.HeadlessException;

import net.miginfocom.swing.MigLayout;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.RowSpec;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;

public class UILogin extends JDialog {
	private JTextField txtUsername;
	private JPasswordField txtPassword;
	private UIClients uiClients;
	private JButton btnConnect;

	public UILogin(UIClients uiClients) {

		this.uiClients = uiClients;

		setLocationRelativeTo(uiClients);
		getContentPane().setLayout(null);

		JPanel panel = new JPanel();
		panel.setBounds(0, 0, 0, 0);
		getContentPane().add(panel);
		panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));

		btnConnect = new JButton("Conectar");
		btnConnect.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				connect();
			}
		});
		btnConnect.setBounds(17, 83, 89, 23);
		btnConnect.setEnabled(false);
		getContentPane().add(btnConnect);

		JLabel lblUsername = new JLabel("Usuario:");
		lblUsername.setBounds(29, 14, 46, 14);
		getContentPane().add(lblUsername);

		JLabel lblPassword = new JLabel("Contraseña:");
		lblPassword.setBounds(29, 42, 77, 14);
		getContentPane().add(lblPassword);

		txtUsername = new JTextField();
		txtUsername.setBounds(114, 11, 116, 20);
		txtUsername.setColumns(10);
		txtUsername.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent VK) {
				if (canConnect() && VK.getKeyCode() == KeyEvent.VK_ENTER) {
					connect();
				}
			}
		});
		getContentPane().add(txtUsername);

		txtPassword = new JPasswordField();
		txtPassword.setColumns(10);
		txtPassword.setBounds(114, 39, 116, 20);
		txtPassword.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent VK) {				
				if (canConnect() && VK.getKeyCode() == KeyEvent.VK_ENTER) {
					connect();
				}
			}
		});
		getContentPane().add(txtPassword);

		JButton btnCancel = new JButton("Cancelar");
		btnCancel.setBounds(132, 83, 89, 23);
		getContentPane().add(btnCancel);

		setBounds(100, 100, 274, 158);
		setTitle("Loguearse");
		setAlwaysOnTop(true);
		setModal(true);
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setLocationRelativeTo(uiClients);
		setVisible(true);
	}

	public boolean canConnect() {
		boolean canConnect =  !this.txtUsername.getText().isEmpty() && !this.txtPassword.getText().isEmpty();
		this.btnConnect.setEnabled(canConnect);
		return canConnect;
	}

	/*
	 * Se loguea en el servidor usando la UIClients
	 */
	private void connect() {
		try {
			if (this.uiClients.login(this.txtUsername.getText(), this.txtPassword.getText())) {
				this.dispose();
			} else {
				JOptionPane.showMessageDialog(this, "Usuario o contraseña incorrecto", "Login",
						JOptionPane.INFORMATION_MESSAGE);
			}
		} catch (HeadlessException | ClassNotFoundException | IOException e1) {
			JOptionPane.showMessageDialog(this,
					"No se puede conectar al servidor. Compruebe la dirección y puerto configurados.", "Erro servidor",
					JOptionPane.INFORMATION_MESSAGE);
		}
	}
}
