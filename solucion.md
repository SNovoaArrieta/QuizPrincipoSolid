## Solución: Aplicación de los principios SOLID al módulo GestorCampeonato
El código original concentra múltiples responsabilidades en una única clase GestorCampeonato (registro, lógica de bonificaciones, generación de reportes y salida por consola). Esto lo hace rígido y frágil. He aplicado SRP, OCP, LSP, ISP y DIP mediante la creación de clases e interfaces con responsabilidades claras, inyección de dependencias para formateadores de reportes, y separación de comportamientos (registro, cálculo de bonificaciones, y formateo de reportes). La salida en consola del código refactorizado es idéntica a la del programa original.

## 1) SRP — (Principio de Responsabilidad Única)
La clase GestorCampeonato mezcla: registro de participantes, cálculo de bonificaciones, y generación/formatado de reportes (impresión en consola). Esto viola SRP porque una clase tiene múltiples razones para cambiar.

## Por qué viola SRP:
porque modifica la forma de generar reportes o en el formato de salida obliga a cambiar GestorCampeonato aunque no tenga relación con la lógica de registro o bonificaciones.

Refactorización (aplicación SRP):
Separamos responsabilidades en clases:
ParticipantRegistrar — registra participantes y retorna lista 
BonusCalculator — se encarga únicamente del cálculo (y la impresión) de bonificaciones.
ReportService — orquesta generación de reportes usando ReportFormatter (responsabilidad: delegar formateo).

