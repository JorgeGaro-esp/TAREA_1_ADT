package entidades;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.Optional;

/**
 * Verifica credenciales contra credenciales.txt
 * (nombre_usuario|password|nombre_persona|perfil).
 */
public class GestorCredenciales {
	private static final String ADMIN_USER = "admin", ADMIN_PASS = "admin";
	private final Path fichero;

	public GestorCredenciales(String ruta) {
		this.fichero = Paths.get(ruta);
	}

	public Optional<Usuario> verificar(String user, String pass) throws IOException {
		if (ADMIN_USER.equals(user) && ADMIN_PASS.equals(pass)) {
			return Optional.of(new Usuario(ADMIN_USER, "Administrador General", Perfil.ADMIN));
		}
		if (!Files.exists(fichero)) {
			throw new FileNotFoundException("No se encuentra " + fichero);
		}
		for (String linea : Files.readAllLines(fichero)) {
			if (linea.trim().isEmpty()) {
				continue; // Línea vacia
			}
			String[] c = linea.split("\\|");
			if (c.length != 4) {
				continue; // Línea mal formada en credenciales.txt
			}
			if (c[0].trim().equals(user) && c[1].trim().equals(pass)) {
				Perfil p = Perfil.desdeTexto(c[3]); // Cambiamos el perfil de usuario
				if (p == Perfil.ADMIN)
					continue; // el admin solo existe con credenciales fijas
				return Optional.of(new Usuario(c[0].trim(), c[2].trim(), p)); //Nos devuelve un usuario nuevo
			}
		}
		return Optional.empty();
	}
	
	public boolean existeUsuario(String nombreUsuario) throws IOException {
		if (ADMIN_USER.equals(nombreUsuario)) {
			return true;
		}
		if (!Files.exists(fichero)) {
			return false;
		}
		for (String linea : Files.readAllLines(fichero, StandardCharsets.UTF_8)) {
			String[] partes = linea.split("\\|");
			if (partes.length > 0 && partes[0].trim().equals(nombreUsuario)) {
				return true;
			}
		}
		return false;
	}

	 
	public void anadirUsuario(String usuario, String password, String nombrePersona, String perfil)
			throws IOException {
		// Si el fichero no termina en salto de línea, se añade para no pegar líneas
		boolean necesitaSalto = false;
		if (Files.exists(fichero) && Files.size(fichero) > 0) {
			try (RandomAccessFile raf = new RandomAccessFile(fichero.toFile(), "r")) {
				raf.seek(raf.length() - 1);
				necesitaSalto = (raf.read() != '\n');
			}
		}
		String linea = (necesitaSalto ? System.lineSeparator() : "")
				+ usuario + "|" + password + "|" + nombrePersona + "|" + perfil
				+ System.lineSeparator();
		Files.write(fichero, linea.getBytes(StandardCharsets.UTF_8), StandardOpenOption.CREATE,
				StandardOpenOption.APPEND);
	}


}
