package cn.taqu.dns.check;

import cn.taqu.dns.util.ThreadPoolUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.UnknownHostException;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * 稳定类型检查任务
 * 当dns服务器出错时，停止定时任务
 * @author LinZhengTao
 * @date 2021/01/13
 */
public class StableTypeTask {
    private static final Logger logger = LoggerFactory.getLogger(StableTypeTask.class);

    /**
     * 将所有稳定类型dns进行检查
     * 若dns发生变化，记录日志
     * 若dns解析出错，则调用 remoteDnsNoResponse 进行检查dns服务器是否正常
     * 当dns服务器故障时，记录错误日志，并停止检查任务 30s
     */
    public static void doStableTypeTask(){
        StableTypeCheckStrategy stableTypeCheckStrategy = new StableTypeCheckStrategy();
        ScheduledExecutorService executorScheduledThreadService = ThreadPoolUtil.getExecutorScheduledThreadService();
        executorScheduledThreadService.scheduleAtFixedRate(()->{
            try {
                // 检查dns域名是否发生变化
                if (stableTypeCheckStrategy.differentDns()) {
                    logger.warn("检测出dns域名发生变化，并更新dns缓存数据成功。");
                } else {
                    logger.info("检测dns任务结束，自定义dns缓存可保持正常使用。");
                }
                // Inet6AddressImpl 的 lookupAllHostAddr 只有 UnknownHostException异常
            } catch (UnknownHostException e) {
                // 检查dns服务器是否正常
                if(stableTypeCheckStrategy.checkRemoteDnsServer()){
                    logger.warn("dns服务器正常，请检查dns配置");
                }else{
                    logger.error("由于dns服务器无法解析，暂停本次dns检查缓存任务30s。");
                    try {
                        Thread.sleep(30000L);
                    } catch (InterruptedException ie) {
                        logger.warn("稳定类型定时检查任务休眠被中断。", ie);
                    }
                }
            }
        },0, stableTypeCheckStrategy.checkTime(), TimeUnit.MILLISECONDS);

    }
}
