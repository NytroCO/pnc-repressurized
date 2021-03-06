package me.desht.pneumaticcraft.common.progwidgets.area;

import java.util.List;
import java.util.function.Consumer;

import me.desht.pneumaticcraft.common.config.ConfigHandler;
import me.desht.pneumaticcraft.common.progwidgets.ProgWidgetArea.EnumAreaType;
import me.desht.pneumaticcraft.common.progwidgets.area.AreaType.AreaTypeWidget;
import me.desht.pneumaticcraft.common.progwidgets.area.AreaType.AreaTypeWidgetEnum;
import me.desht.pneumaticcraft.common.progwidgets.area.AreaType.EnumAxis;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

public class AreaTypeWall extends AreaType{

    public static final String ID = "wall";
    
    private EnumAxis axis = EnumAxis.X;
    
    public AreaTypeWall(){
        super(ID);
    }

    @Override
    public void addArea(Consumer<BlockPos> areaAdder, BlockPos p1, BlockPos p2, int minX, int minY, int minZ, int maxX, int maxY, int maxZ){
        switch (axis) {
            case X:
                {
                    Vec3d lineVec = new Vec3d(0, p2.getY() - p1.getY(), p2.getZ() - p1.getZ()).normalize();
                    lineVec = new Vec3d(lineVec.x, lineVec.y / 10, lineVec.z / 10);
                    double curY = p1.getY() + 0.5;
                    double curZ = p1.getZ() + 0.5;
                    double totalDistance = 0;
                    double maxDistance = Math.sqrt(Math.pow(p1.getY() - p2.getY(), 2) + Math.pow(p1.getZ() - p2.getZ(), 2));
                    while (totalDistance <= maxDistance) {
                        totalDistance += 0.1;
                        curY += lineVec.y;
                        curZ += lineVec.z;
                        for (int i = minX; i <= maxX; i++) {
                            if (curY >= 0 && curY < 256) {
                                BlockPos pos = new BlockPos(i, (int) curY, (int) curZ);
                                areaAdder.accept(pos);
                            }
                        }
                    }
                }
                break;
            case Y:
                {
                    Vec3d lineVec = new Vec3d(p2.getX() - p1.getX(), 0, p2.getZ() - p1.getZ()).normalize();
                    lineVec = new Vec3d(lineVec.x, lineVec.y / 10, lineVec.z / 10);
                    double curX = p1.getX() + 0.5;
                    double curZ = p1.getZ() + 0.5;
                    double totalDistance = 0;
                    double maxDistance = Math.sqrt(Math.pow(p1.getX() - p2.getX(), 2) + Math.pow(p1.getZ() - p2.getZ(), 2));
                    while (totalDistance <= maxDistance) {
                        totalDistance += 0.1;
                        curX += lineVec.x;
                        curZ += lineVec.z;
                        for (int i = Math.max(0, minY); i <= Math.min(maxY, 255); i++) {
                            BlockPos pos = new BlockPos((int) curX, i, (int) curZ);
                            areaAdder.accept(pos);
                        }
                    }
                }
                break;
            case Z:
                {
                    Vec3d lineVec = new Vec3d(p2.getX() - p1.getX(), p2.getY() - p1.getY(), 0).normalize();
                    lineVec = new Vec3d(lineVec.x / 10, lineVec.y / 10, lineVec.z);
                    double curX = p1.getX() + 0.5;
                    double curY = p1.getY() + 0.5;
                    double totalDistance = 0;
                    double maxDistance = Math.sqrt(Math.pow(p1.getX() - p2.getX(), 2) + Math.pow(p1.getY() - p2.getY(), 2));
                    while (totalDistance <= maxDistance) {
                        totalDistance += 0.1;
                        curX += lineVec.x;
                        curY += lineVec.y;
                        for (int i = minZ; i <= maxZ; i++) {
                            if (curY >= 0 && curY < 256) {
                                BlockPos pos = new BlockPos((int) curX, (int) curY, i);
                                areaAdder.accept(pos);
                            }
                        }
                    }
                }
                break;
            default:
                throw new IllegalArgumentException(axis.toString());
        }       
    }
    
    @Override
    public void addUIWidgets(List<AreaTypeWidget> widgets){
        super.addUIWidgets(widgets);
        widgets.add(new AreaTypeWidgetEnum<>("gui.progWidget.area.type.general.axis", EnumAxis.class, () -> axis, axis -> this.axis = axis));
    }
    
    @Override
    public void writeToNBT(NBTTagCompound tag){
        super.writeToNBT(tag);
        tag.setByte("axis", (byte)axis.ordinal());
    }
    
    @Override
    public void readFromNBT(NBTTagCompound tag){
        super.readFromNBT(tag);
        axis = EnumAxis.values()[tag.getByte("axis")];
    }

    @Override
    public void convertFromLegacy(EnumAreaType oldAreaType, int typeInfo){
        switch(oldAreaType){
            case X_WALL:
                axis = EnumAxis.X;
                break;
            case Y_WALL:
                axis = EnumAxis.Y;
                break;
            case Z_WALL:
                axis = EnumAxis.Z;
                break;
            default:
                throw new IllegalArgumentException();
        }
    }
}
