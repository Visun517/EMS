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
import java.sql.SQLException;
import java.util.Map;

import static java.lang.System.out;

@WebServlet("/api/v1/signup")
public class SignUpServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            ServletContext servletContext= getServletContext();
            out.println("sign up");
            BasicDataSource basicDataSource = (BasicDataSource) servletContext.getAttribute("basicDataSource");

            Connection connection = basicDataSource.getConnection();
            ObjectMapper objectMapper = new ObjectMapper();
            Map<String, String> event = objectMapper.readValue(req.getReader(), Map.class);

            out.println(event.get("email"));
            out.println(event.get("name"));
            out.println(event.get("password"));


            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO employeeTable (email, name, password) VALUES (?, ?, ?)");
            preparedStatement.setString(1, event.get("email"));
            preparedStatement.setString(2, event.get("name"));
            preparedStatement.setString(3, event.get("password"));
            int o = preparedStatement.executeUpdate();

            resp.setContentType("application/json");
            PrintWriter out = resp.getWriter();
            if (o > 0){
                resp.setStatus(HttpServletResponse.SC_OK);
                objectMapper.writeValue(out,Map.of(
                        "code","201",
                        "status","success",
                        "message","User signed up successfully"
                ));
            }else{
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                objectMapper.writeValue(out,Map.of(
                        "code","400",
                        "status","error",
                        "message","Bad Request"
                ));
            }
        connection.close();
        } catch (SQLException e) {
            ObjectMapper objectMapper = new ObjectMapper();
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            objectMapper.writeValue(resp.getWriter(),Map.of(
                    "code","500",
                    "status","error",
                    "message","Internal Server Error"
            ));
            throw new RuntimeException(e);
        }
    }
}