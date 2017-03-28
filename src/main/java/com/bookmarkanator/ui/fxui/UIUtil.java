package com.bookmarkanator.ui.fxui;

import java.awt.*;
import java.util.*;

public class UIUtil
{
    private static UIUtil uiUtil;
    private Map<Integer,Dimension> bestWindowSizeMap;

    public Map<Integer, Dimension> getBestWindowSizeMap()
    {
        if (bestWindowSizeMap ==null)
        {
            bestWindowSizeMap = new HashMap<>();

            GraphicsDevice gd =  GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
            Dimension d = new Dimension((int)(gd.getDisplayMode().getWidth()* .65),(int) (gd.getDisplayMode().getHeight()* .5));

            bestWindowSizeMap.put(0,d);

//            GraphicsDevice[] graphicsDevices = GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices();
//            int c=0;
//
//            for (GraphicsDevice graphicsDevice: graphicsDevices)
//            {
//                int width = graphicsDevice.getDisplayMode().getWidth();
//                int height = graphicsDevice.getDisplayMode().getHeight();
//                Dimension dimension = new Dimension((int) (width * .65), (int) (height * .5));
//                bestWindowSizeMap.put(c, dimension);
//            }
        }
        return bestWindowSizeMap;
    }

    public static UIUtil use()
    {
        if (uiUtil==null)
        {
            uiUtil = new UIUtil();
        }
        return uiUtil;
    }
}
