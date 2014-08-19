/*******************************************************************************
 * Copyright (c) 2014 Stichting Mapcode Foundation
 * For terms of use refer to http://www.mapcode.com/downloads.html
 ******************************************************************************/
package com.mapcode.example;

import java.util.List;

import com.mapcode.MapcodeDecoder;
import com.mapcode.MapcodeEncoder;
import com.mapcode.common.ParentTerritory;
import com.mapcode.common.Territory;
import com.mapcode.common.Territory.CodeFormat;
import com.mapcode.common.Point;
import com.mapcode.common.UnknownTerritoryNameException;

public class MapCodeExample {
    public static void main(String[] args) {

        // example 1 - disambiguate MN in several conditions
        System.out.println("Example 1:");
        String iso = "MN";
        try {
            System.out.println("ISO code " + iso + " if we don't care about context: "
                    + Territory.fromString(iso).toString());
            System.out.println("ISO code " + iso + " in USA context                : "
                    + Territory.fromString(iso, ParentTerritory.USA).toString());
            System.out.println("ISO code " + iso + " in IND context                : "
                    + Territory.fromString(iso, ParentTerritory.IND).toString());
            System.out.println("ISO code " + iso + " in RUS context                : "
                    + Territory.fromString(iso, ParentTerritory.RUS).toString());
        } catch (UnknownTerritoryNameException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        System.out.println();

        // example 2 - get the Mapcode Territory for a territory abbreviation
        // 'isocode',
        // e.g. entered by the user

        String isocode = "NLD"; // abbreviation for The Netherlands
        Territory mapcodeTerritory = Territory.AAA;
        try {
            mapcodeTerritory = Territory.fromString(isocode);
        } catch (UnknownTerritoryNameException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        // example 3 - print the full English name of the territory.
        System.out.println("Example 2/3:");
        System.out.println("Territory " + isocode + ": " + mapcodeTerritory.getFullName());
        System.out.println();

        // example 4 - decode some mapcodes in this territory
        System.out.println("Example 4 (Decode examples):");
        String mapcode = "49.4V";
        Point p = MapcodeDecoder.decode(mapcode, mapcodeTerritory);
        if (p.isDefined()) {
            System.out.println("Mapcode " + mapcode + " in territory " + mapcodeTerritory.toString()
                    + " represents latitude " + p.getLatDeg() + ", longitude " + p.getLonDeg());
        } else {
            System.out.println("Mapcode " + mapcode + " in territory " + mapcodeTerritory.toString() + " is invalid");
        }

        mapcode = "49.4A";
        p = MapcodeDecoder.decode(mapcode, mapcodeTerritory);
        if (p.isDefined()) {
            System.out.println("Mapcode " + mapcode + " in territory " + mapcodeTerritory.toString()
                    + " represents latitude " + p.getLatDeg() + ", longitude " + p.getLonDeg());
        } else {
            System.out.println("Mapcode " + mapcode + " in territory " + mapcodeTerritory.toString() + " is invalid");
        }
        System.out.println();

        // example 5 - encode a coordinate in this territory
        System.out.println("Example 5 (Encode examples):");
        double lat = 52.376514;
        double lon = 4.908542;

        // get all results, exclude "world"
        List<String> s = MapcodeEncoder.encode(lat, lon, mapcodeTerritory, false, false, false);
        System.out.println("latitude " + lat + ", longitude " + lon + " has " + s.size() + " possible mapcodes in "
                + mapcodeTerritory.getFullName());
        int i = 1;
        for (String result : s) {
            String[] parts = result.split("/");
            String result_mapcode = parts[0];
            int result_ccode = Integer.parseInt(parts[1]); // the ccode

            // let's give non-earth mapcodes an explicit, non-ambiguous
            // territory prefix
            String result_prefix = "";
            if (result_mapcode.length() != 10) {
                result_prefix = Territory.fromTerritoryCode(result_ccode).toString(CodeFormat.MINIMAL_UNAMBIGUOUS)
                        + " ";
            }
            System.out.println(" alternative " + String.valueOf(i++) + ": \"" + result_prefix + result_mapcode + "\"");
        }
        System.out.println();

        // example 6 - encode a coordinate in ALL possible territories
        System.out.println("Example 6:");
        lat = 26.87016;
        lon = 75.847;
        System.out.println("All possible mapcodes in the world for latitude " + lat + ", longitude " + lon);

        s = MapcodeEncoder.encode(lat, lon, null, false, false, true);

        i = 1;
        for (String result : s) {
            String[] parts = result.split("/");
            String result_mapcode = parts[0];
            int result_ccode = Integer.parseInt(parts[1]); // the ccode

            // let's give non-earth mapcodes an explicit, non-ambiguous
            // territory prefix
            String result_prefix = "";
            if (result_mapcode.length() != 10) {
                result_prefix = Territory.fromTerritoryCode(result_ccode).toString(CodeFormat.MINIMAL_UNAMBIGUOUS)
                        + " ";
            }
            System.out.println(" alternative " + String.valueOf(i++) + ": \"" + result_prefix + result_mapcode + "\"");
        }
    }
}
