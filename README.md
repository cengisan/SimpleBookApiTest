Hello!

In this project, I aimed to write a automation test with java. I took API that I used from "https://github.com/vdespa/introduction-to-postman-course.git". When I wrote the cod, I used rest-assured library. 

I write my test based on the course in link and test cases are in following;

|Test Step|Test Case|
|:-------:|---------|
|1|Get status|
|2|Get list of book|
|3|Get single book|
|4|Login and creating authorization token|
|5|Giving order|
|6|Get all order|
|7|Finding order id|
|8|Getting order wtih order id|
|9|Patch order information|
|10|Get same order again for check|
|11|Delete order|


First of all, I defined a base url for each test case. It is running before each test because of @BeforeSuite annotation.

```java
@BeforeSuite
public static void setup() {RestAssured.baseURI = "https://simple-books-api.glitch.me";}
```
After that, I set the priorities according to test step. Therefore, it is running the same test in every run. I wrote priority, requests and my expectation as seen as in following.

```java
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
```



