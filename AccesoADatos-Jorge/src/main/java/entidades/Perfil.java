	package entidades;
	
	public enum Perfil {
		INVITADO, SECRETARIADO, ENFERMERIA, ADMIN, CIRUJANO;
	
		//Metodos
		public static Perfil desdeTexto(String t) {
			switch (t.trim().toLowerCase()) {
			case "secretariado":
				return SECRETARIADO;
			case "enfermeria":
				return ENFERMERIA;
			case "cirujano":
				return CIRUJANO;
			case "admin":
				return ADMIN;
			default:
				throw new IllegalArgumentException("Perfil desconocido: " + t);
			}
		}
	}
