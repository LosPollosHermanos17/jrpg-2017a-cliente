package mensajeria;

import java.io.IOException;
import java.io.Serializable;
import javax.swing.JOptionPane;
import com.google.gson.JsonSyntaxException;
import cliente.Cliente;
import frames.MenuCreacionPj;

public class ComandoRegistro extends Comando implements Serializable {
	private PaqueteUsuario paqueteUsuario;

	public ComandoRegistro(PaqueteUsuario paqueteUsuario) {
		this.paqueteUsuario = paqueteUsuario;
	}

	@Override
	public void resolver(Object argumento) {
		try {
			Cliente cliente = (Cliente) argumento;
			if (this.getMensaje().equals(Comando.msjExito)) {

				// Abro el menu para la creación del personaje
				MenuCreacionPj menuCreacionPJ = new MenuCreacionPj(cliente, cliente.getPaquetePersonaje());
				menuCreacionPJ.setVisible(true);

				// Espero a que el usuario cree el personaje
				wait();

				// Le envio los datos al servidor
				cliente.enviarComando(new ComandoCrearPersonaje(cliente.getPaquetePersonaje()));
				JOptionPane.showMessageDialog(null, "Registro exitoso.");

				// Recibo el paquete personaje con los datos (la id incluida)
				Comando comando = cliente.recibirComando();
				comando.resolver(cliente);

			} else {
				if (this.getMensaje().equals(Comando.msjFracaso))
					JOptionPane.showMessageDialog(null, "No se pudo registrar.");
				// El usuario no pudo iniciar sesi�n
				cliente.getPaqueteUsuario().setInicioSesion(false);
			}
		} catch (JsonSyntaxException | ClassNotFoundException | IOException | InterruptedException e) {
			e.printStackTrace();
		}
	}
}
