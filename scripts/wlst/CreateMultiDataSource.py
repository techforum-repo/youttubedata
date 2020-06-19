adminURL='t3://localhost:7001'
adminUserName='weblogic'
adminPassword='weblogic1'
connect(adminUserName, adminPassword, adminURL)
edit()
startEdit()
jdbcSystemResource = create("MS1","JDBCSystemResource")
jdbcResource = jdbcSystemResource.getJDBCResource()
jdbcResource.setName("MS1")
dsParams = jdbcResource.getJDBCDataSourceParams()
jndiName='jdbc/MS1'
dsParams.setJNDINames([jndiName])
dsParams.setAlgorithmType('Failover')
dsParams.setDataSourceList('DS1,DS2')
dsParams.setFailoverRequestIfBusy(true)
jdbcSystemResource.addTarget(getMBean('Servers/AdminServer'))
print('MDS1 created successfully...')
save()
activate()
disconnect()