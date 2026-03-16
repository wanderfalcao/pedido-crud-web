# ─────────────────────────────────────────────────────────────────────────────
# Estágio 1 — Build
#   Usa a imagem oficial Maven + JDK 21 Alpine para compilar e empacotar
#   Dependências são baixadas antes de copiar o código-fonte, criando uma
#   camada de cache que acelera rebuilds quando só o código muda.
# ─────────────────────────────────────────────────────────────────────────────
FROM maven:3.9-eclipse-temurin-21-alpine AS build

WORKDIR /app

# 1. Copia só o pom.xml primeiro para cachear as dependências
COPY pom.xml .
RUN mvn dependency:go-offline -B -q

# 2. Copia o código-fonte e empacota sem rodar testes
COPY src ./src
RUN mvn package -DskipTests -B -q

# ─────────────────────────────────────────────────────────────────────────────
# Estágio 2 — Runtime
#   Usa apenas JRE (sem compilador/Maven), reduzindo a imagem final em ~60%.
#   Apenas o .jar gerado no estágio anterior é copiado.
# ─────────────────────────────────────────────────────────────────────────────
FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

# Copia o jar produzido no estágio de build
COPY --from=build /app/target/pedido-crud-web-*.jar app.jar

# Porta da aplicação
EXPOSE 8082

# Perfil ativo padrão para contêiner; pode ser sobrescrito via env var
ENV SPRING_PROFILES_ACTIVE=docker

ENTRYPOINT ["java", "-jar", "app.jar"]
