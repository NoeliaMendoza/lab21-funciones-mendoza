package ec.edu.espe.pa.funcional.reglas;

import ec.edu.espe.pa.funcional.modelo.Incidencia;

import java.util.Locale;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

public final class ReglasIncidencia {
    private ReglasIncidencia() {
    }

    public static final int UMBRAL_PRIORIDAD_ALTA = 4;

    // Predicate: recibe una incidencia y devuelve si está abierta.
    public static final Predicate<Incidencia> ESTA_ABIERTA =
            incidencia -> "ABIERTA".equals(incidencia.estado());

    // Predicate: recibe una incidencia y devuelve si alcanza el umbral.
    public static final Predicate<Incidencia> PRIORIDAD_ALTA = new PrioridadAlta()::evaluar;

    // Predicate: combina dos condiciones y devuelve una sola decisión.
    public static final Predicate<Incidencia> REQUIERE_ATENCION = ESTA_ABIERTA.and(PRIORIDAD_ALTA);

    // Function: transforma una incidencia en un resumen de texto.
    public static final Function<Incidencia, String> RESUMEN =
            incidencia -> incidencia.id() + " - " + incidencia.titulo();

    // Function: transforma la prioridad de una incidencia en una categoría.
    public static final Function<Incidencia, String> CATEGORIA = incidencia -> switch (incidencia.prioridad()) {
        case 1 -> "BAJA";
        case 2, 3 -> "MEDIA";
        case 4 -> "ALTA";
        case 5 -> "CRITICA";
        default -> throw new IllegalArgumentException("Prioridad inválida: " + incidencia.prioridad());
    };

    // Consumer: recibe una incidencia, imprime su aviso y no devuelve un valor.
    public static final Consumer<Incidencia> NOTIFICAR =
            incidencia -> System.out.println("[ATENCION] " + RESUMEN.apply(incidencia));

    // Supplier: genera una incidencia sintética sin recibir argumentos.
    public static Supplier<Incidencia> generadorSintetico() {
        AtomicInteger contador = new AtomicInteger(1);
        String[] estados = {"ABIERTA", "EN_PROCESO", "CERRADA"};
        return () -> {
            int numero = contador.getAndIncrement();
            return new Incidencia(String.format(Locale.ROOT, "INC-%03d", numero),
                    "Incidencia sintética " + numero,
                    estados[Math.floorMod(numero, estados.length)],
                    Math.floorMod(numero, 5) + 1);
        };
    }

    public static boolean esCritica(Incidencia incidencia) {
        return incidencia.prioridad() == 5;
    }
}
