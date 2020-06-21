adminURL='t3://localhost:7001'
adminUserName='weblogic'
adminPassword='weblogic1'
connect(adminUserName, adminPassword, adminURL)

edit()
startEdit()

cmo.setClusterConstraintsEnabled(true)

save()
activate()