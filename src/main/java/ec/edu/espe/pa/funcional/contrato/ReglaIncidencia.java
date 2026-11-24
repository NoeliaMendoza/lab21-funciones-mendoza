package ec.edu.espe.pa.funcional.contrato;

import ec.edu.espe.pa.funcional.modelo.Incidencia;

/**
 * Condición sobre una incidencia. Su único método abstracto permite implementarla con una lambda.
 */
@FunctionalInterface
public interface ReglaIncidencia {
    /**
     * Devuelve {@code true} si la incidencia cumple la condición.
     */
    boolean evaluar(Incidencia incidencia);

    /**
     * Combina dos reglas con un AND. Evalúa la segunda solo si se cumple la primera.
     */
    default ReglaIncidencia y(ReglaIncidencia otra) {
        return incidencia -> evaluar(incidencia) && otra.evaluar(incidencia);
    }
}
