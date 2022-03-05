import com.jayway.jsonpath.Configuration;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.Assertions;
import org.testng.annotations.Test;

import java.util.List;

import static io.restassured.RestAssured.given;

public class TokenAndOrderIdGenerator {

    private String AuthToken;

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

    public String getAuthToken(){
        return AuthToken;
    }

    private String id;

    @Test(priority = 7)
    public void OrderId(){

        Response response = given()
                .header("Authorization","Bearer " + getAuthToken())
                .contentType(ContentType.JSON)
                .when()
                .get("/orders")
                .then()
                .extract().response();

        Configuration conf = Configuration.defaultConfiguration();
        List<String> a = com.jayway.jsonpath.JsonPath.using(conf).parse(response.getBody().asString()).read("$..id");
        id = a.get(0);
        System.out.println(id);

    }

    public String getId(){
        return id;
    }

}
