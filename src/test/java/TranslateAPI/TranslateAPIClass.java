package TranslateAPI;

import io.github.cdimascio.dotenv.Dotenv;
import io.restassured.RestAssured;
import io.restassured.response.Response;

public class TranslateAPIClass {

    private static final String RAPIDAPI_URL = "https://rapid-translate-multi-traduction.p.rapidapi.com";
    private static final String RAPIDAPI_KEY = System.getenv("RAPIDAPI_KEY");
    private static final String RAPIDAPI_HOST = System.getenv("RAPIDAPI_HOST");

    public static String translatedText;

    public static String translateAPI(String textTranslate) {
        RestAssured.baseURI = RAPIDAPI_URL;
        Response response = RestAssured.given()
                .header("x-rapidapi-key", RAPIDAPI_KEY)
                .header("x-rapidapi-host", RAPIDAPI_HOST)
                .header("Content-Type", "application/json")
                .body("{\"from\":\"es\",\"to\":\"en\",\"q\":\""+textTranslate+"\"}")
                .post("/t");

        if (response.statusCode() == 200) {
            translatedText = response.jsonPath().getString("[0]");
            return translatedText;
        } else {
            System.err.println("Translation failed status code: " + response.statusCode());
            System.err.println("Response error: " + response.getBody().asString());
            return null;
        }
    }

}
