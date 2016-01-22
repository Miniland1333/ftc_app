package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

/**
 * Created by Henry on 1/9/2016.
 */
public class HenryAuto extends LinearOpMode{
    DcMotor LFront;
    DcMotor LBack;
    DcMotor RFront;
    DcMotor RBack;
    DcMotor FLift;
    DcMotor BLift;
    DcMotor Bucket;//Bucket Encoder is on LBack

    Servo LClaw;
    Servo RClaw;

    Boolean isClawOpen;


    //Initalization Code
    private void initialize() throws InterruptedException {
        LFront = hardwareMap.dcMotor.get("LFront");
        LBack = hardwareMap.dcMotor.get("LBack");
        RFront = hardwareMap.dcMotor.get("RFront");
        RBack = hardwareMap.dcMotor.get("RBack");
        FLift = hardwareMap.dcMotor.get("FLift");
        Bucket = hardwareMap.dcMotor.get("Bucket");
        BLift = hardwareMap.dcMotor.get("BLift");

        RFront.setDirection(DcMotor.Direction.REVERSE); //Reversing based on default motor rotation direction
        RBack.setDirection(DcMotor.Direction.REVERSE);  //Reversing based on default motor rotation direction
        FLift.setDirection(DcMotor.Direction.REVERSE);  //Reversing based on default motor rotation direction
        BLift.setDirection(DcMotor.Direction.REVERSE);  //Reversing based on default motor rotation direction

        LClaw = hardwareMap.servo.get("LClaw");
        RClaw = hardwareMap.servo.get("RClaw");


/*        one=hardwareMap.servo.get("one");
        two=hardwareMap.servo.get("two");
        three=hardwareMap.servo.get("three");
        four=hardwareMap.servo.get("four");
        five=hardwareMap.servo.get("five");
        six=hardwareMap.servo.get("six");*/

        //Motor and Servo Initialization
        telemetry.addData("Task", "Init");
        makeClawOpen();
        waitOneFullHardwareCycle();
    }
    @Override
    public void runOpMode() throws InterruptedException{
        initialize();
        waitForStart();
        Bucket.setPower(0);
        makeClawDown();
        Lift(-.5, 730);
        BLift.setPower(0);
        Lift(.5, 100);
        BLift.setPower(0);
        telemetry.addData("Task", "Down");
        task(.5, .5, 2000, "Forward1");
        task(.5, -.5, 900, "Turn Left1");
        task(.5, .5, 1200, "Forward2");
        task(.5, -.5, 900, "Turn Left2");
        task(0, 0, 0, "Wait1");
        /*RClaw.setPosition(.3);
        Lift(.5,1000);
        BLift.setPower(0);
        task(.20, .20, 700, "Forward3");
        task(0, 0, 1000, "Wait2");
        makeClawClosed();*/



    }

    private void makeClawOpen() throws InterruptedException{
        waitOneFullHardwareCycle();
        RClaw.setPosition(0);
        LClaw.setPosition(.7);
        isClawOpen = true;
        telemetry.addData("Task", "Inside");
    }
    private void makeClawDown(){
        RClaw.setPosition(0);
        LClaw.setPosition(.3);
    }
    private void makeClawClosed(){
        RClaw.setPosition(.7);
        LClaw.setPosition(.3);
        isClawOpen = false;
    }
    private void task (double left, double right, long time) throws InterruptedException{
        LBack.setPower(left);
        LFront.setPower(left);
        RBack.setPower(right);
        RFront.setPower(right);
        sleep(time);
        STOP();
    }
    private void task (double left, double right, long time,String Message) throws InterruptedException{
        LBack.setPower(left);
        LFront.setPower(left);
        RBack.setPower(right);
        RFront.setPower(right);
        telemetry.addData("Task", Message);
        sleep(time);
        STOP();
    }
    private void STOP() throws InterruptedException{
        LBack.setPower(0);
        LFront.setPower(0);
        RBack.setPower(0);
        RFront.setPower(0);
        telemetry.addData("Task", "STOP");
        sleep(1500);
    }
    private void Lift (double power, long time) throws InterruptedException{
        BLift.setPower(power);
        sleep(time);
    }
}
