package com.fluffy.universe.controllers;

import com.fluffy.universe.controllers.PostController;
import com.fluffy.universe.exceptions.HttpException;
import com.fluffy.universe.models.Role;
import com.fluffy.universe.services.CommentService;
import com.fluffy.universe.services.PostService;
import com.fluffy.universe.utils.ServerData;
import com.fluffy.universe.utils.SessionUtils;
import io.javalin.Javalin;
import io.javalin.http.Context;
import io.javalin.http.HttpCode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class PostControllerTest {
    private Javalin app;
    private PostController postController;
    private Context context;

    @BeforeEach
    void setUp() {
        app = mock(Javalin.class);
        postController = new PostController(app);
        context = mock(Context.class);

        // Mocking the path parameter for post ID
        when(context.pathParam("post")).thenReturn("1");
    }

    @Test
    void testShowPostNotFound() {
        // Arrange: Mock the PostService static method to return null for the given post ID
        try (MockedStatic<PostService> mockedPostService = Mockito.mockStatic(PostService.class)) {
            mockedPostService.when(() -> PostService.getUserPost(1)).thenReturn(null); // Simulating that the post is not found

            // Act & Assert: Verify the exception is thrown
            HttpException thrown = assertThrows(HttpException.class, () -> postController.show(context));
            assertEquals(HttpCode.NOT_FOUND, thrown.getHttpCode());
            assertEquals("Post not found", thrown.getMessage());
        }
    }

    @Test
    void testShowPostFound() {
        // Arrange: Mock the PostService static method
        Map<String, Object> mockPost = new HashMap<>();
        mockPost.put("post.id", 1);
        mockPost.put("post.title", "Test Title");
        mockPost.put("post.description", "Test Description");
        mockPost.put("post.publicationDateTime", "2024-09-30T12:00:00");

        // Mocking CommentService to return a predefined value for comments
        List<Map<String, Object>> mockComments = List.of(new HashMap<>(
                Map.of("comment.id", 1, "comment.text", "Nice post!"))); // Replace with desired mock comment data

        // Mocking SessionUtils to return a non-null model
        Map<String, Object> mockModel = new HashMap<>();
        when(SessionUtils.getCurrentModel(context)).thenReturn(mockModel); // Mocking to return an initialized model

        // Mocking SessionUtils to return a non-null ServerData
        ServerData mockServerData = mock(ServerData.class); // Create a mock ServerData
        when(SessionUtils.getCurrentServerData(context)).thenReturn(mockServerData); // Mocking to return the ServerData

        try (MockedStatic<PostService> mockedPostService = Mockito.mockStatic(PostService.class);
             MockedStatic<CommentService> mockedCommentService = Mockito.mockStatic(CommentService.class)) {

            mockedPostService.when(() -> PostService.getUserPost(1)).thenReturn(mockPost); // Simulating that the post is found
            mockedCommentService.when(() -> CommentService.getUserCommentsByPostId(1)).thenReturn(mockComments); // Mocking comment retrieval

            // Act: Call the show method
            postController.show(context);

            // Assert: Verify that the post and comments are set in the model
            assertEquals(mockPost, mockModel.get("post"));
            assertEquals(mockComments, mockModel.get("comments"));
        }
    }

    private void mockSessionUtils(ServerData mockServerData, Map<String, Object> mockModel) {
        when(SessionUtils.getCurrentServerData(context)).thenReturn(mockServerData);
        when(SessionUtils.getCurrentModel(context)).thenReturn(mockModel);
    }

    @Test
    void testIndexPage() {
        postController.indexPage(context);
        verify(context).redirect("/");
    }

    @Test
    void testCreatePage() {
        // Arrange: Create mocks for ServerData and the model
        ServerData mockServerData = mock(ServerData.class);
        Map<String, Object> mockModel = new HashMap<>();
        mockSessionUtils(mockServerData, mockModel);

        // Act: Call the method under test
        postController.createPage(context);

        // Assert: Verify that the correct view is rendered and ServerData's clear() is called
        verify(context).render(eq("/views/pages/models/post/create.vm"), eq(mockModel));
        verify(mockServerData).clear();
    }

    @Test
    void testEditPage() {
        // Arrange: Create mocks for ServerData and the model
        ServerData mockServerData = mock(ServerData.class);
        Map<String, Object> mockModel = new HashMap<>();
        mockSessionUtils(mockServerData, mockModel);

        // Set up the expected post ID path parameter
        when(context.pathParam("post")).thenReturn("1");

        // Act: Call the method under test
        postController.editPage(context);

        // Assert: Verify that the correct view is rendered and ServerData's clear() is called
        verify(context).render(eq("/views/pages/models/post/edit.vm"), eq(mockModel));
        verify(mockServerData).clear();
    }


    @Test
    void testRegisterRoutes() {
        // Act is already handled in the setUp method
        // Assert: Verify that routes are registered correctly
        verify(app, times(1)).get(eq("/posts"), any(), eq(Role.GUEST));
        verify(app, times(1)).get(eq("/posts/create"), any(), eq(Role.USER));
        verify(app, times(1)).get(eq("/posts/{post}/edit"), any(), eq(Role.USER));
        verify(app, times(1)).get(eq("/posts/{post}"), any(), eq(Role.GUEST), eq(Role.USER));
        verify(app, times(1)).post(eq("/posts"), any(), eq(Role.USER));
    }
}
