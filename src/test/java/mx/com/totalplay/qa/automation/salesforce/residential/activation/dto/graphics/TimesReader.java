package mx.com.totalplay.qa.automation.salesforce.residential.activation.dto.graphics;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Properties;
import java.util.stream.IntStream;
import mx.com.totalplay.qa.automation.salesforce.residential.activation.dto.jsonCucumberReport.ElementDTO;
import mx.com.totalplay.qa.automation.salesforce.residential.activation.dto.jsonCucumberReport.ReportJsonDTO;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtils;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.chart.title.TextTitle;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class reads the json file reports of load tests and generates a graphic time in JPEG format
 *
 * @version 1.0
 * @author Alejandro Uribe
 */
public class TimesReader {

  private ArrayList<Long> json1 = new ArrayList<>();
  private ArrayList<Long> json10 = new ArrayList<>();
  private ArrayList<Long> json50 = new ArrayList<>();
  private ArrayList<Long> json100 = new ArrayList<>();
  Properties path = new Properties();

  private String pathFile;
  private final Logger logger = LoggerFactory.getLogger(TimesReader.class);

  public TimesReader(String pathFile) throws IOException {

    this.pathFile = pathFile;
    path.load(TimesReader.class.getClassLoader().getResourceAsStream("path.properties"));
  }

  /**
   * This method calls functions to plot time steps
   */
  public void graphic(String serviceTitle) {
    getTimeCucumberSteps();
    graphicLoad(serviceTitle);
  }

  /**
   * This method calls methods which insert steps times into graphic lines
   */
  public void getTimeCucumberSteps() {
    json1 = readJson("1");
    json10 = readJson("10");
    json50 = readJson("50");
    json100 = readJson("100");
  }

  /**
   * This method reads the json report files of the tests
   *
   * @param file represents file path
   * @return Long ArrayList object
   */
  public ArrayList<Long> readJson(String file) {
    JSONParser jsonParser = new JSONParser();
    ObjectMapper mapper = new ObjectMapper();
    long totalTime;
    ArrayList<Long> timeStepList = new ArrayList<>();
    try {
      String filename = String.format(path.getProperty("reportJson"), file);
      FileReader reader = new FileReader(filename);
      Object obj = jsonParser.parse(reader);
      JSONArray testResults = (JSONArray) obj;

      String result = testResults.toJSONString();
      mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
      ReportJsonDTO[] report = mapper.readValue(result, ReportJsonDTO[].class);
      ElementDTO[] elements = report[0].getElements();

      for (ElementDTO element : elements) {
        /*totalTime = Arrays.stream(element.getSteps())
            .mapToLong(step -> step.getResult().getDuration()).sum();*/
        totalTime = element.getSteps()[1].getResult().getDuration();
        if (totalTime != 0) {
          timeStepList.add((long) (totalTime * 0.000001));
        }
      }
    } catch (Exception e) {
      logger.error("An error has occurred reading json files of cucumber reports", e);
    }
    return timeStepList;
  }

  /**
   * This method defines the kind of line and colors of the graphics, inserts the times list of
   * every load test and generates a JPEG image with the time graphic
   */
  public void graphicLoad(String serviceTitle) {
    XYSeries unitary = generateSequence("Unit request", json1);
    XYSeries Load10 = generateSequence("10 requests", json10);
    XYSeries Load50 = generateSequence("50 requests", json50);
    XYSeries Load100 = generateSequence("100 requests", json100);

    XYSeriesCollection dataset = new XYSeriesCollection();
    dataset.addSeries(unitary);
    dataset.addSeries(Load10);
    dataset.addSeries(Load50);
    dataset.addSeries(Load100);

    JFreeChart chart = ChartFactory.createXYLineChart(
        "Load test steps execution time",
        "Requests",
        "Time in milli seconds",
        dataset,
        PlotOrientation.VERTICAL,
        true,
        true,
        false
    );

    XYItemRenderer render = chart.getXYPlot().getRenderer();

    render.setSeriesPaint(0, Color.RED);
    render.setSeriesStroke(0, new BasicStroke(3.0f));
    render.setSeriesPaint(1, Color.BLACK);
    render.setSeriesStroke(1, new BasicStroke(3.0f));
    render.setSeriesPaint(2, Color.ORANGE);
    render.setSeriesStroke(2, new BasicStroke(3.0f));
    render.setSeriesPaint(3, Color.BLUE);
    render.setSeriesStroke(3, new BasicStroke(3.0f));

    chart.getXYPlot().setBackgroundPaint(Color.white);
    chart.getXYPlot().setDomainGridlinePaint(Color.BLACK);
    chart.getXYPlot().setDomainMinorGridlinePaint(Color.BLACK);
    chart.getXYPlot().setRangeGridlinePaint(Color.BLACK);

    String title = String.format("Load testing report (%s)",serviceTitle);
    chart.setTitle(new TextTitle(title, new Font("Serif", Font.BOLD, 18)));

    try {
      String fileName = String.format(path.getProperty("reportJsonImage"), pathFile);
      ChartUtils.saveChartAsJPEG(new File(fileName), chart, 540, 350);
    } catch (Exception e) {
      logger.error("An error has occurred generating time graphics images of cucumber reports", e);
    }
  }

  /**
   * This method inserts steps times of cucumber json report into XYSeries objects to generate
   * graphics with jfree library
   *
   * @param name Represents name of the graphic line
   * @param load Represents list of time steps
   * @return XYSeries object which represents graphic line
   */
  public XYSeries generateSequence(String name, ArrayList<Long> load) {
    XYSeries sequence = new XYSeries(name);

    IntStream.range(0, load.size()).filter(i -> load.get(i) == 0).forEach(load::remove);
    if (load.size() == 1) {
      load.add(load.get(0)+1);
    }
    IntStream.range(0, load.size()).forEach(i -> sequence.add(i, load.get(i)));
    return sequence;
  }
}
