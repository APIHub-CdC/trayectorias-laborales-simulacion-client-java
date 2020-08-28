package io.tl.simulacion.mx.client.api;

import io.tl.simulacion.mx.client.ApiClient;
import io.tl.simulacion.mx.client.ApiException;
import io.tl.simulacion.mx.client.ApiResponse;
import io.tl.simulacion.mx.client.model.Busqueda;
import io.tl.simulacion.mx.client.model.CatalogoSexoPersona;
import io.tl.simulacion.mx.client.model.DomicilioConsulta;
import io.tl.simulacion.mx.client.model.PersonaConsulta;
import io.tl.simulacion.mx.client.model.Respuesta;
import okhttp3.OkHttpClient;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.junit.Assert;
import org.junit.Before;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;

import org.apache.commons.codec.binary.Base64;

public class ApiTest {
    
	private Logger logger = LoggerFactory.getLogger(ApiTest.class.getName());
	private final ConsultaApi api = new ConsultaApi();
	private ApiClient apiClient = null;  
	private static final String xApiKey = "your_api_key";
	private static final String Authorization = "Authorization";
	private static final String Username = "your-basic-auth-username";
	private static final String Password = "your-basic-auth-password";
	private static final String urlApi = "the_url";
    
	@Before()
	public void setUp() {
		
		byte[] encoding = Base64.encodeBase64((Username + ":" + Password).getBytes());		
		String encodingAuth = "Basic "+(new String(encoding, StandardCharsets.UTF_8));
	
		this.apiClient = api.getApiClient();
		this.apiClient.setBasePath(urlApi);
		OkHttpClient insecureClient = ApiClient.getClientNoSSLVerification();
		OkHttpClient okHttpClient = insecureClient.newBuilder()
				.readTimeout(60, TimeUnit.SECONDS).build();
		apiClient.setHttpClient(okHttpClient);
		apiClient.addDefaultHeader(Authorization, encodingAuth);
	}

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
    
}
