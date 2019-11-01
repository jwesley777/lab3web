package com.lab;

import com.lab.chart.Area;
import com.lab.chart.DefaultArea;
import com.lab.chart.Point;
import com.lab.model.HistoryRecord;
import com.lab.validation.Limiter;
import com.lab.validation.LimiterImpl;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@WebServlet(name = "AreaCheckServlet")
public class AreaCheckServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<Point> points;
        Area area;
        try {
            points = buildPoints(request);
            area = buildArea(request);
        } catch (Exception e) {
            request.getRequestDispatcher("/index.jsp").forward(request, response);
            return;
        }
        updateHistory(request, points, area);
        response.setContentType("text/html");
        response.setCharacterEncoding("UTF-8");
        PrintWriter writer = response.getWriter();

        writer.println("<html lang=\"ru\">");

        writer.println("<head>");
        writer.println("<link rel=\"stylesheet\" type=\"text/css\" href=\"css/result.css\">");
        writer.println("</head>");

        writer.println("<body>");

        writer.println("<div id=\"header\">");
        writer.println("<span id=\"author-name\" class=\"header-left-element header-content\">Супрун Павел Владимирович</span>");
        writer.println("<span id=\"author-group\" class=\"header-center-element header-content\">P3211</span>");
        writer.println("<span id=\"lab-variant\" class=\"header-rigth-element header-content\">Вариант 56465</span>");
        writer.println("</div>");

        writer.println("<div id=\"result-container\" class=\"horisontal-centering-container\">");
        writer.println("<h1>Результаты последнего запроса</h1>");
        writer.println("<a href=\"/\" class=\"return-link\">Вернуться на главную</a>");
        writer.println("<div id=\"result-table\" class=\"table\">");
        for (Point point : points) {
            writer.println("<div class=\"table-tr result-table-tr\">");
            writer.println("<div class=\"table-td\">X: </div>");
            writer.println("<div class=\"table-td result-value result-x\">");
            writer.println(point.getX());
            writer.println("</div>");

            writer.println("<div class=\"table-td\">Y: </div>");
            writer.println("<div class=\"table-td result-value result-y\">");
            writer.println(point.getY());
            writer.println("</div>");

            writer.println("<div class=\"table-td\">R: </div>");
            writer.println("<div class=\"table-td result-value result-r\">");
            writer.println(area.getR());
            writer.println("</div>");

            writer.println("<div class=\"table-td\">Попадание: </div>");
            writer.println("<div class=\"table-td result-value result-hit\">");
            writer.println(area.hit(point) ? "Да" : "Нет");
            writer.println("</div>");
            writer.println("</div>");
        }
        if (points.size() == 0) {
            writer.println("Некорректные данные");
        }
        writer.println("</div>");
        writer.println("</div>");

        writer.println("</body>");
        writer.println("</html>");
    }

    private List<Point> buildPoints(HttpServletRequest request) {
        List<Point> points = new ArrayList<Point>();

        if (request.getParameter("X").length() > 10) {
            throw new IllegalArgumentException();
        }
        if (request.getParameter("Y").length() > 10) {
            throw new IllegalArgumentException();
        }
        if (request.getParameter("R").length() > 10) {
            throw new IllegalArgumentException();
        }
        double x,y, r;
        try {
            x = Double.parseDouble(request.getParameter("X"));
            y = Double.parseDouble(request.getParameter("Y"));
            r = Double.parseDouble(request.getParameter("R"));
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(e);
        }
        boolean isForm;
        try {
            if (request.getParameter("isForm") != null || Boolean.parseBoolean(request.getParameter("isForm")) == true) {
                isForm = true;
            } else {
                isForm = false;
            }
        } catch (NumberFormatException e) {
            isForm = false;
        }
        Limiter limiter = new LimiterImpl();
        try {
            Point point = new Point(x, y);
            if (!isForm || limiter.isInLimits(point.getX(), point.getY(), r)) {
                points.add(point);
            }
        } catch (NumberFormatException e) {
        }
        return points;
    }

    private Area buildArea(HttpServletRequest request) {
        try {
            return new DefaultArea(Double.parseDouble(request.getParameter("R")));
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(e);
        }
    }

    private void updateHistory(HttpServletRequest request, List<Point> points, Area area) {
        List<HistoryRecord> records = new ArrayList<HistoryRecord>();
        for (Point point : points) {
            records.add(new HistoryRecord(point, area.getR(), area.hit(point)));
        }
        List<HistoryRecord> history = (List<HistoryRecord>)getServletContext().getAttribute("history");
        //(List<HistoryRecord>)request.getSession().getAttribute("history");
        if (history == null) {
            history = Collections.synchronizedList(new ArrayList<HistoryRecord>());
            getServletContext().setAttribute("history", history);
            //request.getSession().setAttribute("history", history);
        }
        Collections.reverse(records);
        history.addAll(records);
    }
}
