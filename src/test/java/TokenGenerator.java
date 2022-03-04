import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.Assertions;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;

public class TokenGenerator {

    @BeforeSuite
    public static void setup() {
        RestAssured.baseURI = "https://simple-books-api.glitch.me";
    }

    public static String AuthToken;

    @Test(priority = 4)
    public void Token() {

        RequestSpecification request = RestAssured.given();

        String ClientBody = "{\n" +
                "  \"clientName\": \"RandomName\",\n" +
                "  \"clientEmail\": \"example"+ (int)(Math.random()*1000+1) + "@example.com\"\n}";

        request.contentType(ContentType.JSON);
        Response responseFromGenerateToken = request.body(ClientBody).post("/api-clients");
        responseFromGenerateToken.prettyPrint();

        String jsonString = responseFromGenerateToken.getBody().asString();
        String tokenGenerated = JsonPath.from(jsonString).get("accessToken");
        Assertions.assertEquals(201, responseFromGenerateToken.statusCode());

        AuthToken = tokenGenerated;

    }


}
