import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.JsonPath;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.Assertions;
import static io.restassured.RestAssured.given;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;
import java.util.List;

public class BookOrderApiTest extends TokenGenerator {

    public static String id;

    @BeforeSuite
    public static void setup() {
        RestAssured.baseURI = "https://simple-books-api.glitch.me";
    }

    @Test(priority = 1)
    void getStatus() {
        Response response = given()
                .contentType(ContentType.JSON)
                .when()
                .get("/status")
                .then()
                .statusCode(200)
                .extract().response();

        Assertions.assertEquals(200, response.statusCode());
        System.out.println("Get Status: " + response.getStatusCode());
    }
    @Test(priority = 2)
    void getListOfBooks(){
        Response response = given()
                .contentType(ContentType.JSON)
                .when()
                .get("/books")
                .then()
                .extract().response();

        Assertions.assertEquals(200, response.statusCode());
        System.out.println("List of books status: " + response.getStatusCode());
        response.prettyPrint();
    }
    @Test(priority = 3)
    void getSingleBook(){

        int bookId = (int)(Math.random()*5+1);
        Response response = given()
                .contentType(ContentType.JSON)
                .when()
                .get("/books/" + bookId)
                .then()
                .statusCode(200)
                .extract().response();

        Assertions.assertEquals(200, response.statusCode());
        Assertions.assertEquals(bookId, response.jsonPath().getInt("id"));
        System.out.println("Get a Single Book Status: " + response.getStatusCode());
        response.prettyPrint();
    }

    @Test(priority = 5)
    void order(){

        RequestSpecification request = RestAssured.given();
        request.header("Authorization","Bearer " + TokenGenerator.AuthToken)
                .contentType(ContentType.JSON);

        String bookOrderingDetail = "{\n" +
                "  \"bookId\": 3,\n" +
                "  \"customerName\": \"David Beckham\"\n}";

        Response bookOrderingDetailResponse = request.body(bookOrderingDetail).post("/orders");
        Assertions.assertEquals(201, bookOrderingDetailResponse.getStatusCode());
        bookOrderingDetailResponse.prettyPrint();
    }

   @Test(priority = 6)
   void getAllOrders(){
        Response response = given()
                .header("Authorization","Bearer " + TokenGenerator.AuthToken)
                .contentType(ContentType.JSON)
                .accept("*/*")
                .when()
                .get("/orders")
                .then()
                .statusCode(200)
                .extract().response();

        Assertions.assertEquals(200, response.statusCode());
        System.out.println("Get all orders Status: " + response.getStatusCode());
        response.prettyPrint();
   }

    @Test(priority = 7)
    public void OrderId(){

        Response response = given()
                .header("Authorization","Bearer " + TokenGenerator.AuthToken)
                .contentType(ContentType.JSON)
                .when()
                .get("/orders")
                .then()
                .extract().response();

        Configuration conf = Configuration.defaultConfiguration();
        List<String> a = JsonPath.using(conf).parse(response.getBody().asString()).read("$..id");
        id = a.get(0);
        System.out.println(id);

    }
    @Test(priority = 8)
    void getAnOrder(){

        Response response = given()
                .contentType(ContentType.JSON)
                .header("Authorization","Bearer " + TokenGenerator.AuthToken)
                .when()
                .get("/orders/" + id)
                .then()
                .statusCode(200)
                .extract().response();

        Assertions.assertEquals(200, response.statusCode());
        Assertions.assertEquals(3, response.jsonPath().getInt("bookId"));
        System.out.println("Get an Order Status: " + response.getStatusCode());
        response.prettyPrint();
    }

   @Test(priority = 9)
   void patchOrder(){

        RequestSpecification request = RestAssured.given();

                request.header("Authorization","Bearer " + TokenGenerator.AuthToken)
                .contentType(ContentType.JSON);

        String patchBody = "{\n" +
                "\"customerName\": \"Zinédine Zidane\"\n}";

        Response patchBodyResponse = request.body(patchBody).patch("/orders/"+id);
        Assertions.assertEquals(204, patchBodyResponse.statusCode());
        System.out.println("Patch status: " + patchBodyResponse.getStatusCode() + " but don't worry it is OK.");
        patchBodyResponse.prettyPrint();
    }

    @Test(priority = 10)
    void getAnOrderAgain(){

        Response response = given()
                .contentType(ContentType.JSON)
                .header("Authorization","Bearer " + TokenGenerator.AuthToken)
                .when()
                .get("/orders/" + id)
                .then()
                .statusCode(200)
                .extract().response();

        Assertions.assertEquals(200, response.statusCode());
        Assertions.assertEquals(3, response.jsonPath().getInt("bookId"));
        System.out.println("Get an Order Again Status: " + response.getStatusCode());
        response.prettyPrint();
    }
    @Test(priority = 11)
    void deleteOrder(){

        RequestSpecification request = RestAssured.given();

        request.header("Authorization","Bearer " + TokenGenerator.AuthToken)
                .contentType(ContentType.JSON);

        Response patchBodyResponse = request.delete("/orders/"+id);
        Assertions.assertEquals(204, patchBodyResponse.statusCode());
        System.out.println("Delete status: " + patchBodyResponse.getStatusCode() + " but don't worry it deleted.");
        patchBodyResponse.prettyPrint();
    }
}