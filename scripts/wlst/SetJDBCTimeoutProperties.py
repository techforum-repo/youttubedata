def setJDBCTimeoutProperties():
   dsName='CRM6EAIReference'
   edit()
   startEdit()
       
   server='AdminServer'
   cd("Servers/"+server)
   target=cmo
 
   print '========================================='
   print 'Setting the timeout properties for DataSource....'
   print '========================================='  
           
   cd('/JDBCSystemResources/'+dsName+'/JDBCResource/'+dsName+'/JDBCDriverParams/'+dsName+'/Properties/'+dsName)
   #cmo.destroyProperty(getMBean('/JDBCSystemResources/'+dsName+'/JDBCResource/'+dsName+'/JDBCDriverParams/'+dsName+'/Properties/'+dsName+'/Properties/oracle.net.CONNECT_TIMEOUT'))
   cmo.createProperty('oracle.net.CONNECT_TIMEOUT')


   #cmo.destroyProperty(getMBean('/JDBCSystemResources/'+dsName+'/JDBCResource/'+dsName+'/JDBCDriverParams/'+dsName+'/Properties/'+dsName+'/Properties/oracle.jdbc.ReadTimeout'))
   cmo.createProperty('oracle.jdbc.ReadTimeout')

   cd('/JDBCSystemResources/'+dsName+'/JDBCResource/'+dsName+'/JDBCDriverParams/'+dsName+'/Properties/'+dsName+'/Properties/oracle.net.CONNECT_TIMEOUT')
   cmo.setValue('10000')

   cd('/JDBCSystemResources/'+dsName+'/JDBCResource/'+dsName+'/JDBCDriverParams/'+dsName+'/Properties/'+dsName+'/Properties/oracle.jdbc.ReadTimeout')
   cmo.setValue('20000')

   cd('/JDBCSystemResources/'+dsName+'/JDBCResource/'+dsName+'/JDBCConnectionPoolParams/'+dsName)
   cmo.setInactiveConnectionTimeoutSeconds(120)

   cd('/JDBCSystemResources/'+dsName+'/JDBCResource/'+dsName+'/JDBCConnectionPoolParams/'+dsName)
   cmo.setConnectionReserveTimeoutSeconds(30)

   cd('/JDBCSystemResources/'+dsName+'/JDBCResource/'+dsName+'/JDBCConnectionPoolParams/'+dsName)
   cmo.setStatementTimeout(120)

   save()
   activate()

   print 'Timeout settings for the datasource '+dsName+' has been completed'
 
 
def main():
     
    adminURL='t3://localhost:7001'
    adminUserName='weblogic'
    adminPassword='weblogic1'
    connect(adminUserName, adminPassword, adminURL)
    setJDBCTimeoutProperties()
    disconnect()

     

main()