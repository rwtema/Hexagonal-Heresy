package com.rwtema.hexeresy;

import net.minecraftforge.fml.common.ModMetadata;
import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Map;

@IFMLLoadingPlugin.TransformerExclusions(value = {"com.rwtema.hexeresy.", "com.rwtema.hexeresy.HexRenderingCoreMod"})
@IFMLLoadingPlugin.SortingIndex(Integer.MAX_VALUE)
public class HexRenderingCoreMod implements IFMLLoadingPlugin {
    protected static final ModMetadata md;

    public static boolean loaded = false;
    public static Logger logger = LogManager.getLogger("HexcraftCoreMod");

    static {
        md = new ModMetadata();
        md.autogenerated = false;
        md.authorList.add("RWTema");
        md.credits = "RWTema";
        md.modId = "Hexcraft";
        md.version = "1";
        md.name = "CoreXU2";
        md.description = "Core mod for Extra Utilities 2";
    }

    public HexRenderingCoreMod() {
        //		super(md);
        loaded = true;
        logger.info("Hexcraft Core Mod - loaded");
    }

    @Override
    public String[] getASMTransformerClass() {
        return new String[]{HexClassTransformer.class.getName()};
    }

    @Override
    public String getModContainerClass() {
        return null;
    }

    @Override
    public String getSetupClass() {
        return null;
    }

    @Override
    public void injectData(Map<String, Object> data) {

    }

    @Override
    public String getAccessTransformerClass() {
        return null;
    }
}