package entidades;

public class Usuario {
	
	//Atributos y constantes
	
	private final String nombreUsuario;
	private final String nombrePersona;
	private final Perfil perfil;

	//Constructor
	public Usuario(String nombreUsuario, String nombrePersona, Perfil perfil) {
		this.nombreUsuario = nombreUsuario;
		this.nombrePersona = nombrePersona;
		this.perfil = perfil;
	}

	//Getters
	public String getNombreUsuario() {
		return nombreUsuario;
	}

	public String getNombrePersona() {
		return nombrePersona;
	}

	public Perfil getPerfil() {
		return perfil;
	}
}
