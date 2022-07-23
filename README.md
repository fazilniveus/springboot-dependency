# Sample Spring boot application


mvn clean build
mvn test -Dtest=TestControllerTests

docker build -t gcr.io/tech-rnd-project/springcicddemo:1.0.0 .
docker push gcr.io/tech-rnd-project/springcicddemo:1.0.0
docker run --name springcicddemo -p 9090:9090 gcr.io/tech-rnd-project/springcicddemo:1.0.0




