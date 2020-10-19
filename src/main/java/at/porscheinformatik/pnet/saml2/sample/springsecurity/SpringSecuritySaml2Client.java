package at.porscheinformatik.pnet.saml2.sample.springsecurity;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ImportResource;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@SpringBootApplication
@EnableWebSecurity
@ImportResource(value = "classpath:at/porscheinformatik/pnet/saml2/sample/springsecurity/spring-security.xml")
public class SpringSecuritySaml2Client {

	public static void main(String[] args) {
		SpringApplication.run(SpringSecuritySaml2Client.class, args);
	}
}
