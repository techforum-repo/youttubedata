adminIP = raw_input("Enter domain1.AdminIP:")
adminPort = raw_input("Enter domain1.AdminPort:")
adminPassword = raw_input("Enter adminPassword:")
DBPASSWORD= raw_input("Enter new DBPASSWORD:")
DOMAIN_PATH='C://Albin/SW/Oracle/Middleware/Oracle_Home/user_projects/domains/base_domain'
es = encrypt(DBPASSWORD,DOMAIN_PATH)
adminURL='t3://'+adminIP+':'+adminPort
adminUserName='weblogic'
connect(adminUserName, adminPassword, adminURL)
server='AdminServer'
cd('Servers/'+server)
target=cmo
edit()
startEdit()

# SOADomain Datasource Configuration
cd('JDBCSystemResources')
allDS=cmo.getJDBCSystemResources()
for tmpDS in allDS:
    dsName=tmpDS.getName();
    print  'Changing the Password for DataSource ', dsName
    cd('/JDBCSystemResources/'+dsName+'/JDBCResource/'+dsName+'/JDBCDriverParams/'+dsName)
    set('PasswordEncrypted',es)
save()
activate()
disconnect()