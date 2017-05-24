package dev.eurie.tglfmbot;

import java.nio.charset.Charset;
import org.apache.commons.cli2.*;
import org.apache.commons.cli2.builder.*;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;


public class TglScrobbleBot extends TelegramLongPollingBot {
  private String lfmApiKey;
  private String tglApiKey;
  private SecretKey dbSecret;

  public TglScrobbleBot() {
    this.lfmApiKey = null;
    this.tglApiKey = null;
    this.dbSecret = null;
  }

  Argument finArg(ArgumentBuilder b) {
    b.withMaximum(1);
    b.withMinimum(1);
    b.required(true);
    return b.create();
  }

  public void main(String[] args) {
    ArgumentBuilder abuilder = new ArgumentBuilder();

    Argument cipher = abuilder.withName("cipher")
        .withMaximum(1)
        .withMinimum(1)
        .withDescription("Cipher to use")
        .withDefault("AES")
        .required(false)
        .create();

    abuilder.withName("secret");
    abuilder.withDescription("Cipher key file path");
    Argument aesKeyStr = finArg(abuilder);

    abuilder.withName("lfm-key");
    abuilder.withDescription("last.fm API key");
    Argument lfmKeyStr = finArg(abuilder);

    abuilder.withName("tgl-key");
    abuilder.withDescription("Telegram Bot API key");
    Argument tglKeyStr = finArg(abuilder);

    Parser p = new Parser();
    Group opts = new GroupBuilder()
        .withOption(aesKeyStr)
        .withOption(lfmKeyStr)
        .withOption(tglKeyStr)
        .create();
    p.setGroup(opts);
    // CommandLine cl = p.parseAndHelp(args);
    Option[] parsed = p.parseAndHelp(args).getOptions();
    for(int i = 0; i < parsed.length; i++) {
      switch(parsed[i]) {
        case aesKeyStr:
          RandomAccessFile raf = new RandomAccessFile(parsed[i].getValue(), "r");
          byte[] buf = new byte[raf.length()];
          raf.readFully(buf);
          this.dbKey = new SecretKeySpec(buf, cipher.getValue());
          break;
        case lfmKeyStr: this.lfmApiKey = parsed[i].getValue(); break;
        case tglKeyStr: this.tglApiKey = parsed[i].getValue(); break;
      }
    }
  }
}
