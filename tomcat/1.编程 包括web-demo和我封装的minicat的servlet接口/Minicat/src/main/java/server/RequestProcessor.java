package server;

import mapper.Host;
import mapper.Mapper;

import java.io.InputStream;
import java.net.Socket;
import java.util.Map;

public class RequestProcessor extends Thread {

    private Socket socket;
//    private Map<String,HttpServlet> servletMap;
    private Mapper mapper;

//    public RequestProcessor(Socket socket, Map<String, HttpServlet> servletMap) {
//        this.socket = socket;
//        this.servletMap = servletMap;
//    }
    public RequestProcessor(Socket socket, Mapper mapper) {
        this.socket = socket;
        this.mapper = mapper;
    }

    @Override
    public void run() {
        try{
            InputStream inputStream = socket.getInputStream();

            // 封装Request对象和Response对象
            Request request = new Request(inputStream);
            Response response = new Response(socket.getOutputStream());

            // 静态资源处理
            if(!mapper.hosts.containsKey(request.getWebName())){
                response.output(HttpProtocolUtil.getHttpHeader404());
            }else{
                Host host = mapper.hosts.get(request.getWebName());
                if(!host.servletMap.containsKey(request.getUrl())){
                    response.outputHtml(request.getUrl());
                }else{
                    HttpServlet httpServlet = host.servletMap.get(request.getUrl());
                    httpServlet.service(request,response);
                }
            }
//            if(servletMap.get(request.getUrl()) == null) {
//                response.outputHtml(request.getUrl());
//            }else{
//                // 动态资源servlet请求
//                HttpServlet httpServlet = servletMap.get(request.getUrl());
//                httpServlet.service(request,response);
//            }

            socket.close();

        }catch (Exception e) {
            e.printStackTrace();
        }

    }
}
