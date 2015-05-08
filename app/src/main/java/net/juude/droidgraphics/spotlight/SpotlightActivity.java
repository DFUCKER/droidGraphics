package net.juude.droidgraphics.spotlight;

/**
 * Created by juude on 15-4-2.
 */


import android.os.Bundle;
import android.view.View;
import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;

import net.juude.droidgraphics.R;

public class SpotlightActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spotlight);
        getWindow().setBackgroundDrawable(null);
        SpotlightView spotlight = (SpotlightView) findViewById(R.id.spotlight);
        spotlight.setAnimationSetupCallback(new SpotlightView.AnimationSetupCallback() {
            @Override
            public void onSetupAnimation(SpotlightView spotlight) {

                createAnimation(spotlight);
            }
        });
    }

    private void createAnimation(final SpotlightView spotlight) {
        View topTextView = findViewById(R.id.textView1);
        View bottomTextView = findViewById(R.id.textView2);

        final float textHeight = bottomTextView.getBottom() - topTextView.getTop();
        final float startX = topTextView.getLeft();
        final float startY = topTextView.getTop() + textHeight / 2.0f;
        final float endX = Math.max(topTextView.getRight(), bottomTextView.getRight());

        spotlight.setMaskX(endX);
        spotlight.setMaskY(startY);

        spotlight.animate().alpha(1.0f).withLayer().withEndAction(new Runnable() {
            @Override
            public void run() {
                ObjectAnimator moveLeft = ObjectAnimator.ofFloat(spotlight, "maskX", endX, startX);
                moveLeft.setDuration(2000);

                float startScale = spotlight.computeMaskScale(textHeight);
                ObjectAnimator scaleUp = ObjectAnimator.ofFloat(spotlight, "maskScale", startScale, startScale * 3.0f);
                scaleUp.setDuration(2000);

                ObjectAnimator moveCenter = ObjectAnimator.ofFloat(spotlight, "maskX", spotlight.getWidth() / 2.0f);
                moveCenter.setDuration(2000);

                ObjectAnimator moveUp = ObjectAnimator.ofFloat(spotlight, "maskY", spotlight.getHeight() / 2.0f);
                moveUp.setDuration(2000);

                ObjectAnimator superScale = ObjectAnimator.ofFloat(spotlight, "maskScale",
                        spotlight.computeMaskScale(Math.max(spotlight.getHeight(), spotlight.getWidth()) * 1.7f));
                superScale.setDuration(2000);

                AnimatorSet set = new AnimatorSet();
                set.play(moveLeft).with(scaleUp);
                set.play(moveCenter).after(scaleUp);
                set.play(moveUp).after(scaleUp);
                set.play(superScale).after(scaleUp);
                set.start();

                set.addListener(new AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {
                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {
                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        findViewById(R.id.content).setVisibility(View.VISIBLE);
                        findViewById(R.id.spotlight).setVisibility(View.GONE);
                        getWindow().setBackgroundDrawable(null);
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {
                    }
                });
            }
        });
    }
}
