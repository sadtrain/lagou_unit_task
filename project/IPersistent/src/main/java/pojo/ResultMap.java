package pojo;

import java.util.Map;

public class ResultMap {

    private String id;
    private String resultType;
    private Map<String,String> propertyColumnMap;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getResultType() {
        return resultType;
    }

    public void setResultType(String resultType) {
        this.resultType = resultType;
    }

    public Map<String, String> getPropertyColumnMap() {
        return propertyColumnMap;
    }

    public void setPropertyColumnMap(Map<String, String> propertyColumnMap) {
        this.propertyColumnMap = propertyColumnMap;
    }
}
