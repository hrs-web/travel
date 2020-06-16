package cn.itcast.travel.web.servlet;

import cn.itcast.travel.domain.PageBean;
import cn.itcast.travel.domain.Route;
import cn.itcast.travel.domain.User;
import cn.itcast.travel.service.FavoriteService;
import cn.itcast.travel.service.RouteService;
import cn.itcast.travel.service.impl.FavoriteServiceImpl;
import cn.itcast.travel.service.impl.RouteServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/route/*")
public class RouteServlet extends BaseServlet {
    private RouteService routeService = new RouteServiceImpl();
    private FavoriteService favoriteService = new FavoriteServiceImpl();
    /**
     * 分页查询
     * @param req
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
    public void pageQuery(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // 1.获取数据
        String cidStr = req.getParameter("cid");
        String currentPageStr = req.getParameter("currentPage");
        String pageSizeStr = req.getParameter("pageSize");
        String rname = req.getParameter("rname"); //   name="è¥¿å®"
        rname = new String(rname.getBytes("iso-8859-1"),"utf-8");  // name="西安“

        // 2.处理数据
        int cid = 0;  // 类别id
        if (cidStr != null && cidStr.length() > 0){
            cid = Integer.parseInt(cidStr);
        }
        int currentPage = 0; // 当前页码，如果不转递参数，则为1
        if (currentPageStr != null && currentPageStr.length() > 0){
            currentPage = Integer.parseInt(currentPageStr);
        }else {
            currentPage = 1;
        }
        int pageSize = 0;  // 每页显示条数，如果没转递参数，则为5
        if (pageSizeStr != null && pageSizeStr.length() > 0){
            pageSize = Integer.parseInt(pageSizeStr);
        }else {
            pageSize = 5;
        }


        // 3.调用service查询pageBean对象
        PageBean<Route> pb = routeService.pageQuery(cid, currentPage, pageSize,rname);

        // 4.将pageBean对象序列化为json，返回
        resp.setContentType("application/json;charset=utf-8");
        writeValue(pb,resp);
    }

    /**
     *查询商品信息
     * @param req
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
    public void findOne(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // 1.获取参数
        String rid = req.getParameter("rid");

        // 2.调用service完成查询
        Route route = routeService.findOne(rid);

        // 3.序列化为json并发回给前端
        resp.setContentType("application/json;charset=utf-8");
        writeValue(route,resp);
    }

    /**
     * 查询用户是否收藏该线路
     * @param req
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
    public void isFavorite(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // 1.获取线路id
        String rid = req.getParameter("rid");
        // 2.获取当前登录的用户
        User user = (User)req.getSession().getAttribute("user");
        int uid; // 用户id
        // 2.1.判断是否登录
        if (user == null){
            // 用户未登录
            uid = 0;
        }else {
            // 用户已登录
            uid = user.getUid();
        }

        // 3.调用service完成查询
        boolean flag = favoriteService.isFavorite(rid, uid);

        // 4.序列化啊为json并返回
        resp.setContentType("application/json;charset=utf-8");
        writeValue(flag,resp);
    }

    /**
     * 添加收藏
     * @param req
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
    public void addFavorite(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // 1.获取参数
        String rid = req.getParameter("rid");

        // 2.判断是否登录
        User user = (User)req.getSession().getAttribute("user");
        int uid;  // 定义用户id
        if (user != null){
            // 用户已登录
            uid = user.getUid();
        }else {
            // 用户未登录
            return;
        }

        // 3.调用service添加
        favoriteService.add(rid,uid);
    }

}
