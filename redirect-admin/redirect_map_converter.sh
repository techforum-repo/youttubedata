#!/bin/bash
export PATH="/usr/local/sbin:/usr/local/bin:/usr/sbin:/usr/bin:/sbin:/bin"

#### Test Site1 Configuration Starts ###

##### Test Site1 301 ###
wget http://localhost:4502/content/redirectmanager/test-site1/_jcr_content.301.txt -O /tmp/redirectmap-testsite1-301.txt > /var/log/update-redirect-map-testsite1-301.log 2>&1
httxt2dbm -v -f db -i /tmp/redirectmap-testsite1-301.txt -o /etc/httpd/conf.dispatcher.d/redirectmaps/redirectmap-testsite1-301_temp.db >> /var/log/update-redirect-map-testsite1-301.log 2>&1

cp -rf  /etc/httpd/conf.dispatcher.d/redirectmaps/redirectmap-testsite1-301.db /etc/httpd/conf.dispatcher.d/redirectmaps/redirectmap-testsite1-301.db_`date -I`
mv  /etc/httpd/conf.dispatcher.d/redirectmaps/redirectmap-testsite1-301_temp.db  /etc/httpd/conf.dispatcher.d/redirectmaps/redirectmap-testsite1-301.db


##### Test Site1 302 ###

wget http://localhost:4502/content/redirectmanager/test-site1/_jcr_content.302.txt -O /tmp/redirectmap-testsite1-302.txt > /var/log/update-redirect-map-testsite1-302.log 2>&1
httxt2dbm -v -f db -i /tmp/redirectmap-testsite1-302.txt -o /etc/httpd/conf.dispatcher.d/redirectmaps/redirectmap-testsite1-302_temp.db >> /var/log/update-redirect-map-testsite1-302.log 2>&1

cp -rf  /etc/httpd/conf.dispatcher.d/redirectmaps/redirectmap-testsite1-302.db /etc/httpd/conf.dispatcher.d/redirectmaps/redirectmap-testsite1-302.db_`date -I`
mv  /etc/httpd/conf.dispatcher.d/redirectmaps/redirectmap-testsite1-302_temp.db  /etc/httpd/conf.dispatcher.d/redirectmaps/redirectmap-testsite1-302.db

#### Test Site1 Configuration Ends ###


#### Test Site2 Configuration Starts ###


##### Test Site2 301 ###
wget http://localhost:4502/content/redirectmanager/test-site2/_jcr_content.301.txt -O /tmp/redirectmap-testsite2-301.txt > /var/log/update-redirect-map-testsite2-301.log 2>&1
httxt2dbm -v -f db -i /tmp/redirectmap-testsite2-301.txt -o /etc/httpd/conf.dispatcher.d/redirectmaps/redirectmap-testsite2-301_temp.db >> /var/log/update-redirect-map-testsite2-301.log 2>&1

cp -rf  /etc/httpd/conf.dispatcher.d/redirectmaps/redirectmap-testsite2-301.db /etc/httpd/conf.dispatcher.d/redirectmaps/redirectmap-testsite2-301.db_`date -I`
mv  /etc/httpd/conf.dispatcher.d/redirectmaps/redirectmap-testsite2-301_temp.db  /etc/httpd/conf.dispatcher.d/redirectmaps/redirectmap-testsite2-301.db



##### Test Site2 302 ###

wget http://localhost:4502/content/redirectmanager/test-site2/_jcr_content.302.txt -O /tmp/redirectmap-testsite2-302.txt > /var/log/update-redirect-map-testsite2-302.log 2>&1
httxt2dbm -v -f db -i /tmp/redirectmap-testsite2-302.txt -o /etc/httpd/conf.dispatcher.d/redirectmaps/redirectmap-testsite2-302_temp.db >> /var/log/update-redirect-map-testsite2-302.log 2>&1

cp -rf  /etc/httpd/conf.dispatcher.d/redirectmaps/redirectmap-testsite2-302.db /etc/httpd/conf.dispatcher.d/redirectmaps/redirectmap-testsite2-302.db_`date -I`
mv  /etc/httpd/conf.dispatcher.d/redirectmaps/redirectmap-testsite2-302_temp.db  /etc/httpd/conf.dispatcher.d/redirectmaps/redirectmap-testsite2-302.db

#### Test Site2 Configuration Starts ###