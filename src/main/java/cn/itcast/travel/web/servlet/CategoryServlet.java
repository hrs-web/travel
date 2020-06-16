package cn.itcast.travel.web.servlet;

import cn.itcast.travel.domain.Category;
import cn.itcast.travel.service.CategoryService;
import cn.itcast.travel.service.impl.CategoryServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/category/*")
public class CategoryServlet extends BaseServlet {
    public void findAll(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // 1.调用service
        CategoryService service = new CategoryServiceImpl();
        List<Category> list = service.findAll();
        // 2.序列化json返回
        resp.setContentType("application/json;charset=utf-8");
        writeValue(list,resp);
    }
}
