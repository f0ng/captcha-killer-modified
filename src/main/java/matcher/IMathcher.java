package matcher;

import entity.MatchResult;

public interface IMathcher {
    public MatchResult match(String strResp, String keyword);
    public String buildKeyword(String strResp,String value);
}
