package com.marccusz.mm_dependencyinjection.Models;

import dev.morphia.annotations.*;
import org.bson.types.ObjectId;

import java.util.List;

@Entity("placed_block")
@Indexes(
        @Index(fields = {@Field("X"), @Field("Y"), @Field("Z")}, options = @IndexOptions(unique = true))
)
public class PlacedBlockData
{
    public PlacedBlockData(){}

    public PlacedBlockData(int x, int y, int z, String itemName, List<String> itemLore)
    {
        X = x;
        Y = y;
        Z = z;
        ItemName = itemName;
        ItemLore = itemLore;
    }

    @Id
    public ObjectId Id;

    public int X;
    public int Y;
    public int Z;
    public String ItemName;
    public List<String> ItemLore;
}
