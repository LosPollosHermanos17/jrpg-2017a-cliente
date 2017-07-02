package client;

import java.awt.Frame;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.JOptionPane;

import com.google.gson.Gson;

import mensajeria.Command;
import mensajeria.Packet;
import mensajeria.PacketLogout;
import mensajeria.PacketMessage;
import mensajeria.PacketUpdate;
import mensajeria.PacketUser;

public class Client extends Thread {
	private UIClients uiClients;
	private Socket socket;
	private FileProperties fileProperties;
	private ObjectInputStream in;
	private ObjectOutputStream out;
	private Gson gson;
	private Packet packet;
	private PacketUser user;

	public Client(UIClients uiClients, FileProperties fileProperties) {
		this.uiClients = uiClients;
		this.fileProperties = fileProperties;
		this.gson = new Gson();
	}

	/*
	 * Escucha mensajes provenientes desde el servidor y realiza las acciones
	 * pertinentes
	 */
	public void run() {

		try {
			while (this.packet != null && this.packet.getCommand() != Command.LOGOUT) {
				String readedObject = (String) this.in.readObject();
				this.packet = gson.fromJson(readedObject, Packet.class);
				switch (this.packet.getCommand()) {
				case LOGOUT: {
					PacketLogout packetLogout = gson.fromJson(readedObject, PacketLogout.class);
					this.in.close();
					this.out.close();
					this.socket.close();
					break;
				}
				case MESSAGE: {
					PacketMessage packetMessage = gson.fromJson(readedObject, PacketMessage.class);
					this.uiClients.receiveMessage(packetMessage.getFrom(), packetMessage.getTo(),
							packetMessage.getMessage(), packetMessage.isPrivate());
					break;
				}
				case UPDATE: {
					PacketUpdate packetUpdate = gson.fromJson(readedObject, PacketUpdate.class);
					this.uiClients.updateUsers(packetUpdate.getUsers());
					break;
				}
				default:
					break;
				}
			}

		} catch (ClassNotFoundException | IOException e) {
			this.uiClients.showMessageDialog("Se ha desconectado del servidor", "Desconexión",
					JOptionPane.INFORMATION_MESSAGE);
		}
		// Limpio los usuarios logueados
		this.uiClients.updateUsers(null);
		// Elimino este cliente en la UIClient
		this.uiClients.setClient(null);
	}

	/*
	 * Se conecta al servidor y se loguea
	 */
	public boolean login(String username, String password) throws IOException, ClassNotFoundException {

		this.socket = new Socket(fileProperties.getIP(), fileProperties.getPuerto());

		// Login
		this.user = new PacketUser(username, password);
		this.user.setCommand(Command.LOGIN);
		this.out = new ObjectOutputStream(this.socket.getOutputStream());
		this.out.writeObject(gson.toJson(this.user, PacketUser.class));

		// Respuesta del server al loguearse
		this.in = new ObjectInputStream(this.socket.getInputStream());
		String readedObject = (String) this.in.readObject();
		this.packet = gson.fromJson(readedObject, Packet.class);

		// Si el comando es el mismo que le envie
		if (this.packet.getCommand() == Command.LOGIN) {
			// Si pudo loguearse
			if (this.packet.getStatus()) {
				// Seteo en el usuario el logueo
				this.user.setLogged(true);

				// Recibo los usuarios conectados desde el server
				PacketUpdate packetUpdate = gson.fromJson(readedObject, PacketUpdate.class);

				// Actualizo los usuario en la UIClients
				this.uiClients.updateUsers(packetUpdate.getUsers());

				// Inicio el thread para que escuche mensajes desde el server
				this.start();
			} else {
				// No se pudo loguear
				return false;
			}
		}
		return true;
	}

	public void logout() {

		try {
			this.user.setCommand(Command.LOGOUT);
			this.out.writeObject(gson.toJson(this.user, PacketUser.class));
		} catch (IOException e) {
		}
	}

	/*
	 * Envia mensaje al servidor
	 */
	public void sendMessage(String username, String message, boolean isPrivate) throws IOException {
		// Envío paquete al servidor de tipo mensaje
		PacketMessage packetMessage = new PacketMessage(this.user.getUsername(), username, message, isPrivate);
		this.out.writeObject(gson.toJson(packetMessage, PacketMessage.class));
	}

	/*
	 * Devuelve el nombre de usuario del cliente
	 */
	public String getUsername() {
		return this.user.getUsername();
	}
}
