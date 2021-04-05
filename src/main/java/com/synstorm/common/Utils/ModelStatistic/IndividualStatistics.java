package com.synstorm.common.Utils.ModelStatistic;

import com.synstorm.common.Utils.Annotations.Classes.ReadyForCleanup;
import com.synstorm.common.Utils.EnumTypes.SimulationEvents;
import com.synstorm.common.Utils.EnumTypes.StatisticType;
import com.synstorm.common.Utils.SimulationEvents.ISimulationEvent;
import com.synstorm.common.Utils.SimulationEvents.IndividualEvents.*;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;


import java.util.*;

/**
 * Class for calculating and storing statistic for Individual model
 * Created by dvbozhko on 15/06/16.
 */

@ReadyForCleanup
public class IndividualStatistics {
    //region Fields
    private final UUID individualId;
    private long significantTick;
    private long significantTickCount;
    private int cellCount;
    private int maxCellCount;
    private int synapseCount;
    private int maxSynapseCount;
    private int minX, minY, minZ;
    private int maxX, maxY, maxZ;
//    private Map<TeacherMode, double[]> lqs;
    private Map<String, Integer> cellTypeCounts;
    private Set<Long> checkpointTicks;
    private String actionsHash;
    private final EnumMap<SimulationEvents, IProceedEvent> proceedMethods;
    //endregion

    //region Constructors
    public IndividualStatistics(UUID id, String hash) {
        individualId = id;
        actionsHash = hash;
        significantTick = 0;
        significantTickCount = 0;
        cellCount = 0;
        maxCellCount = 0;
        synapseCount = 0;
        maxSynapseCount = 0;
        minX = minY = minZ = Integer.MAX_VALUE;
        maxX = maxY = maxZ = 0;
        checkpointTicks = new LinkedHashSet<>();
        cellTypeCounts = new HashMap<>();
        proceedMethods = new EnumMap<>(SimulationEvents.class);
//        lqs = new HashMap<>();
        initializeProceedMethods();
    }
    //endregion

    //region Getters and Setters
    public UUID getIndividualId() {
        return individualId;
    }

    public String getActionsHash() {
        return actionsHash;
    }

    public long getSignificantTick() {
        return significantTick;
    }

    public long getSignificantTickCount() {
        return significantTickCount;
    }

    public int getCellCount() {
        return cellCount;
    }

    public int getMaxCellCount() {
        return maxCellCount;
    }

    public int getSynapseCount() {
        return synapseCount;
    }

    public int getMaxSynapseCount() {
        return maxSynapseCount;
    }

//    public double getLqValue(TeacherMode mode) {
//        Median median = new Median();
//        final double[] lqArr = lqs.get(mode);
//        if (lqArr == null)
//            return -1d;
//        return median.evaluate(lqArr);
//    }
//
//    public void setLqs(Map<TeacherMode, double[]> lqs) {
//        this.lqs = lqs;
//    }

    public Set<Long> getCheckpointTicks() {
        return checkpointTicks;
    }

    public Map<String, Integer> getCellTypeCounts() {
        return cellTypeCounts;
    }

    public int[] getMinCoordinate() {
        return new int[] {minX, minY, minZ};
    }

    public int[] getMaxCoordinate() {
        return new int[] {maxX, maxY, maxZ};
    }
    //endregion

    //region Public Methods
    public void proceedEvent(ISimulationEvent event) {
        proceedMethods.get(event.getEventMethod()).execute(event);
    }

    public Set<ISimulationEvent> getStatisticFormEvents() {
        Set<ISimulationEvent> result = new LinkedHashSet<>();
        List<Object> cellTypes = new ArrayList<>();
//        List<Object> ntTypes = new ArrayList<>();
//        List<Object> factorTypes = new ArrayList<>();
        List<Object> ligandTypes = new ArrayList<>();

        result.add(formStatisticRule(StatisticType.ModelId, "\"" + individualId.toString() + "\""));
        result.add(formStatisticRule(StatisticType.MinTick, 0));
//        result.add(formStatisticRule(StatisticType.Capacity, CoordinateUtils.INSTANCE.getCapacity()));
        result.add(formStatisticRule(StatisticType.MinCoordinate, "\"" + getMinCoordinate() + "\""));
        result.add(formStatisticRule(StatisticType.MaxCoordinate, "\"" + getMaxCoordinate().toString() + "\""));
        result.add(formStatisticRule(StatisticType.MaxTick, significantTick));
        result.add(formStatisticRule(StatisticType.SignificantTicksCount, significantTickCount));
        result.add(formStatisticRule(StatisticType.FinalCellCount, maxCellCount));

//        ModelLoader.getCellTypes().keySet().stream()
//                .forEach(item -> cellTypes.add("\"" + item + "\""));
//        ModelLoader.getNeurotransmitters().stream()
//                .forEach(item -> ntTypes.add("\"" + item + "\""));
//        ModelLoader.getAllFactors().stream()
//                .forEach(item -> factorTypes.add("\"" + item + "\""));

//        result.add(statisticArrayFormEvent(StatisticType.CellType, cellTypes));
//        result.add(statisticArrayFormEvent(StatisticType.NtType, ntTypes));
//        result.add(statisticArrayFormEvent(StatisticType.FactorType, factorTypes));

        return result;
    }
    //endregion

    //region Package-local Methods
    //endregion

    //region Protected Methods
    //endregion

    //region Private Methods
    private void initializeProceedMethods() {
        proceedMethods.put(SimulationEvents.TickEvent, this::proceedTickEvent);
        proceedMethods.put(SimulationEvents.ObjectAddEvent, this::proceedBaseObjectAddEvent);
        proceedMethods.put(SimulationEvents.ObjectDeleteEvent, this::proceedObjectDeleteEvent);
        proceedMethods.put(SimulationEvents.ObjectDifferentiatedEvent, this::proceedObjectDifferentiatedEvent);
        proceedMethods.put(SimulationEvents.ObjectMoveEvent, this::proceedObjectMoveEvent);
//        proceedMethods.put(SimulationEvents.InitialAxonGrowEvent, this::proceedBaseAxonGrowEvent);
//        proceedMethods.put(SimulationEvents.AxonGrowEvent, this::proceedBaseAxonGrowEvent);
        proceedMethods.put(SimulationEvents.InitialSynapseAddEvent, this::proceedBaseSynapseAddedEvent);
        proceedMethods.put(SimulationEvents.SynapseAddEvent, this::proceedBaseSynapseAddedEvent);
        proceedMethods.put(SimulationEvents.SynapseDeleteEvent, this::proceedSynapseDeletedEvent);
        proceedMethods.put(SimulationEvents.SynapticPowerChangedEvent, this::proceedSynapticPowerChangedEvent);
//        proceedMethods.put(SimulationEvents.CheckpointNodeFormEvent, this::proceedCheckpointNodeFormEvent);
        proceedMethods.put(SimulationEvents.SpreadChemicalSignal, this::proceedSpreadChemicalSignalEvent);
        proceedMethods.put(SimulationEvents.GatherChemicalSignal, this::proceedGatherChemicalSignalEvent);
    }

    private void proceedGatherChemicalSignalEvent(ISimulationEvent event) {

    }

    private void proceedSpreadChemicalSignalEvent(ISimulationEvent event) {

    }

    private void proceedTickEvent(ISimulationEvent event) {
        significantTick = ((TickEvent) event).getTick();
        incrementSignificantTickCount();
    }

    private void proceedBaseObjectAddEvent(ISimulationEvent event) {
        BaseObjectAddEvent objectAddEvent = (BaseObjectAddEvent) event;
        addCellTypes(objectAddEvent.getType());
        checkCoordinateForMinMax(objectAddEvent.getCoordinate());
        incrementCells();
    }

    private void proceedObjectDeleteEvent(ISimulationEvent event) {
        ObjectDeleteEvent objectDeleteEvent = (ObjectDeleteEvent) event;
        deleteCellTypes(objectDeleteEvent.getType());
        decrementCells();
    }

    private void proceedObjectDifferentiatedEvent(ISimulationEvent event) {
        ObjectDifferentiatedEvent objectProliferateEvent = (ObjectDifferentiatedEvent) event;
        deleteCellTypes(objectProliferateEvent.getPreviousType());
        addCellTypes(objectProliferateEvent.getType());
    }

    private void proceedObjectMoveEvent(ISimulationEvent event) {
        ObjectMoveEvent objectMoveEvent = (ObjectMoveEvent) event;
        checkCoordinateForMinMax(objectMoveEvent.getCoordinate());
    }

    private void proceedBaseSynapseAddedEvent(ISimulationEvent event) {
        incrementSynapses();
    }

    private void proceedSynapseDeletedEvent(ISimulationEvent event) {
        decrementSynapses();
    }

    private void proceedSynapticPowerChangedEvent(ISimulationEvent event) {

    }

    private void incrementSignificantTickCount() {
        significantTickCount++;
    }

    private void incrementCells() {
        cellCount++;
        if (cellCount > maxCellCount)
            maxCellCount = cellCount;
    }

    private void decrementCells() {
        cellCount--;
    }

    private void incrementSynapses() {
        synapseCount++;
        if (synapseCount > maxSynapseCount)
            maxSynapseCount = synapseCount;
    }

    private void decrementSynapses() {
        synapseCount--;
    }

    private void checkCoordinateForMinMax(@NotNull int[] coordinate) {
        int x = coordinate[0];
        int y = coordinate[1];
        int z = coordinate[2];

        if (x < minX)
            minX = x;
        if (x > maxX)
            maxX = x;
        if (y < minY)
            minY = y;
        if (y > maxY)
            maxY = y;
        if (z < minZ)
            minZ = z;
        if (z > maxZ)
            maxZ = z;
    }

    private void addCellTypes(String cellType) {
        if (cellTypeCounts.containsKey(cellType))
            cellTypeCounts.put(cellType, cellTypeCounts.get(cellType) + 1);
        else
            cellTypeCounts.put(cellType, 1);
    }

    private void deleteCellTypes(String cellType) {
        cellTypeCounts.put(cellType, cellTypeCounts.get(cellType) - 1);
    }

    @NotNull
    @Contract("_, _ -> !null")
    private StatisticSingleFormEvent formStatisticRule(StatisticType sType, Object value) {
        return new StatisticSingleFormEvent<>(sType, value);
    }

    @NotNull
    @Contract("_, _ -> !null")
    private StatisticArrayFormEvent statisticArrayFormEvent(StatisticType sType, List<Object> value) {
        return new StatisticArrayFormEvent<>(sType, value);
    }
    //endregion

    @FunctionalInterface
    private interface IProceedEvent {
        void execute(ISimulationEvent event);
    }
}
