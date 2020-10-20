package at.porscheinformatik.pnet.saml2.sample.springsecurity;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import javax.xml.namespace.QName;

import org.opensaml.common.SAMLException;
import org.opensaml.saml2.core.AudienceRestriction;
import org.opensaml.saml2.core.Condition;
import org.opensaml.saml2.core.Conditions;
import org.opensaml.saml2.core.OneTimeUse;
import org.opensaml.saml2.core.ProxyRestriction;
import org.opensaml.saml2.core.Response;
import org.opensaml.xml.encryption.DecryptionException;
import org.opensaml.xml.security.SecurityException;
import org.opensaml.xml.validation.ValidationException;
import org.springframework.security.saml.SAMLCredential;
import org.springframework.security.saml.context.SAMLMessageContext;
import org.springframework.security.saml.websso.WebSSOProfileConsumerImpl;

public class PnetWebSSOProfileConsumer extends WebSSOProfileConsumerImpl {
	// In a real world environemnt you should change this to a database call or
	// whatever and cleanup the list after the timestamp is reached
	private static final Set<String> PROCESSED_RESPONSES = new HashSet<>();

	@Override
	public SAMLCredential processAuthenticationResponse(SAMLMessageContext context)
			throws SAMLException, SecurityException, ValidationException, DecryptionException {
		SAMLCredential credentials = super.processAuthenticationResponse(context);
		Response response = (Response) context.getInboundSAMLMessage();

		if (PROCESSED_RESPONSES.contains(response.getID())) {
			throw new ValidationException("Response " + response.getID() + " was already processed");
		}

		return credentials;
	}

	@Override
	protected void verifyAssertionConditions(Conditions conditions, SAMLMessageContext context,
			boolean audienceRequired) throws SAMLException {

		// Verify that audience is present when required
		if (audienceRequired && (conditions == null || conditions.getAudienceRestrictions().size() == 0)) {
			throw new SAMLException("Assertion invalidated by missing Audience Restriction");
		}

		// If no conditions are implied, storage is deemed valid
		if (conditions == null) {
			return;
		}

		if (conditions.getNotBefore() != null) {
			if (conditions.getNotBefore().minusSeconds(getResponseSkew()).isAfterNow()) {
				throw new SAMLException(
						"Assertion is not yet valid, invalidated by condition notBefore " + conditions.getNotBefore());
			}
		}
		if (conditions.getNotOnOrAfter() != null) {
			if (conditions.getNotOnOrAfter().plusSeconds(getResponseSkew()).isBeforeNow()) {
				throw new SAMLException("Assertion is no longer valid, invalidated by condition notOnOrAfter "
						+ conditions.getNotOnOrAfter());
			}
		}

		List<Condition> notUnderstoodConditions = new LinkedList<Condition>();

		for (Condition condition : conditions.getConditions()) {

			QName conditionQName = condition.getElementQName();

			if (conditionQName.equals(AudienceRestriction.DEFAULT_ELEMENT_NAME)) {

				verifyAudience(context, conditions.getAudienceRestrictions());

			} else if (conditionQName.equals(OneTimeUse.DEFAULT_ELEMENT_NAME)) {

				// Everything fine, we don't reuse assertions and we prevent Replay Attacks by
				// checking the response ID against a map

			} else if (conditionQName.equals(ProxyRestriction.DEFAULT_ELEMENT_NAME)) {

				ProxyRestriction restriction = (ProxyRestriction) condition;
				log.debug("Honoring ProxyRestriction with count {}, system does not issue assertions to 3rd parties",
						restriction.getProxyCount());

			} else {

				log.debug("Condition {} is not understood", condition);
				notUnderstoodConditions.add(condition);

			}

		}

		// Check not understood conditions
		verifyConditions(context, notUnderstoodConditions);
	}

}
