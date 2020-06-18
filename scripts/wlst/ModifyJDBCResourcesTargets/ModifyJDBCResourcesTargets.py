import re
from java.io import FileInputStream   

def UntargetTargetJDBCResources():
    edit()
    startEdit()
    propInputStream = FileInputStream('JDBCProperties.properties')
    configProps = Properties()
    configProps.load(propInputStream)    

    totalDataSource_to_untargetTarget=configProps.get("domain1.total.DS")
    
    server='AdminServer'
    cd("Servers/"+server)
       
    i=1
    while (i <= int(totalDataSource_to_untargetTarget)) :
	   prefix = configProps.get("domain1.prefix."+ str(i))
	   dsName = configProps.get("domain1."+prefix+".datasource.name."+ str(i))
	   datasourceTargets = re.split(",",configProps.get("domain1."+prefix+".datasource.target."+ str(i)))
	   targetArray=[]	
	   
	   for datasourceTarget in datasourceTargets:
			print 'DataSourceTargets',datasourceTargets
			print 'DataSourceTarget',datasourceTarget
			if datasourceTarget=='':
				print ''
			else:
				cd ('/JDBCSystemResources/'+dsName)
				set('Targets',jarray.array([], ObjectName))
				target=datasourceTarget[datasourceTarget.index("/")+1:len(datasourceTarget)]							
				if datasourceTarget.startswith('Cluster'):
					targetArray.append(ObjectName('com.bea:Name='+target+',Type=Cluster'))					
				elif datasourceTarget.startswith('Server'):
					targetArray.append(ObjectName('com.bea:Name='+target+',Type=Server'))						   
			
	   print 'Targets: ',targetArray
	   set('Targets',jarray.array(targetArray, ObjectName))
	   print 'DataSource: ',dsName,',Target has been updated Successfully !!!'
	   print '========================================='    
	   i = i + 1

    print '========================================='
    save()
    activate()   


   
def main():
        adminURL='t3://localhost:7001'
        adminUserName='weblogic'
        adminPassword='weblogic1'
        connect(adminUserName, adminPassword, adminURL)
        UntargetTargetJDBCResources();
        print 'Successfully Modified the JDBC resources'

        disconnect()
main()