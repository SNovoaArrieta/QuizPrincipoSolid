## Solución: Aplicación de los principios SOLID al módulo GestorCampeonato
El código original concentra múltiples responsabilidades en una única clase GestorCampeonato (registro, lógica de bonificaciones, generación de reportes y salida por consola). Esto lo hace rígido y frágil. He aplicado SRP, OCP, LSP, ISP y DIP mediante la creación de clases e interfaces con responsabilidades claras, inyección de dependencias para formateadores de reportes, y separación de comportamientos (registro, cálculo de bonificaciones, y formateo de reportes). La salida en consola del código refactorizado es idéntica a la del programa original.

## 1) SRP —(Principio de Responsabilidad Única)
La clase GestorCampeonato mezcla: registro de participantes, cálculo de bonificaciones, y generación/formatado de reportes (impresión en consola). Esto viola SRP porque una clase tiene múltiples razones para cambiar.

## Por qué viola SRP:
porque modifica la forma de generar reportes o en el formato de salida obliga a cambiar GestorCampeonato aunque no tenga relación con la lógica de registro o bonificaciones.

## Refactorización (aplicación SRP):
Separamos responsabilidades en clases:
ParticipantRegistrar — registra participantes y retorna lista 
BonusCalculator — se encarga únicamente del cálculo (y la impresión) de bonificaciones.
ReportService — orquesta generación de reportes usando ReportFormatter (responsabilidad: delegar formateo).

## 2) OCP (Abierto/Cerrado)
generarReportes(String formato) contiene condicionales if (formato.equalsIgnoreCase("TEXTO")) ... else if (formato.equalsIgnoreCase("HTML")) ... — para añadir un nuevo formato habría que modificar este método, rompiendo OCP. 

## Por qué viola OCP:
El método no está abierto para extensión sin modificación. Cada nuevo formato implica editar el método central.

## Refactorización (aplicación OCP):
Definimos la interfaz ReportFormatter y creamos implementaciones TextReportFormatter y HtmlReportFormatter. ReportService solicita un ReportFormatter (vía fábrica o inyección) y lo utiliza. Para añadir un nuevo formato solo hay que crear una nueva implementación de ReportFormatter y registrarla — no modificar ReportService.


## 3) LSP (Sustitución de Liskov)
No hay herencia explícita en el anexo que sea problemática; sin embargo, la forma en que se llevan a cabo los reportes (condicionales) hace difícil sustituir piezas por otras sin afectar comportamiento. La refactorización crea jerarquías de formateadores que deben ser intercambiables sin cambiar el cliente.

## Por qué es importante aquí:
Cuando creamos ReportFormatter y sus implementaciones, todas las implementaciones deben respetar el contrato (devolver un String formateado), de forma que ReportService pueda sustituir una implementación por otra sin alteración del flujo.

## Refactorización (aplicación LSP):
Todas las implementaciones de ReportFormatter devuelven un String que ReportService imprimirá; ninguna implementación lanza excepciones inesperadas o cambia semántica esperada. Esto asegura que ReportService.generateReport("HTML") o ("TEXTO") producirá siempre un String válido.







