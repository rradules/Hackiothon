package hackathon.greenhouse.predictions.POJO;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
public class PredictionResult {

    @JsonProperty("total_B_cost")
    private Double total_B_cost;
    @JsonProperty("total_T_efficiency")
    private Double total_T_efficiency;
    @JsonProperty("temp_cost")
    private List<Double> temp_cost = new ArrayList<Double>();
    @JsonProperty("t_predicted")
    private List<Double> t_predicted = new ArrayList<Double>();
    @JsonProperty("b_efficiency")
    private List<Double> b_efficiency = new ArrayList<Double>();
    @JsonProperty("t")
    private List<Double> t = new ArrayList<Double>();
    @JsonProperty("total_efficiency")
    private Double total_efficiency;
    @JsonProperty("temperature")
    private List<Double> temperature = new ArrayList<Double>();
    @JsonProperty("b_predicted")
    private List<Double> b_predicted = new ArrayList<Double>();
    @JsonProperty("t_efficiency")
    private List<Double> t_efficiency = new ArrayList<Double>();
    @JsonProperty("total_T_cost")
    private Double total_T_cost;
    @JsonProperty("total_cost")
    private Double total_cost;
    @JsonProperty("b_cost")
    private List<Double> b_cost = new ArrayList<Double>();
    @JsonProperty("total_B_efficiency")
    private Double total_B_efficiency;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("total_B_cost")
    public Double getTotal_B_cost() {
        return total_B_cost;
    }

    @JsonProperty("total_B_cost")
    public void setTotal_B_cost(Double total_B_cost) {
        this.total_B_cost = total_B_cost;
    }

    @JsonProperty("total_T_efficiency")
    public Double getTotal_T_efficiency() {
        return total_T_efficiency;
    }

    @JsonProperty("total_T_efficiency")
    public void setTotal_T_efficiency(Double total_T_efficiency) {
        this.total_T_efficiency = total_T_efficiency;
    }

    @JsonProperty("T_cost")
    public List<Double> getTemp_cost() {
        return temp_cost;
    }

    @JsonProperty("T_cost")
    public void setTemp_cost(List<Double> temp_cost) {
        this.temp_cost = temp_cost;
    }

    @JsonProperty("T_predicted")
    public List<Double> getT_predicted() {
        return t_predicted;
    }

    @JsonProperty("T_predicted")
    public void setT_predicted(List<Double> t_predicted) {
        this.t_predicted = t_predicted;
    }

    @JsonProperty("B_efficiency")
    public List<Double> getB_efficiency() {
        return b_efficiency;
    }

    @JsonProperty("B_efficiency")
    public void setB_efficiency(List<Double> b_efficiency) {
        this.b_efficiency = b_efficiency;
    }

    @JsonProperty("t")
    public List<Double> getT() {
        return t;
    }

    @JsonProperty("t")
    public void setT(List<Double> t) {
        this.t = t;
    }

    @JsonProperty("total_efficiency")
    public Double getTotal_efficiency() {
        return total_efficiency;
    }

    @JsonProperty("total_efficiency")
    public void setTotal_efficiency(Double total_efficiency) {
        this.total_efficiency = total_efficiency;
    }

    @JsonProperty("temperature")
    public List<Double> getTemperature() {
        return temperature;
    }

    @JsonProperty("temperature")
    public void setTemperature(List<Double> temperature) {
        this.temperature = temperature;
    }

    @JsonProperty("B_predicted")
    public List<Double> getB_predicted() {
        return b_predicted;
    }

    @JsonProperty("B_predicted")
    public void setB_predicted(List<Double> b_predicted) {
        this.b_predicted = b_predicted;
    }

    @JsonProperty("T_efficiency")
    public List<Double> getT_efficiency() {
        return t_efficiency;
    }

    @JsonProperty("T_efficiency")
    public void setT_efficiency(List<Double> t_efficiency) {
        this.t_efficiency = t_efficiency;
    }

    @JsonProperty("total_T_cost")
    public Double getTotal_T_cost() {
        return total_T_cost;
    }

    @JsonProperty("total_T_cost")
    public void setTotal_T_cost(Double total_T_cost) {
        this.total_T_cost = total_T_cost;
    }

    @JsonProperty("total_cost")
    public Double getTotal_cost() {
        return total_cost;
    }

    @JsonProperty("total_cost")
    public void setTotal_cost(Double total_cost) {
        this.total_cost = total_cost;
    }

    @JsonProperty("B_cost")
    public List<Double> getB_cost() {
        return b_cost;
    }

    @JsonProperty("B_cost")
    public void setB_cost(List<Double> b_cost) {
        this.b_cost = b_cost;
    }

    @JsonProperty("total_B_efficiency")
    public Double getTotal_B_efficiency() {
        return total_B_efficiency;
    }

    @JsonProperty("total_B_efficiency")
    public void setTotal_B_efficiency(Double total_B_efficiency) {
        this.total_B_efficiency = total_B_efficiency;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}