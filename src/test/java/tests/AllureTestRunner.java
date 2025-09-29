package tests;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

// Запускаем сразу все тесты в одном раннере
@RunWith(Suite.class)
@Suite.SuiteClasses({
        OrderCreationTests.class,
        UserLoginTests.class,
        UserOrdersTests.class,
        UserRegistrationTests.class,
        UserUpdateTests.class
})
public class AllureTestRunner {
    // Тело пустое — используется только для агрегации тестов
}