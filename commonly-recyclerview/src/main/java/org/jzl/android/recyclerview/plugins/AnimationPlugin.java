package org.jzl.android.recyclerview.plugins;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.TimeInterpolator;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;

import androidx.recyclerview.widget.RecyclerView;

import org.jzl.android.recyclerview.builder.RecyclerViewConfigurator;
import org.jzl.lang.util.ObjectUtils;

public class AnimationPlugin<T, VH extends RecyclerView.ViewHolder> implements RecyclerViewConfigurator.RecyclerViewPlugin<T, VH> {
    private static final TimeInterpolator INTERPOLATOR = new LinearInterpolator();
    private static final DecelerateInterpolator DECELERATE_INTERPOLATOR = new DecelerateInterpolator(1.8f);


    private Animation animation;
    private int lastPosition = 0;

    public AnimationPlugin(Animation animation) {
        this.animation = ObjectUtils.requireNonNull(animation, "animation");
    }

    @Override
    public void setup(RecyclerViewConfigurator<T, VH> configurator, int... viewTypes) {
        configurator.bindDataProviderBinder(dataProvider -> dataProvider.register(() -> lastPosition = 0));
        configurator.itemViewAttachedToWindows(holder -> {
            if (holder.getAdapterPosition() > lastPosition) {
                animation.animator(holder.itemView).start();
                lastPosition = holder.getAdapterPosition();
            }
        }, viewTypes);
    }

    public static <T, VH extends RecyclerView.ViewHolder> AnimationPlugin<T, VH> ofAlphaIn(float startAlpha, int duration, TimeInterpolator interpolator) {
        return new AnimationPlugin<>(new AlphaInAnimation(startAlpha, duration, interpolator));
    }

    public static <T, VH extends RecyclerView.ViewHolder> AnimationPlugin<T, VH> ofAlphaIn(float startAlpha, int duration) {
        return ofAlphaIn(startAlpha, duration, INTERPOLATOR);
    }

    public static <T, VH extends RecyclerView.ViewHolder> AnimationPlugin<T, VH> ofAlphaIn(float startAlpha) {
        return ofAlphaIn(startAlpha, 300, INTERPOLATOR);
    }

    public static <T, VH extends RecyclerView.ViewHolder> AnimationPlugin<T, VH> ofAlphaIn() {
        return ofAlphaIn(0.5f, 300, INTERPOLATOR);
    }

    public static <T, VH extends RecyclerView.ViewHolder> AnimationPlugin<T, VH> ofScaleIn(float startScale, int duration, TimeInterpolator interpolator) {
        return new AnimationPlugin<>(new ScaleInAnimation(startScale, duration, interpolator));
    }

    public static <T, VH extends RecyclerView.ViewHolder> AnimationPlugin<T, VH> ofScaleIn(float startScale, int duration) {
        return ofScaleIn(startScale, duration, INTERPOLATOR);
    }

    public static <T, VH extends RecyclerView.ViewHolder> AnimationPlugin<T, VH> ofScaleIn(float startScale) {
        return ofScaleIn(startScale, 300, INTERPOLATOR);
    }

    public static <T, VH extends RecyclerView.ViewHolder> AnimationPlugin<T, VH> ofScaleIn() {
        return ofScaleIn(0.5f, 300, INTERPOLATOR);
    }

    public static <T, VH extends RecyclerView.ViewHolder> AnimationPlugin<T, VH> ofSlideInLeft(int duration, TimeInterpolator interpolator) {
        return new AnimationPlugin<>(new SlideInLeftAnimation(duration, interpolator));
    }

    public static <T, VH extends RecyclerView.ViewHolder> AnimationPlugin<T, VH> ofSlideInLeft(int duration) {
        return ofSlideInLeft(duration, DECELERATE_INTERPOLATOR);
    }

    public static <T, VH extends RecyclerView.ViewHolder> AnimationPlugin<T, VH> ofSlideInLeft() {
        return ofSlideInLeft(400);
    }

    public static <T, VH extends RecyclerView.ViewHolder> AnimationPlugin<T, VH> ofSlideInRight(int duration, TimeInterpolator interpolator) {
        return new AnimationPlugin<>(new SlideInRightAnimation(duration, interpolator));
    }

    public static <T, VH extends RecyclerView.ViewHolder> AnimationPlugin<T, VH> ofSlideInRight(int duration) {
        return ofSlideInRight(duration, DECELERATE_INTERPOLATOR);
    }

    public static <T, VH extends RecyclerView.ViewHolder> AnimationPlugin<T, VH> ofSlideInRight() {
        return ofSlideInRight(400);
    }

    public static <T, VH extends RecyclerView.ViewHolder> AnimationPlugin<T, VH> ofSlideInBottom(int duration, TimeInterpolator interpolator) {
        return new AnimationPlugin<>(new SlideInBottomAnimation(duration, interpolator));
    }

    public static <T, VH extends RecyclerView.ViewHolder> AnimationPlugin<T, VH> ofSlideInBottom(int duration) {
        return ofSlideInBottom(duration, DECELERATE_INTERPOLATOR);
    }

    public static <T, VH extends RecyclerView.ViewHolder> AnimationPlugin<T, VH> ofSlideInBottom() {
        return ofSlideInBottom(400);
    }

    public interface Animation {
        Animator animator(View view);
    }

    public static class AlphaInAnimation implements Animation {

        private float startAlpha;
        private int duration;
        private TimeInterpolator interpolator;

        public AlphaInAnimation(float startAlpha, int duration, TimeInterpolator interpolator) {
            this.startAlpha = startAlpha;
            this.duration = duration;
            this.interpolator = ObjectUtils.get(interpolator, INTERPOLATOR);
        }

        public AlphaInAnimation(float startAlpha, int duration) {
            this(startAlpha, duration, INTERPOLATOR);
        }

        public AlphaInAnimation(float startAlpha) {
            this(startAlpha, 300);
        }

        @Override
        public Animator animator(View view) {
            ObjectAnimator animator = ObjectAnimator.ofFloat(view, "alpha", startAlpha, 1);
            animator.setDuration(duration);
            animator.setInterpolator(interpolator);
            return animator;
        }
    }

    public static class ScaleInAnimation implements Animation {

        private float startScale;
        private int duration;
        private TimeInterpolator interpolator;

        public ScaleInAnimation(float startScale, int duration, TimeInterpolator interpolator) {
            this.startScale = startScale;
            this.duration = duration;
            this.interpolator = ObjectUtils.get(interpolator, INTERPOLATOR);
        }

        @Override
        public Animator animator(View view) {
            AnimatorSet animatorSet = new AnimatorSet();
            animatorSet.play(ObjectAnimator.ofFloat(view, "scaleX", startScale, 1))
                    .with(ObjectAnimator.ofFloat(view, "scaleY", startScale, 1));
            animatorSet.setDuration(duration);
            animatorSet.setInterpolator(interpolator);
            return animatorSet;
        }
    }


    public static class SlideInRightAnimation implements Animation {

        private int duration;
        private TimeInterpolator interpolator;

        public SlideInRightAnimation(int duration, TimeInterpolator interpolator) {
            this.duration = duration;
            this.interpolator = ObjectUtils.get(interpolator, DECELERATE_INTERPOLATOR);
        }

        @Override
        public Animator animator(View view) {
            ObjectAnimator animator = ObjectAnimator.ofFloat(view, "translationX", view.getRootView().getWidth(), 0);
            animator.setDuration(duration).setInterpolator(interpolator);
            return animator;
        }
    }

    public static class SlideInLeftAnimation implements Animation {

        private int duration;
        private TimeInterpolator interpolator;

        public SlideInLeftAnimation(int duration, TimeInterpolator interpolator) {
            this.duration = duration;
            this.interpolator = ObjectUtils.get(interpolator, DECELERATE_INTERPOLATOR);
        }

        @Override
        public Animator animator(View view) {
            ObjectAnimator animator = ObjectAnimator.ofFloat(view, "translationX", -view.getRootView().getWidth(), 0);
            animator.setDuration(duration).setInterpolator(interpolator);
            return animator;
        }
    }

    public static class SlideInBottomAnimation implements Animation {

        private int duration;
        private TimeInterpolator interpolator;

        public SlideInBottomAnimation(int duration, TimeInterpolator interpolator) {
            this.duration = duration;
            this.interpolator = ObjectUtils.get(interpolator, DECELERATE_INTERPOLATOR);
        }

        @Override
        public Animator animator(View view) {
            ObjectAnimator animator = ObjectAnimator.ofFloat(view, "translationY", view.getMeasuredHeight(), 0);
            animator.setDuration(duration).setInterpolator(interpolator);
            return animator;
        }
    }
}
