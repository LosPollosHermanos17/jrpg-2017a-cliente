package mensajeria;

import java.io.Serializable;
import javax.swing.JOptionPane;

import cliente.Cliente;

public class ComandoInicioSesion extends Comando implements Serializable {

	private PaqueteUsuario paqueteUsuario;
	private PaquetePersonaje paquetePersonaje;
	
	public ComandoInicioSesion(){
		
	}
	
	public ComandoInicioSesion(PaqueteUsuario paqueteUsuario) {
		this.paqueteUsuario = paqueteUsuario;
	}

	@Override
	public void resolver(Object argumento) {
		Cliente cliente = (Cliente) argumento;
		if (this.getMensaje().equals(Comando.msjExito)) {

			// El usuario ya inicio sesión
			cliente.getPaqueteUsuario().setInicioSesion(true);

			// Guardo el paquete personaje con los datos
			cliente.setPaquetePersonaje(this.paquetePersonaje);

		} else {
			if (this.getMensaje().equals(Comando.msjFracaso))
				JOptionPane.showMessageDialog(null, "Error al iniciar sesión. Revise el usuario y la contraseña");

			// El usuario no pudo iniciar sesión
			cliente.getPaqueteUsuario().setInicioSesion(false);
		}
	}
	
	public void setPaquetePersonaje(PaquetePersonaje paquetepj){
		this.paquetePersonaje=paquetepj;
	}
	
	public void setPaqueteUsuario(PaqueteUsuario paqueteUsuario){
		this.paqueteUsuario=paqueteUsuario;
	}
	
	public PaquetePersonaje getPaquetePersonaje(){
		return this.paquetePersonaje;
	}
	
}
