package at.porscheinformatik.pnet.saml2.sample.springsecurity;

import org.opensaml.Configuration;
import org.opensaml.xml.security.BasicSecurityConfiguration;
import org.opensaml.xml.signature.SignatureConstants;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.security.saml.SAMLBootstrap;

public class PnetSamlBootstrap extends SAMLBootstrap {

	@Override
	public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
		super.postProcessBeanFactory(beanFactory);

		BasicSecurityConfiguration config = (BasicSecurityConfiguration) Configuration.getGlobalSecurityConfiguration();

		config.registerSignatureAlgorithmURI("RSA", SignatureConstants.ALGO_ID_SIGNATURE_RSA_SHA256);
		config.registerSignatureAlgorithmURI("EC", SignatureConstants.ALGO_ID_SIGNATURE_ECDSA_SHA256);

		config.deregisterDataEncryptionAlgorithmURI("AES", 192);
	}

}
