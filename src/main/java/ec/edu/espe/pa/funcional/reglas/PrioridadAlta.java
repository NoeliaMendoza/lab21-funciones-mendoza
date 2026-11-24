package ec.edu.espe.pa.funcional.reglas;

import ec.edu.espe.pa.funcional.contrato.ReglaIncidencia;
import ec.edu.espe.pa.funcional.modelo.Incidencia;

/**
 * Regla que acepta incidencias con prioridad igual o superior al umbral configurado.
 */
public class PrioridadAlta implements ReglaIncidencia {
    /**
     * {@inheritDoc}
     */
    @Override
    public boolean evaluar(Incidencia incidencia) {
        return incidencia.prioridad() >= ReglasIncidencia.UMBRAL_PRIORIDAD_ALTA;
    }
}

