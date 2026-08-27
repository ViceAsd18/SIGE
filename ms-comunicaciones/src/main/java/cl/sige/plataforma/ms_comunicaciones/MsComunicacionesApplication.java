package cl.sige.plataforma.ms_comunicaciones;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@ConfigurationPropertiesScan
@EnableFeignClients
public class MsComunicacionesApplication {

	public static void main(String[] args) {
		SpringApplication.run(MsComunicacionesApplication.class, args);
	}

}
