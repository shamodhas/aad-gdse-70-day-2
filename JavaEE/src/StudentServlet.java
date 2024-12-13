import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

@WebServlet(urlPatterns = "/student")
public class StudentServlet extends HttpServlet {
    private final List<StudentDTO> studentDTOList = new ArrayList<>();

    @Override
    public void init(ServletConfig config) throws ServletException {
        studentDTOList.add(new StudentDTO(1, "Alice", "alice@gmail.com", 20));
        studentDTOList.add(new StudentDTO(2, "Bob", "bob@gmail.com", 22));
        studentDTOList.add(new StudentDTO(3, "Charlie", "charlie@gmail.com", 19));
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String idString = req.getParameter("id");
        String name = req.getParameter("name");
        String email = req.getParameter("email");
        String ageString = req.getParameter("age");

        System.out.println(idString);
        System.out.println(name);
        System.out.println(email);
        System.out.println(ageString);

        if (idString == null || idString.isEmpty()) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write("{\"error\" : \"ID is required..!\"}");
        } else {
            try {
                int id = Integer.parseInt(idString);
                int age = Integer.parseInt(ageString);

                StudentDTO studentDTO = findFindById(id);
                if (studentDTO == null) {
                    resp.setStatus(HttpServletResponse.SC_NOT_FOUND); // 404
                    resp.getWriter().write("{\"error\" : \"Student not found\"}");
                } else {
                    studentDTO.setName(name);
                    studentDTO.setEmail(email);
                    studentDTO.setAge(age);

                    resp.getWriter().write("{\"message\" : \"Student update successful\"}");
                }
            } catch (NumberFormatException e) {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                resp.getWriter().write("{\"error\" : \"Invalid ID or age\"}");
            }
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String idString = req.getParameter("id");

        if (idString == null || idString.isEmpty()) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write("{\"error\" : \"ID is required..!\"}");
        } else {
            try {
                int id = Integer.parseInt(idString);
                StudentDTO studentDTO = findFindById(id);

                if (studentDTO == null) {
                    resp.setStatus(HttpServletResponse.SC_NOT_FOUND); // 404
                    resp.getWriter().write("{\"error\" : \"Student not found\"}");
                } else {
                    studentDTOList.remove(studentDTO);
                    resp.getWriter().write("{\"message\" : \"Student delete successful\"}");
                }
            } catch (NumberFormatException e) {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                resp.getWriter().write("{\"error\" : \"Invalid ID\"}");
            }
        }
    }

    private StudentDTO findFindById(int id) {
        for (StudentDTO studentDTO : studentDTOList) {
            if (studentDTO.getId() == id) {
                return studentDTO;
            }
        }
        return null;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        PrintWriter printWriter = resp.getWriter();

//        Json object list
//        [
//                {
//                'id':1,
//                'name':'kamal',
//                'email':'kamal@gmail.com',
//                'age':2
//            } ,
//        {
//        'id':1,
//            'name':'kamal',
//                'email':'kamal@gmail.com',
//                'age':2
//        }
//        ]

//        String studentJsonListString = "";

        StringBuilder studentJsonList = new StringBuilder("[");
        for (int i = 0; i < studentDTOList.size(); i++) {
            StudentDTO studentDTO = studentDTOList.get(i);
            String studentJson = String.format(
                    "{\"id\": %d, \"name\": \"%s\", \"email\": \"%s\", \"age\": %d}",
                    studentDTO.getId(),
                    studentDTO.getName(),
                    studentDTO.getEmail(),
                    studentDTO.getAge()
            );
            studentJsonList.append(studentJson);
            if (i < studentDTOList.size() - 1) {
                studentJsonList.append(',');
            }
        }
        studentJsonList.append(']');
        String studentJsonListString = studentJsonList.toString();

        printWriter.write(studentJsonListString);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String name = req.getParameter("name");
        String email = req.getParameter("email");
        String ageString = req.getParameter("age");

        if (name == null || name.isEmpty() || email == null || email.isEmpty() || ageString == null || ageString.isEmpty()) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write("{\"error\" : \"Name, email and age are required\"}");
        }

        try {
            int id = studentDTOList.size() + 1;
            int age = Integer.parseInt(ageString);

            StudentDTO studentDTO = new StudentDTO(id, name, email, age);
            studentDTOList.add(studentDTO);

            resp.setStatus(HttpServletResponse.SC_CREATED); // 201
        } catch (NumberFormatException e) {
            System.out.println("invalid age");

            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write("{\"error\" : \"Invalid age\"}");
        }
    }

}


















