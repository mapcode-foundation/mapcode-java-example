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
        exampleDisambiguateTerritory();
        exampleGetTerritoryFromISOCode();
        exampleDecode();
        exampleEncode();
        exampleEncodeInAllTerritories();
    }

    private static void exampleGetTerritoryFromISOCode() throws UnknownTerritoryException {
        LOG.info("Example: Get territory from ISO code");

        // Print the full English name of the territory.
        final Territory territory = Territory.fromString("NLD");
        LOG.info("Territory {}: {}", territory.name(), territory.getFullName());
        LOG.info("");
    }

    private static void exampleDisambiguateTerritory() throws UnknownTerritoryException {
        LOG.info("Example: (disambiguate code MN, which can be in USA and IND):");

        final String isoCode = "MN";
        LOG.info("ISO code {} without context : {}", isoCode,
            Territory.fromString(isoCode).toString());
        LOG.info("ISO code {} in USA context  : {}", isoCode,
            Territory.fromString(isoCode, ParentTerritory.USA).toString());
        LOG.info("ISO code {} in IND context  : {}", isoCode,
            Territory.fromString(isoCode, ParentTerritory.IND).toString());
        LOG.info("ISO code {} in RUS context  : {}", isoCode,
            Territory.fromString(isoCode, ParentTerritory.RUS).toString());
        LOG.info("");
    }

    private static void exampleDecode() throws UnknownTerritoryException {
        LOG.info("Example: Decode");

        final Territory territory = Territory.fromString("NLD");
        final String mapcode1 = "49.4V";
        try {
            final Point p = Mapcode.decode(mapcode1, territory);
            LOG.info("Mapcode {} in territory {} represents latitude {}, longitude {}",
                mapcode1, territory, p.getLatDeg(), p.getLonDeg());
        }
        catch (final UnknownMapcodeException ignored) {
            LOG.info("Mapcode {} in territory {} is invalid", mapcode1, territory);
        }

        final String mapcode2 = "49.4A";
        try {
            final Point p = Mapcode.decode(mapcode2, territory);
            LOG.info("Mapcode {} in territory {} represents latitude {}, longitude {}",
                mapcode2, territory, p.getLatDeg(), p.getLonDeg());
        }
        catch (final UnknownMapcodeException ignored) {
            LOG.info("Mapcode {} in territory {} is invalid", mapcode2, territory);
        }
        LOG.info("");
    }

    private static void exampleEncode() throws UnknownTerritoryException {
        LOG.info("Example: Encode");

        final Territory territory = Territory.NLD;
        final double lat = 52.376514;
        final double lon = 4.908542;
        LOG.info("All possible Mapcodes in {} for latitude {}, longitude {}", territory.getFullName(), lat, lon);

        // Get all results for territory NLD.
        final List<MapcodeInfo> results = Mapcode.encode(lat, lon, territory);
        LOG.info("Point latitude {}, longitude {} has {} possible Mapcodes in {}",
            lat, lon, results.size(), territory.getFullName());

        int count = 1;
        for (final MapcodeInfo result : results) {
            LOG.info("  Alternative {}: {} {}", count,
                territory.toNameFormat(Territory.NameFormat.MINIMAL_UNAMBIGUOUS), result.getMapcode());
            ++count;
        }
        LOG.info("");
    }

    private static void exampleEncodeInAllTerritories() throws UnknownTerritoryException {
        LOG.info("Example: Encode in all territories");

        final double lat = 26.87016;
        final double lon = 75.847;
        LOG.info("All possible Mapcodes in the world for latitude {}, longitude {}", lat, lon);

        final List<MapcodeInfo> results = Mapcode.encode(lat, lon);
        int count = 1;
        for (final MapcodeInfo result : results) {
            LOG.info("  Alternative {}: {} {}", count,
                result.getTerritory().toNameFormat(Territory.NameFormat.MINIMAL), result.getMapcode());
            ++count;
        }
        LOG.info("");
    }
}
