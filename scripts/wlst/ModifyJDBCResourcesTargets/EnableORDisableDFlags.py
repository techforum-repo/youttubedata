import re
from java.io import FileInputStream

def EnableORDisableDFlags():

	edit()
	startEdit()
	propInputStream = FileInputStream('EnableORDisableDFlags.properties')
	configProps = Properties()
	configProps.load(propInputStream)  
	
	serverflagconfigs = re.split(",",configProps.get("serverflagconfigs"))
	print serverflagconfigs
	
	for serverConfig in serverflagconfigs:	
	
		 serverName=re.split("=",serverConfig)[0]
		 config=re.split("=",serverConfig)[1]
		 print config
		 cd('/Servers/'+serverName+'/ServerDebug/'+serverName)
		 flagName=re.split(":",config)[0]
		 flagValue=re.split(":",config)[1]
		 set(flagName,flagValue)
		 print 'Modified the DFlag for '+serverName+' '+ flagName+':'+flagValue
	save()
	activate()	 
		 
		
def main():
    adminURL='t3://localhost:7001'
    adminUserName='weblogic'
    adminPassword='weblogic1'
    connect(adminUserName, adminPassword, adminURL)
    EnableORDisableDFlags();
    print 'Successfully Modified the DFlags'
    disconnect()
main()