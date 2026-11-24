package ec.edu.espe.pa.funcional.modelo;

import java.util.Objects;

/**
 * Representa los datos inmutables de una incidencia de soporte.
 *
 * @param id identificador obligatorio
 * @param titulo descripción breve de la incidencia
 * @param estado uno de {@code ABIERTA}, {@code EN_PROCESO} o {@code CERRADA}
 * @param prioridad valor entre 1 y 5, inclusive
 */
public record Incidencia(String id, String titulo, String estado, int prioridad) {
    /**
     * Valida el identificador, el estado y el rango de prioridad.
     *
     * @throws NullPointerException si el identificador o el estado es nulo
     * @throws IllegalArgumentException si el estado o la prioridad no es válido
     */
    public Incidencia {
        Objects.requireNonNull(id, "El id es obligatorio");
        Objects.requireNonNull(estado, "El estado es obligatorio");
        if (prioridad < 1 || prioridad > 5) {
            throw new IllegalArgumentException("La prioridad debe estar entre 1 y 5: " + prioridad);
        }
        switch (estado) {
            case "ABIERTA", "EN_PROCESO", "CERRADA" -> { }
            default -> throw new IllegalArgumentException("Estado inválido: " + estado);
        }
    }
}

