package ec.edu.espe.pa.funcional.contrato;

import ec.edu.espe.pa.funcional.modelo.Incidencia;

@FunctionalInterface
public interface ReglaIncidencia {
    boolean evaluar(Incidencia incidencia);

    default ReglaIncidencia y(ReglaIncidencia otra) {
        return incidencia -> evaluar(incidencia) && otra.evaluar(incidencia);
    }
}
