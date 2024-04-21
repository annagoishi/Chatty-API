package test;

import com.github.javafaker.Faker;
import io.restassured.response.Response;
import dto.RegisterRequest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class RegistrationUserTest extends BaseTest {
    @Test @org.testng.annotations.Test
    public void successRegistrationTest() {
        Response registerUser = registerUser(200);
        assertEquals(200, registerUser.getStatusCode());
        assertTrue(registerUser.getBody().asString().contains("accessToken"));
        assertTrue(registerUser.getBody().asString().contains("refreshToken"));
    }

    @Test @org.testng.annotations.Test
    public void registrationWithoutPasswordDataTest() {
        Faker faker = new Faker();
        String email = faker.internet().emailAddress();

        RegisterRequest registerRequest = RegisterRequest.builder()
                .email(email)
                .password("")
                .confirmPassword("")
                .role("user")
                .build();

        Response registerResponse = postRequest(REG_PATH, 400, registerRequest);

        assertEquals(400, registerResponse.getStatusCode());
        assertTrue(registerResponse.getBody().asString().contains("Password cannot be empty")
                && registerResponse.getBody().asString().contains("Password must contain at least 8 characters")
                && registerResponse.getBody().asString().contains("Password must contain letters and numbers"));
    }
}
