package test;

import dto.UserResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static test.BaseTest.*;

public class DeleteUserTest {
    @Test @org.testng.annotations.Test
    public void deleteUserTest() {
        Response registerUser = registerUser(201);
        String accessToken = registerUser.jsonPath().getString("accessToken");

        Response userInfo = getRequestWithAccessToken(ME_PATH, 200, accessToken);
        UserResponse userResponse = userInfo.as(UserResponse.class);
        String id = userResponse.getId();

        String accessAdminToken = getAdminAccessToken();

        Response deleteUserResponse = deleteRequest(DELETE_USER_PATH + id, 200, accessAdminToken);
        assertEquals(204, deleteUserResponse.getStatusCode(), "User deletion failed");
        Response getUserInfoAfterDeletion = getRequestWithAccessToken(ME_PATH, 404, accessToken);
        String errorText = getUserInfoAfterDeletion.getBody().jsonPath().getString("message");
        assertEquals("User not found!", errorText);
    }

    @Test @org.testng.annotations.Test
    public void unauthorizedDeleteUserTest() {
        Response registerUser = registerUser(201);
        String accessToken = registerUser.jsonPath().getString("accessToken");

        Response userInfo = getRequestWithAccessToken(ME_PATH, 200, accessToken);
        UserResponse userResponse = userInfo.as(UserResponse.class);
        String id = userResponse.getId();

        Response deleteUserResponse = deleteRequest(DELETE_USER_PATH + id, 401, null);
        String errorText = deleteUserResponse.getBody().jsonPath().getString("message");
        assertEquals("Unauthorized", errorText);
    }
}
