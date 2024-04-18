package test;

import com.github.javafaker.Faker;
import dto.UserResponse;
import dto.UserUpdateRequest;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class UpdateUserTest extends BaseTest {

    @Test
    public void successUpdateUserInfoTest() {
        Faker faker = new Faker();
        Response registerUser = registerUser(201);
        String accessToken = registerUser.jsonPath().getString("accessToken");
        String name = faker.name().firstName();
        String surname = faker.name().lastName();
        String phone = "+13453454545";

        Response userInfo = getRequestWithAccessToken(ME_PATH, 200, accessToken);
        UserResponse userResponse = userInfo.as(UserResponse.class);
        String id = userResponse.getId();

        UserUpdateRequest updateRequest = UserUpdateRequest.builder()
                .name(name)
                .surname(surname)
                .phone(phone)
                .build();

        putRequest(UPDATE_USER_PATH + id, 200, updateRequest, accessToken);
        Response updatedUserInfo = getRequestWithAccessToken(ME_PATH, 200, accessToken);
        UserResponse updatedUserResponse = updatedUserInfo.as(UserResponse.class);
        assertEquals(name, updatedUserResponse.getName());
        assertEquals(surname, updatedUserResponse.getSurname());
        assertEquals(phone, updatedUserResponse.getPhone());
    }
    @Test
    public void failUpdateUserInfoTest() {
        Response registerUser = registerUser(201);
        String accessToken = registerUser.jsonPath().getString("accessToken");
        String name = "";
        String surname = "";
        String phone = "";

        Response userInfo = getRequestWithAccessToken(ME_PATH, 200, accessToken);
        UserResponse userResponse = userInfo.as(UserResponse.class);
        String id = userResponse.getId();

        UserUpdateRequest updateRequest = UserUpdateRequest.builder()
                .name(name)
                .surname(surname)
                .phone(phone)
                .build();

        putRequest(UPDATE_USER_PATH + id, 400, updateRequest, accessToken);

        Response updatedUserInfo = getRequestWithAccessToken(ME_PATH, 200, accessToken);
        UserResponse updatedUserResponse = updatedUserInfo.as(UserResponse.class);
        assertNotEquals(name, updatedUserResponse.getName());
        assertNotEquals(surname, updatedUserResponse.getSurname());
        assertNotEquals(phone, updatedUserResponse.getPhone());
    }
    @Test
    public void unauthorizedUpdateUserInfoTest() {
        Faker faker = new Faker();
        Response registerUser = registerUser(201);
        String accessToken = registerUser.jsonPath().getString("accessToken");
        String name = faker.name().firstName();
        String surname = faker.name().lastName();
        String phone = "+13453454545";

        Response userInfo = getRequestWithAccessToken(ME_PATH, 200, accessToken);
        UserResponse userResponse = userInfo.as(UserResponse.class);
        String id = userResponse.getId();

        UserUpdateRequest updateRequest = UserUpdateRequest.builder()
                .name(name)
                .surname(surname)
                .phone(phone)
                .build();

        putRequest(UPDATE_USER_PATH + id, 401, updateRequest, null);

        Response updatedUserInfo = getRequestWithAccessToken(ME_PATH, 200, accessToken);
        UserResponse updatedUserResponse = updatedUserInfo.as(UserResponse.class);
        assertNotEquals(name, updatedUserResponse.getName());
        assertNotEquals(surname, updatedUserResponse.getSurname());
        assertNotEquals(phone, updatedUserResponse.getPhone());
    }
}
