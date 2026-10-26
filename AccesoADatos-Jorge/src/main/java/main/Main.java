package main;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.InputMismatchException;
import java.util.Scanner;

import entidades.GestorCredenciales;
import entidades.Perfil;
import entidades.Sesion;
import entidades.Usuario;

public class Main {

	static Scanner sc = new Scanner(System.in);

	public static void main(String[] args) {

		String ruta = "src/main/resources/credenciales.txt";
		GestorCredenciales gestor = new GestorCredenciales(ruta);
		Sesion sesion = new Sesion(gestor);

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

			opcion = sc.nextInt(); // Opción a elegir del menú
			sc.nextLine(); // Limpiar el buffer
			switch (opcion) {
			case 1: {

				// Login en el main -CU1
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
				break;
			}
			case 2: {
				sesion.logout();
				break;
			}
			case 3: {

				// Comprobamos si es admin o secretariado
				if (sesion.estaAutenticado() && (sesion.getUsuarioActual().getPerfil() == Perfil.ADMIN
						|| sesion.getUsuarioActual().getPerfil() == Perfil.SECRETARIADO)) {
					int opcion_empleado = -1;
					do {
						// Submenu
						System.out.println("\n=======GESTION DE EMPLEADO=======");
						System.out.println("1. Crear un nuevo empleado");
						System.out.println("2. Salir");

						switch (opcion_empleado) {

						case 1: {

							String nombreUsuario;
							String nombrePersona;
							Perfil perfil = null;

							System.out.println("Introduzca el nombre del usuario");
							nombreUsuario = sc.nextLine();

							System.out.println("Introduzca el nombre de la persona");
							nombreUsuario = sc.nextLine();

							do {

							} while (perfil != Perfil.ENFERMERIA || perfil != Perfil.SECRETARIADO);

						}
						case 2: {
							System.out.println("Saliendo del submenu");
							break;
						}

						}

					} while (opcion_empleado != 2);

				} else
					System.out.println("\nNo esta autorizado para esta accion");
				break;
			}
			case 4: {
				System.out.println("\nEn progreso");
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

	// CU-2
	public Usuario crearUsuario(String nombreUsuario, String nombrePersona, Perfil perfil) {
		Usuario u = new Usuario(nombreUsuario, nombrePersona, perfil);
		return u;
	}
}
