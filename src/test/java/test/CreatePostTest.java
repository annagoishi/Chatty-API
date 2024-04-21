package test;

import com.github.javafaker.Faker;
import dto.PostCreateRequest;
import dto.PostResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


public class CreatePostTest extends BaseTest {
    @Test @org.testng.annotations.Test
    public void successCreatePostTest() {
        Faker faker = new Faker();
        String title = faker.lorem().words(1).get(0);
        String description = faker.lorem().sentence();
        String body = faker.lorem().paragraph();
        String imageUrl = faker.internet().image();

        Response registerUser = registerUser(201);
        String accessToken = registerUser.jsonPath().getString("accessToken");

        PostCreateRequest postCreateRequest = PostCreateRequest.builder()
                .title(title)
                .description(description)
                .body(body)
                .imageUrl(imageUrl)
                .draft(false)
                .build();

        Response createPostResponse = postRequestWithAccessToken(CREATE_POST_PATH, 200, postCreateRequest, accessToken);
        PostResponse createdPostResponse = createPostResponse.as(PostResponse.class);
        assertEquals(title, createdPostResponse.getTitle(), "Returned post title should match the sent one");
        assertEquals(description, createdPostResponse.getDescription(), "Returned post description should match the sent one");
    }

    @Test @org.testng.annotations.Test
    public void createPostInvalidDateTest() {
        Faker faker = new Faker();
        String title = faker.lorem().sentence();
        String description = faker.lorem().paragraph();
        String publishDate = LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME);

        Response registerUser = registerUser(201);
        String accessToken = registerUser.jsonPath().getString("accessToken");

        PostCreateRequest postCreateRequest = PostCreateRequest.builder()
                .title(title)
                .description(description)
                .publishDate(publishDate) // Некорректное поле
                .draft(false)
                .build();

        Response createPostResponse = postRequestWithAccessToken(CREATE_POST_PATH, 400, postCreateRequest, accessToken);
        String errorMessage = createPostResponse.getBody().asString();
        assertTrue(errorMessage.contains("Cannot deserialize value of type `java.time.LocalDateTime`"));
    }
    @Test @org.testng.annotations.Test
    public void unauthorizedCreatePostTest() {
        Faker faker = new Faker();
        String title = faker.lorem().words(1).get(0);
        String description = faker.lorem().sentence();
        String body = faker.lorem().paragraph();
        String imageUrl = faker.internet().image();

        PostCreateRequest postCreateRequest = PostCreateRequest.builder()
                .title(title)
                .description(description)
                .body(body)
                .imageUrl(imageUrl)
                .draft(false)
                .build();

        Response createPostResponse = postRequestWithAccessToken(CREATE_POST_PATH, 401, postCreateRequest, null);
        String errorText = createPostResponse.getBody().jsonPath().getString("message");
        assertEquals("Unauthorized", errorText);
    }

}
