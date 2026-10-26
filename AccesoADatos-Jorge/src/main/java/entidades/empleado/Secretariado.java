package entidades.empleado;

import entidades.Empleado;

public class Secretariado extends Empleado {
	// Atributos
	private int nanios;

	// Constructor
	public Secretariado(long id, String nombre, String apellidos, String telefono, String email, String nif, int nanios) {
		super(id, nombre, apellidos, telefono, email, nif);
		this.nanios = nanios;
	}

	// Getters y Setters
	public int getNanios() {
		return nanios;
	}

	public void setNanios(int nanios) {
		this.nanios = nanios;
	}

	
	
}
