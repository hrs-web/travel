package cn.itcast.travel.service.impl;

import cn.itcast.travel.dao.UserDao;
import cn.itcast.travel.dao.impl.UserDaoImpl;
import cn.itcast.travel.domain.User;
import cn.itcast.travel.service.UserService;
import cn.itcast.travel.util.MailUtils;
import cn.itcast.travel.util.UuidUtil;

public class UserServiceImpl implements UserService {
    private UserDao dao = new UserDaoImpl();
    @Override
    public boolean registerUser(User user) {
        // 1.判断用户名是否存在
        User username = dao.findByUsername(user.getUsername());
        if (username != null){
            // 用户名存在
            return false;
        }
        // 2.保存用户
        user.setCode(UuidUtil.getUuid());
        user.setStatus("N");
        dao.save(user);
        // 3.激活邮箱发送
        String content = "<a href = 'http://192.168.19.120:8080/travel/user/active?code="+user.getCode()+"'>点击激活【优享旅游网】</a>";
        MailUtils.sendMail(user.getEmail(),content,"激活邮件");

        return true;
    }

    /**
     * 激活用户
     * @param code
     * @return
     */
    @Override
    public boolean active(String code) {
        // 1.调用Dao验证激活码是否有效
        User user = dao.findUserCode(code);
        if (user != null){
            // 2.调用Dao修改Stuart状态码
            dao.updateStatus(user);
            return true;
        }else {
            return false;
        }
    }

    /**
     * 用户登录
     * @param user
     * @return
     */
    @Override
    public User login(User user) {
        return dao.findByUsernameAndPassword(user.getUsername(),user.getPassword());
    }
}
