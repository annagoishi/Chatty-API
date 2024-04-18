package test;

import dto.PostGetResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class GetAllPostsTest extends BaseTest{
    @Test
    public void successGetAllPostsTest() {
        String accessAdminToken = getAdminAccessToken();
        Response response = getRequestWithAccessToken(GET_ALL_POSTS_PATH, 200, accessAdminToken);

        assertEquals(200, response.getStatusCode(), "Status code should be 200 OK");

        List<PostGetResponse> allPosts = response.jsonPath().getList("$", PostGetResponse.class);
        assertTrue(allPosts.size() > 0, "There should be posts returned");

        for (PostGetResponse post : allPosts) {
            assertNotNull(post.getId(), "Post ID should not be null");
            assertNotNull(post.getTitle(), "Post title should not be null");
            assertNotNull(post.getDescription(), "Post description should not be null");
            assertNotNull(post.getBody(), "Post body should not be null");
            assertNotNull(post.getCreatedAt(), "Post creation date should not be null");
            assertNotNull(post.getUpdatedAt(), "Post update date should not be null");
        }
    }

    @Test
    public void unauthorizedGetAllPostsTest() {
        String accessAdminToken = "invalidToken";
        Response response = getRequestWithAccessToken(GET_ALL_POSTS_PATH, 401, accessAdminToken);
        String errorText = response.getBody().jsonPath().getString("message");
        assertEquals("Unauthorized", errorText);
    }
}
