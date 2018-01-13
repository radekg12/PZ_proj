import com.fasterxml.jackson.databind.ObjectMapper;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class WebClient {
    private String url;

    private String getJson(String base) throws IOException {
        loadProperties();
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(url + base)
                .build();

        return client.newCall(request).execute().body().string();
    }

    public WebApiResponse getResponse(String base) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(getJson(base), WebApiResponse.class);
    }

    private void loadProperties() {
        Properties properties = new Properties();
        InputStream input = null;

        try {
            input = Pionek.class.getResource("config.properties").openStream();
            properties.load(input);

            url = properties.getProperty("APIurl");
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
