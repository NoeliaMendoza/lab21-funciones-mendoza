package ec.edu.espe.pa.funcional;

import ec.edu.espe.pa.funcional.contrato.FabricaIncidencia;
import ec.edu.espe.pa.funcional.funcional.ProcesadorFuncional;
import ec.edu.espe.pa.funcional.imperativo.ProcesadorImperativo;
import ec.edu.espe.pa.funcional.modelo.Incidencia;

import java.util.List;
import java.util.function.Function;

import static ec.edu.espe.pa.funcional.reglas.ReglasIncidencia.CATEGORIA;
import static ec.edu.espe.pa.funcional.reglas.ReglasIncidencia.ESTA_ABIERTA;
import static ec.edu.espe.pa.funcional.reglas.ReglasIncidencia.NOTIFICAR;
import static ec.edu.espe.pa.funcional.reglas.ReglasIncidencia.REQUIERE_ATENCION;
import static ec.edu.espe.pa.funcional.reglas.ReglasIncidencia.RESUMEN;

public class App {
    public static void main(String[] args) {
        FabricaIncidencia fabrica = Incidencia::new;
        List<Incidencia> datos = List.of(
                fabrica.crear("INC-001", "Caída del servidor", "ABIERTA", 5),
                fabrica.crear("INC-002", "Error tipográfico en el menú", "ABIERTA", 2),
                fabrica.crear("INC-003", "Fallo de respaldo nocturno", "CERRADA", 5),
                fabrica.crear("INC-004", "Lentitud en reportes", "ABIERTA", 3),
                fabrica.crear("INC-005", "Error de acceso al sistema", "ABIERTA", 4),
                fabrica.crear("INC-006", "Actualizar certificado", "EN_PROCESO", 4));

        System.out.println("== Versión imperativa ==");
        new ProcesadorImperativo().mostrarQueRequierenAtencion(datos);

        ProcesadorFuncional procesador = new ProcesadorFuncional();
        System.out.println("== Versión funcional ==");
        procesador.titulosQueRequierenAtencion(datos).forEach(System.out::println);

        System.out.println("== Notificaciones ==");
        procesador.ejecutar(datos, REQUIERE_ATENCION, NOTIFICAR);

        System.out.println("== Incidencias abiertas con su categoría ==");
        Function<Incidencia, String> etiqueta = CATEGORIA.andThen(categoria -> "[" + categoria + "]");
        procesador.seleccionar(datos, ESTA_ABIERTA,
                        incidencia -> RESUMEN.apply(incidencia) + " " + etiqueta.apply(incidencia))
                .forEach(System.out::println);
    }
}
