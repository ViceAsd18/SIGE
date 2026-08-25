package cl.sige.plataforma.ms_mensajeria;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
@ConfigurationPropertiesScan
public class MsMensajeriaApplication {

	public static void main(String[] args) {
		SpringApplication.run(MsMensajeriaApplication.class, args);
	}

}
