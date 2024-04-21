package test;

import dto.LoginRequest;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class LoginUserTest extends BaseTest{
    @Test @org.testng.annotations.Test
    public void successAuthenticationTest() {
        LoginRequest loginRequest = LoginRequest.builder()
                .email("qwerty@gm.com")
                .password("Sh123456")
                .build();

        Response authResponse = postRequest(AUTH_PATH, 200, loginRequest);

        assertTrue(authResponse.getBody().asString().contains("accessToken"));
        assertTrue(authResponse.getBody().asString().contains("refreshToken"));
    }

    @Test @org.testng.annotations.Test
    public void invalidPasswordAuthTest() {
        LoginRequest loginRequest = LoginRequest.builder()
                .email("qwerty@gm.com")
                .password("User12345")
                .build();
        Response authWithWrongPasswordResponse = postRequest(AUTH_PATH, 401, loginRequest);
        assertEquals(401, authWithWrongPasswordResponse.getStatusCode());
        assertTrue(authWithWrongPasswordResponse.getBody().asString().contains("The password does not match"));
    }

    @Test @org.testng.annotations.Test
    public void loginWithoutPassword() {
        LoginRequest loginRequest = LoginRequest.builder()
                .email("qwerty@gm.com")
                .password("")
                .build();
        Response authResponse = postRequest(AUTH_PATH, 400, loginRequest);

        assertEquals(400, authResponse.getStatusCode());
        assertTrue(authResponse.getBody().asString().contains("Password cannot be empty"));
    }
}
