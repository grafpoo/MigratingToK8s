## Create running instance of MySQL in docker
```
docker run --name mysql -p 3306:3306 -e MYSQL_ROOT_PASSWORD=password -d mysql/mysql-server:latest
```

## Connect to the docker instance (to run *mysql* command line interface)
```
docker exec -it <34879c7c6016> /bin/bash
```

## Creat database and user in this MySQL (using passowrd *password* configured above)
```
mysql -u root -p <<EOF
create database profiles;
CREATE USER empuser@'localhost' IDENTIFIED BY 'password';
CREATE USER empuser@'%' IDENTIFIED BY 'password';
ALTER USER 'empuser'@'localhost' IDENTIFIED WITH mysql_native_password BY 'password';
ALTER USER 'empuser'@'%' IDENTIFIED WITH mysql_native_password BY 'password';
GRANT ALL PRIVILEGES ON profiles.* to empuser@'localhost';
GRANT ALL PRIVILEGES ON profiles.* to empuser@'%';
EOF
```
