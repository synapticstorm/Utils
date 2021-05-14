package com.synstorm.common.Utils.SimArgs;

import com.synstorm.common.Utils.Annotations.Classes.Model_v1;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;

/**
 * Class container for defined console arguments for simulation.
 * Created by dvbozhko on 23/01/2017.
 */
@Model_v1
public enum SimulationArguments {
    INSTANCE;

    //region Fields


    ////////////////////////////////////////////////////////////
//    @Option(name="-processor", usage = "Processor defines simulation mode")
//    private String processor = "SingleIndividualProcessor";

    @Option(name="-configPath", usage = "Path to a directory with configuration")
    private String configPath = "ModelConfiguration/SingleAxonGrowingTest";

    @Option(name="-logLevel", usage = "Simulation output messages level")
    private int logLevel = 0;

    @Option(name="-ticks", usage = "Total duration of the simulation")
    private long ticks = 10000L;

    @Option(name="-enableLogging", usage = "Telemetry: Enable remote logging to Elastic Search")
    private boolean enableLogging = false;

    @Option(name="-logConditions", usage = "Telemetry: Enable conditions execution logging")
    private boolean logConditions = false;

    @Option(name="-logResponses", usage = "Telemetry: Enable conditions execution logging")
    private String logResponses = "*";

    @Option(name="-mechSeed", usage = "Seed number for mechanisms duration delta")
    private long mechSeed = 32167L;

    @Option(name="-mechRange", usage = "Range for mechanisms duration delta")
    private double mechRange = 0.1;

    @Option(name="-slackToken", usage = "Slack: token")
    private String slackToken = "";
//
//    @Option(name="-teachSeedNum", usage = "Seed number for teacher from corresponding section")
//    private int teacherSeedNum = 0;
//
//    @Option(name="-evoSeedNum", usage = "Seed number for evolution from corresponding section")
//    private int evoSeedNum = 0;

//    @Option(name = "-slackShort", usage = "Send start/stop messages into defined slack channel")
//    private boolean slackShort = false;
//
//    @Option(name = "-slackFull", usage = "Update status messages into defined slack channel")
//    private boolean slackFull = false;
//
//    @Option(name = "-useHungryGeneAlgorithm", usage = "Use hungry algorithm to map actions to genes. If you not use this flag, simple algorithm will be used")
//    private boolean useHungryGeneAlgorithm = false;
//
//    @Option(name = "-randomIndividualSeeds", usage = "Use random seeds for INDIVIDUAL calculations")
//    private boolean randomIndividualSeeds = false;
//
//    @Option(name = "-randomTeacherSeeds", usage = "Use random seeds for LEARNING calculations")
//    private boolean randomTeacherSeeds = false;
//
//    @Option(name = "-initialMutations", usage = "Number of random mutations per individual for initial individuals")
//    private int initialMutations = 0;
//
//    @Option(name = "-allPopulationMutations", usage = "Number of random mutations per individual for all other individuals")
//    private int allPopulationMutations = 0;

    @Option(name = "-updateFrequency", usage = "Percentage value of progress bar and slack updates")
    private int updateFrequency = 5;
//    @Option(name = "-statisticExporters", usage = "Name of the exporters used in this session")
//    private String statisticExporters = "default";

//    @Option(name = "-hasGenes", usage = "Do the model has genes?")
//    private int hasGenes = 1;
    //endregion

    //region Constructors
    public void proceedArguments(String[] args) throws Exception {
        CmdLineParser parser = new CmdLineParser(this);
        parser.parseArgument(args);
    }
    //endregion

    //region Getters and Setters

    public String getConfigPath() {
        return configPath;
    }

    public int getLogLevel() {
        return logLevel;
    }

    public boolean isEnableLogging() {
        return enableLogging;
    }

    public boolean isLogConditions() {
        return logConditions;
    }

    public String getLogResponses() {
        return logResponses;
    }

    public long getMechSeed() {
        return mechSeed;
    }

    public double getMechRange() {
        return mechRange;
    }

    public long getTicks() {
        return ticks;
    }

    public String getSlackToken() {
        return slackToken;
    }

    public int getUpdateFrequency() {
        return updateFrequency;
    }
    //endregion

    //region Public Methods
    //endregion

    //region Package-local Methods
    //endregion

    //region Protected Methods
    //endregion

    //region Private Methods
    //endregion
}
