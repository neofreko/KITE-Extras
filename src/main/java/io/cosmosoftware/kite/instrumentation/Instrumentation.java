/*
 * Copyright (C) CoSMo Software Consulting Pte. Ltd. - All Rights Reserved
 */

package io.cosmosoftware.kite.instrumentation;

import io.cosmosoftware.kite.exception.KiteTestException;

import javax.json.JsonArray;
import javax.json.JsonObject;
import java.util.HashMap;

/**
 * The type Instrumentation.
 */
public class Instrumentation extends HashMap<String, Instance> {

  private  String remoteAddress;
  private  String instrumentUrl;
  private String kiteServerGridId;

  /**
   * Instantiates a new Instrumentation.
   *
   * @param jsonObject the json object
   */
  public Instrumentation(JsonObject jsonObject) throws KiteTestException {
    this.instrumentUrl = null;
    this.remoteAddress = null;
    this.kiteServerGridId = null;
    JsonArray jsonArray = jsonObject.getJsonArray("instances");
    for (int i = 0; i < jsonArray.size(); i++) {
      try {
        Instance instance = new Instance(jsonArray.getJsonObject(i));
        this.put(instance.getId(), instance);
      } catch (KiteTestException e) {
        throw e;
      }
    }
  }

  public String getRemoteAddress() {
    return this.remoteAddress;
  }

  public void setRemoteAddress(String remoteAddress) {
    this.remoteAddress = remoteAddress;
  }

  public String getInstrumentUrl() {
    return this.instrumentUrl;
  }

  public void setInstrumentUrl(String instrumentUrl) {
    this.instrumentUrl = instrumentUrl;
  }

  public void setKiteServerGridId(String kiteServerGridId) {
    this.kiteServerGridId = kiteServerGridId;
  }

  public String getKiteServerGridId() {
    return this.instrumentUrl;
  }
}
