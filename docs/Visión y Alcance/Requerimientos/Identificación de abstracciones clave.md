## **Identificación de abstracciones clave (entidades de negocio)**

El cliente pide la decoración y el negocio se encarga de todo lo necesario para ofrecer el servicio y finalizarlo.   
El cliente se pone en contacto con Decoraciones Colibrí para solicitar una decoración.  
El cliente da la fecha y pide la cotización de la decoración que desea.  
Decoraciones Colibrí revisa en una libreta si la fecha está disponible. Si no lo está, termina el proceso.  
Si la fecha está disponible, Decoraciones Colibrí realiza el proceso que conlleva obtener la cotización.  
Si el cliente considera que la cotización excede su presupuesto, puede solicitar una nueva cotización con una propuesta de decoración ajustada. El proceso de cotización se repite hasta que el cliente acepte una propuesta o decida no contratar el servicio.  
Si el cliente está de acuerdo con la cotización, Decoraciones Colibrí realiza el contrato.  
El cliente firma el contrato, donde se menciona que el anticipo del evento puede ir desde el 25% hasta el 100% del costo total.  
El cliente realiza el pago del anticipo.  
Se revisa en la libreta que tiene el material existente, si hace falta material para la decoración, se lleva a cabo un proceso para comprar el material faltante, de acuerdo al proveedor que corresponda.  
Si no se ha liquidado el costo de la decoración tres días antes del evento, el cliente liquida la decoración.  
Si la decoración es muy grande, un día antes del evento se adelanta una parte.  
El día del evento, se monta la decoración en el lugar acordado.  
En caso de que se haya utilizado mobiliario, se lleva a cabo el proceso para retirarlo.  
El negocio realiza todo lo que lleva hacer una cotización.  
Decoraciones Colibrí solicita información sobre el lugar del evento.  
El cliente envía imágenes y detalles.  
Decoraciones Colibrí lleva a cabo el proceso para obtener una lista de precios con todo lo necesario para la decoración.  
Decoraciones Colibrí calcula todos los costos extra que se necesitarán; como el transporte, renta de mobiliarios o cualquier material necesario para la decoración.  
Decoraciones Colibrí genera la cotización completa.  
El cliente recibe la cotización hecha por Decoraciones Colibrí.  
Se lleva a cabo el proceso de contacto con los proveedores para solicitar y obtener información sobre los precios de los materiales y servicios requeridos.  
Decoraciones Colibrí hace una lista de los materiales cuyo precio conoce.  
Si se utilizarán materiales de los cuales se desconoce el precio, entonces Decoraciones Colibrí realiza una lista con estos materiales.  
Se manda una lista al proveedor correspondiente de los materiales que desconoce su precio.  
El proveedor, mediante su propio proceso, envía una lista con los materiales y sus precios correspondientes.  
Decoraciones Colibrí recibe la lista de precios.  
Se evalúa el estado del mobiliario y se llevan a cabo acciones de cobro en caso de ser necesarias.  
Decoraciones Colibrí revisa el estado en el que se encuentra el mobiliario. Si está dañado se le notifica al cliente.  
Si el daño al mobiliario es parcial, entonces Decoraciones Colibrí cotiza cuánto costará la reparación.  
Si el daño al mobiliario es total, entonces Decoraciones Colibrí calcula cuánto es el valor del mobiliario perdido.  
Se le informa al cliente cuánto es la cantidad que deberá pagar por el mal uso del mobiliario.  
El cliente paga el costo del daño hecho al mobiliario.  
*\[Identificar en los documentos que se tienen (Visión, Glosario, Historias de usuario) nombres que puedan ser candidatos a abstracciones de entidades del dominio del problema. Una vez identificadas proceder a su eliminación considerando los criterios siguientes:*

* *El candidato pertenece al dominio de la solución*  
* *El candidato es un atributo de otra abstracción*  
* *El candidato es igual a otra de las abstracciones pero con otro nombre*  
* *El candidato no tiene atributos*  
* *Sólo existe una instancia de la entidad*  
* *La entidad no es información que deba persistir*

*\]*

| Candidato a entidad | Razón de su eliminación | Nombre final para entidad |
| :---- | :---- | :---- |
| Decoraciones Colibrí | No tiene atributos |  |
| Cliente  |  | Cliente |
| Decoraciones  | Atributo de Evento “tipoDecoración” |  |
| Negocio  | No tiene atributos |  |
| Servicio | Sinónimo de Decoración |  |
| Fecha del evento | Atributo de Evento. “fecha” |  |
| Cotización  |  | Cotizacion |
| Libreta | No tiene atributos |  |
| Proceso | No tiene atributos |  |
| Presupuesto | No tiene atributos |  |
| Propuesta | No tiene atributos |  |
| Contrato | Sólo hay una instancia |  |
| Anticipo del Evento  | Atributo de Cotización. “anticipo” |  |
| Costo total | Atributo de Cotización “total” |  |
| Evento |  | Evento |
| Pago del Anticipo | Atributo de Evento “abono” |  |
| Material Existente  |  | Material |
| Material Faltante  | Atributo de Material. “existencia” |  |
| Proveedor | No tiene atributos |  |
| Dia antes del evento | No tiene atributos |  |
| Lugar acordado | Atributo de Evento. “lugar” |  |
| Mobiliario  |  | Subclase de Material. “Mobiliario” |
| Información lugar evento  | Sinónimo lugar acordado |  |
| Imagenes  | No debe persistir |  |
| Detalles | Atributo de Evento. “detalles” |  |
| Lista de precios  | Es atributo “precio” de los materiales |  |
| Costo extra | Atributo de Cotización. “extra” |  |
| Transporte  | Atributo de Cotización “transporte” |  |
| Renta de mobiliario | Ya incluido en Cotización |  |
| Contacto con proveedores | Parte de la solución |  |
| Precios (materiales pedidos al proveedor) | Sinónimo de lista de precios |  |
| Materiales cuyo precio conoce | Contenido en Materiales |  |
| Lista cuyo precios desconoce | No debe persistir |  |
| Lista de materiales y precios correspondientes |  | ListaMaterial |
| Estado del mobiliario | Atributo de Mobiliario “Estado” |  |
| Cobro en caso de ser necesario (parcial) | Atributo de Mobiliario “costoParcial” |  |
| Cobro en caso de ser necesario (total) | Atributo de Mobiliario “costoTotal” |  |
| Costo reparación | Sinónimo de “Cobro en caso de ser necesario” |  |
| Daño parcial | Atributo de Mobiliario “tipoDaño” |  |
| Daño total | Contenido en Daño Parcial |  |
| Valor del mobiliario perdido  | Sinónimo de CostoParcial/CostoTotal |  |
| Cantidad que deberá pagar | Sinónimo de costos |  |
| Costo del daño | Sinónimo de costos |  |
| Estado del evento | Atributo de evento. “estadoEvento” |  |
| Globos |  | Implementación de Materiales. “Globo” |
| Nombre del cliente | Atributo del Cliente “nombre” |  |
| Telefono del cliente  | Atributo del Cliente “numTelefono” |  |
| Comestible |  | Implementación de Materiales. “Comestible” |
| Color | Atributo de Globo. “color” |  |
| Medida | Atributo de Globo. “medida” |  |
| Tipo de globo | Atributo de globo. “tipoGlobo” |  |
| Tipo de comestible | Atributo de comestible. “tipoComestible” |  |
| Cantidad de materiales en inventario | Atributo de material. “cantInventario” |  |
| Pagos |  | Pago |
| Material decorativo |  | MaterilDercorativo |

Entidades:

* Cliente  
* Cotización  
* Evento  
* Material  
* Mobiliario  
* ListaMaterial  
* Pago  
* Globo  
* MaterialDecorativo  
* Comestible