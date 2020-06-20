from java.io import FileInputStream

propInputStream = FileInputStream("ConfigureForeignJMSServer.properties")
configProps = Properties()
configProps.load(propInputStream)

connect(configProps.get("adminUserName"),configProps.get("adminUserPassword"), 't3://'+configProps.get("adminServerHost")+':'+configProps.get("adminServerPort"))

foreignJMSModuleName=configProps.get("foreignJMSModuleName")
foreignJMSServerName=configProps.get("foreignJMSServerName")
localConnectionFactoryJNDI=configProps.get("localConnectionFactoryJNDI")
remoteConnectionFactoryJNDI=configProps.get("remoteConnectionFactoryJNDI")
localDestinationJNDI=configProps.get("localDestinationJNDI")
remoteDestinationJNDI=configProps.get("remoteDestinationJNDI")


remoteConnectionURL=configProps.get("remoteConnectionURL")
initialJNDIContextFactory=configProps.get("initialJNDIContextFactory")
remoteUserName=configProps.get("remoteUserName")
remotePassword=configProps.get("remotePassword")


edit()
startEdit()

cd('/')
cmo.createJMSSystemResource(foreignJMSModuleName)

cd('/SystemResources/'+foreignJMSModuleName)
set('Targets',jarray.array([ObjectName('com.bea:Name=AdminServer,Type=Server')], ObjectName))

cd('/JMSSystemResources/'+foreignJMSModuleName+'/JMSResource/'+foreignJMSModuleName)
cmo.createForeignServer(foreignJMSServerName)

cd('/JMSSystemResources/'+foreignJMSModuleName+'/JMSResource/'+foreignJMSModuleName+'/ForeignServers/'+foreignJMSServerName)
cmo.setDefaultTargetingEnabled(true)

cmo.setJNDIPropertiesCredential(remotePassword)

cmo.setInitialContextFactory(initialJNDIContextFactory)
cmo.setConnectionURL(remoteConnectionURL)
cmo.createJNDIProperty('java.naming.security.principal')

cd('/JMSSystemResources/'+foreignJMSModuleName+'/JMSResource/'+foreignJMSModuleName+'/ForeignServers/'+foreignJMSServerName+'/JNDIProperties/java.naming.security.principal')
cmo.setValue(remoteUserName)

cd('/JMSSystemResources/'+foreignJMSModuleName+'/JMSResource/'+foreignJMSModuleName+'/ForeignServers/'+foreignJMSServerName)

cmo.createForeignConnectionFactory('ForeignJMSConnectionFactory')

cd('/JMSSystemResources/'+foreignJMSModuleName+'/JMSResource/'+foreignJMSModuleName+'/ForeignServers/'+foreignJMSServerName+'/ForeignConnectionFactories/ForeignJMSConnectionFactory')
cmo.setLocalJNDIName(localConnectionFactoryJNDI)
cmo.setRemoteJNDIName(remoteConnectionFactoryJNDI)

cd('/JMSSystemResources/'+foreignJMSModuleName+'/JMSResource/'+foreignJMSModuleName+'/ForeignServers/'+foreignJMSServerName)
cmo.createForeignDestination('ForeignJMSDestination')

cd('/JMSSystemResources/'+foreignJMSModuleName+'/JMSResource/'+foreignJMSModuleName+'/ForeignServers/'+foreignJMSServerName+'/ForeignDestinations/ForeignJMSDestination')
cmo.setLocalJNDIName(localDestinationJNDI)
cmo.setRemoteJNDIName(remoteDestinationJNDI)

activate()