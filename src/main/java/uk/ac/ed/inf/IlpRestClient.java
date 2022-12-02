package uk.ac.ed.inf;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.geojson.Feature;
import org.geojson.FeatureCollection;
import org.geojson.LineString;
import org.geojson.LngLatAlt;
import org.geojson.jackson.LngLatAltSerializer;

import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;


public class IlpRestClient
{
    public URL baseUrl;
    public IlpRestClient(URL baseUrl) {
        this.baseUrl = baseUrl;
    }
    public URL getBaseUrl() {return baseUrl;}


    public void download(String fromEndpoint, String toFilename){
        URL finalURL = null;

        try{
            finalURL=new URL(baseUrl.toString() + fromEndpoint);
        } catch (MalformedURLException e) {
            System.err.println("URL in invalid : " + baseUrl + fromEndpoint);
            System.exit(2);
        }
        try (BufferedInputStream in = new BufferedInputStream(finalURL.openStream());
             FileOutputStream fileOutputStream =
                     new FileOutputStream(fromEndpoint, false)){
            var dataBuffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = in.read(dataBuffer,0,1024)) != -1){
                fileOutputStream.write(dataBuffer,0,bytesRead);
            }

            System.out.println("File was written at: " + toFilename);
        } catch(IOException e){
            System.err.format("Error loading file: %s from %s -> %s", fromEndpoint, finalURL,e);
        }
    }
    public <T> T deserialize(String fromEndpoint, Class<T> klass){
        URL finalURL = null;
        T response = null;

        try{
            finalURL = new URL(baseUrl.toString() + fromEndpoint);
        } catch (MalformedURLException e){
            System.err.println("URL is invalid : "+baseUrl+fromEndpoint);
            System.exit(2);
        }
        try{
            response = new ObjectMapper().readValue(finalURL, klass);
        } catch (Exception e){
            e.printStackTrace();
        }
        return response;
    }

    public void createFlightPath(ArrayList<LngLat> positions) throws JsonProcessingException {
        FeatureCollection featureCollection = new FeatureCollection();
        ArrayList<LngLatAlt> geo_position = new ArrayList<LngLatAlt>();
        for (LngLat position: positions){
            featureCollection.add(new LineString())
            geo_position.add(new LngLatAlt(position.lng, position.lat));
        }

        LineString lines = new LineString();
        lines.fromLngLats();
        featureCollection.add();

        String json= new ObjectMapper().writeValueAsString(featureCollection);
    }


}
