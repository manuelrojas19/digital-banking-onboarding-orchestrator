/**
 * This package contains configuration classes for setting up TestContainers in the project.
 * TestContainers is a Java library that provides lightweight, throwaway instances of common databases,
 * Selenium web browsers, or anything else that can run in a Docker container.
 *
 * The primary class in this package is {@link TestContainersConfig} which initializes and manages
 * a Docker container for Keycloak. Keycloak is an open-source Identity and Access Management solution.
 *
 * It uses the TestContainers library to start and stop a Docker container during test setup and teardown.
 * The container runs Keycloak with a predefined realm configuration loaded from a JSON file.
 *
 * The package includes the following classes:
 * - {@link TestContainersConfig}: The main configuration class responsible for starting and stopping
 *   the Keycloak Docker container, and providing dynamic properties for the application to connect to Keycloak.
 *
 * The {@link TestContainersConfig} class includes the following methods:
 * - {@code startKeycloakContainer()}: Method annotated with {@link BeforeAll} to start the Keycloak Docker container.
 * - {@code stopKeycloakContainer()}: Method annotated with {@link AfterAll} to stop the Keycloak Docker container.
 * - {@code registerResourceServerIssuerProperty(DynamicPropertyRegistry)}: Method annotated with
 *   {@link DynamicPropertySource} to register dynamic properties for the application to connect to Keycloak.
 * - {@code dbContainer is running()}: Test method to assert whether the Keycloak Docker container is running.
 *
 * The Docker container is configured with Keycloak version 23.0.6 and a predefined realm exported to a JSON file.
 * The container exposes port 8080, and the application dynamically registers the Keycloak URL for the tests.
 *
 * Usage:
 * - {@code @TestContainers(disabledWithoutDocker = true)}: Class-level annotation to enable TestContainers only if Docker is available.
 * - {@code @Container}: Annotation to declare a container field.
 * - {@code @BeforeAll}: Annotation for a method to be executed before all test methods.
 * - {@code @AfterAll}: Annotation for a method to be executed after all test methods.
 * - {@code @DynamicPropertySource}: Annotation for a method providing dynamic properties for Spring.
 *
 * Example:
 * <pre>{@code
 *   &#64;TestContainers(disabledWithoutDocker = true)
 *   open class TestContainersConfig {
 *
 *       // ... (see the class implementation for details)
 *   }
 * }</pre>
 */
package com.manuelr.banking.digital.onboarding.config

import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.slf4j.LoggerFactory
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.testcontainers.containers.BindMode
import org.testcontainers.containers.GenericContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers
import org.testcontainers.utility.DockerImageName
import java.time.Duration
import java.time.Instant

@Testcontainers(disabledWithoutDocker = true)
open class TestContainersConfig {

    companion object {

        private val log = LoggerFactory.getLogger(this::class.java)
        private val start: Instant = Instant.now()

        private val keycloakImageName: DockerImageName = DockerImageName
                .parse("quay.io/keycloak/keycloak:23.0.6")

        @Container
        val keycloak: GenericContainer<*> = GenericContainer(
                keycloakImageName
        )
                .withExposedPorts(8080)
                .withClasspathResourceMapping("keycloak/realm-export.json",
                        "/opt/keycloak/data/import/realm.json",
                        BindMode.READ_ONLY)
                .withCommand("start-dev", "--import-realm")
                .withReuse(true)

        @BeforeAll
        @JvmStatic
        fun startKeycloakContainer() {
            keycloak.start()
            log.info("🐳TestContainers started in {}", Duration.between(start, Instant.now()));
        }

        @AfterAll
        @JvmStatic
        fun stopKeycloakContainer() {
            keycloak.stop()
            log.info("🐳TestContainers stopped in {}", Duration.between(start, Instant.now()));
        }

        @DynamicPropertySource
        @JvmStatic
        fun registerResourceServerIssuerProperty(registry: DynamicPropertyRegistry) {
            val host = keycloak.host
            val mappedPort = keycloak.getMappedPort(8080)
            registry.add("app.config.keycloak.url") { "http://$host:$mappedPort" }
        }
    }

    @Test
    fun `dbContainer is running`() {
        Assertions.assertTrue(keycloak.isRunning)
    }

}