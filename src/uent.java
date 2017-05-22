package dev.eurie.tglfmbot;
import java.util.List;
import java.util.ArrayList;
import javax.crypto.Cipher;
import javax.json.JsonObject;
import javax.json.Json;
import java.util.Base64;

public class UEntry {
  int uid;
  String lfmKey;
  String lfmUName;
  public List<Long> chids;

  public int getUid() { return uid; }
  public String getLfmKey() { return lfmKey; }
  public String getLfmUName() { return lfmUName; }

  public UEntry(int uid, String uname, String key) {
    return UEntry(uid, uname, key, 1);
  }

  public UEntry(int uid, String uname, String key, int chids) {
    this.uid = uid;
    this.lfmKey = key;
    this.lfmUName = uname;
    chids = new ArrayList<Long>(chids);
  }

  public UEntry(JsonObject obj, byte[] secret) {
    return decode(obj, secret);
  }

  public static UEntry decode(JsobObject obj, SecretKey secKey) {
    Cipher c = Cipher.getInstance(Database.CIPHER);
    c.init(Cipher.DECRYPT_MODE, secKey);
    Base64.Decoder dec = Base64.getDecoder();
    UEntry ret = new UEntry(
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
    for(Long chid : this.chids) { chidArr.add(chid.getValue()); }
    JsonObject ret = Json.createObjectBuilder()
        .add(Database.JSON_KEY_UID, this.uid)
        .add(Database.JSON_KEY_LFM_UNAME, enc.encode(c.doFinal(lfmUName)))
        .add(Database.JSON_KEY_LFM_PASS, enc.encode(c.doFinal(lfmKey)))
        .add(Database.JSON_KEY_CHIDS, chidArr.build());
    return ret;
  }
}
