package com.beihui.market.helper;


import android.content.Context;
import android.content.SharedPreferences;

import com.beihui.market.entity.UserProfile;
import com.beihui.market.entity.UserProfileAbstract;
import com.google.gson.Gson;

public class UserHelper {

    private static UserHelper sInstance;

    private Gson gson = new Gson();

    private Profile profile;

    private UserHelper(Context context) {
        String json = readFromSp(context.getApplicationContext());
        if (json != null) {
            profile = gson.fromJson(json, Profile.class);
        }
    }

    public static UserHelper getInstance(Context context) {
        if (sInstance == null) {
            synchronized (UserHelper.class) {
                if (sInstance == null) {
                    sInstance = new UserHelper(context.getApplicationContext());
                }
            }
        }
        return sInstance;
    }

    public Profile getProfile() {
        return profile;
    }

    public void update(UserProfile param, Context context) {
        if (param != null) {
            if (profile == null) {
                profile = new Profile();
            }
            profile.setUserName(param.getUserName());
            profile.setHeadPortrait(param.getHeadPortrait());
            profile.setAccount(param.getAccount());
            profile.setProfession(param.getProfession());

            saveUserToSp(context.getApplicationContext());
        }
    }

    public void update(UserProfileAbstract param, String account, Context context) {
        if (param != null) {
            if (profile == null) {
                profile = new Profile();
            }
            profile.setId(param.getId());
            profile.setHeadPortrait(param.getHeadPortrait());
            profile.setMsgIsRead(param.getMsgIsRead());
            profile.setUserName(param.getUserName());
            profile.setAccount(account);

            saveUserToSp(context.getApplicationContext());
        }
    }

    public void updateUsername(String username, Context context) {
        if (username != null) {
            profile.setUserName(username);

            saveUserToSp(context.getApplicationContext());
        }
    }

    public void updateAvatar(String avatar, Context context) {
        if (avatar != null) {
            profile.setHeadPortrait(avatar);

            saveUserToSp(context.getApplicationContext());
        }
    }

    public void updateProfession(String profession, Context context) {
        if (profession != null) {
            profile.setProfession(profession);

            saveUserToSp(context.getApplicationContext());
        }
    }

    public void clearUser(Context context) {
        profile = null;
        saveUserToSp(context);
    }

    private void saveUserToSp(Context context) {
        String json = null;
        if (profile != null) {
            json = gson.toJson(profile);
        }
        SharedPreferences sp = context.getSharedPreferences("UserHelper", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("profile", json);
        editor.apply();
    }

    private String readFromSp(Context context) {
        SharedPreferences sp = context.getSharedPreferences("UserHelper", Context.MODE_PRIVATE);
        return sp.getString("profile", null);
    }

    public static class Profile {
        String id;
        String account;
        String userName;
        String headPortrait;
        String profession;
        String msgIsRead;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getAccount() {
            return account;
        }

        public void setAccount(String account) {
            this.account = account;
        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public String getHeadPortrait() {
            return headPortrait;
        }

        public void setHeadPortrait(String headPortrait) {
            this.headPortrait = headPortrait;
        }

        public String getProfession() {
            return profession;
        }

        public void setProfession(String profession) {
            this.profession = profession;
        }

        public String getMsgIsRead() {
            return msgIsRead;
        }

        public void setMsgIsRead(String msgIsRead) {
            this.msgIsRead = msgIsRead;
        }
    }
}
