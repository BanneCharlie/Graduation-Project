package com.ruoyi.project.business.utils;

import com.ruoyi.project.business.common.exception.InputListOrderIsNotSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/***
 * ---->
 * 集合工具类X
 * @author xqh, 987072248@qq.com
 * @version 1.0
 * @date: 2021-06-16 10:29 - 星期三
 * @package: com.ruoyi.project.business.utils
 * @JDK-Version : 1.8
 */
public class CollectionUtilX {
    private static Logger logger = LoggerFactory.getLogger(CollectionUtilX.class);
    /**
     * --->
     *  检验 传入的整形列表是否是连续的 如果是连续的则返回连续的最大值+1
     *  如果不连续就则返回不连续的节点在list中的元素 并+1
     *  传入的列表可以有序也可以无序 ， 最终都会被转换为升序 (自动对 编号列表进行排序)
     *  如CollectionUtilX.checkListElementIsContinuous(List->{1,2,3,5,6,7})  返回 4 (3+1)
     *  如CollectionUtilX.checkListElementIsContinuous(List->{3,4,7,9,10})   返回 1 , 因为前边编号已经释放 需要获取前边释放的编号
     *  如CollectionUtilX.checkListElementIsContinuous(List->{1,2,3,4,5})    返回 6 (5+1)
     *  如CollectionUtilX.checkListElementIsContinuous(List->{5,3,2,1);      会先进行排序 排序之后如果还不是升序的 那么就抛出异常
     * @author xqh, 987072248@qq.com
     * @date 2021/6/16 10:34
     * @param checkList 待检验的列表
     * @return {@link int}
     */
    public static int getListElementIsContinuousToNumber (List<Integer> checkList) throws NullPointerException, InputListOrderIsNotSupport {
        if (checkList == null)
            throw new NullPointerException();
        if (checkList.size() == 0){     // 必定连续
            return 1;
        }
        if (judgeListOrder(checkList)){
            // 列表需要有个 下限作为引子(即1)
            if (checkList.get(0) != 1){
                return 1;
            }
            return checkList.get(handleListASCToNumber(checkList)) + 1;
        }else {
            Collections.sort(checkList);
            if (!judgeListOrder(checkList)){
                logger.error("不支持当前数据列表\t" + checkList);
                throw new InputListOrderIsNotSupport("不支持此数据列表的排序");
            }
            return getListElementIsContinuousToNumber(checkList);
        }
    }
    /**
     * --->
     *      调用这个方法的场景是 redis有数据，数据库有数据
     *      场景: 两个人先提交表单 存入数据库 {1,2}
     *      然后又来个人 一直在墨迹不提交 此时编号为 3
     *      紧接着又来个人 此时redis和数据库都有数据 此时要获取编号比较麻烦 ， 需要根据数据库列表 来和redis列表比较
     * @author xqh, 987072248@qq.com
     * @date 2021/6/16 16:24
     * @param dataBaseList
     * @param redisList
     * @return {@link int}
     */
    public static int getDataBaseAndRedisToResult(List<Integer> dataBaseList , List<Integer> redisList) throws InputListOrderIsNotSupport {
        if (! (dataBaseList instanceof ArrayList) || ! (redisList instanceof ArrayList)){
            throw new UnsupportedOperationException("不支持其他ArrayList内部类,请使用java.util.List的实现类ArrayList");
        }
        int currentDataBaseIslLegalNumber = CollectionUtilX.getListElementIsContinuousToNumber(dataBaseList);
        for (int i = 0; i < redisList.size(); i++) {
            Integer currentRedisNumber = redisList.get(i);
            if (currentDataBaseIslLegalNumber == currentRedisNumber){
                // 递归求得 合法编号
                dataBaseList.add(currentRedisNumber);
                List<Integer> tempRedisList = new ArrayList<>(redisList);
                tempRedisList.remove(currentRedisNumber);
                Collections.sort(dataBaseList);
                return getDataBaseAndRedisToResult(dataBaseList, tempRedisList);
            }
        }
        return currentDataBaseIslLegalNumber;
    }
    /**
     * --->
     *  获取当前列表的 排列顺序
     *      如果 出现 1,1,1,1... 这样的 默认按照升序来判断
     * @author xqh, 987072248@qq.com
     * @date 2021/6/16 10:51
     * @param checkList
     * @return {@link boolean}
     */
    private static boolean judgeListOrder(List<Integer> checkList){
        if (checkList.size() == 1){
            return true;    // 必定连续
        }
        int i = 1;
        int listMaxIndex = checkList.size()-1;
        while ( i <= listMaxIndex
                        &&
                (checkList.get(i) >= checkList.get(i - 1))
        ) {
            i++;
        }

        return i > listMaxIndex;
    }
    /**
     * --->
     *  传入已排序的 升序列表 获得其 第一次不连续时元素的下标 , 如果全部连续 则返回最后一个元素的下标
     * @author xqh, 987072248@qq.com
     * @date 2021/6/16 13:46
     * @param handleList 待处理的元素列表
     * @return {@link int}
     */
    private static int handleListASCToNumber(List<Integer> handleList){
        int handleListMaxIndex = handleList.size() - 1;

        for (int i = 0; i < handleListMaxIndex; i++) {
            if ((handleList.get(i) + 1) != (handleList.get(i+1))){
                return i;
            }else if (handleList.get(i).equals(handleList.get(i + 1))){
                logger.error("出现重复编号，请检查!\t" + handleList );
                throw new RuntimeException("编号系统异常!请联系管理员");
            }
        }
        return handleListMaxIndex;
    }
}
