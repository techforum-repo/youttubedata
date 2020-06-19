  
def updateGraceFulShutdownTimings():
    edit()
    startEdit()
    print
    print '##############'
    print '# Update Graceful Shutdown to 300 Seconds and ignore session true#'
    print '##############'
    print
    serverNames=cmo.getServers()
    for name in serverNames:
        try:
            print 'Updating Server==>'+ name.getName()
            cd("/Servers/" + name.getName())
            cmo.setGracefulShutdownTimeout(300)
            cmo.setIgnoreSessionsDuringShutdown(true)
        except WLSTException,e:
            # this typically means the server is not active, just ignore
            print 'Exception While Update the attribute'
    print '========================================='
    save()
    activate()  

  
def main():
	adminURL='t3://localhost:7001'
	adminUserName='weblogic'
	adminPassword='weblogic1'
	connect(adminUserName, adminPassword, adminURL)
	updateGraceFulShutdownTimings()
	print 'Successfully updated Graceful Shutdown Parameters'
	disconnect()
main()