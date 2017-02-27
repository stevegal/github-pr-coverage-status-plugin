package com.github.terma.jenkins.githubprcoveragestatus;

import com.github.terma.jenkins.githubcoverageupdater.JsonUtils;
import com.jayway.jsonpath.JsonPathException;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;

/**
 * Created by stevegal on 24/02/2017.
 */
public class JsonCoverageParser implements CoverageReportParser {

    private final String jsonPath;

    public JsonCoverageParser(String jsonPath) {
        this.jsonPath=jsonPath;
    }


    @Override
    public float get(String simpleCovFilePath) {
        final String content;
        try {
            content = FileUtils.readFileToString(new File(simpleCovFilePath));
        } catch (IOException e) {
            throw new IllegalArgumentException(
                    "Can't read SimpleCov report by path: " + simpleCovFilePath);
        }

        Double covered = extractValueFromPath(content);
        return covered.floatValue();
    }

    private Double extractValueFromPath(String content) {
        try {
            Double covered = JsonUtils.findInJson(content, this.jsonPath);
            return covered;
        } catch (JsonPathException error) {
            throw new IllegalArgumentException( "Strange SimpleCov report!\n" +
                    "Can't extract float value by JsonPath: " + this.jsonPath + "\n" +
                    "from:\n" + content);
        }
    }
}
