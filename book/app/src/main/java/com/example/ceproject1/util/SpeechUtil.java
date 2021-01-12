package com.example.ceproject1.util;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import com.baidu.tts.auth.AuthInfo;
import com.baidu.tts.client.SpeechError;
import com.baidu.tts.client.SpeechSynthesizer;
import com.baidu.tts.client.SpeechSynthesizerListener;
import com.baidu.tts.client.TtsMode;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;


/**
 * Created
 */

public class SpeechUtil implements SpeechSynthesizerListener {
    private Context context;
    private static SpeechSynthesizer mSpeechSynthesizer;
    private String mSampleDirPath;
    private static final String DIR_NAME = "HostComputer";
    private static final String SPEECH_FEMALE_MODEL_NAME = "bd_etts_speech_female.dat";
    private static final String SPEECH_MALE_MODEL_NAME = "bd_etts_speech_male.dat";
    private static final String TEXT_MODEL_NAME = "bd_etts_text.dat";

    //    private static final String ENGLISH_TEXT_MODEL_NAME = "bd_etts_text_en.dat";
    private static final String APP_ID = "11482351";//请更换为自己创建的应用
    private static final String API_KEY = "xYgKPkuUd3GOP9sXj35oTgyH";//请更换为自己创建的应用
    private static final String SECRET_KEY = "i1oh0LEyRKfPtjHSrYXG1xhI08lg8F27";//请更换为自己创建的应用

    public static boolean startVoiceBl = false;
    public static boolean speakFlag = true;

    public SpeechUtil(Context activity) {
        this.context = activity;
        initialEnv();
        initialTts();
    }

    private void initialEnv() {

        if (mSampleDirPath == null) {
            String sdcardPath = Environment.getExternalStorageDirectory().toString();
            mSampleDirPath = sdcardPath + "/" + DIR_NAME;
        }
        makeDir(mSampleDirPath);
        copyFromAssetsToSdcard(false, SPEECH_FEMALE_MODEL_NAME, mSampleDirPath + "/" + SPEECH_FEMALE_MODEL_NAME);
        copyFromAssetsToSdcard(false, SPEECH_MALE_MODEL_NAME, mSampleDirPath + "/" + SPEECH_MALE_MODEL_NAME);
        copyFromAssetsToSdcard(false, TEXT_MODEL_NAME, mSampleDirPath + "/" + TEXT_MODEL_NAME);
//        copyFromAssetsToSdcard(false, LICENSE_FILE_NAME, mSampleDirPath + "/" + LICENSE_FILE_NAME);
//        copyFromAssetsToSdcard(false, "english/" + ENGLISH_SPEECH_FEMALE_MODEL_NAME, mSampleDirPath + "/"
//                + ENGLISH_SPEECH_FEMALE_MODEL_NAME);
//        copyFromAssetsToSdcard(false, "english/" + ENGLISH_SPEECH_MALE_MODEL_NAME, mSampleDirPath + "/"
//                + ENGLISH_SPEECH_MALE_MODEL_NAME);
//        copyFromAssetsToSdcard(false, "english/" + ENGLISH_TEXT_MODEL_NAME, mSampleDirPath + "/"
//                + ENGLISH_TEXT_MODEL_NAME);
    }

    private void makeDir(String dirPath) {
        File file = new File(dirPath);
        if (!file.exists()) {
            file.mkdirs();
        }
    }

    /**
     * 将工程需要的资源文件拷贝到SD卡中使用（授权文件为临时授权文件，请注册正式授权）
     *
     * @param isCover 是否覆盖已存在的目标文件
     * @param source
     * @param dest
     */
    private void copyFromAssetsToSdcard(boolean isCover, String source, String dest) {
        File file = new File(dest);
        if (isCover || (!isCover && !file.exists())) {
            InputStream is = null;
            FileOutputStream fos = null;
            try {
                is = context.getResources().getAssets().open(source);
                String path = dest;
                fos = new FileOutputStream(path);
                byte[] buffer = new byte[1024];
                int size = 0;
                while ((size = is.read(buffer, 0, 1024)) >= 0) {
                    fos.write(buffer, 0, size);
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (fos != null) {
                    try {
                        fos.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                try {
                    if (is != null) {
                        is.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void initialTts() {

        this.mSpeechSynthesizer = SpeechSynthesizer.getInstance();
        this.mSpeechSynthesizer.setContext(context);
        this.mSpeechSynthesizer.setSpeechSynthesizerListener(this);
        // 文本模型文件路径 (离线引擎使用)
        this.mSpeechSynthesizer.setParam(SpeechSynthesizer.PARAM_TTS_TEXT_MODEL_FILE, mSampleDirPath + "/"
                + TEXT_MODEL_NAME);
        // 声学模型文件路径 (离线引擎使用)
        this.mSpeechSynthesizer.setParam(SpeechSynthesizer.PARAM_TTS_SPEECH_MODEL_FILE, mSampleDirPath + "/"
                + SPEECH_FEMALE_MODEL_NAME);
        // 本地授权文件路径,如未设置将使用默认路径.设置临时授权文件路径，LICENCE_FILE_NAME请替换成临时授权文件的实际路径，仅在使用临时license文件时需要进行设置，如果在[应用管理]中开通了正式离线授权，不需要设置该参数，建议将该行代码删除（离线引擎）
        // 如果合成结果出现临时授权文件将要到期的提示，说明使用了临时授权文件，请删除临时授权即可。
//        this.mSpeechSynthesizer.setParam(SpeechSynthesizer.PARAM_TTS_LICENCE_FILE, mSampleDirPath + "/"
//                + LICENSE_FILE_NAME);
        // 请替换为语音开发者平台上注册应用得到的App ID (离线授权)
        this.mSpeechSynthesizer.setAppId(APP_ID);
        // 请替换为语音开发者平台注册应用得到的apikey和secretkey (在线授权)
        this.mSpeechSynthesizer.setApiKey(API_KEY, SECRET_KEY);
        // 发音人（在线引擎），可用参数为0,1,2,3。。。（服务器端会动态增加，各值含义参考文档，以文档说明为准。0--普通女声，1--普通男声，2--特别男声，3--情感男声。。。）
        this.mSpeechSynthesizer.setParam(SpeechSynthesizer.PARAM_SPEAKER, "0");
        // 设置Mix模式的合成策略
        this.mSpeechSynthesizer.setParam(SpeechSynthesizer.PARAM_MIX_MODE, SpeechSynthesizer.MIX_MODE_HIGH_SPEED_SYNTHESIZE_WIFI);
        // 授权检测接口(只是通过AuthInfo进行检验授权是否成功。)
        // AuthInfo接口用于测试开发者是否成功申请了在线或者离线授权，如果测试授权成功了，可以删除AuthInfo部分的代码（该接口首次验证时比较耗时），不会影响正常使用（合成使用时SDK内部会自动验证授权）
        AuthInfo authInfo = this.mSpeechSynthesizer.auth(TtsMode.MIX);

        if (authInfo.isSuccess()) {
//            toPrint("auth success");
            com.baidu.android.common.logging.Log.e("yuyin_success", "auth success");
        } else {
            String errorMsg = authInfo.getTtsError().getDetailMessage();
            com.baidu.android.common.logging.Log.e("yuyin_error", "auth failed errorMsg=" + errorMsg);
        }

        // 初始化tts
        mSpeechSynthesizer.initTts(TtsMode.MIX);

        mSpeechSynthesizer.setParam(SpeechSynthesizer.PARAM_VOLUME, "9");
//        // 加载离线英文资源（提供离线英文合成功能）
//        mSpeechSynthesizer.loadEnglishModel(mSampleDirPath + "/" + ENGLISH_TEXT_MODEL_NAME, mSampleDirPath
//                + "/" + ENGLISH_SPEECH_FEMALE_MODEL_NAME);
    }

    public static void speak(String text) {
        //需要合成的文本text的长度不能超过1024个GBK字节。
        int result = mSpeechSynthesizer.speak(text);
        if (result < 0) {
            Log.e("yuyin_error", "ruslut code = " + result);
        }
    }

    /*
     * @param arg0
     */
    @Override
    public void onSynthesizeStart(String utteranceId) {
        com.baidu.android.common.logging.Log.e("yuyin", "onSynthesizeStart utteranceId=" + utteranceId);
    }

    /*
     * @param arg0
     *
     * @param arg1
     *
     * @param arg2
     */
    @Override
    public void onSynthesizeDataArrived(String utteranceId, byte[] data, int progress) {
    }

    /*
     * @param arg0
     */
    @Override
    public void onSynthesizeFinish(String utteranceId) {
        com.baidu.android.common.logging.Log.e("yuyin", "onSynthesizeFinish utteranceId=" + utteranceId);
    }

    /*
     * @param arg0
     */
    @Override
    public void onSpeechStart(String utteranceId) {
        com.baidu.android.common.logging.Log.e("yuyin", "onSpeechStart utteranceId=" + utteranceId);

            startVoiceBl = true;


    }

    /*
     * @param arg0
     *
     * @param arg1
     */
    @Override
    public void onSpeechProgressChanged(String utteranceId, int progress) {
    }

    /*
     * @param arg0
     */
    @Override
    public void onSpeechFinish(String utteranceId) {

            startVoiceBl = true;

        com.baidu.android.common.logging.Log.e("yuyin", "onSpeechFinish utteranceId=" + utteranceId);
    }

    /*
     * @param arg0
     *
     * @param arg1
     */
    @Override
    public void onError(String utteranceId, SpeechError error) {
        com.baidu.android.common.logging.Log.e("yuyin", "onError error=" + "(" + error.code + ")" + error.description + "--utteranceId=" + utteranceId);
    }
}
