package entidades;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Medicamento implements Serializable {

    // Atributos
    private long id;
    private String nombreM;
    private String principioActivo;
    private double dosisMax; 
    private List<Alergia> alergias = new ArrayList<>();

    // Constructores
    public Medicamento() {
    }

    public Medicamento(long id, String nombreM, String principioActivo, double dosisMax) {
        this.id = id;
        this.nombreM = nombreM;
        this.principioActivo = principioActivo;
        this.dosisMax = dosisMax;
    }

    // Getters y Setters
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNombreM() {
        return nombreM;
    }

    public void setNombreM(String nombreM) {
        this.nombreM = nombreM;
    }

    public String getPrincipioActivo() {
        return principioActivo;
    }

    public void setPrincipioActivo(String principioActivo) {
        this.principioActivo = principioActivo;
    }

    public double getDosisMax() {
        return dosisMax;
    }

    public void setDosisMax(double dosisMax) {
        this.dosisMax = dosisMax;
    }

    public List<Alergia> getAlergias() {
        return alergias;
    }

    public void setAlergias(List<Alergia> alergias) {
        this.alergias = alergias;
    }

    public void agregarAlergia(Alergia alergia) {
        alergia.setMedicamento(this);
    }

    public void eliminarAlergia(Alergia alergia) {
        if (alergias.contains(alergia)) {
            alergia.setMedicamento(null);
        }
    }

    @Override
    public String toString() {
        return "Medicamento [id=" + id + ", nombreM=" + nombreM + ", principioActivo=" + principioActivo
                + ", dosisMax=" + dosisMax + " mg]";
    }
}
