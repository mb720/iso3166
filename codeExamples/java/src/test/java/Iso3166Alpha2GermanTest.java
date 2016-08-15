import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.github.npathai.hamcrestopt.OptionalMatchers.hasValue;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

/**
 * Tests {@link Iso3166Alpha2} for German country names.
 * <p>
 * Created by Matthias Braun on 15/08/16.
 */
public final class Iso3166Alpha2GermanTest {

    private static final Logger LOG = LoggerFactory.getLogger(Iso3166Alpha2GermanTest.class);
    private static final String ISO_3166_ALPHA2_GERMAN_FILE = "iso_3166_alpha2_german.properties";
    private static final URL URL_TO_FILE = getUrl(ISO_3166_ALPHA2_GERMAN_FILE);

    private static URL getUrl(String fileName) {
        String urlAsString = "https://raw.githubusercontent.com/mb720/iso3166/master/alpha2/" + fileName;
        URL url = null;
        try {
            url = new URL(urlAsString);
        } catch (MalformedURLException e) {
            LOG.error("URL to German Alpha2 file is malformed: '{}'", urlAsString, e);
        }
        return url;
    }

    @Test
    public void thereAreCodesAndCountries() {
        Map<String, List<String>> codesAndCountries = Iso3166Alpha2.getCodesAndCountries(URL_TO_FILE);
        assertThat(codesAndCountries, is(not(anEmptyMap())));
    }

    @Test
    public void countryCodeDEstandsForGermany() {
        String countryCode = "DE";
        List<String> countryNames = Iso3166Alpha2.countryCodeToName(countryCode, URL_TO_FILE);

        assertThat(countryNames, both(hasSize(1)).and(contains("Deutschland")));
    }

    @Test
    public void countryCodeMDstandsForMultipleCountries() {
        String countryCode = "MD";
        List<String> countryNames = Iso3166Alpha2.countryCodeToName(countryCode, URL_TO_FILE);

        assertThat(countryNames, containsInAnyOrder("Moldawien", "Republik Moldau"));
    }

    @Test
    public void austriasCountryCodeIsAT() {
        String countryNameInGerman = "Österreich";
        Optional<String> countryCode = Iso3166Alpha2.countryNameToCode(countryNameInGerman, URL_TO_FILE);
        String expectedCountryCode = "AT";
        String msg = "Austria's name in German '" + countryNameInGerman + "' should be associated with the country code '"
                + expectedCountryCode + "'";

        assertThat(msg, countryCode, hasValue(expectedCountryCode));
    }
}