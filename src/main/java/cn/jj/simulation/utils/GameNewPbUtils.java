package cn.jj.simulation.utils;

import cn.jj.simulation.pb_new.*;
import com.google.protobuf.InvalidProtocolBufferException;

import java.util.ArrayList;
import java.util.List;

/**
 * @program: ShuGuangGAGSimu
 * @description:
 * @author: wangyb04
 * @create: 2021-05-27 17:37
 */
public class GameNewPbUtils {

    public static char FIELDS_TERMINATED = HiveUtils.FIELDS_TERMINATED;
    public static char COLLECTION_ITEMS_TERMINATED = HiveUtils.COLLECTION_ITEMS_TERMINATED;
    public static char MAP_KEYS_TERMINATED = HiveUtils.MAP_KEYS_TERMINATED;

    public static List<String> getHeroStateList(String battle_id, byte[] pb_bytes) {
        List<String> list = new ArrayList<String>();
        try {
            Game.MsgGameFrameState msgGameFrameState = Game.MsgGameFrameState.parseFrom(pb_bytes);
            int frameNo = msgGameFrameState.getFrameNo();
            boolean complete = msgGameFrameState.getComplete();
            // TODO 测试，75帧记录一次
//            if (frameNo%75!=1) return list;
            List<Hero.MsgHero> herosList = msgGameFrameState.getHerosList();
            for(Hero.MsgHero hero: herosList) {
                String heroStateHiveRow = getHeroStateHiveRow(battle_id, frameNo, complete, hero);
                heroStateHiveRow = "Hero:"+heroStateHiveRow;
                list.add(heroStateHiveRow);
//                break;
            }
        } catch (InvalidProtocolBufferException e) {
            e.printStackTrace();
        }
        return list;
    }

    private static String getHeroStateHiveRow(String battleId, int frameNo, boolean complete, Hero.MsgHero hero) {
        StringBuilder sb = new StringBuilder();
        // build actorState
        Hero.MsgActorState actorState = hero.getActorState();
        // get tptime
        long tp_beginTime = -1;
        long tp_endTime = -1;
        List<Cmd.MsgBuffState> list =  actorState.getBuffStateList();
        for (Cmd.MsgBuffState buff: list) {
            if (buff.getId()==18000) {
//                System.out.println("---- buff_id: "+buff.getId());
                tp_beginTime = buff.getBeginTime();
                tp_endTime = buff.getEndTime();
//                System.out.println("---- tp_beginTime: "+tp_beginTime);
//                System.out.println("---- tp_endTime: "+tp_endTime);
            }
        }
        sb.append(battleId).append(FIELDS_TERMINATED)
                .append(frameNo).append(FIELDS_TERMINATED)
                .append(complete).append(FIELDS_TERMINATED)
                .append(hero.getId()).append(FIELDS_TERMINATED)
                .append(actorState.getCampId()).append(FIELDS_TERMINATED)
                .append(actorState.getLocation().getX()).append(FIELDS_TERMINATED)
                .append(actorState.getLocation().getY()).append(FIELDS_TERMINATED)
                .append(actorState.getDirection().getX()).append(FIELDS_TERMINATED)
                .append(actorState.getDirection().getY()).append(FIELDS_TERMINATED)
                .append(actorState.getHp()).append(FIELDS_TERMINATED)
                .append(actorState.getHpMax()).append(FIELDS_TERMINATED)
                .append(actorState.getEnergy()).append(FIELDS_TERMINATED)
                .append(actorState.getEnergyMax()).append(FIELDS_TERMINATED)
                .append(actorState.getMana()).append(FIELDS_TERMINATED)
                .append(actorState.getManaMax()).append(FIELDS_TERMINATED)
                .append(actorState.getAnger()).append(FIELDS_TERMINATED)
                .append(actorState.getAngerMax()).append(FIELDS_TERMINATED)
                .append(actorState.getLevel()).append(FIELDS_TERMINATED)
                .append(actorState.getExp()).append(FIELDS_TERMINATED)
                .append(actorState.getCurGold()).append(FIELDS_TERMINATED)
                .append(actorState.getGold()).append(FIELDS_TERMINATED)
                .append(actorState.getPhyAtk()).append(FIELDS_TERMINATED)
                .append(actorState.getPhyDef()).append(FIELDS_TERMINATED)
                .append(actorState.getMgcAtk()).append(FIELDS_TERMINATED)
                .append(actorState.getMgcDef()).append(FIELDS_TERMINATED)
                .append(actorState.getToughness()).append(FIELDS_TERMINATED)
                .append(actorState.getCdr()).append(FIELDS_TERMINATED)
                .append(actorState.getRespawn()).append(FIELDS_TERMINATED)
                .append(actorState.getIsVisiable()).append(FIELDS_TERMINATED)
                .append(actorState.getMoveSpeed()).append(FIELDS_TERMINATED)
                .append(actorState.getKill()).append(FIELDS_TERMINATED)
                .append(actorState.getDead()).append(FIELDS_TERMINATED)
                .append(actorState.getAssist()).append(FIELDS_TERMINATED)
                .append(HiveUtils.buildARRAY(actorState.getCurSkillIDList())).append(FIELDS_TERMINATED)
                .append(actorState.getHpRecover()).append(FIELDS_TERMINATED)
                .append(actorState.getEnergyRecover()).append(FIELDS_TERMINATED)
                .append(actorState.getAtkSpeed()).append(FIELDS_TERMINATED)
                .append(actorState.getCrt()).append(FIELDS_TERMINATED)
                .append(actorState.getCrtTimes()).append(FIELDS_TERMINATED)
                .append(actorState.getPhyThrough()).append(FIELDS_TERMINATED)
                .append(actorState.getMagThrough()).append(FIELDS_TERMINATED)
                .append(actorState.getPhyThroughPercent()).append(FIELDS_TERMINATED)
                .append(actorState.getMagThroughPercent()).append(FIELDS_TERMINATED)
                .append(actorState.getSpellSorb()).append(FIELDS_TERMINATED)
                .append(actorState.getAtkSorb()).append(FIELDS_TERMINATED)
                .append(HiveUtils.buildARRAY(actorState.getExStateList())).append(FIELDS_TERMINATED)
                .append(actorState.getShield()).append(FIELDS_TERMINATED)
                .append(actorState.getShieldPhy()).append(FIELDS_TERMINATED)
                .append(actorState.getShieldMgc()).append(FIELDS_TERMINATED)
                .append(actorState.getShieldAll()).append(FIELDS_TERMINATED)
                .append(actorState.getShieldPhyMax()).append(FIELDS_TERMINATED)
                .append(actorState.getShieldMgcMax()).append(FIELDS_TERMINATED)
                .append(actorState.getShieldAllMax()).append(FIELDS_TERMINATED)
                .append(actorState.getHeadIconLockOnTargetIid()).append(FIELDS_TERMINATED)
                .append(actorState.getChaseTargetIid()).append(FIELDS_TERMINATED)
                .append(actorState.getIsChaseMove()).append(FIELDS_TERMINATED)
                .append(actorState.getIsAutoAttacking()).append(FIELDS_TERMINATED)
                .append(actorState.getLockOnTargetIid()).append(FIELDS_TERMINATED)
                .append(actorState.getIsDead()).append(FIELDS_TERMINATED)
                // new tptime
                .append(tp_beginTime).append(FIELDS_TERMINATED)
                .append(tp_endTime).append(FIELDS_TERMINATED)
                // ----
                .append(actorState.getBulletNum()).append(FIELDS_TERMINATED)
                .append(actorState.getPortalCD()).append(FIELDS_TERMINATED)
                .append(actorState.getCanEatCoin()).append(FIELDS_TERMINATED)
                .append(actorState.getSpringPortalSuccess()).append(FIELDS_TERMINATED)
                .append(HiveUtils.buildARRAY(actorState.getDelcurSkillIDList())).append(FIELDS_TERMINATED)
                .append(HiveUtils.buildARRAY(actorState.getDelexStateList())).append(FIELDS_TERMINATED)
                .append(HiveUtils.buildARRAY(actorState.getDelbuffsList())).append(FIELDS_TERMINATED);
        // build abilityState
        List<Hero.MsgAbilityState> abilityStateList = hero.getAbilityStateList();
        sb.append(buildAbilityStateARRAY(abilityStateList)).append(FIELDS_TERMINATED);
        // build commands
        List<Cmd.MsgCmd> commandsList = hero.getCommandsList();
        sb.append(buildCommandARRAY(commandsList)).append(FIELDS_TERMINATED);

        sb.append(hero.getBServerAI()).append(FIELDS_TERMINATED)
                .append(HiveUtils.buildARRAY(hero.getEquiptIDsList())).append(FIELDS_TERMINATED)
                .append(buildEquipRecommendARRAY(hero.getEquipRecommendList())).append(FIELDS_TERMINATED);
        // build playerState
        sb.append(hero.getHeroId()).append(FIELDS_TERMINATED)
                .append(hero.getRaceId()).append(FIELDS_TERMINATED)
                .append(hero.getAccount()).append(FIELDS_TERMINATED)
                .append(hero.getLevel()).append(FIELDS_TERMINATED)
                .append(hero.getElo()).append(FIELDS_TERMINATED)
                .append(hero.getScore()).append(FIELDS_TERMINATED)
                .append(hero.getDifficulty()).append(FIELDS_TERMINATED)
                .append(hero.getPlayerType()).append(FIELDS_TERMINATED)
                .append(HiveUtils.buildARRAY(hero.getDelabilityStateList()));
        return sb.toString();
    }

    private static String buildCommandARRAY(List<Cmd.MsgCmd> commandsList) {
        StringBuilder ressb = new StringBuilder();
        for(Cmd.MsgCmd cmd: commandsList) {
            StringBuilder sb = new StringBuilder();
            sb.append(cmd.getType()== Cmd.ResultCmdType.UNRECOGNIZED?-1:cmd.getType().getNumber()).append(COLLECTION_ITEMS_TERMINATED)
                    .append(cmd.getAbilityID()).append(COLLECTION_ITEMS_TERMINATED)
                    .append(cmd.getTargetPosX()).append(COLLECTION_ITEMS_TERMINATED)
                    .append(cmd.getTargetPosY()).append(COLLECTION_ITEMS_TERMINATED)
                    .append(cmd.getTargeID()).append(COLLECTION_ITEMS_TERMINATED)
                    .append(cmd.getDegreeX()).append(COLLECTION_ITEMS_TERMINATED)
                    .append(cmd.getDegreeY()).append(COLLECTION_ITEMS_TERMINATED)
                    .append(cmd.getEquiptId()).append(COLLECTION_ITEMS_TERMINATED)
                    .append(cmd.getAngle()).append(COLLECTION_ITEMS_TERMINATED)
                    .append(cmd.getSkillLevel());
            ressb.append(sb).append(COLLECTION_ITEMS_TERMINATED);
        }
        return ressb.length()==0?null:ressb.substring(0, ressb.length()-1);
    }

    private static String buildEquipRecommendARRAY(List<Hero.MsgEquipRecommend> equipRecommendList) {
        StringBuilder ressb = new StringBuilder();
        for(Hero.MsgEquipRecommend er: equipRecommendList) {
            StringBuilder sb = new StringBuilder();
            sb.append(er.getId()).append(COLLECTION_ITEMS_TERMINATED)
                    .append(HiveUtils.buildARRAY(er.getEquipsList())).append(COLLECTION_ITEMS_TERMINATED)
                    .append(HiveUtils.buildARRAY(er.getLanesList())).append(COLLECTION_ITEMS_TERMINATED)
                    .append(er.getWeight());
            ressb.append(sb).append(COLLECTION_ITEMS_TERMINATED);
        }
        return ressb.length()==0?null:ressb.substring(0, ressb.length()-1);
    }

    private static String buildAbilityStateARRAY(List<Hero.MsgAbilityState> abilityStateList) {
        StringBuilder ressb = new StringBuilder();
        for(Hero.MsgAbilityState as: abilityStateList) {
            StringBuilder sb = new StringBuilder();
            sb.append(as.getId()).append(COLLECTION_ITEMS_TERMINATED)
                .append(as.getLevel()).append(COLLECTION_ITEMS_TERMINATED)
                .append(as.getSlot()).append(COLLECTION_ITEMS_TERMINATED)
                .append(as.getType()).append(COLLECTION_ITEMS_TERMINATED)
                .append(as.getUsable()).append(COLLECTION_ITEMS_TERMINATED)
                .append(as.getCd()).append(COLLECTION_ITEMS_TERMINATED)
                .append(as.getCdMax()).append(COLLECTION_ITEMS_TERMINATED)
                .append(buildMsgSkillRange(as.getAtkRangeList())).append(COLLECTION_ITEMS_TERMINATED)
                .append(as.getHpCost()).append(COLLECTION_ITEMS_TERMINATED)
                .append(as.getMpCost()).append(COLLECTION_ITEMS_TERMINATED)
                .append(as.getXpCost()).append(COLLECTION_ITEMS_TERMINATED)
                .append(as.getSegment()).append(COLLECTION_ITEMS_TERMINATED)
                .append(as.getProgress()).append(COLLECTION_ITEMS_TERMINATED)
                .append(as.getIsOutofMana()).append(COLLECTION_ITEMS_TERMINATED)
                .append(as.getStorage());
            ressb.append(sb.toString()).append(COLLECTION_ITEMS_TERMINATED);
        }
        return ressb.length()==0?null:ressb.substring(0, ressb.length()-1);
    }

    private static String buildMsgSkillRange(List<Hero.MsgSkillRange> atkRangeList) {
        StringBuilder ressb = new StringBuilder();
        for (Hero.MsgSkillRange sr: atkRangeList) {
            StringBuilder sb = new StringBuilder();
            sb.append(buildMsgSkillRangeData(sr.getChaseRange())).append(COLLECTION_ITEMS_TERMINATED);
            sb.append(buildMsgSkillRangeData(sr.getCastRange())).append(COLLECTION_ITEMS_TERMINATED);
            sb.append(buildMsgSkillRangeData(sr.getCastRangeWithoutTarget()));
            ressb.append(sb.toString()).append(COLLECTION_ITEMS_TERMINATED);
        }
        return ressb.length()==0?null:ressb.substring(0, ressb.length()-1);
    }

    private static String buildMsgSkillRangeData(Hero.MsgSkillRangeData castRange) {
        StringBuilder sb = new StringBuilder();
        sb.append(castRange.getValue()).append(COLLECTION_ITEMS_TERMINATED)
                .append(castRange.getNextValue()).append(COLLECTION_ITEMS_TERMINATED)
                .append(castRange.getStartTime()).append(COLLECTION_ITEMS_TERMINATED)
                .append(castRange.getDuration());
        return sb.length()==0?null:sb.toString();
    }

    public static List<String> getCreepStateList(String battle_id, byte[] pb_bytes) {
        List<String> list = new ArrayList<String>();
        try {
            Game.MsgGameFrameState msgGameFrameState = Game.MsgGameFrameState.parseFrom(pb_bytes);
            int frameNo = msgGameFrameState.getFrameNo();
            boolean complete = msgGameFrameState.getComplete();
            // TODO 测试，75帧记录一次
//            if (frameNo%75!=1) return list;
            List<Creep.MsgCreep> creepsList = msgGameFrameState.getCreepsList();
            for(Creep.MsgCreep creep: creepsList) {
                String creepStateHiveRow = getCreepStateHiveRow(battle_id, frameNo, complete, creep);
                creepStateHiveRow = "Creep:"+creepStateHiveRow;
                list.add(creepStateHiveRow);
//                break;
            }
        } catch (InvalidProtocolBufferException e) {
            e.printStackTrace();
        }
        return list;
    }

    private static String getCreepStateHiveRow(String battleId, int frameNo, boolean complete, Creep.MsgCreep creep) {
        StringBuilder sb = new StringBuilder();
        sb.append(battleId).append(FIELDS_TERMINATED)
                .append(frameNo).append(FIELDS_TERMINATED)
                .append(complete).append(FIELDS_TERMINATED)
                .append(creep.getId()).append(FIELDS_TERMINATED)
                .append(creep.getCampId()).append(FIELDS_TERMINATED)
                .append(creep.getType()).append(FIELDS_TERMINATED)
                .append(creep.getPosX()).append(FIELDS_TERMINATED)
                .append(creep.getPosY()).append(FIELDS_TERMINATED)
                .append(creep.getHp()).append(FIELDS_TERMINATED)
                .append(creep.getHpMax()).append(FIELDS_TERMINATED)
                .append(creep.getAtk()).append(FIELDS_TERMINATED)
                .append(creep.getDef()).append(FIELDS_TERMINATED)
                .append(creep.getIsVisiable()).append(FIELDS_TERMINATED)
                .append(creep.getTargetId()).append(FIELDS_TERMINATED)
                .append(creep.getAtkRange()).append(FIELDS_TERMINATED)
                .append(creep.getCreepId());
        return sb.toString();
    }

    public static List<String> getMonsterStateList(String battle_id, byte[] pb_bytes) {
        List<String> list = new ArrayList<String>();
        try {
            Game.MsgGameFrameState msgGameFrameState = Game.MsgGameFrameState.parseFrom(pb_bytes);
            int frameNo = msgGameFrameState.getFrameNo();
            boolean complete = msgGameFrameState.getComplete();
            // TODO 测试，75帧记录一次
//            if (frameNo%75!=1) return list;
            List<Monster.MsgMonster> monstersList = msgGameFrameState.getMonstersList();
            for(Monster.MsgMonster monster: monstersList) {
                String monsterStateHiveRow = getMonsterStateHiveRow(battle_id, frameNo, complete, monster);
                monsterStateHiveRow = "Monster:"+monsterStateHiveRow;
                list.add(monsterStateHiveRow);
//                break;
            }
        } catch (InvalidProtocolBufferException e) {
            e.printStackTrace();
        }
        return list;
    }

    private static String getMonsterStateHiveRow(String battleId, int frameNo, boolean complete, Monster.MsgMonster monster) {
        StringBuilder sb = new StringBuilder();
        sb.append(battleId).append(FIELDS_TERMINATED)
                .append(frameNo).append(FIELDS_TERMINATED)
                .append(complete).append(FIELDS_TERMINATED)
                .append(monster.getId()).append(FIELDS_TERMINATED)
                .append(-1).append(FIELDS_TERMINATED)
                .append(monster.getType()).append(FIELDS_TERMINATED)
                .append(monster.getPosX()).append(FIELDS_TERMINATED)
                .append(monster.getPosY()).append(FIELDS_TERMINATED)
                .append(monster.getHp()).append(FIELDS_TERMINATED)
                .append(monster.getHpMax()).append(FIELDS_TERMINATED)
                .append(monster.getAtk()).append(FIELDS_TERMINATED)
                .append(monster.getDef()).append(FIELDS_TERMINATED)
                .append(monster.getIsVisiable()).append(FIELDS_TERMINATED)
                .append(monster.getTargetId()).append(FIELDS_TERMINATED)
                .append(monster.getAtkRange()).append(FIELDS_TERMINATED)
                .append(monster.getMonsterId());
        return sb.toString();
    }


    public static List<String> getTowerStateList(String battle_id, byte[] pb_bytes) {
        List<String> list = new ArrayList<String>();
        try {
            Game.MsgGameFrameState msgGameFrameState = Game.MsgGameFrameState.parseFrom(pb_bytes);
            int frameNo = msgGameFrameState.getFrameNo();
            boolean complete = msgGameFrameState.getComplete();
            // TODO 测试，75帧记录一次
//            if (frameNo%75!=1) return list;
            List<Tower.MsgTower> towersList = msgGameFrameState.getTowersList();
            for(Tower.MsgTower tower: towersList) {
                String towerStateHiveRow = getTowerStateHiveRow(battle_id, frameNo, complete, tower);
                towerStateHiveRow = "Tower:"+towerStateHiveRow;
                list.add(towerStateHiveRow);
//                break;
            }
        } catch (InvalidProtocolBufferException e) {
            e.printStackTrace();
        }
        return list;
    }

    private static String getTowerStateHiveRow(String battleId, int frameNo, boolean complete, Tower.MsgTower tower) {
        StringBuilder sb = new StringBuilder();
        sb.append(battleId).append(FIELDS_TERMINATED)
                .append(frameNo).append(FIELDS_TERMINATED)
                .append(complete).append(FIELDS_TERMINATED)
                .append(tower.getId()).append(FIELDS_TERMINATED)
                .append(tower.getCampId()).append(FIELDS_TERMINATED)
                .append(tower.getType()).append(FIELDS_TERMINATED)
                .append(tower.getPosX()).append(FIELDS_TERMINATED)
                .append(tower.getPosY()).append(FIELDS_TERMINATED)
                .append(tower.getHp()).append(FIELDS_TERMINATED)
                .append(tower.getHpMax()).append(FIELDS_TERMINATED)
                .append(tower.getAtk()).append(FIELDS_TERMINATED)
                .append(tower.getDef()).append(FIELDS_TERMINATED)
                .append(1).append(FIELDS_TERMINATED)
                .append(tower.getTargetId()).append(FIELDS_TERMINATED)
                .append(tower.getAtkRange()).append(FIELDS_TERMINATED)
                .append(tower.getTowerId());
        return sb.toString();
    }

    public static List<String> getStateList(String battle_id, byte[] pb_bytes) {
        List<String> list = new ArrayList<String>();
        if (pb_bytes.length==0) {
            list.add("Error:"+battle_id);
        } else {
            List<String> heroState_list = getHeroStateList(battle_id, pb_bytes);
//            List<String> creepState_list = getCreepStateList(battle_id, pb_bytes);
//            List<String> monsterState_list = getMonsterStateList(battle_id, pb_bytes);
            List<String> towerStateList = getTowerStateList(battle_id, pb_bytes);
            list.addAll(heroState_list);
//            list.addAll(creepState_list);
//            list.addAll(monsterState_list);
            list.addAll(towerStateList);
        }
        return list;
    }
}
