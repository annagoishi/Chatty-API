package test;

import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class DeletePostTest extends BaseTest{
    @Test @org.testng.annotations.Test
    public void successDeletePostTest() {
        Response createPostResponse = createPost(201);
        String postId = createPostResponse.jsonPath().getString("id");

        String accessAdminToken = getAdminAccessToken();
        Response deletePostResponse = deleteRequest(DELETE_POST_PATH + postId, 200, accessAdminToken);

        assertEquals(204, deletePostResponse.getStatusCode(), "Status code should be 200 OK");
        Response getDeletedPostResponse = getRequestWithAccessToken(GET_POST_PATH + postId, 404, accessAdminToken);
        assertEquals(404, getDeletedPostResponse.getStatusCode(), "Deleted post should not be found");
    }

    @Test @org.testng.annotations.Test
    public void unauthorizedDeletePostTest() {
        Response deletePostResponse = deleteRequest(DELETE_POST_PATH + "postId", 401, null);

        assertEquals(401, deletePostResponse.getStatusCode(), "Status code should be 401 Unauthorized");

        String errorText = deletePostResponse.getBody().jsonPath().getString("message");
        assertEquals("Unauthorized", errorText);
    }
}
