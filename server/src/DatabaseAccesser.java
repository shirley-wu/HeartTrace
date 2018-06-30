
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;

@WebServlet(name = "DatabaseAccesser")
public class DatabaseAccesser extends HttpServlet {
    // JDBC 驱动器名称和数据库的 URL
    private static final String JDBC_DRIVER="com.mysql.jdbc.Driver";
    private static final String DB_URL="jdbc:mysql://localhost:3306/TEST";

    //  数据库的用户名与密码，需要根据自己的设置
    private static final String USER = "root";
    private static final String PASS = "huangzp";

    public DatabaseAccesser() {
        super();
        // TODO Auto-generated constructor stub
    }

    public void init(){

    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Connection connection = null;
        Statement statement = null;
        response.setContentType("text/html");
        PrintWriter writer = response.getWriter();
        try{
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection(DB_URL, USER, PASS);
            statement = connection.createStatement();
            String sql = "SELECT id, name FROM username";
            ResultSet rs = statement.executeQuery(sql);

            while (rs.next()){
                int id = rs.getInt("id");
                String name = rs.getString("name");
                writer.println("<h1>" + "database" + "</h1>");
                writer.println("<h1>" + id + "</h1>");
                writer.println("<h1>" + name + "</h1>");
            }
            rs.close();
            connection.close();
            statement.close();
        }catch(SQLException se){
            // 处理 JDBC 错误
            se.printStackTrace();
        }catch(Exception e){
            // 处理 Class.forName 错误
            e.printStackTrace();
        }finally{
            // 关闭资源
            try{
                if(statement!=null) statement.close();
            }catch(SQLException se2){
            }// 什么都不做
            try{
                if(connection!=null) connection.close();
            }catch(SQLException se){
                se.printStackTrace();
            }
        }
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request,response);
    }

    public void destroy(){

    }
}
