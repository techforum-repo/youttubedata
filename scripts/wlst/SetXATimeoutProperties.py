def setXATimeoutProperties():
   dsName='JDBC Data Source-0'
   edit()
   startEdit()
       
   server='AdminServer'
   cd("Servers/"+server)
   target=cmo
 
   print '========================================='
   print 'Setting the timeout properties for DataSource....'
   print '========================================='  
           
   cd('/JDBCSystemResources/'+dsName+'/JDBCResource/'+dsName+'/JDBCXAParams/'+dsName)
   cmo.setXaSetTransactionTimeout(true)

   cd('/JDBCSystemResources/'+dsName+'/JDBCResource/'+dsName+'/JDBCXAParams/'+dsName)
   cmo.setXaTransactionTimeout(3000)

   cd('/JDBCSystemResources/'+dsName+'/JDBCResource/'+dsName+'/JDBCXAParams/'+dsName)
   cmo.setXaRetryDurationSeconds(300)

   cd('/JDBCSystemResources/'+dsName+'/JDBCResource/'+dsName+'/JDBCXAParams/'+dsName)
   cmo.setXaRetryIntervalSeconds(60)

   save()
   activate()

   print 'Timeout settings for the datasource '+dsName+' has been completed'
 
 
def main():
     
    adminURL='t3://localhost:7001'
    adminUserName='weblogic'
    adminPassword='weblogic1'
    connect(adminUserName, adminPassword, adminURL)
    setXATimeoutProperties()
    disconnect()

     

main()