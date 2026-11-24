package ec.edu.espe.pa.funcional.funcional;

import ec.edu.espe.pa.funcional.modelo.Incidencia;
import ec.edu.espe.pa.funcional.reglas.ReglasIncidencia;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * Procesa una lista de incidencias con reglas, transformaciones y acciones recibidas como parámetros.
 */
public class ProcesadorFuncional {
    /**
     * Filtra las incidencias y transforma las que cumplen la regla en el orden de entrada.
     *
     * @param <R> tipo de cada resultado
     * @param incidencias lista de entrada
     * @param regla condición de selección
     * @param transformacion función que genera un resultado por incidencia seleccionada
     * @return lista no modificable con los resultados
     */
    public <R> List<R> seleccionar(List<Incidencia> incidencias, Predicate<Incidencia> regla,
                                  Function<Incidencia, R> transformacion) {
        return incidencias.stream()
                .filter(regla)
                .map(transformacion)
                .toList();
    }

    /**
     * Devuelve en orden los títulos de las incidencias abiertas con prioridad 4 o 5.
     */
    public List<String> titulosQueRequierenAtencion(List<Incidencia> incidencias) {
        return seleccionar(incidencias, ReglasIncidencia.REQUIERE_ATENCION, Incidencia::titulo);
    }

    /**
     * Aplica la acción a cada incidencia que cumple la regla, en el orden de entrada.
     */
    public void ejecutar(List<Incidencia> incidencias, Predicate<Incidencia> regla,
                         Consumer<Incidencia> accion) {
        incidencias.stream().filter(regla).forEachOrdered(accion);
    }
}
