# Azure AD B2C OAuth Provider for AEM #

This providers enables the authentication of AEM websites through Azure AD B2C OAuth.

## Building ##

This project uses Maven for building. 

Common build commands:

From the root directory, run ``mvn clean install -PautoInstallBundle -Daem.port=4503`` to build the bundle and install to a AEM publisher running on 4503.

Change the -Daem.port value to deploy the bundle to the server running on different post, also user -Daem.host to deploy the bundle to the servers not running on localhost 

## Filter to handle B2C Profile Edit(Local Account) with OAuth - AzureADB2COauthProfileEditFilter.java ##

The Filter sends POST request to B2C profile edit policy URL - **https://tenantname.b2clogin.com/tenantname.onmicrosoft.com//oauth2/v2.0/authorize** with the required parameters

```
https://tenantname.b2clogin.com/tenantname.onmicrosoft.com//oauth2/v2.0/authorize?p=B2C_1A_ProfileEdit&client_id=xxxxxxxxxxxx&response_type=code&redirect_uri=https://localhost/content/wknd/us/en.html/callback/j_security_check&response_mode=query&scope=xxxxxxxxxxxxxxxxxxxx&state=xxxxxxxxxxxxxxxxxxx
```