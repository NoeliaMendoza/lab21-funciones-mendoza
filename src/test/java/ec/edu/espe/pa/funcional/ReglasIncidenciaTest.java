package ec.edu.espe.pa.funcional;

import ec.edu.espe.pa.funcional.contrato.ReglaIncidencia;
import ec.edu.espe.pa.funcional.funcional.ProcesadorFuncional;
import ec.edu.espe.pa.funcional.modelo.Incidencia;
import ec.edu.espe.pa.funcional.reglas.PrioridadAlta;
import ec.edu.espe.pa.funcional.reglas.ReglasIncidencia;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

import static ec.edu.espe.pa.funcional.reglas.ReglasIncidencia.*;
import static org.junit.jupiter.api.Assertions.*;

class ReglasIncidenciaTest {
    @ParameterizedTest
    @CsvSource({
            "ABIERTA, 5, true, true, true, CRITICA",
            "ABIERTA, 2, true, false, false, MEDIA",
            "CERRADA, 5, false, true, false, CRITICA",
            "ABIERTA, 3, true, false, false, MEDIA",
            "ABIERTA, 4, true, true, true, ALTA",
            "ABIERTA, 1, true, false, false, BAJA",
            "EN_PROCESO, 4, false, true, false, ALTA"
    })
    void evaluaReglasYCategoria(String estado, int prioridad, boolean abierta,
                                 boolean alta, boolean atencion, String categoria) {
        Incidencia incidencia = new Incidencia("INC-001", "Caída del servidor", estado, prioridad);
        assertEquals(abierta, ESTA_ABIERTA.test(incidencia));
        assertEquals(alta, PRIORIDAD_ALTA.test(incidencia));
        assertEquals(atencion, REQUIERE_ATENCION.test(incidencia));
        assertEquals(categoria, CATEGORIA.apply(incidencia));
    }

    @Test
    void componePredicadosYFunciones() {
        Incidencia abierta = new Incidencia("INC-001", "Caída del servidor", "ABIERTA", 3);
        Incidencia cerrada = new Incidencia("INC-002", "Fallo de respaldo", "CERRADA", 5);

        Predicate<Incidencia> pendienteBaja = ESTA_ABIERTA.and(PRIORIDAD_ALTA.negate());
        Predicate<Incidencia> revisarHoy = REQUIERE_ATENCION.or(ReglasIncidencia::esCritica);
        assertTrue(pendienteBaja.test(abierta));
        assertFalse(pendienteBaja.test(cerrada));
        assertTrue(revisarHoy.test(cerrada));
        assertFalse(revisarHoy.test(abierta));

        assertEquals("INC-001 - Caída del servidor", RESUMEN.apply(abierta));
        Function<String, String> mayusculas = texto -> texto.toUpperCase(Locale.ROOT);
        assertEquals("INC-001 - CAÍDA DEL SERVIDOR", RESUMEN.andThen(mayusculas).apply(abierta));
        assertEquals(RESUMEN.andThen(mayusculas).apply(abierta), mayusculas.compose(RESUMEN).apply(abierta));
    }

    @ParameterizedTest
    @ValueSource(ints = {3, 4, 5})
    void lasTresImplementacionesAplicanLaMismaRegla(int prioridad) {
        Incidencia incidencia = new Incidencia("INC-001", "Ejemplo", "ABIERTA", prioridad);
        ReglaIncidencia conClase = new PrioridadAlta();
        ReglaIncidencia anonima = new ReglaIncidencia() {
            @Override
            public boolean evaluar(Incidencia valor) {
                return PRIORIDAD_ALTA.test(valor);
            }
        };
        ReglaIncidencia lambda = valor -> PRIORIDAD_ALTA.test(valor);
        ReglaIncidencia abierta = ESTA_ABIERTA::test;
        assertEquals(prioridad >= 4, conClase.evaluar(incidencia));
        assertEquals(conClase.evaluar(incidencia), anonima.evaluar(incidencia));
        assertEquals(conClase.evaluar(incidencia), lambda.evaluar(incidencia));
        assertEquals(REQUIERE_ATENCION.test(incidencia), abierta.y(conClase).evaluar(incidencia));
    }

    @Test
    void ejecutaAccionesEnOrdenSoloParaLasIncidenciasSeleccionadas() {
        List<Incidencia> datos = List.of(
                new Incidencia("INC-001", "Caída del servidor", "ABIERTA", 5),
                new Incidencia("INC-002", "Error tipográfico", "ABIERTA", 2),
                new Incidencia("INC-003", "Fallo de respaldo", "CERRADA", 5));

        List<String> bitacora = new ArrayList<>();
        Consumer<Incidencia> registrarId = incidencia -> bitacora.add(incidencia.id());
        Consumer<Incidencia> registrarTitulo = incidencia -> bitacora.add(incidencia.titulo());
        new ProcesadorFuncional().ejecutar(datos, REQUIERE_ATENCION, registrarId.andThen(registrarTitulo));

        assertEquals(List.of("INC-001", "Caída del servidor"), bitacora);
    }

    @ParameterizedTest
    @ValueSource(ints = {0, 6})
    void rechazaPrioridadesFueraDeRango(int prioridad) {
        assertThrows(IllegalArgumentException.class,
                () -> new Incidencia("INC-X", "Inválida", "ABIERTA", prioridad));
    }

    @Test
    void rechazaEstadosDesconocidos() {
        assertThrows(IllegalArgumentException.class,
                () -> new Incidencia("INC-X", "Inválida", "PENDIENTE", 3));
    }

    @Test
    void generadorEntregaIdentificadoresDistintosYPrioridadesValidas() {
        Supplier<Incidencia> generador = generadorSintetico();
        Incidencia primera = generador.get();
        Incidencia segunda = generador.get();
        assertNotEquals(primera.id(), segunda.id());
        assertTrue(primera.prioridad() >= 1 && primera.prioridad() <= 5);
        assertTrue(segunda.prioridad() >= 1 && segunda.prioridad() <= 5);
    }
}
