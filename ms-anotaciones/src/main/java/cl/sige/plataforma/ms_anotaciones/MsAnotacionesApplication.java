package cl.sige.plataforma.ms_anotaciones;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@ConfigurationPropertiesScan
@EnableFeignClients
public class MsAnotacionesApplication {

	public static void main(String[] args) {
		SpringApplication.run(MsAnotacionesApplication.class, args);
	}

}
