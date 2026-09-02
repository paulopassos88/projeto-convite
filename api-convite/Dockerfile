# ==========================================
# 1. Estágio de Build
# ==========================================
FROM eclipse-temurin:25-jdk-jammy AS build
WORKDIR /app

# Copia a estrutura de build e dependências
COPY gradlew .
COPY gradle gradle
COPY build.gradle .
COPY settings.gradle .

# Garante permissão de execução e pré-aquece o cache de dependências
RUN chmod +x gradlew && ./gradlew dependencies --no-daemon

# Copia o código-fonte e compila apenas o fat JAR
COPY src src
RUN ./gradlew bootJar -x test --no-daemon

# ==========================================
# 2. Estágio de Execução
# ==========================================
FROM eclipse-temurin:25-jre-jammy
WORKDIR /app

# Cria usuário não-root para segurança
RUN addgroup --system spring && adduser --system spring --ingroup spring
USER spring:spring

# Copia o JAR garantindo que o usuário 'spring' seja o dono
COPY --chown=spring:spring --from=build /app/build/libs/*[!plain].jar app.jar

# Porta padrão de aplicações Spring Boot
EXPOSE 8080

# Flags de JVM otimizadas para limites de memória em containers
ENTRYPOINT ["java", "-XX:+UseContainerSupport", "-XX:MaxRAMPercentage=75.0", "-Djava.security.egd=file:/dev/./urandom", "-jar", "app.jar"]