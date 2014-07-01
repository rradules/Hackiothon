package weatherdata.POJO;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by christophe on 30.06.14.
 */
public class SensorStatus {
    @SerializedName("Value")
    @Expose
    private Double Value;
    @SerializedName("Date")
    @Expose
    private String Date;
    /**********************************************************************************************/
    /***************** GETTERS AND SETTERS*********************************************************/
    /**********************************************************************************************/
    public Double getValue() {
        return Value;
    }

    public void setValue(Double value) {
        this.Value = value;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        this.Date = date;
    }
    /**********************************************************************************************/
    /***************** LOGIC **********************************************************************/
    /**********************************************************************************************/
    public Date getDateObject()
    {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSSS'Z'");
        Date date;
        try {
            date = df.parse(this.Date);
            return date;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

}

