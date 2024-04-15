package test;

import com.github.javafaker.Faker;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

public class FeedbackTests extends BaseTest{
    @Test
    public void successFeedbackTest() {

        Faker faker = new Faker();
        String name = faker.name().firstName();
        String email = faker.internet().emailAddress();
        String content = faker.lorem().paragraph();

        Response feedbackResponse = postRequest(FEEDBACK_PATH, 200, "{\"name\":\"" + name + "\",\"email\":\"" + email + "\",\"content\":\""+ content + "\"}");
    }
    @Test
    public void feedbackWithoutContentTest() {

        Faker faker = new Faker();
        String name = faker.name().firstName();
        String email = faker.internet().emailAddress();

        Response feedbackResponse = postRequest(FEEDBACK_PATH, 400, "{\"name\":\"" + name + "\",\"email\":\"" + email + "\"}");
    }

}
