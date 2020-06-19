from java.io import FileInputStream

propInputStream = FileInputStream("domain_log.properties")
configProps = Properties()
configProps.load(propInputStream)

rotationType=configProps.get("rotationType")
fileMinSize=configProps.get("fileMinSize")
rotateLogOnStartup=configProps.get("rotateLogOnStartup")
fileCount=configProps.get("fileCount")
log4jEnabled = configProps.get("log4jEnabled")
stdoutSeverity = configProps.get("stdoutSeverity")
logBRSeverity = configProps.get("logBRSeverity")
logFileSeverity = configProps.get("logFileSeverity")
memBufferSeverity = configProps.get("memBufferSeverity")
memBufferSize = configProps.get("memBufferSize")
numOfFilesLimited = configProps.get("numOfFilesLimited")
redirectStdout = configProps.get("redirectStdout")
redirectStdErr = configProps.get("redirectStdErr")
rotateOnStartup = configProps.get("rotateOnStartup")
rotateType = configProps.get("rotateType")
httpAccessLoggingEnabled = configProps.get("httpAccessLoggingEnabled")

domainAdminUrl = configProps.get("domain.admin.url")
folderPath=configProps.get("domain.folderPath")
domainAdminUserName = configProps.get("domain.admin.username")
domainAdminPassword = configProps.get("domain.admin.password")

connect(domainAdminUserName, domainAdminPassword, domainAdminUrl)
servers = cmo.getServers()
for s in servers:
 edit()
 startEdit()
 serverName1 = s.getName()
 print '----------------------------------------------------'
 print 'Changing Log Setting for serverName: ' , serverName1
 print '----------------------------------------------------'
 cd('/Servers/' + serverName1 + '/Log/' + serverName1)
 print "Original FileCount is " ,get("FileCount")
 print "Setting FileCount to be " , fileCount
 set("FileCount", int(fileCount))
 print "Original FileMinSize is " , get("FileMinSize")
 print "Setting FileMinSize to be " , fileMinSize
 set("FileMinSize", int(fileMinSize))
 print "Original FileName is " , get("FileName")
 print "Setting FileName to be " + folderPath+ "/logs/"+serverName1+ "/" + serverName1 + ".log"

 set("FileName", folderPath+ '/logs/'+serverName1+ "/"  + serverName1 + '.log')
 
 print "Original FileTimeSpan is " , get("FileTimeSpan")
 #print "Setting FileTimeSpan to be " , fileTimeSpan
 #set("FileTimeSpan", fileTimeSpan)
 
 print "Original Log4jEnabled is " , get("Log4jLoggingEnabled")
 print "Setting Log4jLoggingEnabled to be " , log4jEnabled
 set("Log4jLoggingEnabled", log4jEnabled)
 
 print "Original StdoutSeverity is " , get("StdoutSeverity")
 print "Setting StdoutSeverity to be " , stdoutSeverity
 set("StdoutSeverity", stdoutSeverity)
 
 print "Original DomainLogBroadcastSeverity is " , get("DomainLogBroadcastSeverity")
 print "Setting DomainLogBroadcastSeverity to be " , logBRSeverity
 set("DomainLogBroadcastSeverity", logBRSeverity)
 
 print "Original LogFileSeverity is " , get("LogFileSeverity")
 print "Setting LogFileSeverity to be " , logFileSeverity
 set("LogFileSeverity", logFileSeverity)
 
 print "Original MemoryBufferSeverity is " , get("MemoryBufferSeverity")
 print "Setting MemoryBufferSeverity to be " , memBufferSeverity
 set("MemoryBufferSeverity", memBufferSeverity)
 
 print "Original MemoryBufferSize is " , get("MemoryBufferSize")
 print "Setting MemoryBufferSize to be " , memBufferSize
 set("MemoryBufferSize", int(memBufferSize))
 
 print "Original NumberOfFilesLimited is " , get("NumberOfFilesLimited")
 print "Setting NumberOfFilesLimited to be " , numOfFilesLimited
 set("NumberOfFilesLimited", numOfFilesLimited)
 
 print "Original RedirectStdoutToServerLogEnabled is " , get("RedirectStdoutToServerLogEnabled")
 print "Setting RedirectStdoutToServerLogEnabled to be " , redirectStdout
 set("RedirectStdoutToServerLogEnabled", redirectStdout)
 
 print "Original RedirectStderrToServerLogEnabled is " , get("RedirectStderrToServerLogEnabled")
 print "Setting RedirectStderrToServerLogEnabled to be " , redirectStdErr
 set("RedirectStderrToServerLogEnabled", redirectStdErr)

 print "Original RotateLogOnStartup is " , get("RotateLogOnStartup")
 print "Setting RotateLogOnStartup to be " , rotateOnStartup
 set("RotateLogOnStartup", rotateOnStartup)

 print "Original RotationType is " , get("RotationType")
 print "Setting RotationType to be " , rotateType
 set("RotationType", rotateType)

 print '===> Log Setting for serverName: ' , serverName1, ' has been changed Successfully !!'
 print ''
 
 #Webserver Http Access Log
 print '===> Log Setting changes for Http Access Log: '
 cd('/Servers/'+serverName1+'/WebServer/'+serverName1+'/WebServerLog/'+serverName1)
 
 set("FileName", folderPath+ '/logs/'+serverName1+ "/"  + 'accesss.log')
 print "Original Http Access LoggingEnabled is " , get("LoggingEnabled")
 print "Setting Http Access LoggingEnabled to be " , httpAccessLoggingEnabled
 set("LoggingEnabled", httpAccessLoggingEnabled)
 
 print "Original NumberOfFilesLimited is " , get("NumberOfFilesLimited")
 print "Setting NumberOfFilesLimited to be " , numOfFilesLimited
 set("NumberOfFilesLimited", numOfFilesLimited)
 
 print "Original RotateLogOnStartup is " , get("RotateLogOnStartup")
 print "Setting RotateLogOnStartup to be " , rotateOnStartup
 set("RotateLogOnStartup", rotateOnStartup)

 print "Original RotationType is " , get("RotationType")
 print "Setting RotationType to be " , rotateType
 set("RotationType", rotateType)
 
 save()
 activate()

 print '===> Log Settings for serverName: ' , serverName1, ' has been changed Successfully !!' 

print ''