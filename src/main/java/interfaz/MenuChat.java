package interfaz;
import java.awt.Graphics;
import juego.Juego;
import recursos.Recursos;

public class MenuChat {

	private final int BOTON_POS_X = 380;
	private final int BOTON_POS_Y = 0;

	private final int BOTON_ANCHO = 180;
	private final int BOTON_ALTO = 120;

	
	private MenuClientesChat menuClientesChat;
	
	public MenuChat(Juego juego) {
		this.menuClientesChat = juego.getMenuClientesChat();
	}

	
	public void graficarBoton(Graphics g) {
		g.drawImage(Recursos.botonChat, BOTON_POS_X, BOTON_POS_Y, BOTON_ANCHO, BOTON_ALTO, null);
	}
	
	public boolean botonClickeado(int mouseX, int mouseY) {
		if (mouseX >= BOTON_POS_X && mouseX <= (BOTON_POS_X + BOTON_ANCHO) && mouseY >= BOTON_POS_Y
				&& mouseY <= (BOTON_POS_Y + BOTON_ALTO))
			return true;
		return false;
	}
	
	public void mostrarChat() {
		menuClientesChat.setVisible(true);
	}
	
}
