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
    @Test
    @DisplayName("Should invalid verification code")
    void shouldInvalidCodes() {
        var authInfo = DataHelper.getAuthInfo();
        var verificationPage = loginPage.validLogin(authInfo);
        verificationPage.verifyVerificationPageVisibility();
        var verificationCode = DataHelper.generateRandomVerificationCode();
        verificationPage.verify(verificationCode.getCode());
        verificationPage.verifyErrorNotification("Ошибка! Неверно указан код! Попробуйте ещё раз.");
    }

    @Test
    @DisplayName("Test three failed login")
    void shouldLockAfterThreeUnsuccessfulPasswords() {
        var authInfo = DataHelper.generateRandomUser();
        var verificationPage = loginPage.validLogin(authInfo);
        verificationPage.verifyErrorNotification("Ошибка! Неверно указан логин или пароль");
        for (int count = 0; count < 3; count++) {
            loginPage.validLogin(DataHelper.generateRandomUser());
        }
        verificationPage.verifyErrorNotification("Превышено максимальное количество попыток авторизации");
    }
}