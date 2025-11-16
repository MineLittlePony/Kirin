package com.minelittlepony.common.util.animation;

import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

/**
 * Calculates roll and incline for a player based on their motion vectors.
 *
 * @author Polyakovich - Big thank you to this dude and his meta-physics friend for working this all out.
 */
public class MotionCompositor {

    private static final double THIRD_PI = Math.PI / 3;

    /**
     * Gets the angle of horizontal roll in degrees based on the player's vertical and horizontal motion.
     */
    public double calculateRoll(LivingEntity entity, double motionX, double motionY, double motionZ) {

        // since model roll should probably be calculated from model rotation rather than entity rotation...
        double roll = sensibleAngle(entity.yBodyRotO - entity.yBodyRot);
        double horMotion = Math.sqrt(motionX * motionX + motionZ * motionZ);
        float modelYaw = sensibleAngle(entity.yBodyRot);

        // detecting that we're flying backwards and roll must be inverted
        if (Math.abs(sensibleAngle((float) Math.toDegrees(Math.atan2(motionX, motionZ)) + modelYaw)) > 90) {
            roll *= -1;
        }

        // ayyy magic numbers (after 5 - an approximation of nice looking coefficients calculated by hand)

        // roll might be zero, in which case Math.pow produces +Infinity. Anything x Infinity = NaN.
        double pow = roll != 0 ? Math.pow(Math.abs(roll), -0.191) : 0;

        roll *= horMotion * 5 * (3.6884f * pow);

        assert !Float.isNaN((float)roll);

        return Mth.clamp(roll, -54, 54);
    }

    /**
     * Gets the angle of inclination in degrees based on the player's vertical and horizontal motion.
     */
    public double calculateIncline(LivingEntity entity, double motionX, double motionY, double motionZ) {
        double dist = Math.sqrt(motionX * motionX + motionZ * motionZ);
        double angle = Math.atan2(motionY, dist);

        if (entity instanceof Player player && !player.getAbilities().mayfly) {
            angle /= 2;
        }

        angle = Mth.clamp(angle, -THIRD_PI, THIRD_PI);

        return Math.toDegrees(angle);
    }

    private static float sensibleAngle(float angle) {
        angle %= 360;

        if (angle > 180) angle -= 360;
        if (angle < -180) angle += 360;

        return angle;
    }
}
