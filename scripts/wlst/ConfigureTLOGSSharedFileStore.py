import sys

print "@@@ Starting the script ..."

from java.util import *
from javax.management import *
from java.io import FileInputStream

print "@@@ Starting the script ..."
global props
 
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

adminserverDir = File(wlsDomain+'/cluster1/tlogs')
bool = adminserverDir.mkdirs()

cd('/Servers/MS1/DefaultFileStore/MS1')
cmo.setDirectory(wlsDomain+'/cluster_1/tlogs')

cd('/Servers/MS2/DefaultFileStore/MS2')
cmo.setDirectory(wlsDomain+'/cluster_1/tlogs')

cd('/Servers/MS3/DefaultFileStore/MS3')
cmo.setDirectory(wlsDomain+'/cluster_1/tlogs')

save()
activate()