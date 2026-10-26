package entidades;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;


public class GestorAlergias {

	private final File carpeta;

	public GestorAlergias(String rutaCarpeta) {
		this.carpeta = new File(rutaCarpeta);
		if (!carpeta.exists()) {
			carpeta.mkdirs();
		}
	}

	private File ficheroDe(long id) {
		return new File(carpeta, "alergia_" + id + ".dat");
	}

	public void guardar(Alergia a) throws IOException {
		try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(ficheroDe(a.getId())))) {
			oos.writeObject(a);
		}
	}

	private Alergia leer(File f) throws IOException, ClassNotFoundException {
		try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(f))) {
			return (Alergia) ois.readObject();
		}
	}

	
	public List<Alergia> listarTodas() {
		List<Alergia> lista = new ArrayList<>();
		File[] ficheros = carpeta.listFiles((d, n) -> n.endsWith(".dat"));
		
		if (ficheros == null)
			return lista;
		
		for (File f : ficheros) {
			try {
				lista.add(leer(f));
			} catch (IOException | ClassNotFoundException | ClassCastException e) {
				System.out.println("Aviso: no se pudo leer el fichero " + f.getName());
			}
		}
		
		lista.sort(Comparator.comparingLong(Alergia::getId));
		return lista;
	}

	
	public long siguienteId() {
		long max = 0;
		for (Alergia a : listarTodas()) {
			if (a.getId() > max)
				max = a.getId();
		}
		return max + 1;
	}

	
	public Alergia buscarPorId(long id) {
		File f = ficheroDe(id);
		if (!f.exists())
			return null;
		try {
			return leer(f);
		} catch (IOException | ClassNotFoundException | ClassCastException e) {
			System.out.println("Aviso: el fichero de la alergia está dañado.");
			return null;
		}
	}
}