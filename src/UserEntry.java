package dev.eurie.tglfmbot;
import java.util.List;
import java.util.ArrayList;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.json.JsonObject;
import javax.json.JsonLocation;
import javax.json.JsonParser.Event;
import javax.json.Json;
import java.util.Base64;
import java.io.FileOutputStream;

public class UserEntry {
  private int uid;
  private String lfmKey;
  private String lfmUName;
  private List<Long> chids;

  public int getUid() { return uid; }
  public String getLfmKey() { return lfmKey; }
  public String getLfmUName() { return lfmUName; }
  public List<Long> getChids() { return this.chids; }

  public void setLfmKey(String key) { this.lfmKey = key; }
  public void setLfmUName(String uname) { this.lfmUName = uname; }

  public UserEntry(int uid, String uname, String key, int chids) {
    this.uid = uid;
    this.lfmKey = key;
    this.lfmUName = uname;
    this.chids = new ArrayList<Long>(chids);
  }

  public UserEntry(int uid, String uname, String key) {
    return UserEntry(uid, uname, key, 1);
  }

  public boolean rmChannel(long id) {
    for(int i = 0; i < this.chids.length(); i++) {
      if(this.chids.get(i).getLong() == id) {
        this.chids.remove(i);
        return true;
      }
    }
    return false;
  }

  public void addChannel(long id) {
    this.chids.add(new Long(id));
  }

  public static UserEntry decode(JsobObject obj, SecretKey secKey) {
    Cipher c = Cipher.getInstance(Database.CIPHER);
    c.init(Cipher.DECRYPT_MODE, secKey);
    Base64.Decoder dec = Base64.getDecoder();
    UserEntry ret = new UserEntry(
        obj.getInt(Database.JSON_KEY_UID),
        c.doFinal(dec.decode(obj.getString(Database.JSON_KEY_LFM_UNAME))),
        c.doFinal(dec.decode(obj.getString(Database.JSON_KEY_LFM_PASS))));
    for(long id : obj.getJsonArray(Database.JSON_KEY_CHIDS)) {
      ret.add(id);
    }
    return ret;
  }

  public JsonObject encode(SecretKey secKey) {
    Cipher c = Cipher.getInstance(Database.CIPHER);
    c.init(Cipher.ENCRYPT_MODE, secKey);
    Base64.Encoder enc = Base64.getEncoder();
    JsonArray chidArr = Json.createArrayBuilder();
    for(Long chid : this.chids) { chidArr.add(chid.longValue()); }
    JsonObject ret = Json.createObjectBuilder()
        .add(Database.JSON_KEY_UID, this.uid)
        .add(Database.JSON_KEY_LFM_UNAME, enc.encode(c.doFinal(lfmUName)))
        .add(Database.JSON_KEY_LFM_PASS, enc.encode(c.doFinal(lfmKey)))
        .add(Database.JSON_KEY_CHIDS, chidArr.build());
    return ret;
  }
}
