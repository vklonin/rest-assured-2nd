import io.restassured.response.Response;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Cookie;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.WebDriverRunner.getWebDriver;
import static com.codeborne.selenide.Selenide.open;
import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.core.Is.is;

public class RestAssuredDemoWebShop {
    String cookieBask = "Nop.customer=dcd31474-51fb-4451-a8ae-dd2b9dc7349a;";
    @Test
    void addToCartWithCookieTest() {
        Response response =
                given()
                        .contentType("application/x-www-form-urlencoded; charset=UTF-8")
                        .cookie(cookieBask)
                        .when()
                        .post("http://demowebshop.tricentis.com/addproducttocart/catalog/13/1/1")
                        .then()
                        .statusCode(200)
                        .body("success", is(true))
                        .body("message", is("The product has been added to your <a href=\"/cart\">shopping cart</a>"))
                        .extract().response();

        String basketSize = response.path("updatetopcartsectionhtml");
        String cookieBaskBack = response.cookie("NOP.CUSTOMER");

        open("http://demowebshop.tricentis.com/Themes/DefaultClean/Content/images/logo.png");
        getWebDriver().manage().addCookie(new Cookie("NOP.CUSTOMER", cookieBaskBack));
        open("http://demowebshop.tricentis.com");
        assertThat($(".cart-qty").getText()).isEqualTo(basketSize);

    }

}

