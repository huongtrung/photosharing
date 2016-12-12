package com.hg.photoshare.contants;

import static android.R.id.message;

/**
 * Created by GMORUNSYSTEM on 12/12/2016.
 */

public class ErrorCodeUlti {

    public static String getErrorCode(int failCode) {
        String message = "";
            switch (failCode) {
                case 400:
                    message = "Bad Request . Please try again !";
                    break;
                case 403:
                    message = "Forbidden . Please try again !";
                    break;
                case 404:
                    message = "Not Found . Please try again !";
                    break;
                case 408:
                    message = "Request Time-out . Please try again !";
                    break;
                case 500:
                    message = "Internal Server Error . Please try again !";
                    break;
                case 502:
                    message = "Bad Gateway . Please try again !";
                    break;
                case 503:
                    message = "Service Unavailable . Please try again !";
                    break;
                case 520:
                    message = "Unknown Error . Please try again !";
                    break;
                case 999:
                    message = "No Internet Connect . Please check network !";
                    break;
            }
        return message;
    }
}
