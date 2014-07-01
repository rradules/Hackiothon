package weatherdata.POJO;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by christophe on 30.06.14.
 */
public class SensorResponse {
    @SerializedName("Values")
    @Expose
    private List<SensorStatus> values = new ArrayList<SensorStatus>();
    @SerializedName("ResponseStatus")
    @Expose
    private ResponseStatus responseStatus;

    public List<SensorStatus> getValues() {
        // Sort the values.


        return values;
    }

    public void setValues(List<SensorStatus> values) {
        this.values = values;
    }

    public ResponseStatus getResponseStatus() {
        return responseStatus;
    }

    public void setResponseStatus(ResponseStatus responseStatus) {
        this.responseStatus = responseStatus;
    }


}
