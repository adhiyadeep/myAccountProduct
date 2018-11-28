package skytechhub.myaccounts.component;

import android.app.Activity;
import android.support.annotation.Nullable;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;

import skytechhub.myaccounts.R;

/**
 * Created by sushil on 22/12/15.
 */
public class CustomDialogAdapter {

    public static void showYesNoDialog(final Activity activity, String content, MaterialDialog.SingleButtonCallback yesCallback, MaterialDialog.SingleButtonCallback noCallback) {
        new MaterialDialog.Builder(activity)
                .title(R.string.app_name)
                .iconRes(R.mipmap.ic_launcher)
                .content(content)
                .positiveText(R.string.dialog_btn_yes)
                .negativeText(R.string.dialog_btn_no)
                .onPositive(yesCallback)
                .onNegative(noCallback)
                .show();
    }

    public static void showOkDialog(final Activity activity, String content, @Nullable MaterialDialog.SingleButtonCallback okCallback) {

        if (okCallback == null) {
            new MaterialDialog.Builder(activity)
                    .title(R.string.app_name)
                    .iconRes(R.mipmap.ic_launcher)
                    .content(content)
                    .positiveText(R.string.dialog_btn_ok)
                    .onPositive(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(MaterialDialog dialog, DialogAction which) {
                            dialog.dismiss();
                        }
                    })
                    .show();
        }else {
            new MaterialDialog.Builder(activity)
                    .title(R.string.app_name)
                    .iconRes(R.mipmap.ic_launcher)
                    .content(content)
                    .positiveText(R.string.dialog_btn_ok)
                    .onPositive(okCallback)
                    .show();
        }
    }

    public static void showUnavailableInternetDialog(final Activity activity) {
        new MaterialDialog.Builder(activity)
                .title(R.string.app_name)
                .iconRes(R.mipmap.ic_launcher)
                .content(activity.getResources().getString(R.string.dialog_msg_internet_unavailable))
                .positiveText(R.string.dialog_btn_ok)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(MaterialDialog dialog, DialogAction which) {
                        dialog.dismiss();
                    }
                })
                .show();
    }

    public static void showAlertLocation(final Activity activity) {
        new MaterialDialog.Builder(activity)
                .title(R.string.app_name)
                .iconRes(R.mipmap.ic_launcher)
                .content(activity.getResources().getString(R.string.dialog_msg_internet_unavailable))
                .positiveText(R.string.dialog_btn_ok)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(MaterialDialog dialog, DialogAction which) {
                        dialog.dismiss();
                    }
                })
                .show();
    }

    public static void showYesNoDialog1(final Activity activity, String title, String content, String PosTEXT, String NegText, MaterialDialog.SingleButtonCallback yesCallback, MaterialDialog.SingleButtonCallback noCallback) {
        new MaterialDialog.Builder(activity)
                .title(title)
                .content(content)
                .positiveText(PosTEXT)
                .negativeText(NegText)
                .onPositive(yesCallback)
                .onNegative(noCallback)
                .show();
    }
}
