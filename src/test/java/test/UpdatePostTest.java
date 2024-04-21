package test;
import dto.PostGetResponse;
import dto.PostUpdateRequest;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class UpdatePostTest extends BaseTest{
    @Test @org.testng.annotations.Test
    public void successUpdatePostTest() {
        Response createPostResponse = createPost(201);
        String postId = createPostResponse.jsonPath().getString("id");

        PostUpdateRequest updateRequest = new PostUpdateRequest();
        updateRequest.setTitle("Updated Title");
        updateRequest.setDescription("Updated Description");
        updateRequest.setBody("Updated Body");
        updateRequest.setImageUrl("Updated Image URL");
        updateRequest.setDraft(false);

        String accessAdminToken = getAdminAccessToken();
        Response updatePostResponse = putRequest(UPDATE_POST_PATH + postId, 200, updateRequest, accessAdminToken);

        assertEquals(200, updatePostResponse.getStatusCode(), "Status code should be 200 OK");
        PostGetResponse updatedPost = updatePostResponse.as(PostGetResponse.class);
        assertEquals("Updated Title", updatedPost.getTitle(), "Post title should be updated");
        assertEquals("Updated Description", updatedPost.getDescription(), "Post description should be updated");
    }

    @Test @org.testng.annotations.Test
    public void unauthorizedUpdatePostTest() {
        Response createPostResponse = createPost(201);
        String postId = createPostResponse.jsonPath().getString("id");

        PostUpdateRequest updateRequest = new PostUpdateRequest();
        updateRequest.setTitle("Updated Title");
        updateRequest.setDescription("Updated Description");
        updateRequest.setBody("Updated Body");
        updateRequest.setImageUrl("Updated Image URL");
        updateRequest.setDraft(false);

        Response updatePostResponse = putRequest(UPDATE_POST_PATH + postId, 401, updateRequest, null);

        assertEquals(401, updatePostResponse.getStatusCode(), "Status code should be 401 Unauthorized");
        String errorText = updatePostResponse.getBody().jsonPath().getString("message");
        assertEquals("Unauthorized", errorText);
    }
}
