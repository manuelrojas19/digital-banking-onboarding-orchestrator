package com.manuelr.banking.digital.onboarding

import com.manuelr.banking.digital.onboarding.config.TestContainersConfig
import com.manuelr.banking.digital.onboarding.model.dto.Credentials
import com.manuelr.banking.digital.onboarding.model.dto.UserInformation
import com.manuelr.banking.digital.onboarding.model.request.LoginRefreshTokenRequest
import com.manuelr.banking.digital.onboarding.model.request.LoginRequest
import com.manuelr.banking.digital.onboarding.model.request.OnboardRequest
import io.restassured.RestAssured
import io.restassured.RestAssured.given
import io.restassured.response.Response
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.assertNotNull
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.server.LocalServerPort
import java.time.LocalDate
import java.util.UUID

/**
 * Integration tests for the OnboardingApplication app.
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class OnboardingApplicationTests : TestContainersConfig() {

    @LocalServerPort
    private val port = 0

    /**
     * Initializes the base URI and port for RestAssured.
     */
    @BeforeEach
    fun setup() {
        RestAssured.baseURI = "http://localhost"
        RestAssured.port = port
    }

    /**
     * Given a call to signup endpoint
     * When user info is valid
     * Then should create user and return a 201 status code.
     */
    @Test
    @Order(1)
    fun `Signup Should Create User And Return Created Status Code When User Info Is Valid`() {
        val firstUser = OnboardRequest(
            deviceId = "Iphone 15 pro",
            origin = "Mobile",
            flowId = UUID.randomUUID().toString(),
            userInformation = UserInformation(
                citizenId = "123456789",
                taxId = "987654321",
                name = "John",
                lastName = "Doe",
                birthday = LocalDate.of(1990, 1, 1),
                email = "john.doe@example.com",
                genre = "Male",
                country = "USA",
                state = "California",
                postCode = "90001",
                phoneNumber = "123-456-7890"
            ),
            Credentials(
                username = "john.doe@example.com",
                password = "password"
            )
        )

        val secondUser = OnboardRequest(
            deviceId = "Samsung Galaxy S21",
            origin = "Mobile",
            flowId = UUID.randomUUID().toString(),
            userInformation = UserInformation(
                citizenId = "987654321",
                taxId = "123456789",
                name = "Manuel",
                lastName = "Gonzalez",
                birthday = LocalDate.of(1985, 5, 15),
                email = "manuel.gonzalez@example.com",
                genre = "Male",
                country = "Mexico",
                state = "Jalisco",
                postCode = "44100",
                phoneNumber = "321-654-9870"
            ),
            credentials = Credentials(
                username = "manuel.gonzalez@example.com",
                password = "securepassword"
            )
        )
        val usersToCreate = listOf(firstUser, secondUser)
        usersToCreate.forEach { user ->
            given()
                .contentType("application/json")
                .body(user)
                .`when`()
                .post("/api/v1/auth/sign-up")
                .then()
                .statusCode(201)
        }
    }

    /**
     * Given a call to signup endpoint
     * When user is already
     * Then should return a 409 status code.
     */
    @Test
    @Order(2)
    fun `Signup Should Not Create User And Return Conflict Status Code When User Is Already There`() {
        val request = OnboardRequest(
            deviceId = "Samsung Galaxy S21",
            origin = "Mobile",
            flowId = UUID.randomUUID().toString(),
            userInformation = UserInformation(
                citizenId = "987654321",
                taxId = "123456789",
                name = "Manuel",
                lastName = "Gonzalez",
                birthday = LocalDate.of(1985, 5, 15),
                email = "manuel.gonzalez@example.com",
                genre = "Male",
                country = "Mexico",
                state = "Jalisco",
                postCode = "44100",
                phoneNumber = "321-654-9870"
            ),
            credentials = Credentials(
                username = "manuel.gonzalez@example.com",
                password = "securepassword"
            )
        )
        given()
            .contentType("application/json")
            .body(request)
            .`when`()
            .post("/api/v1/auth/sign-up")
            .then()
            .statusCode(409)
    }

    /**
     * Given a call to login endpoint
     * When user credentials are valid
     * Then return a 200 status code.
     */
    @Test
    @Order(3)
    fun `Login Should Return Access Token When Credentials Are Valid`() {
        val accessToken = performLoginAndExtractFromResponse(
            "manuel.gonzalez@example.com",
            "securepassword",
            "access_token"
        )
        assertNotNull(accessToken)
        assert(accessToken.isNotBlank())
    }

    /**
     * Given a call to login endpoint
     * When user credentials are invalid
     * Then return a 401 Unauthorized status code.
     */
    @Test
    @Order(4)
    fun `Login Should Return Forbidden Response When Credentials Are Invalid`() {
        given()
            .contentType("application/json")
            .body(LoginRequest("manuel.gonzalez@example.com", "123465"))
            .`when`()
            .post("/api/v1/auth/login")
            .then()
            .statusCode(401)
    }


    /**
     * Given a call to refresh token endpoint
     * When a valid refresh token is provided
     * Then return a new access token.
     */
    @Test
    @Order(7)
    fun `Should Return Access Token When Refresh Token is Valid`() {
        val refreshToken = performLoginAndExtractFromResponse(
            "manuel.gonzalez@example.com",
            "securepassword",
            "refresh_token"
        )
        val response = given()
            .contentType("application/json")
            .body(LoginRefreshTokenRequest(refreshToken))
            .`when`()
            .post("/api/v1/auth/refresh-token")
            .then()
            .statusCode(200)
            .extract()
            .response()
        val accessToken = response.jsonPath().getString("access_token")
        assert(accessToken.isNotBlank())
    }

    private fun performLoginAndExtractFromResponse(email: String, password: String, param: String): String {
        return login(email, password).jsonPath().getString(param)
    }

    /**
     * Common method to perform login and obtain the access token.
     *
     * @param email    The user's email.
     * @param password The user's password.
     * @return The access token obtained after a successful login.
     */
    private fun login(email: String, password: String): Response {
        val request = LoginRequest(email, password)
        return given()
            .contentType("application/json")
            .body(request)
            .`when`()
            .post("/api/v1/auth/login")
            .then()
            .statusCode(200)
            .extract()
            .response()
    }
}