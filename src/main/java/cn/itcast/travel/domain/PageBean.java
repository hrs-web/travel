package cn.itcast.travel.domain;

import java.util.List;

/**
 * 分页对象
 */
public class PageBean<R> {
    private int totalCount;   // 总记录数
    private int totalPage;      // 总页数
    private int currentPage;    // 当前页码
    private int pageSize;       // 每页显示条数
    private List<Route> list;   //  每页展示的数据

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public int getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public List<Route> getList() {
        return list;
    }

    public void setList(List<Route> list) {
        this.list = list;
    }
}
