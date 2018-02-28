package spinoffpyme.com.talentmobile.Animations;

import android.content.Context;
import android.util.AttributeSet;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.ProgressBar;

/**
 * Created by tomas on 28/02/2018.
 */

public class ProgressBarAnimation extends Animation {
    private ProgressBar progressBar;
    private float desde;
    private float hasta;

    public ProgressBarAnimation(ProgressBar progressBar, float desde, float hasta) {
        super();
        this.progressBar = progressBar;
        this.desde = desde;
        this.hasta = hasta;
    }



    @Override
    protected void applyTransformation(float interpolatedTime, Transformation t) {
        super.applyTransformation(interpolatedTime, t);
        float valor=desde+(hasta-desde)*interpolatedTime;
        progressBar.setProgress((int)valor);
    }
}
