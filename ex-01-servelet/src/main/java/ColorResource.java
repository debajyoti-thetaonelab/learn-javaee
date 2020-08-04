import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.function.Predicate;

@WebServlet("/color")
public class ColorResource extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(this.getClass().getResourceAsStream("color_list.txt")))) {
            final String filterColor = req.getParameter("filter");
            final Predicate<String> matchPredicate;
            if (filterColor == null || filterColor.isEmpty()) {
                matchPredicate = s -> !s.startsWith("#") && !s.isEmpty();
            } else {
                final String upperColor = filterColor.toUpperCase();
                matchPredicate = s -> !s.startsWith("#") && !s.isEmpty() && s.toUpperCase().contains(upperColor);
            }
            resp.setContentType("text/html");
            final PrintWriter writer = resp.getWriter();
            writer.append("<ol>");
            br.lines()
                    .filter(matchPredicate)
                    .forEach(s -> writer.append("<li>").append(s).append("</li>"));
            writer.append("</ol>");
        }
    }
}
