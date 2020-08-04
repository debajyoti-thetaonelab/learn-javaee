import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

@WebServlet("/colorful-page")
public class ColorfulPage extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html");
        StringBuilder doc = makePage(null);
        final PrintWriter writer = resp.getWriter();
        writer.append(doc);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        final String selectedColor = req.getParameter("color");
        resp.setContentType("text/html");
        StringBuilder doc = makePage(selectedColor);
        final PrintWriter writer = resp.getWriter();
        writer.append(doc);
    }

    private StringBuilder makePage(String selectedColor) throws IOException {
        final StringBuilder style = new StringBuilder();
        final String prompt;
        style.append("body {");
        style.append("\n\tdisplay: flex;")
                .append("\n\tflex-direction: column;")
                .append("\n\tjustify-content: center;")
                .append("\n\talign-items: center;")
                .append("\n\tmin-height: 100vh;");
        if (selectedColor != null) {
            style.append("\n\tbackground-color: ").append(selectedColor).append(';');
            prompt = "You have selected " + selectedColor;
        } else {
            prompt = "Please select a color";
        }
        style.append("\n}");
        style.append("\nform {")
                .append("\n\tpadding: 12px 34px 48px;")
                .append("\n\tborder: 1px solid black;")
                .append("\n\ttext-align: left;")
                .append("\n}");
        StringBuilder colorOptions = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(this.getClass().getResourceAsStream("color_list.txt")))) {
            br.lines()
                    .filter(s -> !s.startsWith("#") && !s.isEmpty())
                    .forEach(s ->
                            colorOptions.append("<option value='")
                                    .append(s)
                                    .append('\'')
                                    .append(s.equals(selectedColor) ? " selected " : "")
                                    .append(">")
                                    .append(s)
                                    .append("</option>"));
        }
        StringBuilder doc = new StringBuilder();
        doc.append("<html>")
                .append("<head>")
                .append("<title>").append("Colorful Page").append("</title>")
                .append("<style>").append(style).append("</style>")
                .append("</head>")
                .append("<body>")
                .append("<form name='goodform' action='' method='POST'>")
                .append("<h3>").append(prompt).append("</h3>")
                .append("<select autofocus required name='color' onchange='document.goodform.submit()'>")
                .append(colorOptions)
                .append("</select>")
                .append("</form>")
                .append("</body>")
                .append("</html>");
        return doc;
    }
}
