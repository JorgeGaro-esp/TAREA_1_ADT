package entidades;

public enum Perfil {
	INVITADO, SECRETARIADO, ENFERMERIA, ADMIN;

	//Metodos
	public static Perfil desdeTexto(String t) {
		switch (t.trim().toLowerCase()) {
		case "secretariado":
			return SECRETARIADO;
		case "enfermeria":
			return ENFERMERIA;
		case "admin":
			return ADMIN;
		default:
			throw new IllegalArgumentException("Perfil desconocido: " + t);
		}
	}
}
