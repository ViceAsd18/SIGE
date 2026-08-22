package cl.sige.plataforma.ms_evaluaciones_notas;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@ConfigurationPropertiesScan
@EnableFeignClients
public class MsEvaluacionesNotasApplication {

	public static void main(String[] args) {
		SpringApplication.run(MsEvaluacionesNotasApplication.class, args);
	}

}
