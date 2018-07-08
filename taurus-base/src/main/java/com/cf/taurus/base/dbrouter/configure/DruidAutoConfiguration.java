package com.cf.taurus.base.dbrouter.configure;



import com.alibaba.druid.pool.DruidDataSource;
import com.cf.taurus.base.dbrouter.DatabaseManualRoute;
import com.cf.taurus.base.dbrouter.DatabaseRoute;
import com.cf.taurus.base.dbrouter.DatabaseRouteRegister;
import com.cf.taurus.base.dbrouter.interceptor.SplitInterceptor;
import com.cf.taurus.base.dbrouter.interceptor.TransactionalInterceptor;
import com.cf.taurus.base.dbrouter.configure.DruidProperties.DataSourceConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * DruidAutoConfiguration
 *
 * @author 于文硕
 * @since 2018/5/11 17:15
 */
@Slf4j
@Configuration
@ConditionalOnProperty(
        prefix = "data-base-router",
        name = {"enabled"},
        havingValue = "true"
)
@EnableConfigurationProperties({DruidProperties.class})
public class DruidAutoConfiguration {
    private final int SINGLE_TYPE = 1;
    private final int CLUSTER_TYPE = 2;
    private final int SINGLE_INDEX = 100;
    private final int CLUSTER_INDEX = 0;
    private final int MAX_CLUSTER_NUM = 32;

    @Bean
    @ConditionalOnMissingBean({DatabaseRoute.class})
    public DatabaseRoute DatabaseRoute(DruidProperties properties) {
        List<DataSourceConfig> sourceConfigs = properties.getCluster();
        if (sourceConfigs != null && sourceConfigs.size() > 32) {
            log.error(String.format("cluster db number must lt %d", Integer.valueOf(32)));
            throw new IllegalArgumentException(String.format("cluster db number must lt %d", Integer.valueOf(32)));
        } else {
            Map<Integer, DataSource> singleSource = this.buildDataSourceMap(properties.getSingle(), 1);
            Map<Integer, DataSource> clusterSource = this.buildDataSourceMap(properties.getCluster(), 2);
            DatabaseRoute DatabaseRoute = new DatabaseRoute();
            DatabaseRoute.setTargetDataSources(clusterSource, singleSource);
            if (singleSource.containsKey(Integer.valueOf(100))) {
                DatabaseRoute.setDefaultTargetDataSource(singleSource.get(Integer.valueOf(100)));
            } else {
                if (!clusterSource.containsKey(Integer.valueOf(0))) {
                    log.error("can not set default data source");
                    throw new RuntimeException("can not set default data source");
                }

                DatabaseRoute.setDefaultTargetDataSource(clusterSource.get(Integer.valueOf(0)));
            }

            log.info("DataBase route config success");
            return DatabaseRoute;
        }
    }


    @Bean
    @ConditionalOnBean({DatabaseRoute.class})
    @ConditionalOnMissingBean({DatabaseManualRoute.class})
    public DatabaseManualRoute dataBaseManualRoute(DruidProperties properties, DatabaseRoute DatabaseRoute) {
        DatabaseManualRoute dataBaseManualRoute = new DatabaseManualRoute();
        dataBaseManualRoute.init(DatabaseRoute, this.buildDataSourceMap(properties.getCluster(), 0));
        return dataBaseManualRoute;
    }

    @Bean
    @ConditionalOnBean({DatabaseRoute.class})
    @ConditionalOnMissingBean({SplitInterceptor.class})
    public SplitInterceptor splitInterceptor() {
        return new SplitInterceptor(1);
    }

    @Bean
    @ConditionalOnBean({DatabaseRoute.class})
    @ConditionalOnMissingBean({TransactionalInterceptor.class})
    public TransactionalInterceptor transactionalInterceptor() {
        return new TransactionalInterceptor(2);
    }

    private Map<Integer, DataSource> buildDataSourceMap(List<DataSourceConfig> sourceConfigs, int type) {
         Map<Integer, DataSource> sourceMap = new HashMap();
        if (sourceConfigs != null && !sourceConfigs.isEmpty()) {
            DataSourceConfig config = null;

            for(int i = 0; i < sourceConfigs.size(); ++i) {
                config = sourceConfigs.get(i);
                if (type == 1) {
                    sourceMap.put(i + 100, this.buildDataSource(config));
                    String alias = config.getAlias();
                    if (alias != null && alias.length() > 0) {
                        DatabaseRouteRegister.registSingleRouter(config.getAlias(), i + 100);
                    }
                } else {
                    sourceMap.put(i + 0, this.buildDataSource(config));
                }
            }

            return sourceMap;
        } else {
            return sourceMap;
        }
    }

    private DataSource buildDataSource(DataSourceConfig config) {
        log.debug("DATASOURCE CONFIG: {}", config.toString());
        DruidDataSource dataSource = new DruidDataSource();
        BeanUtils.copyProperties(config,dataSource);
        return dataSource;
    }
}
