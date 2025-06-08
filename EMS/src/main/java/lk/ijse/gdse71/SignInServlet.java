package lk.ijse.gdse71;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.dbcp2.BasicDataSource;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

@WebServlet("/api/v1/signin")
public class SignInServlet extends HttpServlet {
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ServletContext servletContext= getServletContext();
        BasicDataSource basicDataSource = (BasicDataSource) servletContext.getAttribute("basicDataSource");
        try {
            System.out.println("sign in");
            Connection connection = basicDataSource.getConnection();
            ObjectMapper objectMapper = new ObjectMapper();
            Map<String, String> event = objectMapper.readValue(req.getReader(), Map.class);

            String email = event.get("email");
            String password = event.get("password");

            PreparedStatement preparedStatement = connection.prepareStatement("select * from employeeTable where email=? and password=?");
            preparedStatement.setString(1, email);
            preparedStatement.setString(2, password);
            ResultSet resultSet = preparedStatement.executeQuery();

            PrintWriter out = resp.getWriter();
            resp.setContentType("application/json");
            if(resultSet.next()){
                resp.setStatus(HttpServletResponse.SC_OK);
                objectMapper.writeValue(out, Map.of(
                        "code", "200",
                        "status", "success",
                        "message", "User signed in successfully"
                ));
            }else{
                System.out.println("Unauthorized");
                resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                objectMapper.writeValue(out,Map.of(
                        "code","401",
                        "status","Unauthorized",
                        "message","Unauthorized Behaviour"
                ));
            }
        connection.close();
        } catch (SQLException e) {
            ObjectMapper objectMapper = new ObjectMapper();
            PrintWriter printWriter = resp.getWriter();
            objectMapper.writeValue(printWriter,Map.of(
                    "code","500",
                    "status","Internal Server Error",
                    "message",e.getMessage()
            ));
            throw new RuntimeException(e);
        }

    }
}