package ru.otus.spring.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.otus.spring.service.ui.login.LoginProcessService;
import ru.otus.spring.service.ui.menu.MenuService;
import ru.otus.spring.service.ui.testing.TestProcessService;
import ru.otus.spring.service.ui.testing.TestResultSender;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@DisplayName("Класс TestControllerImpl")
@ExtendWith(MockitoExtension.class)
class TestControllerImplTest {
    private TestController testController;
    @Mock
    private TestProcessService testProcessService;
    @Mock
    private TestResultSender testResultPrinter;
    @Mock
    private LoginProcessService loginService;
    @Mock
    private MenuService menuService;

    @BeforeEach
    void init(){
        testController = new TestControllerImpl(testProcessService, loginService, testResultPrinter, menuService);
    }
    @DisplayName("Метод testing должен вызывать метод LoginService.login и UITestService.testStudent")
    @Test
    void shouldInvokeLoginAndTesting(){
        given(menuService.getCommand()).willReturn(MenuService.Command.EXIT);
        testController.start();
        verify(testProcessService, times(1)).testStudent(any());
        verify(loginService, times(1)).login();
        verify(testResultPrinter, times(1)).send(any());
    }
}