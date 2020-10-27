package sqlsession;

import com.mysql.jdbc.StringUtils;
import pojo.Configuration;
import pojo.MappedStatement;
import pojo.ResultMap;
import util.GenericTokenParser;
import util.ParameterMapping;
import util.ParameterMappingTokenHandler;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @Description :
 * @Author : ZGS
 * @Date: 2020-10-11 13:34
 */
public class SimpleExecutor implements Executor {
    @Override
    public <E> List<E>  query(Configuration configuration, MappedStatement mappedStatement, Object... objects) throws SQLException, ClassNotFoundException, NoSuchFieldException, IllegalAccessException, InstantiationException {
        String sqlText = mappedStatement.getSqlText();
        Connection connection = configuration.getDataSource().getConnection();
        ParameterMappingTokenHandler parameterMappingTokenHandler = new ParameterMappingTokenHandler();
        GenericTokenParser genericTokenParser = new GenericTokenParser("#{","}",parameterMappingTokenHandler);
        String sql = genericTokenParser.parse(sqlText);
        PreparedStatement statement = connection.prepareStatement(sql);
        List<ParameterMapping> parameterMappings = parameterMappingTokenHandler.getParameterMappings();
        if(mappedStatement.getParameterType()!=null&&mappedStatement.getParameterType().length()>0) {
            Class parameterClass = Class.forName(mappedStatement.getParameterType());
            for (int i = 0; i < parameterMappings.size(); i++) {
                ParameterMapping parameterMapping = parameterMappings.get(i);
                String content = parameterMapping.getContent();
                Field field = parameterClass.getDeclaredField(content);
                field.setAccessible(true);
                Object o = field.get(objects[0]);
                statement.setObject(i + 1, o);
            }
        }

        String resultType = mappedStatement.getResultType();
        Class resultClass;
        boolean useMap = false;
        Map<String, String> propertyColumnMap = null;
        if(!StringUtils.isNullOrEmpty(mappedStatement.getResultMapStr())){
            ResultMap resultMap = configuration.getResultMapMap().get(mappedStatement.getResultMapStr());
            propertyColumnMap = resultMap.getPropertyColumnMap();
            useMap = true;
            resultClass = Class.forName(resultMap.getResultType());
        }else{
            resultClass = Class.forName(resultType);
        }


        ResultSet resultSet = statement.executeQuery();
        List<E> list = new ArrayList<>();
        Field[] declaredFields = resultClass.getDeclaredFields();
        while(resultSet.next()){
            Object o = resultClass.newInstance();
            for (int i = 0; i < declaredFields.length; i++) {
                Field field = declaredFields[i];
                field.setAccessible(true);
                if(useMap){
                    String name = propertyColumnMap.get(field.getName());
                    if(name != null && name.length()>0){
                        field.set(o,resultSet.getObject(name));
                    }
                }else{
                    field.set(o,resultSet.getObject(field.getName()));
                }
            }
            list.add((E) o);
        }
        statement.close();
        connection.close();
        return list;
    }

    @Override
    public void  insert(Configuration configuration, MappedStatement mappedStatement, Object... objects) throws SQLException, ClassNotFoundException, NoSuchFieldException, IllegalAccessException {
        String sqlText = mappedStatement.getSqlText();
        Connection connection = configuration.getDataSource().getConnection();
        ParameterMappingTokenHandler parameterMappingTokenHandler = new ParameterMappingTokenHandler();
        GenericTokenParser genericTokenParser = new GenericTokenParser("#{","}",parameterMappingTokenHandler);
        String sql = genericTokenParser.parse(sqlText);
        PreparedStatement statement = connection.prepareStatement(sql);
        List<ParameterMapping> parameterMappings = parameterMappingTokenHandler.getParameterMappings();

        Class parameterClass=Class.forName(mappedStatement.getParameterType());
        for (int i = 0; i < parameterMappings.size(); i++) {
            ParameterMapping parameterMapping = parameterMappings.get(i);
            String content = parameterMapping.getContent();
            Field field = parameterClass.getDeclaredField(content);
            field.setAccessible(true);
            Object o = field.get(objects[0]);
            statement.setObject(i+1,o);
        }
        statement.execute();
        statement.close();
        connection.close();
    }

    @Override
    public void  update(Configuration configuration, MappedStatement mappedStatement, Object... objects) throws SQLException, ClassNotFoundException, NoSuchFieldException, IllegalAccessException {
        String sqlText = mappedStatement.getSqlText();
        Connection connection = configuration.getDataSource().getConnection();
        ParameterMappingTokenHandler parameterMappingTokenHandler = new ParameterMappingTokenHandler();
        GenericTokenParser genericTokenParser = new GenericTokenParser("#{","}",parameterMappingTokenHandler);
        String sql = genericTokenParser.parse(sqlText);
        PreparedStatement statement = connection.prepareStatement(sql);
        List<ParameterMapping> parameterMappings = parameterMappingTokenHandler.getParameterMappings();

        Class parameterClass=Class.forName(mappedStatement.getParameterType());
        for (int i = 0; i < parameterMappings.size(); i++) {
            ParameterMapping parameterMapping = parameterMappings.get(i);
            String content = parameterMapping.getContent();
            Field field = parameterClass.getDeclaredField(content);
            field.setAccessible(true);
            Object o = field.get(objects[0]);
            statement.setObject(i+1,o);
        }
        statement.execute();
        statement.close();
        connection.close();
    }

    @Override
    public void  delete(Configuration configuration, MappedStatement mappedStatement, Object... objects) throws SQLException, ClassNotFoundException, NoSuchFieldException, IllegalAccessException {
        String sqlText = mappedStatement.getSqlText();
        Connection connection = configuration.getDataSource().getConnection();
        ParameterMappingTokenHandler parameterMappingTokenHandler = new ParameterMappingTokenHandler();
        GenericTokenParser genericTokenParser = new GenericTokenParser("#{","}",parameterMappingTokenHandler);
        String sql = genericTokenParser.parse(sqlText);
        PreparedStatement statement = connection.prepareStatement(sql);
        List<ParameterMapping> parameterMappings = parameterMappingTokenHandler.getParameterMappings();

        Class parameterClass=Class.forName(mappedStatement.getParameterType());
        for (int i = 0; i < parameterMappings.size(); i++) {
            ParameterMapping parameterMapping = parameterMappings.get(i);
            String content = parameterMapping.getContent();
            Field field = parameterClass.getDeclaredField(content);
            field.setAccessible(true);
            Object o = field.get(objects[0]);
            statement.setObject(i+1,o);
        }
        statement.execute();
        statement.close();
        connection.close();
    }
}
