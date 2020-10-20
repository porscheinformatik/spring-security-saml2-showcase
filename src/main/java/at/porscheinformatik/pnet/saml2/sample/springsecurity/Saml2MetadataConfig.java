package at.porscheinformatik.pnet.saml2.sample.springsecurity;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.opensaml.saml2.metadata.provider.HTTPMetadataProvider;
import org.opensaml.saml2.metadata.provider.MetadataProvider;
import org.opensaml.saml2.metadata.provider.MetadataProviderException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.saml.key.JKSKeyManager;
import org.springframework.security.saml.key.KeyManager;
import org.springframework.security.saml.metadata.ExtendedMetadata;
import org.springframework.security.saml.metadata.ExtendedMetadataDelegate;
import org.springframework.security.saml.metadata.MetadataDisplayFilter;
import org.springframework.security.saml.metadata.MetadataGenerator;
import org.springframework.security.saml.metadata.MetadataGeneratorFilter;
import org.springframework.security.saml.metadata.MetadataManager;

@Configuration
public class Saml2MetadataConfig {

	@Bean
	public KeyManager keyManager() {
		Map<String, String> passwords = new HashMap<>();
		passwords.put("saml2-sample", "changeme");

		return new JKSKeyManager(
				new ClassPathResource("at/porscheinformatik/pnet/saml2/sample/springsecurity/saml2.keystore"),
				"changeme", passwords, "saml2-sample");
	}

	@Bean
	public ExtendedMetadataDelegate idpMetadata() throws MetadataProviderException {
		HTTPMetadataProvider delegate = new HTTPMetadataProvider("https://localhost:5443/identity/saml2", 5000);
		delegate.setParserPool(org.opensaml.Configuration.getParserPool());

		return new ExtendedMetadataDelegate(delegate);
	}

	@Bean
	public MetadataManager metadataManager(List<MetadataProvider> providers) throws MetadataProviderException {
		return new MetadataManager(providers);
	}

	@Bean
	public ExtendedMetadata extendedMetadata() {
		ExtendedMetadata metadata = new ExtendedMetadata();
		metadata.setSigningKey("saml2-sample");
		metadata.setEncryptionKey("saml2-sample");

		return metadata;
	}

	@Bean
	public MetadataGenerator metadataGenerator(ExtendedMetadata extendedMetadata) {
		PnetMetadataGenerator generator = new PnetMetadataGenerator();
		generator.setEntityBaseURL("https://localhost:1443");
		generator.setEntityId("https://localhost:1443/saml2");
		generator.setWantAssertionSigned(true);
		generator.setBindingsSSO(Arrays.asList("post"));
		generator.setNameID(Arrays.asList("transient"));
		generator.setExtendedMetadata(extendedMetadata);

		return generator;
	}

	@Bean
	public MetadataGeneratorFilter metadataGeneratorFilter(MetadataGenerator generator) {
		return new MetadataGeneratorFilter(generator);
	}

	@Bean
	public MetadataDisplayFilter metadataDisplayFilter(MetadataGenerator generator) {
		return new PnetMetadataDisplayFilter();
	}
}
