package mensajeria;

import java.io.IOException;
import java.io.Serializable;

import cliente.Cliente;
import cliente.EscuchaMensajes;

public class ComandoMensajeChat extends Comando implements Serializable {

	private String desde;
	private String para;
	private String mensajeChat;
	private boolean esPrivado;

	public ComandoMensajeChat(String from, String to, String message, boolean isPrivate) {
		this.desde = from;
		this.para = to;
		this.mensajeChat = message;
		this.esPrivado = isPrivate;
	}

	@Override
	public void resolver(Object argumento) {
		EscuchaMensajes escuchaMensajes = (EscuchaMensajes) argumento;
		escuchaMensajes.getJuego().getMenuClientesChat().recibirMensaje(this.desde, this.para, this.mensajeChat,this.esPrivado);
	}
}
