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

    Values value = new Values();

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
        String AuthToken = JsonPath.from(jsonString).get("accessToken");

        value.setAuthToken(AuthToken);

        Assertions.assertEquals(201, responseFromGenerateToken.statusCode());
    }



    @Test (priority = 7)
    public void OrderId(){

        Response response = given()
                .header("Authorization","Bearer " + value.getAuthToken())
                .contentType(ContentType.JSON)
                .when()
                .get("/orders")
                .then()
                .extract().response();

        Configuration conf = Configuration.defaultConfiguration();
        List<String> a = com.jayway.jsonpath.JsonPath.using(conf).parse(response.getBody().asString()).read("$..id");
        String orderId = a.get(0);

        value.setOrderId(orderId);

        System.out.println("OrderId: " + orderId);
    }

}
