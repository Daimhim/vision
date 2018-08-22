package org.daimhim.banner2;

import android.support.v4.view.ViewPager.PageTransformer;

import org.daimhim.banner2.transformer.AccordionTransformer;
import org.daimhim.banner2.transformer.BackgroundToForegroundTransformer;
import org.daimhim.banner2.transformer.CubeInTransformer;
import org.daimhim.banner2.transformer.CubeOutTransformer;
import org.daimhim.banner2.transformer.DefaultTransformer;
import org.daimhim.banner2.transformer.DepthPageTransformer;
import org.daimhim.banner2.transformer.FlipHorizontalTransformer;
import org.daimhim.banner2.transformer.FlipVerticalTransformer;
import org.daimhim.banner2.transformer.ForegroundToBackgroundTransformer;
import org.daimhim.banner2.transformer.RotateDownTransformer;
import org.daimhim.banner2.transformer.RotateUpTransformer;
import org.daimhim.banner2.transformer.ScaleInOutTransformer;
import org.daimhim.banner2.transformer.StackTransformer;
import org.daimhim.banner2.transformer.TabletTransformer;
import org.daimhim.banner2.transformer.ZoomInTransformer;
import org.daimhim.banner2.transformer.ZoomOutSlideTransformer;
import org.daimhim.banner2.transformer.ZoomOutTranformer;


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
