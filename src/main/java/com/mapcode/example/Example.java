/*
 * Copyright (C) 2014-2019, Stichting Mapcode Foundation (http://www.mapcode.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.mapcode.example;

import com.mapcode.Mapcode;
import com.mapcode.MapcodeCodec;
import com.mapcode.Point;
import com.mapcode.Territory;
import com.mapcode.UnknownMapcodeException;
import com.mapcode.UnknownTerritoryException;

import java.util.List;

/**
 * This class serves as an example of how to use the Mapcode Java library.
 */
@SuppressWarnings("UseOfSystemOutOrSystemErr")
public class Example {

    private Example() {
        // Empty.
    }

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
        System.out.println("Example: Decoding Mapcodes to lat/lon points");
        System.out.println("--------");

        /**
         * This example shows you how to decode a Mapcode to a point. The first example decodes
         * a valid Mapcode to a point.
         */
        final Territory territory = Territory.fromString("NLD");
        final String mapcode1 = "49.4V";
        try {
            final Point p = MapcodeCodec.decode(mapcode1, territory);
            System.out.println("Mapcode " + mapcode1 + " in territory " + territory + " represents latitude " + p
                    .getLatDeg() + ", " +
                    "longitude " + p.getLonDeg());
        } catch (final UnknownMapcodeException ignored) {
            System.out.println("This should never happen in this example as the Mapcode is valid.");
        }

        /**
         * This second example shows you how to decode a Mapcode to a point, which already includes a
         * territory name.
         */
        final String mapcode2 = "NLD 49.4V";
        try {
            final Point p = MapcodeCodec.decode(mapcode2);
            System.out.println("Mapcode " + mapcode2 + " in territory "
                    + territory + " represents latitude " + p.getLatDeg()
                    + ", longitude " + p.getLonDeg());
        } catch (final UnknownMapcodeException ignored) {
            System.out.println("This should never happen in this example as the Mapcode is valid.");
        }

        /**
         * The third example tries to decode an invalid Mapcode to a point which results
         * in an exception being thrown.
         */
        final String mapcode3 = "49.4A";
        try {
            final Point p = MapcodeCodec.decode(mapcode3, territory);
            System.out.println(
                    "Mapcode " + mapcode3 + " in territory " + territory + " represents latitude " + p.getLatDeg()
                            + ", longitude " + p.getLonDeg());
        } catch (final UnknownMapcodeException ignored) {
            System.out.println("Mapcode " + mapcode3 + " in territory " + territory + " is invalid, " +
                    "as expected in this example");
        }

        /**
         * The fourth example tries to decode a valid international Mapcode without a territory context.
         * This will succeed.
         */
        final String mapcode4 = "PQ0PF.5M1H";
        try {
            final Point p = MapcodeCodec.decode(mapcode4);
            System.out.println(
                    "Mapcode " + mapcode4 + " represents latitude " + p.getLatDeg() + ", longitude " + p.getLonDeg());
        } catch (final UnknownMapcodeException ignored) {
            System.out.println("This should never happen in this example as the Mapcode is valid.");
        }

        /**
         * The fifth example tries to decode a valid Mapcode that contains
         * Unicode characters. This will succeed.
         */
        final String mapcode5 = "JPN \u30c1\u30ca.8\u30c1";
        try {
            final Point p = MapcodeCodec.decode(mapcode5);
            System.out.println("Mapcode " + mapcode5 + " represents latitude "
                    + p.getLatDeg() + ", longitude " + p.getLonDeg());
        } catch (final UnknownMapcodeException ignored) {
            System.out
                    .println("This should never happen in this example as the Mapcode is valid.");
        }

        System.out.println("");
    }

    private static void exampleEncode() {
        System.out.println("Example: Encoding lat/lon points to Mapcodes");
        System.out.println("--------");

        /**
         * This example shows you how to encode a lat/lon point into a Mapcode.
         */
        double lat = 52.376514;
        double lon = 4.908542;

        /**
         * First, we will try to get all possible Mapcodes, for all territories.
         */
        List<Mapcode> results = MapcodeCodec.encode(lat, lon);
        System.out.println("There are " + results.size() + " Mapcodes in total for latitude " + lat + ", " +
                "longitude " + lon + " world-wide");

        int count = 1;
        for (final Mapcode result : results) {
            System.out.println("  #" + count + ": " + result);
            ++count;
        }
        System.out.println("");

        /**
         * Now, we will limit our search to a specific territory.
         */
        final Territory territory = Territory.NLD;
        results = MapcodeCodec.encode(lat, lon, territory);
        System.out.println("There are " + results.size() + " Mapcodes in total for latitude " + lat + ", " +
                "longitude " + lon + " in " + territory.getFullName());

        count = 1;
        for (final Mapcode result : results) {
            System.out.println("  #" + count + ": " + result);
            ++count;
        }
        System.out.println("");

        /**
         * This example tries to encode a lat/lon to Mapcode, regardless of the territory.
         * This produces a full list of Mapcodes.
         */
        lat = 26.87016;
        lon = 75.847;

        results = MapcodeCodec.encode(lat, lon);
        System.out.println("There are " + results.size() + " Mapcodes in total for latitude " + lat + ", " +
                "longitude " + lon + " world-wide");
        count = 1;
        for (final Mapcode result : results) {
            System.out.println("  #" + count + ": " + result);
            ++count;
        }
        System.out.println("");

        /**
         * Finally, we will see what happens when we try to encode a location that is not
         * contained within a territory.
         */
        results = MapcodeCodec.encode(0, 0, territory);
        if (results.isEmpty()) {
            System.out.println("  No Mapcode results found, as expected in this example");
        } else {
            System.out.println("This should never happen in this example.");
        }
        System.out.println("");
    }

    private static void exampleGetTerritoryFromISOCode() throws UnknownTerritoryException {
        System.out.println("Example: Get territory from an ISO code");
        System.out.println("--------");

        /**
         * This examples print the full English name of the territory, given an ISO code as a string,
         * which may be obtained from user input, for example.
         */
        final Territory territory = Territory.fromString("NLD");
        System.out.println("Territory " + territory.name() + ": " + territory.getFullName());
        System.out.println("");
    }

    private static void exampleDisambiguateTerritory() throws UnknownTerritoryException {
        System.out.println("Example: Disambiguate a territory code");
        System.out.println("--------");

        /**
         * This example uses an ISO code which is ambiguous: MN. MN is a state in the USA (Minnesota)
         * as well as a state in India. The examples shows how such a territory code can be
         * disambiguated using a parent territory context.
         */
        final String isoCode = "MN";

        // No disambiguation context.
        System.out.println("ISO code " + isoCode + " without context : " + Territory.fromString(isoCode).toString());

        // Disambiguation using a correct parent territory context.
        System.out.println("ISO code " + isoCode + " in USA context  : " +
                Territory.fromString(isoCode, Territory.USA).toString());
        System.out.println("ISO code " + isoCode + " in IND context  : " +
                Territory.fromString(isoCode, Territory.IND).toString());

        // Disambiguation using an incorrect parent territory context, which does not contains the state.
        // This call will actually fail and throw an exception because the disambiguation cannot be
        // completed.
        try {
            System.out.println("ISO code " + isoCode + " in RUS context  : " + Territory.fromString(isoCode,
                    Territory.RUS).toString());
        } catch (final UnknownTerritoryException ignored) {
            System.out.println("ISO code " + isoCode + " in RUS context  : failed (as expected in this example)");
        }
        System.out.println("");
    }
}
