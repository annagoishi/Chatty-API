package test;

import dto.RefreshTokenRequest;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static test.BaseTest.*;

public class RefreshTokenTest {
    @Test
    public void successRefreshTokenTest() {

        Response authResponse = registerUser(201);
        String refreshToken = authResponse.jsonPath().getString("refreshToken");

        RefreshTokenRequest refreshTokenRequest = RefreshTokenRequest.builder()
                .refreshToken(refreshToken)
                .build();

        Response refreshResponse = postRequest(REFRESH_PATH, 201, refreshTokenRequest);

        assertEquals(201, refreshResponse.getStatusCode(), "Refresh token request should return status code 200");

        String newAccessToken = refreshResponse.jsonPath().getString("accessToken");
        assertTrue(newAccessToken != null && !newAccessToken.isEmpty(), "New access token should not be null or empty");
    }

    @Test
    public void refreshTokenUnauthorizedTest() {
        RefreshTokenRequest refreshTokenRequest = RefreshTokenRequest.builder()
                .refreshToken("invalid_refresh_token")
                .build();

        Response refreshResponse = postRequest(REFRESH_PATH, 401, refreshTokenRequest);

        assertEquals(401, refreshResponse.getStatusCode(), "Refresh token request without refresh token should return status code 401");
        String errorText = refreshResponse.getBody().jsonPath().getString("message");
        assertEquals("Unauthorized", errorText);
    }
}
