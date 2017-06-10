package mensajeria;

import java.io.Serializable;
import java.util.List;

public class PaqueteBatalla extends Paquete implements Serializable, Cloneable {
	
	private int id;
	private int idEnemigo;
	private boolean miTurno;
	private List<PaqueteItem> items;
	
	public PaqueteBatalla(){
		setComando(Comando.BATALLA);
	}
	
	public int getId() {
		return id;
	}


	public void setId(int id) {
		this.id = id;
	}


	public int getIdEnemigo() {
		return idEnemigo;
	}

	public void setIdEnemigo(int idEnemigo){
		this.idEnemigo = idEnemigo;
	}

	public boolean isMiTurno() {
		return miTurno;
	}

	public void setMiTurno(boolean miTurno) {
		this.miTurno = miTurno;
	}
	
	public void setItems(List<PaqueteItem> items)
	{
		this.items = items;
	}
	
	public List<PaqueteItem> getItems()
	{
		return this.items;
	}
}
