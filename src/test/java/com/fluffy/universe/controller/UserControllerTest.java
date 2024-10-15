package com.fluffy.universe.controller;

import com.fluffy.universe.controllers.UserController;
import com.fluffy.universe.models.Role;
import com.fluffy.universe.models.User;
import com.fluffy.universe.utils.Configuration;
import com.fluffy.universe.utils.ServerData;
import com.fluffy.universe.utils.SessionUtils;
import io.javalin.Javalin;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.internal.verification.VerificationModeFactory;

import java.io.File;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class UserControllerTest {

    @Test
    public void registerRoutesTest() {
        Javalin app = Mockito.mock(Javalin.class);
        UserController usrctr=new UserController(app);
        usrctr.registerRoutes(app);
        Mockito.inOrder(app).verify(app, VerificationModeFactory.calls(12)).get(Mockito.anyString(), Mockito.any(Handler.class), Mockito.any(Role.class));
    }

    @Test
    public void updateAccountTest(){
        Configuration.load(new File("application.properties"));
        Javalin app = Mockito.mock(Javalin.class);
        Context ctx = Mockito.mock(Context.class);
        SessionUtils sessUtils = Mockito.mock(SessionUtils.class);
        ServerData serverData = new ServerData();
        ctx.sessionAttribute("serverData",serverData);
        UserController userController = new UserController(app);
        User user = new User();

        user.setId(42);
        user.setEmail("test@test.test");
        user.setFirstName("OldTestName");

        when(ctx.formParam("_method")).thenReturn("PUT");
        when(ctx.formParam("first-name")).thenReturn("TestFirstName");
        when(ctx.formParam("last-name")).thenReturn("TestLastName");
        when(ctx.formParam("gender")).thenReturn("male");
        when(ctx.formParam("birthday")).thenReturn("2023-01-01");
        when(ctx.formParam("address")).thenReturn("TestAddress");
        when(ctx.formParam("website")).thenReturn("https://www.google.com/");

        when(sessUtils.getCurrentUser(ctx)).thenReturn(user);
        when(sessUtils.getCurrentServerData(ctx)).thenReturn(serverData);

        userController.updateAccount(ctx);

        assertNotEquals("OldTestName",user.getFirstName());
        assertEquals("TestFirstName",user.getFirstName());

        Mockito.inOrder(ctx).verify(ctx, VerificationModeFactory.calls(8)).formParam(Mockito.anyString());
        Mockito.inOrder(ctx).verify(ctx, VerificationModeFactory.calls(2)).sessionAttribute(Mockito.anyString());
        verify(ctx).redirect("/account"); }

}
