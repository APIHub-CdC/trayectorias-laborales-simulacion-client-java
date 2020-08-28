# trayectorias-laborales-simulacion-client-java

API para consulta de Trayectorias Laborales (Empleos, Cedulas y Listas).

## Requisitos

1. Java >= 1.7
2. Maven >= 3.3

## Instalación

Para la instalación de las dependencias se deberá ejecutar el siguiente comando:

```shell
mvn install -Dmaven.test.skip=true
```

> **NOTA:** Este fragmento del comando *-Dmaven.test.skip=true* evitará que se lance la prueba unitaria.


## Guía de inicio

### Paso 1. Agregar el producto a la aplicación

Al iniciar sesión seguir los siguientes pasos:

 1. Dar clic en la sección "**Mis aplicaciones**".
 2. Seleccionar la aplicación.
 3. Ir a la pestaña de "**Editar '@tuApp**' ".
    <p align="center">
      <img src="https://github.com/APIHub-CdC/imagenes-cdc/blob/master/edit_applications.jpg" width="900">
    </p>
 4. Al abrirse la ventana emergente, seleccionar el producto.
 5. Dar clic en el botón "**Guardar App**":
    <p align="center">
      <img src="https://github.com/APIHub-CdC/imagenes-cdc/blob/master/selected_product.jpg" width="400">
    </p>

### Paso 2. Capturar los datos de la petición

Los siguientes datos a modificar se encuentran en ***src/test/java/io/lae/client/api/ApiTest.java***

Es importante contar con el setUp() que se encargará de inicializar la petición. Por tanto, se debe modificar la URL (**urlApi**); el usuario (**Username**) y contraseña (**Password**) de autenticación de acceso básica; y la API KEY (**xApiKey**), como se muestra en el siguiente fragmento de código:

```java
private static final String xApiKey = "your_api_key";
private static final String Authorization = "Authorization";
private static final String Username = "your-basic-auth-username";
private static final String Password = "your-basic-auth-password";
private static final String urlApi = "the_url";
    
@Before()
public void setUp() {
	//... code
}
```

La petición se deberá modificar el siguiente fragmento de código con los datos correspondientes:

```java
@Test
public void consultarTrayectoriasTest() throws ApiException {
    Busqueda busqueda = new Busqueda();
    PersonaConsulta persona = new PersonaConsulta();
    DomicilioConsulta domicilio = new DomicilioConsulta();
    
    Integer estatusOK = 200;
    Integer estatusNoContent = 204;
    
    try {
    	
        persona.setPrimerNombre("Juan");
        persona.setApellidoPaterno("Pruebauno");
        persona.setApellidoMaterno("Pruebauno");
        persona.setFechaNacimiento("1966-12-07");
        persona.setSexo(CatalogoSexoPersona.M);
        
        domicilio.setDireccion("TORNO 301 EL ROSARIO");
        domicilio.setColonia("PEDREGAL DE SANTO DOMINGO");
        domicilio.setCp("02100");
        
        
        busqueda.setClaveEmpresaConsulta("2007310044");
        busqueda.setFolioConsultaEmpleador("2620100");
        busqueda.setProductoRequerido(new BigDecimal(4));
        busqueda.setPuestoSolicitado("Vendedor");
        busqueda.setPersona(persona);
        busqueda.setDomicilio(domicilio);
        
        ApiResponse<?> response = api.genericConsultarTrayectorias(xApiKey, busqueda);
  
    	Assert.assertTrue(estatusOK.equals(response.getStatusCode()));
    	
    	if(estatusOK.equals(response.getStatusCode())) {
    		Respuesta responseOK = (Respuesta) response.getData();
    		logger.info(responseOK.toString());
    	}
    	
    }catch (ApiException e) {
    	if(!estatusNoContent.equals(e.getCode())) {
    		logger.info(e.getResponseBody());
    	}
    	Assert.assertTrue(estatusOK.equals(e.getCode()));
	}
}
```

### Paso 3. Ejecutar la prueba unitaria

Teniendo los pasos anteriores ya solo falta ejecutar la prueba unitaria, con el siguiente comando:

```shell
mvn test -Dmaven.install.skip=true
```
