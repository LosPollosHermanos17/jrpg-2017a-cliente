package mensajeria;

import java.io.Serializable;
import java.util.Map;

import dominio.Inventario;

public class PaqueteInventario extends Paquete implements Serializable, Cloneable {
	
	private int id;
	private Map<Integer, PaqueteItem> items;

	public PaqueteInventario(int id, Map<Integer, PaqueteItem> items) {
		this.id = id;
		this.items = items;
	}
	
	public int getId() {
		return id;
	}

	public Map<Integer, PaqueteItem> getItems() {
		return items;
	}

	public Inventario getInventario() {
		Inventario inventario = new Inventario();
		for(PaqueteItem pItem : items.values())
			inventario.addItem(pItem.getItem());
		return inventario;
	}
}
