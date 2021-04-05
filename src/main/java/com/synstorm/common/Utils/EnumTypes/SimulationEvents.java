package com.synstorm.common.Utils.EnumTypes;

import com.synstorm.common.Utils.Annotations.Classes.Model_v1;

/**
 * Created by dvbozhko on 20/02/2017.
 */

@Model_v1

public enum SimulationEvents {
    TickEvent,
    ObjectAddEvent,
    ObjectDeleteEvent,
    ObjectDifferentiatedEvent,
    ObjectMoveEvent,
    InitialSynapseAddEvent,
    SynapseAddEvent,
    SynapseDeleteEvent,
    SynapticPowerChangedEvent,
    CheckpointNodeFormEvent,
    LQCalculatedEvent,
    StatisticTypeExportEvent,
    StatisticSingleFormEvent,
    StatisticArrayFormEvent,
    StatisticMapFormEvent,
    AnswerGetEvent,
    CellTypeExportEvent,
    ScoreTableFormRowEvent,
    LearningScoreTableFormRowEvent,
    ActionToGenesMapEvent,
    SpreadChemicalSignal,
    GatherChemicalSignal
}
