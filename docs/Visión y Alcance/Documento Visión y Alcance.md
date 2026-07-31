# **Documento de Visión y Alcance** {#documento-de-visión-y-alcance}

[**Documento de Visión y Alcance	1**](#documento-de-visión-y-alcance)

[1\. Introducción	1](#1.-introducción)

[2\. Contexto de negocio	1](#2.-contexto-de-negocio)

[Antecedentes y problemática	1](#antecedentes-y-problemática)

[Necesidades	1](#necesidades)

[Procesos clave del negocio actuales	1](#procesos-clave-del-negocio-actuales)

[3\. Visión de la solución	2](#3.-visión-de-la-solución)

[Frase de visión	2](#frase-de-visión)

[Procesos clave del negocio futuros	2](#procesos-clave-del-negocio-futuros)

[Funcionalidades de alto nivel del sistema (épicas)	3](#funcionalidades-de-alto-nivel-del-sistema-\(épicas\))

[4\. Alcance del proyecto	3](#4.-alcance-del-proyecto)

[Alcance	3](#alcance)

[5\. Contexto del sistema	3](#heading=h.andluz277ib3)

[Resumen de Involucrados	4](#resumen-de-involucrados)

[Entorno de operación	4](#entorno-de-operación)

## **1\. Introducción** {#1.-introducción}

El presente documento describe la visión y el alcance previsto para la creación del software de la aplicación *ColiWeb*.

*ColiWeb* es un software cuyo propósito es fungir de apoyo para el negocio Decoraciones Colibrí, dedicado a ofrecer servicios de decoraciones, principalmente, con globos.

En el documento se presentan diversos procesos que lleva a cabo Decoraciones Colibrí con el fin de analizarlos para identificar las mayores problemáticas y necesidades del negocio, así como se presentan algunas soluciones que permitan al mismo llegar a sus objetivos como negocio.

## **2\. Contexto de negocio** {#2.-contexto-de-negocio}

### **Antecedentes y problemática** {#antecedentes-y-problemática}

Decoraciones Colibrí es un negocio dedicado al arreglo de globos para cualquier tipo de eventos como:

* Eventos sociales  
* Cumpleaños  
* Fiestas infantiles  
* XV años  
* Eventos escolares

Su servicio se puede ofrecer en el Estado de México, Hidalgo y Puebla.  
Como parte de los objetivos del negocio, éste busca:

* Reducir el tiempo de respuesta a cotizaciones de eventos y facilitar su elaboración.  
* Facilitar la consulta de precios de materiales en almacén y/o faltantes para la decoración.  
* Llevar un manejo más eficiente de los eventos confirmados.  
* Mejorar la actividad en redes sociales.   
* Permitir de manera más efectiva la recomendación de clientes satisfechos.  
* Mejorar los lineamientos del contrato en caso de daño al mobiliario.   
* Facilitar y mejorar la consulta del mobiliario que se utiliza en cada evento. 

**Problemáticas**

Actualmente, el proceso de cotización de eventos es un proceso que puede tardar días debido a la inexactitud de las cosas existentes en el inventario. Hay clientes que quieren una respuesta en 5 a 15 minutos, como consecuencia, terminan optando por otras opciones generando para el negocio la pérdida de un cliente.

Dada la urgencia de resolver el problema anteriormente mencionado, se requiere optimizar la consulta de precios e inventario para poder cotizar un evento.

### **Necesidades** {#necesidades}

| ID | Descripción de la necesidad |
| :---- | :---- |
| NEC-1 | Facilitar la consulta del inventario existente. |
| NEC-2  | Proporcionar un mejor manejo de los eventos solicitados y confirmados de manera más eficiente. |
| NEC-3 | Facilitar la consulta de los costos de la decoración. |
| NEC-4 | Mejorar la actividad en redes sociales. |
| NEC-5 | Facilitar la gestión de contratos personalizados para cada evento  Implementar un nuevo contrato para mejorar los lineamientos y condiciones en caso de daño al mobiliario.  |
| NEC-6 | Mejorar el control del mobiliario involucrado en las decoraciones.  |

### **Procesos clave del negocio actuales** {#procesos-clave-del-negocio-actuales}

Los procesos principales del negocio son los siguientes:

| ID Proceso | Nombre del proceso | Descripción y pasos (se puede incluir una liga a un diagrama BPMN) |
| :---- | :---- | :---- |
| PROCES-1 | Contratación de servicio | El cliente pide la decoración y el negocio se encarga de todo lo necesario para ofrecer el servicio y finalizarlo.  El cliente se pone en contacto con Decoraciones Colibrí para solicitar una decoración. El cliente da la fecha y pide la cotización de la decoración que desea. Decoraciones Colibrí revisa en una libreta si la fecha está disponible. Si no lo está, termina el proceso. Si la fecha está disponible, Decoraciones Colibrí realiza el proceso que conlleva obtener la cotización. Si el cliente considera que la cotización excede su presupuesto, puede solicitar una nueva cotización con una propuesta de decoración ajustada. El proceso de cotización se repite hasta que el cliente acepte una propuesta o decida no contratar el servicio. Si el cliente está de acuerdo con la cotización, Decoraciones Colibrí realiza el contrato. El cliente firma el contrato, donde se menciona que el anticipo del evento puede ir desde el 25% hasta el 100% del costo total. El cliente realiza el pago del anticipo. Se revisa en la libreta que tiene el material existente, si hace falta material para la decoración, se lleva a cabo un proceso para comprar el material faltante, de acuerdo al proveedor que corresponda. Si no se ha liquidado el costo de la decoración tres días antes del evento, el cliente liquida la decoración. Si la decoración es muy grande, un día antes del evento se adelanta una parte. El día del evento, se monta la decoración en el lugar acordado. En caso de que se haya utilizado mobiliario, se lleva a cabo el proceso para retirarlo. [Documento del proceso.](https://docs.google.com/document/d/1JbR4fXU-C53q6k1lSe5yGdFfxn5k8zDqOAIDOpDo5p0/edit?usp=sharing) |
| PROCES-2  | Cotización de decoración | El negocio realiza todo lo que lleva hacer una cotización. Decoraciones Colibrí solicita información sobre el lugar del evento. El cliente envía imágenes y detalles. Decoraciones Colibrí lleva a cabo el proceso para obtener una lista de precios con todo lo necesario para la decoración. Decoraciones Colibrí calcula todos los costos extra que se necesitarán; como el transporte, renta de mobiliarios o cualquier material necesario para la decoración. Decoraciones Colibrí genera la cotización completa. El cliente recibe la cotización hecha por Decoraciones Colibrí. [Documento del proceso.](https://docs.google.com/document/d/1p0o15aCjncyQsTSkWGQ7BT6lZP-tOp1vKjxvTy4LNtA/edit?usp=sharing) |
| PROCES-3 | Consulta de precios con proveedor | Se lleva a cabo el proceso de contacto con los proveedores para solicitar y obtener información sobre los precios de los materiales y servicios requeridos. Decoraciones Colibrí hace una lista de los materiales cuyo precio conoce. Si se utilizarán materiales de los cuales se desconoce el precio, entonces Decoraciones Colibrí realiza una lista con estos materiales. Se manda una lista al proveedor correspondiente de los materiales que desconoce su precio. El proveedor, mediante su propio proceso, envía una lista con los materiales y sus precios correspondientes. Decoraciones Colibrí recibe la lista de precios. [Documento del proceso.](https://docs.google.com/document/d/1RewEnQO89wi6x6FsgmYo3fB38xml0r5PzmWFC7tqYcI/edit?usp=sharing) |
| PROCES-4 | Recolección de mobiliario | Se evalúa el estado del mobiliario y se llevan a cabo acciones de cobro en caso de ser necesarias. Decoraciones Colibrí revisa el estado en el que se encuentra el mobiliario. Si está dañado se le notifica al cliente. Si el daño al mobiliario es parcial, entonces Decoraciones Colibrí cotiza cuánto costará la reparación. Si el daño al mobiliario es total, entonces Decoraciones Colibrí calcula cuánto es el valor del mobiliario perdido. Se le informa al cliente cuánto es la cantidad que deberá pagar por el mal uso del mobiliario. El cliente paga el costo del daño hecho al mobiliario. [Documento del proceso.](https://docs.google.com/document/d/1rI8mVEtAifGpev3hXI221iMGLDdCUyM4QIN88-AEfZk/edit?usp=sharing) |

## **3\. Visión de la solución** {#3.-visión-de-la-solución}

### **Frase de visión** {#frase-de-visión}

*ColiWeb* consiste en una aplicación web diseñada para apoyar la gestión operativa de Decoraciones Colibrí. Su propósito es optimizar la administración de la agenda de eventos confirmados, el control de inventario y la elaboración de cotizaciones.

*ColiWeb* permitirá generar listas de materiales faltantes para la elaboración de una decoración, determinar los costos asociados a los materiales y servicios requeridos, facilitar el proceso de contratación mediante la generación de documentos que especifiquen detalladamente el mobiliario rentado al cliente.

### **Procesos clave del negocio futuros** {#procesos-clave-del-negocio-futuros}

Los procesos principales del negocio son los siguientes:

| ID Proceso | Nombre del proceso | Descripción y pasos (se puede incluir una liga a un diagrama BPMN) |
| :---- | :---- | :---- |
| PROCES-1 | Contratación de servicio | El cliente pide la decoración y el empleado realiza todo el proceso necesario para atenderlo con ayuda de ColiWeb El cliente se pone en contacto con Decoraciones Colibrí para solicitar una decoración. El cliente da la fecha y pide la cotización de la decoración que desea. El empleado revisa en la agenda de ColiWeb si tiene la fecha disponible. Si la fecha no está disponible, se notifica al cliente y termina el proceso. Si la fecha está disponible, el empleado selecciona la fecha y comienza el proceso ***‘Cotización de Decoración’***. El empleado envía la cotización obtenida al cliente. Si el cliente necesita tiempo para pensar la cotización, el empleado guarda como borrador la cotización hecha. Si el cliente no acepta la cotización recibida, puede proponer cambios solicitando una nueva cotización. Si el cliente acepta la cotización, el empleado manda a ColiWeb hacer el contrato personalizado en base a la cotización hecha previamente. El cliente firma el contrato, donde se menciona el anticipo del evento que puede ir desde el 25% hasta el 100% del pago total del evento. El cliente realiza el pago del anticipo y el empleado registra la cantidad pagada en ColiWeb. El empleado confirma el borrador que contiene la cotización y el día del evento, para convertirlo en evento confirmado en ColiWeb. El empleado revisa en ColiWeb si hay inventario suficiente para el evento, en caso de que no, toma la lista de materiales faltantes con la cantidad proporcionada por Coliweb para enviarla al proveedor y llevar el proceso necesario para comprarlo. En caso de que haya faltado material en inventario, una vez comprado, se registra en el inventario de ColiWeb Si no se ha liquidado el costo de la decoración, tres días antes del evento, el cliente liquida la decoración y el empleado lo marca como pagado en ColiWeb. Si el evento es muy grande, un día antes del evento se realiza una parte de la decoración. El día del evento, se monta la decoración en el lugar del evento. En caso de que se haya rentado mobiliario, el empleado lleva a cabo el proceso ***‘Recolección de mobiliario’***. El empleado marca el evento como terminado y Coliweb disminuye las cantidades existentes en almacén en ColiWeb |
| PROCES-2 | Cotización de decoración | El empleado realiza todo lo que lleva hacer una cotización para enviársela al cliente. Decoraciones Colibrí solicita información extra sobre el lugar del evento. El cliente envía imágenes y detalles sobre la decoración y el espacio donde se colocará. El empleado, aún en ColiWeb y con la fecha seleccionada, revisa el catálogo de materiales y selecciona aquellos que necesitará con su cantidad. En caso de que hayan faltado materiales, el empleado lleva a cabo el proceso ***‘Consulta de precios con proveedor’*** Ya con los precios completos, el empleado ingresa los costos extras de la decoración como transporte y/o material adicional. El empleado finaliza la cotización y recibe las opciones de exportarla para enviarla. |
| PROCES-3 | Consulta de precios con proveedor | Se lleva a cabo el proceso de contacto con los proveedores para solicitar y obtener información sobre precios faltantes para registrarlos en ColiWeb. Tras seleccionar los materiales que ya existen en el catálogo, el empleado agrega materiales que no estén registrados. El empleado extrae la lista de materiales con precio desconocido y la envía al proveedor. Coliweb permite extraer una lista de los materiales que se desconoce su precio. Si se necesita esperar un tiempo a la respuesta del proveedor, el empleado guarda la cotización como un borrador dejando los precios como “Pendiente”. El proveedor mediante su proceso, saca una lista con el presupuesto y la envía al empleado. El empleado llena los precios pendientes con la lista en el borrador de la cotización, permitiendo continuar con la decoración. |
| PROCES-4 | Recolección de mobiliario | Se evalúa el estado del mobiliario y se llevan a cabo acciones de cobro en caso de ser necesarias, ColiWeb sirve de apoyo para conocer el costo de los daños. El empleado revisa el estado en el que se encuentra el mobiliario rentado. Si no fue dañado, procediendo al paso 5\. Si existe daño en el mobiliario, el empleado notifica al cliente y, con ayuda del catálogo del evento, se registra el tipo de daño, y queda como no disponible hasta su reparación o reposición. Coliweb devuelve el costo de indemnización. Se le informa al cliente el costo que deberá pagar por el mal uso del mobiliario. El cliente paga el costo del daño hecho al mobiliario. El empleado recolecta el mobiliario. |

### **Funcionalidades de alto nivel del sistema (épicas)** {#funcionalidades-de-alto-nivel-del-sistema-(épicas)}

| ID | Descripción de la funcionalidad | Prioridad | Proceso de negocio asociado |
| :---- | :---- | ----- | :---- |
| FUN-1 | El sistema debe permitir a Decoraciones Colibrí consultar qué materiales tienen existencia en el inventario para saber cuánto se debe comprar para la decoración. | Alta | PROCES-1, PROCES-2 |
| FUN-2 | El sistema, debe permitir a Decoraciones Colibrí ver e imprimir un contrato el cual contenga el mobiliario utilizado en la decoración junto a la indemnización que se requiere en caso de dañarlos (si aplica). | Media | PROCES-1 |
| FUN-3 | El sistema debe dar a Decoraciones Colibrí el costo total de la cotización tras seleccionar todos los materiales faltantes con sus respectivos precios y cantidades para enviarlo directamente al cliente. | Alta | PROCES-2 |
| FUN-4 | El sistema debe devolver una lista con los materiales de los que se desconoce el precio para enviarse directamente al proveedor. | Alta | PROCES-3 |
| FUN-5 | El sistema debe ofrecer una manera óptima de ver los eventos que ya existen así como de crear nuevos para que queden registrados. Esto a su vez debe notificar cuando la fecha esté próxima para evitar que se olviden de algún evento. | Alta  | PROCES-1 |
| FUN-6 | El sistema debe ofrecer un apartado para subir imágenes sobre cómo quedó una decoración para poder subirlo a redes sociales automáticamente. | Baja |  |
| FUN-7 | El sistema debe permitir a Decoraciones Colibrí informar que un mobiliario fue dañado y en qué gravedad para saber de cuánto es la indemnización necesaria y dictada por el contrato. | Baja | PROCES-4 |
| FUN-8 | El sistema debe permitir administrar el catálogo de materiales para dar de alta materiales, aumentar su existencia, consultar y actualizar sus precios. | Alta | PROCES-1, PROCES-2, PROCES-3 |
| FUN-9  | El sistema debe permitir guardar borradores de eventos, confirmarlos y marcar como terminados para llevar un control de eventos y eventos posibles. | Alta | PROCES-1, PROCES-3 |
| FUN-10 | El sistema debe permitir registrar pagos parciales y liquidaciones asociadas a cada evento para llevar un control de los pagos. | Media | PROCES-1, PROCES-4 |

## **4\. Alcance del proyecto** {#4.-alcance-del-proyecto}

### **Alcance** {#alcance}

| Número de entrega | Tema principal | ID de épicas a incluir |
| :---- | :---- | ----- |
| Entrega 1.0 | Consulta de materiales disponibles en inventario.  | FUN-1 |
| Entrega 1.0 | Permite ingresar nuevos materiales | FUN-8 |
| Entrega 1.0 | Obtiene lista de materiales faltantes. | FUN-4  |
| Entrega 2.0 | Obtiene cotizaciones de eventos, incluyendo materiales, mobiliario, gastos extras y materiales extras. | FUN-3 |
| Entrega 3.0 | Agenda y consulta de fechas de disponibles y eventos confirmados.  | FUN-5 |
| Entrega 3.0 | Crear borradores de eventos, confirmarlos y marcarlos como confirmados | FUN-9 |
| Entrega 4.0 | Personaliza contratos. | FUN-2 |
| Entrega 4.0 | Permite marcar pagos. | FUN-10 |
| Entrega 5.0 | Calcula el costo de los daños al mobiliario. | FUN-7 |
| Entrega 6.0 | Facilita la publicidad en redes sociales | FUN-6 |

##  **5\. Contexto del sistema**

### **Resumen de Involucrados** {#resumen-de-involucrados}

| Nombre | Descripción | Responsabilidades   |
| :---- | :---- | :---- |
| Rosalba Martínez Sáenz | Responsable de decoración y del material utilizado en los eventos. | \-Supervisión y verificación de mobiliario \-Decoracion de mobiliario \-Decoración del lugar del evento \-Control de materiales e inventario \-Elaboración de cotizaciones. |
| Edilia Pérez Romero  | Responsable de la atención a clientes, cotizaciones y redes sociales. | \-Responsable de manejo de contrato con los clientes \-Manejo de redes sociales \-Decoración del lugar del evento \-Atención por WhatsApp. \-Cotización de insumos. \-Gestión de apartados y anticipos. \-Administración de agenda de eventos. |

### 

### **Entorno de operación** {#entorno-de-operación}

El sistema será utilizado en cualquier dispositivo de escritorio capaz de correr programas en Java. La interfaz será sencilla y tendrá el fin de ser intuitiva y progresiva según las necesidades y los procesos del negocio.

El sistema se ejecutará en un servidor local en la computadora sobre la que esté la aplicación.

**Información adicional**

El uso del sistema es exclusivo para personas que participen como parte del negocio en alguno de los procesos mencionados en este documento.

Junto con el sistema se entregará:

* Manual de usuario  
* Código fuente