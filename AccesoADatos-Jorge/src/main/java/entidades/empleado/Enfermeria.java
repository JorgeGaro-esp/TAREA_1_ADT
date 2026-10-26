package entidades.empleado;

import entidades.Empleado;

public class Enfermeria extends Empleado {

	public Enfermeria(long id, String nombre, String apellidos, String telefono, String email, String nif) {
		super(id, nombre, apellidos, telefono, email, nif);
		// TODO Auto-generated constructor stub
	}

	//Atributos
	private Categoria categoria;
	//Constructor

	public Enfermeria(long id, String nombre, String apellidos, String telefono, String email, String nif,
			Categoria categoria) {
		super(id, nombre, apellidos, telefono, email, nif);
		this.categoria = categoria;
	}

	//Getters y Setters
	public Categoria getCategoria() {
		return categoria;
	}

	public void setCategoria(Categoria categoria) {
		this.categoria = categoria;
	}
	
	
	
	
}
