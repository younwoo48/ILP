package uk.ac.ed.inf;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class CentralArea {

    private static final String defaultEndpoint = "https://ilp-rest.azurewebsites.net";
    private static CentralArea instance;
    private URL baseUrl;

    private CentralArea() {

    }

    public static CentralArea getInstance(String baseUrlEndpoint){
        if (instance == null){
            instance = new CentralArea();
        }
        try{
            instance.baseUrl = new URL(baseUrlEndpoint);
        } catch (MalformedURLException ignored){
            try{
                instance.baseUrl = new URL(baseUrlEndpoint);
            } catch (MalformedURLException e){
                e.printStackTrace();
                System.exit(2);
            }
        }
        return instance;
    }
    public Area getArea(){
        Point[] points = new IlpRestClient(baseUrl).deserialize("/centralArea", Point[].class);
        Area area = new Area(points);
        return area;
    }

}
