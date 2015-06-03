package com.team8.server;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.*;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

import src.com.team8.License.License;
import com.team8.PerformanceManagement.PM;
import com.eva.me.cm.ConfigUtil;
import com.manager.failure.FailureManager;


public class Server {

    public enum ServiceType {
        TEAM,STUDENT,BOTH
    }


    public static int PORT = 8080;
    public static String CMRoot="CMRoot/config.properties";
    public static License license;
    public static String encode="Unicode";


    private static ServiceType serviceType;


    private static void initPM()
    {
        String pmPath =  ConfigUtil.getInstance().getProperty("PMRoot");
        if(pmPath!=null)
        {
            PM.setPathName(pmPath);
        }
        Integer pmPeriod =  Integer.valueOf(ConfigUtil.getInstance().getProperty("PM_PERIOD"));
        if (pmPeriod!=null)
        {
            String PeriodUnit = ConfigUtil.getInstance().getProperty("PM_PERIOD_UNIT").trim();
            if(PeriodUnit.equals("m")){
                PM.setPeriod(pmPeriod, TimeUnit.MINUTES);
            }
            else if(PeriodUnit.equals("s"))
            {
                PM.setPeriod(pmPeriod, TimeUnit.SECONDS);
            }
            else if(PeriodUnit.equals("h"))
            {
                PM.setPeriod(pmPeriod, TimeUnit.HOURS);
            }
            else
            {
                PM.setPeriod(pmPeriod, TimeUnit.MINUTES);
            }
        }
    }

    private static void initLicense()
    {
        Integer LNum =  Integer.valueOf(ConfigUtil.getInstance().getProperty("LicenseNum"));
        if(LNum!=null)
        {
            license=new License(LNum);
        }

    }

    private static void initFM()
    {
        String fmPath = ConfigUtil.getInstance().getProperty("FMRoot");
        if(fmPath!=null)
        {
            FailureManager.resetOutputFile(ConfigUtil.getInstance().getProperty("FMRoot"));
        }
        FailureManager.setRepetation(false);
    }

    private static void initServiceType() {
        String type = ConfigUtil.getInstance().getProperty("SERVICE_TYPE");
        if(type!=null)
        {
            switch (type) {
                case "TEAM":
                    serviceType = ServiceType.TEAM;
                    break;
                case "STUDENT":
                    serviceType = ServiceType.STUDENT;
                    break;
                default:
                    serviceType = ServiceType.BOTH;
                    break;
            }
        }
    }

    public static void init() throws IOException {
        ConfigUtil.changeConfigFilePath(CMRoot);

        initPM();
        initLicense();
        initFM();
        initServiceType();

        String serviceName = ConfigUtil.getInstance().getProperty("SERVICE_TYPE");
        serviceType = getType(serviceName);
    }



    private static ServiceType getType(String serviceName)
    {
        switch (serviceName) {
            case "TEAM":
                return ServiceType.TEAM;
            case "STUDENT":
                return ServiceType.STUDENT;
            default:
                return ServiceType.BOTH;
        }
    }


    public void StartServer() throws IOException {
        init();
        ServerThread serverThread = new ServerThread();
        serverThread.start();


        WatchService watchService = FileSystems.getDefault().newWatchService();

        Path path = FileSystems.getDefault().getPath(new File(CMRoot).getParent());

        path.register(watchService, StandardWatchEventKinds.ENTRY_MODIFY);
        while (true) {
            try {
                final WatchKey wk = watchService.take();
                for (WatchEvent<?> event : wk.pollEvents()) {
                    final Path changed = (Path) event.context();
                    if (changed.endsWith(new File(CMRoot).getName())) {
                        System.out.println(changed+" has changed");
                        init();
                    }
                }
                // reset the key
                boolean valid = wk.reset();
                if (!valid) {
                    System.out.println("Key has been unregisterede");
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }

    }

    public class ServerThread extends Thread{
        @Override
        public void run() {
            try {

                //设定服务端的端口号
                ServerSocket serverSocket = new ServerSocket(PORT);
                System.out.println("ServerSocket Start:"+serverSocket);
                WorkerThread_local stl=new WorkerThread_local();
                stl.start();
                //等待请求,此方法会一直阻塞,直到获得请求才往下走
                while(true){
                    Socket socket = serverSocket.accept();
                    WorkerThread sThread = new WorkerThread();
                    sThread.setSocket(socket);
                    sThread.start();

                }
            } catch (Exception e) {
                e.printStackTrace();
            }finally{
                System.out.println("Close.....");
            }
        }
    }


    public class WorkerThread extends Thread{
        private Socket socket=null;
        BufferedReader br = null;
        protected PrintWriter pw = null;
        public void setSocket(Socket s){
            this.socket=s;
        }

        @Override
        public void run() {
            try
            {
                System.out.println("Connection accept socket:"+socket);
                //用于接收客户端发来的请求
                br = new BufferedReader(new InputStreamReader(socket.getInputStream(),encode));
                //用于发送返回信息,可以不需要装饰这么多io流使用缓冲流时发送数据要注意调用.flush()方法
                pw = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(),encode)),true);
                while(true){
                    String str;
                    str = br.readLine();
                    if(str==null||str.equals("END")){
                        break;
                    }

                    System.out.println("Client Socket Message:"+str);
                    pw.println("Message Received");
                    pw.flush();

                    ProvideService(str);

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            finally{
                try {
                    pw.close();
                    br.close();
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            super.run();
        }

        protected void ProvideService(String str) {
            PM.sendPMMessage("get Message "+str, 1);
            if(license.inLicense())
            {

                FailureManager.logInfo("Provide Service Success");
                PM.sendPMMessage("Provide Service "+str, 1);
                PM.sendPMMessage("Return Message", 1);

                if(serviceType==ServiceType.STUDENT)
                {
                    pw.println("Student: "+str+" belong to team: "+ConfigUtil.getInstance().getProperty(str));
                }
                else if(serviceType==ServiceType.TEAM)
                {
                    pw.println("Team: "+str+" has student: "+ConfigUtil.getInstance().getProperty(str));
                }
                else
                {
                    pw.println("response: "+ConfigUtil.getInstance().getProperty(str));
                }
                pw.flush();
            }
            else {
                FailureManager.logInfo("Provide Service Fail");
                PM.sendPMMessage("Reject Service "+str, 1);
                PM.sendPMMessage("Return Message", 1);
                pw.println("Reject Service");
                pw.flush();

            }
        }
    }



    public class WorkerThread_local extends WorkerThread {
        @Override
        public void run() {
            this.pw = new PrintWriter(System.out);
            Scanner scanner = new Scanner(System.in);
            while(true)
            {
                String m=scanner.nextLine();
                ProvideService(m);

            }
        }

    }


}
	