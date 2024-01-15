//package cn.jj.simulation.test;
//
//import cn.jj.simulation.utils.BdUtils;
//import cn.jj.simulation.utils.BDRead;
//import com.google.common.io.ByteStreams;
//import com.sun.jna.NativeLong;
//import com.sun.jna.Pointer;
//
//import java.io.BufferedInputStream;
//import java.io.DataInputStream;
//import java.io.FileInputStream;
//import java.util.ArrayList;
//import java.util.List;
//
///**
// * @program: JavaSOTest
// * @description:
// * @author: wangyb04
// * @create: 2021-04-28 14:55
// */
//public class BDReadTest {
//    public static void main(String[] args) throws Exception {
//        System.setProperty("jna.debug_load", "true");
////        System.setProperty("jna.library.path", "./");
//        int parallize = Integer.valueOf(args[0]);
//        BDReadTest bdReadTest = new BDReadTest();
//        for (int i=0; i <parallize; i++) {
//            bdReadTest.test();
//        }
//    }
//
//    private void test() throws Exception {
//        BufferedInputStream inputStream = new BufferedInputStream(new FileInputStream("./51011750014327.bd"));
//        DataInputStream dataInputStream = new DataInputStream(inputStream);
//        byte[] bytes = ByteStreams.toByteArray(dataInputStream);
//        BDRead BDRead = new BDRead();
//        String bd_version = BDRead.get_head_line(bytes, 8);
//        byte[] data = BDRead.get_data(bytes);
//        List<byte[]> pb_bytes_list = new ArrayList<byte[]>();
//        // 游戏command解析，底层调用录像工具so
//        BdUtils bdUtils = new BdUtils();
//        bdUtils.analysis(data, bd_version, "./", new OutFuncI() {
//            @Override
//            public void OutputFunc(Pointer msg, NativeLong sz) {
//                byte[] pb_bytes = bdUtils.getPbBytes(msg, sz);
//                if (pb_bytes != null && pb_bytes.length != 0) {
//                    pb_bytes_list.add(pb_bytes);
//                } else {
//                    System.out.println("---- invalid msg");
//                }
//            }
//        });
//        System.out.println("---- simu complete ----");
//    }
//}
