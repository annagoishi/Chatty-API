package test;

import dto.UserResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class GetMyInfoTest extends BaseTest {
    @Test @org.testng.annotations.Test
    public void getUserInfoTest() {
        String accessToken = getAdminAccessToken();
        Response userInfo = getRequestWithAccessToken(ME_PATH, 200, accessToken);
        UserResponse userResponse = userInfo.as(UserResponse.class);
        assertEquals("qwerty@gm.com", userResponse.getEmail());
    }
    @Test @org.testng.annotations.Test
    public void unauthorizedGetUserInfoTest() {
        Response userInfo = getRequestWithAccessToken(ME_PATH, 401, "");
        assertEquals(401, userInfo.getStatusCode());
        String status = userInfo.getBody().jsonPath().getString("httpStatus");
        assertEquals("UNAUTHORIZED", status);
    }
}
