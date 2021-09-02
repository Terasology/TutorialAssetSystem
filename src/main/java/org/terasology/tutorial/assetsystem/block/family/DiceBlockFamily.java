// Copyright 2021 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0
package org.terasology.tutorial.assetsystem.block.family;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.terasology.engine.math.Pitch;
import org.terasology.engine.math.Roll;
import org.terasology.engine.math.Rotation;
import org.terasology.engine.math.Side;
import org.terasology.engine.math.Yaw;
import org.terasology.gestalt.naming.Name;
import org.terasology.engine.world.block.Block;
import org.terasology.engine.world.block.BlockBuilderHelper;
import org.terasology.engine.world.block.BlockUri;
import org.terasology.engine.world.block.family.AbstractBlockFamily;
import org.terasology.engine.world.block.family.BlockPlacementData;
import org.terasology.engine.world.block.family.RegisterBlockFamily;
import org.terasology.engine.world.block.loader.BlockFamilyDefinition;
import org.terasology.engine.world.block.shapes.BlockShape;

import java.util.List;
import java.util.Locale;
import java.util.Map;

@RegisterBlockFamily("dice")
public class DiceBlockFamily extends AbstractBlockFamily {
    private Map<Side, Block> blockMapping = Maps.newEnumMap(Side.class);
    private List<Block> blocks = Lists.newArrayList();
    private Block archetype;


    private static final Map<Side,Map<Side,Rotation>> BLOCK_ROTATIONS;

    static {
        BLOCK_ROTATIONS = Maps.newEnumMap(Side.class);
        //rotations where the TOP is on top and we rotate the front (add yaw)
        Map<Side, Rotation> topOnTopRotations = Maps.newEnumMap(Side.class);
        topOnTopRotations.put(Side.FRONT, Rotation.rotate(Yaw.NONE, Pitch.NONE, Roll.NONE));
        topOnTopRotations.put(Side.RIGHT, Rotation.rotate(Yaw.CLOCKWISE_90, Pitch.NONE, Roll.NONE));
        topOnTopRotations.put(Side.BACK, Rotation.rotate(Yaw.CLOCKWISE_180, Pitch.NONE, Roll.NONE));
        topOnTopRotations.put(Side.LEFT, Rotation.rotate(Yaw.CLOCKWISE_270, Pitch.NONE, Roll.NONE));
        BLOCK_ROTATIONS.put(Side.TOP, topOnTopRotations);

        //rotations where FRONT is on top (pitch 90) and we rotate the front (add yaw)
        Map<Side, Rotation> frontOnTopRotations = Maps.newEnumMap(Side.class);
        frontOnTopRotations.put(Side.BOTTOM, Rotation.rotate(Yaw.NONE, Pitch.CLOCKWISE_90, Roll.NONE));
        frontOnTopRotations.put(Side.BACK, Rotation.rotate(Yaw.CLOCKWISE_90, Pitch.CLOCKWISE_90, Roll.NONE));
        frontOnTopRotations.put(Side.TOP, Rotation.rotate(Yaw.CLOCKWISE_180, Pitch.CLOCKWISE_90, Roll.NONE));
        frontOnTopRotations.put(Side.FRONT, Rotation.rotate(Yaw.CLOCKWISE_270, Pitch.CLOCKWISE_90, Roll.NONE));
        BLOCK_ROTATIONS.put(Side.FRONT, frontOnTopRotations);

        //rotations where RIGHT is on top (roll 270) and we rotate the front (add yaw)
        Map<Side, Rotation> rightOnTopRotations = Maps.newEnumMap(Side.class);
        rightOnTopRotations.put(Side.FRONT, Rotation.rotate(Yaw.NONE, Pitch.NONE, Roll.CLOCKWISE_270));
        rightOnTopRotations.put(Side.BOTTOM, Rotation.rotate(Yaw.CLOCKWISE_90, Pitch.NONE, Roll.CLOCKWISE_270));
        rightOnTopRotations.put(Side.BACK, Rotation.rotate(Yaw.CLOCKWISE_180, Pitch.NONE, Roll.CLOCKWISE_270));
        rightOnTopRotations.put(Side.TOP, Rotation.rotate(Yaw.CLOCKWISE_270, Pitch.NONE, Roll.CLOCKWISE_270));
        BLOCK_ROTATIONS.put(Side.RIGHT, rightOnTopRotations);

        //rotations where LEFT is on top (roll 90) and we rotate the front (add yaw)
        Map<Side, Rotation> leftOnTopRotations = Maps.newEnumMap(Side.class);
        leftOnTopRotations.put(Side.FRONT, Rotation.rotate(Yaw.NONE, Pitch.NONE, Roll.CLOCKWISE_90));
        leftOnTopRotations.put(Side.TOP, Rotation.rotate(Yaw.CLOCKWISE_90, Pitch.NONE, Roll.CLOCKWISE_90));
        leftOnTopRotations.put(Side.BACK, Rotation.rotate(Yaw.CLOCKWISE_180, Pitch.NONE, Roll.CLOCKWISE_90));
        leftOnTopRotations.put(Side.BOTTOM, Rotation.rotate(Yaw.CLOCKWISE_270, Pitch.NONE, Roll.CLOCKWISE_90));
        BLOCK_ROTATIONS.put(Side.LEFT, leftOnTopRotations);

        //rotations where BACK is on top (pitch 270) and we rotate the front (add yaw)
        Map<Side, Rotation> backOnTopRotations = Maps.newEnumMap(Side.class);
        backOnTopRotations.put(Side.TOP, Rotation.rotate(Yaw.NONE, Pitch.CLOCKWISE_270, Roll.NONE));
        backOnTopRotations.put(Side.RIGHT, Rotation.rotate(Yaw.CLOCKWISE_90, Pitch.CLOCKWISE_270, Roll.NONE));
        backOnTopRotations.put(Side.BOTTOM, Rotation.rotate(Yaw.CLOCKWISE_180, Pitch.CLOCKWISE_270, Roll.NONE));
        backOnTopRotations.put(Side.LEFT, Rotation.rotate(Yaw.CLOCKWISE_270, Pitch.CLOCKWISE_270, Roll.NONE));
        BLOCK_ROTATIONS.put(Side.BACK, backOnTopRotations);

        //rotations where BOTTOM is on top (pitch 180) and we rotate the front (add yaw)
        Map<Side, Rotation> bottomOnTopRotations = Maps.newEnumMap(Side.class);
        bottomOnTopRotations.put(Side.FRONT, Rotation.rotate(Yaw.NONE, Pitch.CLOCKWISE_180, Roll.NONE));
        bottomOnTopRotations.put(Side.LEFT, Rotation.rotate(Yaw.CLOCKWISE_90, Pitch.CLOCKWISE_180, Roll.NONE));
        bottomOnTopRotations.put(Side.BACK, Rotation.rotate(Yaw.CLOCKWISE_180, Pitch.CLOCKWISE_180, Roll.NONE));
        bottomOnTopRotations.put(Side.RIGHT, Rotation.rotate(Yaw.CLOCKWISE_270, Pitch.CLOCKWISE_180, Roll.NONE));
        BLOCK_ROTATIONS.put(Side.BOTTOM, bottomOnTopRotations);
    }

    public DiceBlockFamily(BlockFamilyDefinition definition, BlockShape shape, BlockBuilderHelper blockBuilder) {
        super(definition, shape, blockBuilder);
        throw new UnsupportedOperationException("Freeform blocks not supported");
    }

    public DiceBlockFamily(BlockFamilyDefinition definition, BlockBuilderHelper blockBuilder) {
        super(definition, blockBuilder);
        for (Side topSide : BLOCK_ROTATIONS.keySet()) {
            Map<Side, Rotation> frontSides = BLOCK_ROTATIONS.get(topSide);
            for (Side frontSide : frontSides.keySet()) {
                Rotation rotation = frontSides.get(frontSide);
                Block block = blockBuilder.constructTransformedBlock(definition, rotation,new BlockUri(getURI(),new Name(topSide.name() + "-" + frontSide.name())),this);
                blocks.add(block);
                if (!blockMapping.containsKey(topSide)) {
                    blockMapping.put(topSide, block);
                }
                if (topSide == Side.TOP && frontSide == Side.FRONT) {
                    archetype = block;
                }
            }
        }
    }

    @Override
    public Block getBlockForPlacement(BlockPlacementData data) {
        return blockMapping.get(data.attachmentSide);
    }

    @Override
    public Block getArchetypeBlock() {
        return archetype;
    }

    @Override
    public Block getBlockFor(BlockUri blockUri) {
        if (getURI().equals(blockUri.getFamilyUri())) {
            try {
                Side side = Side.valueOf(blockUri.getIdentifier().toString().toUpperCase(Locale.ENGLISH));
                return blockMapping.get(side);
            } catch (IllegalArgumentException e) {
                return null;
            }
        }
        return null;
    }

    @Override
    public Iterable<Block> getBlocks() {
        return blocks;
    }
}
