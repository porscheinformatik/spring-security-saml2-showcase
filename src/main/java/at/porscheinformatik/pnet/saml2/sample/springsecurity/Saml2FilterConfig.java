package at.porscheinformatik.pnet.saml2.sample.springsecurity;

import java.util.Arrays;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.saml.SAMLAuthenticationProvider;
import org.springframework.security.saml.SAMLEntryPoint;
import org.springframework.security.saml.context.SAMLContextProvider;
import org.springframework.security.saml.context.SAMLContextProviderImpl;
import org.springframework.security.saml.log.SAMLDefaultLogger;
import org.springframework.security.saml.log.SAMLLogger;
import org.springframework.security.saml.processor.HTTPPostBinding;
import org.springframework.security.saml.processor.HTTPRedirectDeflateBinding;
import org.springframework.security.saml.processor.SAMLProcessor;
import org.springframework.security.saml.processor.SAMLProcessorImpl;
import org.springframework.security.saml.userdetails.SAMLUserDetailsService;
import org.springframework.security.saml.util.VelocityFactory;
import org.springframework.security.saml.websso.WebSSOProfile;
import org.springframework.security.saml.websso.WebSSOProfileImpl;

@Configuration
public class Saml2FilterConfig {

	@Bean
	public SAMLContextProvider contextProvider() {
		return new SAMLContextProviderImpl();
	}

	@Bean
	public SAMLProcessor samlProcessor() {

		HTTPPostBinding postBinding = new HTTPPostBinding(org.opensaml.Configuration.getParserPool(),
				VelocityFactory.getEngine());
		HTTPRedirectDeflateBinding redirectBinding = new HTTPRedirectDeflateBinding(
				org.opensaml.Configuration.getParserPool());

		return new SAMLProcessorImpl(Arrays.asList(postBinding, redirectBinding));
	}

	@Bean
	public WebSSOProfile webSSOprofile() {
		return new WebSSOProfileImpl();
	}

	@Bean
	public SAMLEntryPoint samlEntryPoint() {
		return new SAMLEntryPoint();
	}

	@Bean
	public SAMLLogger samlLogger() {
		SAMLDefaultLogger logger = new SAMLDefaultLogger();

		logger.setLogAllMessages(true);
		logger.setLogErrors(true);
		logger.setLogMessagesOnException(true);

		return logger;
	}

	@Bean
	public SAMLUserDetailsService samlUserDetailsService() {
		return new PnetSamlUserDetailsService();
	}

	@Bean(name = { "webSSOprofileConsumer", "hokWebSSOprofileConsumer" })
	public PnetWebSSOProfileConsumer webSSOprofileConsumer() {
		return new PnetWebSSOProfileConsumer();
	}

	@Bean
	public AuthenticationProvider samlAuthenticationProvider() {
		return new SAMLAuthenticationProvider();
	}
}
