package fr.unice.polytech.qgl.qda.Island;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * IslandProject created on 18/11/2015 by Keynes Timothy - Aloysius_tim
 */
public enum Ressource {
    FISH("fresh fish from water areas", TypeRessource.PRIMARY, new ArrayList<Biome>(){{
        add(Biome.OCEAN);
        add(Biome.LAKE);
    }}),
    FLOWER("different species of very rare aromatic flowers", TypeRessource.PRIMARY, new ArrayList<Biome>(){{
        add(Biome.ALPINE);
        add(Biome.GLACIER);
        add(Biome.MANGROVE);
    }}),
    FRUITS("exotic fruits from tropical regions", TypeRessource.PRIMARY, new ArrayList<Biome>(){{
        add(Biome.TROPICAL_RAIN_FOREST);
        add(Biome.TROPICAL_SEASONAL_FOREST);
        add(Biome.SUB_TROPICAL_DESERT);
    }}),
    FUR("obtained from games", TypeRessource.PRIMARY, new ArrayList<Biome>(){{
        add(Biome.GRASSLAND);
        add(Biome.SHRUBLAND);
        add(Biome.TEMPERATE_RAIN_FOREST);
    }}),
    ORE("mined resource", TypeRessource.PRIMARY, new ArrayList<Biome>(){{
        add(Biome.ALPINE);
        add(Biome.SUB_TROPICAL_DESERT);
        add(Biome.TEMPERATE_DESERT);
        add(Biome.TEMPERATE_DECIDUOUS_FOREST);
    }}),
    QUARTZ("silicium cristal", TypeRessource.PRIMARY, new ArrayList<Biome>(){{
        add(Biome.BEACH);
        add(Biome.SUB_TROPICAL_DESERT);
        add(Biome.TEMPERATE_DESERT);
        add(Biome.TEMPERATE_DECIDUOUS_FOREST);
    }}),
    SUGAR_CANE("flavored plant with high economic value", TypeRessource.PRIMARY, new ArrayList<Biome>(){{
        add(Biome.TROPICAL_RAIN_FOREST);
        add(Biome.TROPICAL_SEASONAL_FOREST);
    }}),
    WOOD("trunks and logs of solid wood", TypeRessource.PRIMARY, new ArrayList<Biome>(){{
        add(Biome.MANGROVE);
        add(Biome.TAIGA);
        add(Biome.TEMPERATE_DECIDUOUS_FOREST);
        add(Biome.TEMPERATE_RAIN_FOREST);
        add(Biome.TROPICAL_RAIN_FOREST);
        add(Biome.TROPICAL_SEASONAL_FOREST);
    }}),
    GLASS("fine glass jewelry", TypeRessource.MANUFACTURED, new ArrayList<Biome>(){{
        addAll(QUARTZ.associatedBiomes);
        addAll(WOOD.associatedBiomes);
    }}, new HashMap<Ressource, Double>(){{
        put(QUARTZ, (double) 11);
        put(WOOD, (double) 6);
    }}),
    INGOT("standardized ingots", TypeRessource.MANUFACTURED, new ArrayList<Biome>(){{
        addAll(ORE.associatedBiomes);
        addAll(WOOD.associatedBiomes);
    }}, new HashMap<Ressource, Double>(){{
        put(ORE, (double) 5.5);
        put(WOOD, (double) 5.5);
    }}),
    LEATHER("high quality leather pieces", TypeRessource.MANUFACTURED, new ArrayList<Biome>(){{
        addAll(FUR.associatedBiomes);
    }}, new HashMap<Ressource, Double>(){{
        put(FUR, (double) 3.3);
    }}),
    PLANK("materials for carpenters", TypeRessource.MANUFACTURED, new ArrayList<Biome>(){{
        addAll(WOOD.associatedBiomes);
    }}, new HashMap<Ressource, Double>(){{
        put(WOOD, 0.3);
    }}),
    RUM("world renowned beverage", TypeRessource.MANUFACTURED, new ArrayList<Biome>(){{
        addAll(SUGAR_CANE.associatedBiomes);
        addAll(FRUITS.associatedBiomes);
    }}, new HashMap<Ressource, Double>(){{
        put(SUGAR_CANE, (double) 11);
        put(FRUITS, (double) 1.1);
    }});

    private String description;
    private TypeRessource typeRessource;
    private ArrayList<Biome> associatedBiomes;
    private HashMap<Ressource, Double> recipe;

    Ressource(String description, TypeRessource typeRessource, ArrayList<Biome> associatedBiomes) {
        this.associatedBiomes = associatedBiomes;
        this.description = description;
        this.typeRessource = typeRessource;
        this.recipe=null;
    }

    Ressource(String description, TypeRessource typeRessource, ArrayList<Biome> associatedBiomes, HashMap<Ressource, Double> recipe) {
        this.associatedBiomes = associatedBiomes;
        this.description = description;
        this.typeRessource = typeRessource;
        this.recipe=recipe;
    }

    public enum TypeRessource{
        PRIMARY,
        MANUFACTURED
    }

    public HashMap<Ressource, Integer> getFinalRecipe(int nbUnit){
        HashMap<Ressource, Integer> recipe=new HashMap<>();

        Iterator it = this.getRecipe().entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();

            recipe.put((Ressource) pair.getKey(), (int)(((Double)pair.getValue())*nbUnit));
        }
        return recipe;
    }

    public ArrayList<Biome> getAssociatedBiomes() {
        return associatedBiomes;
    }

    public String getDescription() {
        return description;
    }

    public HashMap<Ressource, Double> getRecipe() {
        return recipe;
    }

    public TypeRessource getTypeRessource() {
        return typeRessource;
    }
}
