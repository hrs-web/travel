package cn.itcast.travel.web.servlet;

import cn.itcast.travel.domain.ResultInfo;
import cn.itcast.travel.domain.User;
import cn.itcast.travel.service.UserService;
import cn.itcast.travel.service.impl.UserServiceImpl;
import org.apache.commons.beanutils.BeanUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;

@WebServlet("/user/*")
public class UserServlet extends BaseServlet {
    private UserService service = new UserServiceImpl();
    /**
     * 注册功能
     * @param req
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
    public void register(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // 0.验证码效验
        String check = req.getParameter("check");  // 用户输入的验证码
        HttpSession session = req.getSession();
        String checkCode = (String)session.getAttribute("CHECKCODE_SERVER"); // 随机生成的验证码
        session.removeAttribute("CHECKCODE_SERVER");
        // 判断用户输入是否正确
        if (checkCode == null || !checkCode.equalsIgnoreCase(check)){
            ResultInfo rf = new ResultInfo();
            rf.setFlag(false);
            rf.setErrorMsg("验证码错误");
            // 将错误信息封装成json数据发送给前端
            String json = writeValueAsSting(rf);
            resp.setContentType("application/json;charset=utf-8");
            resp.getWriter().write(json);
            return;
        }

        // 1.获取请求消息
        Map<String, String[]> map = req.getParameterMap();

        // 2.封装到实体类上
        User user = new User();
        try {
            BeanUtils.populate(user, map);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        // 3.调用service完成注册
        boolean flag = service.registerUser(user);
        ResultInfo info = new ResultInfo();
        if (flag){
            // 注册成功
            info.setFlag(true);
        }else {
            // 注册失败
            info.setFlag(false);
            info.setErrorMsg("注册失败，可能用户名已存在");
        }
        // 4.将info对象序列化为json
        String json = writeValueAsSting(info);
        // 5.将json数据发送给前端
        resp.setContentType("application/json;charset=utf-8");
        resp.getWriter().write(json);
    }

    /**
     * 激活功能
     * @param req
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
    public void active(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // 1.获取激活码
        String code = req.getParameter("code");
        // 2.调用server完成激活
        boolean flag = service.active(code);
        String msg = null;
        if (flag){
            msg = "验证通过，请点击<a href = '"+req.getContextPath()+"/login.html'>登录</a>";
        }else {
            msg = "验证不通过，请联系管理员";
        }
        resp.setContentType("text/html;charset=utf-8");
        resp.getWriter().write(msg);
    }

    /**
     * 登录功能
     * @param req
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
    public void login(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // 0.效验验证码
        String check = req.getParameter("check");
        HttpSession session = req.getSession();
        String checkCode = (String)session.getAttribute("CHECKCODE_SERVER");
        session.removeAttribute("CHECKCODE_SERVER");
        if (checkCode == null || !checkCode.equalsIgnoreCase(check)){
            // 响应封装json数据
            ResultInfo info = new ResultInfo();
            info.setFlag(false);
            info.setErrorMsg("验证码错误");
            resp.setContentType("application/json;charset=utf-8");
            writeValue(info,resp);
            return;
        }

        // 1.获取请求数据
        Map<String, String[]> map = req.getParameterMap();

        // 2.封装
        User user = new User();
        try {
            BeanUtils.populate(user,map);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

        // 3.调用service完成登录
        User u = service.login(user);

        ResultInfo info = new ResultInfo();

        // 4.账号或密码错误
        if (u == null){
            info.setFlag(false);
            info.setErrorMsg("账号或密码错误");
        }
        // 5.未激活
        if (u != null && !"Y".equals(u.getStatus())){
            info.setFlag(false);
            info.setErrorMsg("该账号未激活，请先去邮箱激活");
        }
        // 6.登录成功
        if (u != null && "Y".equals(u.getStatus())){
            req.getSession().setAttribute("user",u);
            info.setFlag(true);
        }
        // 7.响应封装json数据
        resp.setContentType("application/json;charset=utf-8");
        String json = writeValueAsSting(info);
        resp.getWriter().write(json);
    }

    /**
     * 查询单用户功能
     * @param req
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
    public void findOne(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // 从session中获取user对象
        HttpSession session = req.getSession();
        Object user = session.getAttribute("user");

        // 响应给客户端
        resp.setContentType("application/json;charset=utf-8");
        writeValue(user,resp);
    }

    /**
     * 退出功能
     * @param req
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
    public void exit(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // 删除session中的user
        HttpSession session = req.getSession();
        session.invalidate();  // 销毁session

        // 跳转登录界面
        resp.sendRedirect(req.getContextPath()+"/login.html");
    }
}
