import sys


print "@@@ Starting the script ..."

from java.util import *
from javax.management import *
from java.io import FileInputStream

#The directory of the domain configuration

DOMAIN_PATH= 'C://Albin/SW/Oracle/Middleware/Oracle_Home/user_projects/domains/base_domain'
print 'reading domain from '+DOMAIN_PATH


readDomain(DOMAIN_PATH)

cd('/')
applyJRF('Cluster1', DOMAIN_PATH)
print 'Successfully updated domain.'
updateDomain()
closeDomain()

exit()