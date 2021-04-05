package com.synstorm.common.Utils.ConfigurationStrings;

import com.synstorm.common.Utils.Annotations.Classes.Model_v0;
import com.synstorm.common.Utils.Annotations.Classes.ProductionLegacy;

import java.io.File;

/**
 * Created by human-research on 16/07/15.
 */

@Model_v0
@ProductionLegacy
public class FilePathsStrings {
    /**
     * Filenames: FILES
     */
    public static final String FILENAME_MODEL_CONFIG = File.separator + "Model.xml";
    public static final String FILENAME_INDIVIDUAL_CONFIG = File.separator + "Individual.xml";

    /**
     * Filenames: VALIDATION
     */
    public static final String FILENAME_MODEL_XSL = File.separator + "Model.xsl";
    public static final String FILENAME_INDIVIDUAL_XSL = File.separator + "Individual.xsl";

    /**
     * Filenames: XSD
     */
    public static final String FILENAME_MODEL_SCHEMA = File.separator + "Model.xsd";
    public static final String FILENAME_INDIVIDUAL_SCHEMA = File.separator + "Individual.xsd";
}
