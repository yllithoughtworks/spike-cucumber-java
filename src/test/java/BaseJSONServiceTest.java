import static com.jayway.restassured.RestAssured.baseURI;
import static com.jayway.restassured.RestAssured.port;
public class BaseJSONServiceTest {

    {
        baseURI = "http://test.web.deliflow";
        port = 2400;
    }


}
