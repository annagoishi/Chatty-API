package test;

import com.github.javafaker.Faker;
import dto.PostCreateRequest;
import dto.PostGetResponse;
import dto.UserResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class GetPostByUserTest extends BaseTest{
    @Test
    public void successGetUserPostsTest() {
        Faker faker = new Faker();
        String title = faker.lorem().words(1).get(0);
        String description = faker.lorem().sentence();
        String body = faker.lorem().paragraph();
        String imageUrl = faker.internet().image();
        Response registerUser = registerUser(201);
        String accessToken = registerUser.jsonPath().getString("accessToken");
        Response userInfo = getRequestWithAccessToken(ME_PATH, 200, accessToken);
        UserResponse userResponse = userInfo.as(UserResponse.class);
        String userId = userResponse.getId();
        PostCreateRequest postCreateRequest = PostCreateRequest.builder()
                .title(title)
                .description(description)
                .body(body)
                .imageUrl(imageUrl)
                .draft(false)
                .build();

        postRequestWithAccessToken(CREATE_POST_PATH, 201, postCreateRequest, accessToken);
        postRequestWithAccessToken(CREATE_POST_PATH, 201, postCreateRequest, accessToken);


        String accessAdminToken = getAdminAccessToken();
        Response response = getRequestWithAccessToken(GET_POSTS_PATH.replace("{user_id}", userId), 200, accessAdminToken);

        assertEquals(200, response.getStatusCode(), "Status code should be 200 OK");

        PostGetResponse[] posts = response.as(PostGetResponse[].class);
        assertEquals(2, posts.length, "There should be 2 posts returned");
        for (PostGetResponse post : posts) {
            assertEquals(userId, post.getUser().getId(), "Each post should belong to the specified user");
        }
    }
    @Test
    public void unauthorizedGetUserPostsTest() {
        Faker faker = new Faker();
        String title = faker.lorem().words(1).get(0);
        String description = faker.lorem().sentence();
        String body = faker.lorem().paragraph();
        String imageUrl = faker.internet().image();
        Response registerUser = registerUser(201);
        String accessToken = registerUser.jsonPath().getString("accessToken");
        Response userInfo = getRequestWithAccessToken(ME_PATH, 200, accessToken);
        UserResponse userResponse = userInfo.as(UserResponse.class);
        String userId = userResponse.getId();
        PostCreateRequest postCreateRequest = PostCreateRequest.builder()
                .title(title)
                .description(description)
                .body(body)
                .imageUrl(imageUrl)
                .draft(false)
                .build();

        postRequestWithAccessToken(CREATE_POST_PATH, 201, postCreateRequest, accessToken);

        Response response = getRequestWithAccessToken(GET_POSTS_PATH.replace("{user_id}", userId), 401, null);

        assertEquals(401, response.getStatusCode(), "Status code should be 401 Unauthorized");

        String errorText = response.getBody().jsonPath().getString("message");
        assertEquals("Unauthorized", errorText);
    }

}
