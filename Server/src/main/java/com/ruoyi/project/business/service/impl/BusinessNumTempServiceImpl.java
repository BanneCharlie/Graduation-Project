package com.ruoyi.project.business.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ruoyi.framework.redis.RedisCache;
import com.ruoyi.project.business.domain.BusinessNumTemp;
import com.ruoyi.project.business.mapper.BusinessNumTempMapper;
import com.ruoyi.project.business.service.BusinessNumTempService;
import com.ruoyi.project.business.utils.CollectionUtilX;
import com.ruoyi.project.template.commons.mybatis.base.service.impl.BaseServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/***
 * @author xqh, 987072248@qq.com
 * @version 1.0
 * @date: 2021-06-10 11:29 - 星期四
 * @package: com.ruoyi.project.business.service.impl
 * @JDK-Version : 1.8
 */
@Service
@Transactional
public class BusinessNumTempServiceImpl extends BaseServiceImpl<BusinessNumTempMapper, BusinessNumTemp> implements BusinessNumTempService {
    private final static Lock lock = new ReentrantLock();
    private final RedisCache redisCache;
    private final static Logger logger = LoggerFactory.getLogger(BusinessNumTempServiceImpl.class);
    private final BusinessNumTempMapper businessNumTempMapper;

    @Autowired
    public BusinessNumTempServiceImpl(
            BusinessNumTempMapper businessNumTempMapper,
            RedisCache redisCache) {
        this.businessNumTempMapper = businessNumTempMapper;
        this.redisCache = redisCache;
    }

    /**
     *  取号
     *  根据 类型 DT , CJ ..... 获取当前类型中在数据库中存在的最大编号 , 如果为null 那么就设置为 1
     * @param deviceType DT , CJ ,.....
     * @return
     */
    private Integer getDatabaseInnerMaxNumberByType(String deviceType) {
        return businessNumTempMapper.selectMaxBusinessNumByType(deviceType);
    }

    @Override
    public Integer saveCurrentOperationNumberToRedis(String deviceType) {
        int resultNumber = 0;
        try {
            /*
                如果不加锁，可能会产生非原子性操作
                    使用cas 或者 lock 都可以
             */
//            int srcItem = 0;
//            AtomicInteger targetItem = new AtomicInteger(srcItem);
//            if (targetItem.compareAndSet(srcItem,srcItem+1)){
//                int handleAfterItem = targetItem.get();
//            }
            lock.lock();
            List<Integer> dataBaseNumberList = businessNumTempMapper.selectTableListByType(deviceType);
            if (redisCache.getBusinessNumberCacheMapValue(deviceType) == null){
                if (dataBaseNumberList.size() == 0){
                    // redis 无数据 ， 数据库无数据
                    resultNumber = 1;
                }else {
                    // redis 无数据 ， 数据库有数据 ， 则根据数据库数据列表找到合法编号（）
                    resultNumber = CollectionUtilX.getListElementIsContinuousToNumber(dataBaseNumberList);
                }
            }else {
                List<Integer> redisList = redisCache.getBusinessNumberCacheMapValue(deviceType);
                if (dataBaseNumberList.size() == 0){
                    // redis 有数据 ， 数据库无数据 (场景：多个人一起使用同意设备类型获取编号 ， 但是没有存库)
                    resultNumber = CollectionUtilX.getListElementIsContinuousToNumber(redisList);
                }else {
                    // redis 有数据 ， 数据库有数据 ， 要根据数据库和redis数据列表共同确定当前 返回编号
                    resultNumber = CollectionUtilX.getDataBaseAndRedisToResult(dataBaseNumberList,redisList);
                }
            }
            List<Integer> saveRedisList = new ArrayList<>();
            saveRedisList.add(resultNumber);
            redisCache.setBusinessNumberCacheMapValue(deviceType,saveRedisList);
            logger.info("redis--入库编号-->:" + resultNumber);
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            lock.unlock();
        }
        return resultNumber;
    }

    @Override
    public boolean handleCurrentCancelNumber(String deviceType, String ajaxNumber) {
        try {
            lock.lock();
            List<Integer> redisCacheNumberList = redisCache.getBusinessNumberCacheMapValue(deviceType);
            if (redisCacheNumberList != null && redisCacheNumberList.remove(Integer.valueOf(ajaxNumber))){
                if (redisCacheNumberList.size() == 0){
                    redisCache.clearBusinessNumberCacheMapOneValueByKey(deviceType);
                }else {
                    redisCache.setBusinessNumberCacheMapValue(deviceType,redisCacheNumberList);
                }
                return true;
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            lock.unlock();
        }
        return false;
    }

    @Override
    public List<Object> selectBusinessRowIds() {
        QueryWrapper<BusinessNumTemp> wrapper = new QueryWrapper<>();
        wrapper.select("business_row_id");
        return businessNumTempMapper.selectObjs(wrapper);
    }

}
