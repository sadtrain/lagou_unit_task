package mapper;

import server.HttpServlet;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Description :
 * @Author : ZGS
 * @Date: 2020-12-25 21:55
 */
public class Host {
    public ConcurrentHashMap<String, HttpServlet> servletMap = new ConcurrentHashMap<String,HttpServlet>();
}
