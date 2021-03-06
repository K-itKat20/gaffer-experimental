export const exampleElementsSchema: object = {
    edges: {
        RoadUse: {
            description: "A directed edge representing vehicles moving from junction A to junction B.",
            source: "junction",
            destination: "junction",
            directed: "true",
            properties: {
                startDate: "date.earliest",
                endDate: "date.latest",
                count: "count.long",
                countByVehicleType: "counts.freqmap",
            },
            groupBy: ["startDate", "endDate"],
        },
        RoadHasJunction: {
            description: "A directed edge from each road to all the junctions on that road.",
            source: "road",
            destination: "junction",
            directed: "true",
        },
        RegionContainsLocation: {
            description: "A directed edge from each region to location.",
            source: "region",
            destination: "location",
            directed: "true",
        },
        LocationContainsRoad: {
            description: "A directed edge from each location to road.",
            source: "location",
            destination: "road",
            directed: "true",
        },
        JunctionLocatedAt: {
            description: "A directed edge from each junction to its coordinates",
            source: "junction",
            destination: "coordinates",
            directed: "true",
        },
    },
    entities: {
        Cardinality: {
            description: "An entity that is added to every vertex representing the connectivity of the vertex.",
            vertex: "anyVertex",
            properties: {
                edgeGroup: "set",
                hllp: "hllp",
                count: "count.long",
            },
            groupBy: ["edgeGroup"],
        },
        JunctionUse: {
            description:
                "An entity on the junction vertex representing the counts of vehicles moving from junction A to junction B.",
            vertex: "junction",
            properties: {
                startDate: "date.earliest",
                endDate: "date.latest",
                count: "count.long",
                countByVehicleType: "counts.freqmap",
            },
            groupBy: ["startDate", "endDate"],
        },
    },
};
