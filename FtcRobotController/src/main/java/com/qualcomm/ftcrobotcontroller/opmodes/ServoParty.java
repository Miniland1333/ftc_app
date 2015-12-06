package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Range;

/**
 * Created by robotics on 12/6/2015.
 */
public class ServoParty extends LinearOpMode{

    Servo one;
    Servo two;
    Servo three;
    Servo four;
    Servo five;
    Servo six;

    private void initialize(){
        one=hardwareMap.servo.get("one");
        two=hardwareMap.servo.get("two");
        three=hardwareMap.servo.get("three");
        four=hardwareMap.servo.get("four");
        five=hardwareMap.servo.get("five");
        six=hardwareMap.servo.get("six");

    }

    @Override
    public void runOpMode() throws InterruptedException {
        initialize();
        waitForStart();

        while (opModeIsActive()) {


            one.setPosition((gamepad1.left_stick_y+1)/2);
            two.setPosition((-gamepad1.left_stick_y+1)/2);
            three.setPosition((gamepad1.left_stick_y+1)/2);
            four.setPosition((gamepad1.right_stick_y+1)/2);
            five.setPosition((gamepad1.right_stick_y+1)/2);
            six.setPosition((gamepad1.right_stick_y+1)/2);


            double pickle;
            pickle=(gamepad1.left_stick_y+1)/2;
            pickle= Range.clip(pickle,.25,.65);
            one.setPosition(pickle);
            two.setPosition(-pickle);
            Telemetry();
            waitForNextHardwareCycle();
        }
    }

    public void Telemetry(){
        telemetry.addData("Value",gamepad1.left_stick_y);
        telemetry.addData("Value2",gamepad1.right_stick_y);
    }
}
