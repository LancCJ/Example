package com.lanccj.demo;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.TimeInterval;
import com.google.common.collect.Lists;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * james_chen
 * 计算工艺步骤中时间消耗
 */
public class CaculatorStepTime {


    public static void main(String[] args) {

        TimeInterval timer = DateUtil.timer();


        String startTime = "2019-3-6 08:00:00";//开始生产时间
        int proNum = 20;//一共多少产品
        int stepNum = 20;//一共多少工艺步骤

        //随机生成工艺周期时间 每个周期时间在1-10之间
        List<Integer> routeTimes = Lists.newArrayList();
        for (int i = 0; i < stepNum; i++) {
            int random = (int) (Math.random() * 10 + 1);
            routeTimes.add(random);
        }

        System.out.println("=======模拟实际生产流水线计算产品在每个步骤的时间消耗=======");
        System.out.print("产品/步骤\t");
        //打印列头
        for (int i = 0; i < routeTimes.size(); i++) {//步骤循环
            System.out.print("  第" + (i + 1) + "步骤\t\t\t\t\t\t\t\t");
        }
        System.out.println();
        //输出结果
        for (int j = 0; j < proNum; j++) {//循环产品
            System.out.print(" 产品" + (j + 1) + "\t");
            for (int i = 0; i < routeTimes.size(); i++) {//步骤循环
                //计算秒数
                //int[] caculatorTimes = caculatorTimes(j+1,i+1,routeTimes);
                //计算日期时间
                String[] caculatorTimes = caculatorTimes(j + 1, i + 1, routeTimes, startTime);
                System.out.print(caculatorTimes[0] + "-" + caculatorTimes[1] + " ");
            }
            System.out.println();
        }

        System.out.println("计算消耗（毫秒）:" + timer.interval());
    }

    /**
     * 计算 第proNum个产品在工艺路线routeTimes中第stepNum个工艺步骤所消耗的时间开始和时间结束
     *
     * @param proNum     生产流水线中第几个产品
     * @param stepNum    生产工艺中第几个工艺步骤
     * @param routeTimes 某工艺步骤所有周期时间int数组
     * @param startTime  生产开始时间字符串 格式 2019-3-6 09:44:18
     * @return 返回开始时间   结束时间 的  年月日时间
     */
    public static String[] caculatorTimes(int proNum, int stepNum, List<Integer> routeTimes, String startTime) {
        String[] caculatorDate = new String[2];
        int[] caculatorTimes = caculatorTimes(proNum, stepNum, routeTimes);
        //时间计算转换
        caculatorDate[0] = convertDate(startTime, caculatorTimes[0]);
        caculatorDate[1] = convertDate(startTime, caculatorTimes[1]);
        return caculatorDate;
    }

    /**
     * 计算 第proNum个产品在工艺路线routeTimes中第stepNum个工艺步骤所消耗的时间开始和时间结束
     *
     * @param proNum     生产流水线中第几个产品
     * @param stepNum    生产工艺中第几个工艺步骤
     * @param routeTimes 某工艺步骤所有周期时间int数组
     * @return 返回开始时间   结束时间 的  秒数
     */
    public static int[] caculatorTimes(int proNum, int stepNum, List<Integer> routeTimes) {
        int[] caculatorTimes = new int[2];
        if (proNum == 1 && stepNum == 1) {//是第一个产品也是第一个工艺步骤
            caculatorTimes[0] = 0;
            caculatorTimes[1] = caculatorTimes[0] + routeTimes.get(stepNum - 1);
        } else if (proNum == 1 && stepNum != 1) {//是第一个产品不是第一个工艺步骤
            caculatorTimes[0] = getPreTotalTime(stepNum, routeTimes) + (stepNum - 1);
            caculatorTimes[1] = caculatorTimes[0] + routeTimes.get(stepNum - 1);
        } else if (proNum != 1 && stepNum == 1) {//不是第一个产品是第一个工艺步骤
            caculatorTimes[0] = getCurTime(stepNum, routeTimes) * (proNum - 1) + (proNum - 1);
            caculatorTimes[1] = caculatorTimes[0] + routeTimes.get(stepNum - 1);
        } else {//不是第一个产品不是第一个工艺步骤 递归
            //获取相同个产品但是1前一步骤的结束时间
            int sameProNumTime = caculatorTimes(proNum, stepNum - 1, routeTimes)[1];
            //获取相同步骤，但是上1产品的结束时间
            int sameStepNum = caculatorTimes(proNum - 1, stepNum, routeTimes)[1];
            if (sameProNumTime > sameStepNum) {
                caculatorTimes[0] = sameProNumTime + 1;
                caculatorTimes[1] = caculatorTimes[0] + routeTimes.get(stepNum - 1);
            } else {
                caculatorTimes[0] = sameStepNum + 1;
                caculatorTimes[1] = caculatorTimes[0] + routeTimes.get(stepNum - 1);
            }
        }
        return caculatorTimes;
    }

    /**
     * 获取前stepNum步骤的时间总计
     *
     * @param stepNum    步骤数
     * @param routeTimes 工艺路线时间定义
     * @return
     */
    public static int getPreTotalTime(int stepNum, List<Integer> routeTimes) {
        //校验
        int timeLength = routeTimes.size();
        if (stepNum > timeLength) {
            return -1;
        }
        //再计算
        int totalTime = 0;
        for (int i = 0; i < routeTimes.size(); i++) {
            if ((stepNum - 1) > i) {
                totalTime += routeTimes.get(i);
            }
        }
        return totalTime;
    }

    /**
     * 获取当前步骤时间
     *
     * @param stepNum
     * @param routeTimes
     * @return
     */
    public static int getCurTime(int stepNum, List<Integer> routeTimes) {
        //校验
        int timeLength = routeTimes.size();
        if (stepNum > timeLength) {
            return -1;
        }
        return routeTimes.get(stepNum - 1);
    }

    /**
     * 时间转换
     *
     * @param DateStr  传入开始时间
     * @param senconds 传入增加的秒数
     * @return 返回计算后的时间
     */
    public static String convertDate(String DateStr, int senconds) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date startTime = sdf.parse(DateStr);
            return sdf.format(startTime.getTime() + senconds * 1000);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }


}
