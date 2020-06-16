package cn.itcast.travel.service.impl;

import cn.itcast.travel.dao.CategoryDao;
import cn.itcast.travel.dao.impl.CategoryDaoImpl;
import cn.itcast.travel.domain.Category;
import cn.itcast.travel.service.CategoryService;
import cn.itcast.travel.util.JedisUtil;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Tuple;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class CategoryServiceImpl implements CategoryService {
    private CategoryDao dao = new CategoryDaoImpl();
    @Override
    public List<Category> findAll() {
        List<Category> list = null;
        // 1.查询redis
        Jedis jedis = JedisUtil.getJedis();
        //Set<String> categorys = jedis.zrange("category", 0, -1);
        Set<Tuple> tuples = jedis.zrangeWithScores("category", 0, -1);  //查询带分数的数据
        // 1.1判断redis中是否有数据
        if (tuples == null || tuples.size() == 0){
            // 2.查询MySQL并存入redis
            list = dao.findAll();
            for (int i = 0; i < list.size(); i++){
                jedis.zadd("category",list.get(i).getCid(),list.get(i).getCname());
            }
        }else {
            list = new ArrayList<Category>();
            // 3.把redis存到list中
            for (Tuple tuple : tuples){
                Category category = new Category();
                category.setCname(tuple.getElement());
                category.setCid((int) tuple.getScore());
                list.add(category);
            }
        }
        return list;
    }
}
