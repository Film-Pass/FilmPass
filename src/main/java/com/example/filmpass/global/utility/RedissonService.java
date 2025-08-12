package com.example.filmpass.global.utility;

import com.example.filmpass.global.exception.CustomException;
import com.example.filmpass.global.exception.ErrorCode;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Rdisson 기반의 분산락 처리 전용 서비스
 * - 복합 키 바탕으로 락을 획득하고, 주어진 로직을 안전하게 실행
 * - 락 획득 실패시 예외 발생
 */
@Component
public class RedissonService {

    // Redisson 클라이언트: Redis 서버와 연결된 객체
    private final RedissonClient redissonClient;

    // 생성자 주입을 통한 Redisson 객체 주입
    public RedissonService(RedissonClient redissonClient) {
        this.redissonClient = redissonClient;
    }

    /**
     * 주어진 key로 분산락을 획득한 후, 해당 로직을 실행
     *
     * @param key      Redis에 사용할 락 키: 스케쥴Id와 좌석Id 조합으로 유니크함
     * @param waitTime 락을 획득하기 위해 기다리는 최대 시간
     * @param useTime  락을 소유 후 자동 해제까지 걸리는 시간
     * @param executor 락을 얻으면 실행할 사용자 로직
     * @param <T>      로직 실행 결과 반환 타입은 다양할 수 있음으로 제네릭 타입으로 받음
     * @throws CustomException 쓰레드가 락을 획득하기 위해 대기 중 중단 명령을 받았을 때 던지는 예외
     *
     */
    public <T> T runWithLock(String key, long waitTime, long useTime, LockExecutor<T> executor) {

        // key 값을 기반으로 분산 락 객체 생성
        RLock lock = redissonClient.getLock(key);

        try {

            // 락을 일정 시간동안 시도하고 획득하면 useTime만큼 유지. 들어온 Long값은 초 단위로 명시
            boolean isLock = lock.tryLock(waitTime, useTime, TimeUnit.SECONDS);

            // 락 획득에 실패했을때 예외처리
            if (!isLock) {
                throw new CustomException(ErrorCode.SEAT_RESERVATION_LOCKED);
            }
            // 락을 획득하면 비지니스 로직을 실행
            return executor.execute();

        } catch (InterruptedException e) {
            // 현재 쓰레드의 인터럽트 플래그를 다시 살려서 다음 로직에서도 인터럽트 여부 알 수 있게 함
            Thread.currentThread().interrupt();
            // 커스텀 예외 처리
            throw new CustomException(ErrorCode.THREAD_INTERRUPTED);
        } finally {
            // 현재 쓰레드가 락을 가지고 있는 경우에만 락을 해제
            if (lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
        }
    }

    public <T> T runWithMultiLock(List<String> keys, long waitTime, long useTime, LockExecutor<T> executor) {

        List<RLock> locks = keys.stream()
                .map(redissonClient::getLock)
                .toList();

        List<RLock> acquiredLocks = new ArrayList<>();

        try {
            for (RLock lock : locks) {
                boolean isLock = lock.tryLock(waitTime, useTime, TimeUnit.SECONDS);
                if (!isLock) {
                    throw new CustomException(ErrorCode.SEAT_RESERVATION_LOCKED);
                }
                acquiredLocks.add(lock);
            }
            return executor.execute(); // Transaction 전파 확인하기 분리함으로써 잘한것인가 / 구조를 복잡하게 한게 아닐까?

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new CustomException(ErrorCode.THREAD_INTERRUPTED);

        } finally {
            for (RLock lock : acquiredLocks) {
                if (lock.isHeldByCurrentThread()) {
                    lock.unlock();
                }
            }
        }
    }

    public <T> T runWithPhysicalMultiLock(List<String> keys, long waitTime, long useTime, LockExecutor<T> executor) {

        RLock[] locks = keys.stream()
                .map(redissonClient::getLock)
                .toArray(RLock[]::new);

        RLock multiLock = redissonClient.getMultiLock(locks);

        try {
            boolean isLock = multiLock.tryLock(waitTime, useTime, TimeUnit.SECONDS);
            if (!isLock) {
                throw new CustomException(ErrorCode.SEAT_RESERVATION_LOCKED);
            }
            return executor.execute();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new CustomException(ErrorCode.THREAD_INTERRUPTED);
        } finally {
            if (multiLock.isHeldByCurrentThread()) {
                multiLock.unlock();
            }
        }
    }


    /**
     * 락을 획득한 후 실행할 로직을 전달하기 위한 함수형 인터페이스
     * 다양한 반환 값을 사용할 수 있게 제네릭 T 타입을 활용
     */
    @FunctionalInterface
    public interface LockExecutor<T> {

        // 실제로 실행할 로직
        T execute();
    }
}
