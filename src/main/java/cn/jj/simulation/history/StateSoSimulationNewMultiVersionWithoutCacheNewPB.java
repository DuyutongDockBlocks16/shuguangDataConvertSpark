//package cn.jj.simulation.history;
//
//import cn.jj.simulation.test.OutFuncI;
//import cn.jj.simulation.utils.BdUtils;
//import cn.jj.simulation.utils.BDRead;
//import cn.jj.simulation.utils.GameNewPbUtils;
//import cn.jj.simulation.utils.StringUtils;
//import com.sun.jna.NativeLong;
//import com.sun.jna.Pointer;
//import org.apache.spark.HashPartitioner;
//import org.apache.spark.SparkConf;
//import org.apache.spark.SparkFiles;
//import org.apache.spark.api.java.JavaPairRDD;
//import org.apache.spark.api.java.JavaRDD;
//import org.apache.spark.api.java.JavaSparkContext;
//import org.apache.spark.input.PortableDataStream;
//
//import java.io.File;
//import java.util.ArrayList;
//import java.util.List;
//
///**
// * 1. 多个task来处理小文件，而不是按照block大小分发task
// * 2. executor的内存设置的大些
// * @program: JavaSOTest
// * @description: 模拟曙光state的so模块功能，线程安全
// * @author: wangyb04
// * @create: 2021-05-13 10:12
// */
//public class StateSoSimulationNewMultiVersionWithoutCacheNewPB {
//
//    public static void main(String[] args) throws Exception {
//        SparkConf conf = new SparkConf().setAppName("StateSoSimulationNew");
//        conf.set("mapreduce.map.output.compress", "false");
//        conf.set("mapreduce.output.fileoutputformat.compress", "false");
//        conf.set("mapred.map.output.compress", "false");
//        conf.set("mapred.output.fileoutputformat.compress", "false");
//        conf.set("spark.hadoop.mapred.map.output.compress", "false");
//        conf.set("spark.hadoop.mapred.output.fileoutputformat.compress", "false");
//        conf.set("spark.hadoop.mapred.output.compress", "false");
//        JavaSparkContext sc = new JavaSparkContext(conf);
//        // 分区字段
//        String year = args[0];
//        String month = args[1];
//        String day = args[2];
//        // task 并发数量
//        int partition = Integer.valueOf(args[3]);
////        int file_read_num = Integer.valueOf(args[4]);
//        // gagsimu 版本号
////        20210729184830_formal_1.0.6.10_6979_fb02f1b5_9af34c90_b1d08f64
//        String version = args[5];
//        // gagsimu 对应的版本号（只能处理对应版本的bd文件）
//        System.out.println("---- gagsimu version: "+version+"----");
////        String part = i < 10?"0"+i:""+i;
//        String part = args[4];
//        // 读HDFS上的bd文件
//        // TODO har文件和普通小文件读取的性能对比
//        // 测试文件 51012257660900.bd
//        JavaPairRDD<String, PortableDataStream> bdBinaryFiles_pairRDD = sc.binaryFiles("har:///staging/shuguang/rec/archive/"+year+"-"+month+"-"+day+".har/"+part+"/");
////        JavaPairRDD<String, PortableDataStream> bdBinaryFiles_pairRDD = sc.binaryFiles("hdfs:///staging/shuguang/rec/tmp/51011750014327.bd");
//        JavaPairRDD<String, PortableDataStream> bdBinaryFiles_repartition_pairRDD = bdBinaryFiles_pairRDD.partitionBy(new HashPartitioner(partition));
//        // 每一个bd文件，先用版本号过滤，再flat为pb字节流的集合，并且cache，供多个action使用
//        // TODO 需要测试一下flatMap是否会重新分发key，产生网络损耗
//        JavaPairRDD<String, byte[]> pb_bytes_pairRDD = bdBinaryFiles_repartition_pairRDD.filter(bd_binaryfiles-> {
//            String rootDirectory = SparkFiles.getRootDirectory();
//            File root_path = new File(rootDirectory);
//            for (File file: root_path.listFiles()) {
//                System.out.println("---- file name: "+file.getName());
//            }
//            String f_n = bd_binaryfiles._1;
//            PortableDataStream b_stream = bd_binaryfiles._2;
//            BDRead BDRead = new BDRead();
//            String bd_version = null;
//            bd_version = BDRead.get_head_line(b_stream.toArray(), 8);
//            System.out.println("---- bd_version: "+bd_version+"");
////            try {
////            } catch (Exception e) {
////                e.printStackTrace();
////                System.out.println("---- bd_name: "+f_n+" ----");
////                return false;
////            }
//            if (bd_version!=null && version.contains(bd_version)) return true;
//            return false;
//        }).flatMapValues(b_stream -> {
//            BDRead BDRead = new BDRead();
//            List<byte[]> pb_bytes_list = new ArrayList<byte[]>();
//            String bd_version = BDRead.get_head_line(b_stream.toArray(), 8);
//            if (bd_version==null) return pb_bytes_list;
//            byte[] data = BDRead.get_data(b_stream.toArray());
//            // 游戏command解析，底层调用录像工具so
//            BdUtils bdUtils = new BdUtils();
//            String bd_templates_path = BDRead.get_head_line(b_stream.toArray(), 6);
//            if (bd_templates_path==null) return pb_bytes_list;
//            bd_templates_path = "./simu/1.0.6/" + bd_templates_path + "/";
//            bdUtils.analysis(data, bd_version, bd_templates_path, new OutFuncI() {
//                @Override
//                public void OutputFunc(Pointer msg, NativeLong sz) {
//                    byte[] pb_bytes = bdUtils.getPbBytes(msg, sz);
//                    if (pb_bytes != null && pb_bytes.length != 0) {
//                        pb_bytes_list.add(pb_bytes);
//                    } else {
//                        System.out.println("---- invalid msg");
//                    }
//                }
//            });
//            return pb_bytes_list;
//        });
//        // heroState_RDD
//        JavaRDD<String> state_RDD = pb_bytes_pairRDD.flatMap(pair-> {
//            String battle_id = StringUtils.getSplitIndex(pair._1.substring(0, pair._1.length()-3), "/", -1);
//            byte[] pb_bytes = pair._2;
//            List<String> list = new ArrayList<String>();
//            if (pb_bytes.length==0) {
//                list.add("Error:"+battle_id);
//            } else {
//                List<String> heroState_list = GameNewPbUtils.getHeroStateList(battle_id, pb_bytes);
//                List<String> creepState_list = GameNewPbUtils.getCreepStateList(battle_id, pb_bytes);
//                List<String> monsterState_list = GameNewPbUtils.getMonsterStateList(battle_id, pb_bytes);
//                List<String> towerStateList = GameNewPbUtils.getTowerStateList(battle_id, pb_bytes);
//                list.addAll(heroState_list);
//                list.addAll(creepState_list);
//                list.addAll(monsterState_list);
//                list.addAll(towerStateList);
//            }
//            return list.iterator();
//        });
//        state_RDD.saveAsTextFile("hdfs:///staging/shuguang/rec_parse/ods/game/state/delta/"+version+"/"+ year + "/" + month + "/" + day+"/"+part);
//    }
//}
