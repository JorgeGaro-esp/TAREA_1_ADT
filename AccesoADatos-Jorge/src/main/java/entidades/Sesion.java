package entidades;

import java.io.IOException;
import java.util.Optional;

public class Sesion {

	// Atributos
	private final GestorCredenciales gestor;
	public static final Usuario INVITADO = new Usuario("", "Invitado", Perfil.INVITADO);
	private Usuario actual = INVITADO;

	// Constructor
	public Sesion(GestorCredenciales gestor) {
		this.gestor = gestor;
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

		// Si no hay ninguna excepcion
		Optional<Usuario> u = gestor.verificar(user, pass); // Nuevo usuario que sustituira al Invitado
		// Comrpueba que u no es un objeto vacio
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
