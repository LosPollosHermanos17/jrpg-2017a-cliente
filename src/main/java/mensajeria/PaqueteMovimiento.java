package mensajeria;

public class PaqueteMovimiento {
	private int id;
	private float posX;
	private float posY;
	private int direccion;
	private int frame;
	
	public PaqueteMovimiento() {
	}
	
	public PaqueteMovimiento(int idPersonaje) {
		id = idPersonaje;		
	}
	
	public PaqueteMovimiento(int idPersonaje, float posX, float posY) {
		this.id = idPersonaje;
		this.posX = posX;
		this.posY = posY;	
	}

	public int getIdPersonaje() {
		return id;
	}

	public void setIdPersonaje(int idPersonaje) {
		this.id = idPersonaje;
	}

	public float getPosX() {
		return posX;
	}

	public void setPosX(float posX) {
		this.posX = posX;
	}

	public float getPosY() {
		return posY;
	}

	public void setPosY(float posY) {
		this.posY = posY;
	}

	public int getDireccion() {
		return direccion;
	}

	public void setDireccion(int direccion) {
		this.direccion = direccion;
	}

	public int getFrame() {
		return frame;
	}

	public void setFrame(int frame) {
		this.frame = frame;
	}

	public Object clone() {
		Object obj = null;
		try {
			obj = super.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		return obj;
	}
}
