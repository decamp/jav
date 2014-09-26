/*
 * Copyright (c) 2014. Massachusetts Institute of Technology
 * Released under the BSD 2-Clause License
 * http://opensource.org/licenses/BSD-2-Clause
 * This file might include comments and code snippets from FFMPEG, released under LGPL 2.1 or later.
 */

package bits.jav;

import java.awt.image.BufferedImage;
import javax.swing.JFrame;

import bits.jav.codec.*;
import bits.jav.swscale.SwsContext;
import static bits.jav.Jav.*;


/**
 * @author decamp
 */
public class FrameDisplayer {


    private final SwsContext mConverter;
    private final JavFrame mOutFrame;
    private final PictureFormatT mSourceFormat;

    private final ImagePanel mPanel;

    private BufferedImage mPanelImage;
    private BufferedImage mImage;
    private int[] mPix;

    
    public FrameDisplayer( PictureFormatT sourceFormat ) throws JavException {
        mSourceFormat = sourceFormat;

        SwsContext sws = SwsContext.alloc();
        PictureFormatT destFormat = new PictureFormatT( sourceFormat.width(), sourceFormat.height(), AV_PIX_FMT_BGRA );

        sws.configure( sourceFormat.width(), sourceFormat.height(), sourceFormat.pixelFormat(),
                       destFormat.width(), destFormat.height(), destFormat.pixelFormat(),
                       Jav.SWS_FAST_BILINEAR );

        sws.initialize();

        mConverter  = sws;
        mOutFrame   = JavFrame.allocVideo( destFormat.width(), destFormat.height(), destFormat.pixelFormat(), null );
        mPanelImage = new BufferedImage( destFormat.width(), destFormat.height(), BufferedImage.TYPE_INT_ARGB );
        mImage      = new BufferedImage( destFormat.width(), destFormat.height(), BufferedImage.TYPE_INT_ARGB );
        mPix        = new int[destFormat.width() * destFormat.height()];
        mPanel      = new ImagePanel( mImage );

        JFrame f = ImagePanel.frameImagePanel( mPanel );
        f.setLocation( 1024, 50 );
        f.setVisible( true );
    }


    public void paint( JavFrame frame ) throws JavException {
        mConverter.convert( frame, 0, mSourceFormat.height(), mOutFrame );
        mOutFrame.directBuffer().asIntBuffer().get( mPix );
        mImage.setRGB( 0, 0, mImage.getWidth(), mImage.getHeight(), mPix, 0, mImage.getWidth() );
    }


    public BufferedImage image() {
        return mImage;
    }


    public void swap() {
        BufferedImage im = mPanelImage;
        mPanelImage = mImage;
        mImage = im;

        mPanel.setImage( mPanelImage );
    }

}
