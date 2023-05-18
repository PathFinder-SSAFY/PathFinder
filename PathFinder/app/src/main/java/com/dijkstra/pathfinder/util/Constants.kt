package com.dijkstra.pathfinder.util

import com.dijkstra.pathfinder.data.dto.BeaconPosition
import com.dijkstra.pathfinder.data.dto.Path
import com.dijkstra.pathfinder.data.dto.Point
import com.google.gson.GsonBuilder

val beaconPositionList = listOf<BeaconPosition>(
    BeaconPosition(
        id = 33246,
        x = 19.18,
        y = 2.38,
//        z = -5.5,
        z = -6.0
    ),
    BeaconPosition(
        id = 33234,
        x = 21.68,
        y = 2.09,
//        z = -4.97
        z = -5.62
    ),
    BeaconPosition(
        id = 33235,
        x = 17.1,
        y = 2.38,
//        z = -7.18
        z = -7.86
    ),
    BeaconPosition(
        id = 33247,
        x = 16.26,
        y = 2.38,
        z = -2.99
    ),
    BeaconPosition(
        id = 33240,
        x = 22.24,
        y = 2.38,
        z = -1.7
    ),
    BeaconPosition(
        id = 33233,
        x = 32.49,
        y = 2.38,
//        z = 0.61
        z = -0.12
    ),
    BeaconPosition(
        id = 33159,
        x = 37.29,
        y = 2.38,
        z = -5.15
    ),
    BeaconPosition(
        id = 33250,
        x = 26.84,
        y = 2.38,
//        z = -5.18
        z = -5.8
    ),
    BeaconPosition(
        id = 33236,
        x = 29.04,
        y = 2.38,
//        z = -7.72
        z = -8.61
    ),
    BeaconPosition(
        id = 33242,
        x = 24.24,
        y = 2.38,
//        z = -7.18
        z = -7.84
    ),
    BeaconPosition(
        id = 33160,
        x = 19.0,
        y = 2.38,
//        z = -7.0
        z = -4.99
    ),
    BeaconPosition(
        id = 33157,
        x = 27.19,
        y = 2.38,
        z = -1.7
    ),
    BeaconPosition(
        id = 33164,
        x = 24.18,
        y = 2.38,
//        z = -12.71
        z = -5.33
    ),
    BeaconPosition(
        id = 33158,
        x = 31.25,
        y = 2.38,
//        z = -8.41
        z = -5.66
    ),
    BeaconPosition(
        id = 33241,
        x = 23.6,
        y = -1.07,
//        z = -8.27
        z = -8.93
    ),
)

val tempPathList =  listOf<Path>(
    Path(Point(15.759, 0.0, -7.0), 5.921, 3),
    Path(Point(21.68, 0.0, -7.0), 0.0, 5),
    Path(Point(21.68, 0.0, -7.0), 6.88, 2),
    Path(Point(21.68, 0.0, -0.12), 0.0, 6),
    Path(Point(21.68, 0.0, -0.12), 10.81, 3)
)

val tempPointList = "[\n" +
        "    {\n" +
        "        \"x\": 17.0,\n" +
        "        \"y\": 0.0,\n" +
        "        \"z\": -7.0\n" +
        "    },\n" +
        "    {\n" +
        "        \"x\": 18.0,\n" +
        "        \"y\": 0.0,\n" +
        "        \"z\": -7.0\n" +
        "    },\n" +
        "    {\n" +
        "        \"x\": 19.0,\n" +
        "        \"y\": 0.0,\n" +
        "        \"z\": -7.0\n" +
        "    },\n" +
        "    {\n" +
        "        \"x\": 20.0,\n" +
        "        \"y\": 0.0,\n" +
        "        \"z\": -7.0\n" +
        "    },\n" +
        "    {\n" +
        "        \"x\": 21.0,\n" +
        "        \"y\": 0.0,\n" +
        "        \"z\": -7.0\n" +
        "    },\n" +
        "    {\n" +
        "        \"x\": 22.0,\n" +
        "        \"y\": 0.0,\n" +
        "        \"z\": -7.0\n" +
        "    },\n" +
        "    {\n" +
        "        \"x\": 23.0,\n" +
        "        \"y\": 0.0,\n" +
        "        \"z\": -7.0\n" +
        "    },\n" +
        "    {\n" +
        "        \"x\": 24.0,\n" +
        "        \"y\": 0.0,\n" +
        "        \"z\": -7.0\n" +
        "    },\n" +
        "    {\n" +
        "        \"x\": 25.0,\n" +
        "        \"y\": 0.0,\n" +
        "        \"z\": -7.0\n" +
        "    },\n" +
        "    {\n" +
        "        \"x\": 26.0,\n" +
        "        \"y\": 0.0,\n" +
        "        \"z\": -7.0\n" +
        "    },\n" +
        "    {\n" +
        "        \"x\": 27.0,\n" +
        "        \"y\": 0.0,\n" +
        "        \"z\": -7.0\n" +
        "    },\n" +
        "    {\n" +
        "        \"x\": 28.0,\n" +
        "        \"y\": 0.0,\n" +
        "        \"z\": -7.0\n" +
        "    },\n" +
        "    {\n" +
        "        \"x\": 29.0,\n" +
        "        \"y\": 0.0,\n" +
        "        \"z\": -7.0\n" +
        "    },\n" +
        "    {\n" +
        "        \"x\": 29.0,\n" +
        "        \"y\": 0.0,\n" +
        "        \"z\": -6.0\n" +
        "    },\n" +
        "    {\n" +
        "        \"x\": 29.0,\n" +
        "        \"y\": 0.0,\n" +
        "        \"z\": -5.0\n" +
        "    },\n" +
        "    {\n" +
        "        \"x\": 29.0,\n" +
        "        \"y\": 0.0,\n" +
        "        \"z\": -4.0\n" +
        "    },\n" +
        "    {\n" +
        "        \"x\": 29.0,\n" +
        "        \"y\": 0.0,\n" +
        "        \"z\": -3.0\n" +
        "    },\n" +
        "    {\n" +
        "        \"x\": 29.0,\n" +
        "        \"y\": 0.0,\n" +
        "        \"z\": -2.0\n" +
        "    },\n" +
        "    {\n" +
        "        \"x\": 29.0,\n" +
        "        \"y\": 0.0,\n" +
        "        \"z\": -1.0\n" +
        "    },\n" +
        "    {\n" +
        "        \"x\": 29.0,\n" +
        "        \"y\": 0.0,\n" +
        "        \"z\": 0.0\n" +
        "    }\n" +
        "]"