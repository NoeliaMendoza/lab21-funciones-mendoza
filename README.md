# Laboratorio 2.1 · Funciones y contratos funcionales en Java

Práctica de Programación Avanzada de la Universidad de las Fuerzas Armadas ESPE, sede Santo Domingo. El proyecto usa un conjunto de incidencias de soporte ficticias para comparar una solución imperativa con una solución funcional en Java 21.

El objetivo es separar tres operaciones del procesamiento: decidir qué incidencias cumplen una regla, transformar los datos seleccionados y ejecutar una acción. Las pruebas comprueban que ambas versiones conservan los resultados, el orden y los datos de entrada.

## Reglas del dominio

Cada incidencia tiene un identificador, un título, un estado y una prioridad. El modelo exige un identificador y un estado, admite los estados `ABIERTA`, `EN_PROCESO` y `CERRADA`, y valida que la prioridad esté entre 1 y 5.

Una incidencia requiere atención cuando está **abierta y su prioridad es 4 o 5**. La categoría depende de la prioridad.

| Prioridad | Categoría |
| --- | --- |
| 1 | `BAJA` |
| 2 o 3 | `MEDIA` |
| 4 | `ALTA` |
| 5 | `CRITICA` |

## Cómo funciona

`ProcesadorImperativo` recorre la lista con un `for`, aplica la condición con un `if` y agrega el título de cada incidencia seleccionada. `ProcesadorFuncional` recibe las operaciones como parámetros. Su método `seleccionar` filtra con un `Predicate` y transforma con una `Function`, mientras que `ejecutar` aplica un `Consumer` a los elementos seleccionados. Así, el mismo procesador puede generar títulos, producir resúmenes o enviar notificaciones según las funciones que reciba.

| Interfaz | Operación en el laboratorio |
| --- | --- |
| `Predicate<Incidencia>` | Decide si una incidencia está abierta o tiene prioridad alta. `REQUIERE_ATENCION` combina ambas reglas con `and`. |
| `Function<Incidencia, String>` | Transforma una incidencia en un resumen o categoría. |
| `Consumer<Incidencia>` | Ejecuta una acción sobre una incidencia, como imprimir una notificación o registrar su identificador. |
| `Supplier<Incidencia>` | Genera una incidencia sintética por llamada a `get()`, sin parámetros. |
| `ReglaIncidencia` | Define el contrato `evaluar` y permite combinar reglas con el método `y`. Se usa con una clase, una clase anónima y una lambda. |
| `FabricaIncidencia` | Define el contrato de creación con cuatro argumentos y permite usar la referencia `Incidencia::new`. |

## Estructura del proyecto

```text
src/main/java/ec/edu/espe/pa/funcional/
├── App.java
├── Demostracion.java
├── modelo/
│   └── Incidencia.java
├── contrato/
│   ├── FabricaIncidencia.java
│   └── ReglaIncidencia.java
├── reglas/
│   ├── PrioridadAlta.java
│   └── ReglasIncidencia.java
├── imperativo/
│   └── ProcesadorImperativo.java
└── funcional/
    └── ProcesadorFuncional.java

src/test/java/ec/edu/espe/pa/funcional/
├── EquivalenciaTest.java
└── ReglasIncidenciaTest.java
```

## Ejecutar el proyecto

Necesitas **JDK 21** y **Apache Maven**. Desde la raíz del proyecto, donde está `pom.xml`, ejecuta:

```shell
mvn -v
mvn test
java -cp target/classes ec.edu.espe.pa.funcional.App
java -cp target/classes ec.edu.espe.pa.funcional.Demostracion
```

`App` ejecuta los dos procesadores sobre seis incidencias ficticias y muestra los títulos seleccionados, las notificaciones y las categorías de las incidencias abiertas. Las dos primeras secciones de la salida muestran los mismos títulos.

```text
== Versión imperativa ==
Caída del servidor
Error de acceso al sistema
== Versión funcional ==
Caída del servidor
Error de acceso al sistema
```

`Demostracion` ejecuta los ejemplos de las fases 2, 3 y 5. Muestra cuándo se ejecutan las lambdas, cómo se combina el contrato `ReglaIncidencia` y cómo se usan `and`, `or`, `negate`, `andThen`, `compose` y las referencias a métodos.

También puedes abrir el proyecto como proyecto Maven en IntelliJ IDEA, seleccionar el JDK 21 y ejecutar `App.main()` o `Demostracion.main()` desde el botón junto a cada método `main`.

## Pruebas y verificación del umbral

El proyecto ejecuta **20 pruebas con JUnit 5** mediante `mvn test`.

- `ReglasIncidenciaTest` comprueba los estados, las categorías, las prioridades límite, las entradas inválidas, la composición de funciones y el comportamiento del generador.
- `EquivalenciaTest` comprueba que ambas versiones producen los mismos títulos en el mismo orden y conservan la lista original. Incluye una lista vacía, un caso individual, un conjunto mixto y una lista con 50 incidencias sintéticas.

Para comprobar que las pruebas detectan errores en el umbral, cambia temporalmente `>=` por `>` en `PrioridadAlta.evaluar` y ejecuta `mvn test`. Con ese cambio, una incidencia abierta de prioridad 4 queda fuera de la selección y las pruebas fallan. Restaura `>=` y ejecuta el comando otra vez para confirmar que todas pasan.
