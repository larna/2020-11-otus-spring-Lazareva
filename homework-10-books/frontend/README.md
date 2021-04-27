# books

## Project setup
```
yarn install
```

### Compiles and hot-reloads for development
```
yarn serve
```

### Compiles and minifies for production
```
yarn build
```

### Lints and fixes files
```
yarn lint
```

### Customize configuration
See [Configuration Reference](https://cli.vuejs.org/config/).

###Создание docker образа
docker build -t book-frontend:latest .
###Запуск docker контейнера
docker run -d -p 8080:8080 book-frontend:latest



