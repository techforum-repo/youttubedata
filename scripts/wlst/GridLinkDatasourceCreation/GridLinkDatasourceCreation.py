from java.io import FileInputStream

def createGridLinkJDBCResources(configProps):
   edit()
   startEdit()
        
   server='AdminServer'
   cd("Servers/"+server)
   target=cmo
   cd("../..")
   print '========================================='
   print 'Creating GridLink DataSource....'
   print '========================================='
   dsTestQuery=configProps.get("connectionpool.test.query")
   dsDriverName=configProps.get("connectionpool.driver.class")
  
            
   cd('/')
   dbURL= configProps.get("dbURL")           
   dsUserName = configProps.get("connectionpool.username")
   dsPassword = configProps.get("connectionpool.password")
   initCapacity = configProps.get("connectionpool.initCapacity")
   maxCapacity = configProps.get("connectionpool.maxCapacity")
   dsName = configProps.get("datasource.name")
   jndiname = configProps.get("datasource.jndi.name")
   datasourceTargets = configProps.get("datasource.target").split(",")

   print 'dsUserName',dsUserName
   print 'dsPassword',dsPassword
   print 'initCapacity',initCapacity
   print 'maxCapacity',maxCapacity
   print 'dsName',dsName
   print 'jndiname',jndiname
   print 'datasourceTargets',datasourceTargets

   print 'Creating DataSource: ',dsName,' ....'
  
   myResourceName = dsName   
   jdbcSystemResource = create(myResourceName,"JDBCSystemResource")     
   myFile = jdbcSystemResource.getDescriptorFileName()     
   jdbcResource = jdbcSystemResource.getJDBCResource()     
   jdbcResource.setName(myResourceName)   
  
  

   # Create the DataSource Params     
   dpBean = jdbcResource.getJDBCDataSourceParams()     
   myName=jndiname 
   dpBean.setJNDINames([myName]) 
   dpBean.setGlobalTransactionsProtocol('TwoPhaseCommit')
    
   # Create the Driver Params     
   drBean = jdbcResource.getJDBCDriverParams()     
   drBean.setPassword(dsPassword)     
   drBean.setUrl(dbURL)     
   drBean.setDriverName(dsDriverName)

   #Create the Oracle params
   orapr=jdbcResource.getJDBCOracleParams()
   orapr.setFanEnabled(true)
   orapr.setOnsNodeList('node1:6200,node2:6200')
   propBean = drBean.getProperties()
   driverProps = Properties()     
   driverProps.setProperty("user",dsUserName)
   e = driverProps.propertyNames()
   while e.hasMoreElements() :             
		propName = e.nextElement()             
		myBean = propBean.createProperty(propName)           
		myBean.setValue(driverProps.getProperty(propName))   
  
		# Create the ConnectionPool Params     
		ppBean = jdbcResource.getJDBCConnectionPoolParams()     
		ppBean.setInitialCapacity(int(initCapacity))     
		ppBean.setMaxCapacity(int(maxCapacity))     
		ppBean.setTestConnectionsOnReserve(true)     
		ppBean.setTestTableName('SQL SELECT 1 FROM DUAL')

		xaParams = jdbcResource.getJDBCXAParams()     
		xaParams.setKeepXaConnTillTxComplete(1)   

   # Add Target
   for datasourceTarget in datasourceTargets:
		print 'DataSourceTargets',datasourceTargets
		print 'DataSourceTarget',datasourceTarget
		if datasourceTarget=='':
			print ''
		else:
			jdbcSystemResource.addTarget(getMBean(datasourceTarget))     
  
 
   print 'DataSource: ',dsName,', has been created Successfully !!!'
   print '========================================='   
        
  
   save()
   activate()   


  
def main():
    propInputStream1 = FileInputStream("GridLinkDataSource.properties")
    configProps = util.Properties()
    configProps.load(propInputStream1)
  
    adminURL='t3://'+configProps.get('domain.AdminIP')+':'+configProps.get('domain.AdminPort')
    adminUserName='weblogic'
    adminPassword=configProps.get("domain.AdminPasswd")
    connect(adminUserName, adminPassword, adminURL)
  
    createGridLinkJDBCResources(configProps); 
  
    print 'Successfully created JDBC resources for SOACoreDomain'

    disconnect()

      
main()