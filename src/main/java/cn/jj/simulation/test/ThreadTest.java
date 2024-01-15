package cn.jj.simulation.test;

import java.util.concurrent.ConcurrentHashMap;

/**
 * @program: sgods
 * @description:
 * @author: wangyb04
 * @create: 2021-07-02 14:26
 */
public class ThreadTest {
    private static ConcurrentHashMap<String, Boolean> initMap = new ConcurrentHashMap<String, Boolean>();
    private static ConcurrentHashMap<String, Boolean> initMap2 = new ConcurrentHashMap<String, Boolean>();

    public void put(String key) throws InterruptedException {
        if (initMap2.get(key)==null) {
            synchronized (ThreadTest.class) {
                if (initMap.putIfAbsent(key, true)==null) {
                    System.out.println("---- init "+key);
                    Thread.currentThread().sleep(5000);
                    initMap2.put(key, true);
                }
            }
        }
        System.out.println("---- execute ");
    }

    public static void main(String[] args) {
        int i = 10000;
        while(i>0) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    ThreadTest threadTest = new ThreadTest();
                    try {
                        threadTest.put("v1");
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
            i--;
        }
    }
}
