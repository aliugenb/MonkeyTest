import java.io.*;
import java.util.HashSet;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created with IntelliJ IDEA.
 * Author: ye.liu
 * Date: 2018/9/19
 * Time: 3:51 PM
 */

public class Monkey {
    private File classpathRoot;
    private File logDir;
    private File xmlDir;
    public static final String packageName = "com.ximalaya.ting.android.live";


    public Monkey() {
        this.classpathRoot = new File(System.getProperty("user.dir"));
        this.logDir = new File(classpathRoot, "log");
        this.xmlDir = new File(classpathRoot, "xml");
        if (!logDir.exists()) {
            logDir.mkdir();
        }
        if (!xmlDir.exists()) {
            xmlDir.mkdir();
        }
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        new Monkey().startMonkey();
//        new Timer(new TimerTask(new ),1);
    }


    public void startMonkey() throws IOException, InterruptedException {
        File log = new File(logDir, "MonkeyLog.txt");
        System.out.println(log);
        String[] cmd = new String[]{"sh", "-c", "adb shell monkey -v -v -v -p com.ximalaya.ting.android 200000 --throttle 3000 > " + log};
        try {
            Runtime runtime = Runtime.getRuntime();
            Process proc = runtime.exec(cmd);
//            new ProcessClearStream(proc.getInputStream(), "INFO").start();
//            new ProcessClearStream(proc.getErrorStream(), "ERROR").start();
//            if (proc.waitFor() != 0) {
//                System.err.println("exit value = " + proc.exitValue());
//            }
            System.out.println("success");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void killMonkey() {
        String getPidCmd = "adb shell ps |grep monkey";
        String killPidCmd = "adb shell kill -9";
        String pid = null;
        try {
            Runtime runtime = Runtime.getRuntime();
            Process proc = runtime.exec(getPidCmd);

            if (proc.waitFor() != 0) {
                System.err.println("exit value = " + proc.exitValue());
            }
            BufferedReader br = new BufferedReader(new InputStreamReader(proc.getInputStream()));
            String line = null;
            while ((line = br.readLine()) != null) {
                pid = getPid(line);
            }
            if (pid != null) {
                killPidCmd = killPidCmd + " " + pid;
                proc = runtime.exec(killPidCmd);
                System.out.println(killPidCmd);
                if (proc.waitFor() != 0) {
                    System.err.println("exit value = " + proc.exitValue());
                } else {
                    System.out.println("进程关闭");
                }
            } else {
                System.out.println("无进程");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean checkInLive() throws InterruptedException {
        String xmlCmd = "adb shell uiautomator dump";
        String pullXmlCmd = null;
        try {

            //生成Android布局xml文件
            Runtime runtime = Runtime.getRuntime();
            Process proc = runtime.exec(xmlCmd);

            if (proc.waitFor() != 0) {
                System.err.println("exit value = " + proc.exitValue());
            }
            BufferedReader br = new BufferedReader(new InputStreamReader(proc.getInputStream()));
            String line = null;
            String xmlPath = null;
            while ((line = br.readLine()) != null) {
                xmlPath = line.split(":")[1];
                xmlPath = xmlPath.trim();
            }

            //导出xml文件
            if (xmlPath != null) {
                pullXmlCmd = "adb pull " + xmlPath + " " + xmlDir;
                proc = runtime.exec(pullXmlCmd);
                System.out.println(pullXmlCmd);
                if (proc.waitFor() != 0) {
                    System.err.println("exit value = " + proc.exitValue());
                } else {
                    System.out.println("进程关闭");
                }
            } else {
                System.out.println("无进程");
            }

            //解析并判断xml文件内容包含直播resouce-id
            File xml = new File(xmlDir, "window_dump.xml");
            HashSet<String> valueSets = new XmlParser().iterateWholeXML(xml);
            for (String value : valueSets) {
                if (value.indexOf(packageName) != -1) {
                    return true;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    private String getPid(String str) {
        String[] strings = str.split("\\s+");
        return strings[1];
    }
}