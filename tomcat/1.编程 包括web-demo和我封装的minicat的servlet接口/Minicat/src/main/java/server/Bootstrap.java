package server;

import classloader.LoadClass;
import classloader.WebClassLoader;
import mapper.Host;
import mapper.Mapper;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

/**
 * Minicat的主类
 */
public class Bootstrap {

    /**定义socket监听的端口号*/
    private int port = 8080;

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }


    /**
     * Minicat启动需要初始化展开的一些操作
     */
    public void start() throws Exception {
        String webDir = "F:\\github\\lagou\\tomcat\\Minicat\\webapps";
//        new LoadClass(webClassLoader).loadWebs(listFile.getAbsolutePath());
        File file = new File(webDir);
        //webapps目录
        for (File listFile : file.listFiles()) {

            //每个web
            WebClassLoader webClassLoader = new WebClassLoader();
//            new LoadClass(webClassLoader).loadWebs(listFile.getAbsolutePath());
            LoadClass loadClass = new LoadClass(webClassLoader);
            loadClass.loadWeb(listFile.getAbsolutePath());
            loadClass.printAll();
            InputStream inputStream = getWebXMLStream(listFile);
            Host host = new Host();
            loadServlet(inputStream,host);
            mapper.hosts.put(listFile.getName(),host);

        }
        // 加载解析相关的配置，web.xml
//        loadServlet();
//        loadServlet();


        // 定义一个线程池
        int corePoolSize = 10;
        int maximumPoolSize =50;
        long keepAliveTime = 100L;
        TimeUnit unit = TimeUnit.SECONDS;
        BlockingQueue<Runnable> workQueue = new ArrayBlockingQueue<>(50);
        ThreadFactory threadFactory = Executors.defaultThreadFactory();
        RejectedExecutionHandler handler = new ThreadPoolExecutor.AbortPolicy();


        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(
                corePoolSize,
                maximumPoolSize,
                keepAliveTime,
                unit,
                workQueue,
                threadFactory,
                handler
        );





        /*
            完成Minicat 1.0版本
            需求：浏览器请求http://localhost:8080,返回一个固定的字符串到页面"Hello Minicat!"
         */
        ServerSocket serverSocket = new ServerSocket(port);
        System.out.println("=====>>>Minicat start on port：" + port);

        /*while(true) {
            Socket socket = serverSocket.accept();
            // 有了socket，接收到请求，获取输出流
            OutputStream outputStream = socket.getOutputStream();
            String data = "Hello Minicat!";
            String responseText = HttpProtocolUtil.getHttpHeader200(data.getBytes().length) + data;
            outputStream.write(responseText.getBytes());
            socket.close();
        }*/


        /**
         * 完成Minicat 2.0版本
         * 需求：封装Request和Response对象，返回html静态资源文件
         */
        /*while(true) {
            Socket socket = serverSocket.accept();
            InputStream inputStream = socket.getInputStream();

            // 封装Request对象和Response对象
            Request request = new Request(inputStream);
            Response response = new Response(socket.getOutputStream());

            response.outputHtml(request.getUrl());
            socket.close();

        }*/


        /**
         * 完成Minicat 3.0版本
         * 需求：可以请求动态资源（Servlet）
         */
        /*while(true) {
            Socket socket = serverSocket.accept();
            InputStream inputStream = socket.getInputStream();

            // 封装Request对象和Response对象
            Request request = new Request(inputStream);
            Response response = new Response(socket.getOutputStream());

            // 静态资源处理
            if(servletMap.get(request.getUrl()) == null) {
                response.outputHtml(request.getUrl());
            }else{
                // 动态资源servlet请求
                HttpServlet httpServlet = servletMap.get(request.getUrl());
                httpServlet.service(request,response);
            }

            socket.close();

        }
*/

        /*
            多线程改造（不使用线程池）
         */
        /*while(true) {
            Socket socket = serverSocket.accept();
            RequestProcessor requestProcessor = new RequestProcessor(socket,servletMap);
            requestProcessor.start();
        }*/



        System.out.println("=========>>>>>>使用线程池进行多线程改造");
        /*
            多线程改造（使用线程池）
         */
        while(true) {

            Socket socket = serverSocket.accept();
            RequestProcessor requestProcessor = new RequestProcessor(socket,mapper);
            //requestProcessor.start();
            threadPoolExecutor.execute(requestProcessor);
        }



    }


//    private Map<String,HttpServlet> servletMap = new HashMap<String,HttpServlet>();
    private Mapper mapper = new Mapper();

    /**
     * 加载解析web.xml，初始化Servlet
     */
//    private void loadServlet() {
//        InputStream resourceAsStream = this.getClass().getClassLoader().getResourceAsStream("web.xml");
//        SAXReader saxReader = new SAXReader();
//
//        try {
//            Document document = saxReader.read(resourceAsStream);
//            Element rootElement = document.getRootElement();
//
//            List<Element> selectNodes = rootElement.selectNodes("//servlet");
//            for (int i = 0; i < selectNodes.size(); i++) {
//                Element element =  selectNodes.get(i);
//                // <servlet-name>lagou</servlet-name>
//                Element servletnameElement = (Element) element.selectSingleNode("servlet-name");
//                String servletName = servletnameElement.getStringValue();
//                // <servlet-class>server.LagouServlet</servlet-class>
//                Element servletclassElement = (Element) element.selectSingleNode("servlet-class");
//                String servletClass = servletclassElement.getStringValue();
//
//
//                // 根据servlet-name的值找到url-pattern
//                Element servletMapping = (Element) rootElement.selectSingleNode("/web-app/servlet-mapping[servlet-name='" + servletName + "']");
//                // /lagou
//                String urlPattern = servletMapping.selectSingleNode("url-pattern").getStringValue();
//                servletMap.put(urlPattern, (HttpServlet) Class.forName(servletClass).newInstance());
//
//            }
//
//
//
//        } catch (DocumentException e) {
//            e.printStackTrace();
//        } catch (IllegalAccessException e) {
//            e.printStackTrace();
//        } catch (InstantiationException e) {
//            e.printStackTrace();
//        } catch (ClassNotFoundException e) {
//            e.printStackTrace();
//        }
//
//    }
    private void loadServlet(InputStream resourceAsStream, Host host) {
        SAXReader saxReader = new SAXReader();

        try {
            Document document = saxReader.read(resourceAsStream);
            Element rootElement = document.getRootElement();

            List<Element> selectNodes = rootElement.selectNodes("//servlet");
            for (int i = 0; i < selectNodes.size(); i++) {
                Element element =  selectNodes.get(i);
                // <servlet-name>lagou</servlet-name>
                Element servletnameElement = (Element) element.selectSingleNode("servlet-name");
                String servletName = servletnameElement.getStringValue();
                // <servlet-class>server.LagouServlet</servlet-class>
                Element servletclassElement = (Element) element.selectSingleNode("servlet-class");
                String servletClass = servletclassElement.getStringValue();


                // 根据servlet-name的值找到url-pattern
                Element servletMapping = (Element) rootElement.selectSingleNode("/web-app/servlet-mapping[servlet-name='" + servletName + "']");
                // /lagou
                String urlPattern = servletMapping.selectSingleNode("url-pattern").getStringValue();
                host.servletMap.put(urlPattern, (HttpServlet) LoadClass.allLoadedClasses.get(servletClass).newInstance());
//                servletMap.put(urlPattern, (HttpServlet) Class.forName(servletClass).newInstance());

            }



        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
    }


    /**
     * Minicat 的程序启动入口
     * @param args
     */
    public static void main(String[] args) throws Exception {


//        WebClassLoader webClassLoader = new WebClassLoader();
//        Class<?> aClass = webClassLoader.loadClass("E:\\projects\\lagou\\Tomcat\\classloader-demo\\out\\artifacts\\classloader_demo_Web_exploded\\WEB-INF\\classes\\com\\lagou\\TestServlet.class");
//
//        Servlet servlet = (Servlet) aClass.newInstance();
//        servlet.init();
//        System.out.println(aClass.getClassLoader());
        Bootstrap bootstrap = new Bootstrap();
        try {
            // 启动Minicat
            bootstrap.start();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static InputStream getWebXMLStream(File file) throws FileNotFoundException {
        for (File listFile : file.listFiles()) {
            if("WEB-INF".equals(listFile.getName())){
                for (File file1 : listFile.listFiles()) {
                    if("web.xml".equals(file1.getName())){
                        return new FileInputStream(file1);
                    }
                }
            }
        }
        return null;

    }
}
