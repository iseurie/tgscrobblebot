package dev.eurie.tglfmbot;

import java.nio.file.Files;
import java.nio.charset.Charset;
import java.io.File;
import java.io.FileInputStream;
import javax.crypto.*;

public class Database {
  public final String JSON_KEY_LFM_UNAME = "lfm_uname";
  public final String JSON_KEY_LFM_PASS = "lfm_key";
  public final String JSON_KEY_CHIDS = "chats";
  public final String JSON_KEY_UID = "uid";
  public final String CIPHER = "AES";
  File file;
  SecretKey secret;

  public Database(byte[] secret, File file) {
    this.secret = new SecretKeySpec(secret, CIPHER);
    this.file = file;
  }

  public void rmEntry(int uid) throws Exception {
    long line = findEntById(uid).getLineNumber();
    List<String> lines = Files.readAllLines(this.file.getAbsolutePath(),
        Charset.defaultCharset());
    lines.remove(line);
    BufferedWriter w = new BufferedWriter(new FileOutputStream(this.file));
    for(int i = 0; i < lines.length(); i++) {
      w.write(lines.get(i));
      w.newLine();
    }
  }

  public void writeEntry(UEntry newEnt) throws Exception {
      // search current DOM for any entries matching this ID
      JsonLocation loc = findEntById(newEnt.getUid());
      FileOutputStream ofstream = new FileOutputStream(this.file);
      String nStr = newEnt.encode().toString();
      if(loc != null) {
        List<String> lines = Files.readAllLines(this.file.getAbsolutePath(),
            Charset.defaultCharset());
        lines.set(loc.getLineNumber(), nStr);
        BufferedWriter w = new BufferedWriter(new FileOutputStream(this.file));
        for(int i = 0; i < lines.length(); i++) {
          w.write(lines.get(i));
          w.newLine();
        }
        w.close();
      } else {

      }
  }

  JsonLocation findEntById(int uid) throws Exception {
    FileInputStream fstream = new FileInputStream(this.file);
    JsonParser p = Json.createParser(fstream);
    while(p.hasNext()) {
      Event e = parser.next();
      if(e == Event.KEY_NAME && p.getString().equals(JSON_KEY_UID)) {
        e = p.next();
        if(e == Event.KEY_NAME && p.getInt() == uid) {
          return p.getLocation();
        }
      }
    }
    return null;
  }

  /* long findEntById(int uid, byte[] out) {
    JsonParser p = Json.createParser(new FileInputStream(this.file));
    long floc;
    while(p.hasNext()) {
      Event e = parser.next();
      if(e == Event.START_OBJECT) {
        bfloc = p.getLocation().getStreamOffset();
      } else if(e == Event.KEY_NAME
          && p.getString().equals(JSON_KEY_UID)) {
        e = p.next();
        if(id != p.getInt()) { break; }
        long ffloc;
        while(p.hasNext()) {
          Event e = p.next();
          if(e == Event.END_OBJECT) {
            ffloc = p.getLocation().getStreamOffset();
            p.close();
            break;
          }
        }
        FileInputStream fstream = new FileInputStream(this.file);
        byte[ffloc-bfloc] json;
        fstream.read(json, bfloc, json.length);
        out = json;
        return bfloc;
        // Json.createReader(new ByteArrayInputStream(json)).readObject();
      }
    }
    return -1;
  }*/
}
