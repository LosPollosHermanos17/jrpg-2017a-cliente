package mensajeria;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import dominio.Inventario;
import dominio.Item;

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

	public int getBonusSalud() {
		return this.getInventario().getBonusSalud();
	}

	public int getBonusEnergia() {
		return this.getInventario().getBonusEnergia();
	}

	public Inventario getInventario() {

		Map<Integer, Item> auxItems = new HashMap<Integer, Item>();
		for (Entry<Integer, PaqueteItem> entry : items.entrySet())
			auxItems.put(entry.getKey(), entry.getValue().getItem());

		Inventario inventario = new Inventario(auxItems);
		return inventario;
	}

	public void actualizar(Inventario inventario) {
		if (inventario != null) {
			this.id = inventario.getId();
			for (Entry<Integer, Item> entry : inventario.getItems().entrySet()) {
				this.items.put(entry.getKey(), new PaqueteItem(entry.getValue()));
			}
		}
	}
}
