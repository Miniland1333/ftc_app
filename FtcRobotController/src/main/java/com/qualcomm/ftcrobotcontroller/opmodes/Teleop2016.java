package com.qualcomm.ftcrobotcontroller.opmodes;

import android.util.Log;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

/**
 * Created by Staff on 1/3/2016.
 */
public class Teleop2016 extends LinearOpMode {

    DcMotor LFront;
    DcMotor LBack;
    DcMotor RFront;
    DcMotor RBack;
    //Driver 1 motors

    DcMotor FLift;
    DcMotor BLift;
    DcMotor Bucket;//Bucket Encoder is on LBack
    //Driver 2 motors

    Boolean Claw_closed=true;
    //For toggling the X button that controls the Claw


    Servo LClaw;
    Servo RClaw;
    //These servos positions always have to add up to 1 (e.g. .7 and .3)
    //The closed position is RClaw at .7 and LClaw at .3

    private void initialize(){
        LFront = hardwareMap.dcMotor.get("LFront");
        LBack = hardwareMap.dcMotor.get("LBack");
        RFront = hardwareMap.dcMotor.get("RFront");
        RBack = hardwareMap.dcMotor.get("RBack");
        FLift = hardwareMap.dcMotor.get("FLift");
        Bucket = hardwareMap.dcMotor.get("Bucket");
        BLift = hardwareMap.dcMotor.get("BLift");
        LClaw = hardwareMap.servo.get ("LClaw");
        RClaw = hardwareMap.servo.get("RClaw");
        //These are the variables that this code will be using in this code

        LFront.setDirection(DcMotor.Direction.REVERSE); //Reversing based on default motor rotation direction
        LBack.setDirection(DcMotor.Direction.REVERSE);  //Reversing based on default motor rotation direction
        FLift.setDirection(DcMotor.Direction.REVERSE);   //Reversing based on default motor rotation direction
        LClaw.setPosition(.4);
        RClaw.setPosition(.6);

    }
    @Override
    public void runOpMode() throws InterruptedException {
        initialize();
        waitForStart();
        //Gets robot ready for competition

        while (opModeIsActive()) {

            Claw_Toggle();
            //This is the piece that controls the Claw open/close

            LBack.setPower(gamepad1.left_stick_y);
            LFront.setPower(gamepad1.left_stick_y);
            RBack.setPower(gamepad1.right_stick_y);
            RFront.setPower(gamepad1.right_stick_y);
            //These all control driving on joystick 1

            BLift.setPower(-(gamepad2.right_stick_y));
            //This moves the spool motor; it will be your main control as driver 2

            Bucket.setPower(-((gamepad2.left_stick_x)/6));
            //This moves the bucket around in a circle so we can score


            //These if statements below move the front motor
            if (gamepad2.y) {
                FLift.setPower(.5);
            } //Hold down the Y-button to move the front lift up
            else if (gamepad2.b) {
                FLift.setPower(-(.5));
            } //Hold down the B-button to move the front lift down
            else{
                FLift.setPower(0);
            }
            //Remember that Y and B make Front Lift move if you need to move it


        }
    }

    private void Claw_Toggle() throws InterruptedException{  //This class control the toggle to open/close the Claw
        if (gamepad2.x){
            do{
                waitForNextHardwareCycle();
            } while (gamepad2.x);
            if (Claw_closed){
                RClaw.setPosition(.4);
                LClaw.setPosition(.6);
                Claw_closed = false;
            }
            else {
                RClaw.setPosition(.6);
                LClaw.setPosition(.4);
                Claw_closed = true;
            }
        }
    }
}
