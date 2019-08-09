/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test.uts.aai.pbmn;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.drools.KnowledgeBase;
import org.drools.builder.KnowledgeBuilder;
import org.drools.builder.KnowledgeBuilderFactory;
import org.drools.builder.ResourceType;
import org.drools.io.ResourceFactory;
import org.drools.runtime.StatefulKnowledgeSession;
import uts.aai.feature.configuration.MLComponentConfiguration;
import uts.aai.pn.utils.IOUtils;
import org.apache.commons.io.FileUtils;

/**
 *
 * @author ntdun
 */
public class AutoProcessTestApp {

    public static final void main(String[] args) {

        String folderName = "C:\\DATA\\Projects\\eclipse-workspace\\aai_aw\\weka-3-7-7\\combination\\dataset_1";
        AutoProcessApp apa = new AutoProcessApp();
        apa.readAllFiles(folderName);
    }
}
