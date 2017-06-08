package mensajeria;

import java.io.Serializable;
public class PaqueteItem extends Paquete implements Serializable, Cloneable {

	private int id;
	private int bonoAtaque;
	private int bonoDefensa;
	private int bonoMagia;
	private int bonoSalud;
	private int bonoEnergia;
	
	private int fuerzaRequerida;
	private int destrezaRequerida;
	private int inteligenciaRequerida;
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getBonoAtaque() {
		return bonoAtaque;
	}

	public void setBonoAtaque(int bonoAtaque) {
		this.bonoAtaque = bonoAtaque;
	}

	public int getBonoDefensa() {
		return bonoDefensa;
	}

	public void setBonoDefensa(int bonoDefensa) {
		this.bonoDefensa = bonoDefensa;
	}

	public int getBonoMagia() {
		return bonoMagia;
	}

	public void setBonoMagia(int bonoMagia) {
		this.bonoMagia = bonoMagia;
	}

	public int getBonoSalud() {
		return bonoSalud;
	}

	public void setBonoSalud(int bonoSalud) {
		this.bonoSalud = bonoSalud;
	}

	public int getBonoEnergia() {
		return bonoEnergia;
	}

	public void setBonoEnergia(int bonoEnergia) {
		this.bonoEnergia = bonoEnergia;
	}

	public int getFuerzaRequerida() {
		return fuerzaRequerida;
	}

	public void setFuerzaRequerida(int fuerzaRequerida) {
		this.fuerzaRequerida = fuerzaRequerida;
	}

	public int getDestrezaRequerida() {
		return destrezaRequerida;
	}

	public void setDestrezaRequerida(int destrezaRequerida) {
		this.destrezaRequerida = destrezaRequerida;
	}

	public int getInteligenciaRequerida() {
		return inteligenciaRequerida;
	}

	public void setInteligenciaRequerida(int inteligenciaRequerida) {
		this.inteligenciaRequerida = inteligenciaRequerida;
	}

	public PaqueteItem() {		
	}
}
