package com.loto.baseloto.block;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockPortal;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockWorldState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.block.state.pattern.BlockPattern;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.Teleporter;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import com.google.common.cache.LoadingCache;
import com.loto.baseloto.dimension.DimensionOverlord.OverlordRealmTeleporter;
import com.loto.baseloto.reg.CreateBlocks;
public class BlockOverlordPortal extends BlockPortal {
	
    @Override
    public boolean trySpawnPortal(World worldIn, BlockPos pos) {
        BlockOverlordPortal.Size blockportal$size = new BlockOverlordPortal.Size(worldIn, pos, EnumFacing.Axis.X);
        if (blockportal$size.isValid() && blockportal$size.portalBlockCount == 0) {
            blockportal$size.placePortalBlocks();
            
            worldIn.playSound((double) pos.getX() + 0.5D, (double) pos.getY() + 0.5D, (double) pos.getZ() + 0.5D,
                    net.minecraft.util.SoundEvent.REGISTRY.getObject(new ResourceLocation(
                            ("block.end_portal.spawn"))), SoundCategory.BLOCKS, 0.5F, 1.0f, false);
            
            return true;
        } else {
            BlockOverlordPortal.Size blockportal$size1 = new BlockOverlordPortal.Size(worldIn, pos, EnumFacing.Axis.Z);
            if (blockportal$size1.isValid() && blockportal$size1.portalBlockCount == 0) {
                blockportal$size1.placePortalBlocks();
                worldIn.playSound((double) pos.getX() + 0.5D, (double) pos.getY() + 0.5D, (double) pos.getZ() + 0.5D,
                        net.minecraft.util.SoundEvent.REGISTRY.getObject(new ResourceLocation(
                                ("block.end_portal.spawn"))), SoundCategory.BLOCKS, 0.5F, 1.0f, false);
                return true;
            } else {
                return false;
            }
        }
    }

    @Override
    public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos) {
        EnumFacing.Axis enumfacing$axis = state.getValue(AXIS);
        if (enumfacing$axis == EnumFacing.Axis.X) {
            Size blockportal$size = new Size(worldIn, pos, EnumFacing.Axis.X);
            if (!blockportal$size.isValid() || blockportal$size.portalBlockCount < blockportal$size.width * blockportal$size.height) {
                worldIn.setBlockState(pos, Blocks.AIR.getDefaultState());
                portalDestroyed(worldIn, pos);
            }
        } else if (enumfacing$axis == EnumFacing.Axis.Z) {
            Size blockportal$size1 = new Size(worldIn, pos, EnumFacing.Axis.Z);
            if (!blockportal$size1.isValid() || blockportal$size1.portalBlockCount < blockportal$size1.width * blockportal$size1.height) {
                worldIn.setBlockState(pos, Blocks.AIR.getDefaultState());
                portalDestroyed(worldIn, pos);
            }
        }
    }

    private void portalDestroyed(World world, BlockPos pos) {
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void randomDisplayTick(IBlockState stateIn, World world, BlockPos pos, Random rand) {
        if (rand.nextInt(100) == 0)
            world.playSound((double) pos.getX() + 0.5D, (double) pos.getY() + 0.5D, (double) pos.getZ() + 0.5D,
                    net.minecraft.util.SoundEvent.REGISTRY.getObject(new ResourceLocation(
                            ("block.portal.ambient"))), SoundCategory.BLOCKS, 0.5F, rand.nextFloat() * 0.4F + 0.8F, false);
        for (int i = 0; i < 4; ++i) {
            double d0 = (double) ((float) pos.getX() + rand.nextFloat());
            double d1 = (double) ((float) pos.getY() + rand.nextFloat());
            double d2 = (double) ((float) pos.getZ() + rand.nextFloat());
            double d3 = ((double) rand.nextFloat() - 0.5D) * 0.5D;
            double d4 = ((double) rand.nextFloat() - 0.5D) * 0.5D;
            double d5 = ((double) rand.nextFloat() - 0.5D) * 0.5D;
            int j = rand.nextInt(2) * 2 - 1;
            if (world.getBlockState(pos.west()).getBlock() != this && world.getBlockState(pos.east()).getBlock() != this) {
                d0 = pos.getX() + 0.5D + 0.25D * (double) j;
                d3 = (double) (rand.nextFloat() * 2.0F * (float) j);
            } else {
                d2 = pos.getZ() + 0.5D + 0.25D * (double) j;
                d5 = (double) (rand.nextFloat() * 2.0F * (float) j);
            }
            world.spawnParticle(EnumParticleTypes.SMOKE_LARGE, d0, d1, d2, d3, d4, d5);
        }
    }

    public BlockPattern.PatternHelper createPatternHelper(World worldIn, BlockPos pos) {
        EnumFacing.Axis enumfacing$axis = EnumFacing.Axis.Z;
        BlockOverlordPortal.Size blockportal$size = new BlockOverlordPortal.Size(worldIn, pos, EnumFacing.Axis.X);
        LoadingCache<BlockPos, BlockWorldState> loadingcache = BlockPattern.createLoadingCache(worldIn, true);
        if (!blockportal$size.isValid()) {
            enumfacing$axis = EnumFacing.Axis.X;
            blockportal$size = new BlockOverlordPortal.Size(worldIn, pos, EnumFacing.Axis.Z);
        }
        if (!blockportal$size.isValid()) {
            return new BlockPattern.PatternHelper(pos, EnumFacing.NORTH, EnumFacing.UP, loadingcache, 1, 1, 1);
        } else {
            int[] aint = new int[EnumFacing.AxisDirection.values().length];
            EnumFacing enumfacing = blockportal$size.rightDir.rotateYCCW();
            BlockPos blockpos = blockportal$size.bottomLeft.up(blockportal$size.getHeight() - 1);
            for (EnumFacing.AxisDirection enumfacing$axisdirection : EnumFacing.AxisDirection.values()) {
                BlockPattern.PatternHelper blockpattern$patternhelper = new BlockPattern.PatternHelper(
                        enumfacing.getAxisDirection() == enumfacing$axisdirection ? blockpos : blockpos.offset(blockportal$size.rightDir,
                                blockportal$size.getWidth() - 1), EnumFacing.getFacingFromAxis(enumfacing$axisdirection, enumfacing$axis),
                        EnumFacing.UP, loadingcache, blockportal$size.getWidth(), blockportal$size.getHeight(), 1);
                for (int i = 0; i < blockportal$size.getWidth(); ++i) {
                    for (int j = 0; j < blockportal$size.getHeight(); ++j) {
                        BlockWorldState blockworldstate = blockpattern$patternhelper.translateOffset(i, j, 1);
                        if (blockworldstate.getBlockState() != null && blockworldstate.getBlockState().getMaterial() != Material.AIR) {
                            ++aint[enumfacing$axisdirection.ordinal()];
                        }
                    }
                }
            }
            EnumFacing.AxisDirection enumfacing$axisdirection1 = EnumFacing.AxisDirection.POSITIVE;
            for (EnumFacing.AxisDirection enumfacing$axisdirection2 : EnumFacing.AxisDirection.values()) {
                if (aint[enumfacing$axisdirection2.ordinal()] < aint[enumfacing$axisdirection1.ordinal()]) {
                    enumfacing$axisdirection1 = enumfacing$axisdirection2;
                }
            }
            return new BlockPattern.PatternHelper(enumfacing.getAxisDirection() == enumfacing$axisdirection1 ? blockpos : blockpos.offset(
                    blockportal$size.rightDir, blockportal$size.getWidth() - 1), EnumFacing.getFacingFromAxis(enumfacing$axisdirection1,
                    enumfacing$axis), EnumFacing.UP, loadingcache, blockportal$size.getWidth(), blockportal$size.getHeight(), 1);
        }
    }

    public void onEntityCollidedWithBlock(World worldIn, BlockPos pos, IBlockState state, Entity entityIn) {
        if (!worldIn.isRemote && !entityIn.isRiding() && !entityIn.isBeingRidden() && entityIn.isNonBoss()) {
            if (entityIn instanceof EntityPlayerMP) {
                EntityPlayerMP thePlayer = (EntityPlayerMP) entityIn;
                if (entityIn.timeUntilPortal > 0) {
                    entityIn.timeUntilPortal = 10;
                } else if (entityIn.dimension != 911) {
                    entityIn.timeUntilPortal = 10;
                    thePlayer.mcServer.getPlayerList().transferPlayerToDimension(thePlayer, 911,
                            getTeleporterForDimension(thePlayer, pos, 911));
                } else {
                    entityIn.timeUntilPortal = 10;
                    thePlayer.mcServer.getPlayerList().transferPlayerToDimension(thePlayer, 0, getTeleporterForDimension(thePlayer, pos, 0));
                }
            } else if (!(entityIn instanceof EntityPlayer) && !(entityIn instanceof EntityPlayerSP)) {
                if (entityIn.timeUntilPortal > 0) {
                    entityIn.timeUntilPortal = 10;
                } else if (entityIn.dimension != 911) {
                    entityIn.timeUntilPortal = 10;
                    entityIn.changeDimension(911, getTeleporterForDimension(entityIn, pos, 911));
                } else {
                    entityIn.timeUntilPortal = 10;
                    entityIn.changeDimension(911, getTeleporterForDimension(entityIn, pos, 0));
                }
            }
        }
    }

    private OverlordRealmTeleporter getTeleporterForDimension(Entity entity, BlockPos pos, int DIMID) {
        BlockPattern.PatternHelper blockpattern$patternhelper = createPatternHelper(entity.world, new BlockPos(pos));
        double d0 = blockpattern$patternhelper.getForwards().getAxis() == EnumFacing.Axis.X ? (double) blockpattern$patternhelper
                .getFrontTopLeft().getZ() : (double) blockpattern$patternhelper.getFrontTopLeft().getX();
        double d1 = blockpattern$patternhelper.getForwards().getAxis() == EnumFacing.Axis.X ? entity.posZ : entity.posX;
        d1 = Math.abs(MathHelper.pct(d1
                        - (double) (blockpattern$patternhelper.getForwards().rotateY().getAxisDirection() == EnumFacing.AxisDirection.NEGATIVE ? 1 : 0),
                d0, d0 - (double) blockpattern$patternhelper.getWidth()));
        double d2 = MathHelper.pct(entity.posY - 1.0D, (double) blockpattern$patternhelper.getFrontTopLeft().getY(),
                (double) (blockpattern$patternhelper.getFrontTopLeft().getY() - blockpattern$patternhelper.getHeight()));
        return new OverlordRealmTeleporter(entity.getServer().getWorld(DIMID), new Vec3d(d1, d2, 0.0D), blockpattern$patternhelper.getForwards());
    }

    class Size {
        private final World world;
        private final EnumFacing.Axis axis;
        private final EnumFacing rightDir;
        private final EnumFacing leftDir;
        private int portalBlockCount;
        private BlockPos bottomLeft;
        private int height;
        private int width;

        public Size(World worldIn, BlockPos p_i45694_2_, EnumFacing.Axis p_i45694_3_) {
            this.world = worldIn;
            this.axis = p_i45694_3_;
            if (p_i45694_3_ == EnumFacing.Axis.X) {
                this.leftDir = EnumFacing.EAST;
                this.rightDir = EnumFacing.WEST;
            } else {
                this.leftDir = EnumFacing.NORTH;
                this.rightDir = EnumFacing.SOUTH;
            }
            for (BlockPos blockpos = p_i45694_2_; p_i45694_2_.getY() > blockpos.getY() - 21 && p_i45694_2_.getY() > 0
                    && this.isEmptyBlock(worldIn.getBlockState(p_i45694_2_.down()).getBlock()); p_i45694_2_ = p_i45694_2_.down()) {
                ;
            }
            int i = this.getDistanceUntilEdge(p_i45694_2_, this.leftDir) - 1;
            if (i >= 0) {
                this.bottomLeft = p_i45694_2_.offset(this.leftDir, i);
                this.width = this.getDistanceUntilEdge(this.bottomLeft, this.rightDir);
                if (this.width < 2 || this.width > 21) {
                    this.bottomLeft = null;
                    this.width = 0;
                }
            }
            if (this.bottomLeft != null) {
                this.height = this.calculatePortalHeight();
            }
        }

        protected int getDistanceUntilEdge(BlockPos p_180120_1_, EnumFacing p_180120_2_) {
            int i;
            for (i = 0; i < 22; ++i) {
                BlockPos blockpos = p_180120_1_.offset(p_180120_2_, i);
                if (!this.isEmptyBlock(this.world.getBlockState(blockpos).getBlock())
                        || this.world.getBlockState(blockpos.down()).getBlock() != Blocks.RED_NETHER_BRICK.getDefaultState().getBlock()) {
                    break;
                }
            }
            Block block = this.world.getBlockState(p_180120_1_.offset(p_180120_2_, i)).getBlock();
            return block == Blocks.RED_NETHER_BRICK.getDefaultState().getBlock() ? i : 0;
        }

        public int getHeight() {
            return this.height;
        }

        public int getWidth() {
            return this.width;
        }

        protected int calculatePortalHeight() {
            label56:
            for (this.height = 0; this.height < 21; ++this.height) {
                for (int i = 0; i < this.width; ++i) {
                    BlockPos blockpos = this.bottomLeft.offset(this.rightDir, i).up(this.height);
                    Block block = this.world.getBlockState(blockpos).getBlock();
                    if (!this.isEmptyBlock(block)) {
                        break label56;
                    }
                    if (block == CreateBlocks.overlordPortal) {
                        ++this.portalBlockCount;
                    }
                    if (i == 0) {
                        block = this.world.getBlockState(blockpos.offset(this.leftDir)).getBlock();
                        if (block != Blocks.RED_NETHER_BRICK.getDefaultState().getBlock()) {
                            break label56;
                        }
                    } else if (i == this.width - 1) {
                        block = this.world.getBlockState(blockpos.offset(this.rightDir)).getBlock();
                        if (block != Blocks.RED_NETHER_BRICK.getDefaultState().getBlock()) {
                            break label56;
                        }
                    }
                }
            }
            for (int j = 0; j < this.width; ++j) {
                if (this.world.getBlockState(this.bottomLeft.offset(this.rightDir, j).up(this.height)).getBlock() != Blocks.RED_NETHER_BRICK.getDefaultState().getBlock()) {
                    this.height = 0;
                    break;
                }
            }
            if (this.height <= 21 && this.height >= 3) {
                return this.height;
            } else {
                this.bottomLeft = null;
                this.width = 0;
                this.height = 0;
                return 0;
            }
        }

        protected boolean isEmptyBlock(Block blockIn) {
            return blockIn.getDefaultState().getMaterial() == Material.AIR || blockIn == Blocks.FIRE || blockIn == CreateBlocks.overlordPortal.getDefaultState();
        }

        public boolean isValid() {
            return this.bottomLeft != null && this.width >= 2 && this.width <= 21 && this.height >= 3 && this.height <= 21;
        }

        public void placePortalBlocks() {
            for (int i = 0; i < this.width; ++i) {
                BlockPos blockpos = this.bottomLeft.offset(this.rightDir, i);
                for (int j = 0; j < this.height; ++j) {
                    this.world.setBlockState(blockpos.up(j), CreateBlocks.overlordPortal.getDefaultState().withProperty(BlockPortal.AXIS, this.axis), 2);
                }
            }
        }
    }
}