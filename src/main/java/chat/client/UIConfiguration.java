package chat.client;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

public class UIConfiguration extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JTextField txtIp;
	private JTextField txtPort;
	private FileProperties file;

	public UIConfiguration(UIClients uiClients, FileProperties file) {
		this.file = file;

		setTitle("Conectar");
		setAlwaysOnTop(true);
		setModal(true);
		setBounds(100, 100, 319, 100);
		
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setLayout(new FlowLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		{
			JLabel lblIp = new JLabel("IP");
			contentPanel.add(lblIp);
		}
		{
			txtIp = new JTextField();
			contentPanel.add(txtIp);
			txtIp.setColumns(10);
		}
		{
			JLabel lblPort = new JLabel("Puerto");
			contentPanel.add(lblPort);
		}
		{
			txtPort = new JTextField();
			txtPort.addKeyListener(new KeyAdapter() {
				@Override
				public void keyTyped(KeyEvent e) {
					char c = e.getKeyChar();
					if (c < '0' || c > '9') {
						e.consume();
					}

				}
			});
			contentPanel.add(txtPort);
			txtPort.setColumns(10);
		}
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("Aceptar");
				okButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						if (txtIp.getText().length() > 0 && txtPort.getText().length() > 0)
						{
							file.write(txtIp.getText(), Integer.parseInt(txtPort.getText()));
							file.read();
						}
						dispose();
					}
				});
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
			{
				JButton cancelButton = new JButton("Cancelar");
				cancelButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						dispose();
					}
				});
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
			}
		}

		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		
		file.read();
		txtIp.setText(file.getIP());
		txtPort.setText("" + file.getPuerto());
		setLocationRelativeTo(uiClients);
		setVisible(true);
		txtIp.requestFocus();
		txtIp.selectAll();
	}

}
