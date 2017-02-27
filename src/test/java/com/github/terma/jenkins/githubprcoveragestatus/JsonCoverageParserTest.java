package com.github.terma.jenkins.githubprcoveragestatus;

import junit.framework.TestCase;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.startsWith;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.number.IsCloseTo.closeTo;


/**
 * Created by stevegal on 24/02/2017.
 */
public class JsonCoverageParserTest {

    @Test
    public void extractsCoverageFromSimpleCovReport(){
        String filePath = JsonCoverageParserTest.class.getResource(
                "/com/github/terma/jenkins/githubprcoveragestatus/SimpleCovParserTest/coverage.json").getFile();

        float coverage = new JsonCoverageParser("$.metrics.covered_percent").get(filePath);

        // won't be an exact match as we're converting double to float
        assertThat((double)coverage,is(closeTo(85.7142857142857,0.00001)));

    }

    @Test
    public void errorsReadingNoExistantFile(){
        try {
            float coverage = new JsonCoverageParser("$.metrics.covered_percent").get("wibble/wobble.not_here");
            TestCase.fail("should have thrown exception");
        } catch (IllegalArgumentException e) {
            assertThat(e.getMessage(),is(equalTo("Can't read SimpleCov report by path: wibble/wobble.not_here")));
        }
    }

    @Test
    public void notPresentPercentButValidJson() {
        String filePath = JsonCoverageParserTest.class.getResource(
                "/com/github/terma/jenkins/githubprcoveragestatus/SimpleCovParserTest/coverage_no_covered_percent.json").getFile();

        try {
            float coverage = new JsonCoverageParser("$.metrics.covered_percent").get(filePath);
            TestCase.fail("should not reach here");
        } catch (IllegalArgumentException e) {
            assertThat(e.getMessage(),startsWith("Strange SimpleCov report!\nCan't extract float value by JsonPath:"));
        }
    }

    @Test
    public void inValidJson() {
        String filePath = JsonCoverageParserTest.class.getResource(
                "/com/github/terma/jenkins/githubprcoveragestatus/SimpleCovParserTest/coverage_invalid.json").getFile();

        try {
            float coverage = new JsonCoverageParser("$.metrics.covered_percent").get(filePath);
            TestCase.fail("should not reach here");
        } catch (IllegalArgumentException e) {
            assertThat(e.getMessage(),startsWith("Strange SimpleCov report!\nCan't extract float value by JsonPath:"));
        }
    }


    @Test
    public void parsesChefCoverageJson() {
        String filePath = JsonCoverageParserTest.class.getResource(
                "/com/github/terma/jenkins/githubprcoveragestatus/SimpleCovParserTest/chefCoverage.json").getFile();

        float coverage = new JsonCoverageParser("$.coverage").get(filePath);

        // won't be an exact match as we're converting double to float
        assertThat((double)coverage,is(closeTo(50.000,0.00001)));
    }

}