package test;

import dto.PostGetResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class GetDraftPostsTest extends BaseTest {
    @Test @org.testng.annotations.Test
    public void successGetDraftPostsTest() {
        String accessAdminToken = getAdminAccessToken();
        Response response = getRequestWithAccessToken(GET_DRAFT_POSTS_PATH, 200, accessAdminToken);

        List<PostGetResponse> draftPosts = response.jsonPath().getList("$", PostGetResponse.class);
        assertTrue(draftPosts.size() > 0, "There should be draft posts returned");
        for (PostGetResponse post : draftPosts) {
            assertNotNull(post.getId(), "Post ID should not be null");
            assertNotNull(post.getTitle(), "Post title should not be null");
            assertNotNull(post.getDescription(), "Post description should not be null");
            assertNotNull(post.getBody(), "Post body should not be null");
            assertNotNull(post.getCreatedAt(), "Post creation date should not be null");
            assertNotNull(post.getUpdatedAt(), "Post update date should not be null");
        }
    }

    @Test @org.testng.annotations.Test
    public void unauthorizedGetDraftPostsTest() {
        String accessAdminToken = "invalidToken";
        Response response = getRequestWithAccessToken(GET_DRAFT_POSTS_PATH, 401, accessAdminToken);

        String errorText = response.getBody().jsonPath().getString("message");
        assertEquals("Unauthorized", errorText, "Error message should indicate Unauthorized access");
    }
}

