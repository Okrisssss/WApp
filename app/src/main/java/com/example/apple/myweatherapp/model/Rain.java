package com.example.apple.myweatherapp.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by apple on 3/24/18.
 */

public class Rain {

  @SerializedName("3h")
  @Expose
  private Double _3h;

  public Double get3h() {
    return _3h;
  }

  public void set3h(Double _3h) {
    this._3h = _3h;
  }

}
