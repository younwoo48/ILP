package uk.ac.ed.inf;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
public class TestClient {
    public static void main(String[] args) {
        if (args.length != 2) {
            System.err.println(" Test client Base−URL Echo−Parameter");
            System.err.println("you must supply the base address of the ILP REST Service \n" + "e.g.http://restservice.somewhere and a string to be echoed");
            System.exit(1);
        }
        try {
            String baseUrl = args[0];
            String echoBasis = args[1];
            if (!baseUrl.endsWith("/")) {
                baseUrl += "/";
            }
            // we c a l l the t e s t endpoint and pass in some t e s t data which w i l l be echoed
            URL url = new URL(baseUrl + "test/" + echoBasis);
            TestResponse response = new ObjectMapper().readValue(new URL(baseUrl + "test/" + echoBasis), TestResponse.class);

            if (!response.greeting.endsWith(echoBasis)) {
                throw new RuntimeException("wrong echo returned");
            }
            System.out.println("The server responded as JSON−greeting: \n\n" + response.greeting);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}