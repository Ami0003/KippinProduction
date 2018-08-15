package com.kippinretail.Modal.AddEmployeeSearch;

/**
 * Created by kamaljeet.singh on 3/24/2016.
 */

 import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

 public class SearchDataa {

    @SerializedName("Id")
    @Expose
    private Integer Id;
    @SerializedName("Email")
    @Expose
    private String Email;

    /**
     *
     * @return
     * The Id
     */
    public Integer getId() {
        return Id;
    }

    /**
     *
     * @param Id
     * The Id
     */
    public void setId(Integer Id) {
        this.Id = Id;
    }

    /**
     *
     * @return
     * The Email
     */
    public String getEmail() {
        return Email;
    }

    /**
     *
     * @param Email
     * The Email
     */
    public void setEmail(String Email) {
        this.Email = Email;
    }

}