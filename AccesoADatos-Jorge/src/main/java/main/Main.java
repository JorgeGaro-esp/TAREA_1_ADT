package main;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Properties;
import java.util.Scanner;

import entidades.Alergia;
import entidades.GestorAlergias;
import entidades.GestorCredenciales;
import entidades.Medicamento;
import entidades.Perfil;
import entidades.Sesion;
import entidades.Usuario;

public class Main {

	//Propiedades
	static final String PROPERTIES_FILE = "app.properties";
	static Properties properties = new Properties();
	
	//Scanner para poder usar el teclado
	static Scanner sc = new Scanner(System.in);
	
	//Las rutas donde estan o estaran los documentos .txt o .dat  para los gestores
	
	
	//Instancia de los gestores que usaremos para poder registrar usuarios y alergias con medicamento
	static GestorCredenciales gestor = new GestorCredenciales(properties.getProperty("credenciales.fichero"));
	static GestorAlergias gestorAlergias = new GestorAlergias(properties.getProperty("alergias.fichero"));
	
	//Sesion
	static Sesion sesion = new Sesion(gestor,properties);

	public static void main(String[] args) {

		int opcion = -1;

		do {

			// Menu básico
			System.out.println("=======CLINICA DENTAL=======");
			System.out.println("1. Log-in");
			System.out.println("2. Log-out");
			System.out.println("3. Gestionar empleados");
			System.out.println("4. Gestionar alergias");
			System.out.println("5. Salir");
			System.out.print("Eliga una opcion: ");

			opcion = leerEntero();
			
			
			switch (opcion) {
			case 1: {
				login();
				break;
			}
			case 2: {
				sesion.logout();
				break;
			}
			case 3: {

				if (tienePerfil(sesion, Perfil.ADMIN, Perfil.SECRETARIADO)) {

					menuEmpleados(gestor);

				} else {
					System.out.println("\nNo está autorizado para esta acción.");
				}
				break;

			}
			case 4: {
				if (tienePerfil(sesion, Perfil.ADMIN, Perfil.ENFERMERIA)) {
					menuAlergias(gestorAlergias);
				} else {
					System.out.println("\nNo está autorizado para esta acción.");
				}
				break;
			}
			case 5: {
				System.out.println("\nSaliendo del programa");
				break;
			}
			default: {
				System.err.println("\nError, se debe elegir una de las opciones mostradas\n");
				break;
			}
			}
		} while (opcion != 5);

	}

	//Metodos estaticos
	
	// CU-1 Login
	private static void login() {
		try {

			// Pides el usuario
			System.out.print("Usuario: ");
			String usuario = sc.nextLine();

			// Pides la contraseña
			System.out.print("Contraseña: ");
			String password = sc.nextLine();

			// Si el login es correcto, el perfil de usuario cambia de invitado a lo
			// pertinente
			if (sesion.login(usuario, password)) {

				Usuario u = sesion.getUsuarioActual();

				System.out.println("\nLogin correcto");
				System.out.println("Bienvenido/a " + u.getNombrePersona());
				System.out.println("Perfil: " + u.getPerfil() + "\n");

			} else {

				System.out.println("Usuario o contraseña incorrectos.");

			}

		} catch (IOException e) {

			System.out.println("Error leyendo credenciales.txt");

		} catch (IllegalArgumentException | IllegalStateException e) {

			System.out.println(e.getMessage());

		}
	}
	
	private static boolean tienePerfil(Sesion sesion, Perfil... permitidos) {
		if (!sesion.estaAutenticado())
			return false;

		for (Perfil p : permitidos) {

			if (sesion.getUsuarioActual().getPerfil() == p)
				return true;

		}

		return false;
	}
	
	// CU-2 Gestionar personal de la clínica

	// Menu para gestionar el caso de uso
	private static void menuEmpleados(GestorCredenciales gestor) {
		int op = -1;
		do {
			
			System.out.println("\n=======GESTION DE EMPLEADOS=======");
			System.out.println("1. Dar de alta un empleado");
			System.out.println("2. Volver");
			System.out.print("Elija una opción: ");
			
			op = leerEntero();
			
			switch (op) {
			case 1:
				altaEmpleado(gestor);
				break;
			case 2:
				System.out.println("Volviendo al menú principal...");
				break;
			default:
				System.out.println("Opción no válida.");
				break;
			}
		} while (op != 2);
	}

	// CU-2 Metodo para dar de alta a los empleados nuevos, validando el formato de
	// diferentes datos
	private static void altaEmpleado(GestorCredenciales gestor) {
		System.out.println("\n--- Alta de empleado ---");

		// 1. Tipo de empleado
		int tipo;
		do {
			System.out.println("Tipo de empleado:");
			System.out.println("1. Secretariado");
			System.out.println("2. Enfermería");
			System.out.print("Elija tipo: ");
			tipo = leerEntero();
			if (tipo != 1 && tipo != 2)
				System.out.println("Opción no válida.");
		} while (tipo != 1 && tipo != 2);

		// 2. Datos personales (se validan todos aunque solo se guarden algunos, como
		// pide el enunciado)
		String nombre = pedirTexto("Nombre: ");
		String apellidos = pedirTexto("Apellidos: ");

		pedirConRegex("Teléfono (9 dígitos): ", "\\d{9}", "Teléfono no válido.");
		pedirConRegex("Email: ", "^[\\w.+-]+@[\\w-]+(\\.[\\w-]+)+$", "Email no válido.");
		pedirConRegex("NIF/NIE: ", "(?i)(\\d{8}|[XYZ]\\d{7})[A-Z]", "NIF/NIE no válido (ej: 12345678A o X1234567B).");
		pedirTexto("Dirección: ");

		// 3. Datos propios del puesto
		Perfil perfil;
		String perfilTexto;
		if (tipo == 1) {
			perfil = Perfil.SECRETARIADO;
			perfilTexto = "secretariado";
			pedirConRegex("Años de experiencia: ", "\\d{1,2}", "Introduzca un entero entre 0 y 99.");
		} else {
			perfil = Perfil.ENFERMERIA;
			perfilTexto = "enfermeria";
			pedirConRegex("Categoría (A, B o C): ", "(?i)[ABC]", "La categoría debe ser A, B o C.");
		}

		// 4. Credenciales (usuario único)
		String nombreUsuario;
		while (true) {
			nombreUsuario = pedirSinEspacios("Nombre de usuario: ");
			try {
				if (!gestor.existeUsuario(nombreUsuario))
					break;
				System.out.println("Ese nombre de usuario ya existe. Elija otro.");
			} catch (IOException e) {
				System.out.println("Error leyendo credenciales. Registro cancelado.");
				return;
			}
		}
		String password = pedirSinEspacios("Contraseña: ");

		// 5. Se crea el usuario
		String nombreCompleto = nombre + " " + apellidos;
		Usuario nuevo = crearUsuario(nombreUsuario, nombreCompleto, perfil);

		// 6. Confirmación y persistencia en credenciales.txt
		System.out.println("\nSe va a registrar:");
		System.out.println("  Usuario: " + nuevo.getNombreUsuario());
		System.out.println("  Nombre:  " + nuevo.getNombrePersona());
		System.out.println("  Perfil:  " + perfilTexto);
		if (!confirmar("¿Confirmar el alta?")) {
			System.out.println("Alta cancelada.");
			return;
		}
		try {
			gestor.anadirUsuario(nuevo.getNombreUsuario(), password, nuevo.getNombrePersona(), perfilTexto);
			System.out.println("Empleado registrado correctamente en credenciales.txt.");
		} catch (IOException e) {
			System.out.println("Error escribiendo en credenciales.txt: " + e.getMessage());
		}
	}

	// CU-2 Crear el usuario
	private static Usuario crearUsuario(String nombreUsuario, String nombrePersona, Perfil perfil) {
		return new Usuario(nombreUsuario, nombrePersona, perfil);
	}

	// CU-3 Gestionar las alergias

	private static void menuAlergias(GestorAlergias gestor) {
		int op = -1;
		do {
			
			// Menu básico
			System.out.println("\n=======GESTION DE ALERGIAS=======");
			System.out.println("1. Ver alergias");
			System.out.println("2. Nueva alergia");
			System.out.println("3. Editar alergia");
			System.out.println("4. Volver");
			System.out.print("Elija una opción: ");
			
			op = leerEntero();
			
			switch (op) {
			case 1:
				verAlergias(gestor);
				break;
			case 2:
				nuevaAlergia(gestor);
				break;
			case 3:
				editarAlergia(gestor);
				break;
			case 4:
				System.out.println("Volviendo al menú principal...");
				break;
			default:
				System.out.println("Opción no válida.");
				break;
			}
		} while (op != 4);
	}


	// CU3A - Ver alergias (ordenadas por id)
	private static void verAlergias(GestorAlergias gestor) {
		List<Alergia> lista = gestor.listarTodas();
		
		if (lista.isEmpty()) {
			System.out.println("\nNo hay alergias registradas.");
			return;
		}
		
		System.out.println("\n=== ALERGIAS REGISTRADAS (" + lista.size() + ") ===");
		for (Alergia a : lista) {
			imprimirAlergia(a);
			System.out.println("=============================");
		}
	}
	
	private static void imprimirAlergia(Alergia a) {
		Medicamento m = a.getMedicamento();
		System.out.println("Alergia nº " + a.getId());
		System.out.println("  Nombre: " + a.getNombreA());
		if (m != null) {
			System.out.println("  Medicamento");
			System.out.println("    Nombre:            " + m.getNombreM());
			System.out.println("    Principio activo:  " + m.getPrincipioActivo());
			System.out.println("    Dosis máx. diaria: " + m.getDosisMax() + " mg");
		}
	}

	
	// CU3B - Nueva alergia a medicamento
	private static void nuevaAlergia(GestorAlergias gestor) {

		System.out.println("\n=== Nueva alergia ===");
		String nombreAlergia = pedirTexto("Nombre de la alergia: ");
		String nombreMed = pedirTexto("Nombre del medicamento: ");
		String principio = pedirTexto("Principio activo: ");
		double dosis = pedirDosis("Dosis máxima diaria (mg): ");

		long id = gestor.siguienteId(); // idAlergia = idMedicamento
		Medicamento m = new Medicamento(id, nombreMed, principio, dosis);
		Alergia a = new Alergia(id, nombreAlergia, m);

		System.out.println("\nSe va a crear:");
		imprimirAlergia(a);

		if (!confirmar("¿Confirmar la creación?")) {

			System.out.println("Operación cancelada.");
			return;

		}

		try {

			gestor.guardar(a);
			System.out.println("Alergia creada con id " + id + ".");

		} catch (java.io.IOException e) {

			System.out.println("Error al guardar el fichero de la alergia: " + e.getMessage());

		}
	}

	
	// CU3C - Editar alergia (los ids no se tocan)
	private static void editarAlergia(GestorAlergias gestor) {

		System.out.println("\n=== Editar alergia ===");
		System.out.print("Id de la alergia a editar: ");
		int id = leerEntero();

		if (id <= 0) {

			System.out.println("Id no válido.");
			return;

		}

		Alergia a = gestor.buscarPorId(id);
		if (a == null) {

			System.out.println("No existe ninguna alergia con id " + id + " (o su fichero está dañado).");
			return;

		}

		Medicamento m = a.getMedicamento();

		System.out.println("\nDatos actuales:");
		imprimirAlergia(a);
		System.out.println("\n(Pulse Enter para mantener el valor actual)");

		String nombreAlergia = pedirOpcional("Nombre de la alergia [" + a.getNombreA() + "]: ", a.getNombreA());
		String nombreMed = pedirOpcional("Nombre del medicamento [" + m.getNombreM() + "]: ", m.getNombreM());
		String principio = pedirOpcional("Principio activo [" + m.getPrincipioActivo() + "]: ", m.getPrincipioActivo());

		double dosis = pedirDosisOpcional("Dosis máxima mg [" + m.getDosisMax() + "]: ", m.getDosisMax());

		if (!confirmar("¿Guardar los cambios?")) {

			System.out.println("Edición cancelada.");
			return;

		}

		// Los cambios se aplican solo tras confirmar
		a.setNombreA(nombreAlergia);
		m.setNombreM(nombreMed);
		m.setPrincipioActivo(principio);
		m.setDosisMax(dosis);

		try {

			gestor.guardar(a);
			System.out.println("Alergia actualizada correctamente.");

		} catch (java.io.IOException e) {

			System.out.println("Error al actualizar el fichero de la alergia: " + e.getMessage());

		}
	}

	private static String pedirOpcional(String mensaje, String actual) {
		while (true) {

			System.out.print(mensaje);
			String v = sc.nextLine().trim();

			if (v.isEmpty())
				return actual;

			if (v.length() > 100) {

				System.out.println("Texto demasiado largo (máx. 100 caracteres).");
				continue;

			}

			return v;
		}
	}

	private static double pedirDosis(String mensaje) {
	    while (true) {
	        System.out.print(mensaje);
	
	        String valor = sc.nextLine().trim().replace(',', '.');
	
	        double dosis = validarDosis(valor);
	
	        if (dosis > 0) {
	            return dosis;
	        }
	    }
	}
	
	
	private static double pedirDosisOpcional(
	        String mensaje, double actual) {
	
	    while (true) {
	        System.out.print(mensaje);
	
	        String valor = sc.nextLine().trim().replace(',', '.');
	
	        if (valor.isEmpty()) {
	            return actual;
	        }
	
	        double dosis = validarDosis(valor);
	
	        if (dosis > 0) {
	            return dosis;
	        }
	    }
	}


	private static double validarDosis(String valor) {
	    try {
	        double dosis = Double.parseDouble(valor);
	
	        if (dosis > 0 && Double.isFinite(dosis)) {
	            return dosis;
	        }
	
	        System.out.println("La dosis debe ser mayor que 0.");
	
	    } catch (NumberFormatException e) {
	        System.out.println("Introduzca un número válido.");
	    }
	
	    return -1;
	}

	// Metodos utiles generales
	private static int leerEntero() {
		try {

			return Integer.parseInt(sc.nextLine().trim());

		} catch (NumberFormatException e) {

			return -1;

		}
	}



	private static boolean confirmar(String mensaje) {
		while (true) {

			System.out.print(mensaje + " (s/n): ");
			String r = sc.nextLine().trim().toLowerCase();

			if (r.equals("s"))
				return true;
			if (r.equals("n"))
				return false;

			System.out.println("Responda 's' o 'n'.");
		}
	}


	private static String pedirTexto(String mensaje) {
	    while (true) {
	        System.out.print(mensaje);
	
	        String v = sc.nextLine().trim();
	
	        if (v.isEmpty()) {
	            System.out.println(
	                "El valor no puede estar vacío."
	            );
	
	        } else if (v.contains("|")) {
	            System.out.println(
	                "El carácter '|' no está permitido."
	            );
	
	        } else if (v.length() > 100) {
	            System.out.println(
	                "Texto demasiado largo (máx. 100 caracteres)."
	            );
	
	        } else {
	            return v;
	        }
	    }
	}


	private static String pedirConRegex(String mensaje, String regex, String error) {
		while (true) {
			System.out.print(mensaje);
			String v = sc.nextLine().trim();
			if (v.matches(regex)) {
				return v;
			}
			System.out.println(error);
		}
	}


	private static String pedirSinEspacios(String mensaje) {
	    while (true) {
	        System.out.print(mensaje);
	
	        String v = sc.nextLine().trim();
	
	        if (v.isEmpty() || v.matches(".*\\s+.*")
	                || v.contains("|")) {
	
	            System.out.println(
	                "Valor no válido: no puede estar vacío, "
	                + "ni contener espacios ni '|'."
	            );
	
	        } else {
	            return v;
	        }
	    }
	}


	private static boolean textoValido(String valor) {
	    return !valor.isEmpty()
	            && !valor.contains("|")
	            && valor.length() <= 100;
	}







}
