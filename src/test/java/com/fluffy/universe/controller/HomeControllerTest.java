package com.fluffy.universe.controller;

import com.fluffy.universe.controllers.HomeController;
import com.fluffy.universe.models.Role;
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
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

public class HomeControllerTest {
    @Test
    public void registerRoutestest() {
        Javalin application=Mockito.mock(Javalin.class);
        HomeController hc=new HomeController(application);
        hc.registerRoutes(application);
        Mockito.inOrder(application).verify(application, VerificationModeFactory.calls(3)).get(Mockito.anyString(), Mockito.any(Handler.class), Mockito.any(Role.class));
    }
    @Test
    public void homePagetest() throws SQLException {
        Context ctx=Mockito.mock(Context.class);
        Javalin application=Mockito.mock(Javalin.class);
        HomeController hc=new HomeController(application);
        when(ctx.queryParam("page")).thenReturn("1");
        when(ctx.queryParam("size")).thenReturn("10");
        Map<String, Object> model = new HashMap<String,Object>();

        Configuration.load(new File("application.properties"));//для стабільної роботи програми
        //нам буде треба існування десь безконтрольно на фоні коннекту до БД
        SessionUtils sessUtils = Mockito.mock(SessionUtils.class);//новий мок об'єкт для керування доступом
        // до того, що має повертатись з контексту
        ServerData serverData = new ServerData();
        ctx.sessionAttribute("serverData",serverData);//В стеку запитів є очищення даних сервера в контексті
        //вони можуть бути порожні, але ініційовані і не null

        when(sessUtils.getCurrentModel(ctx)).thenReturn(model);//повертаємо нашу модель
        when(sessUtils.getCurrentServerData(ctx)).thenReturn(serverData);//і наші серверні дані

        hc.homePage(ctx);
        //починаємо перевірки, що в моделі з'явились дані
        assertEquals(4,model.get("paginationPageSize"));
        assertEquals(1,model.get("paginationCurrentPage"));
        assertEquals(2,model.get("paginationSpread"));
        assertEquals("/",model.get("paginationBaseURL"));
        //перевіряємо, що до наших заглушок звертались в потрібних місцях
        Mockito.inOrder(ctx).verify(ctx, VerificationModeFactory.calls(2)).queryParam(Mockito.anyString());
        Mockito.inOrder(ctx).verify(ctx, VerificationModeFactory.calls(2)).sessionAttribute(Mockito.anyString());
    }
}
