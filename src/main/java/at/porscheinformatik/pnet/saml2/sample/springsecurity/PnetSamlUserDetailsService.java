package at.porscheinformatik.pnet.saml2.sample.springsecurity;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.saml.SAMLCredential;
import org.springframework.security.saml.userdetails.SAMLUserDetailsService;

public class PnetSamlUserDetailsService implements SAMLUserDetailsService {

	@Override
	public Object loadUserBySAML(SAMLCredential credential) throws UsernameNotFoundException {
		String subjectId = credential.getAttributeAsString("urn:oasis:names:tc:SAML:attribute:subject-id");

		return new UserData(subjectId);
	}

}
