package com.qualcomm.ftcrobotcontroller.Extras;

import com.qualcomm.robotcore.exception.RobotCoreException;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.util.RobotLog;

import java.nio.BufferOverflowException;
import java.nio.ByteBuffer;

/**
 * Created by Henry on 1/8/2016.
 * Replicates an instance of a gamepad values.
 */
public class AutoGamepad extends Gamepad {
    public float left_stick_x;
    public float left_stick_y;
    public float right_stick_x;
    public float right_stick_y;
    public boolean dpad_up;
    public boolean dpad_down;
    public boolean dpad_left;
    public boolean dpad_right;
    public boolean a;
    public boolean b;
    public boolean x;
    public boolean y;
    public boolean guide;
    public boolean start;
    public boolean back;
    public boolean left_bumper;
    public boolean right_bumper;
    public boolean left_stick_button;
    public boolean right_stick_button;
    public float left_trigger;
    public float right_trigger;

    public AutoGamepad(Gamepad gamepad){
        this.left_stick_x = gamepad.left_stick_x;
        this.left_stick_y = gamepad.left_stick_y;
        this.right_stick_x = gamepad.right_stick_x;
        this.right_stick_y = gamepad.right_stick_y;
        this.dpad_up = gamepad.dpad_up;
        this.dpad_down = gamepad.dpad_down;
        this.dpad_left = gamepad.dpad_left;
        this.dpad_right = gamepad.dpad_right;
        this.a = gamepad.a;
        this.b = gamepad.b;
        this.x = gamepad.x;
        this.y = gamepad.y;
        this.guide = gamepad.guide;
        this.start = gamepad.start;
        this.back = gamepad.back;
        this.left_bumper = gamepad.left_bumper;
        this.right_bumper = gamepad.right_bumper;
        this.left_stick_button = gamepad.left_stick_button;
        this.right_stick_button = gamepad.right_stick_button;
        this.left_trigger = gamepad.left_trigger;
        this.right_trigger = gamepad.right_trigger;
    }

    public AutoGamepad(byte[] in){
        ByteBuffer var2 = ByteBuffer.wrap(in);
        this.left_stick_x = var2.getFloat();
        this.left_stick_y = var2.getFloat();
        this.right_stick_x = var2.getFloat();
        this.right_stick_y = var2.getFloat();
        this.left_trigger = var2.getFloat();
        this.right_trigger = var2.getFloat();
        int var5 = var2.getInt();
        this.left_stick_button = (var5 & 16384) != 0;
        this.right_stick_button = (var5 & 8192) != 0;
        this.dpad_up = (var5 & 4096) != 0;
        this.dpad_down = (var5 & 2048) != 0;
        this.dpad_left = (var5 & 1024) != 0;
        this.dpad_right = (var5 & 512) != 0;
        this.a = (var5 & 256) != 0;
        this.b = (var5 & 128) != 0;
        this.x = (var5 & 64) != 0;
        this.y = (var5 & 32) != 0;
        this.guide = (var5 & 16) != 0;
        this.start = (var5 & 8) != 0;
        this.back = (var5 & 4) != 0;
        this.left_bumper = (var5 & 2) != 0;
        this.right_bumper = (var5 & 1) != 0;
    }

    @Override
    public byte[] toByteArray() throws RobotCoreException {
        ByteBuffer var1 = ByteBuffer.allocate(45);

        try {
            byte var2 = 0;
            var1.putFloat(this.left_stick_x).array();
            var1.putFloat(this.left_stick_y).array();
            var1.putFloat(this.right_stick_x).array();
            var1.putFloat(this.right_stick_y).array();
            var1.putFloat(this.left_trigger).array();
            var1.putFloat(this.right_trigger).array();
            int var4 = (var2 << 1) + (this.left_stick_button?1:0);
            var4 = (var4 << 1) + (this.right_stick_button?1:0);
            var4 = (var4 << 1) + (this.dpad_up?1:0);
            var4 = (var4 << 1) + (this.dpad_down?1:0);
            var4 = (var4 << 1) + (this.dpad_left?1:0);
            var4 = (var4 << 1) + (this.dpad_right?1:0);
            var4 = (var4 << 1) + (this.a?1:0);
            var4 = (var4 << 1) + (this.b?1:0);
            var4 = (var4 << 1) + (this.x?1:0);
            var4 = (var4 << 1) + (this.y?1:0);
            var4 = (var4 << 1) + (this.guide?1:0);
            var4 = (var4 << 1) + (this.start?1:0);
            var4 = (var4 << 1) + (this.back?1:0);
            var4 = (var4 << 1) + (this.left_bumper?1:0);
            var4 = (var4 << 1) + (this.right_bumper?1:0);
            var1.putInt(var4);
        } catch (BufferOverflowException var3) {
            RobotLog.logStacktrace(var3);
        }

        return var1.array();
    }

    @Override
    public void fromByteArray(byte[] byteArray) throws RobotCoreException {
        ByteBuffer var2 = ByteBuffer.wrap(byteArray);
            this.left_stick_x = var2.getFloat();
            this.left_stick_y = var2.getFloat();
            this.right_stick_x = var2.getFloat();
            this.right_stick_y = var2.getFloat();
            this.left_trigger = var2.getFloat();
            this.right_trigger = var2.getFloat();
            int var5 = var2.getInt();
            this.left_stick_button = (var5 & 16384) != 0;
            this.right_stick_button = (var5 & 8192) != 0;
            this.dpad_up = (var5 & 4096) != 0;
            this.dpad_down = (var5 & 2048) != 0;
            this.dpad_left = (var5 & 1024) != 0;
            this.dpad_right = (var5 & 512) != 0;
            this.a = (var5 & 256) != 0;
            this.b = (var5 & 128) != 0;
            this.x = (var5 & 64) != 0;
            this.y = (var5 & 32) != 0;
            this.guide = (var5 & 16) != 0;
            this.start = (var5 & 8) != 0;
            this.back = (var5 & 4) != 0;
            this.left_bumper = (var5 & 2) != 0;
            this.right_bumper = (var5 & 1) != 0;
    }
}

