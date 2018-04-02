package vo;

import org.apache.commons.lang.StringUtils;

public class AccessResult {
    public String access_token;
    public int expires_in;
    public String errcode;
    public String errmsg;

    public boolean succ() {
        if (StringUtils.isNotBlank(this.access_token)
                && this.expires_in == 7200) {
            return true;
        } else {
            return false;
        }
    }


    public boolean baiduAudioSuc() {
        if (StringUtils.isNotBlank(this.access_token)) {
            return true;
        } else {
            return false;
        }
    }
}
