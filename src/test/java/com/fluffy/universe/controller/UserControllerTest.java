package com.fluffy.universe.controller;

import com.fluffy.universe.controllers.UserController;
import com.fluffy.universe.models.Role;
import io.javalin.Javalin;
import io.javalin.http.Handler;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.internal.verification.VerificationModeFactory;
import org.mockito.stubbing.OngoingStubbing;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class UserControllerTest {


    @Test
    public void registerRoutesTest() {
        Javalin app = Mockito.mock(Javalin.class);
        UserController uscontr=new UserController(app);
        uscontr.registerRoutes(app);

        Mockito.inOrder(app).verify(app, VerificationModeFactory.calls(2)).get(Mockito.anyString(), Mockito.any(Handler.class), Mockito.any(Role.class));
        Mockito.inOrder(app).verify(app, VerificationModeFactory.calls(1)).post(Mockito.anyString(), Mockito.any(Handler.class), Mockito.any(Role.class));
    }

}
