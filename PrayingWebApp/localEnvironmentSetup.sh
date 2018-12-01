echo "Hint: run script with 'source localEnvironmentSetup.sh'"
echo "This script prepares the current shell's environment variables (not permanently)"

# Used for backing services like the PostgreSQL database
export VCAP_APPLICATION={}
export VCAP_SERVICES='{"mysql-8.0.11":[{"name":"mysql-instance","label":"mysql-8.0.11","credentials":{"dbname":"PrayingSystem","hostname":"127.0.0.1","password":"ES6wfC42%d","port":"3306","uri":"jdbc:mysql://localhost:3306/PrayingSystem","username":"WebAppUser"},"tags":["relational","mysql"],"plan":"free"}]}'

# Used for dependent service call
#export USER_ROUTE=https://opensapcp5userservice.cfapps.eu10.hana.ondemand.com

# Overwrite logging library defaults
export APPENDER=STDOUT
export LOG_APP_LEVEL=INFO

echo \$VCAP_SERVICES=$VCAP_SERVICES
