package com.team8.PerformanceManagement;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;


/**
* @author      Group_8_Tongji_University <https://github.com/TJSoftwareReuse/2012T08>
* @since       2015-04-24
*/
public class PM {
    private static HashMap<String,Integer> dataMap  = new HashMap<>();
    private static int period = 1;
    private static TimeUnit periodUnit = TimeUnit.MINUTES;
    private static int new_period = 1;
    private static TimeUnit new_periodUnit = TimeUnit.MINUTES;
    private static String pathName = "LOG";
    private static boolean isReset = false;
    private static Date startDate = new Date();
    private static Date endDate = new Date(startDate.getTime() + periodUnit.toMillis(period));



    public static void setPathName(String name){
        if(name!=null&&!name.equals(""))
        {
            pathName=name;
        }
    }

    public static void setPeriod(int period){
        setPeriod(period, TimeUnit.MINUTES);
    }

    public static void setPeriod(int period, TimeUnit periodUnit){
        PM.new_period = period;
        PM.new_periodUnit = periodUnit;
    }


    /**
     * 发送PM信息
     * <p>
     * 发送PM信息给单例，每次发送即时写入文件
     * <p>
     * 每次写入是当前分钟收到的所有数据
     * <p>
     * 间隔一分钟发送的信息将会调用 getFileName(0)  获取新的文件
     * <p>
     * 提供线程锁   支持多线程操作  输出效率降低
     * <p>
     * 使用 {{@link #getFileName()} 获得将要输出的文件名
     * <p>
     * 使用 {{@link #output()}}  输出该分钟内收到的全部信息
     * <p>
     * 因为每次写入是进行写覆盖，所以最坏情况下时间复杂度是O(n^2)
     * <p>
     * 每次调用该函数，即时输出到文件，确保程序尽可能输出PM信息
     * <p>
     * @param  name   PM信息的字符描述
     * @param  value    PM信息的输出次数     value>0
     * @exception  IOException
     */
    public synchronized static void sendPMMessage(String name,int value){
        if(value < 0){
            return;
        }
        updatePerformanceData(name, value);
        if(!dataMap.isEmpty()) {
            output();
        }
    }

    private static void updatePerformanceData(String indexName, int value){
        if(endDate.getTime()<new Date().getTime()){
            dataMap.clear();
        }
        if(dataMap.get(indexName)!=null){
            value += dataMap.get(indexName);
        }
        dataMap.put(indexName, value);
    }

    private static boolean isSameDay(Date date1, Date date2){
        SimpleDateFormat fmt = new SimpleDateFormat("yyyyMMdd");
        return fmt.format(date1).equals(fmt.format(date2));
    }

    /**
     * 根据时间获得要写入的文件名
     * <p>
     * 2014年2月29日23分   获取文件格式 : LOG\2014-02-29-23.log
     * 精确到分钟
     * <p>
     *
     * @return  filename  moreMinute时间后要输出的文件名
     */
    public static String getFileName() {
        Date date = new Date();
        if (date.getTime() > endDate.getTime()) {
            periodUnit = new_periodUnit;
            period = new_period;
            startDate = new Date(endDate.getTime() + ((date.getTime() - endDate.getTime())/periodUnit.toMillis(period))*periodUnit.toMillis(period));
            endDate = new Date(startDate.getTime() + periodUnit.toMillis(period));
        }
        SimpleDateFormat startDateFmt = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");
        SimpleDateFormat endDateFmt;
        if(isSameDay(startDate, endDate))
        {
            endDateFmt = new SimpleDateFormat("HH-mm-ss");
        }
        else
        {
            endDateFmt = startDateFmt;
        }
        return startDateFmt.format(startDate)+" to "+endDateFmt.format(endDate);
    }



    /**
     * 私有函数：输出该分钟内收到的信息
     * <p>
     * 将该分钟内收到的信息输出到file文件下
     * 这是内部私有函数 仅攻类内部输出使用
     * <p>
     */
    private static synchronized void output() {
        File pathDir = new File(pathName);
        if(!pathDir.exists())
        {
            pathDir.mkdirs();
        }
        String fileName = getFileName();
        System.out.println(fileName);
        File file = new File(pathDir, fileName);

        try {
            FileOutputStream out = new FileOutputStream(file);
            PrintWriter pw = new PrintWriter(new OutputStreamWriter(out));
            for (String key: dataMap.keySet()) {
                pw.println(key+":"+dataMap.get(key).toString());
            }
            pw.flush();
            pw.close();
            out.close();
            isReset=false;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * 清空输出文件
     * <p>
     * 重置该类
     * 清空该分钟的输出记录
     * 该方法是为了方便测试人员进行测试
     * <p>
     */
    public static void Reset(){
        if(!isReset)
        {
            isReset=true;
            System.gc();//为了保证文件能够正常删除，需要这个函数
            dataMap.clear();
            File lastFile=new File(getFileName());
            if(lastFile.exists())
                lastFile.delete();
        }
    }

    /**
     * 返回该分钟内收到的信息
     * <p>
     * 将类内部保存的信息通过Map方式返回
     * 该方法是为了方便测试人员进行测试
     * <p>
     * @exception IOException
     * @return  Map  PM信息和输出次数的对应表
     */
    public static Map<String, Integer> getMap(){
        return dataMap;
    }

}


