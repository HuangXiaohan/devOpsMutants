package fr.unice.polytech.qgl.qda.Island;


/**
 * IslandProject created on 17/11/2015 by Keynes Timothy - Aloysius_tim
 */
public enum Biome {
    OCEAN(BiomeType.COMMON, "plain ocean, wide open area full of unknown;"),
    LAKE(BiomeType.COMMON, "internal lake, potentially big, with freshwater;"),
    BEACH(BiomeType.COMMON, "beach (not always sandy);"),
    GRASSLAND(BiomeType.COMMON, "area of prairie;"),
    MANGROVE(BiomeType.TROPICAL, "super wet area, home of the mangle tree;"),
    TROPICAL_RAIN_FOREST(BiomeType.TROPICAL, "hot and wet;"),
    TROPICAL_SEASONAL_FOREST(BiomeType.TROPICAL, "less wet, but not less hot;"),
    TEMPERATE_DECIDUOUS_FOREST(BiomeType.TEMPERATE, "classical forests with trees that lose their leaves each year;", true),
    TEMPERATE_RAIN_FOREST(BiomeType.TEMPERATE, "very rare biome, very wet area coupled to low temperatures;"),
    TEMPERATE_DESERT(BiomeType.TEMPERATE, "aride area with sparse vegetation and very few humidity;"),
    TAIGA(BiomeType.NORDIC_MOUNTAIN, "boreal forest, cold and majestuous;"),
    SNOW(BiomeType.NORDIC_MOUNTAIN, "area covered with snow, wet and cold;"),
    TUNDRA(BiomeType.NORDIC_MOUNTAIN, "arctic prairie, surviving on permanently frozen soil;"),
    ALPINE(BiomeType.NORDIC_MOUNTAIN, "rocky mountains, not always covered by snow;"),
    GLACIER(BiomeType.NORDIC_MOUNTAIN, "inhospitable area, full of ice;"),
    SHRUBLAND(BiomeType.SUBTROPICAL, "prairie dominated by shrubs, such as maquis in Corsica or garrigue in Provence;"),
    SUB_TROPICAL_DESERT(BiomeType.SUBTROPICAL, "very dry and inhospitable area"),
    UNKNOWN(BiomeType.UNKNOWN, "unknown"),
    OUT_OF_RANGE(BiomeType.UNKNOWN, "out of range"),
    GROUND(BiomeType.UNKNOWN, "Terre");

    private BiomeType biomeTypeAssociated;
    private String description;
    private boolean rare;
    Biome(BiomeType type, String description) {
        this.biomeTypeAssociated=type;
        this.description=description;
        this.rare=false;
    }


    Biome(BiomeType type, String description, boolean rare) {
        this.biomeTypeAssociated=type;
        this.description=description;
        this.rare=rare;
    }

    public enum BiomeType {
        COMMON,
        TROPICAL,
        TEMPERATE,
        NORDIC_MOUNTAIN,
        SUBTROPICAL,
        UNKNOWN
    }

    public BiomeType getBiomeTypeAssociated() {
        return biomeTypeAssociated;
    }

    public String getDescription() {
        return description;
    }

    public boolean isRare() {
        return rare;
    }
}
