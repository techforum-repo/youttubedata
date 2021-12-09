package com.core.oauth.provider.azureadb2c;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.jcr.Node;
import javax.jcr.RepositoryException;
import javax.jcr.Session;

import org.apache.jackrabbit.api.security.user.Authorizable;
import org.apache.jackrabbit.api.security.user.User;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.json.JSONException;
import org.json.JSONObject;
import org.osgi.service.component.ComponentContext;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;
import org.osgi.service.metatype.annotations.Designate;
import org.scribe.builder.api.Api;
import org.scribe.model.OAuthRequest;
import org.scribe.model.Response;
import org.scribe.model.Verb;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.adobe.granite.auth.oauth.Provider;
import com.adobe.granite.auth.oauth.Provider2;
import com.adobe.granite.auth.oauth.ProviderConfig;
import com.adobe.granite.auth.oauth.ProviderType;
import com.adobe.granite.security.user.UserPropertiesService;
import com.core.oauth.provider.azureadb2c.conf.AzureADB2CConfiguration;
import com.core.oauth.provider.azureadb2c.models.AzureADB2CConfig;

@SuppressWarnings("deprecation")
@Component(name = "Adobe Granite OAuth Azure AD B2C Provider", configurationPid = "com.core.oauth.provider.azureadb2c.config", immediate = true, service = Provider.class)
@Designate(ocd = AzureADB2CConfiguration.class)
public class AzureADB2COAuth2ProviderImpl implements Provider2 {

	private final Logger log = LoggerFactory.getLogger(getClass());

	@Reference
	private UserPropertiesService userPropertiesService;

	@Reference(cardinality = ReferenceCardinality.MANDATORY, policy = ReferencePolicy.STATIC)
	private ResourceResolverFactory resourceResolverFactory;

	private ResourceResolver serviceUserResolver;

	private Session session;

	private String id;
	private String name;
	private AzureADB2CConfig b2CConfig;
	private static final String USER_ADMIN = "oauth-azureadb2c-service";

	@Override
	public String getId() {
		return this.id;
	}

	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public ProviderType getType() {
		return ProviderType.OAUTH2;
	}

	@Override
	public Api getApi() {
		return new AzureADB2COAuth2Api(b2CConfig);
	}

	@Override
	public String[] getExtendedDetailsURLs(String scope, String userId, Map<String, Object> props) {
		return new String[0];
	}

	public String mapProperty(String property) {

		return getPropertyPath(property);
	}

	@Override
	public String mapUserId(final String userId, final Map<String, Object> props) {
		final String userName = (String) props.get(getPropertyPath("id"));
		if (userName != null && userName.length() > 0) {
			return "b2c-" + userName;
		} else {
			return "b2c-" + userId;
		}
	}

	protected String getPropertyPath(final String property) {
		return "profile/" + property;
	}

	@Override
	public String getUserFolderPath(String userId, String clientId, Map<String, Object> props) {

		StringBuilder sb = new StringBuilder(getId());
		if (userId != null)
			sb.append("/").append(userId.substring(0, 4));
		return sb.toString();
	}

	@Override
	public Map<String, Object> mapProperties(String srcUrl, String clientId, Map<String, Object> existing,
			Map<String, String> newProperties) {
		Map<String, Object> mapped = new HashMap<>();
		mapped.putAll(existing);
		for (Map.Entry<String, String> prop : newProperties.entrySet()) {
			mapped.put(mapProperty(prop.getKey()), prop.getValue());
		}
		return mapped;
	}

	@Override
	public String getAccessTokenPropertyPath(String clientId) {
		return "profile/app-" + clientId;
	}

	@Override
	public User getCurrentUser(SlingHttpServletRequest request) {
		Authorizable authorizable = request.adaptTo(Authorizable.class);
		if (authorizable != null && !authorizable.isGroup())
			return (User) authorizable;
		return null;
	}

	/**
	 * Called after a user is created by Granite
	 * 
	 * @param user
	 */

	@Override
	public void onUserCreate(final User user) {

		try {
			session.refresh(true);
			final Node userNode = session.getNode(userPropertiesService.getAuthorizablePath(user.getID()));
			final Node profNode = userNode.getNode("profile");

			if (user.hasProperty("profile/givenName")) {
				String firstName = user.getProperty("profile/givenName")[0].getString();
				profNode.setProperty("givenName", firstName);
			}

			if (user.hasProperty("profile/familyName")) {
				String lastName = user.getProperty("profile/familyName")[0].getString();
				profNode.setProperty("familyName", lastName);
			}

			if (user.hasProperty("profile/email")) {
				String email = user.getProperty("profile/email")[0].getString();
				profNode.setProperty("email", email);
			}

			session.save();
		} catch (final RepositoryException e) {
			log.error("onUserCreate: failed to copy profile properties to cq profile", e);
		}
	}

	@Override
	public void onUserUpdate(User user) {
		onUserCreate(user);
	}

	@Override
	public OAuthRequest getProtectedDataRequest(String url) {
		return new OAuthRequest(Verb.GET, url);
	}

	@Override
	public Map<String, String> parseProfileDataResponse(Response response) throws IOException {
		String body = null;
		try {
			body = response.getBody();
			JSONObject json = new JSONObject(body);
			Map<String, String> newProps = new HashMap<>();
			for (Iterator<String> keys = json.keys(); keys.hasNext();) {
				String key = keys.next();
				newProps.put(key, json.optString(key));
			}
			return newProps;
		} catch (JSONException je) {
			this.log.debug("problem parsing JSON; response body was: {}", body);
			throw new IOException(je.toString());
		} catch (Exception e) {
			this.log.error("Exception while parsing profile data");
			throw new IOException(e.toString());
		}
	}

	@Override
	public String getUserIdProperty() {
		return "email";
	}

	@Override
	public String getOAuthIdPropertyPath(String clientId) {
		return "oauth/oauthid-" + clientId;
	}

	@Override
	public String getValidateTokenUrl(String clientId, String token) {
		this.log.info("This provider doesn't support the validation of a token");
		return null;
	}

	@Override
	public boolean isValidToken(String responseBody, String clientId, String tokenType) {
		this.log.info("This provider doesn't support the validation of a token");
		return false;
	}

	@Override
	public String getUserIdFromValidateTokenResponseBody(String responseBody) {
		this.log.info("This provider doesn't support the validation of a token");
		return null;
	}

	@Override
	public String getErrorDescriptionFromValidateTokenResponseBody(String responseBody) {
		this.log.info("This provider doesn't support the validation of a token");
		return null;
	}

	@Activate
	protected void activate(final AzureADB2CConfiguration config) throws Exception {
		name = getClass().getSimpleName();
		id = config.providerId();

		b2CConfig = new AzureADB2CConfig();
		b2CConfig.setB2CLoginDomain(config.b2cLoginDomain());
		b2CConfig.setB2CTenantName(config.b2cTenantName());
		b2CConfig.setB2CSignInSignUpPolicy(config.b2cSignInSignUpPolicyName());
		b2CConfig.setB2CEditPolicy(config.b2cEditPolicyName());

		Map<String, Object> serviceParams = new HashMap<String, Object>();
		serviceParams.put(ResourceResolverFactory.SUBSERVICE, USER_ADMIN);
		serviceUserResolver = this.resourceResolverFactory.getServiceResourceResolver(serviceParams);
		session = serviceUserResolver.adaptTo(Session.class);

	}

	@Deactivate
	protected void deactivate(final ComponentContext componentContext) throws Exception {
		log.debug("deactivating provider id {}", id);
		if (session != null && session.isLive()) {
			try {
				session.logout();
			} catch (final Exception e) {
				// ignore
			}
			session = null;
		}
		if (serviceUserResolver != null) {
			serviceUserResolver.close();
		}
	}

	@Override
	public String getDetailsURL() {
		return "https://" + this.b2CConfig.getB2CLoginDomain() + "/" + this.b2CConfig.getB2CTenantName()
				+ ".onmicrosoft.com/" + this.b2CConfig.getB2CSignInSignUpPolicy() + "/openid/v2.0/userinfo";
	}

	@Override
	public String[] getExtendedDetailsURLs(String scope) {
		return null;
	}

	@Override
	public String logout(ProviderConfig config) {
		return "https://" + this.b2CConfig.getB2CLoginDomain() + "/" + this.b2CConfig.getB2CTenantName()
				+ ".onmicrosoft.com/" + this.b2CConfig.getB2CSignInSignUpPolicy()
				+ "/oauth2/v2.0/logout?post_logout_redirect_uri=" + config.getCallBackUrl();
	}

}
