package ec.edu.espe.pa.funcional;

import ec.edu.espe.pa.funcional.contrato.ReglaIncidencia;
import ec.edu.espe.pa.funcional.modelo.Incidencia;
import ec.edu.espe.pa.funcional.reglas.PrioridadAlta;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;


public class Demostracion {
    public static void main(String[] args) {
        System.out.println("== Fase 2: interfaces funcionales ==");
        Predicate<Incidencia> estaAbierta = incidencia -> "ABIERTA".equals(incidencia.estado());
        Function<Incidencia, String> obtenerResumen =
                incidencia -> incidencia.id() + " - " + incidencia.titulo();
        Consumer<Incidencia> mostrar = incidencia -> System.out.println(incidencia.titulo());
        Supplier<Incidencia> crearEjemplo =
                () -> new Incidencia("INC-001", "Error de acceso", "ABIERTA", 3);

        Incidencia ejemplo = crearEjemplo.get();
        boolean abierta = estaAbierta.test(ejemplo);
        String resumen = obtenerResumen.apply(ejemplo);
        System.out.println("Supplier.get() - incidencia creada: " + ejemplo.id());
        System.out.println("Predicate.test() - abierta: " + abierta);
        System.out.println("Function.apply() - resumen: " + resumen);
        System.out.print("Consumer.accept() - título: ");
        mostrar.accept(ejemplo);

        int umbral = 4;
        Predicate<Incidencia> superaUmbral = incidencia -> incidencia.prioridad() >= umbral;
        System.out.println("Prioridad alta con umbral capturado: " + superaUmbral.test(ejemplo));

        System.out.println("\n== Fase 3: contrato ReglaIncidencia ==");
        ReglaIncidencia prioridadAlta = new PrioridadAlta();
        ReglaIncidencia abiertaAnonima = new ReglaIncidencia() {
            @Override
            public boolean evaluar(Incidencia incidencia) {
                return "ABIERTA".equals(incidencia.estado());
            }
        };
        ReglaIncidencia abiertaLambda = incidencia -> "ABIERTA".equals(incidencia.estado());
        ReglaIncidencia requiereAtencion = abiertaLambda.y(prioridadAlta);
        Predicate<Incidencia> comoPredicado = requiereAtencion::evaluar;

        System.out.println("Clase con nombre - prioridad alta: " + prioridadAlta.evaluar(ejemplo));
        System.out.println("Clase anónima - abierta: " + abiertaAnonima.evaluar(ejemplo));
        System.out.println("Lambda - abierta: " + abiertaLambda.evaluar(ejemplo));
        System.out.println("Regla compuesta - requiere atención: " + comoPredicado.test(ejemplo));
    }
}
