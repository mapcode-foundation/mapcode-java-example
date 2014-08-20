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

        // Example 1 - Disambiguate MN in several conditions.
        LOG.info("Example 1:");

        final String iso = "MN";
        LOG.info("ISO code " + iso + " if we don't care about context: " + Territory.fromString(iso).toString());
        LOG.info("ISO code " + iso + " in USA context                : "
            + Territory.fromString(iso, ParentTerritory.USA).toString());
        LOG.info("ISO code " + iso + " in IND context                : "
            + Territory.fromString(iso, ParentTerritory.IND).toString());
        LOG.info("ISO code " + iso + " in RUS context                : "
            + Territory.fromString(iso, ParentTerritory.RUS).toString());
        LOG.info("");

        // Example 2 - Get the Mapcode Territory for a territory abbreviation 'isocode',
        // e.g. entered by the user.

        final String isocode = "NLD";   // Abbreviation for The Netherlands
        final Territory mapcodeTerritory = Territory.fromString(isocode);

        // Example 3 - print the full English name of the territory.
        LOG.info("Example 2/3:");
        LOG.info("Territory " + isocode + ": " + mapcodeTerritory.getFullName());
        LOG.info("");

        // Example 4 - Decode some Mapcodes in this territory.
        LOG.info("Example 4 (Decode examples):");
        String mapcode = "49.4V";
        try {
            final Point p = Mapcode.decode(mapcode, mapcodeTerritory);
            LOG.info(
                "Mapcode " + mapcode + " in territory " + mapcodeTerritory.toString() + " represents latitude " +
                    p.getLatDeg() + ", longitude " + p.getLatDeg());
        }
        catch (final UnknownMapcodeException ignored) {
            LOG.info("Mapcode " + mapcode + " in territory " + mapcodeTerritory.toString() + " is invalid");
        }

        mapcode = "49.4A";
        try {
            final Point p = Mapcode.decode(mapcode, mapcodeTerritory);
            LOG.info(
                "Mapcode " + mapcode + " in territory " + mapcodeTerritory.toString() + " represents latitude " +
                    p.getLatDeg() + ", longitude " + p.getLonDeg());
        }
        catch (final UnknownMapcodeException ignored) {
            LOG.info("Mapcode " + mapcode + " in territory " + mapcodeTerritory.toString() + " is invalid");
        }
        LOG.info("");

        // example 5 - Encode a coordinate in this territory.
        LOG.info("Example 5 (Encode examples):");
        double lat = 52.376514;
        double lon = 4.908542;

        // get all results, exclude "world"
        List<MapcodeInfo> results = Mapcode.encode(lat, lon, mapcodeTerritory);
        LOG.info("latitude " + lat + ", longitude " + lon + " has " + results.size() + " possible Mapcodes" +
            " in " +
            mapcodeTerritory.getFullName());
        int count = 1;
        for (final MapcodeInfo result : results) {
            final String result_mapcode = result.getMapcode();
            final int resultCcode = result.getTerritory().getTerritoryCode();

            // let's give non-earth Mapcodes an explicit, non-ambiguous territory prefix
            String resultPrefix = "";
            if (result_mapcode.length() != 10) {
                resultPrefix =
                    Territory.fromTerritoryCode(resultCcode).toString(
                        Territory.TerritoryNameFormat.MINIMAL_UNAMBIGUOUS) + ' ';
            }
            LOG.info("  alternative " + count + ": \"" + resultPrefix + result_mapcode + '"');
            ++count;
        }
        LOG.info("");

        // example 6 - Encode a coordinate in ALL possible territories.
        LOG.info("Example 6:");
        lat = 26.87016;
        lon = 75.847;
        LOG.info("All possible Mapcodes in the world for latitude " + lat + ", longitude " + lon);

        results = Mapcode.encode(lat, lon);
        count = 1;
        for (final MapcodeInfo result : results) {
            final String result_mapcode = result.getMapcode();
            final int result_ccode = result.getTerritory().getTerritoryCode();

            // let's give non-earth Mapcodes an explicit, non-ambiguous territory prefix
            String result_prefix = "";
            if (result_mapcode.length() != 10) {
                result_prefix =
                    Territory.fromTerritoryCode(result_ccode).toString(
                        Territory.TerritoryNameFormat.MINIMAL_UNAMBIGUOUS) +
                        ' ';
            }
            LOG.info("  alternative " + count + ": \"" + result_prefix + result_mapcode + '"');
            ++count;
        }
    }
}
