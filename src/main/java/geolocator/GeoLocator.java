package geolocator;

import java.net.URL;

import java.io.IOException;

import com.google.gson.Gson;

import com.google.common.net.UrlEscapers;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Class for obtaining geolocation information about an IP address or host
 * name. The class uses the <a href="http://ip-api.com/">IP-API.com</a>
 * service.
 */
public class GeoLocator {

    /**
     * URI of the geolocation service.
     */
    public static final String GEOLOCATOR_SERVICE_URI = "http://ip-api.com/json/";

    private static Gson GSON = new Gson();

    private static final Logger logger = LoggerFactory.getLogger(GeoLocator.class);

    /**
     * Creates a <code>GeoLocator</code> object.
     */
    public GeoLocator() {}

    /**
     * Returns geolocation information about the JVM running the application.
     *
     * @return an object wrapping the geolocation information returned
     * @throws IOException if any I/O error occurs
     */
    public GeoLocation getGeoLocation() throws IOException {
        logger.info("getGeoLocation called with no argument.");
        return getGeoLocation(null);
    }

    /**
     * Returns geolocation information about the IP address or host name
     * specified. If the argument is <code>null</code>, the method returns
     * geolocation information about the JVM running the application.
     *
     * @param ipAddrOrHost the IP address or host name, may be {@code null}
     * @return an object wrapping the geolocation information returned
     * @throws IOException if any I/O error occurs
     */
    public GeoLocation getGeoLocation(String ipAddrOrHost) throws IOException {
        logger.info("getGeoLocation called with a string ipAddrOrHost");
        URL url;
        if (ipAddrOrHost != null) {
            logger.debug("ipAddrOrHost is not null");
            ipAddrOrHost = UrlEscapers.urlPathSegmentEscaper().escape(ipAddrOrHost);
            url = new URL(GEOLOCATOR_SERVICE_URI + ipAddrOrHost);
        } else {
            logger.debug("ipAddrOrHost is null");
            url = new URL(GEOLOCATOR_SERVICE_URI);
        }
        logger.info("Retrieving data from a URL");
        logger.debug("URL: {}", url);
        String s = IOUtils.toString(url, "UTF-8");
        logger.debug("Data: {}", s);
        return GSON.fromJson(s, GeoLocation.class);
    }

    // CHECKSTYLE:OFF: MissingJavadocMethod
    public static void main(String[] args) throws IOException {
        logger.info("main start");
        try {
            String arg = args.length > 0 ? args[0] : null;
            logger.debug("Input arg: {}", arg);
            System.out.println(new GeoLocator().getGeoLocation(arg));
        } catch (IOException e) {
            logger.error("IOException");
            System.err.println(e.getMessage());
        }
        logger.info("main end");
    }
    // CHECKSTYLE:ON
}
