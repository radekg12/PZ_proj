package pl.edu.wat.wcy.pz.exchangerate;

import com.fasterxml.jackson.annotation.JsonProperty;

public class WebApiResponse {

    @JsonProperty
    private String base;
    @JsonProperty
    private String date;
    @JsonProperty
    private Rates rates;

    public Rates getRates() {
        return rates;
    }

    public String getBase() {
        return base;
    }

    public String getDate() {
        return date;
    }
}
