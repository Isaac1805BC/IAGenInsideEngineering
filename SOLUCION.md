IA:Claude

PROMT:En el readme hay un ejercicio el cual se llama Problema#1: El videoclub
de don mario en el readme se explica cual es la mision, las peliculas
disponibles y los casos de ejemplo, desarrolla el ejercicio en la
carpeta de Ejercicio1 identifica los patrones de diseño necesarios para
desarrollar el ejercicio, explica los principios solid que se ultilizan,
Aplica polimorfismo y encapsulamiento y coloca una evidencia de la
ejecucion del ejercicio(solo la ejecucion en consola)

---

# SOLUCION - IAGen Inside Engineering

---

## Problema #1: El Videoclub de Don Mario

### Estructura de archivos (`Ejercicio1/`)

```
Ejercicio1/
├── Pelicula.java               <- Clase abstracta (polimorfismo + encapsulamiento)
├── PeliculaFisica.java         <- Subclase concreta
├── PeliculaDigital.java        <- Subclase concreta
├── Membresia.java              <- Interfaz Strategy
├── MembresiaBasica.java        <- Estrategia sin descuento
├── MembresiaPremium.java       <- Estrategia 20% descuento
├── PeliculaFactory.java        <- Interfaz Factory Method
├── PeliculaFisicaFactory.java  <- Fabrica concreta
├── PeliculaDigitalFactory.java <- Fabrica concreta
├── Util.java                   <- Formateador de precios
├── Recibo.java                 <- Genera e imprime el recibo
├── Videoclub.java              <- Orquesta el flujo de alquiler
└── Main.java                   <- Punto de entrada
```

---

### Patrones de Diseño Utilizados

#### 1. Strategy (Comportamental)
**Donde:** interfaz `Membresia` con implementaciones `MembresiaBasica` y `MembresiaPremium`.

**Por que:** el algoritmo de calculo de precio varia segun el tipo de membresia. Strategy permite intercambiar el algoritmo en tiempo de ejecucion sin cambiar el codigo que lo usa (`Videoclub`, `Recibo`). Agregar una nueva membresia (p. ej. `MembresiaVIP`) solo requiere crear una nueva clase que implemente la interfaz.

```
<<interface>>
Membresia
  + aplicarDescuento(subtotal): double
  + getNombre(): String
       ^
       |
MembresiaBasica        MembresiaPremium
(sin descuento)        (20% descuento)
```

#### 2. Factory Method (Creacional)
**Donde:** interfaz `PeliculaFactory` con implementaciones `PeliculaFisicaFactory` y `PeliculaDigitalFactory`.

**Por que:** desacopla la creacion de objetos de quien los usa. `Videoclub` nunca llama `new PeliculaFisica(...)` directamente; delega la creacion a la fabrica correspondiente. Esto facilita agregar un nuevo tipo de pelicula (p. ej. `PeliculaStreaming`) creando solo una nueva fabrica y subclase, sin tocar `Videoclub`.

```
<<interface>>
PeliculaFactory
  + crearPelicula(...): Pelicula
         ^
         |
PeliculaFisicaFactory   PeliculaDigitalFactory
```

---

### Principios SOLID Aplicados

| Principio | Como se aplica |
|-----------|----------------|
| **S** Single Responsibility | Cada clase tiene una sola razon de cambio: `Pelicula` modela datos de una pelicula, `Recibo` genera el comprobante, `Videoclub` gestiona el flujo, `Util` formatea precios. |
| **O** Open/Closed | `Pelicula`, `Membresia` y `PeliculaFactory` son abstracciones estables. Nuevos tipos de pelicula o membresia se **agregan** con nuevas clases sin **modificar** las existentes. |
| **L** Liskov Substitution | `PeliculaFisica` y `PeliculaDigital` pueden reemplazar a `Pelicula` en cualquier contexto (listas, recibos) sin romper el comportamiento esperado. |
| **I** Interface Segregation | `Membresia` solo expone metodos de descuento; `PeliculaFactory` solo el metodo de creacion. Ninguna clase implementadora se ve forzada a implementar metodos que no necesita. |
| **D** Dependency Inversion | `Videoclub` y `Recibo` dependen de `Membresia` (interfaz) y `Pelicula` (clase abstracta), nunca de `MembresiaPremium` o `PeliculaFisica` directamente. |

---

### Polimorfismo y Encapsulamiento

**Polimorfismo:**
- `Pelicula` declara `getTipo()` como metodo abstracto. `PeliculaFisica` retorna `"Fisica"` y `PeliculaDigital` retorna `"Digital"`. En `Recibo.imprimir()` se itera una `List<Pelicula>` y se llama `p.getTipo()` sin ningun `instanceof` ni condicional; cada objeto responde con su propio comportamiento.
- Lo mismo ocurre con `Membresia.aplicarDescuento()`: la misma llamada produce resultados distintos segun sea `MembresiaBasica` o `MembresiaPremium`.

**Encapsulamiento:**
- En `Pelicula`, los atributos `titulo`, `precio` y `disponible` son `private`. El acceso externo se hace exclusivamente mediante getters (`getTitulo()`, `getPrecio()`, `isDisponible()`). Solo `setDisponible()` esta expuesto porque la disponibilidad puede cambiar al alquilar.
- En `Videoclub`, el catalogo (`List<Pelicula> catalogo`) es `private final`. Ningun codigo externo puede modificarlo directamente; solo se expone a traves de `mostrarCatalogo()` y `elegirPeliculas()`.
- En `MembresiaPremium`, la constante `DESCUENTO = 0.20` es `private static final`, ocultando el detalle de implementacion.

---

### Evidencia de Ejecucion en Consola

#### Caso 1 - Membresia Premium, peliculas 1 y 3 (caso del README)

**Entrada:**
```
2
1,3
```

**Salida:**
```
=================================
    VIDEOCLUB DE DON MARIO
=================================

=== CATALOGO DE PELICULAS ===
  1. [Fisica] Interestellar - $8.000 - Disponible
  2. [Fisica] El Padrino - $7.000 - No disponible
  3. [Digital] Inception - $5.000 - Disponible
  4. [Digital] Matrix - $6.000 - Disponible
=============================

Tipo de membresia:
  1. Basica  (precio normal)
  2. Premium (20% de descuento)
Seleccione membresia (1/2):
Seleccione peliculas (numeros separados por coma):
--- RECIBO DE ALQUILER ---
Cliente: Premium
Peliculas:
  - Interestellar (Fisica) - $8.000
  - Inception (Digital) - $5.000
Subtotal: $13.000
Descuento (20%): $2.600
Total a pagar: $10.400
--------------------------
¡Disfrute su pelicula!
```

#### Caso 2 - Membresia Basica, pelicula no disponible + disponible

**Entrada:**
```
1
2,4
```

**Salida:**
```
=================================
    VIDEOCLUB DE DON MARIO
=================================

=== CATALOGO DE PELICULAS ===
  1. [Fisica] Interestellar - $8.000 - Disponible
  2. [Fisica] El Padrino - $7.000 - No disponible
  3. [Digital] Inception - $5.000 - Disponible
  4. [Digital] Matrix - $6.000 - Disponible
=============================

Tipo de membresia:
  1. Basica  (precio normal)
  2. Premium (20% de descuento)
Seleccione membresia (1/2):
Seleccione peliculas (numeros separados por coma):   [!] 'El Padrino' no esta disponible y no se incluye.

--- RECIBO DE ALQUILER ---
Cliente: Basica
Peliculas:
  - Matrix (Digital) - $6.000
Subtotal: $6.000
Total a pagar: $6.000
--------------------------
¡Disfrute su pelicula!
```

---

### Como compilar y ejecutar

```bash
cd Ejercicio1
javac *.java
java Main
```
