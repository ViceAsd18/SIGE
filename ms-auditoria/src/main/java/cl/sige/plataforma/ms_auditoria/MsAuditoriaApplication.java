package cl.sige.plataforma.ms_auditoria;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan 
public class MsAuditoriaApplication {

	public static void main(String[] args) {
		SpringApplication.run(MsAuditoriaApplication.class, args);
	}

}
