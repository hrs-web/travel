package cn.itcast.travel.service.impl;

import cn.itcast.travel.dao.FavoriteDao;
import cn.itcast.travel.dao.RouteDao;
import cn.itcast.travel.dao.RouteImgDao;
import cn.itcast.travel.dao.SellerDao;
import cn.itcast.travel.dao.impl.FavoriteDaoImpl;
import cn.itcast.travel.dao.impl.RouteDaoImpl;
import cn.itcast.travel.dao.impl.RouteImgDaoImpl;
import cn.itcast.travel.dao.impl.SellerDaoImpl;
import cn.itcast.travel.domain.PageBean;
import cn.itcast.travel.domain.Route;
import cn.itcast.travel.domain.RouteImg;
import cn.itcast.travel.domain.Seller;
import cn.itcast.travel.service.RouteService;

import java.util.List;

public class RouteServiceImpl implements RouteService {
    private RouteDao routeDao = new RouteDaoImpl();
    private SellerDao sellerDao = new SellerDaoImpl();
    private RouteImgDao routeImgDao = new RouteImgDaoImpl();
    private FavoriteDao favoriteDao = new FavoriteDaoImpl();
    /**
     * 根据类别进行分页查询
     * @param cid
     * @param currentPage
     * @param pageSize
     * @return
     */
    @Override
    public PageBean<Route> pageQuery(int cid, int currentPage, int pageSize,String rname) {
        // 封装pageBean对象
        PageBean<Route> pb = new PageBean<>();
        // 设置当前页码
        pb.setCurrentPage(currentPage);
        // 设置页面显示条数
        pb.setPageSize(pageSize);
        // 设置总记录数
        int totalCount = routeDao.findTotalCount(cid,rname);
        pb.setTotalCount(totalCount);
        // 设置总页码 = 总条数 / 页面显示条数
        int totalPage = totalCount % pageSize == 0 ? totalCount / pageSize : (totalCount / pageSize) + 1;
        pb.setTotalPage(totalPage);
        // 设置每页展示的数据
        int start = (currentPage-1) * pageSize;  // 开始的记录数
        List<Route> list = routeDao.findByPage(cid, start, pageSize,rname);
        pb.setList(list);

        return pb;
    }

    @Override
    public Route findOne(String rid) {
        // 1.根据rid查询商品信息并封装到route对象
        Route route = routeDao.findOne(Integer.parseInt(rid));

        // 2.根据sid查询商家信息并封装到route对象里
        int sid = route.getSid();
        Seller seller = sellerDao.findBySid(sid);
        route.setSeller(seller);

        // 3.根据rid查询商品图片并封装到route对象里
        List<RouteImg> list = routeImgDao.findByRid(rid);
        route.setRouteImgList(list);

        // 4.查询收藏次数
        int count = favoriteDao.findCountByRid(Integer.parseInt(rid));
        route.setCount(count);

        return route;
    }
}
