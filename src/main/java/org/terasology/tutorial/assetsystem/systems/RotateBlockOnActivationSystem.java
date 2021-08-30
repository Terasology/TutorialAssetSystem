package org.terasology.tutorial.assetsystem.systems;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.terasology.engine.entitySystem.entity.EntityRef;
import org.terasology.engine.entitySystem.event.ReceiveEvent;
import org.terasology.engine.entitySystem.systems.BaseComponentSystem;
import org.terasology.engine.entitySystem.systems.RegisterMode;
import org.terasology.engine.entitySystem.systems.RegisterSystem;
import org.terasology.engine.logic.common.ActivateEvent;
import org.terasology.engine.math.Side;
import org.terasology.engine.registry.In;
import org.terasology.tutorial.assetsystem.components.RotateBlockOnActivateComponent;
import org.terasology.engine.utilities.random.FastRandom;
import org.terasology.engine.utilities.random.Random;
import org.terasology.engine.world.WorldProvider;
import org.terasology.engine.world.block.Block;
import org.terasology.engine.world.block.BlockComponent;

import com.google.common.collect.Lists;

@RegisterSystem(RegisterMode.AUTHORITY)
public class RotateBlockOnActivationSystem extends BaseComponentSystem {

    @In
    private WorldProvider worldProvider;

    private Random random = new FastRandom();

    private static final Logger LOG = LoggerFactory.getLogger(RotateBlockOnActivationSystem.class);

    @ReceiveEvent(components = {RotateBlockOnActivateComponent.class, BlockComponent.class})
    public void onActivate(ActivateEvent event, EntityRef entity) {
        BlockComponent blockComponent = entity.getComponent(BlockComponent.class);
        Block block = blockComponent.block;
        List<Block> blocks = Lists.newArrayList(block.getBlockFamily().getBlocks());
        //-1 because we omit the block with the same rotation as the current one
        int index = random.nextInt(blocks.size() - 1);
        Side currentDirection = block.getDirection();
        for (int i = 0; i < blocks.size(); i++) {
            Block newBlock = blocks.get(i);
            if (newBlock.getDirection() != currentDirection) {
                index--;
            }
            if (index == 0) {
                LOG.info(newBlock.getURI().toString());
                worldProvider.setBlock(blockComponent.getPosition(), newBlock);
                return;
            }
        }
    }
}
