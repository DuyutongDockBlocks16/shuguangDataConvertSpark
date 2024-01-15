//package cn.jj.simulation.history;
//
//import cn.jj.simulation.test.OutFuncI;
//import cn.jj.simulation.utils.BdUtils;
//import cn.jj.simulation.utils.BDRead;
//import cn.jj.simulation.utils.GamePbUtils;
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
//import org.apache.spark.storage.StorageLevel;
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
//public class StateSoSimulationNewMultiVersion {
//
//    public static void main(String[] args) throws Exception {
//        SparkConf conf = new SparkConf().setAppName("StateSoSimulationNew");
//        JavaSparkContext sc = new JavaSparkContext(conf);
//        // 分区字段
//        String year = args[0];
//        String month = args[1];
//        String day = args[2];
//        // task 并发数量
//        int partition = Integer.valueOf(args[3]);
//        // gagsimu 文件名（包含版本号）
////        String f_name = args[4];
//        String f_name = "20210601144922_formal_1.0.6.2_6001_ce347f5d_2b00d8c1_cd81619b.tar.gz";
//        // gagsimu 对应的版本号（只能处理对应版本的bd文件）
//        String version = f_name.substring(0, f_name.indexOf(".tar"));
//        System.out.println("---- gagsimu version: "+version+"----");
//        sc.setCheckpointDir("hdfs:///staging/shuguang/rec_parse/checkpoint/"+version+"/"+ year + "/" + month + "/" + day);
//        for (int i = 0; i < 100; i++) {
//            String part = i < 10?"0"+i:""+i;
//            // 读HDFS上的bd文件
//            // TODO har文件和普通小文件读取的性能对比
//            JavaPairRDD<String, PortableDataStream> bdBinaryFiles_pairRDD = sc.binaryFiles("har:///staging/shuguang/rec/archive/"+year+"-"+month+"-"+day+".har/"+part+"/");
////        JavaPairRDD<String, PortableDataStream> bdBinaryFiles_pairRDD = sc.binaryFiles("hdfs:///staging/shuguang/rec/tmp/51011750014327.bd");
//            JavaPairRDD<String, PortableDataStream> bdBinaryFiles_repartition_pairRDD = bdBinaryFiles_pairRDD.partitionBy(new HashPartitioner(partition));
//            // 每一个bd文件，先用版本号过滤，再flat为pb字节流的集合，并且cache，供多个action使用
//            // TODO 需要测试一下flatMap是否会重新分发key，产生网络损耗
//            JavaPairRDD<String, byte[]> pb_bytes_pairRDD = bdBinaryFiles_repartition_pairRDD.filter(bd_binaryfiles-> {
//                String rootDirectory = SparkFiles.getRootDirectory();
//                File root_path = new File(rootDirectory);
//                for (File file: root_path.listFiles()) {
//                    System.out.println("---- file name: "+file.getName());
//                }
//                String f_n = bd_binaryfiles._1;
//                PortableDataStream b_stream = bd_binaryfiles._2;
//                BDRead BDRead = new BDRead();
//                String bd_version = null;
//                bd_version = BDRead.get_head_line(b_stream.toArray(), 8);
//                System.out.println("---- bd_version: "+bd_version+"");
////            try {
////            } catch (Exception e) {
////                e.printStackTrace();
////                System.out.println("---- bd_name: "+f_n+" ----");
////                return false;
////            }
//                if (version.contains(bd_version)) return true;
//                return false;
//            }).flatMapValues(b_stream -> {
//                BDRead BDRead = new BDRead();
//                String bd_version = BDRead.get_head_line(b_stream.toArray(), 8);
//                byte[] data = BDRead.get_data(b_stream.toArray());
//                List<byte[]> pb_bytes_list = new ArrayList<byte[]>();
//                // 游戏command解析，底层调用录像工具so
//                BdUtils bdUtils = new BdUtils();
//                bdUtils.analysis(data, bd_version, "./", new OutFuncI() {
//                    @Override
//                    public void OutputFunc(Pointer msg, NativeLong sz) {
//                        byte[] pb_bytes = bdUtils.getPbBytes(msg, sz);
//                        if (pb_bytes != null && pb_bytes.length != 0) {
//                            pb_bytes_list.add(pb_bytes);
//                        } else {
//                            System.out.println("---- invalid msg");
//                        }
//                    }
//                });
//                return pb_bytes_list;
//            });
//            // 缓存二进制RDD，避免多个action重复解析bd文件
//            pb_bytes_pairRDD.persist(StorageLevel.DISK_ONLY());
//            // 切断父rdd血缘，避免由于子任务读取磁盘中的缓存之后，由于executor内存不够导致的重算
//            pb_bytes_pairRDD.checkpoint();
//
//            // heroState_RDD
//            JavaRDD<String> heroState_RDD = pb_bytes_pairRDD.flatMap(pair-> {
//                String battle_id = StringUtils.getSplitIndex(pair._1.substring(0, pair._1.length()-3), "/", -1);
//                byte[] pb_bytes = pair._2;
//                List<String> heroState_list = GamePbUtils.getHeroStateList(battle_id, pb_bytes);
//                return heroState_list.iterator();
//            });
//            heroState_RDD.saveAsTextFile("hdfs:///staging/shuguang/rec_parse/ods/game/hero_state/delta/"+version+"/"+ year + "/" + month + "/" + day+"/"+part);
//            // creepState_RDD
//            JavaRDD<String> creepState_RDD = pb_bytes_pairRDD.flatMap(pair -> {
//                String battle_id = StringUtils.getSplitIndex(pair._1.substring(0, pair._1.length() - 3), "/", -1);
//                byte[] pb_bytes = pair._2;
//                List<String> creepState_list = GamePbUtils.getCreepStateList(battle_id, pb_bytes);
//                return creepState_list.iterator();
//            });
//            creepState_RDD.saveAsTextFile("hdfs:///staging/shuguang/rec_parse/ods/game/creep_state/delta/"+version+"/"+ year + "/" + month + "/" + day+"/"+part);
//
//            // monsterState_RDD
//            JavaRDD<String> monsterState_RDD = pb_bytes_pairRDD.flatMap(pair -> {
//                String battle_id = StringUtils.getSplitIndex(pair._1.substring(0, pair._1.length() - 3), "/", -1);
//                byte[] pb_bytes = pair._2;
//                List<String> monsterState_list = GamePbUtils.getMonsterStateList(battle_id, pb_bytes);
//                return monsterState_list.iterator();
//            });
//            monsterState_RDD.saveAsTextFile("hdfs:///staging/shuguang/rec_parse/ods/game/monster_state/delta/"+version+"/"+ year + "/" + month + "/" + day+"/"+part);
//
//            // towerState_RDD
//            JavaRDD<String> towerState_RDD = pb_bytes_pairRDD.flatMap(pair -> {
//                String battle_id = StringUtils.getSplitIndex(pair._1.substring(0, pair._1.length() - 3), "/", -1);
//                byte[] pb_bytes = pair._2;
//                List<String> towerStateList = GamePbUtils.getTowerStateList(battle_id, pb_bytes);
//                return towerStateList.iterator();
//            });
//            towerState_RDD.saveAsTextFile("hdfs:///staging/shuguang/rec_parse/ods/game/tower_state/delta/"+version+"/"+ year + "/" + month + "/" + day+"/"+part);
//            pb_bytes_pairRDD.unpersist(true);
//        }
//    }
//}
