//import java.io.BufferedReader;
//import java.io.InputStreamReader;
//import java.util.TimerTask;
//
///**
// * Created with IntelliJ IDEA.
// * Author: ye.liu
// * Date: 2018/9/21
// * Time: 1:30 PM
// */
//public class killMonkey extends TimerTask {
//    @Override
//    public void run() {
//        String getPidCmd = "adb shell ps |grep monkey";
//        String killPidCmd = "adb shell kill -9";
//        String pid = null;
//        try {
//            Runtime runtime = Runtime.getRuntime();
//            Process proc = runtime.exec(getPidCmd);
//
//            if (proc.waitFor() != 0) {
//                System.err.println("exit value = " + proc.exitValue());
//            }
//            BufferedReader br = new BufferedReader(new InputStreamReader(proc.getInputStream()));
//            String line = null;
//            while ((line = br.readLine()) != null) {
//                pid = getPid(line);
//            }
//            if (pid != null) {
//                killPidCmd = killPidCmd + " " + pid;
//                proc = runtime.exec(killPidCmd);
//                System.out.println(killPidCmd);
//                if (proc.waitFor() != 0) {
//                    System.err.println("exit value = " + proc.exitValue());
//                } else {
//                    System.out.println("进程关闭");
//                }
//            } else {
//                System.out.println("无进程");
//            }
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    private String getPid(String str) {
//        String[] strings = str.split("\\s+");
//        return strings[1];
//    }
//}
