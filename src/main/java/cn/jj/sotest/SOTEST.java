package cn.jj.sotest;

/**
 * @program: LuaSpark
 * @description:
 * @author: wangyb04
 * @create: 2021-04-02 15:16
 */
public class SOTEST {

    public static void main(String[] args) throws Exception {
        if (args!=null && args.length !=0) {
            System.setProperty("jna.library.path", args[0]);
        }
        int parallelize = Integer.valueOf(args[1]);
        long accum = 0;
        for(int i = 0; i < parallelize; i++) {
            int res = FightSO.Instance.luafight(5, 7);
            accum += res;
        }
        System.out.println("----------- accum duration: "+accum);
        System.out.println("----------- parallel: "+parallelize);
        System.out.println("----------- avg duration: "+Float.valueOf(accum)/parallelize);
    }
}
