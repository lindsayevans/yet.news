# yet.news

```sh
open http://localhost:8080/
./mvnw spring-boot:run -Dspring-boot.run.profiles=local
```

```sh
docker build --platform linux/amd64 -t yet-news .
docker run --platform linux/amd64 --rm -it yet-news ls -la /app
docker run --platform linux/amd64 -p 80:8080 --rm yet-news
```
