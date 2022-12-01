package uk.ac.ed.inf;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;



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


}
