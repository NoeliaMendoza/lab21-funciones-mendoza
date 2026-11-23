package ec.edu.espe.pa.funcional;

import ec.edu.espe.pa.funcional.funcional.ProcesadorFuncional;
import ec.edu.espe.pa.funcional.imperativo.ProcesadorImperativo;
import ec.edu.espe.pa.funcional.modelo.Incidencia;
import ec.edu.espe.pa.funcional.reglas.ReglasIncidencia;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class EquivalenciaTest {
    static Stream<List<Incidencia>> escenarios() {
        return Stream.of(
                List.of(),
                List.of(new Incidencia("INC-001", "Caída del servidor", "ABIERTA", 5)),
                List.of(new Incidencia("INC-004", "Lentitud en reportes", "ABIERTA", 3),
                        new Incidencia("INC-005", "Error de acceso", "ABIERTA", 4),
                        new Incidencia("INC-003", "Fallo de respaldo", "CERRADA", 5),
                        new Incidencia("INC-001", "Caída del servidor", "ABIERTA", 5),
                        new Incidencia("INC-006", "Actualizar certificado", "EN_PROCESO", 4)),
                Stream.generate(ReglasIncidencia.generadorSintetico()).limit(50).toList());
    }

    @ParameterizedTest
    @MethodSource("escenarios")
    void ambasSolucionesConservanElResultadoYLosDatos(List<Incidencia> escenario) {
        List<Incidencia> datos = new ArrayList<>(escenario);

        List<String> esperado = new ProcesadorImperativo().titulosQueRequierenAtencion(datos);
        assertEquals(escenario, datos);

        List<String> obtenido = new ProcesadorFuncional().titulosQueRequierenAtencion(datos);
        assertEquals(esperado, obtenido);
        assertEquals(escenario, datos);
    }
}
