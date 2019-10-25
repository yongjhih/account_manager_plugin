package io.flutter.plugins.account;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.util.Patterns;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

//import android.support.v4.content.ContextCompat;

import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.MethodChannel.MethodCallHandler;
import io.flutter.plugin.common.MethodChannel.Result;
import io.flutter.plugin.common.PluginRegistry.Registrar;

/**
 * AccountManagerPlugin
 */
public class AccountManagerPlugin implements MethodCallHandler {
    private final Registrar mRegistrar;

    public AccountManagerPlugin(Registrar registrar) {
        this.mRegistrar = registrar;
    }

    /**
     * Plugin registration.
     */
    public static void registerWith(Registrar registrar) {
        final MethodChannel channel = new MethodChannel(registrar.messenger(), "account_manager_plugin");
        channel.setMethodCallHandler(new AccountManagerPlugin(registrar));
    }

    @Override
    public void onMethodCall(MethodCall call, Result result) {
        Context context = context();
        Activity activity = activity();

        switch (call.method) {
            case "getAllAccounts":
                result.success(getAllAccounts(context, activity));
                break;
            case "getMailsAccounts":
                result.success(getMailsAccounts(context));
                break;
            default:
                result.notImplemented();
                break;
        }
    }

    private List<String> getAllAccounts(Context context, Activity activity) {

        List<String> accounts = new ArrayList<>();
        AccountManager manager = (AccountManager) context.getSystemService(Context.ACCOUNT_SERVICE);

//        if (ContextCompat.checkSelfPermission(context, Manifest.permission.GET_ACCOUNTS) != PackageManager.PERMISSION_GRANTED) {
//            if (ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.GET_ACCOUNTS)) {
//                ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.GET_ACCOUNTS}, 1);
//            } else {
//                ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.GET_ACCOUNTS}, 1);
//            }
//        } else {
            try {
                Account[] list = manager.getAccounts();

                for (Account account : list) {
                    accounts.add(account.name);
                }
            } catch (Throwable e) {
                Log.e("Error:", e.getMessage());
            }

        return accounts;
    }

    private List<String> getMailsAccounts(Context context) {
        List<String> emails = new ArrayList<>();
        Pattern mailPattern = Patterns.EMAIL_ADDRESS;
        Account[] accounts = AccountManager.get(context).getAccounts();
        for (Account account : accounts) {
            if (mailPattern.matcher(account.name).matches()) {
                emails.add(account.name);
            }
        }
        return emails;
    }

    private Context context() {
        return mRegistrar.context();
        //return (mRegistrar.activity() != null) ? mRegistrar.activity() : mRegistrar.context();
    }

    private Activity activity() {
        return mRegistrar.activity();
    }
}
