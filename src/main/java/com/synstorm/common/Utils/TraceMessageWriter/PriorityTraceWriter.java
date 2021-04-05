package com.synstorm.common.Utils.TraceMessageWriter;

import allbegray.slack.SlackClientFactory;
import allbegray.slack.webapi.SlackWebApiClient;
import com.synstorm.common.Utils.Annotations.Classes.Model_v0;
import com.synstorm.common.Utils.Annotations.Classes.ProductionLegacy;
import com.synstorm.common.Utils.SimArgs.SimulationArguments;
import org.jetbrains.annotations.Nullable;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by Dmitry.Bozhko on 12/10/2014.
 */
@Model_v0
@ProductionLegacy
public class PriorityTraceWriter {
    //region Fields
    private static int maxAllowedLevel = 0;
    private static boolean slackOutputEnabled = false;
    private static BufferedWriter bufferedWriter;
    private static StringBuilder stringBuilder;

    private static SlackWebApiClient webApiClient;
    private static String shortChannelId;
    private static String fullChannelId;
    //endregion

    //region Constructors
    //endregion

    //region Getters and Setters
    public static void setLevel(int maximumAllowedLevel) {
        maxAllowedLevel = maximumAllowedLevel;
    }

    public static void initFile(String name) {
        long tStart = System.currentTimeMillis();
        DateFormat dateFormatter = new SimpleDateFormat("dd_MM_YYYY_HH_mm");
        dateFormatter.setTimeZone(TimeZone.getTimeZone("GMT"));
        Date startTime = new Date(tStart);
        String fileName = name + "_" + dateFormatter.format(startTime) + ".log";
        File f = new File(fileName);
        stringBuilder = new StringBuilder();
        try {
            if (!f.exists())
                f.createNewFile();
            FileWriter fw = new FileWriter(f.getAbsoluteFile());
            bufferedWriter = new BufferedWriter(fw);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void closeFile() {
        try {
            bufferedWriter.write(stringBuilder.toString());
            bufferedWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void allowSlackOutput() {
        int tries = 5;
        do {
            tries = tryConnectToSlack(tries);
        } while (tries > 0);

        if (!slackOutputEnabled)
            System.out.println("=== Slack output disabled due to connection problems ===");
    }

    public static void disconnectSlackSession() {
        if (slackOutputEnabled)
            webApiClient.shutdown();

        slackOutputEnabled = false;
    }
    //endregion

    //region Public Methods
    public static void println(@Nullable Object message, int level) {
        if (level <= maxAllowedLevel)
            System.out.println(message);
    }

    public static void println(String message, int level) {
        if (level <= maxAllowedLevel)
            System.out.println(message);
    }

    public static void printf(int level, String format, Object... args) {
        if (level <= maxAllowedLevel)
            System.out.printf(format, args);
    }

    public static void print(String message, int level) {
        if (level <= maxAllowedLevel)
            System.out.print(message);
    }

    public static void printlnToFile(String message, int level) {
        if (level <= maxAllowedLevel)
            stringBuilder.append(message).append("\n");
    }

    public static String sendMessageToSlack(String message) {
        if (slackOutputEnabled)
            try {
                return webApiClient.postMessage(fullChannelId, message, "model-notifier", true);
            } catch (Exception ex) {

            }

        return "";
    }

    public static String sendShortMessageToSlack(String message) {
        if (slackOutputEnabled)
            try {
                return webApiClient.postMessage(shortChannelId, message, "model-notifier", true);
            } catch (Exception ex) {

            }

        return "";
    }

    public static void deleteMessageInSlack(String timeStamp) {
        if (slackOutputEnabled)
            try {
                webApiClient.deleteMessage(fullChannelId, timeStamp);
            } catch (Exception ex) {

            }
    }

    public static void deleteShortMessageInSlack(String timeStamp) {
        if (slackOutputEnabled)
            try {
                webApiClient.deleteMessage(shortChannelId, timeStamp);
            } catch (Exception ex) {

            }
    }

    public static void updateMessageInSlack(String timeStamp, String message) {
        if (slackOutputEnabled) {
            int tries = 3;
            do {
                tries = tryUpdateMessageInSlack(fullChannelId, timeStamp, message, tries);
            } while (tries > 0);
        }
    }

    public static void updateShortMessageInSlack(String timeStamp, String message) {
        if (slackOutputEnabled) {
            int tries = 3;
            do {
                tries = tryUpdateMessageInSlack(shortChannelId, timeStamp, message, tries);
            } while (tries > 0);
        }
    }
    //endregion

    //region Private Methods
    private static int tryConnectToSlack(int tries) {
        try {
            webApiClient = SlackClientFactory.createWebApiClient(SimulationArguments.INSTANCE.getSlackToken()); //удалил токен отсюда
            webApiClient.auth();
            webApiClient.getChannelList().stream().forEach(channel -> {
                if (channel.getName().equals("modeling_status_full"))
                    fullChannelId = channel.getId();
                else if (channel.getName().equals("modeling_status"))
                    shortChannelId = channel.getId();
            });

            slackOutputEnabled = true;
        } catch (Exception ex) {
            return tries - 1;
        }

        return -1;
    }

    private static int tryUpdateMessageInSlack(String channelId, String timeStamp, String message, int tries) {
        try {
            webApiClient.updateMessage(channelId, timeStamp, message);
        } catch (Exception ex) {
            return tries - 1;
        }

        return -1;
    }
    //endregion
}
