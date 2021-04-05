package com.synstorm.common.Utils.ConsoleProgressBar;

import com.synstorm.common.Utils.Annotations.Classes.Model_v1;
import com.synstorm.common.Utils.EnumTypes.ConsoleColors;
import com.synstorm.common.Utils.SimArgs.SimulationArguments;
import com.synstorm.common.Utils.TraceMessageWriter.PriorityTraceWriter;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Abstract class for all types of console progress bars.
 * Created by Dmitry.Bozhko on 3/12/2015.
 */

@Model_v1

public abstract class BaseConsoleProgressBar implements IProgressBar {
    //region Fields
    private final int updateFrequency;
    private int prevPercentage;
    private long modelingTime;
    private String title;
    private String progressBar;
    private final List<AdditionalInfo> additionalInfoCollection;
    private String startFullMessageTimeStamp;
    private String startShortMessageTimeStamp;
    private final String experimentName;
    private final PercentageUpdater percentageUpdater;

    protected int percentageDone;
    protected long tEnd;
    protected final long tStart;
    protected final String separator;
    protected final DateFormat modelingTimeFormatter;
    protected final DateFormat dateTimeFormatter;
    protected final DecimalFormat performanceFormatter;
    protected int stageCount;
    protected int stageNumber;
    protected long stageStart;
    protected long stageEnd;
    //endregion

    //region Constructors
    public BaseConsoleProgressBar(String barTitle, String simulationName, int stageCnt, long simulationStartTime) {
        updateFrequency = SimulationArguments.INSTANCE.getUpdateFrequency();

        if (updateFrequency > 0)
            percentageUpdater = this::updatePercentageWithFrequency;
        else
            percentageUpdater = this::updatePercentageWithoutFrequency;

        title = barTitle;
        modelingTime = 0;
        prevPercentage = 0;
        percentageDone = 0;
        separator = " | ";
        additionalInfoCollection = new ArrayList<>();
        performanceFormatter = new DecimalFormat("######.####");
        modelingTimeFormatter = new SimpleDateFormat("HH:mm:ss:SSS");
        modelingTimeFormatter.setTimeZone(TimeZone.getTimeZone("GMT"));
        dateTimeFormatter = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss:SSS");
        dateTimeFormatter.setTimeZone(TimeZone.getTimeZone("GMT"));
        startFullMessageTimeStamp = "";
        startShortMessageTimeStamp = "";
        experimentName = simulationName;
        tStart = simulationStartTime;
        stageCount = stageCnt;
        stageNumber = 0;
        stageStart = 0;
        stageEnd = 0;
    }
    //endregion

    //region Getters and Setters
    //endregion

    //region Public Methods
    @Override
    public void showEmptyBar() {
        //sendGreetingSlackMessage();
        progressBar = "[                    ]";
        String dateFormatted = getModelingTime();
        PriorityTraceWriter.print("\r" + title + " %: " + progressBar + "   0%" + separator + dateFormatted, 0);
    }

    @Override
    public void updatePercentage(Object... args) {
        percentageUpdater.update(args);
    }

    private void updatePercentageWithFrequency(Object... args) {
        percentageDone = (int) args[0];
        final boolean isPrevEqual = prevPercentage == percentageDone;
        if (percentageDone % updateFrequency == 0 && !isPrevEqual) {
            composePercentage(args);
            //sendShortUpdateStatusSlackMessage(args);
        }
    }

    private void updatePercentageWithoutFrequency(Object... args) {
        percentageDone = (int) args[0];
        composePercentage(args);
        //sendShortUpdateStatusSlackMessage(args);
    }

    @Override
    public void printModelStatus(UUID modelId, long conditionCounter) {
        PriorityTraceWriter.println("\rIndividual: " + modelId + ": " + ConsoleColors.ANSI_GREEN.value() +
                "Done!" + ConsoleColors.ANSI_RESET.value() + separator + calculateAdditionalInfo(conditionCounter) +
                separator + "Modeling time(Mt): " + getModelingTime(), 0);
    }

    @Override
    public void addAdditionalInfoList(List<AdditionalInfo> additionalInfoList) {
        additionalInfoCollection.addAll(additionalInfoList);
    }

    @Override
    public void addAdditionalInfo(AdditionalInfo additionalInfo) {
        additionalInfoCollection.add(additionalInfo);
    }

    @Override
    public void printAllAdditionalInfo() {
        PriorityTraceWriter.println("\n", 0);
        additionalInfoCollection.stream()
                .forEach(item -> PriorityTraceWriter.println(item.getInfo(), item.getLevel()));
    }

    @Override
    public String getModelingTime() {
        Date date = new Date(modelingTime);
        return modelingTimeFormatter.format(date);
    }

    @Override
    public String getStartTime() {
        return dateTimeFormatter.format(tStart);
    }

//    @Override
//    public void sendFinalSlackMessage() {
//        if (!SimulationArguments.INSTANCE.isSlack())
//            return;
//
//        String outMessage = "*" + experimentName + "*: " + ConstantValues.AWS_S3_BUCKET +
//                String.valueOf(tStart) + ".tar.gz";
//        PriorityTraceWriter.updateShortMessageInSlack(startShortMessageTimeStamp, outMessage);
//        sendFullFinalSlackMessage();
//        PriorityTraceWriter.disconnectSlackSession();
//    }

    @Override
    public void startStage(int stageNum) {
        stageNumber = stageNum;
        stageStart = System.currentTimeMillis();
        PriorityTraceWriter.println("\nStage #" + stageNumber + " started", 0);
        showEmptyBar();
    }

//    @Override
//    public void updateStagePercentage(Object... args) {
//        percentageDone = (int) args[0];
//        composePercentage(args);
//        //sendShortUpdateStatusSlackMessage(args);
//    }

    @Override
    public void endStage() {
        stageEnd = System.currentTimeMillis();
        PriorityTraceWriter.println("\nStage ended. ", 0);
    }

    private void showEmptyStageBar() {
        progressBar = "[                    ]";
        String dateFormatted = getModelingTime();
        PriorityTraceWriter.print("\r" + title + " %: " + progressBar + "   0%" + separator + dateFormatted, 0);
    }

    //endregion

    //region Protected Methods
    protected abstract String calculateAdditionalInfo(Object... args);//long conditionCounter);

    protected double calculatePerformance(long conditionCounter) {
        return (conditionCounter) / ((double) (tEnd - tStart) / 1000);
    }

    protected void composePercentage(Object... args) {//} long conditionCounter) {
        final int percentage = (int) args[0];
        calculateModelingTime();
        String dateFormatted = getModelingTime();
        String percentFormatted = String.format("%1$4s", percentage) + "%";
        StringBuilder percentBar = new StringBuilder();
        int progressCount = percentage / 5;

        for (int i = 1; i <= progressCount; i++)
            percentBar.append("#");

        progressBar = "[" + String.format("%1$-20s", percentBar.toString()) + "]";
        String additionalInfo = calculateAdditionalInfo(args);

        PriorityTraceWriter.print("\r" + title + " %: " + progressBar + percentFormatted +
                separator + dateFormatted + separator + additionalInfo, 0);

        prevPercentage = percentage;
    }
    //endregion

    //region Private Methods
    private void calculateModelingTime() {
        tEnd = System.currentTimeMillis();
        modelingTime = tEnd - tStart;
    }

//    private void sendGreetingSlackMessage() {
//        if (!SimulationArguments.INSTANCE.isSlack())
//            return;
//
//        final String shortMessage = "*" + experimentName + "*: started";
//        PriorityTraceWriter.allowSlackOutput();
//        startShortMessageTimeStamp = PriorityTraceWriter.sendShortMessageToSlack(shortMessage);
//        sendFullGreetingSlackMessage();
//    }
//
//    private void sendShortUpdateStatusSlackMessage(Object... args) {
//        if (!SimulationArguments.INSTANCE.isSlack())
//            return;
//
//        String outMessage = String.format("*%s*:\nStage: *%d/%d* | Progress: *%d%%*\n%s", experimentName, stageNumber + 1, stageCount, percentageDone, calculateAdditionalInfo(args));
//        PriorityTraceWriter.updateShortMessageInSlack(startShortMessageTimeStamp, outMessage);
//    }
//
//    private void sendFullGreetingSlackMessage() {
//        if (!SimulationArguments.INSTANCE.isSlackFull())
//            return;
//
//        final String outMessage = "Experiment: *" + experimentName + "*\nModel processor: *" + SimulationArguments.INSTANCE.getProcessor() +
//                "*\nModeling start time: *" + getStartTime() +
//                "* GMT\nStatus: *Has started!*";
//        startFullMessageTimeStamp = PriorityTraceWriter.sendMessageToSlack(outMessage);
//    }
//
//    public void sendFullFinalSlackMessage() {
//        if (!SimulationArguments.INSTANCE.isSlackFull())
//            return;
//
//        String outMessage = "Experiment: *" + experimentName + "*\nModel processor: *" + SimulationArguments.INSTANCE.getProcessor() +
//                "*\nModeling start time: *" + getStartTime() +
//                "* GMT\nModeling duration: *" + getModelingTime() + "*\nResults: " + ConstantValues.AWS_S3_BUCKET +
//                String.valueOf(tStart) + ".tar.gz\nStatus: ";
//        if (!startFullMessageTimeStamp.equals(""))
//            outMessage += "*Done!*";
//        else
//            outMessage += "*Done with errors!*";
//
//        PriorityTraceWriter.updateMessageInSlack(startFullMessageTimeStamp, outMessage);
//    }
    //endregion

    @FunctionalInterface
    private interface PercentageUpdater {
        void update(Object... args);
    }
}
