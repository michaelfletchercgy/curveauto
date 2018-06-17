FROM openjdk:8-jdk-alpine AS builder

WORKDIR /build

# Copy all of the gradle build parts so we can do a trivial build that will capture all of the dependencies.
COPY gradlew .
COPY gradle ./gradle
COPY build.gradle .
COPY settings.gradle .

RUN ./gradlew assemble

# Now copy everything else in
COPY . .

# And perform a proper build
RUN ./gradlew distZip

RUN unzip -qq build/distributions/curveauto

FROM openjdk:8-alpine

COPY --from=builder /build/curveauto /app
COPY --from=builder /build/web /app/app/web

RUN ls /app/app/web/

WORKDIR /app

ENTRYPOINT ["/app/bin/curveauto"]