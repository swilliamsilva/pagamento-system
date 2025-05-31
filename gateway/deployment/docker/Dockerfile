# Imagem base
FROM eclipse-temurin:17-jdk-alpine

# Argumentos de build
ARG JAR_FILE=gateway-service-*.jar
ARG APP_PORT=8080

# Configuração do ambiente
ENV SPRING_PROFILES_ACTIVE=prod
ENV JAVA_OPTS="-Xms256m -Xmx512m -XX:+UseG1GC"

# Diretório de trabalho
WORKDIR /app

# Copiar artefato
COPY target/$JAR_FILE app.jar

# Expor porta
EXPOSE $APP_PORT

# Health check
HEALTHCHECK --interval=15s --timeout=3s \
    CMD curl -f http://localhost:$APP_PORT/actuator/health || exit 1

# Comando de execução
ENTRYPOINT exec java $JAVA_OPTS -jar app.jar
