package mensajeria;

import java.io.Serializable;
import java.util.Map;
public class PaqueteMochila extends Paquete implements Serializable, Cloneable {

	private int id;
	private Map<Integer, PaqueteItem> items;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Map<Integer, PaqueteItem> getItems() {
		return items;
	}

	public void setItems(Map<Integer, PaqueteItem> items) {
		this.items = items;
	}
	
	public PaqueteMochila(int id, Map<Integer, PaqueteItem> items) {
		this.id = id;
		this.items = items;
	}
	
}
