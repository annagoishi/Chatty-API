package test;

import com.github.javafaker.Faker;
import dto.FeedbackRequest;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class FeedbackTest extends BaseTest{
    @Test
    public void successFeedbackTest() {
        Faker faker = new Faker();
        String name = faker.name().firstName();
        String email = faker.internet().emailAddress();
        String content = faker.lorem().paragraph();

        FeedbackRequest feedbackRequest = FeedbackRequest.builder()
                .name(name)
                .email(email)
                .content(content)
                .build();

        Response feedbackResponse = postRequest(FEEDBACK_PATH, 201, feedbackRequest);
        assertEquals(201, feedbackResponse.getStatusCode(), "Feedback submission failed");
    }
    @Test
    public void feedbackWithoutNameTest() {

        Faker faker = new Faker();
        String name = "";
        String email = faker.internet().emailAddress();
        String content = faker.lorem().paragraph();

        FeedbackRequest feedbackRequest = FeedbackRequest.builder()
                .name(name)
                .email(email)
                .content(content)
                .build();

        Response feedbackResponse = postRequest(FEEDBACK_PATH, 400, feedbackRequest);
        String responseMessage = feedbackResponse.getBody().jsonPath().getString("name");
        assertTrue(responseMessage.contains("Name can not be empty!"));
    }
    @Test
    public void feedbackWithoutEmailTest() {

        Faker faker = new Faker();
        String name = faker.name().firstName();
        String email = "";
        String content = faker.lorem().paragraph();


        FeedbackRequest feedbackRequest = FeedbackRequest.builder()
                .name(name)
                .email(email)
                .content(content)
                .build();

        Response feedbackResponse = postRequest(FEEDBACK_PATH, 400, feedbackRequest);
        String responseMessage = feedbackResponse.getBody().jsonPath().getString("email");
        assertTrue(responseMessage.contains("Email can not be empty!"));
    }
    @Test
    public void feedbackWithoutContentTest() {

        Faker faker = new Faker();
        String name = faker.name().firstName();
        String email = faker.internet().emailAddress();
        String content = "";


        FeedbackRequest feedbackRequest = FeedbackRequest.builder()
                .name(name)
                .email(email)
                .content(content)
                .build();

        Response feedbackResponse = postRequest(FEEDBACK_PATH, 400, feedbackRequest);
        String responseMessage = feedbackResponse.getBody().jsonPath().getString("content");
        assertTrue(responseMessage.contains("Content can not be empty!"));
    }
}
