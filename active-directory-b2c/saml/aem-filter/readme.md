# Filter to handle B2C Profile Edit with SAML Issuer

The Filter sends POST request to B2C profile edit policy URL - https://tenantname.b2clogin.com/tenantname.onmicrosoft.com/B2C_1A_ProfileEdit/samlp/sso/login based on the paramter operation=profileedit in the URL, B2C redirect the user to profile edit page if already logged in, if not send the user to the login screen, upon successful login send the user to profile edit screen.

###Dependencies in core module pom.xml

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
###Embed the following dependencies to the core bundle

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
__Import-Package: javax.annotation;version=0.0.0,!org.apache.xml.resolver*;resolution:=optional;version=0.0.0,!org.apache.xml.serializer;resolution:=optional;version=0.0.0,!sun.io;resolution:=optional;version=0.0.0,*__
__-includeresource: xalan-2.7.2.jar;xercesImpl-2.12.0.jar;xmlsec-2.0.7.jar;lib:=true__
                                ]]></bnd>
                        </configuration>
                    </execution>
                </executions>
            </plugin>
```