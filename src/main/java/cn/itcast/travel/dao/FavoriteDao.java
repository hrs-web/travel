package cn.itcast.travel.dao;

import cn.itcast.travel.domain.Favorite;

public interface FavoriteDao {
    Favorite findByUidAndRid(int rid,int uid);

    int findCountByRid(int rid);

    void add(int rid,int uid);
}
