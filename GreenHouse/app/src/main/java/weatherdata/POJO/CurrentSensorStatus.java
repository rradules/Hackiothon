package weatherdata.POJO;

/**
 * Created by christophe on 01.07.14.
 */
public class CurrentSensorStatus {
    private SensorStatus co2Value;
    private SensorStatus temperatureValue;
    private SensorStatus humidityValue;

    public void setCo2Value(SensorStatus co2Value) {
        this.co2Value = co2Value;
    }

    public SensorStatus getCo2Value() {
        return co2Value;
    }

    public void setTemperatureValue(SensorStatus temperatureValue) {
        this.temperatureValue = temperatureValue;
    }

    public SensorStatus getTemperatureValue() {
        return temperatureValue;
    }

    public void setHumidityValue(SensorStatus humidityValue) {
        this.humidityValue = humidityValue;
    }

    public SensorStatus getHumidityValue() {
        return humidityValue;
    }
}
