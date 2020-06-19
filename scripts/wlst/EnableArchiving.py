adminURL='t3://localhost:7001'
adminUserName='weblogic'
adminPassword='weblogic1'
connect(adminUserName, adminPassword, adminURL)
domainRuntime()
edit()
startEdit()
cmo.setConfigBackupEnabled(true)
cmo.setArchiveConfigurationCount(5)
save()
activate()