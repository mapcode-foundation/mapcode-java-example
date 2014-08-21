/**
 * Copyright (C) 2014 Stichting Mapcode Foundation
 * For terms of use refer to http://www.mapcode.com/downloads.html
 */

package com.mapcode.example;

import com.mapcode.Mapcode;
import com.mapcode.MapcodeInfo;
import com.mapcode.ParentTerritory;
import com.mapcode.Point;
import com.mapcode.Territory;
import com.mapcode.UnknownMapcodeException;
import com.mapcode.UnknownTerritoryException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * This class serves as an example of how to use the Mapcode Java library.
 */
public class Example {
    private static final Logger LOG = LoggerFactory.getLogger(Example.class);

    /**
     * Main method.
     *
     * @param args Arguments (unused).
     */
    public static void main(final String[] args) throws UnknownTerritoryException {
        exampleEncode();
        exampleDecode();
        exampleGetTerritoryFromISOCode();
        exampleDisambiguateTerritory();
    }

    private static void exampleDecode() throws UnknownTerritoryException {
        LOG.info("Example: Decoding Mapcodes to lat/lon points");
        LOG.info("--------");

        /**
         * This example shows you how to decode a Mapcode to a point. The first example decodes
         * a valid Mapcode to a point.
         */
        final Territory territory = Territory.fromString("NLD");
        final String mapcode1 = "49.4V";
        try {
            final Point p = Mapcode.decode(mapcode1, territory);
            LOG.info("Mapcode {} in territory {} represents latitude {}, longitude {}",
                mapcode1, territory, p.getLatDeg(), p.getLonDeg());
        }
        catch (final UnknownMapcodeException ignored) {
            LOG.info("This should never happen in this example as the Mapcode is valid.");
        }

        /**
         * This second example shows you how to decode a Mapcode to a point, which already includes a
         * territory name.
         */
        final String mapcode2 = "NLD 49.4V";
        try {
            final Point p = Mapcode.decode(mapcode2);
            LOG.info("Mapcode {} in territory {} represents latitude {}, longitude {}",
                mapcode1, territory, p.getLatDeg(), p.getLonDeg());
        }
        catch (final UnknownMapcodeException ignored) {
            LOG.info("This should never happen in this example as the Mapcode is valid.");
        }

        /**
         * The third example tries to decode an invalid Mapcode to a point which results
         * in an exception being thrown.
         */
        final String mapcode3 = "49.4A";
        try {
            final Point p = Mapcode.decode(mapcode3, territory);
            LOG.info("Mapcode {} in territory {} represents latitude {}, longitude {}",
                mapcode3, territory, p.getLatDeg(), p.getLonDeg());
        }
        catch (final UnknownMapcodeException ignored) {
            LOG.info("Mapcode {} in territory {} is invalid, as expected in this example", mapcode3, territory);
        }

        /**
         * The fourht example tries to decode a valid international Mapcode without a territory context.
         * This will succeed.
         */
        final String mapcode4 = "PQ0PF.5M1H";
        try {
            final Point p = Mapcode.decode(mapcode4);
            LOG.info("Mapcode {} represents latitude {}, longitude {}", mapcode4, p.getLatDeg(), p.getLonDeg());
        }
        catch (final UnknownMapcodeException ignored) {
            LOG.info("This should never happen in this example as the Mapcode is valid.");
        }
        LOG.info("");
    }

    private static void exampleEncode() {
        LOG.info("Example: Encoding lat/lon points to Mapcodes");
        LOG.info("--------");

        /**
         * This example shows you how to encode a lat/lon point into a Mapcode.
         */
        double lat = 52.376514;
        double lon = 4.908542;

        /**
         * First, we will try to get all possible Mapcodes, for all territories.
         */
        List<MapcodeInfo> results = Mapcode.encode(lat, lon);
        LOG.info("There are {} Mapcodes in total for latitude {}, longitude {} world-wide",
            results.size(), lat, lon);

        int count = 1;
        for (final MapcodeInfo result : results) {
            LOG.info("  #{}: {}", count, result);
            ++count;
        }
        LOG.info("");

        /**
         * Now, we will limit our search to a specific territory.
         */
        final Territory territory = Territory.NLD;
        results = Mapcode.encode(lat, lon, territory);
        LOG.info("There are {} Mapcodes in total for latitude {}, longitude {} in {}",
            results.size(), lat, lon, territory.getFullName());

        count = 1;
        for (final MapcodeInfo result : results) {
            LOG.info("  #{}: {}", count, result);
            ++count;
        }
        LOG.info("");

        /**
         * This example tries to encode a lat/lon to Mapcode, regardless of the territory.
         * This produces a full list of Mapcodes.
         */
        lat = 26.87016;
        lon = 75.847;

        results = Mapcode.encode(lat, lon);
        LOG.info("There are {} Mapcodes in total for latitude {}, longitude {} world-wide",
            results.size(), lat, lon);
        count = 1;
        for (final MapcodeInfo result : results) {
            LOG.info("  #{}: {}", count, result);
            ++count;
        }
        LOG.info("");

        /**
         * Finally, we will see what happens when we try to encode a location that is not
         * contained within a territory.
         */
        results = Mapcode.encode(0, 0, territory);
        if (results.isEmpty()) {
            LOG.info("  No Mapcode results found, as expected in this example");
        }
        else {
            LOG.info("This should never happen in this example.");
        }
        LOG.info("");
    }

    private static void exampleGetTerritoryFromISOCode() throws UnknownTerritoryException {
        LOG.info("Example: Get territory from an ISO code");
        LOG.info("--------");

        /**
         * This examples print the full English name of the territory, given an ISO code as a string,
         * which may be obtained from user input, for example.
         */
        final Territory territory = Territory.fromString("NLD");
        LOG.info("Territory {}: {}", territory.name(), territory.getFullName());
        LOG.info("");
    }

    private static void exampleDisambiguateTerritory() throws UnknownTerritoryException {
        LOG.info("Example: Disambiguate a territory code");
        LOG.info("--------");

        /**
         * This example uses an ISO code which is ambiguous: MN. MN is a state in the USA (Minnesota)
         * as well as a state in India. The examples shows how such a territory code can be
         * disambiguated using a parent territory context.
         */
        final String isoCode = "MN";

        // No disambiguation context.
        LOG.info("ISO code {} without context : {}", isoCode,
            Territory.fromString(isoCode).toString());

        // Disambiguation using a correct parent territory context.
        LOG.info("ISO code {} in USA context  : {}", isoCode,
            Territory.fromString(isoCode, ParentTerritory.USA).toString());
        LOG.info("ISO code {} in IND context  : {}", isoCode,
            Territory.fromString(isoCode, ParentTerritory.IND).toString());

        // Disambiguation using an incorrect parent territory context, which does not contains the state.
        // This call will actually fail and throw an exception because the disambiguation cannot be
        // completed.
        try {
            LOG.info("ISO code {} in RUS context  : {}", isoCode,
                Territory.fromString(isoCode, ParentTerritory.RUS).toString());
        }
        catch (final UnknownTerritoryException ignored) {
            LOG.info("ISO code {} in RUS context  : failed (as expected in this example)", isoCode);
        }
        LOG.info("");
    }
}
