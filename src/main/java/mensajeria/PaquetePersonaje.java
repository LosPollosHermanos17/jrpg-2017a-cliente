package mensajeria;

import java.io.Serializable;

import dominio.Personaje;
import estados.Estado;

public class PaquetePersonaje implements Serializable, Cloneable {

	private int id;
	private int idMapa;
	private int estado;
	private String casta;
	private String nombre;
	private String raza;
	private int saludTope;
	private int energiaTope;
	private int fuerza;
	private int destreza;
	private int inteligencia;
	private int nivel;
	private int experiencia;
	private PaqueteInventario inventario;
	private PaqueteMochila mochila;

	public PaquetePersonaje() {
		estado = Estado.estadoOffline;
	}
	
	public void actualizar(Personaje personaje)
	{
		this.saludTope = personaje.getSaludTope();
		this.energiaTope = personaje.getEnergiaTope();
		this.nivel = personaje.getNivel();
		this.experiencia = personaje.getExperiencia();
		this.destreza = personaje.getDestreza();
		this.fuerza = personaje.getFuerza();
		this.inteligencia = personaje.getInteligencia();
		
		this.inventario.actualizar(personaje.getInventario());
	}

	public PaqueteInventario getPaqueteInventario() {
		return inventario;
	}

	public void setInventario(PaqueteInventario inventario) {
		this.inventario = inventario;
	}

	public PaqueteMochila getMochila() {
		return mochila;
	}

	public void setMochila(PaqueteMochila mochila) {
		this.mochila = mochila;
	}

	public int getEstado() {
		return estado;
	}

	public void setEstado(int estado) {
		this.estado = estado;
	}
	
	public int getMapa(){
		return idMapa;
	}
	
	public void setMapa(int mapa){
		idMapa = mapa;
	}
	
	public int getNivel() {
		return nivel;
	}

	public void setNivel(int nivel) {
		this.nivel = nivel;
	}

	public int getExperiencia() {
		return experiencia;
	}

	public void setExperiencia(int experiencia) {
		this.experiencia = experiencia;
	}

	public int getId() {
		return id;
	}


	public void setId(int id) {
		this.id = id;
	}


	public String getCasta() {
		return casta;
	}


	public void setCasta(String casta) {
		this.casta = casta;
	}

	public String getNombre() {
		return nombre;
	}


	public void setNombre(String nombre) {
		this.nombre = nombre;
	}


	public String getRaza() {
		return raza;
	}


	public void setRaza(String raza) {
		this.raza = raza;
	}


	public int getSaludTope() {
		return saludTope;
	}
	
	public int getSaludTopeConBonus() {
		return saludTope + inventario.getBonusSalud();
	}

	public void setSaludTope(int saludTope) {
		this.saludTope = saludTope;
	}


	public int getEnergiaTope() {
		return energiaTope;
	}
	
	public int getEnergiaTopeConBonus()
	{
		return energiaTope + inventario.getBonusEnergia();
	}


	public void setEnergiaTope(int energiaTope) {
		this.energiaTope = energiaTope;
	}


	public int getFuerza() {
		return fuerza;
	}


	public void setFuerza(int fuerza) {
		this.fuerza = fuerza;
	}


	public int getDestreza() {
		return destreza;
	}


	public void setDestreza(int destreza) {
		this.destreza = destreza;
	}


	public int getInteligencia() {
		return inteligencia;
	}

	public void setInteligencia(int inteligencia) {
		this.inteligencia = inteligencia;
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
