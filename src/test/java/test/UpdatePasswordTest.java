package test;

import com.github.javafaker.Faker;
import dto.LoginRequest;
import dto.PasswordUpdateRequest;
import dto.RegisterRequest;
import dto.UserResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class UpdatePasswordTest extends BaseTest {
    @Test @org.testng.annotations.Test
    public void successUpdatePasswordTest() {
        Faker faker = new Faker();
        String email = faker.internet().emailAddress();

        RegisterRequest registerRequest = RegisterRequest.builder()
                .email(email)
                .password("User1234")
                .confirmPassword("User1234")
                .role("user")
                .build();

        Response registerUser = postRequest(REG_PATH, 201, registerRequest);
        String accessToken = registerUser.jsonPath().getString("accessToken");

        PasswordUpdateRequest updateRequest = PasswordUpdateRequest.builder()
                .currentPassword("User1234")
                .newPassword("User12345")
                .confirmPassword("User12345")
                .build();
        putRequest(UPDATE_PASSWORD_PATH, 200, updateRequest, accessToken);
        Response authResponse = postRequest(AUTH_PATH, 200, new LoginRequest(email, "User12345"));
        assertEquals(200, authResponse.getStatusCode(), "Failed to login with updated password");
    }
    @Test @org.testng.annotations.Test
    public void updateWithInvalidPasswordTest() {
        Faker faker = new Faker();
        String email = faker.internet().emailAddress();

        RegisterRequest registerRequest = RegisterRequest.builder()
                .email(email)
                .password("User1234")
                .confirmPassword("User1234")
                .role("user")
                .build();

        Response registerUser = postRequest(REG_PATH, 201, registerRequest);
        String accessToken = registerUser.jsonPath().getString("accessToken");

        PasswordUpdateRequest updateRequest = PasswordUpdateRequest.builder()
                .currentPassword("User1234")
                .newPassword("User")
                .confirmPassword("User")
                .build();
        Response updateResponse = putRequest(UPDATE_PASSWORD_PATH, 400, updateRequest, accessToken);
        String errorText = updateResponse.getBody().jsonPath().getString("newPassword");
        assertTrue(errorText.contains("Password must contain letters and numbers") || errorText.contains("Password must contain at least 8 characters"));
    }
    @Test @org.testng.annotations.Test
    public void unauthorizedUpdatePasswordTest() {
        registerUser(201);
        PasswordUpdateRequest updateRequest = PasswordUpdateRequest.builder()
                .currentPassword("User1234")
                .newPassword("User12345")
                .confirmPassword("User12345")
                .build();

        Response updateResponse = putRequest(UPDATE_PASSWORD_PATH, 401, updateRequest, null);

        String errorText = updateResponse.getBody().jsonPath().getString("message");
        assertEquals("Unauthorized", errorText);
    }
}
