package com.api.freeapi.utils;

import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

/**
 * redisson 分布式工具类
 *
 * @author gaoyang
 * @date 2022-05-14 08:58
 */
@Slf4j
@Component
public class  RedissonUtils {

  @Resource
  private  RedissonClient redissonClient;

  /**
   * 加锁
   *
   * @param lockKey
   */
  public  void  lock(String lockKey) {
    RLock lock = redissonClient.getLock(lockKey);
    lock.lock();
  }

  /**
   * 带过期时间的锁
   *
   * @param lockKey   key
   * @param leaseTime 上锁后自动释放锁时间
   */
  public  void lock(String lockKey, long leaseTime) {
    RLock lock = redissonClient.getLock(lockKey);
    lock.lock(leaseTime, TimeUnit.SECONDS);
  }

  /**
   * 带超时时间的锁
   *
   * @param lockKey   key
   * @param leaseTime 上锁后自动释放锁时间
   * @param unit      时间单位
   */
  public  void lock(String lockKey, long leaseTime, TimeUnit unit) {
    RLock lock = redissonClient.getLock(lockKey);
    lock.lock(leaseTime, unit);
  }

  /**
   * 尝试获取锁
   *
   * @param lockKey key
   * @return
   */
  public  boolean tryLock(String lockKey) {
    RLock lock = redissonClient.getLock(lockKey);
    return lock.tryLock();
  }

  /**
   * 尝试获取锁
   *
   * @param lockKey   key
   * @param waitTime  最多等待时间
   * @param leaseTime 上锁后自动释放锁时间
   * @return boolean
   */
  public  boolean tryLock(String lockKey, long waitTime, long leaseTime) {
    RLock lock = redissonClient.getLock(lockKey);
    try {
      return lock.tryLock(waitTime, leaseTime, TimeUnit.SECONDS);
    } catch (InterruptedException e) {
      log.error("RedissonUtils - tryLock异常", e);
    }

    return false;
  }

  /**
   * 尝试获取锁
   *
   * @param lockKey   key
   * @param waitTime  最多等待时间
   * @param leaseTime 上锁后自动释放锁时间
   * @param unit      时间单位
   * @return boolean
   */
  public   boolean tryLock(String lockKey, long waitTime, long leaseTime, TimeUnit unit) {
    RLock lock = redissonClient.getLock(lockKey);
    try {
      return lock.tryLock(waitTime, leaseTime, unit);
    } catch (InterruptedException e) {
      log.error("RedissonUtils - tryLock异常", e);
    }

    return false;
  }

  /**
   * 释放锁
   *
   * @param lockKey key
   */
  public  void unlock(String lockKey) {
    RLock lock = redissonClient.getLock(lockKey);
    lock.unlock();
  }

  /**
   * 是否存在锁
   *
   * @param lockKey key
   * @return
   */
  public  boolean isLocked(String lockKey) {
    RLock lock = redissonClient.getLock(lockKey);
    return lock.isLocked();
  }
}
