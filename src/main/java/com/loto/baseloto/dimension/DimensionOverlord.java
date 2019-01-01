package com.loto.baseloto.dimension;

import java.util.List;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockFalling;
import net.minecraft.block.BlockPortal;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.block.state.pattern.BlockMatcher;
import net.minecraft.block.state.pattern.BlockPattern;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Mirror;
import net.minecraft.util.ReportedException;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.DimensionType;
import net.minecraft.world.Teleporter;
import net.minecraft.world.World;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.WorldServer;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeCache;
import net.minecraft.world.biome.BiomeProvider;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.ChunkPrimer;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraft.world.gen.MapGenBase;
import net.minecraft.world.gen.MapGenCavesHell;
import net.minecraft.world.gen.NoiseGeneratorOctaves;
import net.minecraft.world.gen.feature.WorldGenFire;
import net.minecraft.world.gen.feature.WorldGenGlowStone1;
import net.minecraft.world.gen.feature.WorldGenGlowStone2;
import net.minecraft.world.gen.feature.WorldGenMinable;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.minecraft.world.gen.layer.GenLayer;
import net.minecraft.world.gen.layer.GenLayerVoronoiZoom;
import net.minecraft.world.gen.layer.GenLayerZoom;
import net.minecraft.world.gen.layer.IntCache;
import net.minecraft.world.gen.structure.template.PlacementSettings;
import net.minecraft.world.gen.structure.template.Template;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import com.loto.baseloto.DrewMod;
import com.loto.baseloto.reg.CreateBiomes;
import com.loto.baseloto.reg.CreateBlocks;

public class DimensionOverlord {

	public static int DIMID = 911;

	public static class OverlordWorldProvider extends WorldProvider {
		@Override
		public void init() {
			this.biomeProvider = new OverlordRealmBiomeProvider(
					this.world.getSeed());
		}

		public DimensionType getDimensionType() {
			return DrewMod.overlordRealmDimensionType;
		}

		@Override
		@SideOnly(Side.CLIENT)
		public Vec3d getFogColor(float par1, float par2) {
			return new Vec3d(0.6, 0, 0);
		}

		@Override
		public IChunkGenerator createChunkGenerator() {
			return new OverlordChunkProvider(this.world, this.world.getSeed()
					- DIMID);
		}

		@Override
		public boolean isSurfaceWorld() {
			return false;
		}

		@Override
		public boolean canRespawnHere() {
			return false;
		}

		@SideOnly(Side.CLIENT)
		@Override
		public boolean doesXZShowFog(int par1, int par2) {
			return true;
		}

		@Override
		protected void generateLightBrightnessTable() {
			float f = 0.5F;
			for (int i = 0; i <= 15; ++i) {
				float f1 = 1.0F - (float) i / 15.0F;
				this.lightBrightnessTable[i] = (1.0F - f1) / (f1 * 3.0F + 1.0F)
						* (1.0F - f) + f;
			}
		}
	}

	public static class GenLayerFix {

		public static GenLayer[] makeTheWorld(long seed) {
			GenLayer biomes = new GenLayerBiomesOverlordRealm(1L);
			biomes = new GenLayerZoom(1000L, biomes);
			biomes = new GenLayerZoom(1001L, biomes);
			biomes = new GenLayerZoom(1002L, biomes);
			biomes = new GenLayerZoom(1003L, biomes);
			biomes = new GenLayerZoom(1004L, biomes);
			biomes = new GenLayerZoom(1005L, biomes);
			GenLayer genLayerVoronoiZoom = new GenLayerVoronoiZoom(10L, biomes);
			biomes.initWorldGenSeed(seed);
			genLayerVoronoiZoom.initWorldGenSeed(seed);
			return new GenLayer[] { biomes, genLayerVoronoiZoom };
		}
	}

	public static class GenLayerBiomesOverlordRealm extends GenLayer {

		private Biome[] allowedBiomes = { CreateBiomes.overlordRealmBiome };

		public GenLayerBiomesOverlordRealm(long seed) {
			super(seed);
		}

		@Override
		public int[] getInts(int x, int z, int width, int depth) {
			int[] dest = IntCache.getIntCache(width * depth);
			for (int dz = 0; dz < depth; dz++) {
				for (int dx = 0; dx < width; dx++) {
					this.initChunkSeed(dx + x, dz + z);
					dest[(dx + dz * width)] = Biome
							.getIdForBiome(this.allowedBiomes[nextInt(this.allowedBiomes.length)]);
				}
			}
			return dest;
		}
	}

	public static class OverlordRealmBiomeProvider extends BiomeProvider {
		private GenLayer genBiomes;
		private GenLayer biomeIndexLayer;
		private BiomeCache biomeCache;

		public OverlordRealmBiomeProvider() {
			this.biomeCache = new BiomeCache(this);
		}

		public OverlordRealmBiomeProvider(long seed) {
			this();
			GenLayer[] agenlayer = GenLayerFix.makeTheWorld(seed);
			this.genBiomes = agenlayer[0];
			this.biomeIndexLayer = agenlayer[1];
		}

		public OverlordRealmBiomeProvider(World world) {
			this(world.getSeed());
		}

		@Override
		public Biome getBiome(BlockPos pos) {
			return this.getBiome(pos, null);
		}

		@Override
		public Biome getBiome(BlockPos pos, Biome defaultBiome) {
			return this.biomeCache.getBiome(pos.getX(), pos.getZ(),
					defaultBiome);
		}

		@Override
		public Biome[] getBiomesForGeneration(Biome[] par1ArrayOfBiome,
				int par2, int par3, int par4, int par5) {
			IntCache.resetIntCache();
			if (par1ArrayOfBiome == null
					|| par1ArrayOfBiome.length < par4 * par5) {
				par1ArrayOfBiome = new Biome[par4 * par5];
			}
			int[] aint = this.genBiomes.getInts(par2, par3, par4, par5);
			try {
				for (int i = 0; i < par4 * par5; ++i) {
					par1ArrayOfBiome[i] = Biome.getBiome(aint[i]);
				}
				return par1ArrayOfBiome;
			} catch (Throwable throwable) {
				CrashReport crashreport = CrashReport.makeCrashReport(
						throwable, "Invalid Biome id");
				CrashReportCategory crashreportcategory = crashreport
						.makeCategory("RawBiomeBlock");
				crashreportcategory.addCrashSection("biomes[] size",
						par1ArrayOfBiome.length);
				crashreportcategory.addCrashSection("x", par2);
				crashreportcategory.addCrashSection("z", par3);
				crashreportcategory.addCrashSection("w", par4);
				crashreportcategory.addCrashSection("h", par5);
				throw new ReportedException(crashreport);
			}
		}

		@Override
		public Biome[] getBiomes(Biome[] oldBiomeList, int x, int z, int width,
				int depth) {
			return this.getBiomes(oldBiomeList, x, z, width, depth, true);
		}

		@Override
		public Biome[] getBiomes(Biome[] listToReuse, int x, int y, int width,
				int length, boolean cacheFlag) {
			IntCache.resetIntCache();
			if (listToReuse == null || listToReuse.length < width * length) {
				listToReuse = new Biome[width * length];
			}
			if (cacheFlag && width == 16 && length == 16 && (x & 15) == 0
					&& (y & 15) == 0) {
				Biome[] aBiome1 = this.biomeCache.getCachedBiomes(x, y);
				System.arraycopy(aBiome1, 0, listToReuse, 0, width * length);
				return listToReuse;
			} else {
				int[] aint = this.biomeIndexLayer.getInts(x, y, width, length);
				for (int i = 0; i < width * length; ++i) {
					listToReuse[i] = Biome.getBiome(aint[i]);
				}
				return listToReuse;
			}
		}

		@Override
		public boolean areBiomesViable(int x, int z, int radius,
				List<Biome> allowed) {
			IntCache.resetIntCache();
			int i = x - radius >> 2;
			int j = z - radius >> 2;
			int k = x + radius >> 2;
			int l = z + radius >> 2;
			int i1 = k - i + 1;
			int j1 = l - j + 1;
			int[] aint = this.genBiomes.getInts(i, j, i1, j1);
			try {
				for (int k1 = 0; k1 < i1 * j1; ++k1) {
					Biome biome = Biome.getBiome(aint[k1]);
					if (!allowed.contains(biome)) {
						return false;
					}
				}
				return true;
			} catch (Throwable throwable) {
				CrashReport crashreport = CrashReport.makeCrashReport(
						throwable, "Invalid Biome id");
				CrashReportCategory crashreportcategory = crashreport
						.makeCategory("Layer");
				crashreportcategory.addCrashSection("Layer",
						this.genBiomes.toString());
				crashreportcategory.addCrashSection("x", x);
				crashreportcategory.addCrashSection("z", z);
				crashreportcategory.addCrashSection("radius", radius);
				crashreportcategory.addCrashSection("allowed", allowed);
				throw new ReportedException(crashreport);
			}
		}

		@Override
		@SuppressWarnings("rawtypes")
		public BlockPos findBiomePosition(int x, int z, int range, List biomes,
				Random random) {
			IntCache.resetIntCache();
			int l = x - range >> 2;
			int i1 = z - range >> 2;
			int j1 = x + range >> 2;
			int k1 = z + range >> 2;
			int l1 = j1 - l + 1;
			int i2 = k1 - i1 + 1;
			int[] aint = this.genBiomes.getInts(l, i1, l1, i2);
			BlockPos blockpos = null;
			int j2 = 0;
			for (int k2 = 0; k2 < l1 * i2; ++k2) {
				int l2 = l + k2 % l1 << 2;
				int i3 = i1 + k2 / l1 << 2;
				Biome biome = Biome.getBiome(aint[k2]);
				if (biomes.contains(biome)
						&& (blockpos == null || random.nextInt(j2 + 1) == 0)) {
					blockpos = new BlockPos(l2, 0, i3);
					++j2;
				}
			}
			return blockpos;
		}

		@Override
		public void cleanupCache() {
			this.biomeCache.cleanupCache();
		}
	}

	public static class OverlordRealmTeleporter extends Teleporter {
		private Vec3d lastPortalVec;
		private EnumFacing teleportDirection;

		public OverlordRealmTeleporter(WorldServer worldServer,
				Vec3d lastPortalVec, EnumFacing teleportDirection) {
			super(worldServer);
			this.lastPortalVec = lastPortalVec;
			this.teleportDirection = teleportDirection;
		}

		@Override
		public void placeInPortal(Entity entityIn, float rotationYaw) {
			if (this.world.provider.getDimensionType().getId() != 1) {
				if (!this.placeInExistingPortal(entityIn, rotationYaw)) {
					this.makePortal(entityIn);
					this.placeInExistingPortal(entityIn, rotationYaw);
				}
			} else {
				int i = MathHelper.floor(entityIn.posX);
				int j = MathHelper.floor(entityIn.posY) - 1;
				int k = MathHelper.floor(entityIn.posZ);
				int l = 1;
				int i1 = 0;
				for (int j1 = -2; j1 <= 2; ++j1) {
					for (int k1 = -2; k1 <= 2; ++k1) {
						for (int l1 = -1; l1 < 3; ++l1) {
							int i2 = i + k1;
							int j2 = j + l1;
							int k2 = k - j1;
							boolean flag = l1 < 0;
							this.world.setBlockState(
									new BlockPos(i2, j2, k2),
									flag ? CreateBlocks.overlordStone
											.getDefaultState() : Blocks.AIR
											.getDefaultState());
						}
					}
				}
				entityIn.setLocationAndAngles((double) i, (double) j,
						(double) k, entityIn.rotationYaw, 0.0F);
				entityIn.motionX = 0.0D;
				entityIn.motionY = 0.0D;
				entityIn.motionZ = 0.0D;
			}
		}

		@Override
		public boolean placeInExistingPortal(Entity entityIn, float rotationYaw) {
			int i = 128;
			double d0 = -1.0D;
			int j = MathHelper.floor(entityIn.posX);
			int k = MathHelper.floor(entityIn.posZ);
			boolean flag = true;
			BlockPos blockpos = BlockPos.ORIGIN;
			long l = ChunkPos.asLong(j, k);
			if (this.destinationCoordinateCache.containsKey(l)) {
				Teleporter.PortalPosition teleporter$portalposition = (Teleporter.PortalPosition) this.destinationCoordinateCache
						.get(l);
				d0 = 0.0D;
				blockpos = teleporter$portalposition;
				teleporter$portalposition.lastUpdateTime = this.world
						.getTotalWorldTime();
				flag = false;
			} else {
				BlockPos blockpos3 = new BlockPos(entityIn);
				for (int i1 = -128; i1 <= 128; ++i1) {
					BlockPos blockpos2;
					for (int j1 = -128; j1 <= 128; ++j1) {
						for (BlockPos blockpos1 = blockpos3.add(
								i1,
								this.world.getActualHeight() - 1
										- blockpos3.getY(), j1); blockpos1
								.getY() >= 0; blockpos1 = blockpos2) {
							blockpos2 = blockpos1.down();
							if (this.world.getBlockState(blockpos1).getBlock() == CreateBlocks.overlordPortal) {
								for (blockpos2 = blockpos1.down(); this.world
										.getBlockState(blockpos2).getBlock() == CreateBlocks.overlordPortal; blockpos2 = blockpos2
										.down()) {
									blockpos1 = blockpos2;
								}
								double d1 = blockpos1.distanceSq(blockpos3);
								if (d0 < 0.0D || d1 < d0) {
									d0 = d1;
									blockpos = blockpos1;
								}
							}
						}
					}
				}
			}
			if (d0 >= 0.0D) {
				if (flag) {
					this.destinationCoordinateCache.put(
							l,
							new Teleporter.PortalPosition(blockpos, this.world
									.getTotalWorldTime()));
				}
				double d5 = (double) blockpos.getX() + 0.5D;
				double d7 = (double) blockpos.getZ() + 0.5D;
				BlockPattern.PatternHelper blockpattern$patternhelper = ((BlockPortal) CreateBlocks.overlordPortal)
						.createPatternHelper(this.world, blockpos);
				boolean flag1 = blockpattern$patternhelper.getForwards()
						.rotateY().getAxisDirection() == EnumFacing.AxisDirection.NEGATIVE;
				double d2 = blockpattern$patternhelper.getForwards().getAxis() == EnumFacing.Axis.X ? (double) blockpattern$patternhelper
						.getFrontTopLeft().getZ()
						: (double) blockpattern$patternhelper.getFrontTopLeft()
								.getX();
				double d6 = (double) (blockpattern$patternhelper
						.getFrontTopLeft().getY() + 1)
						- lastPortalVec.y
						* (double) blockpattern$patternhelper.getHeight();
				if (flag1) {
					++d2;
				}
				if (blockpattern$patternhelper.getForwards().getAxis() == EnumFacing.Axis.X) {
					d7 = d2
							+ (1.0D - lastPortalVec.x)
							* blockpattern$patternhelper.getWidth()
							* blockpattern$patternhelper.getForwards()
									.rotateY().getAxisDirection().getOffset();
				} else {
					d5 = d2
							+ (1.0D - lastPortalVec.x)
							* blockpattern$patternhelper.getWidth()
							* blockpattern$patternhelper.getForwards()
									.rotateY().getAxisDirection().getOffset();
				}
				float f = 0.0F;
				float f1 = 0.0F;
				float f2 = 0.0F;
				float f3 = 0.0F;
				if (blockpattern$patternhelper.getForwards().getOpposite() == teleportDirection) {
					f = 1.0F;
					f1 = 1.0F;
				} else if (blockpattern$patternhelper.getForwards()
						.getOpposite() == teleportDirection.getOpposite()) {
					f = -1.0F;
					f1 = -1.0F;
				} else if (blockpattern$patternhelper.getForwards()
						.getOpposite() == teleportDirection.rotateY()) {
					f2 = 1.0F;
					f3 = -1.0F;
				} else {
					f2 = -1.0F;
					f3 = 1.0F;
				}
				double d3 = entityIn.motionX;
				double d4 = entityIn.motionZ;
				entityIn.motionX = d3 * (double) f + d4 * (double) f3;
				entityIn.motionZ = d3 * (double) f2 + d4 * (double) f1;
				entityIn.rotationYaw = rotationYaw
						- (float) (teleportDirection.getOpposite()
								.getHorizontalIndex() * 90)
						+ (float) (blockpattern$patternhelper.getForwards()
								.getHorizontalIndex() * 90);
				if (entityIn instanceof EntityPlayerMP) {
					((EntityPlayerMP) entityIn).connection.setPlayerLocation(
							d5, d6, d7, entityIn.rotationYaw,
							entityIn.rotationPitch);
				} else {
					entityIn.setLocationAndAngles(d5, d6, d7,
							entityIn.rotationYaw, entityIn.rotationPitch);
				}
				return true;
			} else {
				return false;
			}
		}

		@Override
		public boolean makePortal(Entity entityIn) {
			int i = 16;
			double d0 = -1.0D;
			int j = MathHelper.floor(entityIn.posX);
			int k = MathHelper.floor(entityIn.posY);
			int l = MathHelper.floor(entityIn.posZ);
			int i1 = j;
			int j1 = k;
			int k1 = l;
			int l1 = 0;
			int i2 = this.random.nextInt(4);
			BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();

			for (int j2 = j - 16; j2 <= j + 16; ++j2) {
				double d1 = (double) j2 + 0.5D - entityIn.posX;

				for (int l2 = l - 16; l2 <= l + 16; ++l2) {
					double d2 = (double) l2 + 0.5D - entityIn.posZ;
					label293:

					for (int j3 = this.world.getActualHeight() - 1; j3 >= 0; --j3) {
						if (this.world.isAirBlock(blockpos$mutableblockpos
								.setPos(j2, j3, l2))) {
							while (j3 > 0
									&& this.world
											.isAirBlock(blockpos$mutableblockpos
													.setPos(j2, j3 - 1, l2))) {
								--j3;
							}

							for (int k3 = i2; k3 < i2 + 4; ++k3) {
								int l3 = k3 % 2;
								int i4 = 1 - l3;

								if (k3 % 4 >= 2) {
									l3 = -l3;
									i4 = -i4;
								}

								for (int j4 = 0; j4 < 3; ++j4) {
									for (int k4 = 0; k4 < 4; ++k4) {
										for (int l4 = -1; l4 < 4; ++l4) {
											int i5 = j2 + (k4 - 1) * l3 + j4
													* i4;
											int j5 = j3 + l4;
											int k5 = l2 + (k4 - 1) * i4 - j4
													* l3;
											blockpos$mutableblockpos.setPos(i5,
													j5, k5);

											if (l4 < 0
													&& !this.world
															.getBlockState(
																	blockpos$mutableblockpos)
															.getMaterial()
															.isSolid()
													|| l4 >= 0
													&& !this.world
															.isAirBlock(blockpos$mutableblockpos)) {
												continue label293;
											}
										}
									}
								}

								double d5 = (double) j3 + 0.5D - entityIn.posY;
								double d7 = d1 * d1 + d5 * d5 + d2 * d2;

								if (d0 < 0.0D || d7 < d0) {
									d0 = d7;
									i1 = j2;
									j1 = j3;
									k1 = l2;
									l1 = k3 % 4;
								}
							}
						}
					}
				}
			}

			if (d0 < 0.0D) {
				for (int l5 = j - 16; l5 <= j + 16; ++l5) {
					double d3 = (double) l5 + 0.5D - entityIn.posX;

					for (int j6 = l - 16; j6 <= l + 16; ++j6) {
						double d4 = (double) j6 + 0.5D - entityIn.posZ;
						label231:

						for (int i7 = this.world.getActualHeight() - 1; i7 >= 0; --i7) {
							if (this.world.isAirBlock(blockpos$mutableblockpos
									.setPos(l5, i7, j6))) {
								while (i7 > 0
										&& this.world
												.isAirBlock(blockpos$mutableblockpos
														.setPos(l5, i7 - 1, j6))) {
									--i7;
								}

								for (int k7 = i2; k7 < i2 + 2; ++k7) {
									int j8 = k7 % 2;
									int j9 = 1 - j8;

									for (int j10 = 0; j10 < 4; ++j10) {
										for (int j11 = -1; j11 < 4; ++j11) {
											int j12 = l5 + (j10 - 1) * j8;
											int i13 = i7 + j11;
											int j13 = j6 + (j10 - 1) * j9;
											blockpos$mutableblockpos.setPos(
													j12, i13, j13);

											if (j11 < 0
													&& !this.world
															.getBlockState(
																	blockpos$mutableblockpos)
															.getMaterial()
															.isSolid()
													|| j11 >= 0
													&& !this.world
															.isAirBlock(blockpos$mutableblockpos)) {
												continue label231;
											}
										}
									}

									double d6 = (double) i7 + 0.5D
											- entityIn.posY;
									double d8 = d3 * d3 + d6 * d6 + d4 * d4;

									if (d0 < 0.0D || d8 < d0) {
										d0 = d8;
										i1 = l5;
										j1 = i7;
										k1 = j6;
										l1 = k7 % 2;
									}
								}
							}
						}
					}
				}
			}

			int i6 = i1;
			int k2 = j1;
			int k6 = k1;
			int l6 = l1 % 2;
			int i3 = 1 - l6;

			if (l1 % 4 >= 2) {
				l6 = -l6;
				i3 = -i3;
			}

			if (d0 < 0.0D) {
				j1 = MathHelper
						.clamp(j1, 70, this.world.getActualHeight() - 10);
				k2 = j1;

				for (int j7 = -1; j7 <= 1; ++j7) {
					for (int l7 = 1; l7 < 3; ++l7) {
						for (int k8 = -1; k8 < 3; ++k8) {
							int k9 = i6 + (l7 - 1) * l6 + j7 * i3;
							int k10 = k2 + k8;
							int k11 = k6 + (l7 - 1) * i3 - j7 * l6;
							boolean flag = k8 < 0;
							this.world.setBlockState(
									new BlockPos(k9, k10, k11),
									flag ? CreateBlocks.overlordStone
											.getDefaultState() : Blocks.AIR
											.getDefaultState());
						}
					}
				}
			}

			IBlockState iblockstate = CreateBlocks.overlordPortal
					.getDefaultState().withProperty(BlockPortal.AXIS,
							l6 == 0 ? EnumFacing.Axis.Z : EnumFacing.Axis.X);

			for (int i8 = 0; i8 < 4; ++i8) {
				for (int l8 = 0; l8 < 4; ++l8) {
					for (int l9 = -1; l9 < 4; ++l9) {
						int l10 = i6 + (l8 - 1) * l6;
						int l11 = k2 + l9;
						int k12 = k6 + (l8 - 1) * i3;
						boolean flag1 = l8 == 0 || l8 == 3 || l9 == -1
								|| l9 == 3;
						this.world.setBlockState(
								new BlockPos(l10, l11, k12),
								flag1 ? CreateBlocks.overlordStone
										.getDefaultState() : iblockstate, 2);
					}
				}

				for (int i9 = 0; i9 < 4; ++i9) {
					for (int i10 = -1; i10 < 4; ++i10) {
						int i11 = i6 + (i9 - 1) * l6;
						int i12 = k2 + i10;
						int l12 = k6 + (i9 - 1) * i3;
						BlockPos blockpos = new BlockPos(i11, i12, l12);
						this.world.notifyNeighborsOfStateChange(blockpos,
								this.world.getBlockState(blockpos).getBlock(),
								false);
					}
				}
			}

			return true;
		}
	}

	public static class OverlordChunkProvider implements IChunkGenerator {

		protected static final IBlockState STONE = CreateBlocks.overlordStone
				.getDefaultState();
		protected static final IBlockState LAVA = Blocks.LAVA.getDefaultState();
		protected static final IBlockState GRAVEL = Blocks.NETHERRACK
				.getDefaultState();
		protected static final IBlockState SOUL_SAND = Blocks.MAGMA
				.getDefaultState();
		protected static final IBlockState BEDROCK = Blocks.BEDROCK
				.getDefaultState();
		protected static final IBlockState AIR = Blocks.AIR.getDefaultState();
		private final WorldGenerator quartzGen = new WorldGenMinable(
				Blocks.QUARTZ_ORE.getDefaultState(), 14,
				BlockMatcher.forBlock(STONE.getBlock()));
		private final WorldGenerator magmaGen = new WorldGenMinable(STONE, 33,
				BlockMatcher.forBlock(STONE.getBlock()));
		private final World world;
		private final Random rand;
		private final WorldGenFire fireFeature = new WorldGenFire();
		private final WorldGenGlowStone1 lightGemGen = new WorldGenGlowStone1();
		private final WorldGenGlowStone2 hellPortalGen = new WorldGenGlowStone2();
		public NoiseGeneratorOctaves scaleNoise;
		public NoiseGeneratorOctaves depthNoise;
		double[] pnr;
		double[] ar;
		double[] br;
		double[] noiseData4;
		double[] dr;
		private double[] slowsandNoise = new double[256];
		private double[] gravelNoise = new double[256];
		private double[] depthBuffer = new double[256];
		private double[] buffer;
		private NoiseGeneratorOctaves lperlinNoise1;
		private NoiseGeneratorOctaves lperlinNoise2;
		private NoiseGeneratorOctaves perlinNoise1;
		private NoiseGeneratorOctaves slowsandGravelNoiseGen;
		private NoiseGeneratorOctaves netherrackExculsivityNoiseGen;
		private MapGenBase genNetherCaves;

		public OverlordChunkProvider(World worldIn, long seed) {
			this.world = worldIn;
			this.rand = new Random(seed);
			this.lperlinNoise1 = new NoiseGeneratorOctaves(this.rand, 16);
			this.lperlinNoise2 = new NoiseGeneratorOctaves(this.rand, 16);
			this.perlinNoise1 = new NoiseGeneratorOctaves(this.rand, 8);
			this.slowsandGravelNoiseGen = new NoiseGeneratorOctaves(this.rand,
					4);
			this.netherrackExculsivityNoiseGen = new NoiseGeneratorOctaves(
					this.rand, 4);
			this.scaleNoise = new NoiseGeneratorOctaves(this.rand, 10);
			this.depthNoise = new NoiseGeneratorOctaves(this.rand, 16);
			worldIn.setSeaLevel(63);
			net.minecraftforge.event.terraingen.InitNoiseGensEvent.ContextHell ctx = new net.minecraftforge.event.terraingen.InitNoiseGensEvent.ContextHell(
					lperlinNoise1, lperlinNoise2, perlinNoise1,
					slowsandGravelNoiseGen, netherrackExculsivityNoiseGen,
					scaleNoise, depthNoise);
			ctx = net.minecraftforge.event.terraingen.TerrainGen
					.getModdedNoiseGenerators(worldIn, this.rand, ctx);
			this.lperlinNoise1 = ctx.getLPerlin1();
			this.lperlinNoise2 = ctx.getLPerlin2();
			this.perlinNoise1 = ctx.getPerlin();
			this.slowsandGravelNoiseGen = ctx.getPerlin2();
			this.netherrackExculsivityNoiseGen = ctx.getPerlin3();
			this.scaleNoise = ctx.getScale();
			this.depthNoise = ctx.getDepth();
			this.genNetherCaves = net.minecraftforge.event.terraingen.TerrainGen
					.getModdedMapGen(
							new MapGenCavesHell(),
							net.minecraftforge.event.terraingen.InitMapGenEvent.EventType.NETHER_CAVE);
		}

		@Override
		public Chunk generateChunk(int x, int z) {
			this.rand.setSeed((long) x * 341873128712L + (long) z
					* 132897987541L);
			ChunkPrimer chunkprimer = new ChunkPrimer();
			this.prepareHeights(x, z, chunkprimer);
			this.buildSurfaces(x, z, chunkprimer);
			this.genNetherCaves.generate(this.world, x, z, chunkprimer);
			Chunk chunk = new Chunk(this.world, chunkprimer, x, z);
			Biome[] abiome = this.world.getBiomeProvider().getBiomes(
					(Biome[]) null, x * 16, z * 16, 16, 16);
			byte[] abyte = chunk.getBiomeArray();
			for (int i = 0; i < abyte.length; ++i) {
				abyte[i] = (byte) Biome.getIdForBiome(abiome[i]);
			}
			chunk.resetRelightChecks();
			return chunk;
		}

		@Override
		public void populate(int x, int z) {
			BlockFalling.fallInstantly = true;
			net.minecraftforge.event.ForgeEventFactory.onChunkPopulate(true,
					this, this.world, this.rand, x, z, false);
			int i = x * 16;
			int j = z * 16;
			BlockPos blockpos = new BlockPos(i, 0, j);
			Biome biome = this.world.getBiome(blockpos.add(16, 0, 16));
			ChunkPos chunkpos = new ChunkPos(x, z);
			if (net.minecraftforge.event.terraingen.TerrainGen
					.populate(
							this,
							this.world,
							this.rand,
							x,
							z,
							false,
							net.minecraftforge.event.terraingen.PopulateChunkEvent.Populate.EventType.FIRE))
				for (int i1 = 0; i1 < this.rand
						.nextInt(this.rand.nextInt(10) + 1) + 1; ++i1) {
					this.fireFeature.generate(
							this.world,
							this.rand,
							blockpos.add(this.rand.nextInt(16) + 8,
									this.rand.nextInt(120) + 4,
									this.rand.nextInt(16) + 8));
				}
			if (net.minecraftforge.event.terraingen.TerrainGen
					.populate(
							this,
							this.world,
							this.rand,
							x,
							z,
							false,
							net.minecraftforge.event.terraingen.PopulateChunkEvent.Populate.EventType.GLOWSTONE)) {
				for (int j1 = 0; j1 < this.rand
						.nextInt(this.rand.nextInt(10) + 1); ++j1) {
					this.lightGemGen.generate(
							this.world,
							this.rand,
							blockpos.add(this.rand.nextInt(16) + 8,
									this.rand.nextInt(120) + 4,
									this.rand.nextInt(16) + 8));
				}
				for (int k1 = 0; k1 < 10; ++k1) {
					this.hellPortalGen.generate(
							this.world,
							this.rand,
							blockpos.add(this.rand.nextInt(16) + 8,
									this.rand.nextInt(128),
									this.rand.nextInt(16) + 8));
				}
			}
			net.minecraftforge.event.ForgeEventFactory.onChunkPopulate(false,
					this, this.world, this.rand, x, z, false);
			net.minecraftforge.common.MinecraftForge.EVENT_BUS
					.post(new net.minecraftforge.event.terraingen.DecorateBiomeEvent.Pre(
							this.world, this.rand, blockpos));
			if (net.minecraftforge.event.terraingen.TerrainGen
					.generateOre(
							this.world,
							this.rand,
							quartzGen,
							blockpos,
							net.minecraftforge.event.terraingen.OreGenEvent.GenerateMinable.EventType.QUARTZ))
				for (int l1 = 0; l1 < 16; ++l1) {
					this.quartzGen.generate(
							this.world,
							this.rand,
							blockpos.add(this.rand.nextInt(16),
									this.rand.nextInt(108) + 10,
									this.rand.nextInt(16)));
				}
			int i22 = this.world.getSeaLevel() / 2 + 1;
			if (net.minecraftforge.event.terraingen.TerrainGen
					.populate(
							this,
							this.world,
							this.rand,
							x,
							z,
							false,
							net.minecraftforge.event.terraingen.PopulateChunkEvent.Populate.EventType.NETHER_MAGMA))
				for (int l = 0; l < 4; ++l) {
					this.magmaGen.generate(
							this.world,
							this.rand,
							blockpos.add(this.rand.nextInt(16), i22 - 5
									+ this.rand.nextInt(10),
									this.rand.nextInt(16)));
				}
			biome.decorate(this.world, this.rand, new BlockPos(i, 0, j));
			{
				if ((rand.nextInt(1000000) + 1) <= 10000) {
					int i2 = i + rand.nextInt(16) + 8;
					int k2 = j + rand.nextInt(16) + 8;
					int height = 255;
					boolean notpassed = true;
					while (height > 0) {
						if (notpassed
								&& world.isAirBlock(new BlockPos(i2, height, k2))) {
							notpassed = false;
						} else if (!notpassed
								&& !world.isAirBlock(new BlockPos(i2, height,
										k2)))
							break;
						height--;
					}
					int j2 = Math.abs(rand.nextInt(Math.abs(height)) - 24);
					Template template = ((WorldServer) world)
							.getStructureTemplateManager().getTemplate(
									world.getMinecraftServer(),
									new ResourceLocation("baseloto",
											"overlordrealm_ambient_1"));
					if (template == null)
						return;
					BlockPos spawnTo = new BlockPos(i2, j2, k2);
					IBlockState iblockstate = world.getBlockState(spawnTo);
					world.notifyBlockUpdate(spawnTo, iblockstate, iblockstate,
							3);
					template.addBlocksToWorldChunk(
							world,
							spawnTo,
							new PlacementSettings().setRotation(Rotation.NONE)
									.setMirror(Mirror.NONE)
									.setChunk((ChunkPos) null)
									.setReplacedBlock((Block) null)
									.setIgnoreStructureBlock(false)
									.setIgnoreEntities(false));
				}
			}
			biome.decorate(this.world, this.rand, new BlockPos(i, 0, j));
			{
				if ((rand.nextInt(1000000) + 1) <= 10000) {
					int i2 = i + rand.nextInt(16) + 8;
					int k2 = j + rand.nextInt(16) + 8;
					int height = 255;
					boolean notpassed = true;
					while (height > 0) {
						if (notpassed
								&& world.isAirBlock(new BlockPos(i2, height, k2))) {
							notpassed = false;
						} else if (!notpassed
								&& !world.isAirBlock(new BlockPos(i2, height,
										k2)))
							break;
						height--;
					}
					int j2 = Math.abs(rand.nextInt(Math.abs(height)) - 24);
					Template template = ((WorldServer) world)
							.getStructureTemplateManager().getTemplate(
									world.getMinecraftServer(),
									new ResourceLocation("baseloto",
											"overlordrealm_ambient_2"));
					if (template == null)
						return;
					BlockPos spawnTo = new BlockPos(i2, j2, k2);
					IBlockState iblockstate = world.getBlockState(spawnTo);
					world.notifyBlockUpdate(spawnTo, iblockstate, iblockstate,
							3);
					template.addBlocksToWorldChunk(
							world,
							spawnTo,
							new PlacementSettings().setRotation(Rotation.NONE)
									.setMirror(Mirror.NONE)
									.setChunk((ChunkPos) null)
									.setReplacedBlock((Block) null)
									.setIgnoreStructureBlock(false)
									.setIgnoreEntities(false));
				}
			}
			biome.decorate(this.world, this.rand, new BlockPos(i, 0, j));
			{
				if ((rand.nextInt(1000000) + 1) <= 10000) {
					int i2 = i + rand.nextInt(16) + 8;
					int k2 = j + rand.nextInt(16) + 8;
					int height = 255;
					boolean notpassed = true;
					while (height > 0) {
						if (notpassed
								&& world.isAirBlock(new BlockPos(i2, height, k2))) {
							notpassed = false;
						} else if (!notpassed
								&& !world.isAirBlock(new BlockPos(i2, height,
										k2)))
							break;
						height--;
					}
					int j2 = Math.abs(rand.nextInt(Math.abs(height)) - 24);
					Template template = ((WorldServer) world)
							.getStructureTemplateManager().getTemplate(
									world.getMinecraftServer(),
									new ResourceLocation("baseloto",
											"overlordrealm_dungeon_1"));
					if (template == null)
						return;
					BlockPos spawnTo = new BlockPos(i2, j2, k2);
					IBlockState iblockstate = world.getBlockState(spawnTo);
					world.notifyBlockUpdate(spawnTo, iblockstate, iblockstate,
							3);
					template.addBlocksToWorldChunk(
							world,
							spawnTo,
							new PlacementSettings().setRotation(Rotation.NONE)
									.setMirror(Mirror.NONE)
									.setChunk((ChunkPos) null)
									.setReplacedBlock((Block) null)
									.setIgnoreStructureBlock(false)
									.setIgnoreEntities(false));
				}
			}

			net.minecraftforge.common.MinecraftForge.EVENT_BUS
					.post(new net.minecraftforge.event.terraingen.DecorateBiomeEvent.Post(
							this.world, this.rand, blockpos));
			BlockFalling.fallInstantly = false;
		}

		@Override
		public List<Biome.SpawnListEntry> getPossibleCreatures(
				EnumCreatureType creatureType, BlockPos pos) {
			Biome biome = this.world.getBiome(pos);
			return biome.getSpawnableList(creatureType);
		}

		@Override
		public void recreateStructures(Chunk chunkIn, int x, int z) {
		}

		@Override
		public boolean isInsideStructure(World worldIn, String structureName,
				BlockPos pos) {
			return false;
		}

		@Override
		public BlockPos getNearestStructurePos(World worldIn,
				String structureName, BlockPos position, boolean findUnexplored) {
			return null;
		}

		@Override
		public boolean generateStructures(Chunk chunkIn, int x, int z) {
			return false;
		}

		private double[] getHeights(double[] p_185938_1_, int p_185938_2_,
				int p_185938_3_, int p_185938_4_, int p_185938_5_,
				int p_185938_6_, int p_185938_7_) {
			if (p_185938_1_ == null) {
				p_185938_1_ = new double[p_185938_5_ * p_185938_6_
						* p_185938_7_];
			}
			net.minecraftforge.event.terraingen.ChunkGeneratorEvent.InitNoiseField event = new net.minecraftforge.event.terraingen.ChunkGeneratorEvent.InitNoiseField(
					this, p_185938_1_, p_185938_2_, p_185938_3_, p_185938_4_,
					p_185938_5_, p_185938_6_, p_185938_7_);
			net.minecraftforge.common.MinecraftForge.EVENT_BUS.post(event);
			if (event.getResult() == net.minecraftforge.fml.common.eventhandler.Event.Result.DENY)
				return event.getNoisefield();
			double d0 = 684.412D;
			double d1 = 2053.236D;
			this.noiseData4 = this.scaleNoise.generateNoiseOctaves(
					this.noiseData4, p_185938_2_, p_185938_3_, p_185938_4_,
					p_185938_5_, 1, p_185938_7_, 1.0D, 0.0D, 1.0D);
			this.dr = this.depthNoise.generateNoiseOctaves(this.dr,
					p_185938_2_, p_185938_3_, p_185938_4_, p_185938_5_, 1,
					p_185938_7_, 10.0D, 5.0D, 12.0D);
			this.pnr = this.perlinNoise1.generateNoiseOctaves(this.pnr,
					p_185938_2_, p_185938_3_, p_185938_4_, p_185938_5_,
					p_185938_6_, p_185938_7_, 21D, 24.2206D, 4D);
			this.ar = this.lperlinNoise1.generateNoiseOctaves(this.ar,
					p_185938_2_, p_185938_3_, p_185938_4_, p_185938_5_,
					p_185938_6_, p_185938_7_, 63.412D, 25.236D, 53.412D);
			this.br = this.lperlinNoise2.generateNoiseOctaves(this.br,
					p_185938_2_, p_185938_3_, p_185938_4_, p_185938_5_,
					p_185938_6_, p_185938_7_, 20.412D, 1.236D, 40.412D);
			int i = 0;
			double[] adouble = new double[p_185938_6_];
			for (int j = 0; j < p_185938_6_; ++j) {
				adouble[j] = Math.cos((double) j * Math.PI * 4.0D
						/ (double) p_185938_6_) * 2.0D;
				double d2 = (double) j;
				if (j > p_185938_6_ / 2) {
					d2 = (double) (p_185938_6_ - 1 - j);
				}
				if (d2 < 4.0D) {
					d2 = 4.0D - d2;
					adouble[j] -= d2 * d2 * d2 * 10.0D;
				}
			}
			for (int l = 0; l < p_185938_5_; ++l) {
				for (int i1 = 0; i1 < p_185938_7_; ++i1) {
					double d3 = 0.0D;
					for (int k = 0; k < p_185938_6_; ++k) {
						double d4 = adouble[k];
						double d5 = this.ar[i] / 512.0D;
						double d6 = this.br[i] / 512.0D;
						double d7 = (this.pnr[i] / 10.0D + 1.0D) / 2.0D;
						double d8;
						if (d7 < 0.0D) {
							d8 = d5;
						} else if (d7 > 1.0D) {
							d8 = d6;
						} else {
							d8 = d5 + (d6 - d5) * d7;
						}
						d8 = d8 - d4;
						if (k > p_185938_6_ - 4) {
							double d9 = (double) ((float) (k - (p_185938_6_ - 4)) / 3.0F);
							d8 = d8 * (1.0D - d9) + -10.0D * d9;
						}
						if ((double) k < 0.0D) {
							double d10 = (0.0D - (double) k) / 4.0D;
							d10 = MathHelper.clamp(d10, 0.0D, 1.0D);
							d8 = d8 * (1.0D - d10) + -10.0D * d10;
						}
						p_185938_1_[i] = d8;
						++i;
					}
				}
			}
			return p_185938_1_;
		}

		private void prepareHeights(int p_185936_1_, int p_185936_2_,
				ChunkPrimer primer) {
			int i = 4;
			int j = this.world.getSeaLevel() / 2 + 1;
			int k = 5;
			int l = 17;
			int i1 = 5;
			this.buffer = this.getHeights(this.buffer, p_185936_1_ * 4, 0,
					p_185936_2_ * 4, 5, 17, 5);
			for (int j1 = 0; j1 < 4; ++j1) {
				for (int k1 = 0; k1 < 4; ++k1) {
					for (int l1 = 0; l1 < 16; ++l1) {
						double d0 = 0.125D;
						double d1 = this.buffer[((j1 + 0) * 5 + k1 + 0) * 17
								+ l1 + 0];
						double d2 = this.buffer[((j1 + 0) * 5 + k1 + 1) * 17
								+ l1 + 0];
						double d3 = this.buffer[((j1 + 1) * 5 + k1 + 0) * 17
								+ l1 + 0];
						double d4 = this.buffer[((j1 + 1) * 5 + k1 + 1) * 17
								+ l1 + 0];
						double d5 = (this.buffer[((j1 + 0) * 5 + k1 + 0) * 17
								+ l1 + 1] - d1) * 0.125D;
						double d6 = (this.buffer[((j1 + 0) * 5 + k1 + 1) * 17
								+ l1 + 1] - d2) * 0.125D;
						double d7 = (this.buffer[((j1 + 1) * 5 + k1 + 0) * 17
								+ l1 + 1] - d3) * 0.125D;
						double d8 = (this.buffer[((j1 + 1) * 5 + k1 + 1) * 17
								+ l1 + 1] - d4) * 0.125D;
						for (int i2 = 0; i2 < 8; ++i2) {
							double d9 = 0.25D;
							double d10 = d1;
							double d11 = d2;
							double d12 = (d3 - d1) * 0.25D;
							double d13 = (d4 - d2) * 0.25D;
							for (int j2 = 0; j2 < 4; ++j2) {
								double d14 = 0.25D;
								double d15 = d10;
								double d16 = (d11 - d10) * 0.25D;
								for (int k2 = 0; k2 < 4; ++k2) {
									IBlockState iblockstate = null;
									if (l1 * 8 + i2 < j) {
										iblockstate = LAVA;
									}
									if (d15 > 0.0D) {
										iblockstate = STONE;
									}
									int l2 = j2 + j1 * 4;
									int i3 = i2 + l1 * 8;
									int j3 = k2 + k1 * 4;
									primer.setBlockState(l2, i3, j3,
											iblockstate);
									d15 += d16;
								}
								d10 += d12;
								d11 += d13;
							}
							d1 += d5;
							d2 += d6;
							d3 += d7;
							d4 += d8;
						}
					}
				}
			}
		}

		private void buildSurfaces(int p_185937_1_, int p_185937_2_,
				ChunkPrimer primer) {
			if (!net.minecraftforge.event.ForgeEventFactory
					.onReplaceBiomeBlocks(this, p_185937_1_, p_185937_2_,
							primer, this.world))
				return;
			int i = this.world.getSeaLevel() + 1;
			double d0 = 0.03125D;
			this.slowsandNoise = this.slowsandGravelNoiseGen
					.generateNoiseOctaves(this.slowsandNoise, p_185937_1_ * 16,
							p_185937_2_ * 16, 0, 16, 16, 1, 0.03125D, 0.03125D,
							1.0D);
			this.gravelNoise = this.slowsandGravelNoiseGen
					.generateNoiseOctaves(this.gravelNoise, p_185937_1_ * 16,
							109, p_185937_2_ * 16, 16, 1, 16, 0.03125D, 1.0D,
							0.03125D);
			this.depthBuffer = this.netherrackExculsivityNoiseGen
					.generateNoiseOctaves(this.depthBuffer, p_185937_1_ * 16,
							p_185937_2_ * 16, 0, 16, 16, 1, 0.0625D, 0.0625D,
							0.0625D);
			for (int j = 0; j < 16; ++j) {
				for (int k = 0; k < 16; ++k) {
					boolean flag = this.slowsandNoise[j + k * 16]
							+ this.rand.nextDouble() * 0.2D > 0.0D;
					boolean flag1 = this.gravelNoise[j + k * 16]
							+ this.rand.nextDouble() * 0.2D > 0.0D;
					int l = (int) (this.depthBuffer[j + k * 16] / 3.0D + 3.0D + this.rand
							.nextDouble() * 0.25D);
					int i1 = -1;
					IBlockState iblockstate = STONE;
					IBlockState iblockstate1 = STONE;
					for (int j1 = 127; j1 >= 0; --j1) {
						if (j1 < 127 - this.rand.nextInt(5)
								&& j1 > this.rand.nextInt(5)) {
							IBlockState iblockstate2 = primer.getBlockState(k,
									j1, j);
							if (iblockstate2.getBlock() != null
									&& iblockstate2.getMaterial() != Material.AIR) {
								if (iblockstate2.getBlock() == STONE.getBlock()) {
									if (i1 == -1) {
										if (l <= 0) {
											iblockstate = AIR;
											iblockstate1 = STONE;
										} else if (j1 >= i - 4 && j1 <= i + 1) {
											iblockstate = STONE;
											iblockstate1 = STONE;
											if (flag1) {
												iblockstate = GRAVEL;
												iblockstate1 = STONE;
											}
											if (flag) {
												iblockstate = SOUL_SAND;
												iblockstate1 = SOUL_SAND;
											}
										}
										if (j1 < i
												&& (iblockstate == null || iblockstate
														.getMaterial() == Material.AIR)) {
											iblockstate = LAVA;
										}
										i1 = l;
										if (j1 >= i - 1) {
											primer.setBlockState(k, j1, j,
													iblockstate);
										} else {
											primer.setBlockState(k, j1, j,
													iblockstate1);
										}
									} else if (i1 > 0) {
										--i1;
										primer.setBlockState(k, j1, j,
												iblockstate1);
									}
								}
							} else {
								i1 = -1;
							}
						} else {
							primer.setBlockState(k, j1, j, BEDROCK);
						}
					}
				}
			}
		}
	}
}
