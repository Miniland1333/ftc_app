/* Copyright (c) 2015 Qualcomm Technologies Inc

All rights reserved.

Redistribution and use in source and binary forms, with or without modification,
are permitted (subject to the limitations in the disclaimer below) provided that
the following conditions are met:

Redistributions of source code must retain the above copyright notice, this list
of conditions and the following disclaimer.

Redistributions in binary form must reproduce the above copyright notice, this
list of conditions and the following disclaimer in the documentation and/or
other materials provided with the distribution.

Neither the name of Qualcomm Technologies Inc nor the names of its contributors
may be used to endorse or promote products derived from this software without
specific prior written permission.

NO EXPRESS OR IMPLIED LICENSES TO ANY PARTY'S PATENT RIGHTS ARE GRANTED BY THIS
LICENSE. THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
"AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE
FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE. */

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
import java.lang.Math;
import java.util.ArrayList;

/**
 * A Test Case
 * Created by Staff on 9/13/2015.
 */
public class TwelveTests extends LinearOpMode {

    DcMotor LFront;
    DcMotor LBack;
    DcMotor RFront;
    DcMotor RBack;
    DcMotor BLift;
    DcMotor Bucket;//Bucket Encoder is on LBack

    Servo LClaw;
    Servo RClaw;
    Servo LHook;
    Servo RHook;

    Boolean isClawOpen;
    Boolean isHookOpen;
    int index =0;
    ArrayList<AutoGamepad>Recording=new ArrayList<AutoGamepad>();
    protected Context context;
    private ElapsedTime recordTime = new ElapsedTime();
    final int STATE = 0;// 0=None, 1=RecordRED, 2=RecordBLUE
    String filename = "Auto.txt";
    FileOutputStream outStream;
    ObjectOutputStream out;
    private boolean prevStateX = false;
    private boolean prevStateY = false;


    //Initalization Code
    private void initialize(){
        try {waitOneFullHardwareCycle();}catch (InterruptedException ignore){}
        LFront = hardwareMap.dcMotor.get("LFront");
        LBack = hardwareMap.dcMotor.get("LBack");
        RFront = hardwareMap.dcMotor.get("RFront");
        RBack = hardwareMap.dcMotor.get("RBack");
        Bucket = hardwareMap.dcMotor.get("Bucket");
        BLift = hardwareMap.dcMotor.get("BLift");

        LFront.setDirection(DcMotor.Direction.REVERSE); //Reversing based on default motor rotation direction
        LBack.setDirection(DcMotor.Direction.REVERSE);  //Reversing based on default motor rotation direction

        LClaw = hardwareMap.servo.get("LClaw");
        RClaw = hardwareMap.servo.get("RClaw");
        LHook = hardwareMap.servo.get("LHook");
        RHook = hardwareMap.servo.get("RHook");

        context = FtcRobotControllerActivity.mainContext;
        try{
            outStream = context.openFileOutput(filename, Context.MODE_PRIVATE);
            out = new ObjectOutputStream(outStream);
        }catch (Exception e) {e.printStackTrace();}
        switch (STATE) {
            //noinspection ConstantConditions
            case 0:
                break;
            case 1:
                filename="AutoRED";
                break;
            case 2:
                filename="AutoBLUE";
                break;
        }


        //Motor and Servo Initialization
        makeClawOpen();
        makeHookOpen();

    }


    //Starting point for OpModes
    @Override
    public void runOpMode() throws InterruptedException {
        initialize();
        waitForStart();
        recordTime.reset();

        //Main Loop
        while (opModeIsActive()) {

            switch (STATE) {
                //noinspection ConstantConditions
                case 0:
                    break;
                case 1:
                    Record();
                    break;
                case 2:
                    Record();
                    break;
            }
            //ANY CHANGES MUST ALSO BE ADDED TO TwelveTestsAutoRED/BLUE AS WELL!!!
            LFront.setPower(gamepad1.left_stick_y);
            LBack.setPower(gamepad1.left_stick_y);
            RFront.setPower(gamepad1.right_stick_y*.9);
            RBack.setPower(gamepad1.right_stick_y * .9);
            BLift.setPower(-(gamepad2.left_stick_y));

            //Special Functions
            TiltMotor();
            Hook();
            Claw();


            Telemetry();
        }
        //noinspection ConstantConditions
        if (STATE==1){Save();}
        waitOneFullHardwareCycle();

    }

    //Added functionality for Bucket Motor tilt
    private void TiltMotor(){
        final int MAX_DEGREES = 45;
        final int TOLERANCE_DEGREES = 5;
        final int SLOW_RANGE = 15;

        double motorValue = gamepad2.right_stick_x*MAX_DEGREES;
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


    //Claw Servo controls
    private void Claw() {  //This class control the toggle to open/close the Claw
        if (!gamepad2.x){
            prevStateX =false;}
        if (gamepad2.x&&!prevStateX) {
            prevStateX =true;
            if (isClawOpen) {
                makeClawClosed();
            } else {
                makeClawOpen();
            }
        }

    }
    private void makeClawOpen(){
        RClaw.setPosition(0);
        LClaw.setPosition(1);
        isClawOpen = true;
    }
    private void makeClawClosed(){
        RClaw.setPosition(.7);
        LClaw.setPosition(.3);
        isClawOpen = false;
    }

    //Hook Servo Controls
    private void Hook() {  //This class control the toggle to open/close the Hook
        if (!gamepad2.y){
            prevStateY =false;}
        if (gamepad2.y&&!prevStateY) {
            prevStateY =true;
            if (isHookOpen) {
                makeHookClosed();
            } else {
                makeHookOpen();
            }
        }

    }
    private void makeHookOpen(){
        RHook.setPosition(1);
        LHook.setPosition(0);
        isHookOpen = true;
    }
    private void makeHookClosed(){
        RHook.setPosition(.25);
        LHook.setPosition(.75);
        isHookOpen = false;
    }

    //Autonomous Record/Save and Play/Open
    private void Record () {
        if (recordTime.time()>= .1) {
            try {
                recordTime.reset();
                AutoGamepad Gamepad1 = new AutoGamepad(gamepad1);
                out.writeObject(Gamepad1);
                Recording.add(Gamepad1);
                AutoGamepad Gamepad2 = new AutoGamepad(gamepad2);
                Recording.add(Gamepad2);
                out.writeObject(Gamepad2);
            } catch (Exception e) {e.printStackTrace();}
        }
    }
    private void Save() {
        try {
            outStream.close();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.i("Henry", String.format("Saved Successfully %d",Recording.size()));
    }
    private void Play() {
        if (recordTime.time()>= .1 && index <= Recording.size() - 2) {
            recordTime.reset();
            gamepad1 = Recording.get(index);
            index++;
            gamepad2 = Recording.get(index);
            index++;
        }
    }
    private void Open() {
        try {
            FileInputStream inStream;
            ObjectInputStream in;
            inStream = context.openFileInput(filename);
            in = new ObjectInputStream(inStream);
            Recording = (ArrayList<AutoGamepad>) in.readObject();
            inStream.close();
            in.close();
            Recording.add(new AutoGamepad());
            Recording.add(new AutoGamepad());
            Log.d("Henry", "Opened Successfully");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }



    //Customize telemetry data
    private void Telemetry(){
        telemetry.addData("1Left",gamepad1.left_stick_y);
        telemetry.addData("2Right",gamepad1.right_stick_y);
        telemetry.addData("3Bucket",LBack.getCurrentPosition()/4);
        if (STATE==0){telemetry.addData("4Record size", "Recording disabled");
        }else{telemetry.addData("4Record size", Recording.size());}

    }
}
