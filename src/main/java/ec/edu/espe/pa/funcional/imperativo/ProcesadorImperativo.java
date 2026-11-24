package ec.edu.espe.pa.funcional.imperativo;

import ec.edu.espe.pa.funcional.modelo.Incidencia;

import java.util.ArrayList;
import java.util.List;

/**
 * Procesa incidencias mediante bucles y condiciones explícitas.
 */
public class ProcesadorImperativo {
    /**
     * Devuelve en orden los títulos de las incidencias abiertas con prioridad 4 o 5.
     */
    public List<String> titulosQueRequierenAtencion(List<Incidencia> incidencias) {
        List<String> resultado = new ArrayList<>();
        for (Incidencia incidencia : incidencias) {
            if (incidencia.estado().equals("ABIERTA") && incidencia.prioridad() >= 4) {
                resultado.add(incidencia.titulo());
            }
        }
        return resultado;
    }

    /**
     * Imprime los títulos seleccionados, uno por línea.
     */
    public void mostrarQueRequierenAtencion(List<Incidencia> incidencias) {
        for (String titulo : titulosQueRequierenAtencion(incidencias)) {
            System.out.println(titulo);
        }
    }
}
