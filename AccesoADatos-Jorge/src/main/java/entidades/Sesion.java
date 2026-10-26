package entidades;

import java.io.IOException;
import java.util.Optional;
import java.util.Properties;

public class Sesion {

	// Atributos
	private final GestorCredenciales gestor;
	public static final Usuario INVITADO = new Usuario("", "Invitado", Perfil.INVITADO);
	private Usuario actual = INVITADO;
	private Properties properties;

	// Constructor
	public Sesion(GestorCredenciales gestor, Properties properties) {
		this.gestor = gestor;
		this.properties = properties;
	}

	// Getters y Setters
	public Usuario getUsuarioActual() {
		return actual;
	}

	public boolean estaAutenticado() {
		return actual.getPerfil() != Perfil.INVITADO;
	}

	// Metodos
	public boolean login(String user, String pass) throws IOException {
		if (estaAutenticado())
			throw new IllegalStateException("Ya hay una sesión activa; cierre sesión primero.");
		if (user == null || user.trim().isEmpty() || pass == null || pass.trim().isEmpty())
			throw new IllegalArgumentException("Usuario y contraseña no pueden estar vacíos.");

		// Administrador definido en application.properties
		if (user.equals(properties.getProperty("admin.usuario"))
				&& pass.equals(properties.getProperty("admin.password"))) {
			actual = new Usuario(user, "Administrador",
					Perfil.desdeTexto(properties.getProperty("admin.perfil")));
			return true;
		}

		// Resto de usuarios: credenciales.txt
		Optional<Usuario> u = gestor.verificar(user, pass);
		if (u.isPresent()) {
			actual = u.get();
		}
		return u.isPresent();
	}

	public void logout() {
		if (!estaAutenticado()) {
			System.out.println("No se puede hacer log-out a un usuario invitado");
		} else {
			System.out.println("Hasta luego " + actual.getNombrePersona());
			actual = Sesion.INVITADO;
		}
	}
}
