package test;

import org.junit.jupiter.api.*;
import data.DataHelper;
import data.SqlHelper;
import pages.LoginPage;

import static com.codeborne.selenide.Selenide.open;
import static data.SqlHelper.cleanAuthCodes;
import static data.SqlHelper.cleanDatabase;

class LoginTest {

    LoginPage loginPage;

    @AfterEach
    void tearDown() {
        cleanAuthCodes();
    }

    @AfterAll
    static void tearDownAll() {
        cleanDatabase();
    }

    @BeforeEach
    void setUp() {
        loginPage = open("http://localhost:9999", LoginPage.class);
    }

    @Test //позитивный тест
    @DisplayName("Should successful login validation test")
    void shouldSuccessfulLogin() {
        var authInfo = DataHelper.getAuthInfo();
        var verificationPage = loginPage.validLogin(authInfo);
        verificationPage.verifyVerificationPageVisibility();
        var verificationCode = SqlHelper.getVerificationCode();
        verificationPage.validVerify(verificationCode.getCode());
    }
}