package cn.itcast.travel.service.impl;

import cn.itcast.travel.dao.FavoriteDao;
import cn.itcast.travel.dao.impl.FavoriteDaoImpl;
import cn.itcast.travel.domain.Favorite;
import cn.itcast.travel.service.FavoriteService;

public class FavoriteServiceImpl implements FavoriteService {
    private FavoriteDao favoriteDao = new FavoriteDaoImpl();
    /**
     * 查询用户是否收藏该路线
     * @param rid
     * @param uid
     * @return
     */
    @Override
    public boolean isFavorite(String rid, int uid) {
        // 调用dao完成查询
        Favorite favorite = favoriteDao.findByUidAndRid(Integer.parseInt(rid), uid);

        // 如果对象有值则返回true，反之，则返回false
        return favorite != null;
    }

    /**
     * 添加收藏
     * @param rid
     * @param uid
     */
    @Override
    public void add(String rid, int uid) {
        favoriteDao.add(Integer.parseInt(rid),uid);
    }


}
