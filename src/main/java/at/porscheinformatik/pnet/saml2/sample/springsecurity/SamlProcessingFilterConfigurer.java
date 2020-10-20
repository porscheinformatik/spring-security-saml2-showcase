package at.porscheinformatik.pnet.saml2.sample.springsecurity;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.saml.SAMLProcessingFilter;
import org.springframework.security.saml.context.SAMLContextProvider;
import org.springframework.security.saml.processor.SAMLProcessor;
import org.springframework.security.web.csrf.CsrfFilter;

public class SamlProcessingFilterConfigurer
		extends AbstractHttpConfigurer<SamlProcessingFilterConfigurer, HttpSecurity> {

	private SAMLProcessingFilter filter = new SAMLProcessingFilter();

	@Override
	public void configure(HttpSecurity http) throws Exception {
		filter.setAuthenticationManager(http.getSharedObject(AuthenticationManager.class));

		http.addFilterBefore(filter, CsrfFilter.class);
	}

	public SamlProcessingFilterConfigurer samlProcessor(SAMLProcessor samlProcessor) {
		filter.setSAMLProcessor(samlProcessor);

		return this;
	}

	public SamlProcessingFilterConfigurer contextProvider(SAMLContextProvider contextProvider) {
		filter.setContextProvider(contextProvider);

		return this;
	}

}
