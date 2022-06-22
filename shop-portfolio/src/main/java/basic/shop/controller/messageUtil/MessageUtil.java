package basic.shop.controller.messageUtil;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class MessageUtil {

    public static void init(HttpServletResponse response) throws IOException {
        response.setContentType("text/html");
        response.setCharacterEncoding("utf-8");

    }

    public static void alert(HttpServletResponse response, String msg) throws IOException {
        init(response);
        PrintWriter writer = response.getWriter();
        writer.println("<script>alert('" + msg + "'); </script>");
        writer.flush();
    }

    public static void alertAndMovePage(HttpServletResponse response, String msg, String nextPage) throws IOException {
        init(response);
        PrintWriter writer = response.getWriter();
        writer.println("<script>alert('" + msg + "'); location.href='" + nextPage + "';</script>");
        writer.flush();
    }

    public static void alertAndBackPage(HttpServletResponse response, String msg) throws IOException {
        init(response);
        PrintWriter writer = response.getWriter();
        writer.println("<script>alert('" + msg + "'); history.go(-2);</script>");
        writer.flush();
    }


}
