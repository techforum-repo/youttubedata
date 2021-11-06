
## How to build


To build all the modules and deploy the `all` package to a local instance of AEM, run in the project root directory the following command:

    mvn clean install -PautoInstallSinglePackage

Or to deploy it to a publish instance, run

    mvn clean install -PautoInstallSinglePackagePublish

Or alternatively

    mvn clean install -PautoInstallSinglePackage -Daem.port=4503

```
/apps/aemspaheadless/osgiconfig/config/com.adobe.granite.cors.impl.CORSPolicyImpl~aemspaheadless.cfg.json
/conf/global/settings/dam/cfm/models/sampledata
/content/cq:graphql/global/endpoint
/content/dam/sampledata

```