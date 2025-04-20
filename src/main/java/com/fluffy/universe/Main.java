package com.fluffy.universe;

import com.fluffy.universe.controllers.*;
import com.fluffy.universe.exceptions.HttpException;
import com.fluffy.universe.middleware.CSRFFilter;
import com.fluffy.universe.middleware.ModelFilter;
import com.fluffy.universe.utils.ApplicationAccessManager;
import com.fluffy.universe.utils.Configuration;
import io.javalin.Javalin;
import io.javalin.http.staticfiles.Location;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.UUID;

public class Main {
    private static final Logger logger = LoggerFactory.getLogger(Main.class);
    public static void main(String[] args) {
        logger.info("Application is starting...");
        Javalin application = Javalin.create(configuration -> {
            configuration.addStaticFiles("/public", Location.CLASSPATH);
            configuration.accessManager(new ApplicationAccessManager());
            if (args.length > 0) {
                Configuration.load(new File(args[0]));
            }
        });
        ExceptionHandlerController exceptionHandlerController = new ExceptionHandlerController(application);

        application
                .before(ModelFilter::initializeModel)
                .before(CSRFFilter::verifyToken)
                .before(CSRFFilter::generateToken)
                .exception(HttpException.class, exceptionHandlerController::handleHttpException)
                .error(404, exceptionHandlerController::handlePageNotFoundError)
                .error(500, exceptionHandlerController::handleInternalServerError);

        new HomeController(application);
        new UserController(application);
        new PostController(application);
        new CommentController(application);
        application.exception(Exception.class, (e, ctx) -> {
            String errorId = UUID.randomUUID().toString();
            logger.error("Unhandled exception [{}]: {}", errorId, e.getMessage(), e);
            ctx.status(500);
            ctx.result("Something went wrong. Error code: " + errorId);
        });

        application.start(Configuration.get("application.host"), Configuration.getAsClass("application.port", Integer.class));
    }
}
