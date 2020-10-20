package at.porscheinformatik.pnet.saml2.sample.springsecurity;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.saml.SAMLEntryPoint;
import org.springframework.security.saml.context.SAMLContextProvider;
import org.springframework.security.saml.metadata.MetadataDisplayFilter;
import org.springframework.security.saml.metadata.MetadataGeneratorFilter;
import org.springframework.security.saml.processor.SAMLProcessor;
import org.springframework.security.web.access.channel.ChannelProcessingFilter;

@Configuration
@EnableWebSecurity
public class SpringSecuritySaml2ClientSecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private MetadataGeneratorFilter generatorFilter;

	@Autowired
	private MetadataDisplayFilter displayFilter;

	@Autowired
	private SAMLEntryPoint samlEntryPoint;

	@Autowired
	private SAMLProcessor samlProcessor;

	@Autowired
	private SAMLContextProvider contextProvider;

	@Autowired
	private List<AuthenticationProvider> authProviders;

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		super.configure(auth);

		authProviders.forEach(auth::authenticationProvider);
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {

		http.addFilterBefore(generatorFilter, ChannelProcessingFilter.class);
		http.addFilterAfter(displayFilter, MetadataGeneratorFilter.class);

		http.apply(new SamlProcessingFilterConfigurer().samlProcessor(samlProcessor).contextProvider(contextProvider));

		http.exceptionHandling().authenticationEntryPoint(samlEntryPoint);

		http.authorizeRequests().anyRequest().authenticated();
	}
}
