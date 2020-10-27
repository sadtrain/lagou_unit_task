package config;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import io.Resources;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import pojo.Configuration;
import pojo.MappedStatement;
import pojo.ResultMap;

import java.beans.PropertyVetoException;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.*;

/**
 * @Description : 构建配置类
 * @Author : ZGS
 * @Date: 2020-08-15 15:20
 */
public class ConfigurationBuilder {

    private Configuration configuration;

    public ConfigurationBuilder(Configuration configuration){
        this.configuration = configuration;
    }

    public void parse(InputStream inputStream) throws DocumentException, PropertyVetoException {
        SAXReader saxReader = new SAXReader();
        Document document = saxReader.read(inputStream);

        Element rootElement = document.getRootElement();
        Element datasource = (Element) rootElement.selectObject("datasource");
        List<Element> dataSourceElements = datasource.elements("property");
        Properties properties = new Properties();
        for (int i = 0; i < dataSourceElements.size(); i++) {
            properties.put(dataSourceElements.get(i).attribute("name").getValue(),dataSourceElements.get(i).getTextTrim());
        }

        ComboPooledDataSource dataSource = new ComboPooledDataSource();
        dataSource.setDriverClass(properties.getProperty("driver.class"));
        dataSource.setJdbcUrl(properties.getProperty("jdbc.url"));
        dataSource.setUser(properties.getProperty("username"));
        dataSource.setPassword(properties.getProperty("password"));
//        String driverClass = properties.getProperty("driver.class");
//        String jdbcUrl = properties.getProperty("jdbc.url");
//        String username = properties.getProperty("username");
//        String password = properties.getProperty("password");
//                ComboPooledDataSource dataSource1 = new ComboPooledDataSource();
//        dataSource1.setDriverClass(driverClass);
//        dataSource1.setJdbcUrl(jdbcUrl);
//        dataSource1.setPassword(username);
//        dataSource1.setUser(password);
//        Connection connection = null;
//        try {
//            connection = dataSource.getConnection();
//        } catch (SQLException throwables) {
//            throwables.printStackTrace();
//        }finally {
//            try {
//                connection.close();
//            } catch (SQLException throwables) {
//                throwables.printStackTrace();
//            }
//        }

        configuration.setDataSource(dataSource);
        List<String> paths = new ArrayList<>();
        Element mapper = rootElement.element("mapper");
        List<Element> elements = rootElement.element("mapper").elements("property");
        List<String> locationsReg = new ArrayList<>();
        List<String> locationsAbs = new ArrayList<>();
        for (Element element : elements) {
            if("locations".equals(element.attribute("name").getValue())){
                if(element.element("list")!=null) {
                    List<Element> elements1 = element.element("list").elements("value");
                    for (Element e : elements1) {
                        locationsReg.add(e.getText());
                    }
                }
            }
            if("location".equals(element.attribute("name").getValue())){
                if(element.element("list")!=null) {
                    List<Element> elements1 = element.element("list").elements("value");
                    for (Element e : elements1) {
                        locationsReg.add(e.getText());
                    }
                }
            }
        }

        Map<String,MappedStatement> mappedStatementMap = buildMappedStatementMap(locationsReg);

        configuration.setMappedStatementMap(mappedStatementMap);


    }

    private Map<String, ResultMap> buildResultMapMap(List<Element> resultMaps) {
        Map<String, ResultMap> resultMapMap = new HashMap<>();
        for (Element element : resultMaps) {
            ResultMap resultMap = new ResultMap();
            resultMap.setId(element.attribute("id").getValue());
            resultMap.setResultType(element.attribute("resultType").getValue());
            HashMap<String, String> stringStringHashMap = new HashMap<>();
            List<Element> ids = element.elements("id");
            if(ids!=null&&ids.size()>0){
                Element id = ids.get(0);
                stringStringHashMap.put(id.attribute("property").getValue(),id.attribute("column").getValue());
            }
            List<Element> results = element.elements("result");
            if(results!=null&&results.size()>0){
                Element result = results.get(0);
                stringStringHashMap.put(result.attribute("property").getValue(),result.attribute("column").getValue());
            }
            resultMapMap.put(resultMap.getId(),resultMap);
        }
        return resultMapMap;
    }

    private Map<String, MappedStatement> buildMappedStatementMap(List<String> paths) throws DocumentException {
//        String path = "UserMapper.xml";
        Map<String, MappedStatement> idMapper = new HashMap<>();
        Map<String, ResultMap> resultMapMap = new HashMap<>();
        for (String path : paths) {
            getMappedStatementByPath(path,idMapper,resultMapMap);
        }
        configuration.setResultMapMap(resultMapMap);
        return idMapper;
    }
    public static Set<String> cruds = new HashSet<>();
    static{
        cruds.add("select");
        cruds.add("update");
        cruds.add("delete");
        cruds.add("insert");
    }

    private void getMappedStatementByPath(String path, Map<String, MappedStatement> idMapper, Map<String, ResultMap> resultMapMap) throws DocumentException {

        InputStream inputStream = Resources.getResourceAsStream(path);
        SAXReader saxReader = new SAXReader();
        Document document = saxReader.read(inputStream);
        Element rootElement = document.getRootElement();
        String statement = rootElement.element("statement").getStringValue();

        Iterator<Element> iterator = rootElement.elementIterator();
        while(iterator.hasNext()){
            Element element = iterator.next();
            System.out.println(element.getName());
            if(element.getName()!=null && cruds.contains((element.getName().toLowerCase()))){
                String id = element.attributeValue("id");
                MappedStatement mappedStatement = new MappedStatement();
                mappedStatement.setSqlText(element.getText().trim());
                mappedStatement.setSqlType(element.getName().toLowerCase());
                mappedStatement.setParameterType(element.attributeValue("parameterType"));
                mappedStatement.setResultType(element.attributeValue("resultType"));
                mappedStatement.setResultMapStr(element.attributeValue("resultMap"));
                idMapper.put(statement+"."+id,mappedStatement);
            }
        }

        List<Element> resultMaps = rootElement.elements("resultMap");
        for (Element element : resultMaps) {
            ResultMap resultMap = new ResultMap();
            resultMap.setId(element.attribute("id").getValue());
            resultMap.setResultType(element.attribute("type").getValue());
            HashMap<String, String> stringStringHashMap = new HashMap<>();
            List<Element> ids = element.elements("id");
            if(ids!=null&&ids.size()>0){
                Element id = ids.get(0);
                stringStringHashMap.put(id.attribute("property").getValue(),id.attribute("column").getValue());
            }
            List<Element> results = element.elements("result");
            if(results!=null&&results.size()>0){
                Element result = results.get(0);
                stringStringHashMap.put(result.attribute("property").getValue(),result.attribute("column").getValue());
            }
            resultMap.setPropertyColumnMap(stringStringHashMap);
            resultMapMap.put(resultMap.getId(),resultMap);
        }
    }

    public static void main(String[] args) throws DocumentException, FileNotFoundException, PropertyVetoException {
//        InputStream in = Resources.getResourceAsStream("SqlMapConfig.xml");
        InputStream in = new FileInputStream("E:\\projects\\lagou\\IPersistent\\src\\main\\resources\\SqlMapConfig.xml");
        Configuration configuration = new Configuration();

        ConfigurationBuilder configurationBuilder = new ConfigurationBuilder(new Configuration());
        configurationBuilder.parse(in);
//        configurationBuilder.buildMappedStatementMap()
        String path = "bak/UserMapper.xml";
//        Map<String, MappedStatement> stringMappedStatementMap = configurationBuilder.buildMappedStatementMap();
//        MappedStatement mappedStatementByPath = configurationBuilder.getMappedStatementByPath(path);
//        String sqlText = mappedStatementByPath.getSqlText();
//        System.out.println(sqlText);
        System.out.println(configurationBuilder.configuration);


    }
}
