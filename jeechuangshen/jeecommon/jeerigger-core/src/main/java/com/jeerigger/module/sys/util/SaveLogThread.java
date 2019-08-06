package com.jeerigger.module.sys.util;

import com.jeerigger.frame.support.util.SpringUtil;
import com.jeerigger.module.sys.entity.SysLog;
import com.jeerigger.module.sys.mapper.LogMapper;

import java.util.Queue;
import java.util.concurrent.LinkedTransferQueue;
import java.util.concurrent.locks.Lock;

/**
 * @ Author     ：Seven Lee
 * @ Date       ：上午9:36 2019/1/29
 * @ Description：
 * @ Version    ：
 */
public class SaveLogThread extends Thread {

    private SaveLogThread() {

    }

    private static SaveLogThread saveLogThread = null;
    public Queue<SysLog> queue = new LinkedTransferQueue<SysLog>();

    public static SaveLogThread getInstance() {
        if (saveLogThread == null) {
            synchronized (SaveLogThread.class) {
                if (saveLogThread == null) {
                    saveLogThread = new SaveLogThread();
                }
            }
        }
        return saveLogThread;
    }

    private LogMapper getLogMapper() {
        return SpringUtil.getBean(LogMapper.class);
    }

    @Override
    public void run() {
        while (!queue.isEmpty()) {
            SysLog sysLog = queue.poll();
            int insertResult = getLogMapper().insert(sysLog);
            if (insertResult > 0) {
                queue.remove(sysLog);
            }
        }
//        synchronized (Lock.class) {
//            try {
//                Lock.class.wait();
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        }
    }
}
