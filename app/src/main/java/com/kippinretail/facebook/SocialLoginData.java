package com.kippinretail.facebook;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Rishabh on 26/04/15.
 */
public class SocialLoginData implements Parcelable
{

    public static Creator<SocialLoginData> CREATOR = new Creator<SocialLoginData>() {
        @Override
        public SocialLoginData createFromParcel(Parcel parcel)
        {
            return new SocialLoginData(parcel);
        }

        @Override
        public SocialLoginData[] newArray(int i)
        {
            return new SocialLoginData[i];
        }
    };

    private String email;
    private String first_name;
    private String last_name;
    private String birthday_date;
    private String sex;
    private String access_token;
    private String books;
    private String interests;
    private String movies;
    private String music;
    private String about_me;
    private String pic_big;
    private String socialUserID;
    private String userType;

    /**
     * Constructor to create an instantaneous object of this Class
     *
     * @param email
     * @param first_name
     * @param last_name
     * @param birthday_date
     * @param sex
     * @param access_token
     * @param books
     * @param interests
     * @param movies
     * @param music
     * @param about_me
     * @param pic_big
     * @param socialUserID
     * @param userType
     */
    public SocialLoginData(String email, String first_name, String last_name, String birthday_date,
                           String sex, String access_token, String books, String interests, String movies,
                           String music, String about_me, String pic_big, String socialUserID, String userType) {

        this.email = email;
        this.first_name = first_name;
        this.last_name = last_name;
        this.birthday_date = birthday_date;
        this.sex = sex;
        this.access_token = access_token;
        this.books = books;
        this.interests = interests;
        this.movies = movies;
        this.music = music;
        this.about_me = about_me;
        this.pic_big = pic_big;
        this.socialUserID = socialUserID;
        this.userType = userType;
    }

    public SocialLoginData(Parcel source) {

        email = source.readString();
        first_name = source.readString();
        last_name = source.readString();
        birthday_date = source.readString();
        sex = source.readString();
        access_token = source.readString();
        books = source.readString();
        interests = source.readString();
        movies = source.readString();
        music = source.readString();
        about_me = source.readString();
        pic_big = source.readString();
        socialUserID = source.readString();
        userType = source.readString();
    }

    public SocialLoginData() {

    }

    public String getEmail() {
        return email;
    }


    /**
     * @param email
     */
    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirst_name() {
        return first_name;
    }

    /**
     * @param first_name
     */

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }


    /**
     * @param last_name
     */
    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getBirthday_date() {
        return birthday_date;
    }


    /**
     * @param birthday_date
     */
    public void setBirthday_date(String birthday_date) {
        this.birthday_date = birthday_date;
    }

    public String getSex() {
        return sex;
    }

    /**
     * @param sex
     */

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getAccess_token() {
        return access_token;
    }


    /**
     * @param access_token
     */

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    public String getBooks() {
        return books;
    }


    /**
     * @param books
     */
    public void setBooks(String books) {
        this.books = books;
    }

    public String getInterests() {
        return interests;
    }


    /**
     * @param interests
     */
    public void setInterests(String interests) {
        this.interests = interests;
    }

    public String getMovies() {
        return movies;
    }


    /**
     * @param movies
     */
    public void setMovies(String movies) {
        this.movies = movies;
    }

    public String getMusic() {
        return music;
    }


    /**
     * @param music
     */

    public void setMusic(String music) {
        this.music = music;
    }

    public String getAbout_me() {
        return about_me;
    }


    /**
     * @param about_me
     */
    public void setAbout_me(String about_me) {
        this.about_me = about_me;
    }

    public String getPic_big() {
        return pic_big;
    }


    /**
     * @param pic_big
     */
    public void setPic_big(String pic_big) {
        this.pic_big = pic_big;
    }

    public String getSocialUserID() {
        return socialUserID;
    }


    /**
     * @param socialUserID
     */
    public void setSocialUserID(String socialUserID) {
        this.socialUserID = socialUserID;
    }

    public String getUserType() {
        return userType;
    }


    /**
     * @param userType
     */
    public void setUserType(String userType) {
        this.userType = userType;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int i) {

        dest.writeString(email);
        dest.writeString(first_name);
        dest.writeString(last_name);
        dest.writeString(birthday_date);
        dest.writeString(sex);
        dest.writeString(access_token);
        dest.writeString(books);
        dest.writeString(interests);
        dest.writeString(movies);
        dest.writeString(music);
        dest.writeString(about_me);
        dest.writeString(pic_big);
        dest.writeString(socialUserID);
        dest.writeString(userType);
    }
}
