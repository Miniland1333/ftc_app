package com.qualcomm.ftcrobotcontroller.opmodes;

import android.content.Context;
import android.util.Log;

import com.qualcomm.ftcrobotcontroller.Extras.AutoGamepad;
import com.qualcomm.ftcrobotcontroller.FtcRobotControllerActivity;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

/**
 * Created by Henry on 1/21/2016.
 */
public class TwelveTestAuto extends LinearOpMode {
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
    int index =0;
    ArrayList<AutoGamepad> Recording=new ArrayList<AutoGamepad>();
    protected Context context;
    final String filename = "Auto.txt";
    private ElapsedTime recordTime = new ElapsedTime();
    AutoGamepad Gamepad1 = new AutoGamepad();
    AutoGamepad Gamepad2 = new AutoGamepad();
    private boolean prevState = false;


    //Initalization Code
    private void initialize(){
        LFront = hardwareMap.dcMotor.get("LFront");
        LBack = hardwareMap.dcMotor.get("LBack");
        RFront = hardwareMap.dcMotor.get("RFront");
        RBack = hardwareMap.dcMotor.get("RBack");
        FLift = hardwareMap.dcMotor.get("FLift");
        Bucket = hardwareMap.dcMotor.get("Bucket");
        BLift = hardwareMap.dcMotor.get("BLift");

        LFront.setDirection(DcMotor.Direction.REVERSE); //Reversing based on default motor rotation direction
        LBack.setDirection(DcMotor.Direction.REVERSE);  //Reversing based on default motor rotation direction
        FLift.setDirection(DcMotor.Direction.REVERSE);  //Reversing based on default motor rotation direction

        LClaw = hardwareMap.servo.get("LClaw");
        RClaw = hardwareMap.servo.get("RClaw");

        context = FtcRobotControllerActivity.mainContext;
/*        one=hardwareMap.servo.get("one");
        two=hardwareMap.servo.get("two");
        three=hardwareMap.servo.get("three");
        four=hardwareMap.servo.get("four");
        five=hardwareMap.servo.get("five");
        six=hardwareMap.servo.get("six");*/

        //Motor and Servo Initialization
        makeClawClosed();
        makeClawOpen();

    }
    //Starting point for OpModes
    @Override
    public void runOpMode() throws InterruptedException {
        initialize();
        waitForStart();
        recordTime.reset();
        Open();

        //Main Loop
        while (opModeIsActive()) {
            Play();

            LFront.setPower(Gamepad1.left_stick_y);
            LBack.setPower(Gamepad1.left_stick_y);
            RFront.setPower(Gamepad1.right_stick_y);
            RBack.setPower(Gamepad1.right_stick_y);
            BLift.setPower(-(Gamepad2.left_stick_y));

/*            one.setPosition((Gamepad1.left_stick_y+1)/2);
            two.setPosition((Gamepad1.left_stick_y+1)/2);
            three.setPosition((Gamepad1.left_stick_y+1)/2);
            four.setPosition((Gamepad1.right_stick_y+1)/2);
            five.setPosition((Gamepad1.right_stick_y+1)/2);
            six.setPosition((Gamepad1.right_stick_y+1)/2);*/

            //Special Functions
            TiltMotor();
            FLift();
            Claw();


            Telemetry();
        }
    }

    //Added functionality for Bucket Motor tilt
    private void TiltMotor(){
        final int MAX_DEGREES = 25;
        final int TOLERANCE_DEGREES = 5;
        final int SLOW_RANGE = 15;

        double motorValue = Gamepad2.right_stick_x*MAX_DEGREES;
        double currentValue= LBack.getCurrentPosition()/4f; //Bucket Encoder is on LBack
        double difference = Math.abs(motorValue-currentValue);
        if (difference<=TOLERANCE_DEGREES){Bucket.setPower(0);
        }
        else if (motorValue>currentValue){
            if (difference<=SLOW_RANGE) {Bucket.setPower(-.1);}
            else {Bucket.setPower(-.2);}
        }
        else{
            if (difference<=SLOW_RANGE){Bucket.setPower(.1);}
            else {Bucket.setPower(.2);}
        }
    }

    //Back Lift controls
    private void FLift(){
        if (Gamepad2.dpad_up){FLift.setPower(.5);}
        else if (Gamepad2.dpad_down){FLift.setPower(-.5);}
        else{FLift.setPowerFloat();}
    }

    //Claw Servo controls
    private void Claw() {  //This class control the toggle to open/close the Claw
        if (!Gamepad2.a){prevState=false;}
        if (Gamepad2.a&&!prevState) {
            prevState=true;
            if (isClawOpen) {
                makeClawClosed();
            } else {
                makeClawOpen();
            }
        }

    }
    private void makeClawOpen(){
        RClaw.setPosition(.3);
        LClaw.setPosition(.7);
        isClawOpen = true;
    }
    private void makeClawClosed(){
        RClaw.setPosition(.7);
        LClaw.setPosition(.3);
        isClawOpen = false;
    }

    //Autonomous Play/Open

    private void Play() {
        if (recordTime.time()>= .1 && index <= Recording.size() - 2) {
            recordTime.reset();
            Gamepad1 = Recording.get(index);
            index++;
            Gamepad2 = Recording.get(index);
            index++;
        }
    }
    private void Open() {
        try {
            FileInputStream inStream;
            ObjectInputStream in;
            inStream = context.openFileInput(filename);
            in = new ObjectInputStream(inStream);
            while(inStream.available()>0){Recording.add((AutoGamepad) in.readObject());}
            inStream.close();
            in.close();
            Recording.add(new AutoGamepad());
            Recording.add(new AutoGamepad());
            Log.i("Henry", "Opened Successfully");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }



    //Customize telemetry data
    private void Telemetry(){
        telemetry.addData("Left",Gamepad1.left_stick_y);
        telemetry.addData("Right",Gamepad1.right_stick_y);
        telemetry.addData("Bucket",LBack.getCurrentPosition()/4);
        telemetry.addData("Record size",String.format("%d/%d",index,Recording.size()));
    }
}
