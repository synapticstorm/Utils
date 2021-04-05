package com.synstorm.common.Utils.ProbabilitiesEvaluation.Real;

import com.synstorm.common.Utils.Annotations.Classes.Model_v1;
import org.apache.commons.math3.distribution.UniformRealDistribution;
import org.jetbrains.annotations.NotNull;

@Model_v1
public class UniformReal extends RealDistribution {
    //region Fields
    //endregion

    //region Constructors
    public UniformReal(@NotNull double[] args) {
        distribution = new UniformRealDistribution(args[0], args[1]);
    }
    //endregion

    //region Getters and Setters
    //endregion

    //region Public Methods
    @Override
    public double getProbabilityDensity(double[] args) {
        return 0;
    }

    @Override
    public double getCumulativeProbability(double[] args) {
        return 0;
    }

    @Override
    public double getEventProbability(double[] args) {
        return 0;
    }
    //endregion

    //region Package-local Methods
    //endregion

    //region Protected Methods
    //endregion

    //region Private Methods
    //endregion
}
