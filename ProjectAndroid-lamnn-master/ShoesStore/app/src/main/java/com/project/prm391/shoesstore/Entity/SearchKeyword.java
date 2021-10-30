package com.project.prm391.shoesstore.Entity;

import java.util.Date;

/**
 * Created by nguyen on 3/25/2018.
 */

public class SearchKeyword {
    private String keyword;
    private int count = 0;
    private Date lastTimeSearch = new Date();

    public SearchKeyword() {
    }

    public SearchKeyword(String keyword) {
        this.keyword = keyword;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public Date getLastTimeSearch() {
        return lastTimeSearch;
    }

    public void setLastTimeSearch(Date lastTimeSearch) {
        this.lastTimeSearch = lastTimeSearch;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SearchKeyword that = (SearchKeyword) o;

        return keyword != null ? keyword.equals(that.keyword) : that.keyword == null;
    }

    @Override
    public int hashCode() {
        return keyword != null ? keyword.hashCode() : 0;
    }

    @Override
    public String toString() {
        return keyword;
    }
}
