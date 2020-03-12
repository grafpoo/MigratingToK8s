docker run -it -d \
      -e MYSQL_USER=empuser \
      -e MYSQL_PASSWORD=password \
      -e MYSQL_DATABASE=profiles \
      -e MYSQL_ALLOW_EMPTY_PASSWORD=true \
      -p 3306:3306 \
      --name mymysql \
      mysql
