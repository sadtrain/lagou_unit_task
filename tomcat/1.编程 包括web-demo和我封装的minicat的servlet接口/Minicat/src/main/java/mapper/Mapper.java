package mapper;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Description :
 * @Author : ZGS
 * @Date: 2020-12-25 21:55
 */
public class Mapper {
    public ConcurrentHashMap<String,Host> hosts = new ConcurrentHashMap<>();
}
