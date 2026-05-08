Ejercicio 1
IA:Claude

PROMT:En el readme hay un ejercicio el cual se llama Problema#1: El videoclub
de don mario en el readme se explica cual es la mision, las peliculas
disponibles y los casos de ejemplo, desarrolla el ejercicio en la
carpeta de Ejercicio1 identifica los patrones de diseño necesarios para
desarrollar el ejercicio, explica los principios solid que se ultilizan,
Aplica polimorfismo y encapsulamiento y coloca una evidencia de la
ejecucion del ejercicio(solo la ejecucion en consola)


Ejercicio 2
IA:Claude

PROMT:Analiza el readme del proyecto y tengo que realizar el problema#2 Llamado
  Tienda Virtual usa la pista de los patrones y en el SOLUCION.md agrega el
  apartado de ejercicio 2 y Pon La identificacion de los patrones, lo que hiciste
  para completar la implementacion, revisa el diagrama existente y en el
  SOLUCION.md pon si es necesario hacerle cambios, escribe los errores que
  identificaste, Corrige el codigo y por ultimo realiza y ejecuta unas pruebas
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

---

## Problema #2: Tienda Virtual

---

### Patrones de Diseño Identificados

#### 1. Abstract Factory (Creacional)

**Donde:** la interfaz `PaymentFactory` (faltaba en el código original) con las implementaciones concretas `CreditCardPaymentFactory`, `PaypalPaymentFactory` y `CryptoPaymentFactory`.

**Por qué:** el enunciado pide "crear familias de objetos relacionados (pago + validador)". Cada método de pago lleva su propio proceso de validación (`validatePaymentMethod`) y ejecución (`processPayment`). El Abstract Factory desacopla la creación de estas familias de la lógica principal de compras (`ECIPayment`), que solo conoce `PaymentFactory` y `PaymentMethod`, sin depender de ninguna clase concreta.

```
<<interface>>
PaymentFactory
  + createPaymentMethod(amount, customerId, desc): PaymentMethod
            ^
            |
CreditCardPaymentFactory   PaypalPaymentFactory   CryptoPaymentFactory
  (crea CreditCardFactory)  (crea PaypalFactory)   (crea CryptoFactory)
```

#### 2. Observer (Comportamental)

**Donde:** interfaz `PaymentObserver`, clase subject `ECIPayment` y observer concreto `PaymentEventObserver`.

**Por qué:** el enunciado pide "notificar automáticamente a otros componentes cuando se procesa un pago exitoso". Observer permite que módulos (Inventario, Facturación, Notificaciones) reaccionen a eventos de pago sin que `ECIPayment` conozca nada de ellos. Agregar un nuevo módulo sólo requiere implementar `PaymentObserver` y registrarlo con `addObserver`.

```
<<interface>>
PaymentObserver
  + onPaymentSuccess(payment, name, email, productId)
  + onPaymentFailed(payment, email)
          ^
          |
PaymentEventObserver
  → Inventory.discountProduct(...)
  → Facturation.generateInvoice(...)
  → Notification.sendConfirmationEmail(...)
```

---

### Clases/Interfaces que Faltaban para Completar los Patrones

| Clase/Interface | Rol | Por qué faltaba |
|---|---|---|
| `PaymentFactory` | Interfaz del Abstract Factory | `ECIPayment` la referenciaba pero no existía en el proyecto |
| `CreditCardPaymentFactory` | Fábrica concreta para tarjeta | Faltaba la capa fábrica separada del producto |
| `PaypalPaymentFactory` | Fábrica concreta para PayPal | Idem anterior |
| `CryptoPaymentFactory` | Fábrica concreta para Cripto | Idem anterior |

Las clases `CreditCardFactory`, `PaypalFactory` y `CryptoFactory` son los **productos** del patrón (extienden `PaymentMethod`). Estaban mal nombradas como "Factory" cuando en realidad son los objetos que la fábrica crea.

---

### Análisis del Diagrama

#### Diagrama de clases (`docs/uml/clases.png`)

El diagrama muestra correctamente la jerarquía de `PaymentMethod`, los tres productos (`CreditCardFactory`, `PaypalFactory`, `CryptoFactory`) y los módulos observadores (`Inventory`, `Facturation`, `Notification`). Sin embargo, **es necesario hacerle cambios**:

1. **La interfaz `PaymentFactory` no aparece en el diagrama** a pesar de ser el corazón del patrón Abstract Factory. Debe agregarse con las flechas de implementación hacia las tres factories concretas.
2. **Las clases `CreditCardPaymentFactory`, `PaypalPaymentFactory` y `CryptoPaymentFactory` no aparecen** porque no existían. Deben añadirse como implementaciones de `PaymentFactory`.
3. Las clases `CreditCardFactory`, `PaypalFactory`, `CryptoFactory` deberían renombrarse en el diagrama a `CreditCardPayment`, `PaypalPayment`, `CryptoPayment` para reflejar que son **productos**, no fábricas.

#### Diagrama de contexto (`docs/imagenes/contexto.png`)

El diagrama de contexto es **claro y suficiente** para entender el flujo de negocio: el cliente usa el sistema de pago, y al procesar el pago se activan los módulos de Notificación, Facturación e Inventario. No requiere cambios de contenido, aunque sería más preciso distinguir entre el **mecanismo Observer** (`ECIPayment`) y el **módulo de notificaciones de email** (`Notification`), ya que el diagrama los fusiona bajo el mismo nombre.

---

### Errores Identificados en el Código Original

| # | Archivo | Error | Descripción |
|---|---|---|---|
| 1 | `ECIPayment.java` | `PaymentFactory` no existe | El método `processPayment` recibe un parámetro de tipo `PaymentFactory` pero la interfaz no estaba definida en el proyecto → el código no compilaba. |
| 2 | `PaymentEventObserver.java` | Import incorrecto | `import javax.management.Notification` importa la clase de la JMX API en lugar de `Notification` del propio proyecto. Todos los llamados a `notification.sendConfirmationEmail(...)` fallaban en compilación. |
| 3 | `PaymentMethod.java` | Asignación `customerID` a sí mismo | El constructor tenía el parámetro nombrado `transactionID` (confundiendo campo con parámetro), y la línea `this.customerID = customerID` asignaba el campo a sí mismo en lugar de asignar el parámetro recibido. El `customerID` siempre quedaba `null`. |
| 4 | `CryptoFactory.java` | `this.token = token` sin parámetro | La línea `this.token = token` usaba la variable de campo no inicializada (null) porque `token` no era un parámetro del constructor. Asignación sin efecto útil. |
| 5 | `CreditCardFactory.java` | `address` nunca asignado | El constructor recibe `address` como parámetro pero nunca lo asigna al campo `this.address`. El campo quedaba siempre `null`. |
| 6 | Naming (`*Factory.java`) | Mezcla de responsabilidades | `CreditCardFactory`, `PaypalFactory` y `CryptoFactory` se llaman "Factory" pero extienden `PaymentMethod`: son los productos, no las fábricas. Las fábricas concretas que implementan `PaymentFactory` no existían. |

---

### Correcciones Realizadas

1. **Creada** `PaymentFactory.java` — interfaz con `createPaymentMethod(double, String, String)`.
2. **Creadas** `CreditCardPaymentFactory.java`, `PaypalPaymentFactory.java`, `CryptoPaymentFactory.java` — concretan el patrón Abstract Factory; cada una almacena los datos específicos del método de pago y delega la creación al constructor del producto correspondiente.
3. **Corregido** `PaymentMethod.java` — parámetro renombrado de `transactionID` a `customerId`; `this.customerID = customerId` ahora asigna correctamente.
4. **Corregido** `PaymentEventObserver.java` — eliminado `import javax.management.Notification`; ahora usa la clase del paquete propio.
5. **Corregido** `CryptoFactory.java` — eliminada la línea `this.token = token` que asignaba `null` a sí mismo.
6. **Corregido** `CreditCardFactory.java` — añadida `this.address = address` en el constructor.

---

### Evidencia de Ejecución de Pruebas

```
[INFO] --- compiler:3.14.1:compile (default-compile) @ ejercicio-paper ---
[INFO] --- compiler:3.14.1:testCompile (default-testCompile) @ ejercicio-paper ---
[INFO] --- surefire:3.1.2:test (default-test) @ ejercicio-paper ---
[INFO] Tests run: 1, Failures: 0, Errors: 0, Skipped: 0  -- ApplicationTest
[INFO] Tests run: 36, Failures: 0, Errors: 0, Skipped: 0 -- auxiliaryTest
[INFO] Tests run: 37, Failures: 0, Errors: 0, Skipped: 0
[INFO] BUILD SUCCESS
```

**Total: 37 tests, 0 fallos, 0 errores.**

#### Cobertura de tests (`auxiliaryTest.java`)

| Clase cubierta | Tests que la ejercitan |
|---|---|
| `CreditCardPaymentFactory` | Factory creates payment, validation pass/fail (CVV, expiry, card length) |
| `PaypalPaymentFactory` | Factory creates payment, validation pass/fail (email, token) |
| `CryptoPaymentFactory` | Factory creates payment, validation pass/fail (balance, wallet length) |
| `CreditCardFactory` | maskCardNumber, getCardHolder, getCardType |
| `PaypalFactory` | getEmail, getPaypalTransactionId |
| `CryptoFactory` | getWalletAddress, getCryptoType, getBlockchainHash |
| `ECIPayment` | addObserver, removeObserver |
| `PaymentEventObserver` | onPaymentSuccess (producto conocido y desconocido), onPaymentFailed |
| `Inventory` | getProduct, discountProduct, getStock (casos happy path y error) |
| `Product` | Todos los getters |
| `Facturation` | calculateTax, calculateTotal, getters/setters |
| `Notification` | getters, sendConfirmationEmail, sendFailureNotification |
| `PaymentStatus` | Verificación de los 5 valores del enum |
| `PaymentMethod` | getAmount, setAmount, getStatus, setStatus, getTransactionId, getTimestamp, getDescription, getCustomerId |
