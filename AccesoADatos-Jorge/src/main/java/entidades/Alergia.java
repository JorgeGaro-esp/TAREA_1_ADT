package entidades;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Alergia implements Serializable{

    // Atributos
    private long id;
    private String nombreA;
    private Medicamento medicamento;
    private List<Historial> historiales = new ArrayList<>();

    // Constructores
    public Alergia() {
    }

    public Alergia(long id, String nombreA, Medicamento medicamento) {
        this.id = id;
        this.nombreA = nombreA;
        setMedicamento(medicamento);
    }

    // Getters y Setters
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNombreA() {
        return nombreA;
    }

    public void setNombreA(String nombreA) {
        this.nombreA = nombreA;
    }

    public Medicamento getMedicamento() {
        return medicamento;
    }

    public void setMedicamento(Medicamento nuevo) {
        if (this.medicamento == nuevo) {
            return;
        }
        if (this.medicamento != null) {
            this.medicamento.getAlergias().remove(this);
        }
        this.medicamento = nuevo;
        if (nuevo != null && !nuevo.getAlergias().contains(this)) {
            nuevo.getAlergias().add(this);
        }
    }

    public List<Historial> getHistoriales() {
        return historiales;
    }

    public void setHistoriales(List<Historial> historiales) {
        this.historiales = historiales;
    }

    // Metodos
    public void agregarHistorial(Historial historial) {
        historial.agregarAlergia(this);
    }

    public void eliminarHistorial(Historial historial) {
        historial.eliminarAlergia(this);
    }

    @Override
    public String toString() {
        return "Alergia [id=" + id + ", nombreA=" + nombreA + ", medicamento="
                + (medicamento != null ? medicamento.getNombreM() : null) + "]";
    }
}
