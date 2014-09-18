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
package architecture.ee.web.community.social.tumblr;

public enum PostType {
    TEXT("text"),
    PHOTO("photo"),
    QUOTE("quote"),
    LINK("link"),
    CHAT("chat"),
    AUDIO("audio"),
    VIDEO("video"),
    ANSWER("answer"), // docs only mention this for reading, not for posting/editing???
    REBLOG("reblog"); // not sure if this last one is really necessary

    private String type;

    PostType(String type) {
        this.type = type;
    }

    public String getType() {
        return this.type;
    }

    public static PostType getByType(String type) {
        PostType result = null;
        for (PostType postType : values()) {
            if (postType.getType().equals(type)) {
                result = postType;
                break;
            }
        }
        return result;
    }
}
