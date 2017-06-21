package mensajeria;

import java.io.IOException;
import java.io.Serializable;

import cliente.Cliente;

public class ComandoSalir extends Comando implements Serializable {

	@Override
	public void resolver(Object argumento) {
		try {
			Cliente cliente = (Cliente) argumento;
			// El usuario no pudo iniciar sesión
			cliente.getPaqueteUsuario().setInicioSesion(false);
			cliente.enviarComando(new ComandoDesconectar(cliente.getMiIp()));
			cliente.cerrarConexiones();
		} catch (IOException e) {			
			e.printStackTrace();
		}
	}
}
