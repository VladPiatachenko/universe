package com.fluffy.universe.services;

import com.fluffy.universe.exceptions.HttpException;
import com.fluffy.universe.models.User;
import com.fluffy.universe.utils.Configuration;
import com.fluffy.universe.utils.DataSource;
import io.javalin.http.HttpCode;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.sql2o.Connection;
import org.sql2o.Query;
import org.jdbi.v3.core.Handle;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public  class UserService {//final
    public UserService() {}

    private static final int BCRYPT_STRENGTH = Configuration.getAsClass("application.bcryptStrength", Integer.class);
    private static final String INSERT_USER_SQL = "INSERT INTO User "
            + "(RoleID, FirstName, LastName, Email, Password, Gender, Birthday, Address, Website, ResetPasswordToken) VALUES "
            + "(:roleId, :firstName, :lastName, :email, :password, :gender, :birthday, :address, :website, :resetPasswordToken)";
    private static final String UPDATE_USER_SQL = "UPDATE User SET "
            + "RoleID = :roleId,"
            + "FirstName = :firstName, "
            + "LastName = :lastName, "
            + "Password = :password, "
            + "Gender = :gender,"
            + "Birthday = :birthday, "
            + "Address = :address, "
            + "Website = :website, "
            + "ResetPasswordToken = :resetPasswordToken "
            + "WHERE ID = :id";
    private static final String USER_BY_EMAIL_SQL = "SELECT * FROM User WHERE Email = :email";
    private static final String USER_BY_RESET_PASSWORD_TOKEN_SQL = "SELECT * FROM User WHERE ResetPasswordToken = :resetPasswordToken";
    private static final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(BCRYPT_STRENGTH);

    public static User getUserByEmail(String email) {
        try (Connection connection = DataSource.getConnection()) {
            return connection.createQuery(USER_BY_EMAIL_SQL)
                    .addParameter("email", email)
                    .executeAndFetchFirst(User.class);
        } catch (SQLException throwables) {
            throw new HttpException(HttpCode.INTERNAL_SERVER_ERROR, "Problem with database connection: " + throwables);
        }
    }

    public static User getUserByResetPasswordToken(String resetPasswordToken) {
        try (Connection connection = DataSource.getConnection()) {
            return connection.createQuery(USER_BY_RESET_PASSWORD_TOKEN_SQL)
                    .addParameter("resetPasswordToken", resetPasswordToken)
                    .executeAndFetchFirst(User.class);
        } catch (SQLException throwables) {
            throw new HttpException(HttpCode.INTERNAL_SERVER_ERROR, "Problem with database connection: " + throwables);
        }
    }

    public static void saveUser(User user) {
        try (Connection connection = DataSource.getConnection()) {
            if (user.getId() == null) {
                Integer id = connection
                        .createQuery(INSERT_USER_SQL, true)
                        .bind(user)
                        .executeUpdate()
                        .getKey(Integer.class);
                user.setId(id);
            } else {
                StringBuilder sql = new StringBuilder("UPDATE User SET ");
                List<String> updates = new ArrayList<>();
                Map<String, Object> params = new HashMap<>();

                if (user.getFirstName() != null) {
                    updates.add("FirstName = :firstName");
                    params.put("firstName", user.getFirstName());
                }
                if (user.getLastName() != null) {
                    updates.add("LastName = :lastName");
                    params.put("lastName", user.getLastName());
                }
                if (user.getGender() != null) {
                    updates.add("Gender = :gender");
                    params.put("gender", user.getGender());
                }
                if (user.getBirthday() != null) {
                    updates.add("Birthday = :birthday");
                    params.put("birthday", user.getBirthday());
                }
                if (user.getAddress() != null) {
                    updates.add("Address = :address");
                    params.put("address", user.getAddress());
                }
                if (user.getWebsite() != null) {
                    updates.add("Website = :website");
                    params.put("website", user.getWebsite());
                }

                if (updates.isEmpty()) return; // нема що оновлювати

                sql.append(String.join(", ", updates));
                sql.append(" WHERE ID = :id");
                params.put("id", user.getId());

                Query query = connection.createQuery(sql.toString());

                for (Map.Entry<String, Object> entry : params.entrySet()) {
                    query.addParameter(entry.getKey(), entry.getValue());
                }

                query.executeUpdate();
            }
        } catch (Exception e) {
            throw new HttpException(HttpCode.INTERNAL_SERVER_ERROR,
                    "Problem with database connection: " + e);
        }
    }



    public static String encodePassword(String password) {
        return passwordEncoder.encode(password);
    }

    public static boolean isCorrectPassword(String password, String encodedPassword) {
       return passwordEncoder.matches(password, encodedPassword);
    }
}
