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
package examples;

import java.util.Scanner;

import org.scribe.builder.*;
import org.scribe.builder.api.*;
import org.scribe.model.*;
import org.scribe.oauth.*;

public class TumblrExample
{
    private static final String PROTECTED_RESOURCE_URL = "http://api.tumblr.com/v2/user/info";

    public static void main( String[] args )
    {
        OAuthService service = new ServiceBuilder()
                .provider( TumblrApi.class )
                .apiKey( "VdE7COcqx3c8qiXg45AowMPausYVvYDpBSIrx1jI6gPj1X5V8T" )
                .apiSecret( "SILFKJbnYTuFLWVhCnKiD1wpd4T6ahVC0HXTemu1AqT3X7iL0r" )
                .callback( "http://222.122.63.146/community/tumblr-callback.do" ) // OOB forbidden. We need an url and the better is on the tumblr website !
                .debug()
                .build();
        Scanner in = new Scanner( System.in );

        System.out.println( "=== Tumblr's OAuth Workflow ===" );
        System.out.println();

        // Obtain the Request Token
        System.out.println( "Fetching the Request Token..." );
        Token requestToken = service.getRequestToken();
        System.out.println( "Got the Request Token!" );
        System.out.println();

        System.out.println( "Now go and authorize Scribe here:" );
        System.out.println( service.getAuthorizationUrl( requestToken ) );
        System.out.println( "And paste the verifier here" );
        System.out.print( ">>" );
       Verifier verifier = new Verifier( in.nextLine() );
        System.out.println();

        // Trade the Request Token and Verfier for the Access Token
        System.out.println( "Trading the Request Token for an Access Token..." );
        
       // Token accessToken = service.getAccessToken( requestToken , verifier );
        Token accessToken = new Token("3wBnpezR7CGsmBxD7Mk5clu728RMVibEe1KsP9r1BDA9RUcLUB", "bi1AqWaLwfDXj2as6DrNgL43NocpOxFzlphxwJPwyljTVYNjQ5");
        
       //Token accessToken = new Token("rJiDUkj4GEz1ZaBsWWXBo411XYBuAcutUwwoeRmkDzHF9wjf1s", "54VxMoFQJvlcrUNXsGn6N5muX8wzX001Gc7Wfmd7OOxY7wwZ5t", "oauth_token=rJiDUkj4GEz1ZaBsWWXBo411XYBuAcutUwwoeRmkDzHF9wjf1s&oauth_token_secret=54VxMoFQJvlcrUNXsGn6N5muX8wzX001Gc7Wfmd7OOxY7wwZ5t" );
        		
        System.out.println( "accessSecret : "+ accessToken.getSecret() );
        System.out.println( "accessToken : "+ accessToken.getToken() );
       // System.out.println( "row : "+ accessToken.getRawResponse() ); 
        
        
        System.out.println( "requestToken : "+ requestToken.getToken() );
        System.out.println( "requestSecret : "+ requestToken.getSecret() );
        
        
        System.out.println( "Got the Access Token!" );
        System.out.println( "(if your curious it looks like this: " + accessToken + " )" );
        System.out.println();

        // Now let's go and ask for a protected resource!
        System.out.println( "Now we're going to access a protected resource..." );
        OAuthRequest request = new OAuthRequest( Verb.GET , PROTECTED_RESOURCE_URL );
        service.signRequest( accessToken , request );
        Response response = request.send();
        System.out.println( "Got it! Lets see what we found..." );
        System.out.println();
        System.out.println( response.getBody() );

        System.out.println();
        System.out.println( "Thats it man! Go and build something awesome with Scribe! :)" );
    }
}
