package com.gkortsaridis.androidthingsfirststeps;

import android.app.Activity;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;

import com.gkortsaridis.androidthingsfirststeps.Music.AdvancedSpeaker;
import com.gkortsaridis.androidthingsfirststeps.Music.Notes;
import com.google.android.things.contrib.driver.button.Button;
import com.google.android.things.contrib.driver.button.ButtonInputDriver;
import com.google.android.things.contrib.driver.pwmspeaker.Speaker;
import com.google.android.things.pio.Gpio;
import com.google.android.things.pio.PeripheralManagerService;
import com.google.android.things.pio.Pwm;

import java.io.IOException;
import java.util.ArrayList;

import static java.lang.Thread.sleep;

public class MainActivity extends Activity {

    private static final String GPIO_RGB_RED = "BCM19";
    private static final String GPIO_RGB_GREEN = "BCM6";
    private static final String GPIO_RGB_BLUE = "BCM5";
    private static final String GPIO_BUTTON = "BCM21";
    private static final String GPIO_LED1 = "BCM23";
    private static final String GPIO_LED2 = "BCM24";
    private static final String GPIO_LED3 = "BCM25";
    private static final String GPIO_LED4 = "BCM12";
    private static final String GPIO_LED5 = "BCM16";
    private static final String GPIO_LED6 = "BCM20";
    private static final String GPIO_LED7 = "BCM26";


    private Switch redSw,blueSw, greenSw;
    private Gpio mLedGpioRed,mLedGpioGreen,mLedGpioBlue;
    private Gpio mLed1,mLed2,mLed3,mLed4,mLed5,mLed6,mLed7;
    private ButtonInputDriver mButtonInputDriver;

    private Thread custListLoadThread;
    private int rgbState = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rgbSwitchesSetup();

        PeripheralManagerService service = new PeripheralManagerService();
        Log.i("Your Available GPIOs",service.getGpioList().toString());

        try {

            mLed1 = service.openGpio(GPIO_LED1);
            mLed2 = service.openGpio(GPIO_LED2);
            mLed3 = service.openGpio(GPIO_LED3);
            mLed4 = service.openGpio(GPIO_LED4);
            mLed5 = service.openGpio(GPIO_LED5);
            mLed6 = service.openGpio(GPIO_LED6);
            mLed7 = service.openGpio(GPIO_LED7);

            mLed1.setDirection(Gpio.DIRECTION_OUT_INITIALLY_LOW);
            mLed2.setDirection(Gpio.DIRECTION_OUT_INITIALLY_LOW);
            mLed3.setDirection(Gpio.DIRECTION_OUT_INITIALLY_LOW);
            mLed4.setDirection(Gpio.DIRECTION_OUT_INITIALLY_LOW);
            mLed5.setDirection(Gpio.DIRECTION_OUT_INITIALLY_LOW);
            mLed6.setDirection(Gpio.DIRECTION_OUT_INITIALLY_LOW);
            mLed7.setDirection(Gpio.DIRECTION_OUT_INITIALLY_LOW);

            mLedGpioRed = service.openGpio(GPIO_RGB_RED);
            mLedGpioGreen = service.openGpio(GPIO_RGB_GREEN);
            mLedGpioBlue = service.openGpio(GPIO_RGB_BLUE);

            mLedGpioRed.setDirection(Gpio.DIRECTION_OUT_INITIALLY_HIGH);
            mLedGpioGreen.setDirection(Gpio.DIRECTION_OUT_INITIALLY_HIGH);
            mLedGpioBlue.setDirection(Gpio.DIRECTION_OUT_INITIALLY_HIGH);

            mButtonInputDriver = new ButtonInputDriver(GPIO_BUTTON, Button.LogicState.PRESSED_WHEN_LOW, KeyEvent.KEYCODE_0);
            mButtonInputDriver.register();

            Runnable r=new Runnable() {
                public void run() {
                    try {
                        loop();
                        custListLoadThread.start();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            };

            custListLoadThread = new Thread(r);
            custListLoadThread.start();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void rgbSwitchesSetup(){
        redSw = (Switch) findViewById(R.id.swRed);
        greenSw = (Switch) findViewById(R.id.swGreen);
        blueSw = (Switch) findViewById(R.id.swBlue);

        redSw.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                try {
                    mLedGpioRed.setValue(!isChecked);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });


        greenSw.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                try {
                    mLedGpioGreen.setValue(!isChecked);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        blueSw.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                try {
                    mLedGpioBlue.setValue(!isChecked);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void loop() throws IOException {
        int del = 150;

        mLed1.setValue(true);
        delay(del);
        mLed2.setValue(true);
        delay(del);
        mLed3.setValue(true);
        delay(del);
        mLed4.setValue(true);
        delay(del);
        mLed5.setValue(true);
        delay(del);
        mLed6.setValue(true);
        delay(del);
        mLed7.setValue(true);
        delay(del);

        mLed1.setValue(false);
        delay(del);
        mLed2.setValue(false);
        delay(del);
        mLed3.setValue(false);
        delay(del);
        mLed4.setValue(false);
        delay(del);
        mLed5.setValue(false);
        delay(del);
        mLed6.setValue(false);
        delay(del);
        mLed7.setValue(false);
        delay(del);
    }

    public void delay(long milis){
        try{
            sleep(milis);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_0) {
            Log.i("ON KEY","DOWN");
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_0) {
            Log.i("ON KEY","UP");
            try {
                changeRGBstate();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return true;
        }

        return super.onKeyUp(keyCode, event);
    }

    private void changeRGBstate() throws IOException {
        if(rgbState < 3){
            rgbState++;
        }else{
            rgbState = 0;
        }

        if(rgbState == 0){
            redSw.setChecked(false);
            greenSw.setChecked(false);
            blueSw.setChecked(false);
            mLedGpioRed.setValue(true);
            mLedGpioGreen.setValue(true);
            mLedGpioBlue.setValue(true);
        }else if(rgbState == 1){
            redSw.setChecked(true);
            greenSw.setChecked(false);
            blueSw.setChecked(false);
            mLedGpioRed.setValue(false);
            mLedGpioGreen.setValue(true);
            mLedGpioBlue.setValue(true);
        }else if(rgbState == 2){
            redSw.setChecked(false);
            greenSw.setChecked(true);
            blueSw.setChecked(false);
            mLedGpioRed.setValue(true);
            mLedGpioGreen.setValue(false);
            mLedGpioBlue.setValue(true);
        }else if(rgbState == 3){
            redSw.setChecked(false);
            greenSw.setChecked(false);
            blueSw.setChecked(true);
            mLedGpioRed.setValue(true);
            mLedGpioGreen.setValue(true);
            mLedGpioBlue.setValue(false);
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (mButtonInputDriver!=null){
            try {
                mLed1.close();
                mLed2.close();
                mLed3.close();
                mLed4.close();
                mLed5.close();
                mLed6.close();
                mLed7.close();

                mLedGpioRed.close();
                mLedGpioBlue.close();
                mLedGpioGreen.close();

                mButtonInputDriver.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
