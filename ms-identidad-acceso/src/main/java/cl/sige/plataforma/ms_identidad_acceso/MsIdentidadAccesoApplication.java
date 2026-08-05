package cl.sige.plataforma.ms_identidad_acceso;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
public class MsIdentidadAccesoApplication {

	public static void main(String[] args) {
		SpringApplication.run(MsIdentidadAccesoApplication.class, args);
	}

}
