# Filter to handle B2C Profile Edit with SAML Issuer

The Filter sends POST request to B2C profile edit policy URL - **https://tenantname.b2clogin.com/tenantname.onmicrosoft.com/B2C_1A_ProfileEdit/samlp/sso/login** with the SAML request parameter(the SAMLRequest is same as login request but some changes specific to Profile Edit) based on the paramter **operation=profileedit** e.g **https://localhost/content/spassr/us/en.html?operation=profileedit** in the URL, B2C redirect the user to profile edit page if already logged in, if not send the user to the login screen, upon successful login send the user to profile edit screen. Replace tenantname references with your B2C tenantname.

SAML Request - Issuer is the Entity ID configured in B2C and AEM SAML Authentication Handler

```
<?xml version="1.0" encoding="UTF-8" standalone="no"?>
<samlp:AuthnRequest xmlns:samlp="urn:oasis:names:tc:SAML:2.0:protocol"
                    AssertionConsumerServiceURL="https://localhost/saml_login"
                    Destination="https://tenantname.b2clogin.com/tenantname.onmicrosoft.com/B2C_1A_ProfileEdit/samlp/sso/login"
                    ID="_79985357-cd5d-408b-b3fb-5160cdeed452"
                    IssueInstant="2021-11-21T03:17:15Z"
                    ProtocolBinding="urn:oasis:names:tc:SAML:2.0:bindings:HTTP-POST"
                    Version="2.0">
  <saml:Issuer xmlns:saml="urn:oasis:names:tc:SAML:2.0:assertion">https://tenantname.onmicrosoft.com</saml:Issuer>
  <samlp:NameIDPolicy AllowCreate="true"
                      Format="urn:oasis:names:tc:SAML:2.0:nameid-format:transient" /></samlp:AuthnRequest>
```

#### Dependencies in core module pom.xml

```
 <!-- https://mvnrepository.com/artifact/xalan/xalan -->
		<dependency>
		    <groupId>xalan</groupId>
		    <artifactId>xalan</artifactId>
		    <version>2.7.2</version>
		</dependency>
		
		<!-- https://mvnrepository.com/artifact/xerces/xercesImpl -->
		<dependency>
		    <groupId>xerces</groupId>
		    <artifactId>xercesImpl</artifactId>
		    <version>2.12.0</version>
		</dependency>
		
		<!-- https://mvnrepository.com/artifact/org.apache.santuario/xmlsec -->
		<dependency>
		    <groupId>org.apache.santuario</groupId>
		    <artifactId>xmlsec</artifactId>
		    <version>2.0.7</version>
		</dependency>
		
```
#### Embed the following dependencies to the core bundle

Embed dependencies through bnd plugin

```
 <plugin>
                <groupId>biz.aQute.bnd</groupId>
                <artifactId>bnd-maven-plugin</artifactId>
                <executions>
                    <execution>
                        <id>bnd-process</id>
                        <goals>
                            <goal>bnd-process</goal>
                        </goals>
                        <configuration>
                            <bnd><![CDATA[
	Import-Package: javax.annotation;version=0.0.0,!org.apache.xml.resolver*;resolution:=optional;version=0.0.0,!org.apache.xml.serializer;resolution:=optional;version=0.0.0,!sun.io;resolution:=optional;version=0.0.0,*
	-includeresource: xalan-2.7.2.jar;xercesImpl-2.12.0.jar;xmlsec-2.0.7.jar;lib:=true
                                ]]></bnd>
                        </configuration>
                    </execution>
                </executions>
            </plugin>
```


**Note - The bundle is build based on AEM as a Cloud Service local SDK, may require chanes to build for other AEM versions**