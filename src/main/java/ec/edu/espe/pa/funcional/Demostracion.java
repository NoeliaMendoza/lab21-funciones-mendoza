package ec.edu.espe.pa.funcional;

import ec.edu.espe.pa.funcional.contrato.FabricaIncidencia;
import ec.edu.espe.pa.funcional.contrato.ReglaIncidencia;
import ec.edu.espe.pa.funcional.modelo.Incidencia;
import ec.edu.espe.pa.funcional.reglas.PrioridadAlta;
import ec.edu.espe.pa.funcional.reglas.ReglasIncidencia;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

import static ec.edu.espe.pa.funcional.reglas.ReglasIncidencia.CATEGORIA;
import static ec.edu.espe.pa.funcional.reglas.ReglasIncidencia.ESTA_ABIERTA;
import static ec.edu.espe.pa.funcional.reglas.ReglasIncidencia.NOTIFICAR;
import static ec.edu.espe.pa.funcional.reglas.ReglasIncidencia.PRIORIDAD_ALTA;
import static ec.edu.espe.pa.funcional.reglas.ReglasIncidencia.REQUIERE_ATENCION;
import static ec.edu.espe.pa.funcional.reglas.ReglasIncidencia.RESUMEN;

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

        int umbral = ReglasIncidencia.UMBRAL_PRIORIDAD_ALTA;
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

        System.out.println("\n== Fase 5: composición y referencias a métodos ==");
        Predicate<Incidencia> noAbierta = ESTA_ABIERTA.negate();
        Predicate<Incidencia> pendienteBaja = ESTA_ABIERTA.and(PRIORIDAD_ALTA.negate());
        Predicate<Incidencia> esCritica = ReglasIncidencia::esCritica;
        Predicate<Incidencia> revisarHoy = REQUIERE_ATENCION.or(esCritica);
        Function<String, String> aMayusculas = String::toUpperCase;
        Function<Incidencia, String> resumenMayusculas = RESUMEN.andThen(aMayusculas);
        Function<Incidencia, String> mismoResultado = aMayusculas.compose(RESUMEN);
        Function<Incidencia, String> etiqueta = CATEGORIA.andThen(categoria -> "[" + categoria + "]");
        Function<Incidencia, String> titulo = Incidencia::titulo;
        Consumer<String> imprimir = System.out::println;
        FabricaIncidencia fabrica = Incidencia::new;
        Supplier<List<Incidencia>> nuevaLista = ArrayList::new;

        List<Incidencia> incidencias = nuevaLista.get();
        incidencias.add(ejemplo);
        incidencias.add(fabrica.crear("INC-002", "Fallo de conexión", "ABIERTA", 5));
        incidencias.add(fabrica.crear("INC-003", "Fallo de respaldo", "CERRADA", 5));
        System.out.println("Incidencias no abiertas:");
        incidencias.stream().filter(noAbierta).map(titulo).forEach(imprimir);
        System.out.println("Incidencias abiertas de prioridad baja:");
        incidencias.stream().filter(pendienteBaja).map(RESUMEN).forEach(imprimir);
        System.out.println("Incidencias para revisar hoy:");
        incidencias.stream().filter(revisarHoy).map(RESUMEN).forEach(imprimir);
        System.out.println("andThen: " + resumenMayusculas.apply(ejemplo));
        System.out.println("compose: " + mismoResultado.apply(ejemplo));
        System.out.println("Categoría del ejemplo: " + etiqueta.apply(ejemplo));

        List<String> bitacora = new ArrayList<>();
        Consumer<Incidencia> registrar = incidencia -> bitacora.add(incidencia.id());
        Consumer<Incidencia> notificarYRegistrar = NOTIFICAR.andThen(registrar);
        incidencias.stream().filter(REQUIERE_ATENCION).forEach(notificarYRegistrar);
        System.out.println("Bitácora: " + bitacora);
    }
}
