package org.daimhim.banner;

import android.support.v4.view.ViewPager.PageTransformer;

import org.daimhim.banner.transformer.AccordionTransformer;
import org.daimhim.banner.transformer.BackgroundToForegroundTransformer;
import org.daimhim.banner.transformer.CubeInTransformer;
import org.daimhim.banner.transformer.CubeOutTransformer;
import org.daimhim.banner.transformer.DefaultTransformer;
import org.daimhim.banner.transformer.DepthPageTransformer;
import org.daimhim.banner.transformer.FlipHorizontalTransformer;
import org.daimhim.banner.transformer.FlipVerticalTransformer;
import org.daimhim.banner.transformer.ForegroundToBackgroundTransformer;
import org.daimhim.banner.transformer.RotateDownTransformer;
import org.daimhim.banner.transformer.RotateUpTransformer;
import org.daimhim.banner.transformer.ScaleInOutTransformer;
import org.daimhim.banner.transformer.StackTransformer;
import org.daimhim.banner.transformer.TabletTransformer;
import org.daimhim.banner.transformer.ZoomInTransformer;
import org.daimhim.banner.transformer.ZoomOutSlideTransformer;
import org.daimhim.banner.transformer.ZoomOutTranformer;


public class Transformer {
    public static Class<? extends PageTransformer> Default = DefaultTransformer.class;
    public static Class<? extends PageTransformer> Accordion = AccordionTransformer.class;
    public static Class<? extends PageTransformer> BackgroundToForeground = BackgroundToForegroundTransformer.class;
    public static Class<? extends PageTransformer> ForegroundToBackground = ForegroundToBackgroundTransformer.class;
    public static Class<? extends PageTransformer> CubeIn = CubeInTransformer.class;
    public static Class<? extends PageTransformer> CubeOut = CubeOutTransformer.class;
    public static Class<? extends PageTransformer> DepthPage = DepthPageTransformer.class;
    public static Class<? extends PageTransformer> FlipHorizontal = FlipHorizontalTransformer.class;
    public static Class<? extends PageTransformer> FlipVertical = FlipVerticalTransformer.class;
    public static Class<? extends PageTransformer> RotateDown = RotateDownTransformer.class;
    public static Class<? extends PageTransformer> RotateUp = RotateUpTransformer.class;
    public static Class<? extends PageTransformer> ScaleInOut = ScaleInOutTransformer.class;
    public static Class<? extends PageTransformer> Stack = StackTransformer.class;
    public static Class<? extends PageTransformer> Tablet = TabletTransformer.class;
    public static Class<? extends PageTransformer> ZoomIn = ZoomInTransformer.class;
    public static Class<? extends PageTransformer> ZoomOut = ZoomOutTranformer.class;
    public static Class<? extends PageTransformer> ZoomOutSlide = ZoomOutSlideTransformer.class;
}
