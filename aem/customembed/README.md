# Sample AEM project template

This is a project template for AEM-based applications. Enables the custom embeddable support for vimeo vido

# Changes to Support video embeddable

* core/src/main/java/com/embed/core/models/embeddable/vimeo
* ui.apps/src/main/content/jcr_root/apps/customembed/components/embed/embeddable/vimeo
* ui.config/src/main/content/jcr_root/apps/customembed/osgiconfig/config

## How to build

 <aem.sdk.api>2020.12.4676.20201216T130744Z-201201</aem.sdk.api> is used(parent pom.xml), modify the version based on your cloud server version.

To build all the modules run in the project root directory the following command with Maven 3:

    mvn clean install

To build all the modules and deploy the `all` package to a local instance of AEM, run in the project root directory the following command:

    mvn clean install -PautoInstallSinglePackage

Or to deploy it to a publish instance, run

    mvn clean install -PautoInstallSinglePackagePublish

Or alternatively

    mvn clean install -PautoInstallSinglePackage -Daem.port=4503

Or to deploy only the bundle to the author, run

    mvn clean install -PautoInstallBundle

Or to deploy only a single content package, run in the sub-module directory (i.e `ui.apps`)

    mvn clean install -PautoInstallPackage

