package test;

import dto.UserResponseForAdmin;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class GetAllUsersTest extends BaseTest {
    @Test
    public void getAllUsersSuccessTest() {
        String accessAdminToken = getAdminAccessToken();

        Response getUsersResponse = getRequestWithAccessToken(GET_USERS_PATH, 200, accessAdminToken);

        assertEquals(200, getUsersResponse.getStatusCode(), "Get all users request should return status code 200");
        List<UserResponseForAdmin> userList = getUsersResponse.jsonPath().getList("", UserResponseForAdmin.class);

        assertFalse(userList.isEmpty(), "List of users should not be empty");

        UserResponseForAdmin firstUser = userList.get(0);
        assertNotNull(firstUser.getEmail(), "First user's email should not be null");
    }

    @Test
    public void getAllUsersUnauthorizedTest() {
        Response getUsersResponse = getRequestWithAccessToken(GET_USERS_PATH, 401, "");

        assertEquals(401, getUsersResponse.getStatusCode(), "Get all users request without access token should return status code 401");
        String errorText = getUsersResponse.getBody().jsonPath().getString("message");
        assertEquals("Authentication failed: Full authentication is required to access this resource", errorText);
    }

}
