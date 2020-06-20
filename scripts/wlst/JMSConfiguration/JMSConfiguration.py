from java.io import FileInputStream

propInputStream = FileInputStream("JMSConfiguration.properties")
configProps = Properties()
configProps.load(propInputStream)

connect(configProps.get("adminUserName"),configProps.get("adminUserPassword"), 't3://'+configProps.get("adminServerHost")+':'+configProps.get("adminServerPort"))

fileStoreName=configProps.get("fileStoreName")
jmsServerName=configProps.get("jmsServerName")
moduleName=configProps.get("moduleName")
connectionFactoryName=configProps.get("connectionFactoryName")
subDeploymentName=configProps.get("subDeploymentName")
queueName=configProps.get("queueName")

edit()
startEdit()

print 'Creating File Store'
cd('/')
cmo.createFileStore(fileStoreName)
cd('/FileStores/'+fileStoreName)
cmo.setDirectory(fileStoreName)
set('Targets',jarray.array([ObjectName('com.bea:Name=AdminServer,Type=Server')], ObjectName))


print 'Creating JMS Server'
cd('/')
print 'Creating JMS Server.'
print 'JMS Server: '+jmsServerName
cmo.createJMSServer(jmsServerName)
cd('/JMSServers/'+jmsServerName)
cmo.setPersistentStore(getMBean('/FileStores/'+fileStoreName))
cmo.setTemporaryTemplateResource(None)
cmo.setTemporaryTemplateName(None)
cmo.addTarget(getMBean('/Servers/AdminServer'))

print 'Creating JMS Module'
cd('/')
cmo.createJMSSystemResource(moduleName)
cd('/JMSSystemResources/'+moduleName)
cmo.addTarget(getMBean('/Servers/AdminServer'))
cmo.createSubDeployment(subDeploymentName)

print 'Creating Connection Factory'
cd('/')
cd('/JMSSystemResources/'+moduleName+'/JMSResource/'+moduleName)
cmo.createConnectionFactory(connectionFactoryName)
cd('/JMSSystemResources/'+moduleName+'/JMSResource/'+moduleName+'/ConnectionFactories/'+connectionFactoryName)
cmo.setJNDIName('jms/'+connectionFactoryName)
#set('SubDeploymentName',subDeploymentName)
cd('/JMSSystemResources/'+moduleName+'/JMSResource/'+moduleName+'/ConnectionFactories/'+connectionFactoryName+'/SecurityParams/'+connectionFactoryName)
cmo.setAttachJMSXUserId(false)
cd('/JMSSystemResources/'+moduleName+'/JMSResource/'+moduleName+'/ConnectionFactories/'+connectionFactoryName+'/ClientParams/'+connectionFactoryName)
cmo.setClientIdPolicy('Restricted')
cmo.setSubscriptionSharingPolicy('Exclusive')
cmo.setMessagesMaximum(10)
#cd('/JMSSystemResources/'+moduleName+'/JMSResource/'+moduleName+'/ConnectionFactories/'+connectionFactoryName+'/TransactionParams/'+connectionFactoryName)
#cmo.setXAConnectionFactoryEnabled(true)
cd('/JMSSystemResources/'+moduleName+'/JMSResource/'+moduleName+'/ConnectionFactories/'+connectionFactoryName)
cmo.setDefaultTargetingEnabled(true)

print 'Creating Queue'
cd('/')
cd('/JMSSystemResources/'+moduleName+'/JMSResource/'+moduleName)
cmo.createQueue(queueName)
cd('/JMSSystemResources/'+moduleName+'/JMSResource/'+moduleName+'/Queues/'+queueName)
set('JNDIName','jms/'+queueName)
set('SubDeploymentName',subDeploymentName)
cd('/JMSSystemResources/'+moduleName+'/SubDeployments/'+subDeploymentName)
cmo.addTarget(getMBean('/JMSServers/'+jmsServerName))

print 'JMS Resources are Successfully Created'
activate()