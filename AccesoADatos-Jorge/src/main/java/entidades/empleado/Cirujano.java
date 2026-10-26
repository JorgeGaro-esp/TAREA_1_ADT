package entidades.empleado;

import java.util.Set;

import entidades.Empleado;

public class Cirujano extends Empleado{

	//Atributo
	private Set<Especialidad> especialidades;

	//Constructor
	public Cirujano(long id, String nombre, String apellidos, String telefono, String email, String nif,
			Set<Especialidad> especialidades) {
		super(id, nombre, apellidos, telefono, email, nif);
		this.especialidades = especialidades;
	}

	//Getters y Setters
	public Set<Especialidad> getEspecialidades() {
		return especialidades;
	}

	public void setEspecialidades(Set<Especialidad> especialidades) {
		this.especialidades = especialidades;
	}
	
	
	
	
	
	
}
