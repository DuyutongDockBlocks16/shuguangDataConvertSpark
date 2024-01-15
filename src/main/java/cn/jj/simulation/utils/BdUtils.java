package cn.jj.simulation.utils;

import cn.jj.simulation.history.GAGSIMU;
import cn.jj.simulation.history.GAGSIMUMUITY;
import cn.jj.simulation.history.GAGSIMUNEW;
import cn.jj.simulation.pb.Game;
import cn.jj.simulation.test.OutFuncI;
import com.google.protobuf.InvalidProtocolBufferException;
import com.sun.jna.Memory;
import com.sun.jna.Native;
import com.sun.jna.NativeLong;
import com.sun.jna.Pointer;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.ConcurrentHashMap;


/**
 * @program: JavaSOTest
 * @description:
 * @author: wangyb04
 * @create: 2021-05-13 14:04
 */
public class BdUtils {
//    修改要立即可见
    public static volatile GAGSIMUMUITY Instance;

    private static ConcurrentHashMap<String, Boolean> initMap = new ConcurrentHashMap<String, Boolean>();

    @Deprecated
    public void analysis(byte[] data, OutFuncI outFunc) {
        Pointer frame = new Memory(data.length);
        frame.write(0, data, 0, data.length);
        GAGSIMUNEW.Instance.simulateGag(frame, new NativeLong(data.length), outFunc);
    }

    /**
     * 目前使用的版本
     * @author wangyb04
     * @date 2021/8/11 11:39
     * @params
     * @return
     * @throws
    */
    public void analysis(byte[] data, String bd_version, OutFuncI outFunc) {
        try {
            String game_server_version = BDRead.get_game_server_version(bd_version);
            String game_big_version_short = BDRead.get_game_big_version_short(bd_version);
            String game_key_version = BDRead.get_game_key_version(bd_version);
            String so_path = "./simu/"+game_server_version+"/"+game_big_version_short+"/";
            System.out.println("---- so_path: "+so_path);
            String bd_templates_path = so_path+game_key_version+"/";
            System.out.println("---- bd_templates_path: "+bd_templates_path);
            Pointer frame = new Memory(data.length);
            frame.write(0, data, 0, data.length);
            // 线程安全要求：确保只有一个线程执行init，确保所有线程在init执行完之前阻塞，确保Instance立即对所有线程可见
            if (initMap.get(bd_version)==null) {
                synchronized (BdUtils.class) {
                    if (initMap.get(bd_version) == null) {
                        Instance = (GAGSIMUMUITY) Native.loadLibrary(so_path+"gagsimu.so", GAGSIMUMUITY.class);
                        Instance.init(bd_templates_path);
                        initMap.put(bd_version, true);
                    }
                }
            }
            Instance.simulateGag(frame, new NativeLong(data.length), outFunc);
//            TODO test释放c语言申请的内存
            long peer = Pointer.nativeValue(frame);
            Native.free(peer);
            Pointer.nativeValue(frame, 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Deprecated
    public void analysis(InputStream inputStream, OutFuncI outFunc) {
        GAGSIMU.Instance.createGag(outFunc);
        if (!new BDRead().read_head(inputStream)) return;
        try {
            while(true) {
                long frameSeq = 1;
                byte[] bytes = new byte[8];
                int len = inputStream.read(bytes);
                if (len==-1) break;
                int size = ByteUtils.bytes2int(4, bytes)*8;
                if (size==0) break;
                System.out.println("size: "+ size);
                byte[] contentbytes = new byte[size];
                inputStream.read(contentbytes);
                Pointer frame = new Memory(size);
                frame.write(0, contentbytes, 0, size);
                GAGSIMU.Instance.input(frame, new NativeLong(size), frameSeq++);
                System.out.println("---- input ok. ----");
            }
            System.out.println("---- read complete. ----");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            GAGSIMU.Instance.destroyGag();
        }
    }

    /**
     * @author wangyb04
     * @date 2021/5/18 10:23
     * @params msg: so模块返回的数据的指针
     * @return ods层的row数据
     * @throws
    */
    @Deprecated
    public String transform(String battle_id, Pointer msg, NativeLong sz) {
        // TODO
        String ods_row = null;
        byte[] msg_b = new byte[sz.intValue()];
        System.out.println("---- msg size: "+sz.intValue());
        msg.read(0, msg_b, 0, sz.intValue());
        try {
            Game.MsgGameFrameState msgGameFrameState = Game.MsgGameFrameState.parseFrom(msg_b);
            int frameNo = msgGameFrameState.getFrameNo();
            System.out.println("---- frameNo: "+frameNo+". ----");
        } catch (InvalidProtocolBufferException e) {
            e.printStackTrace();
            System.out.println("---- error transform bytecode. ----");
        }
        return ods_row;
    }

    /**
     * 解析失败获取原始数据
     * @author wangyb04
     * @date 2021/5/18 14:46
     * @params
     * @return
     * @throws
    */
    @Deprecated
    public String getOriginal(Pointer msg, NativeLong sz) {
        String original = "";
        // TODO
        return original;
    }

    /**
     * 读取pb的字节流
     * @author wangyb04
     * @date 2021/5/27 16:01
     * @params msg: pb字节流的头指针; sz: pb字节流的长度
     * @return 
     * @throws
    */
    public byte[] getPbBytes(Pointer msg, NativeLong sz) {
        byte[] msg_b = new byte[sz.intValue()];
//        System.out.println("---- msg size: "+sz.intValue());
        msg.read(0, msg_b, 0, sz.intValue());
        return msg_b;
    }

    @Deprecated
    public String getVersion(DataInputStream inputStream) {
        String version = null;
        return version;
    }

    @Deprecated
    public String union(String battle_id, byte[] pb_bytes) {
        byte[] id_bytes = battle_id.getBytes();
        byte data[] = new byte[id_bytes.length+pb_bytes.length+1];
        data[0] = new Integer(id_bytes.length).byteValue();
        System.arraycopy(id_bytes, 0, data, 1, id_bytes.length);
        System.arraycopy(pb_bytes, 0, data, 1+id_bytes.length, pb_bytes.length);
        return new String(data);
    }
}
