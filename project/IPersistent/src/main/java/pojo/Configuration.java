package pojo;

import com.mchange.v2.c3p0.ComboPooledDataSource;

import java.util.Map;

/**
 * @Description : 配置类
 * @Author : ZGS
 * @Date: 2020-08-15 14:55
 */
public class Configuration {

    private ComboPooledDataSource dataSource;

    private Map<String,MappedStatement> mappedStatementMap;
    private Map<String,ResultMap> resultMapMap;

    public Map<String, ResultMap> getResultMapMap() {
        return resultMapMap;
    }

    public void setResultMapMap(Map<String, ResultMap> resultMapMap) {
        this.resultMapMap = resultMapMap;
    }

    public ComboPooledDataSource getDataSource() {
        return dataSource;
    }

    public void setDataSource(ComboPooledDataSource dataSource) {
        this.dataSource = dataSource;
    }

    public Map<String, MappedStatement> getMappedStatementMap() {
        return mappedStatementMap;
    }

    public void setMappedStatementMap(Map<String, MappedStatement> mappedStatementMap) {
        this.mappedStatementMap = mappedStatementMap;
    }
}
