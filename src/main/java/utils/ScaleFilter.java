package utils;

import com.jhlabs.image.AbstractBufferedImageOp;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;

/**
 * Created by IntelliJ IDEA.
 * User: Basil
 * Date: 11-6-21
 * Time: 下午4:53
 */
public class ScaleFilter extends AbstractBufferedImageOp {

	private int width;
	private int height;

    /**
     * Construct a ScaleFilter.
     */
	public ScaleFilter() {
		this(32, 32);
	}

    /**
     * Construct a ScaleFilter.
     * @param width the width to scale to
     * @param height the height to scale to
     */
	public ScaleFilter( int width, int height ) {
		this.width = width;
		this.height = height;
	}

    public BufferedImage filter( BufferedImage src, BufferedImage dst ) {
		if ( dst == null ) {
			ColorModel dstCM = src.getColorModel();
			dst = new BufferedImage(dstCM, dstCM.createCompatibleWritableRaster(width, height),
                dstCM.isAlphaPremultiplied(), null);
        }

		Image scaleImage = src.getScaledInstance( width, height, Image.SCALE_AREA_AVERAGING );
		Graphics2D g = dst.createGraphics();
		g.drawImage( scaleImage, 0, 0, width, height, null );
		g.dispose();

        return dst;
    }

	public String toString() {
		return "Distort/Scale";
	}


}

