package com.core.oauth.provider.azureadb2c.conf;

import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;

@ObjectClassDefinition(name = "Azure AD B2C Provider Configuration", description = "Azure AD B2C Provider Configuration")
public @interface AzureADB2CConfiguration {

	@AttributeDefinition(name = "oauth.provider.id", description = "OAuth Provider ID")
	String providerId() default "azureadb2c";

	@AttributeDefinition(name = "oauth.b2c.b2clogindomain", description = "B2C Login Domain")
	String b2cLoginDomain() default "";

	@AttributeDefinition(name = "oauth.b2c.b2ctenantname", description = "B2C Tenant Domain")
	String b2cTenantName() default "";

	@AttributeDefinition(name = "oauth.b2c.signinsignup.policy", description = "B2C Signin/SignUp Policy")
	String b2cSignInSignUpPolicyName() default "";

	@AttributeDefinition(name = "oauth.b2c.profileedit.policy", description = "B2C Profile Edit Policy")
	String b2cEditPolicyName() default "";

}
