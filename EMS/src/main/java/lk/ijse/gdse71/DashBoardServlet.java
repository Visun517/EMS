package lk.ijse.gdse71;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;
import org.apache.commons.dbcp2.BasicDataSource;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet("/api/v1/dashboard")
@MultipartConfig
public class DashBoardServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ServletContext servletContext = getServletContext();
        BasicDataSource basicDataSource = (BasicDataSource) servletContext.getAttribute("basicDataSource");
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            Connection connection = basicDataSource.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("select * from employee");
            ResultSet resultSet = preparedStatement.executeQuery();

            List<Map<String , String > >employeeList = new ArrayList<>();
            while (resultSet.next()){
                Map<String , String > employee = new HashMap<>();
                employee.put("email", resultSet.getString("email"));
                employee.put("name", resultSet.getString("name"));
                employee.put("salary", resultSet.getString("salary"));
                employee.put("imageUrl", resultSet.getString("image_url"));
                employee.put("id", resultSet.getString("id"));

                employeeList.add(employee);
            }

            Map<String, Object> response = new HashMap<>();
            response.put("code", "200");
            response.put("message", "employee get successfully");
            response.put("status", "success");
            response.put("data", employeeList);

            resp.setContentType("application/json");
            resp.setStatus(HttpServletResponse.SC_OK);
            objectMapper.writeValue(resp.getWriter(), response);

        } catch (SQLException e) {
            e.printStackTrace();
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            PrintWriter out = resp.getWriter();
            objectMapper.writeValue(out, Map.of(
                    "code","500",
                    "status","error",
                    "message","employee get unsuccessfully"
            ));
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ServletContext servletContext = getServletContext();
        BasicDataSource basicDataSource = (BasicDataSource) servletContext.getAttribute("basicDataSource");
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            Part image = req.getPart("image");
            String fileName = image.getSubmittedFileName();
            String uploadPath = servletContext.getRealPath("") + "images/" + fileName;
            image.write(uploadPath);

            String imageUrl =  "images/" + fileName;
            String email = req.getParameter("email");
            String name = req.getParameter("name");
            double salary = Double.parseDouble(req.getParameter("salary"));

            Connection connection = basicDataSource.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("insert into employee (email,name,salary,image_url) values (?,?,?,?)");
            preparedStatement.setString(1, email);
            preparedStatement.setString(2, name);
            preparedStatement.setDouble(3, salary);
            preparedStatement.setString(    4, imageUrl);
            preparedStatement.executeUpdate();

            resp.setContentType("application/json");
            resp.setStatus(HttpServletResponse.SC_OK);
            PrintWriter out = resp.getWriter();
            objectMapper.writeValue(out, Map.of(
                    "code","200",
                    "status","success",
                    "message","employee post successfully"
            ));


        } catch (SQLException e) {
            e.printStackTrace();
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            PrintWriter out = resp.getWriter();
            objectMapper.writeValue(out, Map.of(
                    "code","500",
                    "status","error",
                    "message","employee post unsuccessfully"
            ));

            throw new RuntimeException(e);
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ServletContext servletContext = getServletContext();
        BasicDataSource basicDataSource = (BasicDataSource) servletContext.getAttribute("basicDataSource");
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            Part image = req.getPart("image");
            String fileName = image.getSubmittedFileName();
            String uploadPath = servletContext.getRealPath("") + "images/" + fileName;
            image.write(uploadPath);

            int id = Integer.parseInt(req.getParameter("id"));
            String imageUrl =  "images/" + fileName;
            String email = req.getParameter("email");
            String name = req.getParameter("name");
            double salary = Double.parseDouble(req.getParameter("salary"));

            Connection connection = basicDataSource.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("UPDATE employee SET email = ?, name = ?, salary = ?, image_url = ? WHERE id = ?");
            preparedStatement.setString(1, email);
            preparedStatement.setString(2, name);
            preparedStatement.setDouble(3, salary);
            preparedStatement.setString(4, imageUrl);
            preparedStatement.setInt(5, id);
            preparedStatement.executeUpdate();

            resp.setContentType("application/json");
            resp.setStatus(HttpServletResponse.SC_OK);
            PrintWriter out = resp.getWriter();
            objectMapper.writeValue(out, Map.of(
                    "code","200",
                    "status","success",
                    "message","employee put successfully"
            ));
        } catch (SQLException e) {
            e.printStackTrace();
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            PrintWriter out = resp.getWriter();
            objectMapper.writeValue(out, Map.of(
                    "code","500",
                    "status","error",
                    "message","employee put unsuccessfully"
            ));
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ServletContext servletContext = getServletContext();
        BasicDataSource basicDataSource = (BasicDataSource) servletContext.getAttribute("basicDataSource");
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            String id = req.getParameter("id");

            Connection connection = basicDataSource.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM employee WHERE id = ?");
            preparedStatement.setString(1, id);
            preparedStatement.executeUpdate();

            resp.setContentType("application/json");
            resp.setStatus(HttpServletResponse.SC_OK);
            PrintWriter out = resp.getWriter();
            objectMapper.writeValue(out, Map.of(
                    "code","200",
                    "status","success",
                    "message","employee delete successfully"
            ));
        } catch (SQLException e) {
            e.printStackTrace();
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            PrintWriter out = resp.getWriter();
            objectMapper.writeValue(out, Map.of(
                    "code","500",
                    "status","error",
                    "message","employee delete unsuccessfully"
            ));
            throw new RuntimeException(e);
        }
    }
}


