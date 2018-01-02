import com.fasterxml.jackson.databind.ObjectMapper;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;

import java.io.IOException;

public class WebClient {

    private String getJson(String base) throws IOException {
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url("https://api.fixer.io/latest?base=" + base)
                .build();

        return client.newCall(request).execute().body().string();
    }

    public WebApiResponse getResponse(String base) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(getJson(base), WebApiResponse.class);
    }

}
