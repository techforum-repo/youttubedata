import sys

print "@@@ Starting the script ..."

from java.util import *
from javax.management import *

#The directory of the domain configuration
#/app/oracle/products/11g/admin/domains
wlsDomain='C://Albin/SW/Oracle/Middleware/Oracle_Home/user_projects/domains/base_domain'
print "WLSDOMAIN="+wlsDomain


adminURL='t3://localhost:7001'
adminUserName='weblogic'
adminPassword='weblogic1'
connect(adminUserName, adminPassword, adminURL)
edit()
startEdit()
############# JDBC stores for STANDALONE ADMINSERVER ## Enable and target unique JDBC Store for all the servers in the domain
cd('/')
cmo.createJDBCStore('BAMMonitoringJMSJDBCStore')
cd('/JDBCStores/BAMMonitoringJMSJDBCStore')
cmo.setDataSource(getMBean('/SystemResources/JDBCStoreDataSource'))
cmo.setPrefixName('bammonitoring')
set('Targets',jarray.array([ObjectName('com.bea:Name=AdminServer,Type=Server')], ObjectName))

#### Add 
#### end of creating jdbc stores

############Set Persistent Stores for the sub systems e.g JMS Servers

cd('/JMSServers/BAMMonitoringServer')
cmo.setPersistentStore(getMBean('/JDBCStores/BAMMonitoringJMSJDBCStore'))


#################DESTROY ALL THE EXISTING FILE STORES 

cd('/')
cmo.destroyFileStore(getMBean('/FileStores/BAMMonitoringJMSFileStore'))
 
save()
activate()
