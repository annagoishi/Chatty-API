package test;

import io.restassured.response.Response;
import org.junit.jupiter.api.Test;
import dto.PostGetResponse;

import static org.junit.jupiter.api.Assertions.*;

public class GetPostByIdTest extends BaseTest{
    @Test
    public void successGetPostById() {
        Response createPostResponse = createPost(201);
        String postId = createPostResponse.jsonPath().getString("id");
        String accessAdminToken = getAdminAccessToken();
        Response getPostResponse = getRequestWithAccessToken(GET_POST_PATH + postId, 200, accessAdminToken);
        assertEquals(200, getPostResponse.getStatusCode(), "Status code should be 200 OK");

        PostGetResponse post = getPostResponse.as(PostGetResponse.class);
        assertNotNull(post.getId(), "Post ID should not be null");
        assertNotNull(post.getTitle(), "Post title should not be null");
    }
    @Test
    public void unauthorizedGetPostById() {
        Response createPostResponse = createPost(201);
        String postId = createPostResponse.jsonPath().getString("id");
        Response getPostResponse = getRequestWithAccessToken(GET_POST_PATH + postId, 401, null);

        assertEquals(401, getPostResponse.getStatusCode(), "Status code should be 401 Unauthorized");

        String errorText = getPostResponse.getBody().jsonPath().getString("message");
        assertEquals("Unauthorized", errorText);
    }
}
