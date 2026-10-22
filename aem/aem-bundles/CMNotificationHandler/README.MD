Cloud Manager Notification Handler Bundle
========
The custom bundle enables the Notification Handler to Handle Cloud Manager Notifications.

Building
--------

This project uses Maven for building. 

Common build commands:

From the root directory, run ``mvn clean install -PautoInstallBundle -Daem.port=4503`` to build the bundle and install to a AEM publisher running on 4503.

Change the -Daem.port value to deploy the bundle to the server running on different post, also user -Daem.host to deploy the bundle to the servers not running on localhost 
