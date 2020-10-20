package at.porscheinformatik.pnet.saml2.sample.springsecurity;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class SpringSecuritySaml2Client {

	public static void main(String[] args) {
		SpringApplication.run(SpringSecuritySaml2Client.class, args);
	}

	@Bean
	public static PnetSamlBootstrap boostrap() {
		return new PnetSamlBootstrap();
	}
}
