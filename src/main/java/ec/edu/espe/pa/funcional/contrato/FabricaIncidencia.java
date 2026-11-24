package ec.edu.espe.pa.funcional.contrato;

import ec.edu.espe.pa.funcional.modelo.Incidencia;

/**
 * Contrato de creación compatible con la referencia al constructor {@code Incidencia::new}.
 */
@FunctionalInterface
public interface FabricaIncidencia {
    /**
     * Crea una incidencia a partir de sus cuatro datos.
     */
    Incidencia crear(String id, String titulo, String estado, int prioridad);
}
