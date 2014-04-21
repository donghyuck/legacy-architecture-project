/*
 * Copyright 2012, 2013 Donghyuck, Son
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package architecture.ee.web.community.social.twitter.api;


import org.scribe.builder.api.DefaultApi10a;
import org.scribe.model.Token;
/**
 * 트위처 보안강화 조치(https://dev.twitter.com/discussions/24239) 에 따른 SSL 적용을 위한 API 변경임.
 * scribe 의 개발자 fernandezpablo85 님의 소스를 가져온것임.
 * 향후 라이브러리 jar 에 적용이 된이후에는 이코드를 사용하지 않아도 됨.
 * @author donghyuck
 *
 */
public class SSLTwitterApi extends DefaultApi10a
{
  private static final String AUTHORIZE_URL = "https://api.twitter.com/oauth/authorize?oauth_token=%s";
  private static final String REQUEST_TOKEN_RESOURCE = "api.twitter.com/oauth/request_token";
  private static final String ACCESS_TOKEN_RESOURCE = "api.twitter.com/oauth/access_token";
  
  @Override
  public String getAccessTokenEndpoint()
  {
    return "https://" + ACCESS_TOKEN_RESOURCE;
  }

  @Override
  public String getRequestTokenEndpoint()
  {
    return "https://" + REQUEST_TOKEN_RESOURCE;
  }

  @Override
  public String getAuthorizationUrl(Token requestToken)
  {
    return String.format(AUTHORIZE_URL, requestToken.getToken());
  }

  public static class SSL extends SSLTwitterApi
  {
    @Override
    public String getAccessTokenEndpoint()
    {
      return "https://" + ACCESS_TOKEN_RESOURCE;
    }

    @Override
    public String getRequestTokenEndpoint()
    {
      return "https://" + REQUEST_TOKEN_RESOURCE;
    }
  }

  /**
   * Twitter 'friendlier' authorization endpoint for OAuth.
   *
   * Uses SSL.
   */
  public static class Authenticate extends SSL
  {
    private static final String AUTHENTICATE_URL = "https://api.twitter.com/oauth/authenticate?oauth_token=%s";

    @Override
    public String getAuthorizationUrl(Token requestToken)
    {
      return String.format(AUTHENTICATE_URL, requestToken.getToken());
    }
  }

  /**
   * Just an alias to the default (SSL) authorization endpoint.
   *
   * Need to include this for symmetry with 'Authenticate' only.
   */
  public static class Authorize extends SSL{}
}
