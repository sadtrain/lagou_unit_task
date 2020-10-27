package pojo;

/**
 * @Description : map类
 * @Author : ZGS
 * @Date: 2020-08-15 14:56
 */
public class MappedStatement {

    private String sqlText;
    private String resultType;
    private String parameterType;
    private String resultMapStr;
    private String sqlType;


    public String getSqlType() {
        return sqlType;
    }

    public void setSqlType(String sqlType) {
        this.sqlType = sqlType;
    }

    public String getResultMapStr() {
        return resultMapStr;
    }

    public void setResultMapStr(String resultMapStr) {
        this.resultMapStr = resultMapStr;
    }

    public String getSqlText() {
        return sqlText;
    }

    public void setSqlText(String sqlText) {
        this.sqlText = sqlText;
    }

    public String getResultType() {
        return resultType;
    }

    public void setResultType(String resultType) {
        this.resultType = resultType;
    }

    public String getParameterType() {
        return parameterType;
    }

    public void setParameterType(String parameterType) {
        this.parameterType = parameterType;
    }
}
