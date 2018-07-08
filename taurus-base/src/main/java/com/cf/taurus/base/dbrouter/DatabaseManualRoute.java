package com.cf.taurus.base.dbrouter;

import javax.sql.DataSource;
import java.util.Map;

/**
 * DatabaseManualRoute
 *
 * @author 于文硕
 * @since 2018/5/11 17:47
 */
public class DatabaseManualRoute {
    private DatabaseRoute dataBaseRoute;
    private Map<Integer, DataSource> clusterSources;

    public DatabaseManualRoute() {
    }

    public void init(DatabaseRoute dataBaseRoute, Map<Integer, DataSource> clusterSources) {
        this.clusterSources = clusterSources;
        this.dataBaseRoute = dataBaseRoute;
    }

    public int getDbSize() {
        return null == this.clusterSources ? 0 : this.clusterSources.size();
    }

    public void changeDataSource(int index) throws Exception {
        if (null != this.clusterSources && null != this.dataBaseRoute) {
            if (index > this.clusterSources.size()) {
                throw new Exception("can not find this datasource,for index is:" + index);
            } else {
                DatabaseRoute._select.set(index);
            }
        } else {
            throw new Exception("no datasource avail or no datasource router");
        }
    }
}
