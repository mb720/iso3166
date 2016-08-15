import com.google.common.base.Charsets;
import com.google.common.base.Splitter;
import com.google.common.io.Resources;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URL;
import java.util.*;

import static java.util.stream.Collectors.toList;


/**
 * Provides standardized country codes according to ISO 3166 Alpha of the International Organization for Standardization (ISO).
 * <p>
 * Created by Matthias Braun on 13/08/16.
 */
public final class Iso3166Alpha2 {
    private static final Logger LOG = LoggerFactory.getLogger(Iso3166Alpha2.class);

    private Iso3166Alpha2() {
    }

    /**
     * @param countryFile the {@link URL} to the file containing the ISO country codes and their associated
     *                    country names in German.
     *                    We expect the codes and the country names to be separated by equal signs.
     *                    We also expect that the individual country names are separated by semicolons.
     * @return a {@link Map} from ISO 3166 alpha 2 country codes to their respective country names in German
     * @see <a href="https://de.wikipedia.org/wiki/ISO-3166-1-Kodierliste">ISO 3166</a>
     */
    public static Map<String, List<String>> getCodesAndCountries(URL countryFile) {
        Map<String, List<String>> isoCodesAndCountries = new HashMap<>();
        try {

            List<String> lines = Resources.readLines(countryFile, Charsets.UTF_8);
            lines.forEach(line -> {
                String separator = "=";
                int splitAt = line.indexOf(separator);
                if (splitAt != -1) {
                    String isoCode = line.substring(0, splitAt).trim();
                    String countries = line.substring(splitAt + separator.length(), line.length()).trim();
                    List<String> countryList = Splitter.on(";").splitToList(countries)
                            .stream()
                            .map(String::trim)
                            .collect(toList());
                    isoCodesAndCountries.put(isoCode, countryList);
                }
            });

        } catch (IOException e) {
            LOG.warn("Could not read {}", countryFile, e);
        }
        return isoCodesAndCountries;
    }

    public static Optional<String> countryNameToCode(String countryName, URL fileWithCodesAndCountries) {
        return getCodesAndCountries(fileWithCodesAndCountries).entrySet().stream()
                .filter(entry -> entry.getValue().contains(countryName))
                .findFirst()
                .map(Map.Entry::getKey);
    }

    public static List<String> countryCodeToName(String countryCode, URL fileWithCodesAndCountries) {
        List<String> countryNames = getCodesAndCountries(fileWithCodesAndCountries)
                .get(countryCode);

        return countryNames == null ?
                Collections.emptyList() :
                countryNames;
    }
}
