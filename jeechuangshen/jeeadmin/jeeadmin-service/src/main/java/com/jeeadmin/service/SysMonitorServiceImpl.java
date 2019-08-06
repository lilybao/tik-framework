package com.jeeadmin.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jeeadmin.api.ISysMonitorService;
import com.jeeadmin.mapper.SysMonitorMapper;
import com.jeeadmin.vo.monitor.MsaOrgUserVo;
import com.jeeadmin.vo.monitor.MsaRoleUserVo;
import com.jeerigger.frame.util.ByteUtil;
import com.jeerigger.frame.util.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import oshi.SystemInfo;
import oshi.hardware.CentralProcessor;
import oshi.hardware.GlobalMemory;
import oshi.software.os.FileSystem;
import oshi.software.os.OSFileStore;
import oshi.util.FormatUtil;

import java.lang.management.*;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SysMonitorServiceImpl implements ISysMonitorService {
    @Autowired
    private SysMonitorMapper monitorMapper;

    @Override
    public List<MsaOrgUserVo> selectMsaOrgUser() {
        return monitorMapper.selectMsaOrgUserPage(new Page(0, 5)).getRecords();
    }

    @Override
    public List<MsaRoleUserVo> selectMsaRoleUser() {
        return monitorMapper.selectMsaRoleUserPage(new Page(0, 5)).getRecords();
    }

    @Override
    public Map<String, String> getJvmInfo() {
        Map<String, String> map = new HashMap<>();
        Runtime runtime = Runtime.getRuntime();
        double totalMemory = (double) runtime.totalMemory();
        double freeMemory = (double) runtime.freeMemory();
        //JVM总内存 total
        map.put("total", ByteUtil.formatByte((long) totalMemory));
        //JVM剩余内存 free
        map.put("free", ByteUtil.formatByte((long) freeMemory));
        //JVM已用内存 used
        map.put("used", ByteUtil.formatByte((long) (totalMemory - freeMemory)));

        long usedPerc = Math.round((totalMemory - freeMemory) / totalMemory * 100.0D);
        //JVM使用率 usedPerc
        map.put("usedPerc", usedPerc + "%");

        MemoryMXBean memoryMXBean = ManagementFactory.getMemoryMXBean();
        MemoryUsage memoryUsage = memoryMXBean.getHeapMemoryUsage();
        //堆初始大小 heapInit
        map.put("heapInit", ByteUtil.formatByte(memoryUsage.getInit()));
        //堆最大内存 heapMax
        map.put("heapMax", ByteUtil.formatByte(memoryUsage.getMax()));
        //堆已用内存 heapUsed
        map.put("heapUsed", ByteUtil.formatByte(memoryUsage.getUsed()));
        //堆可用内存 heapAvailable
        map.put("heapAvailable", ByteUtil.formatByte(memoryUsage.getMax() - memoryUsage.getUsed()));

        memoryUsage = memoryMXBean.getNonHeapMemoryUsage();

        //非堆初始大小 heapInit
        map.put("nonHeapInit", ByteUtil.formatByte(memoryUsage.getInit()));
        //非堆最大内存 heapMax
        map.put("nonHeapMax", ByteUtil.formatByte(memoryUsage.getMax()));
        //非堆已用内存 heapUsed
        map.put("nonHeapUsed", ByteUtil.formatByte(memoryUsage.getUsed()));
        //非堆可用内存 heapAvailable
        map.put("nonHeapAvailable", ByteUtil.formatByte(memoryUsage.getMax() == -1L ? -1L : memoryUsage.getMax() - memoryUsage.getUsed()));
        return map;
    }

    @Override
    public Map<String, String> getSystemInfo() {
        Map<String, String> map = new HashMap<>();
        try {
            InetAddress inetAddress = InetAddress.getLocalHost();
            //服务器名称 hostName
            map.put("hostAddress", inetAddress.getHostAddress());
            //服务器地址 hostAddress
            map.put("hostName", inetAddress.getHostName());
        } catch (UnknownHostException var5) {
            var5.printStackTrace();
        }
        OperatingSystemMXBean operatingSystemMXBean = ManagementFactory.getOperatingSystemMXBean();
        //操作系统 osName：
        map.put("osName", operatingSystemMXBean.getName());
        //osVersion
        map.put("osVersion", operatingSystemMXBean.getVersion());
        //osProcessors
        map.put("osProcessors", operatingSystemMXBean.getAvailableProcessors() + "");
        //osProcessors
        //服务器架构 osArch
        map.put("osArch", operatingSystemMXBean.getArch());
        RuntimeMXBean runtimeMXBean = ManagementFactory.getRuntimeMXBean();
        //java名称 javaName
        map.put("javaName", System.getProperty("java.vm.name"));

        //java 版本 javaVersion
        map.put("javaVersion", System.getProperty("java.runtime.version") + "/" + System.getProperty("java.vm.version"));

        //java供应商 javaVendor
        map.put("javaVendor", System.getProperty("java.vm.vendor"));
        //java地址 javaHome
        map.put("javaHome", System.getProperty("java.home"));

        //java参数 javaArgs
        map.put("javaArgs", runtimeMXBean.getInputArguments().toString());

        //java启动时间 startTime
        map.put("startTime", DateUtil.formatDate(runtimeMXBean.getStartTime(), "yyyy-MM-dd HH:mm:ss"));

        //java运行时间 uptime
        map.put("uptime", DateUtil.formatDateAgo(runtimeMXBean.getUptime()));

        //工作路径 userDir
        map.put("userDir", System.getProperty("user.dir"));
        map.put("logPath", System.getProperty("logPath"));
        map.put("userfilesDir", "");

        return map;
    }

    @Override
    public Map<String, String> getMemInfo() {
        Map<String, String> map = new HashMap<>();
        SystemInfo systemInfo = new SystemInfo();
        GlobalMemory memory = systemInfo.getHardware().getMemory();
        double totalMemory = memory.getTotal();
        double freeMemory = memory.getAvailable();
        //总内存 total
        map.put("total", ByteUtil.formatByte((long) totalMemory));
        //剩余内存 free
        map.put("free", ByteUtil.formatByte((long) freeMemory));
        //已用内存 used
        map.put("used", ByteUtil.formatByte((long) (totalMemory - freeMemory)));
        long usedPerc = Math.round(((totalMemory - freeMemory) / totalMemory) * 100.0D);
        //使用率 usedPerc
        map.put("usedPerc", usedPerc + "%");
        return map;
    }

    @Override
    public List<Map<String, String>> getDiskListInfo() {
        SystemInfo systemInfo = new SystemInfo();
        FileSystem fileSystem = systemInfo.getOperatingSystem().getFileSystem();
        List<Map<String, String>> listMap = new ArrayList<>();
        OSFileStore[] fsArray = fileSystem.getFileStores();
        for (OSFileStore fs : fsArray) {
            Map<String, String> map = new HashMap<>();
            long total = fs.getTotalSpace();
            long free = fs.getUsableSpace();
            //盘符路径 dirName：
            map.put("dirName", fs.getMount().replaceAll("\\\\", ""));
            //文件系统 typeName
            map.put("typeName", fs.getType());
            //文件系统 getDescription
            map.put("description", fs.getDescription());
            //总大小 total
            map.put("total", ByteUtil.formatByte(total));
            //可用大小 free
            map.put("free", ByteUtil.formatByte(free));
            //已用大小 used
            map.put("used", ByteUtil.formatByte(total - free));
            long usedPerc = Math.round(100d * (total - free) / total);
            //已用百分比 usedPerc
            map.put("usedPerc", usedPerc + "%");
            listMap.add(map);
        }
        return listMap;
    }

    @Override
    public Map<String, String> getCpuInfo() {
        SystemInfo systemInfo = new SystemInfo();
        CentralProcessor processor = systemInfo.getHardware().getProcessor();
        Map<String, String> map = new HashMap<>();
        map.put("vendor", processor.getVendor());
        map.put("model", processor.getModel());
        map.put("maxGhz", FormatUtil.formatHertz(processor.getVendorFreq()));
        map.put("cpuNum", processor.getLogicalProcessorCount() + "个");
        long[] prevTicks = processor.getSystemCpuLoadTicks();
        long idle = prevTicks[CentralProcessor.TickType.IDLE.getIndex()];
        long user = prevTicks[CentralProcessor.TickType.USER.getIndex()];
        long nice = prevTicks[CentralProcessor.TickType.NICE.getIndex()];
        long system = prevTicks[CentralProcessor.TickType.SYSTEM.getIndex()];
        long ioWait = prevTicks[CentralProcessor.TickType.IOWAIT.getIndex()];
        long irq = prevTicks[CentralProcessor.TickType.IRQ.getIndex()];
        long softIrq = prevTicks[CentralProcessor.TickType.SOFTIRQ.getIndex()];
        long steal = prevTicks[CentralProcessor.TickType.STEAL.getIndex()];
        long totalCpu = user + nice + system + ioWait + irq + softIrq + steal;
        DecimalFormat decimalFormat = new DecimalFormat("0.00");
        //System.out.println(decimalFormat.format(processor.getVendorFreq()/totalCpu/processor.getPhysicalProcessorCount()));
        map.put("currGhz", decimalFormat.format(processor.getVendorFreq() / totalCpu / processor.getPhysicalProcessorCount()) + "Ghz");
        map.put("usedPerc", Math.round(processor.getSystemCpuLoadBetweenTicks() * 100.0D) + "%");
        map.put("cacheSize", "");

        return map;
    }
}
