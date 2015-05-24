package com.example.alumincan.mtbf;

import android.app.Instrumentation;
import android.content.Intent;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.test.uiautomator.UiAutomatorInstrumentationTestRunner;
import android.support.test.uiautomator.UiAutomatorTestCase;
import android.support.test.uiautomator.UiDevice;
import android.support.test.uiautomator.UiObject;
import android.support.test.uiautomator.UiObjectNotFoundException;
import android.support.test.uiautomator.UiScrollable;
import android.support.test.uiautomator.UiSelector;
import android.test.InstrumentationTestCase;
import android.content.Context;
import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import cci.mtbf.utils.MTBF;

/**
 * Created by alumincan on 2015/4/27.
 */
public class MediaTester extends InstrumentationTestCase {
    private final String TAG = "MediaTester";
    private Context mContext;
    private UiDevice mDevice;
    private final String INTENT_CAMRECORDER = "android.media.action.VIDEO_CAMERA";
    private final String INTENT_CAMERA = "android.media.action.STILL_IMAGE_CAMERA";
    private final String INTENT_RECORDER  = "com.provider.MediaStore.RECORD_SOUND";
    private final String recordingPath = "storage/emulated/legacy/DCIM/Camera";
    private final long recordingTime = 30 * 1000;

    private  final String MTBF_TAG = "MBTF._5_1_7_Multi_Mdeia";
    public void test01_VideoRecorder () throws UiObjectNotFoundException {

        mDevice.pressHome();
        Intent intent = new Intent(INTENT_CAMRECORDER);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        mContext.startActivity(intent);
        UiObject iShutterButtom = new UiObject(new UiSelector().resourceId("org.codeaurora.snapcam:id/shutter_button"));
        iShutterButtom.click();
        try {
            Thread.sleep(recordingTime);
        } catch (InterruptedException e) {
            MTBF.log(MTBF._5_1_7_Multi_Media, "MTBF._RecordingVideo", MTBF.PASS, 1, MTBF.E_CANNOT_CLICK, "");
            e.printStackTrace();
        }
        iShutterButtom.click();
        UiObject thumb = new UiObject(new UiSelector().resourceId("org.codeaurora.snapcam:id/preview_thumb"));
        UiObject pause = new UiObject(new UiSelector().resourceId("org.codeaurora.snapcam:id/video_pause"));
        if (thumb.exists() && !pause.exists()) {
            MTBF.log(MTBF._5_1_7_Multi_Media, "MTBF._RecordingVideo", MTBF.PASS, 1, MTBF.E_NO_ERROR, "");
            //Log.d(TAG, "Pass");
        } else {
            MTBF.log(MTBF._5_1_7_Multi_Media, "MTBF._RecordingVideo", MTBF.FAIL, 1, MTBF.E_CANNOT_CLICK, "");
            Log.d(TAG, "Fail");
        }
    }

    public void test02_PlayVideoRecord() {

        try {
            Thread.sleep(100 * 5);
            UiObject thumb = new UiObject(new UiSelector().resourceId("org.codeaurora.snapcam:id/preview_thumb"));
            thumb.clickAndWaitForNewWindow();
            Thread.sleep(100 * 5);
            mDevice.click(274,496);
            Thread.sleep(100 * 5);
            UiObject photo = new UiObject(new UiSelector().text("Photos"));
            if (photo.exists()) {
                photo.click();
            }
            Thread.sleep(1000 * 1);
            UiObject once = new UiObject(new UiSelector().resourceId("android:id/button_once"));
            once.click();
            Thread.sleep(recordingTime);

            Thread.sleep(1000 * 2);
            MTBF.log(MTBF._5_1_7_Multi_Media, "MTBF._PlayRecordingVideo", MTBF.PASS, 1, MTBF.E_NO_ERROR, "");
        } catch (InterruptedException e) {
            MTBF.log(MTBF._5_1_7_Multi_Media, "MTBF._PlayRecordingVideo", MTBF.FAIL, 1, MTBF.E_CANNOT_CLICK, "");
            e.printStackTrace();
        } catch (UiObjectNotFoundException e) {
            MTBF.log(MTBF._5_1_7_Multi_Media, "MTBF._PlayRecordingVideo", MTBF.FAIL, 1, MTBF.E_CANNOT_CLICK, "");
            e.printStackTrace();
        }
    }
    public void test03_DeleteVideo() {
        try {
            UiObject op = new UiObject(new UiSelector().description("More options"));

            if (!op.exists()) {
                mDevice.click(200, 200);
            }
            Thread.sleep(100 * 5);
            op.click();
            UiObject deleteBtn = new UiObject(new UiSelector().resourceId("android:id/title")
                                                            .text("Delete"));
            deleteBtn.click();
            Thread.sleep(100 * 5);
            UiObject ok = new UiObject(new UiSelector().resourceId("android:id/button1").text("OK"));

            boolean result = ok.click();
            if (result) {
                MTBF.log(MTBF._5_1_7_Multi_Media, "MTBF._DelRecordingVideo", MTBF.PASS, 1, MTBF.E_CANNOT_CLICK, "");
            } else {
                MTBF.log(MTBF._5_1_7_Multi_Media, "MTBF._DelRecordingVideo", MTBF.FAIL, 1, MTBF.E_CANNOT_CLICK, "");
            }
            Thread.sleep(1000 * 3);

        } catch (Exception e) {
            MTBF.log(MTBF._5_1_7_Multi_Media, "MTBF._DelRecordingVideo", MTBF.FAIL, 1, MTBF.E_CANNOT_CLICK, "");
            e.printStackTrace();
        }

    }
    public void test04_CapturePic() {
        Intent intent = new Intent(INTENT_CAMERA);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        mContext.startActivity(intent);
        UiObject iShutterButtom = new UiObject(new UiSelector().resourceId("org.codeaurora.snapcam:id/shutter_button")
                                                                .description("Shutter"));
        for (int i = 0; i < 20; i++) {
            try {
                boolean result =  iShutterButtom.click();
                if(result) {
                    MTBF.log(MTBF._5_1_7_Multi_Media, "MTBF._TakePicture", MTBF.PASS, i, MTBF.E_NO_ERROR, "");
                } else {
                    MTBF.log(MTBF._5_1_7_Multi_Media, "MTBF._TakePicture", MTBF.FAIL, i, MTBF.E_CANNOT_CLICK, "");
                }
                Thread.sleep(1000 * 2);
            } catch (InterruptedException e) {
                MTBF.log(MTBF._5_1_7_Multi_Media, "MTBF._TakePicture", MTBF.FAIL, i, MTBF.E_CANNOT_CLICK, "");
                e.printStackTrace();
            } catch (UiObjectNotFoundException e) {
                MTBF.log(MTBF._5_1_7_Multi_Media, "MTBF._TakePicture", MTBF.FAIL, i, MTBF.E_CANNOT_CLICK, "");
                e.printStackTrace();
            }
        }
    }

    public void test05_OpenPicture() {
        try {
            UiObject thumb = new UiObject(new UiSelector().resourceId("org.codeaurora.snapcam:id/preview_thumb"));
            thumb.clickAndWaitForNewWindow();
            MTBF.log(MTBF._5_1_7_Multi_Media, "MTBF._OpenPicture", MTBF.PASS, 0, MTBF.E_NO_ERROR, "");
        } catch (Exception e) {
            MTBF.log(MTBF._5_1_7_Multi_Media, "MTBF._OpenPicture", MTBF.FAIL, 0, MTBF.E_CANNOT_CLICK, "");
            e.printStackTrace();
        }
        for (int i = 0; i < 19; i++) {
            try {
                UiScrollable select = new UiScrollable(new UiSelector().className(android.view.View.class.getName()));
                select = select.setAsHorizontalList();
                boolean result =  select.scrollForward();
                if(result) {
                    MTBF.log(MTBF._5_1_7_Multi_Media, "MTBF._OpenPicture", MTBF.PASS, i + 1, MTBF.E_NO_ERROR, "");
                } else {
                    MTBF.log(MTBF._5_1_7_Multi_Media, "MTBF._OpenPicture", MTBF.FAIL, i + 1, MTBF.E_CANNOT_CLICK, "");
                }

                Thread.sleep(1000 * 2);
            } catch (Exception e) {
                MTBF.log(MTBF._5_1_7_Multi_Media, "MTBF._OpenPicture", MTBF.FAIL, i + 1, MTBF.E_CANNOT_CLICK, "");
                e.printStackTrace();
            }
        }

    }

    public void test06_DeletePic() {
        for (int i = 0; i < 20; i++) {
            try {
                Thread.sleep(100 * 5);
                UiObject op = new UiObject(new UiSelector().description("More options"));
                if (!op.exists()) {
                    mDevice.click(100, 100);
                }
                Thread.sleep(100 * 5);
                op.click();
                UiObject deleteBtn = new UiObject(new UiSelector().resourceId("android:id/title")
                        .text("Delete"));
                deleteBtn.click();
                Thread.sleep(100 * 5);
                UiObject ok = new UiObject(new UiSelector().resourceId("android:id/button1").text("OK"));
                boolean result = ok.clickAndWaitForNewWindow();
                if(result) {
                    MTBF.log(MTBF._5_1_7_Multi_Media, "MTBF._DelPicture", MTBF.PASS, i , MTBF.E_NO_ERROR, "");
                } else {
                    MTBF.log(MTBF._5_1_7_Multi_Media, "MTBF._DelPicture", MTBF.FAIL, i + 1, MTBF.E_CANNOT_CLICK, "");
                }
                Log.d(TAG, "delete " + String.valueOf(i));
                Thread.sleep(100 * 5);
            } catch (Exception e) {
                MTBF.log(MTBF._5_1_7_Multi_Media, "MTBF._DelPicture", MTBF.FAIL, i , MTBF.E_CANNOT_CLICK, "");
                e.printStackTrace();
            }
        }
    }

    public void test07_RecordAudio() {
        mDevice.pressHome();
        Intent intent = new Intent();
        intent.setClassName("com.android.soundrecorder", "com.android.soundrecorder.SoundRecorder");
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        mContext.startActivity(intent);

        UiObject recorder = new UiObject(new UiSelector().resourceId("com.android.soundrecorder:id/recordButton"));
        UiObject stop = new UiObject(new UiSelector().resourceId("com.android.soundrecorder:id/stopButton"));
        try {
            recorder.click();
            Thread.sleep(1000 * 5);
            stop.click();
            Thread.sleep(1000 * 1);
            UiObject delete = new UiObject(new UiSelector().resourceId("com.android.soundrecorder:id/discardButton"));
            if (delete.exists()) {
                MTBF.log(MTBF._5_1_7_Multi_Media, "MTBF._RecordAudio", MTBF.PASS, 0 , MTBF.E_NO_ERROR, "");
                Log.d(TAG, "Pass");
            } else {
                MTBF.log(MTBF._5_1_7_Multi_Media, "MTBF._RecordAudio", MTBF.FAIL, 0 , MTBF.E_CANNOT_CLICK, "");
                Log.d(TAG, "Fail");
            }
        } catch (UiObjectNotFoundException e) {
            MTBF.log(MTBF._5_1_7_Multi_Media, "MTBF._RecordAudio", MTBF.FAIL, 0 , MTBF.E_CANNOT_CLICK, "");
            e.printStackTrace();
        } catch (Exception e) {
            MTBF.log(MTBF._5_1_7_Multi_Media, "MTBF._RecordAudio", MTBF.FAIL, 0 , MTBF.E_CANNOT_CLICK, "");
            e.printStackTrace();
        }

    }

    public void test08_PlayRecordAudio() {
        UiObject play = new UiObject(new UiSelector().resourceId("com.android.soundrecorder:id/playButton"));
        try {
            boolean result = play.click();
            Thread.sleep(1000 * 5);
            if(result) {
                MTBF.log(MTBF._5_1_7_Multi_Media, "MTBF._PlayRecord", MTBF.PASS, 1 , MTBF.E_NO_ERROR, "");
            } else {
                MTBF.log(MTBF._5_1_7_Multi_Media, "MTBF._PlayRecord", MTBF.FAIL, 1, MTBF.E_CANNOT_CLICK, "");
            }
        } catch (UiObjectNotFoundException e) {
            MTBF.log(MTBF._5_1_7_Multi_Media, "MTBF._PlayRecord", MTBF.FAIL, 1, MTBF.E_CANNOT_CLICK, "");
            e.printStackTrace();
        } catch (Exception e) {
            MTBF.log(MTBF._5_1_7_Multi_Media, "MTBF._PlayRecord", MTBF.FAIL, 1, MTBF.E_CANNOT_CLICK, "");
            e.printStackTrace();
        }
    }

    public void test09_DeleteRecord () {
        UiObject delete = new UiObject(new UiSelector().resourceId("com.android.soundrecorder:id/discardButton"));
        UiObject ok = new UiObject(new UiSelector().resourceId("android:id/button1").text("OK"));
        try {
            delete.clickAndWaitForNewWindow();
            if (ok.exists()) {
                ok.click();
            }
            UiObject time = new UiObject(new UiSelector().resourceId("com.android.soundrecorder:id/timerView").text("00:00"));
            UiObject status = new UiObject(new UiSelector().resourceId("com.android.soundrecorder:id/stateMessage2")
                        .text("Press record"));

            if (time.exists() && status.exists()) {
                MTBF.log(MTBF._5_1_7_Multi_Media, "MTBF._DelRecord", MTBF.PASS, 1 , MTBF.E_NO_ERROR, "");
            } else {
                MTBF.log(MTBF._5_1_7_Multi_Media, "MTBF._DelRecord", MTBF.FAIL, 1, MTBF.E_CANNOT_CLICK, "");
            }
        } catch (UiObjectNotFoundException e) {
            MTBF.log(MTBF._5_1_7_Multi_Media, "MTBF._DelRecord", MTBF.FAIL, 1, MTBF.E_CANNOT_CLICK, "");
            e.printStackTrace();
        } catch (Exception e) {
            MTBF.log(MTBF._5_1_7_Multi_Media, "MTBF._DelRecord", MTBF.FAIL, 1, MTBF.E_CANNOT_CLICK, "");
            e.printStackTrace();
        }
    }
    public void test10_PlayStreaming () {
        for (int i = 0; i < 10; i ++) {
            String url = "rtsp://wowzaec2demo.streamlock.net/vod/mp4:BigBuckBunny_115k.mov";
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            mContext.startActivity(intent);
            try {
                Thread.sleep(100 * 5);
                UiObject videoPlayer = new UiObject(new UiSelector().resourceId("android:id/text1").text("Video player"));
                if (videoPlayer.exists()) {
                    videoPlayer.click();
                    Thread.sleep(100 * 5);
                }
                UiObject once = new UiObject(new UiSelector().resourceId("android:id/button_once"));
                once.click();

                UiObject loading = new UiObject(new UiSelector().text("Loading video…").index(1));
                loading.waitUntilGone(1000 * 10);
                if (loading.exists()) {
                    MTBF.log(MTBF._5_1_7_Multi_Media, "MTBF._PlayStreaming", MTBF.FAIL, i, MTBF.E_TIMEOUT, "");
                    Log.d(TAG, "Fail, timeout");
                } else {
                    Thread.sleep(1000 * 5);
                    mDevice.click(100, 100);
                    Thread.sleep(1000 * 5);
                    mDevice.click(100, 100);

                    UiObject play = new UiObject(new UiSelector().description("Play video"));
                    play.waitForExists(1000 * 10);
                    if (play.exists()) {
                        MTBF.log(MTBF._5_1_7_Multi_Media, "MTBF._PlayStreaming", MTBF.PASS , i, MTBF.E_NO_ERROR, "");
                    } else {
                        MTBF.log(MTBF._5_1_7_Multi_Media, "MTBF._PlayStreaming", MTBF.FAIL, i, MTBF.E_CANNOT_CLICK, "");
                    }
                }
                mDevice.pressBack();

            } catch (Exception e) {
                MTBF.log(MTBF._5_1_7_Multi_Media, "MTBF._PlayStreaming", MTBF.FAIL, i, MTBF.E_CANNOT_CLICK, "");
                e.printStackTrace();
            }
        }


    }
    public void test11_LaunchMusic () {
        Intent intent = mContext.getPackageManager().getLaunchIntentForPackage("com.google.android.music");
        for (int i = 0; i < 20; i++) {
            try {
                mContext.startActivity(intent);
                Thread.sleep(1000 * 3);
                UiObject temp = new UiObject(new UiSelector().resourceId("com.google.android.music:id/header_title_main"));
                if (temp.exists()) {
                    MTBF.log(MTBF._5_1_7_Multi_Media, "MTBF._LaunchMusic", MTBF.PASS, i, MTBF.E_NO_ERROR, "");
                }
                mDevice.pressBack();
            } catch (Exception e) {
                MTBF.log(MTBF._5_1_7_Multi_Media, "MTBF._LaunchMusic", MTBF.FAIL, i, MTBF.E_CANNOT_CLICK, "");
                e.printStackTrace();
            }
        }
    }

    public void test12_PlayMusic () {
        Intent intent = mContext.getPackageManager().getLaunchIntentForPackage("com.google.android.music");
        mContext.startActivity(intent);
        try {
            UiObject op = new UiObject(new UiSelector().description("Show navigation drawer"));
            op.clickAndWaitForNewWindow();
            UiObject lib = new UiObject(new UiSelector().text("My Library"));
            lib.clickAndWaitForNewWindow();
            UiObject songs = new UiObject(new UiSelector().text("SONGS").index(3));
            songs.clickAndWaitForNewWindow();
            UiObject shuffle = new UiObject(new UiSelector().resourceId("com.google.android.music:id/shuffle_all_songs"));
            shuffle.click();
            UiObject header = new UiObject(new UiSelector().resourceId("com.google.android.music:id/header_text"));
            header.click();
        } catch (UiObjectNotFoundException e) {
            e.printStackTrace();
        }

        for (int i = 0; i < 50; i ++) {
            try {
                long time = 0;
                if (i == 0) {
                    time = 14;
                } else {
                    time = 15;
                }
                Thread.sleep(1000 * time);
                UiObject next = new UiObject(new UiSelector().resourceId("com.google.android.music:id/next"));
                boolean result = next.click();
                if(result) {
                    MTBF.log(MTBF._5_1_7_Multi_Media, "MTBF._PlayMusic", MTBF.PASS, i , MTBF.E_NO_ERROR, "");
                } else {
                    MTBF.log(MTBF._5_1_7_Multi_Media, "MTBF._PlayMusic", MTBF.FAIL, i + 1, MTBF.E_CANNOT_CLICK, "");
                }
            } catch (UiObjectNotFoundException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        try {
            UiObject op = new UiObject(new UiSelector().resourceId("com.google.android.music:id/overflow"));
            op.clickAndWaitForNewWindow();
            UiObject clear = new UiObject(new UiSelector().text("Clear queue"));
            clear.clickAndWaitForNewWindow();
        } catch (UiObjectNotFoundException e) {
            MTBF.log(MTBF._5_1_7_Multi_Media, "MTBF._PlayMusic", MTBF.FAIL, 0, MTBF.E_CANNOT_CLICK, "");
            e.printStackTrace();
        }
    }

    public void test13_CLoseMusic () {
        mDevice.pressBack();
        mDevice.pressHome();
        MTBF.log(MTBF._5_1_7_Multi_Media, "MTBF._CloseMusic", MTBF.PASS, 0 , MTBF.E_NO_ERROR, "");
    }

    public void test14_ChangeTheme () {
        MTBF.log(MTBF._5_1_7_Multi_Media, "MTBF._PlayMusic", MTBF.SKIP, 0, MTBF.E_UNKNOWN, "");
    }
    @Override
    protected void setUp() {
        try {
            super.setUp();
            mContext = this.getInstrumentation().getContext();
            mDevice = UiDevice.getInstance(getInstrumentation());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
