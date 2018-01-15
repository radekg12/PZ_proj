import com.fasterxml.jackson.databind.ObjectMapper;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public class WebClient {
    private static final Logger LOGGER = Logger.getLogger(WebClient.class.getSimpleName(), "LogsMessages");
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
        String propertiesName = "config.properties";
        try {
            input = getClass().getResource(propertiesName).openStream();
            properties.load(input);
            url = properties.getProperty("APIurl");
        } catch (IOException ex) {
            LOGGER.log(Level.WARNING, "properties.open", ex);
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    LOGGER.log(Level.WARNING, "properties.close", e);
                }
            }
        }
    }

}
