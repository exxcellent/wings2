package wingset;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wings.session.WingsStatistics;
import org.wings.util.SStringBuilder;

import java.io.File;
import java.io.FileWriter;
import java.util.Timer;
import java.util.TimerTask;

/**
 * A simple {@link TimerTask} implementation logging some statistics received from the WingS framework
 * and the JVM to a file in a ergular interval.
 * Nice to monitor application load and resource needs.
 *
 * @author Benjamin Schmid
 */
public class StatisticsTimerTask extends TimerTask {
    private final static Log log = LogFactory.getLog(StatisticsTimerTask.class);
    private static final TimerTask infoTask = new StatisticsTimerTask();
    private static final Timer timer = new Timer();
    private static long oldRequestCount = 0;
    private static long oldSessionCount = 0;
    private static FileWriter infoWriter;


    private StatisticsTimerTask() {
    }

    /**
     * Do not call this method directly. Used by Timer.
     */
    public void run() {
        SStringBuilder result = new SStringBuilder();
        long totalmem = Runtime.getRuntime().totalMemory();
        long freemem = Runtime.getRuntime().freeMemory();

        WingsStatistics stats = WingsStatistics.getStatistics();

        result.append(System.currentTimeMillis()).append(' ')
                .append(stats.getUptime()).append(' ')
                .append(stats.getOverallSessionCount()).append(' ')
                .append(stats.getOverallSessionCount() - oldSessionCount).append(' ')
                .append(stats.getActiveSessionCount()).append(' ')
                .append(stats.getAllocatedSessionCount()).append(' ')
                .append(stats.getRequestCount()).append(' ')
                .append(stats.getRequestCount() - oldRequestCount).append(' ')
                .append(totalmem).append(' ')
                .append(freemem).append(' ')
                .append(totalmem - freemem).append('\n');

        oldRequestCount = stats.getRequestCount();
        oldSessionCount = stats.getOverallSessionCount();

        try {
            if (infoWriter != null) {
                infoWriter.write(result.toString());
                infoWriter.flush();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Start logging of some wings statistics information into a temporary file calles wings-statisticsxxx.log
     *
     * @param intervalSeconds Interval to do logging in.
     */
    public static void startStatisticsLogging(int intervalSeconds) {
        initInfoWriter();
        timer.scheduleAtFixedRate(infoTask, 0, intervalSeconds * 1000);
    }

    private static void initInfoWriter() {
        try {
            if (infoWriter == null) {
                final File outputFile = File.createTempFile("wings-statistics", "log");
                log.info("Logging session statistics to "+outputFile.getAbsolutePath());
                infoWriter = new FileWriter(outputFile, false);

                SStringBuilder result = new SStringBuilder();
                result.append("timestamp").append(' ')
                        .append("uptime").append(' ')
                        .append("overall_sessions").append(' ')
                        .append("new_sessions").append(' ')
                        .append("active_sessions").append(' ')
                        .append("allocated_sessions").append(' ')
                        .append("overall_processed requests").append(' ')
                        .append("processed_requests").append(' ')
                        .append("total_memory").append(' ')
                        .append("free_memory").append(' ')
                        .append("used_memory").append('\n');
                infoWriter.write(result.toString());
                infoWriter.flush();
            }
        } catch (Exception ex) {
            log.error("Exception on logging session statistics.");
        }
    }

}
