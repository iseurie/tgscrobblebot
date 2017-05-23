package dev.eurie.tglfmbot;

import org.apache.commons.cli2.*;
import org.apache.commons.cli2.builder.*;

public class TglScrobbleBot {
  FileValidator fv;

  Argument finArg(ArgumentBuilder b, Validator v) {
    b.withMaximum(1);
    b.withMinimum(1);
    if(v != null) b.withValidator(v);
    return b.create();
  }

  Argument finArg(ArgumentBuilder b) {
    confArg(b, null);
  }

  void parse(String[] args) {
    FileValidator fv = FileValidator.getExistingFileInstance();
    fv.setReadable(true);
    fv.setWritable(false);
    fv.setHidden(false);

    Argument aesKeyStr = finArg(new ArgumentBuilder()
            .withDescription("AES key to use"));
    Argument lfmKeyStr = finArg(new ArgumentBuilder()
            .withDescription("last.fm key to use"));
    Argument tglKeyStr = finArg(new ArgumentBuilder()
        .withDescription("Telegram key to use"));
    Argument aesKeyPath = finArg(new ArgumentBuilder()
        .withDescription("AES keyfile path"), fv);
    Argument lfmKeyPath = finArg(new ArgumentBuilder()
        .withDescription("last.fm API keyfile path"), fv);
    Argument tglKeyPath = finArg(new ArgumentBuilder()
        .withDescription("Telegram API keyfile path"), fv);

    Parser p = new Parser();

    // TODO: group arguments, add groups to parser
    // use path and warn if both keystr and path are passed

  }

  public void main(String[] args) {
    // TODO: implement command-line options
    // AES and last.fm and Telegram API key file paths (or values)

  }
}
